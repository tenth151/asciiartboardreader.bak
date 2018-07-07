package com.github.hyota.asciiartboardreader.di;

import com.github.hyota.asciiartboardreader.data.repository.FavoriteThreadRepository;
import com.github.hyota.asciiartboardreader.data.repository.SubjectRepository;
import com.github.hyota.asciiartboardreader.presentation.threadlist.ThreadListContract;
import com.github.hyota.asciiartboardreader.presentation.threadlist.ThreadListFragment;
import com.github.hyota.asciiartboardreader.presentation.threadlist.ThreadListPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class ThreadListFragmentModule {

    @FragmentScope
    @Provides
    ThreadListContract.Presenter providePresenter(ThreadListFragment fragment, SubjectRepository subjectRepository, FavoriteThreadRepository favoriteThreadRepository) {
        return new ThreadListPresenter(fragment, subjectRepository, favoriteThreadRepository);
    }

}
