package com.oneone.modules.msg.models;

import android.content.Context;

import com.oneone.api.MsgStub;
import com.oneone.api.NotifyStub;
import com.oneone.api.RestfulAPI;
import com.oneone.api.RestfulAPIFactory;
import com.oneone.framework.ui.BaseModel;
import com.oneone.modules.msg.beans.IMEmoji;
import com.oneone.modules.msg.beans.IMUser;
import com.oneone.modules.msg.beans.IMUserPrerelation;
import com.oneone.modules.msg.beans.MsgMeta;
import com.oneone.modules.msg.dto.GiftProdDto;
import com.oneone.modules.msg.dto.IMRelationListDto;
import com.oneone.modules.msg.dto.MsgMetaDto;
import com.oneone.modules.msg.dto.NotifyListDto;
import com.oneone.restful.ApiResult;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by here on 18/5/10.
 */

public class IMModel extends BaseModel {
    private MsgStub imStub;
    private NotifyStub notifyStub;

    public IMModel(Context context) {
        super(context);
        final RestfulAPIFactory factory = new RestfulAPIFactory(context);
        this.imStub = factory.create(MsgStub.class, RestfulAPI.BASE_API_URL, RestfulAPI.getParams(context));
        this.notifyStub = factory.create(NotifyStub.class, RestfulAPI.BASE_API_URL, RestfulAPI.getParams(context));
    }

    public ApiResult<IMUser> imUserGettoken() {
        ApiResult<IMUser> result = null;
        try {
            result = this.imStub.imUserGettoken();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult<IMUser> imRefreshToken() {
        ApiResult<IMUser> result = null;
        try {
            result = this.imStub.imRefreshToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult<Boolean> imUserIsrelation(final String toUid) {
        ApiResult<Boolean> result = null;
        try {
            result = this.imStub.imUserIsrelation(toUid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult<IMUserPrerelation> imUserPrerelation(final String toUid) {
        ApiResult<IMUserPrerelation> result = null;
        try {
            result = this.imStub.imUserPrerelation(toUid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult<JSONObject> imUserApply(final String toUid, final String prodCode) {
        ApiResult<JSONObject> result = null;
        try {
            result = this.imStub.imUserApply(toUid, prodCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult imUserMsgreply(final String targetImUid) {
        ApiResult result = null;
        try {
            result = this.imStub.imUserMsgreply(targetImUid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult imUserDelrealation(final String targetImUid) {
        ApiResult result = null;
        try {
            result = this.imStub.imUserDelrealation(targetImUid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult<IMRelationListDto> imUserFirstRelationList(int queryType, int skip, int pageCount) {
        ApiResult<IMRelationListDto> result = null;
        try {
            result = this.imStub.imUserFirstRelationList(queryType, skip, pageCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult<IMRelationListDto> imUserRelationList(String targetImUserIds) {
        ApiResult<IMRelationListDto> result = null;
        try {
            result = this.imStub.imUserRelationList(targetImUserIds);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult<String> imMsgCheck(String toImUid, String msgType, String message) {
        ApiResult<String> result = null;
        try {
            result = this.imStub.imMsgCheck(toImUid, msgType, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult<List<IMEmoji>> imMsgListEmoji() {
        ApiResult<List<IMEmoji>> result = null;
        try {
            result = this.imStub.imMsgListEmoji();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult<GiftProdDto> prodGiftList(String prodType) {
        ApiResult<GiftProdDto> result = null;
        try {
            result = this.imStub.prodGiftList(prodType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult<List<MsgMeta>> msgNoteListmeta(String maxTimestamp, String limit) {
        ApiResult<List<MsgMeta>> result = null;
        try {
            result = this.imStub.msgNoteListmeta(maxTimestamp, limit);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult<MsgMetaDto> msgNoteLastmeta() {
        ApiResult<MsgMetaDto> result = null;
        try {
            result = this.imStub.msgNoteLastmeta();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult<NotifyListDto> getNotifyList (long lastNotifyTime, int pageCount, long lastReadTime) {
        ApiResult<NotifyListDto> result = null;
        try {
            result = this.notifyStub.getNotifyList(lastNotifyTime, pageCount, lastReadTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
