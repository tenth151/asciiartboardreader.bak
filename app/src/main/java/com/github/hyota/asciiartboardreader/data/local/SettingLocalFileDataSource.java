package com.github.hyota.asciiartboardreader.data.local;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.github.hyota.asciiartboardreader.R;
import com.github.hyota.asciiartboardreader.data.datasource.SettingLocalDataSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;

import javax.inject.Inject;

import io.reactivex.Single;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

public class SettingLocalFileDataSource implements SettingLocalDataSource, LocalFileDataSource {

    @NonNull
    private Context context;

    @Inject
    SettingLocalFileDataSource(@NonNull Context context) {
        this.context = context;
    }

    @NonNull
    public Single<File> load(@NonNull String scheme, @NonNull String host, @NonNull String category, @Nullable String directory) {
        return Single.create(e -> {
            File file = getFile(context.getString(R.string.app_name), host, category, directory);
            if (file.exists()) {
                e.onSuccess(file);
            } else {
                e.onError(new FileNotFoundException());
            }
        });
    }

    @NonNull
    public Single<File> save(@NonNull String scheme, @NonNull String host, @NonNull String category, @Nullable String directory, @NonNull Source source) {
        return Single.create(e -> {
            File dst = getFile(context.getString(R.string.app_name), host, category, directory);
            if (!dst.getParentFile().exists() && !dst.getParentFile().mkdirs()) {
                throw new IllegalStateException("failed mkdirs " + dst.getParentFile().getAbsolutePath());
            }
            try (BufferedSink sink = Okio.buffer(Okio.sink(dst))) {
                sink.writeAll(source);
                e.onSuccess(dst);
            }
        });
    }

    private File getFile(@NonNull String appName, @NonNull String host, @NonNull String category, @Nullable String directory) throws IOException {
        return new File(new File(getLocalDirectory(appName), URLEncoder.encode(Stream.of(host, category, directory).collect(Collectors.joining("/")), "UTF-8")), "setting.txt");
    }

}
