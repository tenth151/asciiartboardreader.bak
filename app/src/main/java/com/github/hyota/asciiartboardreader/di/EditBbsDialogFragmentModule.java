package com.github.hyota.asciiartboardreader.di;

import com.github.hyota.asciiartboardreader.presentation.bbslist.EditBbsContract;
import com.github.hyota.asciiartboardreader.presentation.bbslist.EditBbsDialogFragment;
import com.github.hyota.asciiartboardreader.presentation.bbslist.EditBbsPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class EditBbsDialogFragmentModule {

    @FragmentScope
    @Provides
    EditBbsContract.View provideView(EditBbsDialogFragment fragment) {
        return fragment;
    }

    @FragmentScope
    @Provides
    EditBbsContract.Presenter providePresenter(EditBbsPresenter presenter) {
        return presenter;
    }

}
