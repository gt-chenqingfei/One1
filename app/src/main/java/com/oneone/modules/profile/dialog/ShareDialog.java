package com.oneone.modules.profile.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.api.constants.Role;
import com.oneone.framework.ui.BasePopDialog;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.modules.support.bean.ShareInfo;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.support.share.Callback;
import com.oneone.support.share.ShareBase;
import com.oneone.support.share.ShareParams;
import com.oneone.support.share.ShareParamsUtil;
import com.oneone.support.share.Wechat;
import com.oneone.support.share.WechatMoments;

import org.slf4j.LoggerFactory;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;

/**
 * @author qingfei.chen
 * @since 2018/6/11.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@LayoutResource(R.layout.dialog_profile_share)
public class ShareDialog extends BasePopDialog implements Callback {
    @BindView(R.id.dialog_profile_share_tv_title)
    TextView mTvTitle;

    @BindView(R.id.dialog_profile_share_iv_moment)
    ImageView mIvMoment;
    @BindView(R.id.dialog_profile_share_iv_wechat)
    ImageView mIvWechat;

    @BindView(R.id.dialog_profile_share_tv_cancel)
    TextView mTvCancel;

    private UserInfo mUserInfo;
    private Context mContext;
    private String mGroupId;
    private OnShareCompleteListener mOnShareCompleteListener;

    public interface OnShareCompleteListener{
        void onShareCompleteListener();
    }

    public ShareDialog(@NonNull Context context, UserInfo userInfo) {
        super(context);
        this.mUserInfo = userInfo;
        this.mContext = context;
    }

    public ShareDialog(@NonNull Context context, String groupId, OnShareCompleteListener onShareCompleteListener) {
        super(context);
        this.mContext = context;
        this.mGroupId = groupId;
        this.mOnShareCompleteListener = onShareCompleteListener;
    }

    public ShareDialog(@NonNull Context context, int style) {
        super(context, style);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String shareHint = "";
        if (mUserInfo != null) {
            shareHint = getContext().getString(R.string.str_share_dialog_title, mUserInfo.getMyNickname());
        } else {
            if (ShareInfo.SHARE_INVITE_MATCHER.equals(mGroupId)) {
                shareHint = getContext().getResources().getString(R.string.str_matcher_invite_title_no_newline);
            } else {
                shareHint = getContext().getResources().getString(R.string.str_activity_singles_invite_title_no_newline);
            }
        }
        mTvTitle.setText(shareHint);
    }

    @OnClick({R.id.dialog_profile_share_iv_moment, R.id.dialog_profile_share_iv_wechat})
    public void onShareItemClick(View view) {
        ShareBase shareBase = null;
        switch (view.getId()) {
            case R.id.dialog_profile_share_iv_moment:
                shareBase = new WechatMoments(this, getContext());
                break;

            case R.id.dialog_profile_share_iv_wechat:
                shareBase = new Wechat(this, getContext());
                break;
        }

        if (shareBase == null) {
            return;
        }

        ShareParams param = getShareParam();
        if (param == null) {
            return;
        }
        shareBase.share(param);
    }

    private ShareParams getShareParam() {
        ShareParams param;
        if (mUserInfo == null) {
            if (ShareInfo.SHARE_INVITE_MATCHER.equals(mGroupId)) {
                // 想知道你眼中的我是什么样子的？
                param = ShareParamsUtil.getParam4InviteMatcher(getContext());
            } else {
                // 我正在为优质单身牵线，希望能给你带来对的人
                param = ShareParamsUtil.getParam4InviteSingle(getContext());
            }
            return param;
        }
        // 分享别人（Single 或者 Matcher）的页面
        if (mUserInfo.getUserId().equals(HereUser.getUserId())) {
            // 分享自己的页面
            if (mUserInfo.getRole() == Role.MATCHER) {
                // Matcher 我在OneOne当Matcher，快来支持我
                param = ShareParamsUtil.getParam4SelfMatcherProfile(getContext());
            } else {
                // 推荐一位优秀的单身给你
                param = ShareParamsUtil.getParam4SelfSingleProfile(getContext());
            }
        } else {
            // 分享别人的页面
            if (mUserInfo.getRole() == Role.MATCHER) {
                // Matcher ${NICKNAME}身边的单身朋友都在这儿了，你想认识吗？
                param = ShareParamsUtil.getParam4MatcherProfile(getContext(), mUserInfo.getUserId(), mUserInfo.getNickname());
            } else {
                // 推荐一位优秀的单身给你
                param = ShareParamsUtil.getParam4SingleProfile(getContext(), mUserInfo.getUserId());
            }
        }
        return param;
    }

    @OnClick(R.id.dialog_profile_share_tv_cancel)
    public void onCancelClick(View view) {
        this.cancel();
    }

    @Override
    public void onComplete(ShareParams shareParams, Platform platform, int i, HashMap<String, Object> hashMap) {
        if (mOnShareCompleteListener != null) {
            mOnShareCompleteListener.onShareCompleteListener();
        }
        this.cancel();
    }

    @Override
    public void onError(ShareParams shareParams, Platform platform, int i, Throwable throwable) {
        LoggerFactory.getLogger("ShareBox").error("onError", throwable);
        this.cancel();
    }

    @Override
    public void onCancel(ShareParams shareParams, Platform platform, int i) {
        LoggerFactory.getLogger("ShareBox").error("onCancel");
        this.cancel();
    }
}
