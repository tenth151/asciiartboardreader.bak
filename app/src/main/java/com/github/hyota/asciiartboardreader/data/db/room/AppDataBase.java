package com.github.hyota.asciiartboardreader.data.db.room;

import android.arch.persistence.room.RoomDatabase;

import com.github.hyota.asciiartboardreader.data.db.room.dao.BbsInfoDao;

public abstract class AppDataBase extends RoomDatabase {

    public abstract BbsInfoDao bbsInfoDao();

}
