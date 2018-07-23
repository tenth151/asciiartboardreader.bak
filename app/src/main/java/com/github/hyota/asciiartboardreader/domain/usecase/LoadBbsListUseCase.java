package com.github.hyota.asciiartboardreader.domain.usecase;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.github.hyota.asciiartboardreader.BuildConfig;
import com.github.hyota.asciiartboardreader.data.repository.BbsInfoRepository;
import com.github.hyota.asciiartboardreader.domain.model.BbsInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoadBbsListUseCase {

    @NonNull
    private BbsInfoRepository bbsInfoRepository;

    @Inject
    LoadBbsListUseCase(@NonNull BbsInfoRepository bbsInfoRepository) {
        this.bbsInfoRepository = bbsInfoRepository;
    }

    @SuppressLint("CheckResult")
    public void execute() {
        if (BuildConfig.DEBUG) { // TODO DEBUG
            bbsInfoRepository.findAll()
                    .subscribe(bbsInfoList -> {
                        if (bbsInfoList.isEmpty()) {
                            bbsInfoRepository.save(new BbsInfo("やる夫スレヒロイン板（新）", "http", "jbbs.shitaraba.net", "otaku", "15956"))
                                    .subscribe();
                            bbsInfoRepository.save(new BbsInfo("やる夫系雑談・避難・投下板（緊急避難用）", "http", "jbbs.shitaraba.net", "internet", "3408"))
                                    .subscribe();
                        }
                    });
        }
        bbsInfoRepository.findAll()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bbsInfoList -> EventBus.getDefault().post(new Event(new ArrayList<>(bbsInfoList))));
    }

    public static class Event {
        @NonNull
        private List<BbsInfo> items;

        private Event(@NonNull List<BbsInfo> items) {
            this.items = items;
        }

        @NonNull
        public List<BbsInfo> getItems() {
            return items;
        }
    }

}
