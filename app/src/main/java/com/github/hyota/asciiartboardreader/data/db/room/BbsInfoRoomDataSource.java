package com.github.hyota.asciiartboardreader.data.db.room;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.github.hyota.asciiartboardreader.data.datasource.BbsInfoDataSource;
import com.github.hyota.asciiartboardreader.data.db.room.dao.BbsInfoDao;
import com.github.hyota.asciiartboardreader.data.db.room.entity.BbsInfoEntity;
import com.github.hyota.asciiartboardreader.data.repository.BbsInfoRepository;
import com.github.hyota.asciiartboardreader.domain.model.BbsInfo;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public class BbsInfoRoomDataSource implements BbsInfoDataSource {

    @NonNull
    private BbsInfoDao dao;

    @Inject
    BbsInfoRoomDataSource(@NonNull BbsInfoDao dao) {
        this.dao = dao;
    }

    @NonNull
    @Override
    public Single<List<BbsInfo>> findAll() {
        return Single.create(e -> e.onSuccess(Stream.of(dao.findAll())
                .map(entity -> new BbsInfo(entity.getId(), entity.getTitle(), entity.getScheme(), entity.getHost(), entity.getCategory(), entity.getDirectory(), entity.getSort()))
                .collect(Collectors.toList())));
    }

    @NonNull
    @Override
    public Maybe<BbsInfo> findByUrl(@NonNull String scheme, @NonNull String host, @NonNull String category, @Nullable String directory) {
        return Maybe.create(e -> {
            BbsInfoEntity entity = dao.findByUrl(scheme, host, category, directory);
            if (entity != null) {
                e.onSuccess(new BbsInfo(entity.getId(), entity.getTitle(), entity.getScheme(), entity.getHost(), entity.getCategory(), entity.getDirectory(), entity.getSort()));
            } else {
                e.onComplete();
            }
        });
    }

    @NonNull
    @Override
    public Maybe<BbsInfo> findByTitle(@NonNull String title) {
        return Maybe.create(e -> {
            BbsInfoEntity entity = dao.findByTitle(title);
            if (entity != null) {
                e.onSuccess(new BbsInfo(entity.getId(), entity.getTitle(), entity.getScheme(), entity.getHost(), entity.getCategory(), entity.getDirectory(), entity.getSort()));
            } else {
                e.onComplete();
            }
        });
    }

    @NonNull
    @Override
    public Single<BbsInfo> save(@NonNull BbsInfo bbsInfo) {
        return Single.create(e -> {
            if (bbsInfo.getId() == BbsInfo.NEW_BBS_INFO_ID) {
                Long maxSort = dao.maxSort();
                if (maxSort == null) {
                    maxSort = 0L;
                } else {
                    maxSort++;
                }
                BbsInfoEntity entity = new BbsInfoEntity(bbsInfo.getTitle(), bbsInfo.getScheme(), bbsInfo.getHost(), bbsInfo.getCategory(), bbsInfo.getDirectory(), maxSort);
                long id = dao.insert(entity);
                e.onSuccess(new BbsInfo(id, bbsInfo.getTitle(), bbsInfo.getScheme(), bbsInfo.getHost(), bbsInfo.getCategory(), bbsInfo.getDirectory(), maxSort));
            } else {
                BbsInfoEntity entity = new BbsInfoEntity(bbsInfo.getTitle(), bbsInfo.getScheme(), bbsInfo.getHost(), bbsInfo.getCategory(), bbsInfo.getDirectory(), bbsInfo.getSort());
                entity.setId(bbsInfo.getId());
                dao.update(entity);
                e.onSuccess(bbsInfo);
            }
        });
    }

    @NonNull
    @Override
    public Completable delete(long id) {
        return Completable.create(e -> {
            dao.delete(id);
            e.onComplete();
        });
    }
}
