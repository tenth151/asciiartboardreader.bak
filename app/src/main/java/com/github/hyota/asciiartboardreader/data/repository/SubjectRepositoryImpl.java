package com.github.hyota.asciiartboardreader.data.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.hyota.asciiartboardreader.domain.model.Subject;

import java.io.File;

import io.reactivex.Maybe;
import io.reactivex.Single;
import okio.Source;

public class SubjectRepositoryImpl implements SubjectRepository {

    @NonNull
    private SubjectRepository subjectLocalRepository;
    @NonNull
    private SubjectRepository subjectNetworkRepository;

    public SubjectRepositoryImpl(@NonNull SubjectRepository subjectLocalRepository, @NonNull SubjectRepository subjectNetworkRepository) {
        this.subjectLocalRepository = subjectLocalRepository;
        this.subjectNetworkRepository = subjectNetworkRepository;
    }

    @Override
    public Maybe<Subject> findByUrl(@NonNull String scheme, @NonNull String host, @NonNull String category, @Nullable String directory) {
        return Maybe.create(e -> subjectLocalRepository.findByUrl(scheme, host, category, directory)
                .subscribe(e::onSuccess,
                        e::onError,
                        () -> subjectNetworkRepository.findByUrl(scheme, host, category, directory)
                                .subscribe(e::onSuccess)));
    }

    @Override
    public Single<File> save(@NonNull String host, @NonNull String category, @Nullable String directory, @NonNull Source source) {
        return subjectLocalRepository.save(host, category, directory, source);
    }
}
