package com.oneone.modules.upgrate.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.framework.ui.upgrade.v2.builder.UIData;

public class CustomUpdateDialog extends Dialog {
    private Context mContext;
    private UIData mVersionBundle;
    private boolean mForceUpdate;
    private String mVersionName;

    public CustomUpdateDialog(@NonNull Context context, UIData versionBundle, String versionName, boolean forceUpdate) {
        super(context);
        this.mContext = context;
        this.mVersionName = versionName;
        this.mForceUpdate = forceUpdate;
        this.mVersionBundle = versionBundle;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.version_update_custom_dialog_layout, null);
        TextView updateContent = view.findViewById(R.id.verion_update_content);
        TextView updateVersion = view.findViewById(R.id.version_update_tv_verison_hint);
        ImageView imageView = view.findViewById(R.id.versionchecklib_version_dialog_cancel);
        if (mForceUpdate) {
            imageView.setVisibility(View.GONE);
        } else {
            imageView.setVisibility(View.VISIBLE);
        }
        this.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
        String content = mVersionBundle.getContent();
        content = content.replaceAll("\\\\n", "\n");
        updateContent.setText(content);
        updateVersion.setText(mContext.getResources().getString(R.string.version_update_new_title_hint) + " " + mVersionName);
        setContentView(view);
    }
}
