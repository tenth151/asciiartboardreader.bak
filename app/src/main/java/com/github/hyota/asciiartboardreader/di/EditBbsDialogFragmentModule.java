package com.github.hyota.asciiartboardreader.di;

import com.github.hyota.asciiartboardreader.domain.usecase.DeleteBbsUseCase;
import com.github.hyota.asciiartboardreader.domain.usecase.EditBbsUseCase;
import com.github.hyota.asciiartboardreader.domain.usecase.GetBbsTitleUseCase;
import com.github.hyota.asciiartboardreader.presentation.bbslist.EditBbsContract;
import com.github.hyota.asciiartboardreader.presentation.bbslist.EditBbsDialogFragment;
import com.github.hyota.asciiartboardreader.presentation.bbslist.EditBbsPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class EditBbsDialogFragmentModule {

    @FragmentScope
    @Provides
    EditBbsContract.Presenter providePresenter(EditBbsDialogFragment fragment, EditBbsUseCase editBbsUseCase, DeleteBbsUseCase deleteBbsUseCase, GetBbsTitleUseCase getBbsTitleUseCase) {
        return new EditBbsPresenter(fragment, editBbsUseCase, deleteBbsUseCase, getBbsTitleUseCase);
    }

}
