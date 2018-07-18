package com.github.hyota.asciiartboardreader.data.db.room;

import android.support.annotation.NonNull;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.github.hyota.asciiartboardreader.data.db.room.dao.ReadHistoryDao;
import com.github.hyota.asciiartboardreader.data.db.room.entity.ReadHistoryEntity;
import com.github.hyota.asciiartboardreader.data.repository.ReadHistoryRepository;
import com.github.hyota.asciiartboardreader.domain.model.BbsInfo;
import com.github.hyota.asciiartboardreader.domain.model.ThreadInfo;

import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public class ReadHistoryRepositoryImpl implements ReadHistoryRepository {

    @NonNull
    private ReadHistoryDao dao;

    @Inject
    public ReadHistoryRepositoryImpl(@NonNull ReadHistoryDao dao) {
        this.dao = dao;
    }

    @NonNull
    @Override
    public Single<List<ThreadInfo>> findByBbs(@NonNull BbsInfo bbsInfo) {
        return Single.create(e -> e.onSuccess(Stream.of(dao.findByBbsId(bbsInfo.getId()))
                .map(entity -> new ThreadInfo(entity.getId(), entity.getUnixTime(), entity.getTitle(), entity.getCount(), bbsInfo, entity.getReadCount(), ZonedDateTime.ofInstant(Instant.ofEpochMilli(entity.getLastUpdate()), ZoneId.systemDefault()), entity.getLastWrite() != null ? ZonedDateTime.ofInstant(Instant.ofEpochMilli(entity.getLastWrite()), ZoneId.systemDefault()) : null))
                .collect(Collectors.toList())));
    }

    @NonNull
    @Override
    public Maybe<ThreadInfo> findByThread(@NonNull ThreadInfo threadInfo) {
        return Maybe.create(e -> {
            ReadHistoryEntity entity = dao.findByUnixTimeAndBbsId(threadInfo.getUnixTime(), threadInfo.getBbsInfo().getId());
            if (entity != null) {
                ThreadInfo readHistory = new ThreadInfo(entity.getId(), entity.getUnixTime(), entity.getTitle(), entity.getCount(), threadInfo.getBbsInfo(), entity.getReadCount(), ZonedDateTime.ofInstant(Instant.ofEpochMilli(entity.getLastUpdate()), ZoneId.systemDefault()), entity.getLastWrite() != null ? ZonedDateTime.ofInstant(Instant.ofEpochMilli(entity.getLastWrite()), ZoneId.systemDefault()) : null);
                e.onSuccess(readHistory);
            } else {
                e.onComplete();
            }
        });
    }

    @NonNull
    @Override
    public Single<ThreadInfo> save(@NonNull ThreadInfo threadInfo) {
        return Single.create(e -> {
            ReadHistoryEntity entity = new ReadHistoryEntity(threadInfo.getUnixTime(), threadInfo.getBbsInfo().getId(), threadInfo.getTitle(), threadInfo.getCount(), Objects.requireNonNull(threadInfo.getReadCount()), Objects.requireNonNull(threadInfo.getLastUpdate()).toInstant().toEpochMilli(), threadInfo.getLastWrite() != null ? threadInfo.getLastWrite().toInstant().toEpochMilli() : null);
            long id = dao.insert(entity);
            e.onSuccess(new ThreadInfo(id, threadInfo.getUnixTime(), threadInfo.getTitle(), threadInfo.getCount(), threadInfo.getBbsInfo(), threadInfo.getReadCount(), threadInfo.getLastUpdate(), threadInfo.getLastWrite()));
        });
    }

    @NonNull
    @Override
    public Completable delete(@NonNull ThreadInfo threadInfo) {
        return Completable.create(e -> {
            dao.delete(Objects.requireNonNull(threadInfo.getHistoryId()));
            e.onComplete();
        });
    }
}
