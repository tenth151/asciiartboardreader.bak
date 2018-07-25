package com.github.hyota.asciiartboardreader.domain.usecase;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.github.hyota.asciiartboardreader.data.repository.BbsInfoRepository;
import com.github.hyota.asciiartboardreader.data.repository.SettingRepository;
import com.github.hyota.asciiartboardreader.domain.model.BaseProgressEvent;
import com.github.hyota.asciiartboardreader.domain.model.BbsInfo;
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

public class EditBbsUseCase {

    @NonNull
    private BbsInfoRepository bbsInfoRepository;
    @NonNull
    private SettingRepository settingRepository;

    @Inject
    EditBbsUseCase(@NonNull BbsInfoRepository bbsInfoRepository, @NonNull SettingRepository settingRepository) {
        this.bbsInfoRepository = bbsInfoRepository;
        this.settingRepository = settingRepository;
    }

    @SuppressLint("CheckResult")
    public void execute(long id, long sort, @NonNull String title, @NonNull String url) {
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
            List<String> elements = Stream.of(path.split("/")).filter(it -> !TextUtils.isEmpty(it)).collect(Collectors.toList());
            if (ShitarabaConstant.HOST.equals(host)) { // したらばの場合
                if (elements.size() == 2) {
                    String category = elements.get(0);
                    String directory = elements.get(1);
                    settingRepository.load(scheme, host, category, directory, (max, progress) -> EventBus.getDefault().post(new ProgressEvent(max, progress)))
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(setting -> validDuplicatedUrl(id, sort, title, scheme, host, category, directory),
                                    throwable -> EventBus.getDefault().post(new ErrorEvent(throwable.getMessage())));
                } else {
                    EventBus.getDefault().post(new ErrorEvent("不正なURLです"));
                }
            } else {
                if (elements.size() != 1) {
                    // TODO
                } else {
                    EventBus.getDefault().post(new ErrorEvent("不正なURLです"));
                }
            }
        } catch (URISyntaxException e) {
            Timber.d(e);
            EventBus.getDefault().post(new ErrorEvent("不正なURLです"));
        }

    }

    @SuppressLint("CheckResult")
    private void validDuplicatedUrl(long id, long sort, @NonNull String title, @NonNull String scheme, @NonNull String host, @NonNull String category, @Nullable String directory) {
        bbsInfoRepository.findByUrl(scheme, host, category, directory)
                .subscribe(entity -> {
                            if (entity.getId() != id) {
                                EventBus.getDefault().post(new ErrorEvent("登録済みのURLです"));
                            } else {
                                validTitle(id, sort, title, scheme, host, category, directory);
                            }
                        }, IllegalStateException::new,
                        () -> validTitle(id, sort, title, scheme, host, category, directory));
    }

    @SuppressLint("CheckResult")
    private void validTitle(long id, long sort, @NonNull String title, @NonNull String scheme, @NonNull String host, @NonNull String category, @Nullable String directory) {
        if (TextUtils.isEmpty(title)) {
            EventBus.getDefault().post(new ErrorEvent("タイトルの入力がありません"));
            return;
        }
        bbsInfoRepository.findByTitle(title)
                .subscribe(entity -> {
                            if (entity.getId() != id) {
                                EventBus.getDefault().post(new ErrorEvent("登録済みの板名です"));
                            } else {
                                save(id, sort, title, scheme, host, category, directory);
                            }
                        }, IllegalStateException::new,
                        () -> save(id, sort, title, scheme, host, category, directory));

    }

    @SuppressLint("CheckResult")
    private void save(long id, long sort, @NonNull String title, @NonNull String scheme, @NonNull String host, @NonNull String category, @Nullable String directory) {
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
                .subscribe(
                        result -> EventBus.getDefault().post(new SuccessEvent(result, create)),
                        throwable -> {
                            Timber.d(throwable);
                            if (throwable instanceof NetworkException) {
                                NetworkException networkException = (NetworkException) throwable;
                                EventBus.getDefault().post(new ErrorEvent("statusCode = " + networkException.getResponseCode() + ", " + "message = " + networkException.getMessage()));
                            } else {
                                EventBus.getDefault().post(new ErrorEvent("保存に失敗しました"));
                            }
                        });
    }

    public static class SuccessEvent {
        @NonNull
        private BbsInfo bbsInfo;
        private boolean create;

        private SuccessEvent(@NonNull BbsInfo bbsInfo, boolean create) {
            this.bbsInfo = bbsInfo;
            this.create = create;
        }

        @NonNull
        public BbsInfo getBbsInfo() {
            return bbsInfo;
        }

        public boolean isCreate() {
            return create;
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
