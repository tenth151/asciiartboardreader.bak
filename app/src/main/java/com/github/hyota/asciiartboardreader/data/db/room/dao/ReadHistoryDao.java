package com.github.hyota.asciiartboardreader.data.db.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.github.hyota.asciiartboardreader.data.db.room.entity.ReadHistoryEntity;

import java.util.List;

@Dao
public interface ReadHistoryDao {

    @Query("SELECT * FROM read_history WHERE bbsId = :bbsId")
    List<ReadHistoryEntity> findByBbsId(long bbsId);

    @Query("SELECT * FROM read_history WHERE unixTime = :unixTime AND bbsId = :bbsId")
    ReadHistoryEntity findByUnixTimeAndBbsId(long unixTime, long bbsId);

    @Insert
    long insert(ReadHistoryEntity entity);

    @Query("DELETE FROM read_history WHERE id = :id")
    void delete(long id);

}
