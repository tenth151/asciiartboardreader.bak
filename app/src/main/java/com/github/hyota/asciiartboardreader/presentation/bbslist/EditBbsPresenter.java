package com.github.hyota.asciiartboardreader.presentation.bbslist;

import android.support.annotation.NonNull;

import com.github.hyota.asciiartboardreader.di.FragmentScope;
import com.github.hyota.asciiartboardreader.domain.usecase.DeleteBbsUseCase;
import com.github.hyota.asciiartboardreader.domain.usecase.EditBbsUseCase;
import com.github.hyota.asciiartboardreader.domain.usecase.GetBbsTitleUseCase;

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

    public EditBbsPresenter(@NonNull EditBbsContract.View view, @NonNull EditBbsUseCase editBbsUseCase, @NonNull DeleteBbsUseCase deleteBbsUseCase, @NonNull GetBbsTitleUseCase getBbsTitleUseCase) {
        this.view = view;
        this.editBbsUseCase = editBbsUseCase;
        this.deleteBbsUseCase = deleteBbsUseCase;
        this.getBbsTitleUseCase = getBbsTitleUseCase;
    }

    @Override
    public void onOk(long id, long sort, @NonNull String title, @NonNull String url) {
        editBbsUseCase.execute(id, sort, title, url, (bbsInfo, create) -> {
            if (create) {
                view.created(bbsInfo);
            } else {
                view.edited(bbsInfo);
            }
        }, message -> view.showAlertMessage(message));
    }

    @Override
    public void onDelete(long id) {
        deleteBbsUseCase.execute(id, view::deleted);
    }

    @Override
    public void onGetTitle(@NonNull String url) {
        getBbsTitleUseCase.execute(url, title -> view.setTitle(title), message -> view.showAlertMessage(message));
    }
}
