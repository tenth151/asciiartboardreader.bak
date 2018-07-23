package com.github.hyota.asciiartboardreader.data.datasource;

import android.support.annotation.NonNull;

import com.github.hyota.asciiartboardreader.domain.model.BbsInfo;

import io.reactivex.Single;
import okio.Source;

public interface SubjectRemoteDataSource {

    @NonNull
    Single<Source> load(@NonNull BbsInfo bbsInfo);

}
