package com.github.hyota.asciiartboardreader.presentation.threadlist;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.github.hyota.asciiartboardreader.data.repository.FavoriteThreadRepository;
import com.github.hyota.asciiartboardreader.data.repository.HistoryRepository;
import com.github.hyota.asciiartboardreader.data.repository.SubjectRepository;
import com.github.hyota.asciiartboardreader.domain.model.BbsInfo;
import com.github.hyota.asciiartboardreader.domain.model.ThreadInfo;
import com.github.hyota.asciiartboardreader.domain.model.ThreadSubject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class ThreadListPresenter implements ThreadListContract.Presenter {

    @NonNull
    private ThreadListContract.View view;
    @NonNull
    private SubjectRepository subjectRepository;
    @NonNull
    private FavoriteThreadRepository favoriteThreadRepository;
    @NonNull
    private HistoryRepository historyRepository;

    private List<ThreadInfo> items;

    public ThreadListPresenter(@NonNull ThreadListContract.View view, @NonNull SubjectRepository subjectRepository, @NonNull FavoriteThreadRepository favoriteThreadRepository) {
        this.view = view;
        this.subjectRepository = subjectRepository;
        this.favoriteThreadRepository = favoriteThreadRepository;
    }

    @SuppressLint("CheckResult")
    @Override
    public void onCreate(@NonNull BbsInfo bbsInfo) {
        // TODO 履歴情報、お気に入り情報とのマージ予定
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
                .subscribe(threadInfoList -> {
                    items = threadInfoList;
                    view.setData(items);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void onFavoriteStateChange(@NonNull ThreadInfo threadInfo, boolean favorite) {
        if (favorite) {
            ThreadInfo favoriteThread = Stream.of(items).filter(it -> it.getUnixTime() == threadInfo.getUnixTime()).findFirst().orElse(null);
            if (favoriteThread != null) {
                favoriteThreadRepository.save(favoriteThread)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(info -> {
                            int index = items.indexOf(favoriteThread);
                            items.set(index, info);
                            view.notifyItemChanged(index);
                        });
            } else {
                Timber.w("favorite target %s is not found", threadInfo.getTitle());
            }
        } else {
            view.showConfirmDeleteFavoriteThread(threadInfo);
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void onDeleteFavoriteThread(@NonNull ThreadInfo threadInfo) {
        ThreadInfo favoriteThread = Stream.of(items).filter(it -> it.getUnixTime() == threadInfo.getUnixTime()).findFirst().orElse(null);
        if (favoriteThread != null) {
            // TODO タグ関連付け削除
            favoriteThreadRepository.delete(favoriteThread)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        favoriteThread.setFavorite(false);
                        view.notifyItemChanged(items.indexOf(favoriteThread));
                    });
        } else {
            Timber.w("delete target %s is not found", threadInfo.getTitle());
        }
    }

}
