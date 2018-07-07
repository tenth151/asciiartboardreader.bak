package com.github.hyota.asciiartboardreader.presentation.threadresponselist;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.github.hyota.asciiartboardreader.data.repository.DatRepository;
import com.github.hyota.asciiartboardreader.domain.model.ResponseInfo;
import com.github.hyota.asciiartboardreader.domain.model.ThreadInfo;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ThreadResponseListPresenter implements ThreadResponseListContract.Presenter {

    @NonNull
    private ThreadResponseListContract.View view;
    @NonNull
    private DatRepository datRepository;

    public ThreadResponseListPresenter(@NonNull ThreadResponseListContract.View view, @NonNull DatRepository datRepository) {
        this.view = view;
        this.datRepository = datRepository;
    }

    private ThreadInfo threadInfo;
    private List<ResponseInfo> items;

    @SuppressLint("CheckResult")
    @Override
    public void onCreate(@NonNull ThreadInfo threadInfo) {
        this.threadInfo = threadInfo;
        // TODO あぼーんとのマージ
        datRepository.findByUrl(threadInfo.getBbsInfo().getScheme(), threadInfo.getBbsInfo().getHost(), threadInfo.getBbsInfo().getCategory(), threadInfo.getBbsInfo().getDirectory(), threadInfo.getUnixTime())
                .subscribeOn(Schedulers.newThread())
                .map(dat -> Stream.of(dat.getThreadResponseList())
                        .map(threadResponse -> new ResponseInfo(threadResponse.getNo(), threadResponse.getName(), threadResponse.getEmail(), threadResponse.getDateTime(), threadResponse.getContent(), threadResponse.getTitle(), threadResponse.getId(), threadInfo))
                        .collect(Collectors.toList()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseInfoList -> {
                    items = responseInfoList;
                    view.setData(items);
                });
    }
}
