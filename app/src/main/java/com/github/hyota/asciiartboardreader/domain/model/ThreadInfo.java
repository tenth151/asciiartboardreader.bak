package com.github.hyota.asciiartboardreader.domain.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.hyota.asciiartboardreader.domain.value.ThreadState;

import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.temporal.ChronoUnit;

public class ThreadInfo extends ThreadSubject {

    @NonNull
    private BbsInfo bbsInfo;
    @NonNull
    private ThreadState state = ThreadState.NONE;

    private boolean favorite = false;
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

    public ThreadInfo(long unixTime, @NonNull String title, long count, @NonNull BbsInfo bbsInfo, @NonNull Integer no) {
        super(unixTime, title, count);
        this.bbsInfo = bbsInfo;
        this.no = no;
        this.since = ZonedDateTime.ofInstant(Instant.ofEpochSecond(unixTime), ZoneId.systemDefault());
        this.push = (double) count / ChronoUnit.MINUTES.between(since, ZonedDateTime.now()) * 60 * 24;
    }

    public ThreadInfo(long unixTime, @NonNull String title, long count, @NonNull BbsInfo bbsInfo, @NonNull Long readCount, @NonNull ZonedDateTime lastUpdate, @Nullable ZonedDateTime lastWrite) {
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
    }

    @NonNull
    public BbsInfo getBbsInfo() {
        return bbsInfo;
    }

    @NonNull
    public ThreadState getState() {
        return state;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
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

}
