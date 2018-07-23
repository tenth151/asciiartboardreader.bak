package com.github.hyota.asciiartboardreader.data.local;

import android.content.Context;
import android.support.annotation.NonNull;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.github.hyota.asciiartboardreader.R;
import com.github.hyota.asciiartboardreader.data.datasource.SubjectLocalDataSource;
import com.github.hyota.asciiartboardreader.domain.model.BbsInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;

import javax.inject.Inject;

import io.reactivex.Single;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

public class SubjectLocalFileDataSource implements SubjectLocalDataSource, LocalFileDataSource {

    @NonNull
    private Context context;

    @Inject
    SubjectLocalFileDataSource(@NonNull Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Single<File> load(@NonNull BbsInfo bbsInfo) {
        return Single.create(e -> {
            File file = getFile(context.getString(R.string.app_name), bbsInfo);
            if (file.exists()) {
                e.onSuccess(file);
            } else {
                e.onError(new FileNotFoundException());
            }
        });
    }

    @NonNull
    @Override
    public Single<File> save(@NonNull BbsInfo bbsInfo, @NonNull Source source) {
        return Single.create(e -> {
            File dst = getFile(context.getString(R.string.app_name), bbsInfo);
            if (!dst.getParentFile().exists() && !dst.getParentFile().mkdirs()) {
                throw new IllegalStateException("failed mkdirs " + dst.getParentFile().getAbsolutePath());
            }
            try (BufferedSink sink = Okio.buffer(Okio.sink(dst))) {
                sink.writeAll(source);
                e.onSuccess(dst);
            }
        });
    }

    private File getFile(@NonNull String appName, @NonNull BbsInfo bbsInfo) throws IOException {
        return new File(new File(getLocalDirectory(context.getString(R.string.app_name)), URLEncoder.encode(Stream.of(bbsInfo.getHost(), bbsInfo.getCategory(), bbsInfo.getDirectory()).collect(Collectors.joining("/")), "UTF-8")), "subject.txt");
    }

}
