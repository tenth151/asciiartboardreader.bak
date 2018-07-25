package com.github.hyota.asciiartboardreader.domain.usecase;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.github.hyota.asciiartboardreader.data.repository.SettingRepository;
import com.github.hyota.asciiartboardreader.domain.model.BaseProgressEvent;
import com.github.hyota.asciiartboardreader.domain.model.NetworkException;
import com.github.hyota.asciiartboardreader.domain.value.ShitarabaConstant;

import org.greenrobot.eventbus.EventBus;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class GetBbsTitleUseCase {

    @NonNull
    private SettingRepository settingRepository;

    @Inject
    GetBbsTitleUseCase(@NonNull SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    @SuppressLint("CheckResult")
    public void execute(@NonNull String url) {
        if (TextUtils.isEmpty(url)) {
            EventBus.getDefault().post(new ErrorEvent("URLの入力がありません"));
            return;
        }
        try {
            Timber.d("validateUrl url = %s", url);
            URI uri = new URI(url);
            String scheme = uri.getScheme();
            if (!"http".equals(scheme) && !"https".equals(scheme)) {
                Timber.d("scheme is invalidate. %s", scheme);
                EventBus.getDefault().post(new ErrorEvent("不正なURLです"));
                return;
            }
            String host = uri.getHost();
            if (host == null) {
                Timber.d("host is null");
                EventBus.getDefault().post(new ErrorEvent("不正なURLです"));
                return;
            }
            String path = uri.getPath();
            if (path == null) {
                Timber.d("path is null");
                EventBus.getDefault().post(new ErrorEvent("不正なURLです"));
                return;
            }
            Timber.d("path = %s", path);
            List<String> elements = Stream.of(path.split("/")).filter(it -> !TextUtils.isEmpty(it)).collect(Collectors.toList());
            if (ShitarabaConstant.HOST.equals(host)) { // したらばの場合
                if (elements.size() == 2) {
                    String category = elements.get(0);
                    String directory = elements.get(1);
                    settingRepository.load(scheme, host, category, directory, (max, progress) -> EventBus.getDefault().post(new ProgressEvent(max, progress)))
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(setting -> {
                                        String title = setting.getTitle();
                                        if (TextUtils.isEmpty(title)) {
                                            EventBus.getDefault().post(new ErrorEvent("板名を取得できませんでした"));
                                        } else {
                                            EventBus.getDefault().post(new SuccessEvent(title));
                                        }
                                    },
                                    throwable -> {
                                        Timber.d(throwable);
                                        if (throwable instanceof NetworkException) {
                                            NetworkException networkException = (NetworkException) throwable;
                                            EventBus.getDefault().post(new ErrorEvent("statusCode = " + networkException.getResponseCode() + ", " + "message = " + networkException.getMessage()));
                                        } else {
                                            EventBus.getDefault().post(new ErrorEvent("板名を取得できませんでした"));
                                        }
                                    });
                } else {
                    EventBus.getDefault().post(new ErrorEvent("不正なURLです"));
                }
            } else {
                if (elements.size() == 1) {
                    // TODO
                    return;
                } else {
                    EventBus.getDefault().post(new ErrorEvent("不正なURLです"));
                }
            }
        } catch (URISyntaxException e) {
            Timber.d(e);
            EventBus.getDefault().post(new ErrorEvent("不正なURLです"));
        }

    }

    public static class SuccessEvent {
        @NonNull
        private String title;

        private SuccessEvent(@NonNull String title) {
            this.title = title;
        }

        @NonNull
        public String getTitle() {
            return title;
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

    public static class ProgressEvent extends BaseProgressEvent {
        private ProgressEvent(int max, int progress) {
            super(max, progress);
        }
    }

}
