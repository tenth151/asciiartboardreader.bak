package com.github.hyota.asciiartboardreader.data.local;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.github.hyota.asciiartboardreader.R;
import com.github.hyota.asciiartboardreader.data.repository.DatRepository;
import com.github.hyota.asciiartboardreader.domain.model.Dat;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.Single;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

public class DatLocalRepository implements DatRepository, LocalRepository {

    @NonNull
    private Context context;

    @Inject
    DatLocalRepository(@NonNull Context context) {
        this.context = context;
    }

    @Override
    public Maybe<Dat> findByUrl(@NonNull String scheme, @NonNull String host, @NonNull String category, @Nullable String directory, long unixTime) {
        return Maybe.create(e -> {
            File file = getFile(context.getString(R.string.app_name), host, category, directory, unixTime);
            if (file.exists()) {
                e.onSuccess(parse(file, host));
            } else {
                e.onComplete();
            }
        });
    }

    @Override
    public Single<File> save(@NonNull String host, @NonNull String category, @Nullable String directory, long unixTime, @NonNull Source source) {
        return Single.create(e -> {
            File dst = getFile(context.getString(R.string.app_name), host, category, directory, unixTime);
            if (!dst.getParentFile().exists() && !dst.getParentFile().mkdirs()) {
                throw new IllegalStateException("failed mkdirs " + dst.getParentFile().getAbsolutePath());
            }
            try (BufferedSink sink = Okio.buffer(Okio.sink(dst))) {
                sink.writeAll(source);
                e.onSuccess(dst);
            }
        });
    }

    private File getFile(@NonNull String appName, @NonNull String host, @NonNull String category, @Nullable String directory, long unixTime) throws IOException {
        return new File(new File(getLocalDirectory(appName), URLEncoder.encode(Stream.of(host, category, directory).collect(Collectors.joining("/")), "UTF-8")), unixTime + ".dat");
    }

}
