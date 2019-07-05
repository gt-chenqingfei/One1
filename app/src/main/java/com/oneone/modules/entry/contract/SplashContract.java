package com.oneone.modules.entry.contract;

import com.oneone.framework.ui.ibase.IBaseView;
import com.oneone.modules.entry.beans.LoginInfo;

/**
 * @author qingfei.chen
 * @since 2018/4/3.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public interface SplashContract {
    interface Model {
    }

    interface View extends IBaseView {
        void goAccountException();

        void goHomeIfPreDataLoaded();

        void goLogin();

        void goRoleSelect();

        void goBindWXIfInstall();
    }

    interface Presenter {
        void getLoginInfo(OnLoginInfoListener loginInfoListener);
    }

    interface OnLoginInfoListener {
        void onLoginInfo(LoginInfo loginInfo);
    }
}
