package com.github.hyota.asciiartboardreader.data.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.hyota.asciiartboardreader.domain.model.Subject;
import com.github.hyota.asciiartboardreader.domain.model.ThreadSubject;
import com.github.hyota.asciiartboardreader.domain.value.ShitarabaConstant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Maybe;
import io.reactivex.Single;
import okio.Source;

public interface SubjectRepository {

    Pattern SHITARABA_PATTERN = Pattern.compile("(\\d+).cgi,(.+)\\((\\d+)\\)");

    Maybe<Subject> findByUrl(@NonNull String scheme, @NonNull String host, @NonNull String category, @Nullable String directory);

    Single<File> save(@NonNull String host, @NonNull String category, @Nullable String directory, @NonNull Source source);

    default Subject parse(@NonNull File file, @NonNull String host) throws IOException {
        if (ShitarabaConstant.HOST.equals(host)) {
            try (FileInputStream fis = new FileInputStream(file);
                 InputStreamReader isr = new InputStreamReader(fis, ShitarabaConstant.ENCODE);
                 BufferedReader br = new BufferedReader(isr)) {
                List<ThreadSubject> threadSubjectList = new ArrayList<>();
                String line;
                while ((line = br.readLine()) != null) {
                    Matcher m = SHITARABA_PATTERN.matcher(line);
                    if (m.find()) {
                        ThreadSubject threadSubject = new ThreadSubject(Long.parseLong(m.group(1)), m.group(2), Long.parseLong(m.group(3)));
                        threadSubjectList.add(threadSubject);
                    }
                }
                return new Subject(threadSubjectList);
            }
        } else {
            throw new IllegalStateException("not implements");
        }
    }
}
