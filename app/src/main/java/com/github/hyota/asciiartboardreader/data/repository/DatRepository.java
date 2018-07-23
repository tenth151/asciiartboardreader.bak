package com.github.hyota.asciiartboardreader.data.repository;

import android.support.annotation.NonNull;

import com.github.hyota.asciiartboardreader.data.datasource.DatLocalDataSource;
import com.github.hyota.asciiartboardreader.data.datasource.DatRemoteDataSource;
import com.github.hyota.asciiartboardreader.domain.model.Dat;
import com.github.hyota.asciiartboardreader.domain.model.ThreadInfo;
import com.github.hyota.asciiartboardreader.domain.model.ThreadResponse;
import com.github.hyota.asciiartboardreader.domain.value.ShitarabaConstant;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import io.reactivex.Single;
import timber.log.Timber;

public class DatRepository {

    private static final Pattern SHITARABA_PATTERN = Pattern.compile("^(\\d+)<>(.*)<>(.*)<>(\\d{4}/\\d{2}/\\d{2}\\(.\\) \\d{2}:\\d{2}:\\d{2})<>(.*)<>(.*)<>(.+)$");
    private static final DateTimeFormatter RESPONSE_DATE_FORMATTER = DateTimeFormatter.ofPattern("uuuu/MM/dd(E) HH:mm:ss", Locale.getDefault());

    @NonNull
    private DatLocalDataSource localDataSource;
    @NonNull
    private DatRemoteDataSource remoteDataSource;

    @Inject
    DatRepository(@NonNull DatLocalDataSource localDataSource, @NonNull DatRemoteDataSource remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }

    @NonNull
    public Single<Dat> load(@NonNull ThreadInfo threadInfo) {
        return localDataSource.load(threadInfo)
                .onErrorResumeNext(remoteDataSource.load(threadInfo)
                        .flatMap(sink -> localDataSource.save(threadInfo, sink)))
                .map(file -> parse(file, threadInfo.getBbsInfo().getHost()));
    }

    @NonNull
    public Single<Dat> loadFromRemote(@NonNull ThreadInfo threadInfo) {
        return remoteDataSource.load(threadInfo)
                .flatMap(sink -> localDataSource.save(threadInfo, sink))
                .map(file -> parse(file, threadInfo.getBbsInfo().getHost()));
    }

    private Dat parse(@NonNull File file, @NonNull String host) throws IOException {
        if (ShitarabaConstant.HOST.equals(host)) {
            try (FileInputStream fis = new FileInputStream(file);
                 InputStreamReader isr = new InputStreamReader(fis, ShitarabaConstant.ENCODE);
                 BufferedReader br = new BufferedReader(isr)) {
                List<ThreadResponse> threadSubjectList = new ArrayList<>();
                String line;
                while ((line = br.readLine()) != null) {
                    Matcher m = SHITARABA_PATTERN.matcher(line);
                    if (m.find()) {
                        LocalDateTime dateTime;
                        try {
                            dateTime = LocalDateTime.parse(m.group(4), RESPONSE_DATE_FORMATTER);
                        } catch (Exception e) {
                            Timber.e(e);
                            dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(0), ZoneId.systemDefault());
                        }
                        ThreadResponse threadResponse = new ThreadResponse(Long.parseLong(m.group(1)), m.group(2), m.group(3), dateTime.atZone(ZoneId.systemDefault()), m.group(5), m.group(6), m.group(7));
                        threadSubjectList.add(threadResponse);
                    }
                }
                return new Dat(threadSubjectList);
            }
        } else {
            throw new IllegalStateException("not implements");
        }
    }

}
