package com.github.hyota.asciiartboardreader.di;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.github.hyota.asciiartboardreader.BuildConfig;
import com.github.hyota.asciiartboardreader.data.db.room.AppDataBase;
import com.github.hyota.asciiartboardreader.data.db.room.dao.BbsInfoDao;
import com.github.hyota.asciiartboardreader.data.db.room.dao.FavoriteThreadDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DataBaseModule {

    @Provides
    @Singleton
    AppDataBase provideDatabase(Context context) {
        RoomDatabase.Builder<AppDataBase> builder = Room.databaseBuilder(context, AppDataBase.class, "app-database");
        if (BuildConfig.DEBUG) {
            builder = builder.allowMainThreadQueries();
        }
        return builder.build();
    }

    @Provides
    @Singleton
    BbsInfoDao provideBbsInfoDao(AppDataBase database) {
        return database.bbsInfoDao();
    }

    @Provides
    @Singleton
    FavoriteThreadDao provideFavoriteThreadDao(AppDataBase dataBase) {
        return dataBase.favoriteThreadDao();
    }

}
