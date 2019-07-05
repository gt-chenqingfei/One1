package com.oneone.modules.profile.contract;

import com.oneone.framework.ui.ibase.IBaseView;
import com.oneone.modules.profile.bean.IntersectionInfo;
import com.oneone.modules.user.bean.UserInfo;

public interface IntersectionContract {

    interface View extends IBaseView {
    }

    interface Presenter{
        void getUserIntersectionInfo(String targetUserId, IntersectionListener listener);
        void getUserInfo(String userId, IntersectionUserInfoListener listener);
    }

    interface IntersectionListener {
        void onIntersectionListener(IntersectionInfo intersectionInfo);
    }

    interface IntersectionUserInfoListener {
        void onIntersectionUserInfoListener(UserInfo userInfo);
    }

}
