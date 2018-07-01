package com.github.hyota.asciiartboardreader.di;

import android.support.annotation.NonNull;

import com.github.hyota.asciiartboardreader.presentation.bbslist.BbsListFragment;
import com.github.hyota.asciiartboardreader.presentation.bbslist.EditBbsDialogFragment;
import com.github.hyota.asciiartboardreader.presentation.main.MainActivity;
import com.github.hyota.asciiartboardreader.presentation.main.MainContract;
import com.github.hyota.asciiartboardreader.presentation.main.MainPresenter;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainActivityModule {

    @ActivityScope
    @Provides
    static MainContract.Presenter providePresenter(@NonNull MainActivity activity) {
        return new MainPresenter(activity);
    }


    @FragmentScope
    @ContributesAndroidInjector(modules = BbsListFragmentModule.class)
    abstract BbsListFragment contributeBoardListFragment();

    @FragmentScope
    @ContributesAndroidInjector(modules = EditBbsDialogFragmentModule.class)
    abstract EditBbsDialogFragment contributeEditBbsDialogFragment();

}
