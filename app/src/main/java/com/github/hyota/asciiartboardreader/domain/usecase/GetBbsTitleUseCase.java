package com.github.hyota.asciiartboardreader.domain.usecase;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.github.hyota.asciiartboardreader.data.repository.SettingRepository;

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

    public interface OnSuccessCallback {
        void onSuccess(@Nullable String title);
    }

    public interface OnErrorCallback {
        void onError(@NonNull String message);
    }

    @Inject
    GetBbsTitleUseCase(@NonNull SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    @SuppressLint("CheckResult")
    public void execute(@NonNull String url, @NonNull OnSuccessCallback onSuccessCallback, @NonNull OnErrorCallback onErrorCallback) {
        if (TextUtils.isEmpty(url)) {
            onErrorCallback.onError("URLの入力がありません");
            return;
        }
        try {
            Timber.d("validateUrl url = %s", url);
            URI uri = new URI(url);
            String scheme = uri.getScheme();
            if (!"http".equals(scheme) && !"https".equals(scheme)) {
                Timber.d("scheme is invalidate. %s", scheme);
                onErrorCallback.onError("不正なURLです");
                return;
            }
            String host = uri.getHost();
            if (host == null) {
                Timber.d("host is null");
                onErrorCallback.onError("不正なURLです");
                return;
            }
            String path = uri.getPath();
            if (path == null) {
                Timber.d("path is null");
                onErrorCallback.onError("不正なURLです");
                return;
            }
            Timber.d("path = %s", path);
            List<String> elements = Stream.of(path.split("/")).filter(it -> !TextUtils.isEmpty(it)).collect(Collectors.toList());
            if ("jbbs.shitaraba.net".equals(host)) { // したらばの場合
                if (elements.size() == 2) {
                    String category = elements.get(0);
                    String directory = elements.get(1);
                    settingRepository.load(scheme, host, category, directory)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(setting -> onSuccessCallback.onSuccess(setting.getTitle()),
                                    throwable -> {
                                        Timber.d(throwable);
                                        onErrorCallback.onError(throwable.getMessage());
                                    });
                } else {
                    onErrorCallback.onError("不正なURLです");
                }
            } else {
                if (elements.size() == 1) {
                    // TODO
                    return;
                } else {
                    onErrorCallback.onError("不正なURLです");
                }
            }
        } catch (URISyntaxException e) {
            Timber.d(e);
            onErrorCallback.onError("不正なURLです");
        }

    }

}
