package com.github.hyota.asciiartboardreader.di;

import com.github.hyota.asciiartboardreader.presentation.bbslist.BbsListFragment;
import com.github.hyota.asciiartboardreader.presentation.bbslist.EditBbsDialogFragment;
import com.github.hyota.asciiartboardreader.presentation.main.MainActivity;
import com.github.hyota.asciiartboardreader.presentation.main.MainContract;
import com.github.hyota.asciiartboardreader.presentation.main.MainPresenter;
import com.github.hyota.asciiartboardreader.presentation.threadlist.ThreadListFragment;
import com.github.hyota.asciiartboardreader.presentation.threadresponselist.ThreadResponseListFragment;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainActivityModule {

    @ActivityScope
    @Provides
    static MainContract.View provideView(MainActivity activity) {
        return activity;
    }

    @ActivityScope
    @Provides
    static MainContract.Presenter providePresenter(MainPresenter presenter) {
        return presenter;
    }

    @FragmentScope
    @ContributesAndroidInjector(modules = BbsListFragmentModule.class)
    abstract BbsListFragment contributeBoardListFragment();

    @FragmentScope
    @ContributesAndroidInjector(modules = EditBbsDialogFragmentModule.class)
    abstract EditBbsDialogFragment contributeEditBbsDialogFragment();

    @FragmentScope
    @ContributesAndroidInjector(modules = ThreadListFragmentModule.class)
    abstract ThreadListFragment contributeThreadListFragment();

    @FragmentScope
    @ContributesAndroidInjector(modules = ThreadResponseListFragmentModule.class)
    abstract ThreadResponseListFragment contributeThreadResponseListFragment();

}
