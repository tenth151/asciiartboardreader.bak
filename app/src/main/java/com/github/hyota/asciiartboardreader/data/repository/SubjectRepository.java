package com.github.hyota.asciiartboardreader.data.repository;

import android.support.annotation.NonNull;

import com.github.hyota.asciiartboardreader.data.datasource.SubjectLocalDataSource;
import com.github.hyota.asciiartboardreader.data.datasource.SubjectRemoteDataSource;
import com.github.hyota.asciiartboardreader.domain.model.BbsInfo;
import com.github.hyota.asciiartboardreader.domain.model.ThreadSubject;
import com.github.hyota.asciiartboardreader.domain.value.ShitarabaConstant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import io.reactivex.Single;

public class SubjectRepository {

    private static final Pattern SHITARABA_PATTERN = Pattern.compile("(\\d+).cgi,(.+)\\((\\d+)\\)");

    @NonNull
    private SubjectLocalDataSource localDataSource;
    @NonNull
    private SubjectRemoteDataSource remoteDataSource;

    @Inject
    public SubjectRepository(@NonNull SubjectLocalDataSource localDataSource, @NonNull SubjectRemoteDataSource remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }

    @NonNull
    public Single<List<ThreadSubject>> load(@NonNull BbsInfo bbsInfo) {
        return localDataSource.load(bbsInfo)
                .onErrorResumeNext(remoteDataSource.load(bbsInfo)
                        .flatMap(sink -> localDataSource.save(bbsInfo, sink)))
                .map(file -> parse(file, bbsInfo.getHost()));
    }

    @NonNull
    public Single<List<ThreadSubject>> loadFromRemote(@NonNull BbsInfo bbsInfo) {
        return remoteDataSource.load(bbsInfo)
                .flatMap(sink -> localDataSource.save(bbsInfo, sink))
                .map(file -> parse(file, bbsInfo.getHost()));
    }

    private List<ThreadSubject> parse(@NonNull File file, @NonNull String host) throws IOException {
        if (ShitarabaConstant.HOST.equals(host)) {
            try (FileInputStream fis = new FileInputStream(file);
                 InputStreamReader isr = new InputStreamReader(fis, ShitarabaConstant.ENCODE);
                 BufferedReader br = new BufferedReader(isr)) {
                List<ThreadSubject> threadSubjectList = new ArrayList<>();
                String line;
                Set<Long> unixTimeSet = new HashSet<>();
                while ((line = br.readLine()) != null) {
                    Matcher m = SHITARABA_PATTERN.matcher(line);
                    if (m.find()) {
                        ThreadSubject threadSubject = new ThreadSubject(Long.parseLong(m.group(1)), m.group(2), Long.parseLong(m.group(3)));
                        if (!unixTimeSet.contains(threadSubject.getUnixTime())) {
                            threadSubjectList.add(threadSubject);
                            unixTimeSet.add(threadSubject.getUnixTime());
                        }
                    }
                }
                return threadSubjectList;
            }
        } else {
            throw new IllegalStateException("not implements");
        }
    }

}
