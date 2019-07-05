package com.oneone.modules.entry.contract;

import com.oneone.framework.ui.ibase.IBaseView;
import com.oneone.modules.user.bean.ShowCaseUserInfo;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.modules.user.bean.UserProfileUpdateBean;

import java.util.List;

/**
 * @author qingfei.chen
 * @since 2018/4/3.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public interface OpenRelationContract {

    interface View extends IBaseView {

//        void onUserUpdate();
    }

    interface Presenter {
        void updateUserByRole(int role, UserProfileUpdateBean userInfo, OnUserInfoUpdateListener listener);

        void openRole(int role, OnOpenRoleListener listener);

        void addRole(int role, OnAddRoleListener listener);

        void getUserShowcaseList(int role, OnUserShowcaseListListener listener);

        void chooseRole(int role, OnRoleChooseListener listener);
    }

    interface OnOpenRoleListener {
        void onOpenRole(boolean isOk);
    }

    interface OnAddRoleListener {
        void onAddRole(boolean isOk);
    }

    interface OnUserShowcaseListListener {
        void onGetUserShowcaseList(int role, List<ShowCaseUserInfo> list);
    }

    interface OnRoleChooseListener {
        void onRoleChoose(boolean isOk, int role);
    }

    interface OnUserInfoUpdateListener {
        void onUserInfoUpdate();
    }
}
