package co.id.klikacara.base.view.dialog;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import co.id.klikacara.R;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BaseJavaDialog extends DialogFragment {
    public static final String TAG = BaseJavaDialog.class.getSimpleName();
    Unbinder unbinder;
    @Nullable
    @BindView(R.id.tv_heading)
    AppCompatTextView tvHeading;
    @Nullable
    @BindView(R.id.tv_content)
    AppCompatTextView tvContent;
    @Nullable
    @BindView(R.id.btn_cancel)
    AppCompatButton btnCancel;
    @Nullable
    @BindView(R.id.btn_ok)
    AppCompatButton btnOk;

    private String title;
    private CharSequence description;
    private String defaultTitle;
    private Boolean isHideBtnCancel = false;
    private WindowManager.LayoutParams dialogLayoutParams = new WindowManager.LayoutParams();
    private View.OnClickListener cancelClickListener;
    private View.OnClickListener okClickListener;
    protected View.OnClickListener defaultCancelClickListener;
    int containerWidth;
    int icon = 0;
    int visibilityIcon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defaultTitle = getResources().getString(R.string.label_info);
        containerWidth = getResources().getDimensionPixelSize
                (R.dimen.dialog_container_width);
        initDefaultCancelClickListener();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null && getDialog().getWindow().getAttributes() != null) {
            dialogLayoutParams.copyFrom(getDialog().getWindow().getAttributes());
            dialogLayoutParams.width = containerWidth;
            getDialog().getWindow().setAttributes(dialogLayoutParams);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    protected void initDefaultCancelClickListener() {
        defaultCancelClickListener = view -> {
            if (isAdded())
                dismiss();
        };
    }

    public void dismissDialog() {
        if (isAdded())
            super.dismissAllowingStateLoss();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        return getInflate(inflater, container, builder);
    }

    @NonNull
    protected View getInflate(@NonNull LayoutInflater inflater, ViewGroup container, AlertDialog.Builder builder) {
        View dialogView = inflater.inflate(getLayout(), container);
        builder.setView(dialogView);
        setCancelable(false);
        unbinder = ButterKnife.bind(this, dialogView);
        if (tvHeading != null)
            tvHeading.setText(getTitle());
        if (tvContent != null) {
            tvContent.setText(getDescription());
            tvContent.setLineSpacing(1, (float) 1.2);
        }
        if (btnOk != null)
            btnOk.setOnClickListener(getOkClickListener() != null ? getOkClickListener() : defaultCancelClickListener);
        if (btnCancel != null) {
            btnCancel.setOnClickListener(getCancelClickListener() != null ? getCancelClickListener() : defaultCancelClickListener);
            btnCancel.setVisibility(isHideBtnCancel ? View.GONE : View.VISIBLE);
        }
        return dialogView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public String getTitle() {
        if (StringUtils.isBlank(title))
            return defaultTitle;
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CharSequence getDescription() {
        return description;
    }

    public void setDescription(CharSequence description) {
        this.description = description;
    }

    public View.OnClickListener getCancelClickListener() {
        return cancelClickListener;
    }

    public void setCancelClickListener(View.OnClickListener cancelClickListener) {
        this.cancelClickListener = cancelClickListener;
    }

    public View.OnClickListener getOkClickListener() {
        return okClickListener;
    }

    public void setOkClickListener(View.OnClickListener okClickListener) {
        this.okClickListener = okClickListener;
    }

    public void hideBtnCancel(Boolean isHideBtnCancel) {
        this.isHideBtnCancel = isHideBtnCancel;
    }

    public void show(FragmentManager manager) {
        show(manager, TAG);
    }

    @Override
    public void show(@NotNull FragmentManager manager, String tag) {
        if (!isAdded())
            super.show(manager, tag);
    }

    public void showAllowingStateLoss(FragmentManager manager, String tag) {
        if (!isAdded()) {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        }
    }

    protected int getLayout() {
        return R.layout.base_dialog;
    }

    public void enableButton() {
        if (btnOk != null)
            btnOk.setEnabled(true);
        if (btnCancel != null)
            btnCancel.setEnabled(true);
    }

    public void disableButton() {
        if (btnOk != null)
            btnOk.setEnabled(false);
        if (btnCancel != null)
            btnCancel.setEnabled(false);
    }

    private void setIcon(int icon) {
        this.icon = icon;
        visibilityIcon = icon == 0 ? View.GONE : View.VISIBLE;
    }
}