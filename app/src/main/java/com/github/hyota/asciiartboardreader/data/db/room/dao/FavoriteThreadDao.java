package com.github.hyota.asciiartboardreader.data.db.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.github.hyota.asciiartboardreader.data.db.room.entity.FavoriteThreadEntity;

import java.util.List;

@Dao
public interface FavoriteThreadDao {

    @Query("SELECT * FROM favorite_thread_info WHERE bbsId = :bbsId")
    List<FavoriteThreadEntity> findByBbsId(long bbsId);

    @Query("SELECT * FROM favorite_thread_info WHERE unixTime = :unixTime AND bbsId = :bbsId")
    FavoriteThreadEntity findByUnixTimeAndBbsId(long unixTime, long bbsId);

    @Insert
    void insert(FavoriteThreadEntity entity);

    @Delete
    void delete(FavoriteThreadEntity entity);

    @Query("DELETE FROM favorite_thread_info WHERE unixTime = :unixTime AND bbsId = :bbsId")
    void deleteByUnixTimeAndBbsId(long unixTime, long bbsId);

}
