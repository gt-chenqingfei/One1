package com.oneone.api;


import com.oneone.modules.profile.bean.IntersectionInfo;
import com.oneone.modules.user.bean.ShowCaseUserInfo;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.modules.user.bean.UserStatisticInfo;
import com.oneone.modules.user.dto.UserInfoDTO;
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
public interface UserStub extends ServiceStub {
    @HttpGet("/user/userinfo")
    ApiResult<UserInfoDTO> getUserInfo(@QueryParameter("userId") String userId);

    @HttpGet("/user/roleinfo")
    ApiResult<UserInfo> getUserRoleInfo(@QueryParameter("role") int role);

    @HttpPost("/user/updatebyrole")
    ApiResult updateUserInfoByRole(@QueryParameter("role") int role,
                                   @BodyJsonParameter("userRoleSettingJsonStr") String userRoleSettingJsonStr);

    @HttpPost("/user/updateinfo")
    ApiResult updateUserInfo(@BodyJsonParameter("userRoleSettingJsonStr") String userRoleSettingJsonStr);

    @HttpGet("/user/chooserole")
    ApiResult chooseRole(@QueryParameter("role") int role);

    @HttpGet("/user/addrole")
    ApiResult addRole(@QueryParameter("role") int role);

    @HttpGet("/user/openrole")
    ApiResult openRole(@QueryParameter("role") int role);

    @HttpGet("/user/showcase/list")
    ApiResult<List<ShowCaseUserInfo>> getUserShowcaseList(@QueryParameter("role") int role);

    @HttpGet("/statistic/user/query")
    ApiResult<UserStatisticInfo> getStatisticUserQueryInfo();

    @HttpGet("/user/intersection")
    ApiResult<IntersectionInfo> getUserntersectionInfo(@QueryParameter("targetUserId") String targetUserId);
}
