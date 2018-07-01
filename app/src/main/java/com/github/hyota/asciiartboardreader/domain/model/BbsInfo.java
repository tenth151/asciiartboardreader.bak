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
    private String server;
    @NonNull
    private String category;
    @Nullable
    private String directory;

    public BbsInfo(long id, @NonNull String title, @NonNull String scheme, @NonNull String server, @NonNull String category, @Nullable String directory) {
        this.id = id;
        this.title = title;
        this.scheme = scheme;
        this.server = server;
        this.category = category;
        this.directory = directory;
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
    public String getServer() {
        return server;
    }

    @NonNull
    public String getCategory() {
        return category;
    }

    @Nullable
    public String getDirectory() {
        return directory;
    }
}
