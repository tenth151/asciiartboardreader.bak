package com.github.hyota.asciiartboardreader.data.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.hyota.asciiartboardreader.domain.model.Dat;
import com.github.hyota.asciiartboardreader.domain.model.Subject;
import com.github.hyota.asciiartboardreader.domain.model.ThreadResponse;
import com.github.hyota.asciiartboardreader.domain.model.ThreadSubject;
import com.github.hyota.asciiartboardreader.domain.value.ShitarabaConstant;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Maybe;
import io.reactivex.Single;
import okio.Source;
import timber.log.Timber;

public interface DatRepository {

    Pattern SHITARABA_PATTERN = Pattern.compile("^(\\d+)<>(.*)<>(.*)<>(\\d{4}/\\d{2}/\\d{2}\\(.\\) \\d{2}:\\d{2}:\\d{2})<>(.*)<>(.*)<>(.+)$");
    DateTimeFormatter RESPONSE_DATE_FORMATTER = DateTimeFormatter.ofPattern("uuuu/MM/dd(E) HH:mm:ss", Locale.getDefault());

    Maybe<Dat> findByUrl(@NonNull String scheme, @NonNull String host, @NonNull String category, @Nullable String directory, long unixTime);

    Single<File> save(@NonNull String host, @NonNull String category, @Nullable String directory, long unixTime, @NonNull Source source);

    default Dat parse(@NonNull File file, @NonNull String host) throws IOException {
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
