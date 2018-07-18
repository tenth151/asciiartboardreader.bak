package com.github.hyota.asciiartboardreader.domain.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.hyota.asciiartboardreader.domain.value.ThreadState;

import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.temporal.ChronoUnit;

import java.io.Serializable;

public class ThreadInfo extends ThreadSubject implements Serializable {

    @NonNull
    private BbsInfo bbsInfo;
    @NonNull
    private ThreadState state = ThreadState.NONE;

    @Nullable
    private Long favoriteId = null;
    @Nullable
    private Integer no;
    @Nullable
    private Long readCount = null;
    @Nullable
    private Long newCount = null;
    @NonNull
    private ZonedDateTime since;

    private double push;
    @Nullable
    private ZonedDateTime lastUpdate = null;
    @Nullable
    private ZonedDateTime lastWrite = null;
    @Nullable
    private Long historyId = null;

    public ThreadInfo(long unixTime, @NonNull String title, long count, @NonNull BbsInfo bbsInfo, @NonNull Integer no) {
        super(unixTime, title, count);
        this.bbsInfo = bbsInfo;
        this.no = no;
        this.since = ZonedDateTime.ofInstant(Instant.ofEpochSecond(unixTime), ZoneId.systemDefault());
        this.push = (double) count / ChronoUnit.MINUTES.between(since, ZonedDateTime.now()) * 60 * 24;
    }

    public ThreadInfo(long favoriteId, long unixTime, @NonNull String title, long count, @NonNull BbsInfo bbsInfo) {
        super(unixTime, title, count);
        this.bbsInfo = bbsInfo;
        this.since = ZonedDateTime.ofInstant(Instant.ofEpochSecond(unixTime), ZoneId.systemDefault());
        this.push = 0.0;
        this.favoriteId = favoriteId;
    }

    public ThreadInfo(long historyId, long unixTime, @NonNull String title, long count, @NonNull BbsInfo bbsInfo, @NonNull Long readCount, @NonNull ZonedDateTime lastUpdate, @Nullable ZonedDateTime lastWrite) {
        super(unixTime, title, count);
        this.bbsInfo = bbsInfo;
        this.no = null;
        this.state = ThreadState.LOG;
        this.readCount = readCount;
        this.newCount = count - readCount;
        this.since = ZonedDateTime.ofInstant(Instant.ofEpochSecond(unixTime), ZoneId.systemDefault());
        this.push = 0.0;
        this.lastUpdate = lastUpdate;
        this.lastWrite = lastWrite;
        this.historyId = historyId;
    }

    @NonNull
    public BbsInfo getBbsInfo() {
        return bbsInfo;
    }

    @NonNull
    public ThreadState getState() {
        return state;
    }

    @Nullable
    public Long getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(@Nullable Long favoriteId) {
        this.favoriteId = favoriteId;
    }

    @Nullable
    public Integer getNo() {
        return no;
    }

    @Nullable
    public Long getReadCount() {
        return readCount;
    }

    public void setReadCount(@NonNull Long readCount) {
        this.readCount = readCount;
        this.newCount = getCount() - readCount;
        if (newCount > 0) {
            this.state = ThreadState.UPDATE;
        } else {
            this.state = ThreadState.NO_UPDATE;
        }
    }

    @Nullable
    public Long getNewCount() {
        return newCount;
    }

    @NonNull
    public ZonedDateTime getSince() {
        return since;
    }

    public double getPush() {
        return push;
    }

    @Nullable
    public ZonedDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(@NonNull ZonedDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Nullable
    public ZonedDateTime getLastWrite() {
        return lastWrite;
    }

    public void setLastWrite(@Nullable ZonedDateTime lastWrite) {
        this.lastWrite = lastWrite;
    }

    @Nullable
    public Long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(@NonNull Long historyId) {
        this.historyId = historyId;
    }

}
