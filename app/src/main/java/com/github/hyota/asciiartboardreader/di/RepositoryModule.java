package com.github.hyota.asciiartboardreader.di;

import com.github.hyota.asciiartboardreader.data.db.room.BbsInfoRepositoryImpl;
import com.github.hyota.asciiartboardreader.data.db.room.FavoriteThreadRepositoryImpl;
import com.github.hyota.asciiartboardreader.data.db.room.ReadHistoryRepositoryImpl;
import com.github.hyota.asciiartboardreader.data.local.DatLocalRepository;
import com.github.hyota.asciiartboardreader.data.local.SettingLocalRepository;
import com.github.hyota.asciiartboardreader.data.local.SubjectLocalRepository;
import com.github.hyota.asciiartboardreader.data.network.retrofit.DatNetworkRepository;
import com.github.hyota.asciiartboardreader.data.network.retrofit.SettingNetworkRepository;
import com.github.hyota.asciiartboardreader.data.network.retrofit.ShitarabaService;
import com.github.hyota.asciiartboardreader.data.network.retrofit.SubjectNetworkRepository;
import com.github.hyota.asciiartboardreader.data.repository.BbsInfoRepository;
import com.github.hyota.asciiartboardreader.data.repository.DatRepository;
import com.github.hyota.asciiartboardreader.data.repository.DatRepositoryImpl;
import com.github.hyota.asciiartboardreader.data.repository.FavoriteThreadRepository;
import com.github.hyota.asciiartboardreader.data.repository.ReadHistoryRepository;
import com.github.hyota.asciiartboardreader.data.repository.SettingRepository;
import com.github.hyota.asciiartboardreader.data.repository.SettingRepositoryImpl;
import com.github.hyota.asciiartboardreader.data.repository.SubjectRepository;
import com.github.hyota.asciiartboardreader.data.repository.SubjectRepositoryImpl;

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
    SettingRepository provideSettingRepository(SettingLocalRepository settingLocalRepository, ShitarabaService shitarabaService) {
        return new SettingRepositoryImpl(settingLocalRepository, new SettingNetworkRepository(shitarabaService, settingLocalRepository));
    }

    @Provides
    @Singleton
    SubjectRepository provideSubjectRepository(SubjectLocalRepository subjectLocalRepository, ShitarabaService shitarabaService) {
        return new SubjectRepositoryImpl(subjectLocalRepository, new SubjectNetworkRepository(shitarabaService, subjectLocalRepository));
    }

    @Provides
    @Singleton
    FavoriteThreadRepository provideFavoriteThreadRepository(FavoriteThreadRepositoryImpl repository) {
        return repository;
    }

    @Provides
    @Singleton
    DatRepository provideDatRepository(DatLocalRepository datLocalRepository, ShitarabaService shitarabaService) {
        return new DatRepositoryImpl(datLocalRepository, new DatNetworkRepository(shitarabaService, datLocalRepository));
    }

    @Provides
    @Singleton
    ReadHistoryRepository provideReadHistoryRepository(ReadHistoryRepositoryImpl repository) {
        return repository;
    }

}
