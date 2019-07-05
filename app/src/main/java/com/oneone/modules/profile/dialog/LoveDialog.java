package com.oneone.modules.profile.dialog;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.oneone.R;
import com.oneone.framework.android.analytics.annotation.Alias;
import com.oneone.framework.ui.BaseDialog;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.modules.msg.IMManager;
import com.oneone.modules.msg.activity.ImTalkActivity;
import com.oneone.modules.msg.beans.IMUserPrerelation;
import com.oneone.modules.msg.widget.UserRelationPhotoLayout;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.bean.UserInfo;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author qingfei.chen
 * @since 2018/6/12.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@Alias("相互喜欢弹出")
@LayoutResource(R.layout.dialog_love)
public class LoveDialog extends BaseDialog {

    @BindView(R.id.love_dialog_relation_photo)
    UserRelationPhotoLayout userRelationPhotoLayout;

    private UserInfo targetUserInfo;

    public LoveDialog(@NonNull Context context, UserInfo targetUserInfo) {
        super(context, com.oneone.framework.ui.R.style.base_dialog);
        this.targetUserInfo = targetUserInfo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRelationPhotoLayout.setAvatarPic(HereUser.getInstance().getUserInfo(), targetUserInfo, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @OnClick(R.id.dialog_like_already_open)
    public void open() {
        IMManager.getInstance().startConversationWithCallBack(getContext(), targetUserInfo.getUserId(), new IMManager.ConversationListener() {
            @Override
            public void onUserRelation(IMUserPrerelation imUserPrerelation) {
                ImTalkActivity.startActivity(getContext(), imUserPrerelation, targetUserInfo);
            }
        });
    }

    @Override
    protected boolean isFullScreen() {
        return true;
    }
}
