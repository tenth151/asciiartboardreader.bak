package com.github.hyota.asciiartboardreader.data.datasource;

import android.support.annotation.NonNull;

import com.github.hyota.asciiartboardreader.domain.model.BbsInfo;

import java.io.File;

import io.reactivex.Single;
import okio.Source;

public interface SubjectLocalDataSource {

    @NonNull
    Single<File> load(@NonNull BbsInfo bbsInfo);

    @NonNull
    Single<File> save(@NonNull BbsInfo bbsInfo, @NonNull Source source);

}
