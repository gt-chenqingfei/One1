package com.oneone.modules.profile.view;

import android.content.Context;
import android.util.AttributeSet;

import com.oneone.R;
import com.oneone.framework.ui.BaseView;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.modules.profile.presenter.ProfilePresenter;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.modules.user.bean.UserInfoBase;

/**
 * @author qingfei.chen
 * @since 2018/5/12.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public abstract class AbstractProfileView extends BaseView {

    public AbstractProfileView(Context context) {
        super(context);
    }

    public AbstractProfileView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onViewCreated() {

    }

    public abstract void bindData(UserInfo userInfo,ProfilePresenter presenter);

}
