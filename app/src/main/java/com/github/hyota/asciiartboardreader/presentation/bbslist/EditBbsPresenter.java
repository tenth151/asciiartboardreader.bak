package com.github.hyota.asciiartboardreader.presentation.bbslist;

import android.support.annotation.NonNull;

import com.github.hyota.asciiartboardreader.di.FragmentScope;
import com.github.hyota.asciiartboardreader.domain.usecase.DeleteBbsUseCase;
import com.github.hyota.asciiartboardreader.domain.usecase.EditBbsUseCase;
import com.github.hyota.asciiartboardreader.domain.usecase.GetBbsTitleUseCase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

@FragmentScope
public class EditBbsPresenter implements EditBbsContract.Presenter {

    @NonNull
    private EditBbsContract.View view;
    @NonNull
    private EditBbsUseCase editBbsUseCase;
    @NonNull
    private DeleteBbsUseCase deleteBbsUseCase;
    @NonNull
    private GetBbsTitleUseCase getBbsTitleUseCase;

    @Inject
    EditBbsPresenter(@NonNull EditBbsContract.View view, @NonNull EditBbsUseCase editBbsUseCase, @NonNull DeleteBbsUseCase deleteBbsUseCase, @NonNull GetBbsTitleUseCase getBbsTitleUseCase) {
        this.view = view;
        this.editBbsUseCase = editBbsUseCase;
        this.deleteBbsUseCase = deleteBbsUseCase;
        this.getBbsTitleUseCase = getBbsTitleUseCase;
    }

    @Override
    public void onStart() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onOk(long id, long sort, @NonNull String title, @NonNull String url) {
        editBbsUseCase.execute(id, sort, title, url);
    }

    @Override
    public void onDelete(long id) {
        deleteBbsUseCase.execute(id);
    }

    @Override
    public void onGetTitle(@NonNull String url) {
        getBbsTitleUseCase.execute(url);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetBbsTitleSuccessEvent(@NonNull GetBbsTitleUseCase.SuccessEvent event) {
        view.setTitle(event.getTitle());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetBbsTitleErrorEvent(@NonNull GetBbsTitleUseCase.ErrorEvent event) {
        view.showAlertMessage(event.getMessage());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetBbsTitleProgressEvent(@NonNull GetBbsTitleUseCase.ProgressEvent event) {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEditBbsSuccessEvent(@NonNull EditBbsUseCase.SuccessEvent event) {
        if (event.isCreate()) {
            view.created(event.getBbsInfo());
        } else {
            view.edited(event.getBbsInfo());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEditBbsErrorEvent(@NonNull EditBbsUseCase.ErrorEvent event) {
        view.showAlertMessage(event.getMessage());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeleteBbsEvent(@NonNull DeleteBbsUseCase.Event event) {
        view.deleted();
    }

}
