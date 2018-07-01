package com.github.hyota.asciiartboardreader.presentation.main;

import android.support.annotation.NonNull;

public class MainPresenter implements MainContract.Presenter {

    @NonNull
    private MainContract.View view;

    public MainPresenter(@NonNull MainContract.View view) {
        this.view = view;
    }
}
