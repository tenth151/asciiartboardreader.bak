package com.github.hyota.asciiartboardreader.presentation.threadlist;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.github.hyota.asciiartboardreader.data.repository.SubjectRepository;
import com.github.hyota.asciiartboardreader.domain.model.BbsInfo;
import com.github.hyota.asciiartboardreader.domain.model.ThreadInfo;
import com.github.hyota.asciiartboardreader.domain.model.ThreadSubject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ThreadListPresenter implements ThreadListContract.Presenter {

    @NonNull
    private ThreadListContract.View view;
    @NonNull
    private SubjectRepository subjectRepository;

    private BbsInfo bbsInfo;

    public ThreadListPresenter(@NonNull ThreadListContract.View view, @NonNull SubjectRepository subjectRepository) {
        this.view = view;
        this.subjectRepository = subjectRepository;
    }

    @SuppressLint("CheckResult")
    @Override
    public void onCreate(@NonNull BbsInfo bbsInfo) {
        this.bbsInfo = bbsInfo;
        // TODO 履歴情報、お気に入り情報とのマージ予定
        subjectRepository.findByUrl(bbsInfo.getScheme(), bbsInfo.getHost(), bbsInfo.getCategory(), bbsInfo.getDirectory())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subject -> {
                    int index = 1;
                    List<ThreadInfo> threadInfoList = new ArrayList<>();
                    for (ThreadSubject threadSubject : subject.getThreadSubjectList()) {
                        threadInfoList.add(new ThreadInfo(threadSubject.getUnixTime(), threadSubject.getTitle(), threadSubject.getCount(), bbsInfo, index++));
                    }
                    view.setData(threadInfoList);
                });
    }
}
