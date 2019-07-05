package com.oneone.modules.qa.model;

import android.content.Context;

import com.oneone.api.RestfulAPI;
import com.oneone.api.RestfulAPIFactory;
import com.oneone.api.QaSub;
import com.oneone.framework.ui.BaseModel;
import com.oneone.modules.qa.beans.QuestionClassify;
import com.oneone.modules.qa.beans.QuestionData;
import com.oneone.modules.qa.dto.QaAnswersFortargetDto;
import com.oneone.modules.qa.dto.QaCountForallDto;
import com.oneone.modules.qa.dto.QaMatchValueDto;
import com.oneone.modules.qa.dto.QuestionListDto;
import com.oneone.modules.user.bean.QaAnswer;
import com.oneone.restful.ApiResult;

import java.util.List;

/**
 * Created by here on 18/4/18.
 */

public class QaModel extends BaseModel {
    private QaSub qaSub;

    public QaModel(Context context) {
        super(context);
        final RestfulAPIFactory factory = new RestfulAPIFactory(context);
        this.qaSub = factory.create(QaSub.class, RestfulAPI.BASE_API_URL, RestfulAPI.getParams(context));
    }

    public ApiResult<QaAnswer> qaAnswered() {
        ApiResult<QaAnswer> result = null;
        try {
            result = this.qaSub.qaCountForall();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult<List<QuestionClassify>> qaCountInfo() {
        ApiResult<List<QuestionClassify>> result = null;
        try {
            result = this.qaSub.qaCountInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult<QuestionListDto> qaUnAnswerMust() {
        ApiResult<QuestionListDto> result = null;
        try {
            result = this.qaSub.qaUnAnswerMust();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult<List<QuestionData>> qaUnAnswerClassify(String classifyId) {
        ApiResult<List<QuestionData>> result = null;
        try {
            result = this.qaSub.qaUnAnswerClassify(classifyId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult<QuestionListDto> qaUnAnswerAll() {
        ApiResult<QuestionListDto> result = null;
        try {
            result = this.qaSub.qaUnAnswerAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult<QuestionListDto> qaAnswered(int skip, int pageCount, String classifyId) {
        ApiResult<QuestionListDto> result = null;
        try {
            result = this.qaSub.qaAnswered(skip, pageCount, classifyId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult qaAnswer(String questionId, String answerId) {
        ApiResult result = null;
        try {
            result = this.qaSub.qaAnswer(questionId, answerId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult<QaMatchValueDto> qaMatchValue(String targetUserId) {
        ApiResult<QaMatchValueDto> result = null;
        try {
            result = this.qaSub.qaMatchValue(targetUserId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult<QaAnswersFortargetDto> qaAnswersFortarget(String targetUserId, int skip, int pageCount) {
        ApiResult<QaAnswersFortargetDto> result = null;
        try {
            result = this.qaSub.qaAnswersFortarget(targetUserId, skip, pageCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
