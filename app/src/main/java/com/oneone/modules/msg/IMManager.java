package com.oneone.modules.msg;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;
import com.oneone.R;
import com.oneone.framework.android.ApplicationContext;
import com.oneone.modules.msg.beans.GiftProd;
import com.oneone.modules.msg.beans.IMEmoji;
import com.oneone.modules.msg.beans.IMFirstRelation;
import com.oneone.modules.msg.beans.IMUser;
import com.oneone.modules.msg.beans.IMUserPrerelation;
import com.oneone.modules.msg.beans.RelationListRequest;
import com.oneone.modules.msg.beans.TalkBeans.EmojiMessage;
import com.oneone.modules.msg.beans.TalkBeans.GiftMessage;
import com.oneone.modules.msg.beans.TalkBeans.MyMessage;
import com.oneone.modules.msg.beans.TalkBeans.MyRecentContact;
import com.oneone.modules.msg.contract.IMFunctionalInterface;
import com.oneone.modules.msg.contract.NIMFunctional;
import com.oneone.modules.msg.dto.GiftProdDto;
import com.oneone.modules.msg.dto.IMRelationListDto;
import com.oneone.modules.msg.event.ImContactChangeEvent;
import com.oneone.modules.msg.event.ImContactListEvent;
import com.oneone.modules.msg.models.IMModel;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.UserSP;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.restful.ApiResult;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.messages.MsgListAdapter;

/**
 * Created by here on 18/5/9.
 */

public class IMManager {
    public static final int ON_IM_LOGIN_SUCESS = 0;
    public static final int ON_IM_LOGIN_FAILED = 1;

    public static final int MY_MSG_TYPE_TXT_SENDER = 13;
    public static final int MY_MSG_TYPE_TXT_RECEIVER = 14;
    public static final int MY_MSG_TYPE_GIFT_SENDER = 15;
    public static final int MY_MSG_TYPE_GIFT_RECEIVER = 16;
    public static final int MY_MSG_TYPE_EMOJI_SENDER = 17;
    public static final int MY_MSG_TYPE_EMOJI_RECEIVER = 18;
    public static final int MY_MSG_TYPE_PIC_SENDER = 19;
    public static final int MY_MSG_TYPE_PIC_RECEIVER = 20;

    public static final int MY_MSG_TYPE_REPORT_SENDER = 51;
    public static final int MY_MSG_TYPE_REPORT_RECEIVER = 52;

    public static final String PROD_GIFT = "im_gift";

    public static final String ONEONE_IM_TYPE_TEXT = "TEXT";
    public static final String ONEONE_IM_TYPE_PICTURE = "PICTURE";

    public static final int HISTORY_COUNT_LIMIT = 10;

    public static int TEXT_COLOR_1;
    public static int TEXT_COLOR_2;


    public static final String HUAN_XIN_IM = "huanxin";

    private static IMManager MANAGER;

    private IMFunctionalInterface imFunctionalInterface;

    public static List<GiftProd> GIFT_PROD_LIST = new ArrayList<GiftProd>();
    public static List<IMEmoji> IM_EMOJI_LIST = new ArrayList<IMEmoji>();

    public static final String IM_STATUS_INIT = "init";
    public static final String IM_STATUS_DOING_LOGIN = "doing_login";
    public static final String IM_STATUS_LOGIN = "login";
    public static String IM_STATUS = IM_STATUS_INIT;

    public static String curToImUid;
    public NIMFunctional minFunctional;

    public static ManagerListener managerListener;

    public interface ManagerListener {
        void onInitIMContract(List<RecentContact> recents);

        void onReceiveImMessage(List<IMMessage> messages);

        void onIMContractChange(List<RecentContact> recents);

        void onMessageSendRlt(IMessage.MessageStatus status, MyMessage myMessage);

        void onLoginRlt(int rlt);
    }

    //------------------------
    private String curSendGiftMsgImUid;
    public static PageListener pageListener;

    public interface PageListener {
        void onLoginRlt(int rlt);

        void refreshMsgAdapter ();

        void msgReply (String targetUid);
    }

    public interface ConversationListener {
        void onUserRelation(IMUserPrerelation imUserPrerelation);
    }

    public void registListener(PageListener pageListener) {
        IMManager.getInstance().pageListener = pageListener;
    }

    public MsgListAdapter adapter;

    public void setAdapter(MsgListAdapter mAdapter) {
        this.adapter = mAdapter;
    }

    public static IMManager getInstance() {
        return MANAGER;
    }

    public static IMManager getInstance(ApplicationContext context) {
        if (MANAGER == null)
            MANAGER = new IMManager(context);
        return MANAGER;
    }

    public ApiResult<List<IMEmoji>> initEmoji(Context context) {
        return new IMModel(context).imMsgListEmoji();
    }

    public ApiResult<GiftProdDto> initGift(Context context) {
        return new IMModel(context).prodGiftList(PROD_GIFT);
    }

    public static List<MyRecentContact> myRecentContactList = new ArrayList<MyRecentContact>();
    public static List<IMFirstRelation> myImRelationList = new ArrayList<IMFirstRelation>();
    public static List<IMFirstRelation> myImFirstRelationList = new ArrayList<IMFirstRelation>();

    public void reset() {
        myRecentContactList.clear();
        myImRelationList.clear();
        myImFirstRelationList.clear();
        IM_STATUS = IM_STATUS_INIT;
    }


    public void init(final ApplicationContext context) {
        reset();
//        getInstance(context);

//        StatusBarNotificationConfig config = UserPreferences.getStatusConfig();
//        config.ring = false;
//        NIMClient.updateStatusBarNotificationConfig(config);

        TEXT_COLOR_1 = context.getResources().getColor(R.color.white);
        TEXT_COLOR_2 = context.getResources().getColor(R.color.color_3E4F6C);

        managerListener = new ManagerListener() {
            @Override
            public void onInitIMContract(final List<RecentContact> recents) {
                System.out.println("onInitIMContract");

                if (recents.size() > 0) {
                    for (RecentContact rc : recents) {
                        MyRecentContact myRecentContact = new MyRecentContact();
                        myRecentContact.setRecentContact(rc);

                        if (!IMManager.myRecentContactList.contains(myRecentContact))
                            myRecentContactList.add(myRecentContact);
                    }


                    new AsyncTask<Object, Void, ApiResult<IMRelationListDto>>() {


                        @Override
                        protected ApiResult<IMRelationListDto> doInBackground(Object... voids) {
                            IMModel imModel = new IMModel(context);
                            Gson gson = new Gson();
                            ArrayList<String> targetImUserIds = new ArrayList<String>();
                            for (MyRecentContact rc : myRecentContactList) {
                                targetImUserIds.add(rc.getMyTargetId());
                            }
                            RelationListRequest relationRequest = new RelationListRequest();
                            relationRequest.setTargetImUserIds(targetImUserIds);
                            return imModel.imUserRelationList(gson.toJson(relationRequest));
                        }

                        @Override
                        protected void onPostExecute(ApiResult<IMRelationListDto> result) {
                            super.onPostExecute(result);
                            if (result != null && result.getData() != null) {
                                if (result.getData().getList() != null && result.getData().getList().size() > 0) {
                                    for (MyRecentContact myRecentContact : myRecentContactList) {
                                        if (myRecentContact.getRecentContact() != null) {
                                            for (IMFirstRelation myRelation : result.getData().getList()) {
                                                if (myRelation.checkRecentContact(myRecentContact.getRecentContact().getContactId())) {
                                                    myRecentContact.setFirstRelation(myRelation);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            EventBus.getDefault().post(new ImContactListEvent(myRecentContactList));
                        }
                    }.execute();
                } else {
                    new AsyncTask<Object, Void, ApiResult<IMRelationListDto>>() {


                        @Override
                        protected ApiResult<IMRelationListDto> doInBackground(Object... voids) {
                            IMModel imModel = new IMModel(context);
                            Gson gson = new Gson();
                            return imModel.imUserRelationList(gson.toJson(new RelationListRequest()));
                        }

                        @Override
                        protected void onPostExecute(ApiResult<IMRelationListDto> result) {
                            super.onPostExecute(result);
                            if (result != null && result.getData() != null) {
                                if (result.getData().getList() != null && result.getData().getList().size() > 0) {
                                    List<IMFirstRelation> firstTimeList = result.getData().getList();
                                    for (IMFirstRelation imFirstRelation : firstTimeList) {
                                        MyRecentContact myRecentContact = new MyRecentContact();
                                        myRecentContact.setFirstRelation(imFirstRelation);
                                        if (!IMManager.myRecentContactList.contains(myRecentContact))
                                            myRecentContactList.add(myRecentContact);
                                    }
                                }
                            }
                            EventBus.getDefault().post(new ImContactListEvent(myRecentContactList));

                        }
                    }.execute();
                }
            }

            @Override
            public void onReceiveImMessage(List<IMMessage> messages) {
                System.out.println("new Msg comming!!!");
//                final IMMessage imMessage = messages.get(0);
//                for (IMFirstRelation imFirstRelation : myImRelationList) {
//                    if (imFirstRelation.getOtherUserImUid().equals(imMessage.getFromAccount())) {
//                        return;
//                    }
//                }


//                new AsyncTask<Object, Void, ApiResult<IMRelationListDto>>() {
//
//                    @Override
//                    protected ApiResult<IMRelationListDto> doInBackground(Object... voids) {
//                        IMModel imModel = new IMModel(context);
//                        Gson gson = new Gson();
//                        RelationListRequest request = new RelationListRequest();
//                        request.getTargetImUserIds().add(imMessage.getFromAccount());
//                        return imModel.imUserRelationList(gson.toJson(request));
//                    }
//
//                    @Override
//                    protected void onPostExecute(ApiResult<IMRelationListDto> result) {
//                        super.onPostExecute(result);
//                        if (result != null && result.getData() != null) {
//                            if (result.getData().getList() != null && result.getData().getList().size() > 0) {
//                                List<IMFirstRelation> firstRelationList = result.getData().getList();
//                                IMFirstRelation imFirstRelation = firstRelationList.get(0);
//                                myImRelationList.add(imFirstRelation);//有可能重复
//                            }
//                        }
//                    }
//                }.execute();
            }

            @Override
            public void onIMContractChange(List<RecentContact> recents) {
                System.out.println("onIMContractChange !!!!!!");
                final RecentContact recentContact = recents.get(0);
                boolean isNew = true;
                for (MyRecentContact myRecentContact : myRecentContactList) {
                    if (myRecentContact.getRecentContact() != null && myRecentContact.getRecentContact().getContactId().equals(recentContact.getContactId())) {
                        isNew = false;
                        System.out.println("OLD MSG!!!");
                        myRecentContact.setRecentContact(recentContact);
                        EventBus.getDefault().post(new ImContactChangeEvent(myRecentContact));
                        break;
                    }
                }

                if (isNew) {
                    final String curSendImUid = curSendGiftMsgImUid;
                    curSendGiftMsgImUid = null;

                    final MyRecentContact myRecentContact = new MyRecentContact();
                    myRecentContact.setRecentContact(recentContact);
                    if (!IMManager.myRecentContactList.contains(myRecentContact))
                        myRecentContactList.add(myRecentContact);

                    new AsyncTask<Object, Void, ApiResult<IMRelationListDto>>() {
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                        }

                        @Override
                        protected ApiResult<IMRelationListDto> doInBackground(Object... voids) {
                            IMModel imModel = new IMModel(context);
                            Gson gson = new Gson();
                            RelationListRequest request = new RelationListRequest();
//                            request.getTargetImUserIds().add(recentContact.getFromAccount());
                            request.getTargetImUserIds().add(recentContact.getContactId());
                            if (curSendImUid != null && !recentContact.getContactId().equals(curSendImUid))
                                request.getTargetImUserIds().add(curSendImUid);
                            return imModel.imUserRelationList(gson.toJson(request));
                        }

                        @Override
                        protected void onPostExecute(ApiResult<IMRelationListDto> result) {
                            super.onPostExecute(result);
                            if (result != null && result.getData() != null) {
                                if (result.getData().getList() != null && result.getData().getList().size() > 0) {
                                    List<IMFirstRelation> firstRelationList = result.getData().getList();
                                    IMFirstRelation imFirstRelation = firstRelationList.get(0);
                                    myImRelationList.add(imFirstRelation);

                                    myRecentContact.setFirstRelation(imFirstRelation);

                                    if (myRecentContact != null)
                                        EventBus.getDefault().post(new ImContactChangeEvent(myRecentContact));
                                }
                            }
                        }
                    }.execute();
                }
            }

            @Override
            public void onMessageSendRlt(IMessage.MessageStatus status, MyMessage myMessage) {
                if (IMManager.getInstance().adapter != null) {
                    myMessage.setMessageStatus(status);
                    IMManager.getInstance().adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onLoginRlt(int rlt) {
//                if (pageListener != null)
//                    pageListener.onLoginRlt(rlt);
            }
        };
        MANAGER.minFunctional = new NIMFunctional(context, null, managerListener);
    }

    public IMManager(ApplicationContext context) {
//        imFunctionalInterface = new HuanXinFunctional(context);
    }

    public void login(IMUser imUser, Context context) {
        if (!IM_STATUS.equals(IM_STATUS_LOGIN) && !IM_STATUS.equals(IM_STATUS_DOING_LOGIN)) {
            IM_STATUS = IM_STATUS_DOING_LOGIN;
            if (imUser == null) {
                getImToken(context);
            } else {
                MANAGER.minFunctional.login(imUser);
            }
        }
    }

    public void getImToken(final Context context) {
        new AsyncTask<Object, Void, ApiResult<IMUser>>() {


            @Override
            protected ApiResult<IMUser> doInBackground(Object... voids) {
                IMModel imModel = new IMModel(context);
                return imModel.imUserGettoken();
            }

            @Override
            protected void onPostExecute(ApiResult<IMUser> result) {
                super.onPostExecute(result);
                if (result != null && result.getData() != null) {
                    IMUser imUser = result.getData();
                    HereUser.getInstance().setImUserInfo(imUser);
                    MANAGER.minFunctional.login(imUser);
                } else {
                    IM_STATUS = IM_STATUS_INIT;
                }
            }
        }.execute();
    }

    public void refreshToken(final Context context) {
        new AsyncTask<Object, Void, ApiResult<IMUser>>() {


            @Override
            protected ApiResult<IMUser> doInBackground(Object... voids) {
                IMModel imModel = new IMModel(context);
                return imModel.imRefreshToken();
            }

            @Override
            protected void onPostExecute(ApiResult<IMUser> result) {
                super.onPostExecute(result);
                if (result != null && result.getData() != null) {
                    IMUser imUser = result.getData();
                    HereUser.getInstance().setImUserInfo(imUser);
                    MANAGER.minFunctional.login(imUser);
                } else {
                    IM_STATUS = IM_STATUS_INIT;
                }
            }
        }.execute();
    }

    public void logout() {
        MANAGER.minFunctional.logout();
    }


    public void startConversationWithCallBack(final Context context, final String uid, final ConversationListener listener) {
        checkRelation(context, uid, listener);
    }

    public void checkRelation(final Context context, final String uid, final ConversationListener listener) {
        new AsyncTask<Object, Void, ApiResult<IMUserPrerelation>>() {


            @Override
            protected ApiResult<IMUserPrerelation> doInBackground(Object... voids) {
                IMModel imModel = new IMModel(context);
                return imModel.imUserPrerelation(uid);
            }

            @Override
            protected void onPostExecute(ApiResult<IMUserPrerelation> result) {
                super.onPostExecute(result);
                if (result != null && result.getData() != null) {
                    IMUserPrerelation imUserPrerelation = result.getData();
                    if (listener != null) {
                        listener.onUserRelation(imUserPrerelation);
                    }
                }
            }
        }.execute();
    }

    public static boolean checkInFirstRelation (MyRecentContact recentContact) {
        if (myImFirstRelationList != null) {
            for (IMFirstRelation firstRelation : myImFirstRelationList) {
                if (firstRelation.getFromImUid().equals(recentContact.getMyTargetId())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void removeFirstRelation (String targetUid) {
        if (myImFirstRelationList != null) {
            for (IMFirstRelation firstRelation : myImFirstRelationList) {
                if (firstRelation.getFromImUid().equals(targetUid)) {
                    myImFirstRelationList.remove(firstRelation);
                    pageListener.msgReply(targetUid);
//                    pageListener.refreshMsgAdapter();
                    return ;
                }
            }
        }
    }

    public List<IMMessage> queryMessageById(List<String> ids) {
        return MANAGER.minFunctional.queryMessageById(ids);
    }

    public void getHistory(IMMessage msgAnchor) {
        MANAGER.minFunctional.getHistoryMsg(msgAnchor);
    }

    private MsgListAdapter currentAdapter;

    public void sendGiftMsg(String targetUid, GiftMessage giftMessage) {
        removeFirstRelation(targetUid);
        curSendGiftMsgImUid = targetUid;
        MANAGER.minFunctional.sendGiftMsg(targetUid, giftMessage, false);
    }

    public void reSendGiftMsg(String targetUid, GiftMessage giftMessage) {
        removeFirstRelation(targetUid);
        curSendGiftMsgImUid = targetUid;
        MANAGER.minFunctional.sendGiftMsg(targetUid, giftMessage, true);
    }

    public void sendEmoji(String targetUid, EmojiMessage emojiMessage) {
        removeFirstRelation(targetUid);
        MANAGER.minFunctional.sendEmojiMsg(targetUid, emojiMessage, false);
    }

    public void reSendEmoji(String targetUid, EmojiMessage emojiMessage) {
        removeFirstRelation(targetUid);
        MANAGER.minFunctional.sendEmojiMsg(targetUid, emojiMessage, true);
    }

    public IMMessage saveTxt(String targetUid, MyMessage myMessage, IMessage.MessageStatus status) {
        System.out.println("######send" + myMessage + ";;;;" + myMessage.getId());
        return MANAGER.minFunctional.saveTxtMsg(targetUid, myMessage, status);
    }

    public void sendTxt(String targetUid, MyMessage myMessage) {
        removeFirstRelation(targetUid);
        System.out.println("######send" + myMessage + ";;;;" + myMessage.getId());
        MANAGER.minFunctional.sendTxtMsg(targetUid, myMessage, false);
    }

    public void reSendTxt(String targetUid, MyMessage myMessage) {
        removeFirstRelation(targetUid);
        System.out.println("######resend" + myMessage + ";;;;" + myMessage.getId());
        MANAGER.minFunctional.sendTxtMsg(targetUid, myMessage, true);
    }

    public static void clearMsgCount(String account, SessionTypeEnum sessionType) {
        NIMClient.getService(MsgService.class).clearUnreadCount(account, sessionType);
    }

//    public ArrayList<MyRecentContact> initMyRecentContact (Context context) {
//
//    }

    public static MyRecentContact findMyRecentContact (String imId) {
        for (MyRecentContact myRecentContact : myRecentContactList) {
            if (myRecentContact.getMyTargetId().equals(imId))
                return myRecentContact;
        }
        return null;
    }

    public static void deleteRecentContact (String imId) {
        MyRecentContact myRecentContact = findMyRecentContact(imId);
        if (myRecentContact != null) {
            myRecentContact.setMyDelete();
            myRecentContactList.remove(myRecentContact);
        }

//        IMManager.clearMsgCount(imId, SessionTypeEnum.P2P);

        MANAGER.minFunctional.deleteContact(imId);
    }

    public void clearMetaMsg () {
        ArrayList<MyRecentContact> removeList = new ArrayList<MyRecentContact>();
        for (MyRecentContact recentContact : IMManager.myRecentContactList) {
            if (recentContact.getMsgMetaDto() != null) {
                removeList.add(recentContact);
            }
        }

        IMManager.myRecentContactList.removeAll(removeList);
    }

    public static void modifyUserInfo (UserInfo userInfo) {//头像、昵称、出生日期、个性签名、性别
        Map<UserInfoFieldEnum, Object> fields = new HashMap<>(1);
        fields.put(UserInfoFieldEnum.AVATAR, userInfo.getMyAvatar());
        fields.put(UserInfoFieldEnum.Name, userInfo.getMyNickname());
        fields.put(UserInfoFieldEnum.BIRTHDAY, userInfo.getBirthdate());
        fields.put(UserInfoFieldEnum.SIGNATURE, userInfo.getMonologue());
        fields.put(UserInfoFieldEnum.GENDER, userInfo.getSex());

        NIMClient.getService(UserService.class).updateUserInfo(fields)
                .setCallback(new RequestCallbackWrapper<Void>() {
                    @Override
                    public void onResult(int code, Void result, Throwable exception) {

                    }
                });
    }
}
