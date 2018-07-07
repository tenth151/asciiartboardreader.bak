package com.github.hyota.asciiartboardreader.di;

import com.github.hyota.asciiartboardreader.data.repository.DatRepository;
import com.github.hyota.asciiartboardreader.presentation.thread.ThreadResponseListContract;
import com.github.hyota.asciiartboardreader.presentation.thread.ThreadResponseListFragment;
import com.github.hyota.asciiartboardreader.presentation.thread.ThreadResponseListPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class ThreadResponseListFragmentModule {

    @FragmentScope
    @Provides
    ThreadResponseListContract.Presenter providePresenter(ThreadResponseListFragment fragment, DatRepository datRepository) {
        return new ThreadResponseListPresenter(fragment, datRepository);
    }

}
