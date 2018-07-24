package com.github.hyota.asciiartboardreader.domain.usecase;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.github.hyota.asciiartboardreader.data.repository.DatRepository;
import com.github.hyota.asciiartboardreader.domain.model.NetworkException;
import com.github.hyota.asciiartboardreader.domain.model.ResponseInfo;
import com.github.hyota.asciiartboardreader.domain.model.ThreadInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoadDatUseCase {

    @NonNull
    private DatRepository datRepository;

    @Inject
    LoadDatUseCase(@NonNull DatRepository datRepository) {
        this.datRepository = datRepository;
    }

    @SuppressLint("CheckResult")
    public void execute(@NonNull ThreadInfo threadInfo) {
        // TODO あぼーんとのマージ
        datRepository.load(threadInfo)
                .subscribeOn(Schedulers.newThread())
                .map(dat -> Stream.of(dat.getThreadResponseList())
                        .map(threadResponse -> new ResponseInfo(threadResponse.getNo(), threadResponse.getName(), threadResponse.getEmail(), threadResponse.getDateTime(), threadResponse.getContent(), threadResponse.getTitle(), threadResponse.getId(), threadInfo))
                        .collect(Collectors.toList()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        responseInfoList -> EventBus.getDefault().post(new SuccessEvent(responseInfoList)),
                        throwable -> {
                            if (throwable instanceof NetworkException) {
                                NetworkException networkException = (NetworkException) throwable;
                                EventBus.getDefault().post(new ErrorEvent("statusCode = " + networkException.getResponseCode() + ", " + "message = " + networkException.getMessage()));
                            } else {
                                EventBus.getDefault().post(new ErrorEvent("スレッドを取得できませんでした"));
                            }
                        }
                );
    }

    public static class SuccessEvent {
        @NonNull
        private List<ResponseInfo> responseInfoList;

        private SuccessEvent(@NonNull List<ResponseInfo> responseInfoList) {
            this.responseInfoList = responseInfoList;
        }

        @NonNull
        public List<ResponseInfo> getResponseInfoList() {
            return responseInfoList;
        }
    }

    public static class ErrorEvent {
        @NonNull
        private String message;

        private ErrorEvent(@NonNull String message) {
            this.message = message;
        }

        @NonNull
        public String getMessage() {
            return message;
        }
    }

}
