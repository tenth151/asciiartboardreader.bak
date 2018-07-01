package com.github.hyota.asciiartboardreader.di;

import com.github.hyota.asciiartboardreader.presentation.bbslist.EditBbsDialogFragment;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent
public interface EditBbsDialogFragmentSubcomponent extends AndroidInjector<EditBbsDialogFragment> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<EditBbsDialogFragment> {
    }

}
