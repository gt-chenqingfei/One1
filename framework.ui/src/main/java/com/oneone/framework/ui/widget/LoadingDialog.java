package com.oneone.framework.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oneone.framework.ui.R;


/**
 * @author qingfei.chen
 * @since 2018/3/15.
 * Copyright © 2018 ZheLi Technology Co.,Ltd. All rights reserved.
 */

public class LoadingDialog {


    public static Dialog createLoadingDialog(Context context, String msg) {

        View loadingView = LayoutInflater.from(context).inflate(
                R.layout.widget_loading_dialog, null);
        LinearLayout loadingRootView = loadingView
                .findViewById(R.id.dialog_view);
        ImageView loadingImageView = loadingView.findViewById(R.id.widget_loading_dialog_image);
        TextView loadingMessageView = loadingView.findViewById(R.id.widget_loading_dialog_text);
        Animation animation = AnimationUtils.loadAnimation(context,
                R.anim.retate_loading_dialog);
        loadingImageView.startAnimation(animation);
        if (TextUtils.isEmpty(msg)) {
            loadingMessageView.setVisibility(View.GONE);
        } else {
            loadingMessageView.setVisibility(View.VISIBLE);
            loadingMessageView.setText(msg);
        }
        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);
        loadingDialog.setContentView(loadingRootView, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        return loadingDialog;
    }

}
