package com.github.hyota.asciiartboardreader.di;

import com.github.hyota.asciiartboardreader.presentation.boardlist.BbsListFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module(subcomponents = MainActivitySubcomponent.class)
public abstract class MainActivityModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = BbsListFragmentModule.class)
    abstract BbsListFragment contributeBoardListFragment();

}
