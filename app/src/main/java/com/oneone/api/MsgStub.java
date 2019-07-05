package com.oneone.api;

import com.oneone.modules.msg.beans.IMEmoji;
import com.oneone.modules.msg.beans.IMUser;
import com.oneone.modules.msg.beans.IMUserPrerelation;
import com.oneone.modules.msg.beans.MsgMeta;
import com.oneone.modules.msg.dto.GiftProdDto;
import com.oneone.modules.msg.dto.IMRelationListDto;
import com.oneone.modules.msg.dto.MsgMetaDto;
import com.oneone.restful.ApiResult;
import com.oneone.restful.ServiceStub;
import com.oneone.restful.annotation.BodyJsonParameter;
import com.oneone.restful.annotation.BodyParameter;
import com.oneone.restful.annotation.HttpGet;
import com.oneone.restful.annotation.HttpPost;
import com.oneone.restful.annotation.QueryParameter;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by here on 18/5/10.
 */

public interface MsgStub extends ServiceStub {
    @HttpGet("/im/user/gettoken")
    ApiResult<IMUser> imUserGettoken();
    @HttpGet("/im/user/refreshtoken")
    ApiResult<IMUser> imRefreshToken();
    @HttpGet("/im/user/isrelation")
    ApiResult<Boolean> imUserIsrelation(@QueryParameter("toUid") String toUid);
    @HttpGet("/im/user/prerelation")
    ApiResult<IMUserPrerelation> imUserPrerelation(@QueryParameter("toUid") String toUid);
    @HttpPost("/im/user/apply")
    ApiResult<JSONObject> imUserApply(@BodyParameter("toUid") String toUid, @BodyParameter("prodCode") String prodCode);
    @HttpPost("/im/user/msgreply")
    ApiResult imUserMsgreply(@BodyParameter("targetImUid") String targetImUid);
    @HttpPost("/im/user/delrealation")
    ApiResult imUserDelrealation(@BodyParameter("targetImUid") String targetImUid);
    @HttpGet("/im/user/firstrelationlist")
    ApiResult<IMRelationListDto> imUserFirstRelationList(@QueryParameter("queryType") int queryType, @QueryParameter("skip") int skip, @QueryParameter("pageCount") int pageCount);
    @HttpPost("/im/user/relationlist")
    ApiResult<IMRelationListDto> imUserRelationList(@BodyJsonParameter("targetImUserIds") String targetImUserIds);
    @HttpPost("/im/msg/check")
    ApiResult<String> imMsgCheck(@BodyParameter("toImUid") String toImUid, @BodyParameter("msgType") String msgType, @BodyParameter("message") String message);
    @HttpGet("/im/msg/listemoji")
    ApiResult<List<IMEmoji>> imMsgListEmoji();

    @HttpGet("/prod/gift/list")
    ApiResult<GiftProdDto> prodGiftList(@QueryParameter("prodType") String prodType);



    //------
    @HttpGet("/msg/note/listmeta")
    ApiResult<List<MsgMeta>> msgNoteListmeta(@QueryParameter("maxTimestamp") String maxTimestamp, @QueryParameter("limit") String limit);
    @HttpGet("/msg/note/lastmeta")
    ApiResult<MsgMetaDto> msgNoteLastmeta();
}
