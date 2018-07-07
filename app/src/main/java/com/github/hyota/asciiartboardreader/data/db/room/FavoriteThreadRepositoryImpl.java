package com.github.hyota.asciiartboardreader.data.db.room;

import android.support.annotation.NonNull;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.github.hyota.asciiartboardreader.data.db.room.dao.FavoriteThreadDao;
import com.github.hyota.asciiartboardreader.data.db.room.entity.FavoriteThreadEntity;
import com.github.hyota.asciiartboardreader.data.repository.FavoriteThreadRepository;
import com.github.hyota.asciiartboardreader.domain.model.BbsInfo;
import com.github.hyota.asciiartboardreader.domain.model.ThreadInfo;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public class FavoriteThreadRepositoryImpl implements FavoriteThreadRepository {

    @NonNull
    private FavoriteThreadDao dao;

    @Inject
    FavoriteThreadRepositoryImpl(@NonNull FavoriteThreadDao dao) {
        this.dao = dao;
    }

    @NonNull
    @Override
    public Single<List<ThreadInfo>> findByBbs(@NonNull BbsInfo bbsInfo) {
        return Single.create(e -> e.onSuccess(Stream.of(dao.findByBbsId(bbsInfo.getId()))
                .map(entity -> new ThreadInfo(entity.getUnixTime(), entity.getTitle(), entity.getCount(), bbsInfo))
                .collect(Collectors.toList())));
    }

    @NonNull
    @Override
    public Maybe<ThreadInfo> findByThread(@NonNull ThreadInfo threadInfo) {
        return Maybe.create(e -> {
            FavoriteThreadEntity entity = dao.findByUnixTimeAndBbsId(threadInfo.getUnixTime(), threadInfo.getBbsInfo().getId());
            if (entity != null) {
                ThreadInfo info = new ThreadInfo(entity.getUnixTime(), entity.getTitle(), entity.getCount(), threadInfo.getBbsInfo());
                e.onSuccess(info);
            } else {
                e.onComplete();
            }
        });
    }

    @NonNull
    @Override
    public Completable save(@NonNull ThreadInfo threadInfo) {
        return Completable.create(e -> {
            FavoriteThreadEntity entity = new FavoriteThreadEntity(threadInfo.getUnixTime(), threadInfo.getBbsInfo().getId(), threadInfo.getTitle(), threadInfo.getCount());
            dao.insert(entity);
            e.onComplete();
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
