package com.github.hyota.asciiartboardreader.presentation.threadresponselist;

import android.support.annotation.NonNull;

import com.github.hyota.asciiartboardreader.di.FragmentScope;
import com.github.hyota.asciiartboardreader.domain.model.ResponseInfo;
import com.github.hyota.asciiartboardreader.domain.model.ThreadInfo;
import com.github.hyota.asciiartboardreader.domain.usecase.LoadDatUseCase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

@FragmentScope
public class ThreadResponseListPresenter implements ThreadResponseListContract.Presenter {

    @NonNull
    private ThreadResponseListContract.View view;
    @NonNull
    private LoadDatUseCase loadDatUseCase;

    private ThreadInfo threadInfo;
    private List<ResponseInfo> items;

    @Inject
    ThreadResponseListPresenter(@NonNull ThreadResponseListContract.View view, @NonNull LoadDatUseCase loadDatUseCase) {
        this.view = view;
        this.loadDatUseCase = loadDatUseCase;
    }

    @Override
    public void onCreate(@NonNull ThreadInfo threadInfo) {
        this.threadInfo = threadInfo;
    }

    @Override
    public void onStart() {
        EventBus.getDefault().register(this);
        loadDatUseCase.execute(threadInfo);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoadDatSuccessEvent(@NonNull LoadDatUseCase.SuccessEvent event) {
        items = event.getResponseInfoList();
        view.setData(items);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoadDatErrorEvent(@NonNull LoadDatUseCase.ErrorEvent event) {
        view.showAlertMessage(event.getMessage());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoadDatProgressEvent(@NonNull LoadDatUseCase.ProgressEvent event) {
        view.updateProgress(event.getMax(), event.getProgress());
    }

}
