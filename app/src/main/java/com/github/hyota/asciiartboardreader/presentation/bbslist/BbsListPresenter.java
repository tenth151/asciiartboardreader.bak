package com.github.hyota.asciiartboardreader.presentation.bbslist;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.github.hyota.asciiartboardreader.di.FragmentScope;
import com.github.hyota.asciiartboardreader.domain.model.BbsInfo;
import com.github.hyota.asciiartboardreader.domain.usecase.LoadBbsListUseCase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

@FragmentScope
public class BbsListPresenter implements BbsListContract.Presenter {

    @NonNull
    private BbsListContract.View view;
    @NonNull
    private LoadBbsListUseCase loadBbsListUseCase;

    private List<BbsInfo> items;

    @Inject
    BbsListPresenter(@NonNull BbsListContract.View view, @NonNull LoadBbsListUseCase loadBbsListUseCase) {
        this.view = view;
        this.loadBbsListUseCase = loadBbsListUseCase;
    }

    @Override
    public void onStart() {
        EventBus.getDefault().register(this);
        view.setData(Collections.emptyList());
        loadBbsListUseCase.execute();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onAddButtonClick() {
        view.showAddBbsDialog();
    }

    @Override
    public void onBbsLongClick(@NonNull BbsInfo bbsInfo) {
        Uri.Builder builder = new Uri.Builder()
                .scheme(bbsInfo.getScheme())
                .authority(bbsInfo.getHost())
                .appendPath(bbsInfo.getCategory());
        if (bbsInfo.getDirectory() != null) {
            builder.appendPath(bbsInfo.getDirectory());
        }
        view.showEditBbsDialog(bbsInfo.getId(), bbsInfo.getSort(), bbsInfo.getTitle(), builder.build().toString());
    }

    @Override
    public void onCreateBbs(@NonNull BbsInfo bbsInfo) {
        items.add(bbsInfo);
        view.notifyItemInserted(items.size() - 1);
    }

    @Override
    public void onEditBbs(@NonNull BbsInfo bbsInfo) {
        int position = Stream.of(items).map(BbsInfo::getId).collect(Collectors.toList()).indexOf(bbsInfo.getId());
        if (position > -1) {
            items.set(position, bbsInfo);
            view.notifyItemChanged(position);
        } else {
            Timber.w("not found id = %s", bbsInfo.getId());
        }
    }

    @Override
    public void onDeleteBbs(long id) {
        int position = Stream.of(items).map(BbsInfo::getId).collect(Collectors.toList()).indexOf(id);
        if (position > -1) {
            items.remove(position);
            view.notifyItemChanged(position);
        } else {
            Timber.w("not found id = %s", position);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoadBbsListEvent(@NonNull LoadBbsListUseCase.Event event) {
        view.setData(event.getItems());
    }

}
