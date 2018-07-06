package com.github.hyota.asciiartboardreader.data.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.github.hyota.asciiartboardreader.data.repository.SubjectRepository;
import com.github.hyota.asciiartboardreader.domain.model.Subject;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.Single;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

public class SubjectLocalRepository implements SubjectRepository, LocalRepository {

    @Inject
    SubjectLocalRepository() {
    }

    @Override
    public Maybe<Subject> findByUrl(@NonNull String scheme, @NonNull String host, @NonNull String category, @Nullable String directory) {
        return Maybe.create(e -> {
            File file = getFile(host, category, directory);
            if (file.exists()) {
                e.onSuccess(parse(file, host));
            } else {
                e.onComplete();
            }
        });
    }

    @Override
    public Single<File> save(@NonNull String host, @NonNull String category, @Nullable String directory, @NonNull Source source) {
        return Single.create(e -> {
            File dst = getFile(host, category, directory);
            if (!dst.getParentFile().exists() && !dst.getParentFile().mkdirs()) {
                throw new IllegalStateException("failed mkdirs " + dst.getParentFile().getAbsolutePath());
            }
            try (BufferedSink sink = Okio.buffer(Okio.sink(dst))) {
                sink.writeAll(source);
                e.onSuccess(dst);
            }
        });
    }

    private File getFile(@NonNull String host, @NonNull String category, @Nullable String directory) throws IOException {
        return new File(new File(getLocalDirectory(), URLEncoder.encode(Stream.of(host, category, directory).collect(Collectors.joining("/")), "UTF-8")), "subject.txt");
    }

}
