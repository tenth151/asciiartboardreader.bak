package com.github.hyota.asciiartboardreader.domain.usecase;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.github.hyota.asciiartboardreader.data.repository.BbsInfoRepository;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DeleteBbsUseCase {

    @NonNull
    private BbsInfoRepository bbsInfoRepository;

    public interface OnSuccessCallback {
        void onSuccess();
    }

    @Inject
    public DeleteBbsUseCase(@NonNull BbsInfoRepository bbsInfoRepository) {
        this.bbsInfoRepository = bbsInfoRepository;
    }

    @SuppressLint("CheckResult")
    public void execute(long id, @NonNull OnSuccessCallback onSuccessCallback) {
        bbsInfoRepository.delete(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccessCallback::onSuccess);
    }

}
