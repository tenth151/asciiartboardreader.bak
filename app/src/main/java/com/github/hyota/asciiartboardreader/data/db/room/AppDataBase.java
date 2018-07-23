package com.github.hyota.asciiartboardreader.data.db.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.github.hyota.asciiartboardreader.data.db.room.dao.BbsInfoDao;
import com.github.hyota.asciiartboardreader.data.db.room.dao.FavoriteThreadDao;
import com.github.hyota.asciiartboardreader.data.db.room.dao.HistoryDao;
import com.github.hyota.asciiartboardreader.data.db.room.entity.BbsInfoEntity;
import com.github.hyota.asciiartboardreader.data.db.room.entity.FavoriteThreadEntity;
import com.github.hyota.asciiartboardreader.data.db.room.entity.HistoryEntity;

@Database(
        entities = {
                BbsInfoEntity.class,
                FavoriteThreadEntity.class,
                HistoryEntity.class,
        },
        version = 1)
public abstract class AppDataBase extends RoomDatabase {

    public abstract BbsInfoDao bbsInfoDao();

    public abstract FavoriteThreadDao favoriteThreadDao();

    public abstract HistoryDao readHistoryDao();

}
