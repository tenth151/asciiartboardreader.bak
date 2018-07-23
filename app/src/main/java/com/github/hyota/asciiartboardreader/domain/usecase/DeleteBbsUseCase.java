package com.github.hyota.asciiartboardreader.domain.usecase;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.github.hyota.asciiartboardreader.data.repository.BbsInfoRepository;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DeleteBbsUseCase {

    @NonNull
    private BbsInfoRepository bbsInfoRepository;

    @Inject
    DeleteBbsUseCase(@NonNull BbsInfoRepository bbsInfoRepository) {
        this.bbsInfoRepository = bbsInfoRepository;
    }

    @SuppressLint("CheckResult")
    public void execute(long id) {
        bbsInfoRepository.delete(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> EventBus.getDefault().post(new Event()));
    }

    public static class Event {
        private Event() {
        }
    }

}
