package com.oneone.modules.profile.contract;

import com.oneone.framework.ui.ibase.IBaseView;
import com.oneone.modules.user.bean.UserInfo;

import java.util.List;

/**
 * @author qingfei.chen
 * @since 2018/5/12.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public interface ProfileContract {
    interface View extends IBaseView {
        void onUserInfoGet(UserInfo userInfo);
    }

    interface Presenter {
        void getUserInfo(String userId);

        void follow(String userId);

        void addFollowAndUnFollowListener(OnFollowListener followListener, OnUnFollowListener unFollowListener);

        void unFollow(String userId);

        void like(String userId, OnLikeListener likeListener);

        void likeCancel(String userId, OnLikeCancelListener likeCancelListener);

        void noFeel(String userId, OnNoFeelListener noFeelListener);

        void noFeelCancel(String userId, OnNoFeelCancelListener noFeelCancelListener);
    }

    interface OnFollowListener {
        void onFollow(int followStatus);
    }

    interface OnUnFollowListener {
        void onUnFollow(int followStatus);
    }

    interface OnLikeListener {
        void onLike(boolean isOk);
    }

    interface OnLikeCancelListener {
        void onLikeCancel(boolean isOk);
    }

    interface OnNoFeelListener {
        void onNoFeel(boolean isOk);
    }

    interface OnNoFeelCancelListener {
        void onNoFeelCancel(boolean isOk);
    }

}
