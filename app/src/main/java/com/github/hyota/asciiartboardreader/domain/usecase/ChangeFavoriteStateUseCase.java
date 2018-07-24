package com.github.hyota.asciiartboardreader.domain.usecase;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.github.hyota.asciiartboardreader.data.repository.FavoriteThreadRepository;
import com.github.hyota.asciiartboardreader.domain.model.ThreadInfo;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ChangeFavoriteStateUseCase {

    @NonNull
    private FavoriteThreadRepository favoriteThreadRepository;

    @Inject
    ChangeFavoriteStateUseCase(@NonNull FavoriteThreadRepository favoriteThreadRepository) {
        this.favoriteThreadRepository = favoriteThreadRepository;
    }

    @SuppressLint("CheckResult")
    public void execute(@NonNull ThreadInfo threadInfo) {
        if (!threadInfo.isFavorite()) {
            favoriteThreadRepository.save(threadInfo)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(info -> EventBus.getDefault().post(new Event(info)));
        } else {
            // TODO タグ関連付け削除
            favoriteThreadRepository.delete(threadInfo)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        threadInfo.setFavorite(false);
                        EventBus.getDefault().post(new Event(threadInfo));
                    });
        }
    }

    public static class Event {
        @NonNull
        private ThreadInfo info;

        private Event(@NonNull ThreadInfo info) {
            this.info = info;
        }

        @NonNull
        public ThreadInfo getInfo() {
            return info;
        }
    }

}
