package com.oneone.api;

import com.oneone.modules.qa.beans.QuestionClassify;
import com.oneone.modules.qa.beans.QuestionData;
import com.oneone.modules.qa.dto.QaAnswersFortargetDto;
import com.oneone.modules.qa.dto.QaCountForallDto;
import com.oneone.modules.qa.dto.QaMatchValueDto;
import com.oneone.modules.qa.dto.QuestionListDto;
import com.oneone.modules.user.bean.QaAnswer;
import com.oneone.restful.ApiResult;
import com.oneone.restful.ServiceStub;
import com.oneone.restful.annotation.HttpGet;
import com.oneone.restful.annotation.QueryParameter;

import java.util.List;

/**
 * Created by here on 18/4/18.
 */

public interface QaSub extends ServiceStub {
    @HttpGet("/qa/count/forall")
    ApiResult<QaAnswer> qaCountForall();

    @HttpGet("/qa/countinfo")
    ApiResult<List<QuestionClassify>> qaCountInfo();

    @HttpGet("/qa/unanswer/must")
    ApiResult<QuestionListDto> qaUnAnswerMust();

    @HttpGet("/qa/unanswer/classify")
    ApiResult<List<QuestionData>> qaUnAnswerClassify(@QueryParameter("classifyId") String classifyId);

    @HttpGet("/qa/unanswer/all")
    ApiResult<QuestionListDto> qaUnAnswerAll();

    @HttpGet("/qa/answered")
    ApiResult<QuestionListDto> qaAnswered(@QueryParameter("skip") int skip, @QueryParameter("pageCount") int pageCount, @QueryParameter("classifyId") String classifyId);

    @HttpGet("/qa/answer")
    ApiResult qaAnswer(@QueryParameter("questionId") String questionId, @QueryParameter("answerId") String answerId);

    @HttpGet("/qa/matchvalue")
    ApiResult<QaMatchValueDto> qaMatchValue(@QueryParameter("targetUserId") String targetUserId);

    @HttpGet("/qa/answers/fortarget")
    ApiResult<QaAnswersFortargetDto> qaAnswersFortarget(@QueryParameter("targetUserId") String targetUserId, @QueryParameter("skip") int skip, @QueryParameter("pageCount") int pageCount);
}
