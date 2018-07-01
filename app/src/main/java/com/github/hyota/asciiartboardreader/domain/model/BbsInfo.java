package com.github.hyota.asciiartboardreader.domain.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class BbsInfo {
    public static final long NEW_BBS_INFO_ID = -1;

    private long id;
    @NonNull
    private String title;
    @NonNull
    private String scheme;
    @NonNull
    private String host;
    @NonNull
    private String category;
    @Nullable
    private String directory;
    private long sort;

    public BbsInfo(@NonNull String title, @NonNull String scheme, @NonNull String host, @NonNull String category, @Nullable String directory) {
        this.id = NEW_BBS_INFO_ID;
        this.title = title;
        this.scheme = scheme;
        this.host = host;
        this.category = category;
        this.directory = directory;
    }

    public BbsInfo(long id, @NonNull String title, @NonNull String scheme, @NonNull String host, @NonNull String category, @Nullable String directory, long sort) {
        this.id = id;
        this.title = title;
        this.scheme = scheme;
        this.host = host;
        this.category = category;
        this.directory = directory;
        this.sort = sort;
    }

    public long getId() {
        return id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getScheme() {
        return scheme;
    }

    @NonNull
    public String getHost() {
        return host;
    }

    @NonNull
    public String getCategory() {
        return category;
    }

    @Nullable
    public String getDirectory() {
        return directory;
    }

    public long getSort() {
        return sort;
    }

    public void setSort(long sort) {
        this.sort = sort;
    }
}
