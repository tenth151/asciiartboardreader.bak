package com.github.hyota.asciiartboardreader.domain.usecase;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.github.hyota.asciiartboardreader.data.repository.BbsInfoRepository;
import com.github.hyota.asciiartboardreader.data.repository.SettingRepository;
import com.github.hyota.asciiartboardreader.domain.model.BbsInfo;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class EditBbsUseCase {

    @NonNull
    private BbsInfoRepository bbsInfoRepository;
    @NonNull
    private SettingRepository settingRepository;

    public interface OnSuccessCallback {
        void onSuccess(@NonNull BbsInfo bbsInfo, boolean create);
    }

    public interface OnErrorCallback {
        void onError(@NonNull String message);
    }

    @Inject
    EditBbsUseCase(@NonNull BbsInfoRepository bbsInfoRepository, @NonNull SettingRepository settingRepository) {
        this.bbsInfoRepository = bbsInfoRepository;
        this.settingRepository = settingRepository;
    }

    @SuppressLint("CheckResult")
    public void execute(long id, long sort, @NonNull String title, @NonNull String url,
                        @NonNull OnSuccessCallback onSuccessCallback, @NonNull OnErrorCallback onErrorCallback) {
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
            List<String> elements = Stream.of(path.split("/")).filter(it -> !TextUtils.isEmpty(it)).collect(Collectors.toList());
            if ("jbbs.shitaraba.net".equals(host)) { // したらばの場合
                if (elements.size() == 2) {
                    String category = elements.get(0);
                    String directory = elements.get(1);
                    settingRepository.findByUrl(scheme, host, category, directory)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(setting -> validDuplicatedUrl(id, sort, title, scheme, host, category, directory, onSuccessCallback, onErrorCallback),
                                    throwable -> onErrorCallback.onError(throwable.getMessage()),
                                    () -> onErrorCallback.onError("不正なURLです"));
                } else {
                    onErrorCallback.onError("不正なURLです");
                }
            } else {
                if (elements.size() != 1) {
                    // TODO
                } else {
                    onErrorCallback.onError("不正なURLです");
                }
            }
        } catch (URISyntaxException e) {
            Timber.d(e);
            onErrorCallback.onError("不正なURLです");
        }

    }

    @SuppressLint("CheckResult")
    private void validDuplicatedUrl(long id, long sort, @NonNull String title, @NonNull String scheme, @NonNull String host, @NonNull String category, @Nullable String directory,
                                    @NonNull OnSuccessCallback onSuccessCallback, @NonNull OnErrorCallback onErrorCallback) {
        bbsInfoRepository.findByUrl(scheme, host, category, directory)
                .subscribe(entity -> {
                            if (entity.getId() != id) {
                                onErrorCallback.onError("登録済みのURLです");
                            } else {
                                validTitle(id, sort, title, scheme, host, category, directory, onSuccessCallback, onErrorCallback);
                            }
                        }, IllegalStateException::new,
                        () -> validTitle(id, sort, title, scheme, host, category, directory, onSuccessCallback, onErrorCallback));
    }

    @SuppressLint("CheckResult")
    private void validTitle(long id, long sort, @NonNull String title, @NonNull String scheme, @NonNull String host, @NonNull String category, @Nullable String directory,
                            @NonNull OnSuccessCallback onSuccessCallback, @NonNull OnErrorCallback onErrorCallback) {
        if (TextUtils.isEmpty(title)) {
            onErrorCallback.onError("タイトルの入力がありません");
            return;
        }
        bbsInfoRepository.findByTitle(title)
                .subscribe(entity -> {
                            if (entity.getId() != id) {
                                onErrorCallback.onError("登録済みの板名です");
                            } else {
                                save(id, sort, title, scheme, host, category, directory, onSuccessCallback, onErrorCallback);
                            }
                        }, IllegalStateException::new,
                        () -> save(id, sort, title, scheme, host, category, directory, onSuccessCallback, onErrorCallback));

    }

    @SuppressLint("CheckResult")
    private void save(long id, long sort, @NonNull String title, @NonNull String scheme, @NonNull String host, @NonNull String category, @Nullable String directory,
                      @NonNull OnSuccessCallback onSuccessCallback, @NonNull OnErrorCallback onErrorCallback) {
        BbsInfo bbsInfo;
        boolean create = id == -1;
        if (create) {
            bbsInfo = new BbsInfo(title, scheme, host, category, directory);
        } else {
            bbsInfo = new BbsInfo(id, title, scheme, host, category, directory, sort);
        }
        bbsInfoRepository.save(bbsInfo)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> onSuccessCallback.onSuccess(result, create), throwable -> onErrorCallback.onError(throwable.getMessage()));
    }
}
