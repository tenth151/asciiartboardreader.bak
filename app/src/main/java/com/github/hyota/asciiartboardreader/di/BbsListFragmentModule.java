package com.github.hyota.asciiartboardreader.di;

import com.github.hyota.asciiartboardreader.presentation.bbslist.BbsListContract;
import com.github.hyota.asciiartboardreader.presentation.bbslist.BbsListFragment;
import com.github.hyota.asciiartboardreader.presentation.bbslist.BbsListPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class BbsListFragmentModule {

    @FragmentScope
    @Provides
    BbsListContract.View provideView(BbsListFragment fragment) {
        return fragment;
    }

    @FragmentScope
    @Provides
    BbsListContract.Presenter providePresenter(BbsListPresenter presenter) {
        return presenter;
    }

}
