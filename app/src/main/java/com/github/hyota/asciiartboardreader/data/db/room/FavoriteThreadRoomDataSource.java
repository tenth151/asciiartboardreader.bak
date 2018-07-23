package com.github.hyota.asciiartboardreader.data.db.room;

import android.support.annotation.NonNull;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.github.hyota.asciiartboardreader.data.datasource.FavoriteThreadDataSource;
import com.github.hyota.asciiartboardreader.data.db.room.dao.FavoriteThreadDao;
import com.github.hyota.asciiartboardreader.data.db.room.entity.FavoriteThreadEntity;
import com.github.hyota.asciiartboardreader.domain.model.BbsInfo;
import com.github.hyota.asciiartboardreader.domain.model.ThreadInfo;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public class FavoriteThreadRoomDataSource implements FavoriteThreadDataSource {

    @NonNull
    private FavoriteThreadDao dao;

    @Inject
    FavoriteThreadRoomDataSource(@NonNull FavoriteThreadDao dao) {
        this.dao = dao;
    }

    @NonNull
    @Override
    public Single<List<ThreadInfo>> findByBbs(@NonNull BbsInfo bbsInfo) {
        return Single.create(e -> e.onSuccess(Stream.of(dao.findByBbsId(bbsInfo.getId()))
                .map(entity -> new ThreadInfo(entity.getUnixTime(), entity.getTitle(), entity.getCount(), bbsInfo, true))
                .collect(Collectors.toList())));
    }

    @NonNull
    @Override
    public Maybe<ThreadInfo> find(@NonNull ThreadInfo threadInfo) {
        return Maybe.create(e -> {
            FavoriteThreadEntity entity = dao.findByUnixTimeAndBbsId(threadInfo.getUnixTime(), threadInfo.getBbsInfo().getId());
            if (entity != null) {
                ThreadInfo info = new ThreadInfo(threadInfo);
                info.setFavorite(true);
                e.onSuccess(info);
            } else {
                e.onComplete();
            }
        });
    }

    @NonNull
    @Override
    public Single<ThreadInfo> save(@NonNull ThreadInfo threadInfo) {
        return Single.create(e -> {
            FavoriteThreadEntity entity = new FavoriteThreadEntity(threadInfo.getUnixTime(), threadInfo.getBbsInfo().getId(), threadInfo.getTitle(), threadInfo.getCount());
            dao.insert(entity);
            ThreadInfo favoriteThread = new ThreadInfo(threadInfo);
            favoriteThread.setFavorite(true);
            e.onSuccess(favoriteThread);
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
