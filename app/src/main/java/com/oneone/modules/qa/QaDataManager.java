package com.oneone.modules.qa;

import android.content.Context;

import com.oneone.api.constants.ApiStatus;
import com.oneone.event.EventQaAnswer;
import com.oneone.event.EventRefreshQaClassify;
import com.oneone.modules.qa.beans.QuestionClassify;
import com.oneone.modules.qa.beans.QuestionData;
import com.oneone.modules.qa.dto.QuestionListDto;
import com.oneone.modules.qa.model.QaModel;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.bean.QaAnswer;
import com.oneone.restful.ApiResult;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by here on 18/4/19.
 */

public class QaDataManager {
    private List<QuestionData> questions;
    public List<QuestionClassify> questionClassifies;

    private static QaDataManager qaManager;
    private Context context;

    public static QaDataManager getInstance(Context context) {
        if (qaManager == null) {
            qaManager = new QaDataManager(context);
        }
        return qaManager;
    }

    private QaDataManager(Context context) {
        this.context = context;
    }

    public void fetchQAUnAnswerMust() {
        ApiResult<QuestionListDto> apiResult = new QaModel(context).qaUnAnswerMust();
        if (apiResult != null && apiResult.getStatus() == ApiStatus.OK) {
            questions = apiResult.getData().getList();
        }
    }

    public void fetchQACountInfo() {
        ApiResult<List<QuestionClassify>> apiResult = new QaModel(context).qaCountInfo();
        if (apiResult != null && apiResult.getStatus() == ApiStatus.OK) {
            questionClassifies = apiResult.getData();
            EventBus.getDefault().post(new EventRefreshQaClassify());
        }
    }

    public QuestionClassify getClassifyById(String typeId) {
        for (QuestionClassify classify : questionClassifies) {
            if (classify.getClassifyId().equals(typeId))
                return classify;
        }
        return null;
    }

    public List<QuestionData> getQuestions() {
        return questions;
    }

    public List<QuestionClassify> getQuestionClassifies() {
        return questionClassifies;
    }


}
