package com.github.hyota.asciiartboardreader.presentation.common.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;

public class AnimationProgressBar extends ProgressBar {

    private ObjectAnimator objectAnimator = null;

    public AnimationProgressBar(Context context) {
        super(context);
    }

    public AnimationProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimationProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AnimationProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public synchronized void setMax(int max) {
        if (max != getMax()) { // 最大値に更新が合った場合はプログレスを初期化する
            super.setProgress(0);
            super.setMax(max);
        }
    }

    @Override
    public void setProgress(int progress) {
        if (progress < getProgress()) { // プログレスが現在値よりも小さい場合は一旦リセット
            super.setProgress(0);
        }
        setVisibility(VISIBLE); // プログレスに変更がある場合は可視化する
        if (objectAnimator == null) {
            objectAnimator = ObjectAnimator.ofInt(this, "progress", getProgress(), progress);
            objectAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (getProgress() == getMax()) { // 完了後は不可視化
                        setVisibility(GONE);
                    }
                }
            });
        } else {
            objectAnimator.cancel();
            objectAnimator.setIntValues(getProgress(), progress);
        }
        objectAnimator.setDuration(1000);
        objectAnimator.setInterpolator(new DecelerateInterpolator(2.0f));
        objectAnimator.start();
    }
}
