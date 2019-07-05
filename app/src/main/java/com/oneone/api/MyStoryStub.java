package com.oneone.api;


import com.oneone.modules.mystory.bean.MyStoryPreviewBean;
import com.oneone.restful.ApiResult;
import com.oneone.restful.ServiceStub;
import com.oneone.restful.annotation.BodyJsonParameter;
import com.oneone.restful.annotation.HttpGet;
import com.oneone.restful.annotation.HttpPost;
import com.oneone.restful.annotation.QueryParameter;

import java.util.List;

/**
 * create by qingfei.chen
 */
public interface MyStoryStub extends ServiceStub {

    @HttpPost("/mystory/mystoryimgs")
    ApiResult<MyStoryPreviewBean> uploadMyStoryImgs(@BodyJsonParameter("myStoryImgsJsonStr") String myStoryImgsJsonStr);

    @HttpGet("/mystory/story")
    ApiResult<MyStoryPreviewBean> story(@QueryParameter("userId") String userId);

    @HttpGet("/mystory/tags")
    ApiResult<List<String>> getMyStoryTags(@QueryParameter("tagtype") int tagtype);

}
