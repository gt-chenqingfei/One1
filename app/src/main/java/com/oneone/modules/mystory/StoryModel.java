package com.oneone.modules.mystory;

import android.content.Context;
import android.text.TextUtils;

import com.oneone.api.RestfulAPI;
import com.oneone.api.RestfulAPIFactory;
import com.oneone.modules.mystory.bean.MyStoryPreviewBean;
import com.oneone.api.MyStoryStub;
import com.oneone.modules.user.HereUser;
import com.oneone.restful.ApiResult;

import java.util.List;

/**
 * Created by chenqingfei on 16/7/18.
 */
public class StoryModel {
    private MyStoryStub mStoryStub = null;

    public StoryModel(Context context) {
        final RestfulAPIFactory factory = new RestfulAPIFactory(context);
        this.mStoryStub = factory.create(MyStoryStub.class, RestfulAPI.BASE_API_URL, RestfulAPI.getParams(context));
    }

    public ApiResult<MyStoryPreviewBean> uploadMyStoryImgs(String myStoryImgJsonStr) {
        ApiResult<MyStoryPreviewBean> result = null;
        try {
            result = this.mStoryStub.uploadMyStoryImgs(myStoryImgJsonStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<String> getMyStoryTags(int tagtype) {
        List<String> userStoryTag = null;
        try {
            ApiResult<List<String>> result = this.mStoryStub.getMyStoryTags(tagtype);
            if (null == result) {
                return null;
            }
            userStoryTag = result.getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userStoryTag;
    }

    public ApiResult<List<String>> getStoryTagsByType(int type) {
        try {
            return this.mStoryStub.getMyStoryTags(type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public ApiResult<MyStoryPreviewBean> getStory(String userId) {
        ApiResult<MyStoryPreviewBean> result = null;
        try {
            String userIdParam = null;
            if (!TextUtils.equals(userId, HereUser.getUserId())) {
                userIdParam = userId;
            }
            result = this.mStoryStub.story(userIdParam);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
