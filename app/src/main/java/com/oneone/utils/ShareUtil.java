package com.oneone.utils;

import android.content.Context;

import com.oneone.R;
import com.oneone.framework.ui.dialog.SheetDialog;
import com.oneone.framework.ui.dialog.SheetItem;
import com.oneone.modules.profile.dialog.ShareDialog;
import com.oneone.modules.user.bean.UserInfo;

/**
 * @author qingfei.chen
 * @since 2018/6/11.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class ShareUtil implements SheetDialog.OnSheetItemClickListener {
    private static final int SHEET_DIALOG_ID = 1;
    private SheetDialog mDialog;
    private Context mContext;
    private ShareDialog mShareDialog;
    private UserInfo mUserInfo;
    private String mGroupId;
    private ShareDialog.OnShareCompleteListener mOnShareCompleteListener;

    public ShareUtil(Context context, UserInfo userInfo) {
        this.mContext = context;
        this.mUserInfo = userInfo;
    }

    public ShareUtil(Context context, String groupId, ShareDialog.OnShareCompleteListener onShareCompleteListener) {
        this.mContext = context;
        this.mGroupId = groupId;
        this.mOnShareCompleteListener = onShareCompleteListener;
    }

    public void show() {
        if (mDialog == null) {
            mDialog = new SheetDialog(mContext, R.array.profile_4single_activity_sheet_share, SHEET_DIALOG_ID, this);
        }
        mDialog.show();
    }

    @Override
    public void onItemClick(SheetItem item, int id) {
        if (mShareDialog == null) {
            if (mUserInfo == null) {
                mShareDialog = new ShareDialog(mContext, mGroupId, mOnShareCompleteListener);
            } else {
                mShareDialog = new ShareDialog(mContext, mUserInfo);
            }
        }
        mShareDialog.show();
    }

}
