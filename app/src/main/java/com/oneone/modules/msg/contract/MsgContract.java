package com.oneone.modules.msg.contract;

import com.oneone.framework.ui.ibase.IBaseView;
import com.oneone.modules.find.beans.FindCondition;
import com.oneone.modules.find.dto.FindPageUserInfoDTO;
import com.oneone.modules.find.dto.LikeListDto;
import com.oneone.modules.find.dto.LikeUnreadListDto;
import com.oneone.modules.msg.beans.GiftProd;
import com.oneone.modules.msg.beans.IMEmoji;
import com.oneone.modules.msg.beans.IMFirstRelation;
import com.oneone.modules.msg.beans.IMUser;
import com.oneone.modules.msg.beans.IMUserPrerelation;
import com.oneone.modules.msg.beans.MsgMeta;
import com.oneone.modules.msg.beans.NotifyListItem;
import com.oneone.modules.msg.beans.TalkBeans.MyMessage;
import com.oneone.modules.msg.beans.TimeLineInfo;
import com.oneone.modules.msg.dto.MsgMetaDto;
import com.oneone.modules.profile.contract.ProfileContract;
import com.oneone.restful.ApiResult;

import java.util.List;

/**
 * Created by here on 18/4/23.
 */

public interface MsgContract {
    interface View extends IBaseView {
        void onFindRecommend(List<FindPageUserInfoDTO> userList, int expire);
        void onFindSetCondition();
        void onFindGetCondition(FindCondition findCondition);

        void onSetLike();
        void onCancelLike();
        void onSetNoFeel();
        void onCancelNoFeel();


        void onImUserGettoken(IMUser imUser);
        void onImRefreshToken(IMUser imUser);
        void onImUserIsrelation(boolean relation);
        void onImUserPrerelation(IMUserPrerelation imUserPrerelation);
        void onImUserApply(int rltStatus, String toImUserId);
        void onImUserMsgreply(ApiResult rlt);
        void onImUserDelrealation(ApiResult rlt);
        void onImUserFirstRelationList(boolean isLoadMore, int oldCount, int newCount, int count, List<IMFirstRelation> imFirstRelationList);
        void onImUserRelationList(int count, List<IMFirstRelation> imFirstRelationList);
        void onImMsgCheck(ApiResult<String> rlt, MyMessage myMessage);
        void onImMsgListEmoji(List<IMEmoji> imEmojiList);
        void onProdGiftList(int count, List<GiftProd> giftProdList);

        void onMsgNoteListmeta(boolean isLoadMore, List<MsgMeta> msgBeanList);
        void onMsgNoteLastmeta(MsgMetaDto msgMetaDto);

        void onMyLikeList (boolean isLoadMore, LikeListDto listDto);
        void onLikeMeList (boolean isLoadMore, LikeListDto listDto);
        void onLikeEachother(boolean isLoadMore, LikeListDto listDto);
        void onLikeUnread(LikeUnreadListDto likeUnreadListDto);
        void onGetNotifyList(boolean isRefresh, int count, List<TimeLineInfo> timelineList, List<NotifyListItem> list, long lastReadTime);
    }

    interface Presenter {
        void findRecommend();
        void findSetCondition(int character, String cityCode);
        void findGetCondition();


        void imUserGettoken();
        void imRefreshToken();
        void imUserIsrelation(String toUid);
        void imUserPrerelation(String toUid);
        void imUserApply(String toUid, String prodCode);
        void imUserMsgreply(String targetImUid);
        void imUserDelrealation(String targetImUid);
        void imUserFirstRelationList(boolean isLoadMore, int queryType, int skip, int pageCount);
        void imUserRelationList(boolean isShowLoading, String targetImUserIds);
        void imMsgCheck(String toImUid, String msgType, String message, MyMessage myMessage);
        void imMsgListEmoji();
        void prodGiftList(String prodType);

        void msgNoteListmeta(boolean isLoadMore, String maxTimestamp, String limit);
        void msgNoteLastmeta();

        void getNotifyList(boolean isRefresh, long lastNotifyTime, int pageCount, long lastReadTime);

        void follow(String userId, ProfileContract.OnFollowListener listener);
        void unFollow(String userId, ProfileContract.OnUnFollowListener listener);
    }
}
