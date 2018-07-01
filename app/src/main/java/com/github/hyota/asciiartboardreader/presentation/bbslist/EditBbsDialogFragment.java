package com.github.hyota.asciiartboardreader.presentation.bbslist;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.hyota.asciiartboardreader.R;
import com.github.hyota.asciiartboardreader.domain.value.PermissionRequestCode;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;
import timber.log.Timber;

public class EditBbsDialogFragment extends DialogFragment implements EditBbsContract.View {
    private static final String TAG = EditBbsDialogFragment.class.getSimpleName();

    private static final String ARG_ID = "id";
    private static final String ARG_SORT = "sort";
    private static final String ARG_TITLE = "title";
    private static final String ARG_URL = "url";

    @Inject
    EditBbsContract.Presenter presenter;
    @BindView(R.id.edit_title)
    EditText title;
    @BindView(R.id.edit_url)
    EditText url;
    @BindView(R.id.button_get_title)
    Button getTitleButton;

    private Context context;
    @Nullable
    private Unbinder unbinder;
    private OnEditBbsListener listener;
    private long id;
    private long sort;

    public interface OnEditBbsListener {
        void onEditBbs();
    }

    public static void show(@NonNull FragmentManager manager) {
        Bundle args = new Bundle();
        EditBbsDialogFragment dialogFragment = new EditBbsDialogFragment();
        dialogFragment.setArguments(args);
        dialogFragment.show(manager, TAG);
    }

    public static void show(@NonNull FragmentManager manager, long id, long sort, @NonNull String title, @NonNull String url) {
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        args.putLong(ARG_SORT, sort);
        args.putString(ARG_TITLE, title);
        args.putString(ARG_URL, url);
        EditBbsDialogFragment dialogFragment = new EditBbsDialogFragment();
        dialogFragment.setArguments(args);
        dialogFragment.show(manager, TAG);

    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
        Object obj = getParentFragment();
        if (obj instanceof OnEditBbsListener) {
            listener = (OnEditBbsListener) obj;
        } else if (context instanceof OnEditBbsListener) {
            listener = (OnEditBbsListener) context;
        } else {
            Timber.w("OnEditBbsListener is not implemented");
        }
        this.context = context;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_TITLE, title.getText().toString());
        outState.putString(ARG_URL, url.getText().toString());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = View.inflate(context, R.layout.dialog_edit_bbs, null);
        unbinder = ButterKnife.bind(this, view);

        Bundle args = Objects.requireNonNull(getArguments());
        id = args.getLong(ARG_ID, -1);
        sort = args.getLong(ARG_SORT, -1);
        args = savedInstanceState != null ? savedInstanceState : args;
        title.setText(args.getString(ARG_TITLE));
        url.setText(args.getString(ARG_URL));
        getTitleButton.setOnClickListener(v -> {
            if (PermissionChecker.checkSelfPermission(context, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED &&
                    PermissionChecker.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                presenter.onGetTitle(url.getText().toString());
            } else {
                requestPermissions(new String[]{Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PermissionRequestCode.GET_BBS_TITLE.ordinal());
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setView(view)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    if (PermissionChecker.checkSelfPermission(context, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED &&
                            PermissionChecker.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        presenter.onOk(id, sort, title.getText().toString(), url.getText().toString());
                    } else {
                        requestPermissions(new String[]{Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PermissionRequestCode.ADD_BBS.ordinal());
                    }
                })
                .setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss());
        if (id != -1) {
            builder.setNeutralButton("削除", (dialogInterface, i) -> presenter.onDelete(id));
        }
        AlertDialog dialog = builder.create();
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // NOOP
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // NOOP
            }

            @Override
            public void afterTextChanged(Editable editable) {
                dialog.getButton(DialogInterface.BUTTON_POSITIVE)
                        .setEnabled(!TextUtils.isEmpty(url.getText()) && !TextUtils.isEmpty(title.getText()));
            }
        };
        dialog.setOnShowListener((DialogInterface dialogInterface) -> {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE)
                    .setEnabled(!TextUtils.isEmpty(url.getText()) && !TextUtils.isEmpty(title.getText()));
            url.addTextChangedListener(textWatcher);
            title.addTextChangedListener(textWatcher);
        });
        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionRequestCode permissionRequestCode = PermissionRequestCode.valueOf(requestCode);
        if (permissionRequestCode == null) {
            return;
        }
        switch (permissionRequestCode) {
            case ADD_BBS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    presenter.onOk(id, sort, title.getText().toString(), url.getText().toString());
                } else {
                    Toast.makeText(context, "キャンセルされました", Toast.LENGTH_LONG).show();
                }
                break;
            case GET_BBS_TITLE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    presenter.onGetTitle(url.getText().toString());
                } else {
                    Toast.makeText(context, "キャンセルされました", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void showAlertMessage(@NonNull String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setTitle(@Nullable String title) {
        this.title.setText(title);
    }

    @Override
    public void succeed() {
        if (listener != null) {
            listener.onEditBbs();
        }
        dismiss();
    }

    @Override
    public void deleted() {
        if (listener != null) {
            listener.onEditBbs();
        }
        dismiss();
    }
}
