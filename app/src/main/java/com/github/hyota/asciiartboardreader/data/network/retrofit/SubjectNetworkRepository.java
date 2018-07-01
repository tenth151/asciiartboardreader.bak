package com.github.hyota.asciiartboardreader.data.network.retrofit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.hyota.asciiartboardreader.data.repository.SubjectRepository;
import com.github.hyota.asciiartboardreader.domain.model.Subject;

import java.io.File;
import java.util.Objects;

import io.reactivex.Maybe;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import okio.Source;

public class SubjectNetworkRepository implements SubjectRepository {

    private ShitarabaService shitarabaService;
    private SubjectRepository subjectLocalRepository;

    public SubjectNetworkRepository(ShitarabaService shitarabaService, SubjectRepository subjectLocalRepository) {
        this.shitarabaService = shitarabaService;
        this.subjectLocalRepository = subjectLocalRepository;
    }

    @Override
    public Maybe<Subject> findByUrl(@NonNull String scheme, @NonNull String host, @NonNull String category, @Nullable String directory) {
        if ("jbbs.shitaraba.net".equals(host)) {
            if (directory == null) {
                throw new IllegalStateException("shitaraba host must not null directory");
            }
            return shitarabaService.subject(category, directory)
                    .flatMap(response -> {
                        if (response.isSuccessful()) {
                            ResponseBody body = Objects.requireNonNull(response.body());
                            return subjectLocalRepository.save(host, category, directory, body.source())
                                    .map(file -> parse(file, host));
                        } else {
                            throw new IllegalStateException("httpCode = " + response.code() + ", message = " + response.message());
                        }
                    }).toMaybe();
        } else {
            throw new IllegalStateException("not implemented");
        }
    }

    @Override
    public Single<File> save(@NonNull String host, @NonNull String category, @Nullable String directory, @NonNull Source source) {
        throw new IllegalStateException("network repository is not implemented save");
    }
}
