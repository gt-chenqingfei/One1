package com.oneone.modules.profile.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.oneone.R;
import com.oneone.framework.android.analytics.annotation.Alias;
import com.oneone.framework.ui.BaseDialog;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.modules.find.adapter.DiscoverSuggestUserAdapter;
import com.oneone.modules.msg.IMManager;
import com.oneone.modules.msg.activity.ImTalkActivity;
import com.oneone.modules.msg.beans.IMUserPrerelation;
import com.oneone.modules.user.bean.UserInfo;

import butterknife.OnClick;

/**
 * @author qingfei.chen
 * @since 2018/6/12.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@Alias("已喜欢弹出")
@LayoutResource(R.layout.dialog_like_already)
public class LikeAlreadyDialog extends BaseDialog {
    private DiscoverSuggestUserAdapter.DiscoverSuggestUserAdapterListener listener;
    private UserInfo userInfo;

    public LikeAlreadyDialog(@NonNull Context context, DiscoverSuggestUserAdapter.DiscoverSuggestUserAdapterListener listener) {
        super(context, com.oneone.framework.ui.R.style.base_dialog);
        this.listener = listener;
    }

    public LikeAlreadyDialog(@NonNull Context context, DiscoverSuggestUserAdapter.DiscoverSuggestUserAdapterListener listener, UserInfo userInfo) {
        super(context, com.oneone.framework.ui.R.style.base_dialog);
        this.listener = listener;
        this.userInfo = userInfo;
    }

    @OnClick(R.id.dialog_like_already_open)
    public void onOpenConvertionClick(View view) {
        if (listener != null) {
            listener.onOpenConvertionClick(userInfo);
            this.dismiss();
        }
    }
}
