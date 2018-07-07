package com.github.hyota.asciiartboardreader.data.network.retrofit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.hyota.asciiartboardreader.data.repository.DatRepository;
import com.github.hyota.asciiartboardreader.domain.model.Dat;
import com.github.hyota.asciiartboardreader.domain.value.ShitarabaConstant;

import java.io.File;
import java.util.Objects;

import io.reactivex.Maybe;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import okio.Source;

public class DatNetworkRepository implements DatRepository {

    private ShitarabaService shitarabaService;
    private DatRepository datLocalRepository;

    public DatNetworkRepository(ShitarabaService shitarabaService, DatRepository datLocalRepository) {
        this.shitarabaService = shitarabaService;
        this.datLocalRepository = datLocalRepository;
    }

    @Override
    public Maybe<Dat> findByUrl(@NonNull String scheme, @NonNull String host, @NonNull String category, @Nullable String directory, long unixTime) {
        if (ShitarabaConstant.HOST.equals(host)) {
            if (directory == null) {
                throw new IllegalStateException("shitaraba host must not null directory");
            }
            return shitarabaService.dat(category, directory, unixTime)
                    .flatMap(response -> {
                        if (response.isSuccessful()) {
                            ResponseBody body = Objects.requireNonNull(response.body());
                            return datLocalRepository.save(host, category, directory, unixTime, body.source())
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
    public Single<File> save(@NonNull String host, @NonNull String category, @Nullable String directory, long unixTime, @NonNull Source source) {
        throw new IllegalStateException("network repository is not implemented save");
    }
}
