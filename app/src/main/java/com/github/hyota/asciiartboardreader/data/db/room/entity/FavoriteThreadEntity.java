package com.github.hyota.asciiartboardreader.data.db.room.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "favorite_thread_info",
        indices = {
                @Index(value = {
                        "unixTime",
                        "bbsId",
                }, unique = true),
                @Index(value = {
                        "bbsId",
                }),
        },
        foreignKeys = {
                @ForeignKey(entity = BbsInfoEntity.class,
                        parentColumns = "id",
                        childColumns = "bbsId",
                        onDelete = ForeignKey.CASCADE),
        })
public class FavoriteThreadEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private long unixTime;

    private long bbsId;
    @NonNull
    private String title;

    private long count;

    public FavoriteThreadEntity(long unixTime, long bbsId, @NonNull String title, long count) {
        this.unixTime = unixTime;
        this.bbsId = bbsId;
        this.title = title;
        this.count = count;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUnixTime() {
        return unixTime;
    }

    public long getBbsId() {
        return bbsId;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public long getCount() {
        return count;
    }

}
