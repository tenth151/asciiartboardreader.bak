package com.github.hyota.asciiartboardreader.data.db.room;

import android.support.annotation.NonNull;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.github.hyota.asciiartboardreader.data.datasource.HistoryDataSource;
import com.github.hyota.asciiartboardreader.data.db.room.dao.HistoryDao;
import com.github.hyota.asciiartboardreader.data.db.room.entity.HistoryEntity;
import com.github.hyota.asciiartboardreader.domain.model.BbsInfo;
import com.github.hyota.asciiartboardreader.domain.model.ThreadInfo;

import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import timber.log.Timber;

public class HistoryRoomDataSource implements HistoryDataSource {

    @NonNull
    private HistoryDao dao;

    @Inject
    HistoryRoomDataSource(@NonNull HistoryDao dao) {
        this.dao = dao;
    }

    @NonNull
    @Override
    public Single<List<ThreadInfo>> findByBbs(@NonNull BbsInfo bbsInfo) {
        return Single.create(e -> e.onSuccess(
                Stream.of(dao.findByBbsId(bbsInfo.getId()))
                        .map(entity -> new ThreadInfo(entity.getUnixTime(), entity.getTitle(), entity.getCount(), bbsInfo, entity.getReadCount(), ZonedDateTime.ofInstant(Instant.ofEpochMilli(entity.getLastUpdate()), ZoneId.systemDefault()), entity.getLastWrite() != null ? ZonedDateTime.ofInstant(Instant.ofEpochMilli(entity.getLastWrite()), ZoneId.systemDefault()) : null))
                        .collect(Collectors.toList())));
    }

    @NonNull
    @Override
    public Maybe<ThreadInfo> find(@NonNull ThreadInfo threadInfo) {
        return Maybe.create(e -> {
            HistoryEntity entity = dao.findByUnixTimeAndBbsId(threadInfo.getUnixTime(), threadInfo.getBbsInfo().getId());
            if (entity != null) {
                ThreadInfo history = new ThreadInfo(threadInfo);
                history.setLastUpdate(ZonedDateTime.ofInstant(Instant.ofEpochMilli(entity.getLastUpdate()), ZoneId.systemDefault()));
                history.setReadCount(entity.getReadCount());
                if (entity.getLastWrite() != null) {
                    history.setLastWrite(ZonedDateTime.ofInstant(Instant.ofEpochMilli(entity.getLastWrite()), ZoneId.systemDefault()));
                }
                e.onSuccess(history);
            } else {
                e.onComplete();
            }
        });
    }

    @NonNull
    @Override
    public Single<ThreadInfo> saveLastUpdate(@NonNull ThreadInfo threadInfo, @NonNull ZonedDateTime dateTime) {
        return Single.create(e -> {
            long epochMilli = dateTime.toInstant().toEpochMilli();
            HistoryEntity entity = dao.findByUnixTimeAndBbsId(threadInfo.getUnixTime(), threadInfo.getBbsInfo().getId());
            if (entity != null) {
                dao.updateLastUpdate(threadInfo.getUnixTime(), threadInfo.getBbsInfo().getId(), epochMilli);
            } else {
                entity = new HistoryEntity(threadInfo.getUnixTime(), threadInfo.getBbsInfo().getId(), threadInfo.getTitle(), threadInfo.getCount(), 0, epochMilli, null);
                dao.insert(entity);
            }
            ThreadInfo history = new ThreadInfo(threadInfo);
            history.setLastUpdate(dateTime);
            e.onSuccess(history);
        });
    }

    @NonNull
    @Override
    public Single<ThreadInfo> updateReadCount(@NonNull ThreadInfo threadInfo, long readCount) {
        return Single.create(e -> {
            HistoryEntity entity = dao.findByUnixTimeAndBbsId(threadInfo.getUnixTime(), threadInfo.getBbsInfo().getId());
            if (entity != null) {
                dao.updateReadCount(threadInfo.getUnixTime(), threadInfo.getBbsInfo().getId(), readCount);
            } else {
                Timber.w("updateReadCount not found history");
            }
            ThreadInfo history = new ThreadInfo(threadInfo);
            history.setReadCount(readCount);
            e.onSuccess(history);
        });
    }

    @NonNull
    @Override
    public Single<ThreadInfo> updateLastWrite(@NonNull ThreadInfo threadInfo, @NonNull ZonedDateTime dateTime) {
        return Single.create(e -> {
            long epochMilli = dateTime.toInstant().toEpochMilli();
            HistoryEntity entity = dao.findByUnixTimeAndBbsId(threadInfo.getUnixTime(), threadInfo.getBbsInfo().getId());
            if (entity != null) {
                dao.updateLastWrite(threadInfo.getUnixTime(), threadInfo.getBbsInfo().getId(), epochMilli);
            } else {
                Timber.w("updateLastWrite not found history");
            }
            ThreadInfo history = new ThreadInfo(threadInfo);
            history.setLastWrite(dateTime);
            e.onSuccess(history);
        });
    }

    @NonNull
    @Override
    public Completable delete(@NonNull ThreadInfo threadInfo) {
        return Completable.create(e -> {
            dao.deleteByUnixTimeAndBbsId(threadInfo.getUnixTime(), threadInfo.getBbsInfo().getId());
            e.onComplete();
        });
    }
}
