package com.github.hyota.asciiartboardreader.presentation.bbslist;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.github.hyota.asciiartboardreader.BuildConfig;
import com.github.hyota.asciiartboardreader.data.repository.BbsInfoRepository;
import com.github.hyota.asciiartboardreader.domain.model.BbsInfo;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BbsListPresenter implements BbsListContract.Presenter {

    @NonNull
    private BbsListContract.View view;
    @NonNull
    private BbsInfoRepository bbsInfoRepository;

    public BbsListPresenter(@NonNull BbsListContract.View view, @NonNull BbsInfoRepository bbsInfoRepository) {
        this.view = view;
        this.bbsInfoRepository = bbsInfoRepository;
    }

    @SuppressLint("CheckResult")
    @Override
    public void onCreateView() {
        if (BuildConfig.DEBUG) {
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
                .subscribe(bbsInfoList -> view.setData(bbsInfoList));
    }

}
