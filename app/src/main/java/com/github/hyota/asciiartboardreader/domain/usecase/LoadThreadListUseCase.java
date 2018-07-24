package com.github.hyota.asciiartboardreader.domain.usecase;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.github.hyota.asciiartboardreader.data.repository.FavoriteThreadRepository;
import com.github.hyota.asciiartboardreader.data.repository.HistoryRepository;
import com.github.hyota.asciiartboardreader.data.repository.SubjectRepository;
import com.github.hyota.asciiartboardreader.domain.model.BbsInfo;
import com.github.hyota.asciiartboardreader.domain.model.NetworkException;
import com.github.hyota.asciiartboardreader.domain.model.ThreadInfo;
import com.github.hyota.asciiartboardreader.domain.model.ThreadSubject;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class LoadThreadListUseCase {

    @NonNull
    private SubjectRepository subjectRepository;
    @NonNull
    private FavoriteThreadRepository favoriteThreadRepository;
    @NonNull
    private HistoryRepository historyRepository;

    @Inject
    LoadThreadListUseCase(@NonNull SubjectRepository subjectRepository, @NonNull FavoriteThreadRepository favoriteThreadRepository, @NonNull HistoryRepository historyRepository) {
        this.subjectRepository = subjectRepository;
        this.favoriteThreadRepository = favoriteThreadRepository;
        this.historyRepository = historyRepository;
    }

    @SuppressLint("CheckResult")
    public void execute(@NonNull BbsInfo bbsInfo) {
        subjectRepository.load(bbsInfo)
                .subscribeOn(Schedulers.newThread())
                .map(threadSubjectList -> {
                    int index = 1;
                    List<ThreadInfo> threadInfoList = new ArrayList<>();
                    for (ThreadSubject threadSubject : threadSubjectList) {
                        threadInfoList.add(new ThreadInfo(threadSubject, bbsInfo, index++));
                    }
                    return threadInfoList;
                })
                .map(threadInfoList -> { // お気に入りマージ
                    List<ThreadInfo> ret = new ArrayList<>(threadInfoList);
                    Map<Long, ThreadInfo> unixTimeMap = Stream.of(threadInfoList).collect(Collectors.toMap(ThreadInfo::getUnixTime, threadInfo -> threadInfo));
                    favoriteThreadRepository.findByBbs(bbsInfo)
                            .subscribe(favoriteThreadList -> {
                                for (ThreadInfo favoriteThread : favoriteThreadList) {
                                    ThreadInfo threadInfo = unixTimeMap.get(favoriteThread.getUnixTime());
                                    if (threadInfo != null) {
                                        threadInfo.setFavorite(true);
                                    } else {
                                        ret.add(favoriteThread);
                                    }
                                }
                            });
                    return ret;
                })
                .map(threadInfoList -> { // 履歴マージ
                    List<ThreadInfo> ret = new ArrayList<>(threadInfoList);
                    Map<Long, ThreadInfo> unixTimeMap = Stream.of(threadInfoList).collect(Collectors.toMap(ThreadInfo::getUnixTime, threadInfo -> threadInfo));
                    historyRepository.findByBbs(bbsInfo)
                            .subscribe(historyThreadList -> {
                                for (ThreadInfo historyThread : historyThreadList) {
                                    ThreadInfo threadInfo = unixTimeMap.get(historyThread.getUnixTime());
                                    if (threadInfo != null) {
                                        threadInfo.setLastUpdate(Objects.requireNonNull(historyThread.getLastUpdate()));
                                        threadInfo.setReadCount(Objects.requireNonNull(historyThread.getReadCount()));
                                        threadInfo.setLastWrite(historyThread.getLastWrite());
                                    } else {
                                        ret.add(historyThread);
                                    }
                                }
                            });
                    return ret;
                })
                .doOnSuccess(threadInfoList -> Collections.sort(threadInfoList, (t1, t2) -> {
                    if (t1.getNo() != null && t2.getNo() != null) {
                        return t1.getNo().compareTo(t2.getNo());
                    } else if (t1.getNo() != null && t2.getNo() == null) {
                        return 1;
                    } else if (t1.getNo() == null && t2.getNo() != null) {
                        return -1;
                    } else {
                        return t1.getSince().compareTo(t2.getSince());
                    }
                }))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        threadInfoList -> EventBus.getDefault().post(new SuccessEvent(threadInfoList)),
                        throwable -> {
                            Timber.d(throwable);
                            if (throwable instanceof NetworkException) {
                                NetworkException networkException = (NetworkException) throwable;
                                EventBus.getDefault().post(new ErrorEvent("statusCode = " + networkException.getResponseCode() + ", " + "message = " + networkException.getMessage()));
                            } else {
                                EventBus.getDefault().post(new ErrorEvent("スレッドを取得できませんでした"));
                            }
                        });
    }

    public static class SuccessEvent {
        @NonNull
        private List<ThreadInfo> threadInfoList;

        private SuccessEvent(@NonNull List<ThreadInfo> threadInfoList) {
            this.threadInfoList = threadInfoList;
        }

        @NonNull
        public List<ThreadInfo> getThreadInfoList() {
            return threadInfoList;
        }
    }

    public static class ErrorEvent {
        @NonNull
        private String messge;

        private ErrorEvent(@NonNull String messge) {
            this.messge = messge;
        }

        @NonNull
        public String getMessge() {
            return messge;
        }
    }

}
