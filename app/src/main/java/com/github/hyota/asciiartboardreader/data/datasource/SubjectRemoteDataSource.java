package com.github.hyota.asciiartboardreader.data.datasource;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.annimon.stream.function.BiFunction;
import com.github.hyota.asciiartboardreader.domain.model.BbsInfo;
import com.github.hyota.asciiartboardreader.domain.model.BaseProgressEvent;

import io.reactivex.Single;
import okio.Source;

public interface SubjectRemoteDataSource {

    @NonNull
    Single<Source> load(@NonNull BbsInfo bbsInfo, @Nullable BiFunction<Integer, Integer, ? extends BaseProgressEvent> progressEvent);

}
