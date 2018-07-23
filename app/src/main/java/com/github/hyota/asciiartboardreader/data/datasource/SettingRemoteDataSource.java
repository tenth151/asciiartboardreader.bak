package com.github.hyota.asciiartboardreader.data.datasource;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.reactivex.Single;
import okio.Source;

public interface SettingRemoteDataSource {

    @NonNull
    Single<Source> load(@NonNull String scheme, @NonNull String host, @NonNull String category, @Nullable String directory);

}
