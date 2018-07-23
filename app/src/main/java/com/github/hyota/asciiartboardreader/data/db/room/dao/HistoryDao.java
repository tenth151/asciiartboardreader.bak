package com.github.hyota.asciiartboardreader.data.db.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.github.hyota.asciiartboardreader.data.db.room.entity.HistoryEntity;

import java.util.List;

@Dao
public interface HistoryDao {

    @Query("SELECT * FROM history WHERE bbsId = :bbsId")
    List<HistoryEntity> findByBbsId(long bbsId);

    @Query("SELECT * FROM history WHERE unixTime = :unixTime AND bbsId = :bbsId")
    HistoryEntity findByUnixTimeAndBbsId(long unixTime, long bbsId);

    @Insert
    void insert(HistoryEntity entity);

    @Query("UPDATE history SET readCount = :readCount  WHERE unixTime = :unixTime AND bbsId = :bbsId")
    void updateReadCount(long unixTime, long bbsId, long readCount);

    @Query("UPDATE history SET lastUpdate = :lastUpdate  WHERE unixTime = :unixTime AND bbsId = :bbsId")
    void updateLastUpdate(long unixTime, long bbsId, long lastUpdate);

    @Query("UPDATE history SET lastWrite = :lastWrite  WHERE unixTime = :unixTime AND bbsId = :bbsId")
    void updateLastWrite(long unixTime, long bbsId, long lastWrite);

    @Query("DELETE FROM history WHERE unixTime = :unixTime AND bbsId = :bbsId")
    void deleteByUnixTimeAndBbsId(long unixTime, long bbsId);

    @Query("DELETE FROM history WHERE bbsId = :bbsId")
    void deleteByBbsId(long bbsId);

}
