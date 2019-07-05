package com.oneone.modules.matcher.relations.contract;

import com.oneone.framework.ui.BaseModel;
import com.oneone.framework.ui.ibase.IBaseView;
import com.oneone.modules.matcher.relations.bean.SingleInfo;

import java.util.List;

/**
 * @author qingfei.chen
 * @since 2018/4/10.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public interface SinglesContract {

    interface Model {
    }

    interface View extends IBaseView {

    }

    interface Presenter {
        void getMySingles(boolean isLoadMore,OnMySinglesGetListener listener);

        void matcherSaid(String userId, String matcherSaid,OnMatcherSaidListener listener);

        void matcherRelation(String userId, String matcherRelation,OnMatcherRelationListener listener);

        void getMatcherRelationTag(String userId,OnGetMatherRelationTagsListener listener);

        void getMyMatcher();

        void bindWeChat(String platform, String platformId, OnBindWechatListener listener);
    }

    interface OnBindWechatListener {
        void onBindWeChat();
    }

    interface OnMySinglesGetListener{
        void onMySinglesGet(boolean isLoadMore, List<SingleInfo> singles, int count);
    }

    interface OnMatcherSaidListener{
        void onMatcherSaid();
    }

    interface  OnMatcherRelationListener{
        void onMatcherRelation();
    }

    interface OnGetMatherRelationTagsListener{
        void onMatherRelationTags(List<String> relationTags);
    }

}
