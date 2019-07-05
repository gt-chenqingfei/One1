package com.oneone.modules.entry.contract;

import com.oneone.framework.ui.ibase.IBaseView;
import com.oneone.modules.user.bean.ShowCaseUserInfo;
import com.oneone.modules.user.bean.UserInfo;

import java.util.List;

/**
 * @author qingfei.chen
 * @since 2018/4/3.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public interface AccountContract {

    interface View extends IBaseView {
        void goHome();

        void goBindPhone(String platform, String platformId);

        void goUserRoleSelect();

        void goBindWXIfInstall();

        void validCodeEditViewEnable();

        void onValidCodeGet(int status);

        void alreadyBindWX(final String platform, final String platformId);
    }

    interface Presenter {

        void loginByThirdPart(String platform, String platformId);

        void loginByPhone(final String phoneNumber, final String validCode);

        void getValidateCode(final String phoneNum);

        void bindMobile(String mobileCountryCode, String mobilePhoneNum,
                        String validateCode, String platform, String platformId);

        void bindThird(String platform, String platformId);
    }
}
