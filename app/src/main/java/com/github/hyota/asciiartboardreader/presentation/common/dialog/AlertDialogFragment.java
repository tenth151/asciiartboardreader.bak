package com.github.hyota.asciiartboardreader.presentation.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.github.hyota.asciiartboardreader.domain.value.AlertDialogRequestCode;

import java.util.Objects;

public final class AlertDialogFragment extends DialogFragment {

    /**
     * AlertDialogFragment で何か処理が起こった場合にコールバックされるリスナ.
     */
    public interface Callback {

        /**
         * AlertDialogFragment で positiveButton, NegativeButton, リスト選択など行われた際に呼ばれる.
         *
         * @param requestCode AlertDialogFragmentFragment 実行時 requestCode
         * @param resultCode  DialogInterface.BUTTON_(POSI|NEGA)TIVE 若しくはリストの position
         * @param params      AlertDialogFragmentFragment に受渡した引数
         */
        void onAlertDialogSuccess(AlertDialogRequestCode requestCode, int resultCode, Bundle params);

        /**
         * AlertDialogFragment がキャンセルされた時に呼ばれる.
         *
         * @param requestCode AlertDialogFragmentFragment 実行時 requestCode
         * @param params      AlertDialogFragmentFragment に受渡した引数
         */
        void onAlertDialogCancel(AlertDialogRequestCode requestCode, Bundle params);
    }

    /**
     * AlertDialogFragmentFragment を Builder パターンで生成する為のクラス.
     */
    public static class Builder {

        /** Activity. */
        final AppCompatActivity activity;

        /** 親 Fragment. */
        final Fragment parentFragment;

        /** タイトル. */
        String title;

        /** メッセージ. */
        String message;

        /** 選択リスト. */
        String[] items;

        /** 肯定ボタン. */
        String positiveLabel;

        /** 否定ボタン. */
        String negativeLabel;

        /*** リクエストコード. 親 Fragment 側の戻りで受け取る.         */
        AlertDialogRequestCode requestCode = AlertDialogRequestCode.NONE;

        /** リスナに受け渡す任意のパラメータ. */
        Bundle params;

        /** DialogFragment のタグ. */
        String tag = "default";

        /** Dialog をキャンセル可かどうか. */
        boolean cancelable = true;

        /**
         * コンストラクタ. Activity 上から生成する場合.
         *
         * @param activity Activity
         */
        public <A extends AppCompatActivity & Callback> Builder(@NonNull final A activity) {
            this.activity = activity;
            parentFragment = null;
        }

        /**
         * コンストラクタ. Fragment 上から生成する場合.
         *
         * @param parentFragment 親 Fragment
         */
        public <F extends Fragment & Callback> Builder(@NonNull final F parentFragment) {
            this.parentFragment = parentFragment;
            activity = null;
        }

        /**
         * タイトルを設定する.
         *
         * @param title タイトル
         * @return Builder
         */
        public Builder title(@NonNull final String title) {
            this.title = title;
            return this;
        }

        /**
         * タイトルを設定する.
         *
         * @param title タイトル
         * @return Builder
         */
        public Builder title(@StringRes final int title) {
            return title(getContext().getString(title));
        }

        /**
         * メッセージを設定する.
         *
         * @param message メッセージ
         * @return Builder
         */
        public Builder message(@NonNull final String message) {
            this.message = message;
            return this;
        }

        /**
         * メッセージを設定する.
         *
         * @param message メッセージ
         * @return Builder
         */
        public Builder message(@StringRes final int message) {
            return message(getContext().getString(message));
        }

        /**
         * 選択リストを設定する.
         *
         * @param items 選択リスト
         * @return Builder
         */
        public Builder items(@NonNull final String... items) {
            this.items = items;
            return this;
        }

        /**
         * 肯定ボタンを設定する.
         *
         * @param positiveLabel 肯定ボタンのラベル
         * @return Builder
         */
        public Builder positive(@NonNull final String positiveLabel) {
            this.positiveLabel = positiveLabel;
            return this;
        }

        /**
         * 肯定ボタンを設定する.
         *
         * @param positiveLabel 肯定ボタンのラベル
         * @return Builder
         */
        public Builder positive(@StringRes final int positiveLabel) {
            return positive(getContext().getString(positiveLabel));
        }

        /**
         * 否定ボタンを設定する.
         *
         * @param negativeLabel 否定ボタンのラベル
         * @return Builder
         */
        public Builder negative(@NonNull final String negativeLabel) {
            this.negativeLabel = negativeLabel;
            return this;
        }

        /**
         * 否定ボタンを設定する.
         *
         * @param negativeLabel 否定ボタンのラベル
         * @return Builder
         */
        public Builder negative(@StringRes final int negativeLabel) {
            return negative(getContext().getString(negativeLabel));
        }

        /**
         * リクエストコードを設定する.
         *
         * @param requestCode リクエストコード
         * @return Builder
         */
        public Builder requestCode(final AlertDialogRequestCode requestCode) {
            this.requestCode = requestCode;
            return this;
        }

        /**
         * DialogFragment のタグを設定する.
         *
         * @param tag タグ
         * @return Builder
         */
        public Builder tag(final String tag) {
            this.tag = tag;
            return this;
        }

        /**
         * Positive / Negative 押下時のリスナに受け渡すパラメータを設定する.
         *
         * @param params リスナに受け渡すパラメータ
         * @return Builder
         */
        public Builder params(final Bundle params) {
            this.params = new Bundle(params);
            return this;
        }

        /**
         * Dialog をキャンセルできるか否かをセットする.
         *
         * @param cancelable キャンセル可か否か
         * @return Builder
         */
        public Builder cancelable(final boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        /**
         * DialogFragment を Builder に設定した情報を元に show する.
         */
        public void show() {
            final Bundle args = new Bundle();
            args.putString("title", title);
            args.putString("message", message);
            args.putStringArray("items", items);
            args.putString("positive_label", positiveLabel);
            args.putString("negative_label", negativeLabel);
            args.putBoolean("cancelable", cancelable);
            if (params != null) {
                args.putBundle("params", params);
            }
            args.putSerializable("request_code", requestCode);

            final AlertDialogFragment f = new AlertDialogFragment();
            f.setArguments(args);
            if (parentFragment != null) {
                f.show(parentFragment.getChildFragmentManager(), tag);
            } else {
                f.show(Objects.requireNonNull(activity).getSupportFragmentManager(), tag);
            }
        }

        /**
         * コンテキストを取得する. getString() 呼び出しの為.
         *
         * @return Context
         */
        private Context getContext() {
            return (activity == null ? Objects.requireNonNull(Objects.requireNonNull(parentFragment).getActivity()) : activity).getApplicationContext();
        }
    }

    /** Callback. */
    private Callback callback;
    /** {@link Context}. */
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Object callback = getParentFragment();
        if (callback == null) {
            callback = context;
            if (callback == null || !(callback instanceof Callback)) {
                throw new IllegalStateException();
            }
        }
        this.context = context;
        this.callback = (Callback) callback;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = Objects.requireNonNull(getArguments());
        final DialogInterface.OnClickListener listener = (dialog, which) -> {
            dismiss();
            callback.onAlertDialogSuccess(getRequestCode(), which, args.getBundle("params"));
        };
        final String title = args.getString("title");
        final String message = args.getString("message");
        final String[] items = args.getStringArray("items");
        final String positiveLabel = args.getString("positive_label");
        final String negativeLabel = args.getString("negative_label");
        setCancelable(args.getBoolean("cancelable"));
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }
        if (items != null && items.length > 0) {
            builder.setItems(items, listener);
        }
        if (!TextUtils.isEmpty(positiveLabel)) {
            builder.setPositiveButton(positiveLabel, listener);
        }
        if (!TextUtils.isEmpty(negativeLabel)) {
            builder.setNegativeButton(negativeLabel, listener);
        }
        return builder.create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        callback.onAlertDialogCancel(getRequestCode(), Objects.requireNonNull(getArguments()).getBundle("params"));
    }

    /**
     * リクエストコードを取得する. Activity と ParentFragment 双方に対応するため.
     *
     * @return requestCode
     */
    @NonNull
    private AlertDialogRequestCode getRequestCode() {
        return (AlertDialogRequestCode) Objects.requireNonNull(Objects.requireNonNull(getArguments()).getSerializable("request_code"));
    }
}