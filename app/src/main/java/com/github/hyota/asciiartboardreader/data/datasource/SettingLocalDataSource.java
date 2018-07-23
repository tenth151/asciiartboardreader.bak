package com.github.hyota.asciiartboardreader.data.datasource;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;

import io.reactivex.Single;
import okio.Source;

public interface SettingLocalDataSource {

    @NonNull
    Single<File> load(@NonNull String scheme, @NonNull String host, @NonNull String category, @Nullable String directory);

    @NonNull
    Single<File> save(@NonNull String scheme, @NonNull String host, @NonNull String category, @Nullable String directory, @NonNull Source source);

}
