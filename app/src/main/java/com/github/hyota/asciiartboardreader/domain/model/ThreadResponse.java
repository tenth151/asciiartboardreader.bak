package com.github.hyota.asciiartboardreader.domain.model;

import android.support.annotation.NonNull;

import org.threeten.bp.ZonedDateTime;

public class ThreadResponse {

    private long no;
    @NonNull
    private String name;
    @NonNull
    private String email;
    @NonNull
    private ZonedDateTime dateTime;
    @NonNull
    private String content;
    @NonNull
    private String title;
    @NonNull
    private String id;

    public ThreadResponse(long no, @NonNull String name, @NonNull String email, @NonNull ZonedDateTime dateTime, @NonNull String content, @NonNull String title, @NonNull String id) {
        this.no = no;
        this.name = name;
        this.email = email;
        this.dateTime = dateTime;
        this.content = content;
        this.title = title;
        this.id = id;
    }

    public long getNo() {
        return no;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    @NonNull
    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    @NonNull
    public String getContent() {
        return content;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getId() {
        return id;
    }

}
