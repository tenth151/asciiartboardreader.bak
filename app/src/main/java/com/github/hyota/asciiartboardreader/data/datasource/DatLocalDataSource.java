package com.github.hyota.asciiartboardreader.data.datasource;

import android.support.annotation.NonNull;

import com.github.hyota.asciiartboardreader.domain.model.BbsInfo;
import com.github.hyota.asciiartboardreader.domain.model.ThreadInfo;

import java.io.File;

import io.reactivex.Single;
import okio.Source;

public interface DatLocalDataSource {

    @NonNull
    Single<File> load(@NonNull ThreadInfo threadInfo);

    @NonNull
    Single<File> save(@NonNull ThreadInfo threadInfo, @NonNull Source source);

}
