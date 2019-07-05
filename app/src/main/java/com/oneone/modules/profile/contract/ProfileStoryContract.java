package com.oneone.modules.profile.contract;

import com.oneone.framework.ui.ibase.IBaseView;
import com.oneone.modules.matcher.relations.bean.MatcherInfo;
import com.oneone.modules.mystory.bean.MyStoryPreviewBean;

import java.util.List;

/**
 * @author qingfei.chen
 * @since 2018/5/14.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public interface ProfileStoryContract {

    interface View extends IBaseView {
        void onGetMatcherSaid(List<MatcherInfo> infoList);

        void onGetStoryInfo(MyStoryPreviewBean storyPreviewBean);
    }

    interface Presenter {

        void getMatcherSaid(String userId);

        void getStoryInfo(String userId);
    }
}
