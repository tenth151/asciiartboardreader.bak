package com.github.hyota.asciiartboardreader.presentation.main;

import android.support.annotation.NonNull;

import com.github.hyota.asciiartboardreader.di.ActivityScope;

import javax.inject.Inject;

@ActivityScope
public class MainPresenter implements MainContract.Presenter {

    @NonNull
    private MainContract.View view;

    @Inject
    MainPresenter(@NonNull MainContract.View view) {
        this.view = view;
    }
}
