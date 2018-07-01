package com.github.hyota.asciiartboardreader.di;

import android.support.annotation.NonNull;

import com.github.hyota.asciiartboardreader.presentation.BbsListContract;
import com.github.hyota.asciiartboardreader.presentation.boardlist.BbsListFragment;
import com.github.hyota.asciiartboardreader.presentation.boardlist.BbsListPresenter;

import dagger.Module;
import dagger.Provides;

@Module(subcomponents = BbsListFragmentSubcomponent.class)
public class BbsListFragmentModule {

    @FragmentScope
    @Provides
    BbsListContract.Presenter providePresenter(@NonNull BbsListFragment fragment) {
        return new BbsListPresenter(fragment);
    }

}
