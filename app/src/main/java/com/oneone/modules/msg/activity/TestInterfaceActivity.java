package com.oneone.modules.msg.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.oneone.BaseActivity;
import com.oneone.R;
import com.oneone.api.constants.Role;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.modules.find.beans.FindCondition;
import com.oneone.modules.find.dto.FindPageUserInfoDTO;
import com.oneone.modules.find.dto.LikeListDto;
import com.oneone.modules.find.dto.LikeUnreadListDto;
import com.oneone.modules.msg.IMManager;
import com.oneone.modules.msg.beans.GiftProd;
import com.oneone.modules.msg.beans.IMEmoji;
import com.oneone.modules.msg.beans.IMFirstRelation;
import com.oneone.modules.msg.beans.IMUser;
import com.oneone.modules.msg.beans.IMUserPrerelation;
import com.oneone.modules.msg.beans.MsgMeta;
import com.oneone.modules.msg.beans.NotifyListItem;
import com.oneone.modules.msg.beans.TalkBeans.MyMessage;
import com.oneone.modules.msg.beans.TalkBeans.MyRecentContact;
import com.oneone.modules.msg.beans.TimeLineInfo;
import com.oneone.modules.msg.contract.MsgContract;
import com.oneone.modules.msg.dto.MsgMetaDto;
import com.oneone.modules.msg.presenter.MsgPresenter;
import com.oneone.modules.task.dto.LoginReceiveAwardDTO;
import com.oneone.modules.task.model.TaskModel;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.modules.user.bean.UserInfoBase;
import com.oneone.restful.ApiResult;

import java.util.List;

import butterknife.BindView;

/**
 * Created by here on 18/5/14.
 */

@LayoutResource(R.layout.activity_test_interface)
public class TestInterfaceActivity extends BaseActivity<MsgPresenter, MsgContract.View> implements MsgContract.View,View.OnTouchListener {
    @BindView(R.id.test_btn)
    Button testBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testInterface();

        System.out.println("%%%%%%%%%%%%%%%");
        System.out.println(HereUser.getInstance().getUserId());

        IMManager.getInstance().registListener(new IMManager.PageListener() {
//            @Override
//            public void onUserRelation(IMUserPrerelation imUserPrerelation) {
//                UserInfo userInfo = new UserInfo();
//                userInfo.setUserId("95f7e543577a4b7a888dfc38c4ce6ea0");
//                userInfo.setNickname("aa");
//                userInfo.setAvatar("https://img.1meipo.com/im_emoji/im_emoji_cool.png");
//                imUserPrerelation.setUserInfo(userInfo);
//                ImTalkActivity.startActivity(getActivityContext(), null, imUserPrerelation);
//            }

            @Override
            public void onLoginRlt(int rlt) {

            }

            @Override
            public void refreshMsgAdapter() {

            }

            @Override
            public void msgReply(String targetUid) {

            }
        });

//        NIMClient.getService(MsgService.class).queryRecentContacts()
//                .setCallback(new RequestCallbackWrapper<List<RecentContact>>() {
//                    @Override
//                    public void onResult(int code, List<RecentContact> recents, Throwable e) {
//                        // recents参数即为最近联系人列表（最近会话列表）
//                        System.out.println("recent --------------->");
//                        if (recents != null) {
//                            for (RecentContact contact : recents) {
//                                System.out.println(contact.getContent());
//                            }
//                        }
//                        System.out.println("end --------------->");
//                    }
//                });

        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ClientAndSystemMsgActivity.startActivity(getActivityContext());
//                mPresenter.imUserIsrelation();

//                System.out.println("6666666666666");
//                System.out.println(HereUser.getInstance().getUserId());

//                UserInfoBase userInfoBase = new UserInfoBase();
//                userInfoBase.setUserId("95f7e543577a4b7a888dfc38c4ce6ea0");
//                userInfoBase.setNickname("IM测试");
//                userInfoBase.setAvatar("oo/2f525fd0496d47818c4507053ba30ecb");
//                userInfoBase.setRole(Role.SINGLE);
//                IMManager.getInstance().checkRelation(getActivityContext(), "95f7e543577a4b7a888dfc38c4ce6ea0");

            }
        });
    }

    public void testInterface () {
        mPresenter.prodGiftList("im_gift");

        mPresenter.imMsgListEmoji();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onFindRecommend(List<FindPageUserInfoDTO> userList, int expire) {

    }

    @Override
    public void onFindSetCondition() {

    }

    @Override
    public void onFindGetCondition(FindCondition findCondition) {

    }

    @Override
    public void onSetLike() {

    }

    @Override
    public void onCancelLike() {

    }

    @Override
    public void onSetNoFeel() {

    }

    @Override
    public void onCancelNoFeel() {

    }

    @Override
    public void onImUserGettoken(IMUser imUser) {

    }

    @Override
    public void onImRefreshToken(IMUser imUser) {

    }

    @Override
    public void onImUserIsrelation(boolean relation) {

    }

    @Override
    public void onImUserPrerelation(IMUserPrerelation imUserPrerelation) {

    }

    @Override
    public void onImUserApply(int status, String toImUserId) {

    }

    @Override
    public void onImUserMsgreply(ApiResult rlt) {

    }

    @Override
    public void onImUserDelrealation(ApiResult rlt) {

    }

    @Override
    public void onImUserFirstRelationList(boolean isLoadMore, int oldCount, int newCount, int count, List<IMFirstRelation> imFirstRelationList) {

    }

    @Override
    public void onImUserRelationList(int count, List<IMFirstRelation> imFirstRelationList) {

    }

    @Override
    public void onImMsgCheck(ApiResult<String> rlt, MyMessage myMessage) {

    }

    @Override
    public void onImMsgListEmoji(List<IMEmoji> imEmojiList) {
        if (imEmojiList != null && imEmojiList.size() > 0) {
            IMManager.IM_EMOJI_LIST.clear();
            IMManager.IM_EMOJI_LIST.addAll(imEmojiList);


        }
    }

    @Override
    public void onProdGiftList(int count, List<GiftProd> giftProdList) {
        if (count > 0 && giftProdList != null) {
            IMManager.GIFT_PROD_LIST.clear();
            IMManager.GIFT_PROD_LIST.addAll(giftProdList);
        }
    }

    @Override
    public void onMsgNoteListmeta(boolean isLoadMore, List<MsgMeta> msgBeanList) {

    }

    @Override
    public void onMsgNoteLastmeta(MsgMetaDto msgMetaDto) {

    }

    @Override
    public void onMyLikeList(boolean isLoadMore, LikeListDto listDto) {

    }

    @Override
    public void onLikeMeList(boolean isLoadMore, LikeListDto listDto) {

    }

    @Override
    public void onLikeEachother(boolean isLoadMore, LikeListDto listDto) {

    }


    @Override
    public void onLikeUnread(LikeUnreadListDto likeUnreadListDto) {

    }

    @Override
    public void onGetNotifyList(boolean isRefresh, int count, List<TimeLineInfo> timelineList, List<NotifyListItem> list, long lastReadTime) {

    }

    @Override
    public MsgPresenter onCreatePresenter() {
        return new MsgPresenter();
    }

    public static void startActivity (Context context) {
        Intent it = new Intent(context, TestInterfaceActivity.class);
        context.startActivity(it);
    }
}
