package com.github.hyota.asciiartboardreader.data.db.room;

import android.support.annotation.NonNull;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.github.hyota.asciiartboardreader.data.db.room.dao.BbsInfoDao;
import com.github.hyota.asciiartboardreader.data.db.room.entity.BbsInfoEntity;
import com.github.hyota.asciiartboardreader.data.repository.BbsInfoRepository;
import com.github.hyota.asciiartboardreader.domain.model.BbsInfo;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

public class BbsInfoRepositoryImpl implements BbsInfoRepository {

    @NonNull
    private BbsInfoDao dao;

    @Inject
    public BbsInfoRepositoryImpl(@NonNull BbsInfoDao dao) {
        this.dao = dao;
    }

    @NonNull
    @Override
    public Single<List<BbsInfo>> findAll() {
        return Single.create(e -> e.onSuccess(Stream.of(dao.findAll())
                .map(entity -> new BbsInfo(entity.getId(), entity.getTitle(), entity.getScheme(), entity.getServer(), entity.getCategory(), entity.getDirectory(), entity.getSort()))
                .collect(Collectors.toList())));
    }

    @NonNull
    @Override
    public Completable save(@NonNull BbsInfo bbsInfo) {
        return Completable.create(e -> {
            if (bbsInfo.getId() == BbsInfo.NEW_BBS_INFO_ID) {
                Long maxSort = dao.maxSort();
                if (maxSort == null) {
                    maxSort = 0L;
                }
                BbsInfoEntity entity = new BbsInfoEntity(bbsInfo.getTitle(), bbsInfo.getScheme(), bbsInfo.getServer(), bbsInfo.getCategory(), bbsInfo.getDirectory(), maxSort);
                dao.insert(entity);
            } else {
                BbsInfoEntity entity = new BbsInfoEntity(bbsInfo.getTitle(), bbsInfo.getScheme(), bbsInfo.getServer(), bbsInfo.getCategory(), bbsInfo.getDirectory(), bbsInfo.getSort());
                entity.setId(bbsInfo.getId());
                dao.update(entity);
            }
            e.onComplete();
        });
    }

    @NonNull
    @Override
    public Completable delete(@NonNull BbsInfo bbsInfo) {
        return Completable.create(e -> dao.delete(bbsInfo.getId()));
    }
}
