package com.github.hyota.asciiartboardreader.domain.model;

import android.support.annotation.NonNull;

public class ThreadSubject {

    private long unixTime;
    @NonNull
    private String title;
    private long count;

    public ThreadSubject(long unixTime, @NonNull String title, long count) {
        this.unixTime = unixTime;
        this.title = title;
        this.count = count;
    }

    public long getUnixTime() {
        return unixTime;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public long getCount() {
        return count;
    }

}
