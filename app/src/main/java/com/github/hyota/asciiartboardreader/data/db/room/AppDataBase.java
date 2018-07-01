package com.github.hyota.asciiartboardreader.data.db.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.github.hyota.asciiartboardreader.data.db.room.dao.BbsInfoDao;
import com.github.hyota.asciiartboardreader.data.db.room.entity.BbsInfoEntity;

@Database(
        entities = {
                BbsInfoEntity.class,
        },
        version = 1)
public abstract class AppDataBase extends RoomDatabase {

    public abstract BbsInfoDao bbsInfoDao();

}
