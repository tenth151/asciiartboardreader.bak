package com.github.hyota.asciiartboardreader.di;

import com.github.hyota.asciiartboardreader.presentation.boardlist.BbsListFragment;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent
public interface BbsListFragmentSubcomponent extends AndroidInjector<BbsListFragment> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<BbsListFragment> {
    }

}
