package com.github.hyota.asciiartboardreader.di;

import com.github.hyota.asciiartboardreader.presentation.threadresponselist.ThreadResponseListContract;
import com.github.hyota.asciiartboardreader.presentation.threadresponselist.ThreadResponseListFragment;
import com.github.hyota.asciiartboardreader.presentation.threadresponselist.ThreadResponseListPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class ThreadResponseListFragmentModule {

    @FragmentScope
    @Provides
    ThreadResponseListContract.View provideView(ThreadResponseListFragment fragment) {
        return fragment;
    }

    @FragmentScope
    @Provides
    ThreadResponseListContract.Presenter providePresenter(ThreadResponseListPresenter presenter) {
        return presenter;
    }

}
