package com.github.hyota.asciiartboardreader.presentation.threadlist;

import android.support.annotation.NonNull;

import com.annimon.stream.Stream;
import com.github.hyota.asciiartboardreader.di.FragmentScope;
import com.github.hyota.asciiartboardreader.domain.model.BbsInfo;
import com.github.hyota.asciiartboardreader.domain.model.ThreadInfo;
import com.github.hyota.asciiartboardreader.domain.usecase.ChangeFavoriteStateUseCase;
import com.github.hyota.asciiartboardreader.domain.usecase.LoadThreadListUseCase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

@FragmentScope
public class ThreadListPresenter implements ThreadListContract.Presenter {

    @NonNull
    private ThreadListContract.View view;
    @NonNull
    private LoadThreadListUseCase loadThreadListUseCase;
    @NonNull
    private ChangeFavoriteStateUseCase changeFavoriteStateUseCase;

    private BbsInfo bbsInfo;
    private List<ThreadInfo> items;

    @Inject
    ThreadListPresenter(@NonNull ThreadListContract.View view, @NonNull LoadThreadListUseCase loadThreadListUseCase, @NonNull ChangeFavoriteStateUseCase changeFavoriteStateUseCase) {
        this.view = view;
        this.loadThreadListUseCase = loadThreadListUseCase;
        this.changeFavoriteStateUseCase = changeFavoriteStateUseCase;
    }

    @Override
    public void onCreate(@NonNull BbsInfo bbsInfo) {
        this.bbsInfo = bbsInfo;
    }

    @Override
    public void onStart() {
        EventBus.getDefault().register(this);
        loadThreadListUseCase.execute(bbsInfo);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onFavoriteStateChange(@NonNull ThreadInfo threadInfo) {
        if (!threadInfo.isFavorite()) {
            changeFavoriteStateUseCase.execute(threadInfo);
        } else {
            view.showConfirmDeleteFavoriteThread(threadInfo);
        }
    }

    @Override
    public void onDeleteFavoriteThread(@NonNull ThreadInfo threadInfo) {
        changeFavoriteStateUseCase.execute(threadInfo);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoadThreadListSuccessEvent(@NonNull LoadThreadListUseCase.SuccessEvent event) {
        items = event.getThreadInfoList();
        view.setData(items);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoadThreadListErrorEvent(@NonNull LoadThreadListUseCase.ErrorEvent event) {
        view.showAlertMessage(event.getMessage());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangeFavoriteStateEvent(@NonNull ChangeFavoriteStateUseCase.Event event) {
        ThreadInfo item = Stream.of(items).filter(it -> it.getUnixTime() == event.getInfo().getUnixTime()).findFirst().orElse(null);
        if (item != null) {
            int index = items.indexOf(item);
            items.set(index, event.getInfo());
            view.notifyItemChanged(index);
        } else {
            Timber.w("change favorite target %s is not found", event.getInfo().getTitle());
        }
    }

}
