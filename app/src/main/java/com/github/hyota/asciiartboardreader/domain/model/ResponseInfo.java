package com.github.hyota.asciiartboardreader.domain.model;

import android.support.annotation.NonNull;

import org.threeten.bp.ZonedDateTime;

public class ResponseInfo extends ThreadResponse {

    @NonNull
    private ThreadInfo threadInfo;

    private boolean asciiArt;

    private boolean abone = false;

    public ResponseInfo(long no, @NonNull String name, @NonNull String email, @NonNull ZonedDateTime dateTime, @NonNull String content, @NonNull String title, @NonNull String id, @NonNull ThreadInfo threadInfo) {
        super(no, name, email, dateTime, content, title, id);
        this.threadInfo = threadInfo;
        this.asciiArt = isAsciiArt(content);
    }

    @NonNull
    public ThreadInfo getThreadInfo() {
        return threadInfo;
    }

    public boolean isAsciiArt() {
        return asciiArt;
    }

    public boolean isAbone() {
        return abone;
    }

    private boolean isAsciiArt(@NonNull String content) {
        return content.split("<br>").length > 5;
    }

}
