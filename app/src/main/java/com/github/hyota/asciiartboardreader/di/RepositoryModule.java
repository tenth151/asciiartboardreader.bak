package com.github.hyota.asciiartboardreader.di;

import com.github.hyota.asciiartboardreader.data.db.room.BbsInfoRepositoryImpl;
import com.github.hyota.asciiartboardreader.data.local.DatLocalFileDataSource;
import com.github.hyota.asciiartboardreader.data.network.retrofit.DatNetworkRepository;
import com.github.hyota.asciiartboardreader.data.network.retrofit.ShitarabaService;
import com.github.hyota.asciiartboardreader.data.repository.BbsInfoRepository;
import com.github.hyota.asciiartboardreader.data.repository.DatRepository;
import com.github.hyota.asciiartboardreader.data.repository.DatRepositoryImpl;

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

    @Provides
    @Singleton
    DatRepository provideDatRepository(DatLocalFileDataSource datLocalRepository, ShitarabaService shitarabaService) {
        return new DatRepositoryImpl(datLocalRepository, new DatNetworkRepository(shitarabaService, datLocalRepository));
    }

}
