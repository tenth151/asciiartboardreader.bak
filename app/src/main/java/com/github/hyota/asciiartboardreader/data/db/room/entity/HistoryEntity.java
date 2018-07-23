package com.github.hyota.asciiartboardreader.data.db.room.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

@Entity(tableName = "history",
        primaryKeys = {
                "unixTime",
                "bbsId",
        },
        foreignKeys = {
                @ForeignKey(entity = BbsInfoEntity.class,
                        parentColumns = "id",
                        childColumns = "bbsId",
                        onDelete = ForeignKey.CASCADE),
        },
        indices = {
                @Index(value = {
                        "bbsId",
                }, unique = true),
        }
)
public class HistoryEntity {

    private long unixTime;

    private long bbsId;
    @NonNull
    private String title;

    private long count;

    private long readCount;

    private long lastUpdate;
    @Nullable
    private Long lastWrite;

    public HistoryEntity(long unixTime, long bbsId, @NonNull String title, long count, long readCount, long lastUpdate, @Nullable Long lastWrite) {
        this.unixTime = unixTime;
        this.bbsId = bbsId;
        this.title = title;
        this.count = count;
        this.readCount = readCount;
        this.lastUpdate = lastUpdate;
        this.lastWrite = lastWrite;
    }

    public long getUnixTime() {
        return unixTime;
    }

    public void setUnixTime(long unixTime) {
        this.unixTime = unixTime;
    }

    public long getBbsId() {
        return bbsId;
    }

    public void setBbsId(long bbsId) {
        this.bbsId = bbsId;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public long getReadCount() {
        return readCount;
    }

    public void setReadCount(long readCount) {
        this.readCount = readCount;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Nullable
    public Long getLastWrite() {
        return lastWrite;
    }

    public void setLastWrite(@Nullable Long lastWrite) {
        this.lastWrite = lastWrite;
    }

}
