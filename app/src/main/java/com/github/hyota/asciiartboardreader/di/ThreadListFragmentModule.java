package com.github.hyota.asciiartboardreader.di;

import com.github.hyota.asciiartboardreader.presentation.threadlist.ThreadListContract;
import com.github.hyota.asciiartboardreader.presentation.threadlist.ThreadListFragment;
import com.github.hyota.asciiartboardreader.presentation.threadlist.ThreadListPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class ThreadListFragmentModule {

    @FragmentScope
    @Provides
    ThreadListContract.View provideView(ThreadListFragment fragment) {
        return fragment;
    }

    @FragmentScope
    @Provides
    ThreadListContract.Presenter providePresenter(ThreadListPresenter presenter) {
        return presenter;
    }

}
