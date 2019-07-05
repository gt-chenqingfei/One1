package com.oneone.modules.msg.fragment;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oneone.OneOne;
import com.oneone.R;
import com.oneone.api.constants.RedDot;
import com.oneone.event.EventRefreshMsgBubble;
import com.oneone.event.EventTimelineDelete;
import com.oneone.framework.ui.BasePresenterFragment;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.utils.ScreenUtil;
import com.oneone.modules.find.beans.FindCondition;
import com.oneone.modules.find.dto.FindPageUserInfoDTO;
import com.oneone.modules.find.dto.LikeListDto;
import com.oneone.modules.find.dto.LikeUnreadListDto;
import com.oneone.modules.msg.IMManager;
import com.oneone.modules.msg.activity.ClientAndSystemMsgActivity;
import com.oneone.modules.msg.activity.FirstTimeMeetActivity;
import com.oneone.modules.msg.activity.ImTalkActivity;
import com.oneone.modules.msg.adapter.TalkMsgAdapter;
import com.oneone.modules.msg.beans.GiftProd;
import com.oneone.modules.msg.beans.IMEmoji;
import com.oneone.modules.msg.beans.IMFirstRelation;
import com.oneone.modules.msg.beans.IMUser;
import com.oneone.modules.msg.beans.IMUserPrerelation;
import com.oneone.modules.msg.beans.MsgMeta;
import com.oneone.modules.msg.beans.NotifyListItem;
import com.oneone.modules.msg.beans.TalkBeans.MyMessage;
import com.oneone.modules.msg.beans.TalkBeans.MyRecentContact;
import com.oneone.modules.msg.beans.TalkBeans.attachment.EmojiAttachment;
import com.oneone.modules.msg.beans.TalkBeans.attachment.GiftAttachment;
import com.oneone.modules.msg.beans.TimeLineInfo;
import com.oneone.modules.msg.contract.MsgContract;
import com.oneone.modules.msg.dto.MsgMetaDto;
import com.oneone.modules.msg.event.ImContactChangeEvent;
import com.oneone.modules.msg.event.ImContactListEvent;
import com.oneone.modules.msg.presenter.MsgPresenter;
import com.oneone.modules.msg.widget.NoScrollListView;
import com.oneone.modules.msg.widget.UserRelationPhotoLayout;
import com.oneone.modules.reddot.RedDotManager;
import com.oneone.modules.timeline.activity.TimeLinePostActivity;
import com.oneone.modules.user.HereUser;
import com.oneone.restful.ApiResult;
import com.oneone.widget.CustomGlobalHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by here on 18/4/27.
 */

@LayoutResource(R.layout.frag_talk)
public class TalkFragment extends BasePresenterFragment<MsgPresenter, MsgContract.View> implements MsgContract.View, View.OnClickListener {
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.open_msg_layout)
    RelativeLayout openMsgLayout;
    @BindView(R.id.open_authority_tv)
    TextView openAuthorityTv;
    @BindView(R.id.close_open_msg_layout_btn)
    Button closeMsgBtn;

    @BindView(R.id.first_time_meet_tv)
    TextView firstTimeMeetTv;
    @BindView(R.id.first_time_meet_increast_tv)
    TextView firstTimeIncreaseTv;

    @BindView(R.id.first_time_meet_total_info_see_all_tv)
    TextView firstTimeMeetTotalInfoSeeAllTv;
    @BindView(R.id.first_time_meet_user_layout)
    LinearLayout firstTimeMeetUserLayout;

    @BindView(R.id.first_time_meet_total_info_logo_iv)
    ImageView firstTimeMeetTotalInfoLogoIv;
    @BindView(R.id.talk_total_info_layout)
    RelativeLayout talkTotalInfoLayout;
    @BindView(R.id.no_meet_user_layout)
    RelativeLayout noMeetUserLayout;

    @BindView(R.id.talk_msg_layout)
    LinearLayout talkMsgLayout;

    private static final int FIRST_RELATION_LIMIT_COUNT = 3;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);
//        talkMsgLv.setFocusable(false);
//        NotificationManagerCompat manager = NotificationManagerCompat.from(getContext());
//        OneOne.NOTIFIATION_OPEN = manager.areNotificationsEnabled();

        initView();

        setListener();

        adapter = new TalkMsgAdapter(getActivityContext(), talkMsgLayout, new TalkMsgAdapter.TalkMsgAdapterListener() {
            @Override
            public void onContactDelete(MyRecentContact myRecentContact) {
//                myRecentContact.setMyDelete();
                IMManager.deleteRecentContact(myRecentContact.getMyTargetId());
//                adapter.clearMap();
                adapter.resetView();
                mPresenter.imUserDelrealation(myRecentContact.getMyTargetId());
            }

            @Override
            public void onSystenMsgDelete() {

            }
        });

        mPresenter.msgNoteLastmeta();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter = null;
        EventBus.getDefault().unregister(this);
    }

    public void initData() {

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ImContactChangeEvent imContactChangeEvent) {
        System.out.println("TalkFragment contact change!!!!!!!!!!!");
        MyRecentContact myRecentContact = imContactChangeEvent.getMyRecentContact();
//
//        if (myRecentContact.getRecentContact() != null) {
//            Map<String, Object> map = myRecentContact.getRecentContact().getExtension();
//            if (map != null) {
//                for (String key : map.keySet()) {
//                    System.out.println(key);
//                    System.out.println(map.get(key));
//                    System.out.println(map.get(key).getClass());
//                }
//            }
//        }

//        mPresenter.imUserRelationList(false, myRecentContact.getMyTargetId());
        boolean isChange = false;

        if (adapter == null) {
            adapter = new TalkMsgAdapter(getActivityContext(), talkMsgLayout, new TalkMsgAdapter.TalkMsgAdapterListener() {
                @Override
                public void onContactDelete(MyRecentContact myRecentContact) {
//                myRecentContact.setMyDelete();
                    IMManager.deleteRecentContact(myRecentContact.getMyTargetId());
                    adapter.resetView();
                    mPresenter.imUserDelrealation(myRecentContact.getMyTargetId());
                }

                @Override
                public void onSystenMsgDelete() {

                }
            });
//            ArrayList<MyRecentContact> contractArrayList = new ArrayList<MyRecentContact>();
//            contractArrayList.add(myRecentContact);
//            adapter = new TalkMsgAdapter(getActivityContext());
//            talkMsgLv.setAdapter(adapter);
        } else {
            if (myRecentContact != null)
                adapter.modifyItem(myRecentContact);
        }

        mPresenter.imUserFirstRelationList(true, MsgPresenter.FIRST_RELATION_LIST_TYPE_TALK_PAGE, 0, FIRST_RELATION_LIMIT_COUNT);

        EventBus.getDefault().post(new EventRefreshMsgBubble());
    }

    //    List<MyRecentContact> contactList;
    TalkMsgAdapter adapter;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ImContactListEvent contactListEvent) {
        if (adapter != null) {
            adapter.resetView();
        }
        mPresenter.imUserFirstRelationList(false, MsgPresenter.FIRST_RELATION_LIST_TYPE_TALK_PAGE, 0, FIRST_RELATION_LIMIT_COUNT);
//            adapter.notifyDataSetChanged();
        EventBus.getDefault().post(new EventRefreshMsgBubble());
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!isNotificationEnabled(getContext())) {
            openMsgLayout.setVisibility(View.VISIBLE);
        } else {
            openMsgLayout.setVisibility(View.GONE);
        }

        mPresenter.imUserFirstRelationList(false, MsgPresenter.FIRST_RELATION_LIST_TYPE_TALK_PAGE, 0, FIRST_RELATION_LIMIT_COUNT);

        adapter.resetView();
//        System.out.println("----->" +new JSONArray().toString());
//        Gson gson = new Gson();
//        mPresenter.imUserRelationList(false, gson.toJson(new RelationListRequest()));
//        mPresenter.msgNoteListmeta(System.currentTimeMillis() + "", 100 + "");
    }

    private IMManager.PageListener pageListener;

    public void setListener() {
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishRefresh();
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishLoadMore();
            }
        });
        ((CustomGlobalHeader) smartRefreshLayout.getRefreshHeader()).setBackgroundColor(getResources().getColor(R.color.blue));// TODO
        pageListener = new IMManager.PageListener() {
            @Override
            public void onLoginRlt(int rlt) {

            }

            @Override
            public void refreshMsgAdapter() {
                if (adapter != null)
                    adapter.resetView();
            }

            @Override
            public void msgReply(String targetUid) {
                mPresenter.imUserMsgreply(targetUid);
            }
        };
        IMManager.getInstance().registListener(pageListener);

        closeMsgBtn.setOnClickListener(this);
        firstTimeMeetTotalInfoSeeAllTv.setOnClickListener(this);
        openAuthorityTv.setOnClickListener(this);
//        talkMsgLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                TalkMsgAdapter.ViewHolder holder = (TalkMsgAdapter.ViewHolder) view.getTag();
//                if (holder.recentContact.getMsgMetaDto() == null) {
//                    adapter.modifyItem(holder.recentContact);
//                    IMUserPrerelation imUserPrerelation = new IMUserPrerelation(false, true, holder.recentContact);
//                    ImTalkActivity.startActivity(getActivityContext(), imUserPrerelation, null);
//                } else {
//                    holder.recentContact.getMsgMetaDto().setUnread(0);
//                    adapter.modifyItem(holder.recentContact);
//                    ClientAndSystemMsgActivity.startActivity(getActivityContext());
//                }
//            }
//        });

        noMeetUserLayout.setOnClickListener(this);
    }

    public void initView() {
        firstTimeMeetTotalInfoSeeAllTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) firstTimeMeetUserLayout.getLayoutParams();
        params.topMargin = -(int) (38 * ScreenUtil.WIDTH_RATIO);

        setFirstMeetIncrease();
    }

    public RelativeLayout initFirstTimeMeetUserItem(IMFirstRelation imFirstRelation) {
        MyRecentContact curRecentContact = null;
        for (MyRecentContact myRecentContact : IMManager.myRecentContactList) {
            if (myRecentContact.getFirstRelation() != null && myRecentContact.getFirstRelation().getOtherUserImUid().equals(imFirstRelation.getOtherUserImUid())) {
                curRecentContact = myRecentContact;
                break;
            } else if (myRecentContact.getRecentContact() != null && myRecentContact.getRecentContact().getContactId().equals(imFirstRelation.getOtherUserImUid())) {
                curRecentContact = myRecentContact;
                break;
            }
        }

//        if (curRecentContact == null)
//            return null;

        RelativeLayout outerLayout = new RelativeLayout(getActivityContext());
        LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams((int) (113 * ScreenUtil.WIDTH_RATIO), (int) (122 * ScreenUtil.WIDTH_RATIO));
        outerLayout.setLayoutParams(llParams);
        firstTimeMeetUserLayout.addView(outerLayout);

        RelativeLayout.LayoutParams params;

        RelativeLayout userPhotoContentLayout = new RelativeLayout(getActivityContext());
        userPhotoContentLayout.setBackgroundResource(R.drawable.shape_white_radius_4dp);
        params = new RelativeLayout.LayoutParams((int) (110 * ScreenUtil.WIDTH_RATIO), (int) (119 * ScreenUtil.WIDTH_RATIO));
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        userPhotoContentLayout.setLayoutParams(params);
        outerLayout.addView(userPhotoContentLayout);

        if (imFirstRelation.getRelationType() == 2) {
            ImageView matchTagIv = new ImageView(getActivityContext());
            params = new RelativeLayout.LayoutParams((int) (33 * ScreenUtil.WIDTH_RATIO), (int) (17 * ScreenUtil.WIDTH_RATIO));
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            matchTagIv.setLayoutParams(params);
            matchTagIv.setBackgroundResource(R.drawable.msg_talk_user_photo_like_eachother_bg);
            outerLayout.addView(matchTagIv);

            ImageView matchTagNewMsgPointIv = new ImageView(getActivityContext());
            params = new RelativeLayout.LayoutParams((int) (6 * ScreenUtil.WIDTH_RATIO), (int) (6 * ScreenUtil.WIDTH_RATIO));
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.leftMargin = (int) (29 * ScreenUtil.WIDTH_RATIO);
            params.topMargin = (int) (7 * ScreenUtil.WIDTH_RATIO);
            matchTagNewMsgPointIv.setLayoutParams(params);
            matchTagNewMsgPointIv.setBackgroundResource(R.drawable.msg_talk_user_photo_like_eachother_new_msg_point_bg);
            outerLayout.addView(matchTagNewMsgPointIv);

            if (curRecentContact != null && curRecentContact.getRecentContact() != null
                    && curRecentContact.getRecentContact().getUnreadCount() > 0) {
                matchTagNewMsgPointIv.setVisibility(View.VISIBLE);

            } else {
                matchTagNewMsgPointIv.setVisibility(View.GONE);
            }
        } else {
            ImageView matchTagNewMsgPointIv = new ImageView(getActivityContext());
            params = new RelativeLayout.LayoutParams((int) (6 * ScreenUtil.WIDTH_RATIO), (int) (6 * ScreenUtil.WIDTH_RATIO));
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.leftMargin = (int) (6.93 * ScreenUtil.WIDTH_RATIO);
            params.topMargin = (int) (7.56 * ScreenUtil.WIDTH_RATIO);
            matchTagNewMsgPointIv.setLayoutParams(params);
            matchTagNewMsgPointIv.setBackgroundResource(R.drawable.msg_talk_user_photo_like_eachother_new_msg_point_bg);
            outerLayout.addView(matchTagNewMsgPointIv);

            if (curRecentContact != null && curRecentContact.getRecentContact() != null
                    && curRecentContact.getRecentContact().getUnreadCount() > 0) {
                matchTagNewMsgPointIv.setVisibility(View.VISIBLE);

            } else {
                matchTagNewMsgPointIv.setVisibility(View.GONE);
            }
        }

        UserRelationPhotoLayout photoLayout = new UserRelationPhotoLayout(getActivityContext());
        params = new RelativeLayout.LayoutParams((int) (74 * ScreenUtil.WIDTH_RATIO), (int) (42 * ScreenUtil.WIDTH_RATIO));
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.topMargin = (int) (20 * ScreenUtil.WIDTH_RATIO);
        photoLayout.setLayoutParams(params);
        userPhotoContentLayout.addView(photoLayout);

        if (imFirstRelation.getRelationType() == 2) {
            photoLayout.setLayout(HereUser.getInstance().getUserInfo(), imFirstRelation.getUserInfo(), UserRelationPhotoLayout.LIKE_EACHOTHER);
        } else {
            photoLayout.setLayout(imFirstRelation.getUserInfo(), imFirstRelation.getGiftInfoObj().getImage(), UserRelationPhotoLayout.OTHER);
        }

        TextView nickNameTv = new TextView(getActivityContext());
        nickNameTv.setSingleLine();
        nickNameTv.setEllipsize(TextUtils.TruncateAt.END);
        nickNameTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (13 * ScreenUtil.WIDTH_RATIO));
        nickNameTv.setTextColor(getResources().getColor(R.color.blue));
        nickNameTv.setText(imFirstRelation.getUserInfo().getNickname());
        params = new RelativeLayout.LayoutParams((int) (75 * ScreenUtil.WIDTH_RATIO), RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = (int) (74 * ScreenUtil.WIDTH_RATIO);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        userPhotoContentLayout.addView(nickNameTv, params);

        TextView newMsgTv = new TextView(getActivityContext());
        newMsgTv.setSingleLine();
        newMsgTv.setEllipsize(TextUtils.TruncateAt.END);
        newMsgTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (12 * ScreenUtil.WIDTH_RATIO));
        newMsgTv.setTextColor(getResources().getColor(R.color.text_blue_1));
        if (curRecentContact != null && curRecentContact.getRecentContact() != null) {
            if (curRecentContact.getRecentContact().getAttachment() instanceof GiftAttachment) {
                GiftAttachment attachment = (GiftAttachment) curRecentContact.getRecentContact().getAttachment();
                if (curRecentContact.getRecentContact().getFromAccount().equals(HereUser.getInstance().getImUserInfo().getImUserId()))
                    newMsgTv.setText(getString(R.string.im_send_gift_front_word) + attachment.getGift().getMessage());
                else {
                    newMsgTv.setText(getString(R.string.im_receive_gift_front_word) + attachment.getGift().getMessage());
                }
            } else if (curRecentContact.getRecentContact().getAttachment() instanceof EmojiAttachment) {
                EmojiAttachment emojiAttachment = (EmojiAttachment) curRecentContact.getRecentContact().getAttachment();
                newMsgTv.setText("[" + emojiAttachment.getImEmoji().getMessage() + "]");
            } else {
                newMsgTv.setText(curRecentContact.getRecentContact().getContent());
            }
        } else {
            if (imFirstRelation.getGiftInfoObj() != null) {
                if (HereUser.getInstance().getImUserInfo() != null && imFirstRelation.getFromImUid().equals(HereUser.getInstance().getImUserInfo().getImUserId())) {
                    if (imFirstRelation.getGiftInfoObj() != null)
                        newMsgTv.setText(getString(R.string.im_send_gift_front_word) + imFirstRelation.getGiftInfoObj().getMessage());
                } else {
                    if (imFirstRelation.getGiftInfoObj() != null)
                        newMsgTv.setText(getString(R.string.im_receive_gift_front_word) + imFirstRelation.getGiftInfoObj().getMessage());
                }
            }
        }

        params = new RelativeLayout.LayoutParams((int) (85 * ScreenUtil.WIDTH_RATIO), RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = (int) (94 * ScreenUtil.WIDTH_RATIO);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        userPhotoContentLayout.addView(newMsgTv, params);

//        Button closeBtn = new Button(getActivityContext());
//        closeBtn.setBackgroundResource(R.drawable.close_bg_2);
//        params = new RelativeLayout.LayoutParams((int) (8 * ScreenUtil.WIDTH_RATIO), (int) (8 * ScreenUtil.WIDTH_RATIO));
//        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//        params.rightMargin = (int) (6 * ScreenUtil.WIDTH_RATIO);
//        params.topMargin = (int) (6 * ScreenUtil.WIDTH_RATIO);
//        userPhotoContentLayout.addView(closeBtn, params);

        if (curRecentContact == null) {
            curRecentContact = new MyRecentContact();
            curRecentContact.setFirstRelation(imFirstRelation);
        }
        outerLayout.setTag(curRecentContact);
        outerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyRecentContact myRecentContact = (MyRecentContact) view.getTag();
                IMUserPrerelation imUserPrerelation = new IMUserPrerelation(false, true, myRecentContact);
                ImTalkActivity.startActivity(getActivityContext(), imUserPrerelation, null);
            }
        });

        return outerLayout;
    }

    @Override
    public MsgPresenter onPresenterCreate() {
        return new MsgPresenter();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.open_authority_tv:
                startActivity(getAppDetailSettingIntent(getActivityContext()));
                break;
            case R.id.first_time_meet_total_info_see_all_tv:
                FirstTimeMeetActivity.startActivity(getActivityContext());
                RedDotManager.getInstance().clearDot(RedDot.NEW_MEET);
                break;
            case R.id.close_open_msg_layout_btn:
                openMsgLayout.setVisibility(View.GONE);
                break;
            case R.id.no_meet_user_layout:
                TimeLinePostActivity.startPostImageTextActivity(getContext());
                break;
        }
    }

    /**
     * 获取应用详情页面intent
     *
     * @return
     */
    private Intent getAppDetailSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        return localIntent;
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
        if (pageListener != null) {
            pageListener.refreshMsgAdapter();
        }
    }

    @Override
    public void onImUserDelrealation(ApiResult rlt) {

    }

    @Override
    public void onImUserFirstRelationList(boolean isLoadMore, int oldCount, int newCount, int count, List<IMFirstRelation> imFirstRelationList) {
        if (count > 0) {
            IMManager.myImFirstRelationList = imFirstRelationList;

//            if (newCount > 0) {
//                firstTimeIncreaseTv.setVisibility(View.VISIBLE);
//                firstTimeIncreaseTv.setText("+" + count);

//                talkTotalInfoLayout.setVisibility(View.VISIBLE);
            firstTimeMeetTotalInfoLogoIv.setBackgroundResource(R.drawable.msg_talk_first_time_meet_total_info_logo_bg);
            firstTimeMeetUserLayout.setVisibility(View.VISIBLE);
            noMeetUserLayout.setVisibility(View.GONE);
//            } else {
//                firstTimeIncreaseTv.setVisibility(View.GONE);
//
////                talkTotalInfoLayout.setVisibility(View.GONE);
//                firstTimeMeetTotalInfoLogoIv.setBackgroundResource(R.drawable.msg_talk_interaction_title_img_bg_2);
//                firstTimeMeetUserLayout.setVisibility(View.GONE);
//                noMeetUserLayout.setVisibility(View.VISIBLE);
//            }
            firstTimeMeetTv.setText(getResources().getString(R.string.str_my_msg_talk_first_time_meet_text) + count);
            int i = 0;
            firstTimeMeetUserLayout.removeAllViews();
            for (IMFirstRelation firstRelation : imFirstRelationList) {
                RelativeLayout rl = initFirstTimeMeetUserItem(firstRelation);
                if (rl != null) {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rl.getLayoutParams();
                    if (i == 0)
                        params.leftMargin = (int) (11 * ScreenUtil.WIDTH_RATIO);
                    else
                        params.leftMargin = (int) (8 * ScreenUtil.WIDTH_RATIO);
                    i++;
                }
            }

            if (adapter != null) {
                adapter.resetView();
            }
//                adapter.notifyDataSetChanged();
        } else {
//            firstTimeIncreaseTv.setVisibility(View.GONE);

//            talkTotalInfoLayout.setVisibility(View.GONE);
            firstTimeMeetTotalInfoLogoIv.setBackgroundResource(R.drawable.msg_talk_interaction_title_img_bg_2);
            firstTimeMeetUserLayout.setVisibility(View.GONE);
            noMeetUserLayout.setVisibility(View.VISIBLE);
//            noMeetUserLayout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onImUserRelationList(int count, List<IMFirstRelation> imFirstRelationList) {
        System.out.println("------>onImUserRelationList");
        if (imFirstRelationList != null && imFirstRelationList.size() > 0) {
            for (IMFirstRelation firstRelation : imFirstRelationList) {
                System.out.println(firstRelation);
            }
        }
        System.out.println("------>onImUserRelationList");
    }

    @Override
    public void onImMsgCheck(ApiResult<String> rlt, MyMessage myMessage) {

    }


    @Override
    public void onImMsgListEmoji(List<IMEmoji> imEmojiList) {

    }

    @Override
    public void onProdGiftList(int count, List<GiftProd> giftProdList) {

    }

    @Override
    public void onMsgNoteListmeta(boolean isLoadMore, List<MsgMeta> msgBeanList) {

    }

    @Override
    public void onMsgNoteLastmeta(MsgMetaDto msgMetaDto) {
        IMManager.getInstance().clearMetaMsg();

        if (msgMetaDto == null || msgMetaDto.getMsgMeta() == null) {
            msgMetaDto = new MsgMetaDto();
            MsgMeta msgMeta = new MsgMeta();
            msgMetaDto.setMsgMeta(msgMeta);
        }
        MyRecentContact myRecentContact = new MyRecentContact();
        myRecentContact.setMsgMetaDto(msgMetaDto);
        if (adapter == null) {
            if (!IMManager.myRecentContactList.contains(myRecentContact))
                IMManager.myRecentContactList.add(myRecentContact);
//                adapter = new TalkMsgAdapter(getActivityContext());
//                talkMsgLv.setAdapter(adapter);
            adapter = new TalkMsgAdapter(getActivityContext(), talkMsgLayout, new TalkMsgAdapter.TalkMsgAdapterListener() {
                @Override
                public void onContactDelete(MyRecentContact myRecentContact) {
//                myRecentContact.setMyDelete();
                    IMManager.deleteRecentContact(myRecentContact.getMyTargetId());
//                        adapter.clearMap();
                    adapter.resetView();
                    mPresenter.imUserDelrealation(myRecentContact.getMyTargetId());
                }

                @Override
                public void onSystenMsgDelete() {

                }
            });
        } else {
            adapter.modifyItem(myRecentContact);
        }


//        if (msgMetaDto != null && msgMetaDto.getMsgMeta() != null) {
//            MyRecentContact myRecentContact = new MyRecentContact();
//            myRecentContact.setMsgMetaDto(msgMetaDto);
//            if (adapter == null) {
//                if (!IMManager.myRecentContactList.contains(myRecentContact))
//                    IMManager.myRecentContactList.add(myRecentContact);
////                adapter = new TalkMsgAdapter(getActivityContext());
////                talkMsgLv.setAdapter(adapter);
//                adapter = new TalkMsgAdapter(getActivityContext(), talkMsgLayout, new TalkMsgAdapter.TalkMsgAdapterListener() {
//                    @Override
//                    public void onContactDelete(MyRecentContact myRecentContact) {
////                myRecentContact.setMyDelete();
//                        IMManager.deleteRecentContact(myRecentContact.getMyTargetId());
////                        adapter.clearMap();
//                        adapter.resetView();
//                        mPresenter.imUserDelrealation(myRecentContact.getMyTargetId());
//                    }
//
//                    @Override
//                    public void onSystenMsgDelete() {
//
//                    }
//                });
//            } else {
//                adapter.modifyItem(myRecentContact);
//            }
//        }
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

    //检查系统是否关闭app应用的通知权限
    private boolean isNotificationEnabled(Context context) {

        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;
        /* Context.APP_OPS_MANAGER */
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE,
                    Integer.TYPE, String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) ==
                    AppOpsManager.MODE_ALLOWED);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setFirstMeetIncrease() {
//        int count = RedDotManager.getInstance().getLikeEachOtherUnread();
//        if (count > 0) {
//            firstTimeIncreaseTv.setVisibility(View.VISIBLE);
////            talkTotalInfoLayout.setVisibility(View.VISIBLE);
//            firstTimeIncreaseTv.setText("+" + count);
//            noMeetUserLayout.setVisibility(View.GONE);
//        } else {
//            firstTimeIncreaseTv.setVisibility(View.GONE);
//            noMeetUserLayout.setVisibility(View.VISIBLE);
////            talkTotalInfoLayout.setVisibility(View.GONE);
//        }
//
//        count = 0;
        if (firstTimeIncreaseTv != null) {
            int count = RedDotManager.getInstance().getNewMeetUnread();
            if (count > 0) {
                firstTimeIncreaseTv.setVisibility(View.VISIBLE);
                firstTimeIncreaseTv.setText("+" + count);
            } else {
                firstTimeIncreaseTv.setVisibility(View.GONE);
            }
        }
    }

    public int getBubbleCount() {
        int count = 0;
        if (IMManager.myRecentContactList != null && IMManager.myRecentContactList.size() > 0) {
            for (MyRecentContact mc : IMManager.myRecentContactList) {
                if (mc.getRecentContact() != null) {
                    count += mc.getRecentContact().getUnreadCount();
                } else if (mc.getMsgMetaDto() != null) {
                    count += mc.getMsgMetaDto().getUnread();
                }
            }
        }

        setFirstMeetIncrease();
        return count;
    }
}
