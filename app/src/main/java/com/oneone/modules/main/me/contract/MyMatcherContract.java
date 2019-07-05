package com.oneone.modules.main.me.contract;

import com.oneone.framework.ui.ibase.IBaseView;
import com.oneone.modules.matcher.relations.bean.MatcherInfo;

import java.util.List;

/**
 * @author qingfei.chen
 * @since 2018/4/10.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public interface MyMatcherContract {

    interface Model {
    }

    interface View extends IBaseView {
        void onMyMatcherGet(List<MatcherInfo> singles);
    }

    interface Presenter {
        void getMyMatcher();
    }
}
