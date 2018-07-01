package com.github.hyota.asciiartboardreader.presentation.boardlist;

import android.support.annotation.NonNull;

import com.github.hyota.asciiartboardreader.presentation.BbsListContract;

public class BbsListPresenter implements BbsListContract.Presenter {

    @NonNull
    private BbsListContract.View view;

    public BbsListPresenter(@NonNull BbsListContract.View view) {
        this.view = view;
    }
}
