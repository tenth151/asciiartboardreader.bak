package com.github.hyota.asciiartboardreader.data.db.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import com.github.hyota.asciiartboardreader.data.db.room.entity.BbsInfoEntity;

import java.util.List;

@Dao
public interface BbsInfoDao {

    @Query("SELECT * FROM bbs_info ORDER BY sort ASC")
    List<BbsInfoEntity> findAll();

    @Query("SELECT * FROM bbs_info WHERE scheme = :scheme AND host = :host AND category = :category AND directory = :directory")
    BbsInfoEntity findByUrl(String scheme, String host, String category, String directory);

    @Query("SELECT * FROM bbs_info WHERE title = :title")
    BbsInfoEntity findByTitle(String title);

    @Insert
    @Transaction
    long insert(BbsInfoEntity entity);

    @Update
    void update(BbsInfoEntity entity);

    @Query("DELETE FROM bbs_info WHERE id = :id")
    @Transaction
    void delete(long id);

    @Query("SELECT MAX(sort) FROM bbs_info")
    Long maxSort();

}
