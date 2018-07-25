package com.github.hyota.asciiartboardreader.data.datasource;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.hyota.asciiartboardreader.domain.model.BbsInfo;
import com.github.hyota.asciiartboardreader.domain.model.ProgressUpdateListener;

import io.reactivex.Single;
import okio.Source;

public interface SubjectRemoteDataSource {

    @NonNull
    Single<Source> load(@NonNull BbsInfo bbsInfo, @Nullable ProgressUpdateListener progressUpdateListener);

}
