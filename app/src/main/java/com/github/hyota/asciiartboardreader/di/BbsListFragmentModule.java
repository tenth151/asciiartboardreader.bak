package com.github.hyota.asciiartboardreader.di;

import android.support.annotation.NonNull;

import com.github.hyota.asciiartboardreader.data.repository.BbsInfoRepository;
import com.github.hyota.asciiartboardreader.presentation.bbslist.BbsListContract;
import com.github.hyota.asciiartboardreader.presentation.bbslist.BbsListFragment;
import com.github.hyota.asciiartboardreader.presentation.bbslist.BbsListPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class BbsListFragmentModule {

    @FragmentScope
    @Provides
    BbsListContract.Presenter providePresenter(@NonNull BbsListFragment fragment, @NonNull BbsInfoRepository bbsInfoRepository) {
        return new BbsListPresenter(fragment, bbsInfoRepository);
    }

}
