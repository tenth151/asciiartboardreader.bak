package com.github.hyota.asciiartboardreader.di;

import com.github.hyota.asciiartboardreader.data.datasource.FavoriteThreadDataSource;
import com.github.hyota.asciiartboardreader.data.datasource.HistoryDataSource;
import com.github.hyota.asciiartboardreader.data.datasource.SettingLocalDataSource;
import com.github.hyota.asciiartboardreader.data.datasource.SettingRemoteDataSource;
import com.github.hyota.asciiartboardreader.data.datasource.SubjectLocalDataSource;
import com.github.hyota.asciiartboardreader.data.datasource.SubjectRemoteDataSource;
import com.github.hyota.asciiartboardreader.data.db.room.FavoriteThreadRoomDataSource;
import com.github.hyota.asciiartboardreader.data.db.room.HistoryRoomDataSource;
import com.github.hyota.asciiartboardreader.data.local.SettingLocalFileDataSource;
import com.github.hyota.asciiartboardreader.data.local.SubjectLocalFileDataSource;
import com.github.hyota.asciiartboardreader.data.network.retrofit.SettingNetworkDataSource;
import com.github.hyota.asciiartboardreader.data.network.retrofit.SubjectNetworkDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DataSourceModule {

    @Provides
    @Singleton
    SettingLocalDataSource provideSettingLocalDataSource(SettingLocalFileDataSource dataSource) {
        return dataSource;
    }

    @Provides
    @Singleton
    SettingRemoteDataSource provideSettingRemoteDataSource(SettingNetworkDataSource dataSource) {
        return dataSource;
    }

    @Provides
    @Singleton
    SubjectLocalDataSource provideSubjectLocalDataSource(SubjectLocalFileDataSource dataSource) {
        return dataSource;
    }

    @Provides
    @Singleton
    SubjectRemoteDataSource provideSubjectRemoteDataSource(SubjectNetworkDataSource dataSource) {
        return dataSource;
    }

    @Provides
    @Singleton
    FavoriteThreadDataSource provideFavoriteThreadDataSource(FavoriteThreadRoomDataSource dataSource) {
        return dataSource;
    }

    @Provides
    @Singleton
    HistoryDataSource provideHistoryDataSource(HistoryRoomDataSource dataSource) {
        return dataSource;
    }

}
