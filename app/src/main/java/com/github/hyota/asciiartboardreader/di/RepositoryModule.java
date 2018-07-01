package com.github.hyota.asciiartboardreader.di;

import com.github.hyota.asciiartboardreader.data.db.room.BbsInfoRepositoryImpl;
import com.github.hyota.asciiartboardreader.data.repository.BbsInfoRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    BbsInfoRepository provideBbsInfoRepository(BbsInfoRepositoryImpl repository) {
        return repository;
    }

}
