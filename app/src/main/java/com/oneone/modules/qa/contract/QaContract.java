package com.oneone.modules.qa.contract;

import com.oneone.framework.ui.ibase.IBaseView;
import com.oneone.modules.qa.beans.MatchForClassify;
import com.oneone.modules.qa.beans.QuestionAnswerMeAndTargetUserBean;
import com.oneone.modules.qa.beans.QuestionClassify;
import com.oneone.modules.qa.beans.QuestionData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by here on 18/4/18.
 */

public interface QaContract {
    interface View extends IBaseView {
        void qaCountForall(int count, int answered);

        void qaCountInfo(List<QuestionClassify> questionClassifies);

        void qaUnAnswerMust(int count, List<QuestionData> list);

        void qaUnAnswerClassify(List<QuestionData> list);

        void qaUnAnswerAll(int count, List<QuestionData> list);

        void qaAnswered(boolean isLoadMore,int count, List<QuestionData> list);

        void qaAnswer(String questionId);

        void qaMatchValue(List<MatchForClassify> matchForClassifyList,int matchForAll);

        void qaAnswersFortarget(int count, ArrayList<QuestionAnswerMeAndTargetUserBean> list);
    }


    interface Presenter {
        void qaAnswered();
        void qaCountInfo();
        void qaUnAnswerMust();
        void qaUnAnswerClassify(String classifyId);
        void qaUnAnswerAll();
        void qaAnswered(boolean isLoadMore,int skip, int pageCount, String classifyId);
        void qaAnswer(String questionId, String answerId);
        void qaMatchValue(String targetUserId);
        void qaAnswersFortarget(String targetUserId, int skip, int pageCount);
    }
}
