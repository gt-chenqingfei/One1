package com.oneone.modules.find.contract;

import com.oneone.framework.ui.ibase.IBaseView;
import com.oneone.modules.find.beans.FindCondition;
import com.oneone.modules.find.dto.FindPageUserInfoDTO;

import java.util.List;

/**
 * Created by here on 18/4/23.
 */

public interface FindContract {
    interface View extends IBaseView {
        void onFindRecommend(List<FindPageUserInfoDTO> userList, int expire, int recommandSize);
        void onFindSetCondition();
        void onFindGetCondition(FindCondition findCondition);

        void onSetLike();
        void onCancelLike();
        void onSetNoFeel();
        void onCancelNoFeel();
    }

    interface Presenter {
        void findRecommend();
        void findSetCondition(String findConditionStr);
        void findGetCondition();
    }
}
