package com.github.hyota.asciiartboardreader.data.datasource;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.hyota.asciiartboardreader.domain.model.ProgressUpdateListener;
import com.github.hyota.asciiartboardreader.domain.model.ThreadInfo;

import io.reactivex.Single;
import okio.Source;

public interface DatRemoteDataSource {

    @NonNull
    Single<Source> load(@NonNull ThreadInfo threadInfo, @Nullable ProgressUpdateListener progressUpdateListener);

}
