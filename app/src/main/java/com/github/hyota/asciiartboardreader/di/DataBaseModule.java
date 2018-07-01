package com.github.hyota.asciiartboardreader.di;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;

import com.github.hyota.asciiartboardreader.data.db.room.AppDataBase;
import com.github.hyota.asciiartboardreader.data.db.room.dao.BbsInfoDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DataBaseModule {

    @Provides
    @Singleton
    AppDataBase provideDatabase(@NonNull Context context) {
        return Room.databaseBuilder(context, AppDataBase.class, "app-database").build();
    }

    @Provides
    @Singleton
    BbsInfoDao provideBbsInfoDao(@NonNull AppDataBase database) {
        return database.bbsInfoDao();
    }

}
