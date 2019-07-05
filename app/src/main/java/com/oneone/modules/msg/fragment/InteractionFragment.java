package com.oneone.modules.msg.fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.oneone.R;
import com.oneone.api.constants.FollowStatus;
import com.oneone.api.constants.Gender;
import com.oneone.api.constants.RedDot;
import com.oneone.framework.ui.BasePresenterFragment;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.ibase.IPresenter;
import com.oneone.framework.ui.utils.ScreenUtil;
import com.oneone.modules.find.beans.FindCondition;
import com.oneone.modules.find.dto.FindPageUserInfoDTO;
import com.oneone.modules.find.dto.LikeListDto;
import com.oneone.modules.find.dto.LikeUnreadListDto;
import com.oneone.modules.find.dto.LikeUserDto;
import com.oneone.modules.msg.activity.LikeRelationActivity;
import com.oneone.modules.msg.adapter.MsgNotifyAdapter;
import com.oneone.modules.msg.beans.GiftProd;
import com.oneone.modules.msg.beans.IMEmoji;
import com.oneone.modules.msg.beans.IMFirstRelation;
import com.oneone.modules.msg.beans.IMUser;
import com.oneone.modules.msg.beans.IMUserPrerelation;
import com.oneone.modules.msg.beans.MsgMeta;
import com.oneone.modules.msg.beans.NotifyListItem;
import com.oneone.modules.msg.beans.TalkBeans.MyMessage;
import com.oneone.modules.msg.beans.TimeLineInfo;
import com.oneone.modules.msg.contract.MsgContract;
import com.oneone.modules.msg.dto.MsgMetaDto;
import com.oneone.modules.msg.presenter.MsgPresenter;
import com.oneone.modules.msg.widget.NoScrollListView;
import com.oneone.modules.msg.widget.UserRelationPhotoLayout;
import com.oneone.modules.profile.contract.ProfileContract;
import com.oneone.modules.reddot.RedDotManager;
import com.oneone.modules.timeline.activity.TimeLinePostActivity;
import com.oneone.modules.user.HereUser;
import com.oneone.restful.ApiResult;
import com.oneone.utils.GenderUtil;
import com.oneone.widget.CustomGlobalHeader;
import com.oneone.widget.EmptyView4Common;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * Created by here on 18/4/27.
 */

@LayoutResource(R.layout.frag_interaction)
public class InteractionFragment extends BasePresenterFragment<MsgPresenter, MsgContract.View> implements MsgContract.View, View.OnClickListener {
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.interaction_like_title_tv)
    TextView likeTitleTv;
    @BindView(R.id.interaction_like_title_increase_tv)
    TextView likeIncreaseTv;
    @BindView(R.id.interaction_like_title_total_info_see_all_tv)
    TextView likeSeeAllTv;
    @BindView(R.id.interaction_like_title_total_info_logo_iv)
    ImageView likeLogoIv;

    @BindView(R.id.like_each_other_user_list_outer_layout)
    LinearLayout likeEachotherUserListOuterLayout;
    @BindView(R.id.like_each_other_user_list_layout)
    LinearLayout likeEachotherUserListLayout;
    @BindView(R.id.like_each_other_user_list_content_layout)
    LinearLayout likeEachotherUserListContentLayout;
    @BindView(R.id.like_eachother_see_all_text)
    TextView eachotherSeeAllTv;
    @BindView(R.id.like_me_layout)
    RelativeLayout likeMeLayout;
    @BindView(R.id.like_me_tv)
    TextView likeMeTv;

    @BindView(R.id.notify_title_tv)
    TextView notifyTitleTv;
    @BindView(R.id.notify_layout)
    LinearLayout notifyLayout;

    private LayoutInflater inflater;

    private static final int PAGE_COUNT = 10;
    private long lastNotifyTime;
    private long lastReadTime = -1;
    private List<NotifyListItem> notifyListItemList = new ArrayList<NotifyListItem>();
    MsgNotifyAdapter adapter;

    @BindView(R.id.nodata_layout)
    RelativeLayout emptyLayout;
    @BindView(R.id.no_data_iv)
    LottieAnimationView animView;
    @BindView(R.id.no_data_tv)
    TextView noDataTv;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();

        initPageData();

        setListener();

        refreshList();
    }

    private void refreshList () {
        RedDotManager.getInstance().fetchAllDot();

        notifyListItemList.clear();
        lastNotifyTime = System.currentTimeMillis();
        lastReadTime = -1;
//        if (adapter != null)
//            adapter.refreshList();

        mPresenter.getNotifyList(true, lastNotifyTime, PAGE_COUNT, lastReadTime);
    }

    public void setListener () {
        likeSeeAllTv.setOnClickListener(this);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshList();
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPresenter.getNotifyList(false, lastNotifyTime, PAGE_COUNT, lastReadTime);
            }
        });
        ((CustomGlobalHeader) smartRefreshLayout.getRefreshHeader()).setBackgroundColor(getResources().getColor(R.color.blue));
    }

    public void showLikeMe (int likeMeCount) {
        if (likeIncreaseTv != null) {
            if (likeMeCount > 0) {
                likeIncreaseTv.setVisibility(View.VISIBLE);
                likeIncreaseTv.setText("+" + likeMeCount);
            } else {
                likeIncreaseTv.setVisibility(View.GONE);
            }
        }
    }

    public void initLikeMe () {
        int likeMeCount = RedDotManager.getInstance().getLikeMeUnread();
        if (likeMeLayout != null) {
            if (likeMeCount > 0) {
                likeMeLayout.setVisibility(View.VISIBLE);
                likeLogoIv.setBackgroundResource(R.drawable.msg_talk_interaction_title_img_bg);
                likeMeTv.setText(likeMeCount + getString(R.string.str_my_msg_interaction_like_me_text));
                likeMeLayout.setTag(true);
                likeMeLayout.setOnClickListener(this);
            } else {
                likeMeLayout.setTag(false);
                likeMeLayout.setVisibility(View.GONE);
            }

            showLikeMe(likeMeCount);
        }
    }

    public void showNoLike () {
        int likeMeCount = RedDotManager.getInstance().getLikeMeUnread();
        int likeEachOtherCount = RedDotManager.getInstance().getLikeEachOtherUnread();
        if (likeMeLayout != null) {
            if ((likeMeCount + likeEachOtherCount) <= 0) {
                likeMeLayout.setTag(false);
                likeMeLayout.setVisibility(View.VISIBLE);
                likeLogoIv.setBackgroundResource(R.drawable.msg_talk_interaction_title_img_bg_2);
                likeMeTv.setText(getString(R.string.str_my_msg_interaction_not_have_like_me_text));
            } else {
                likeMeLayout.setTag(true);
            }
        }

        showLikeMe(likeMeCount);

        if (likeEachOtherCount <= 0) {
            likeEachotherUserListOuterLayout.setVisibility(View.GONE);
        } else {
            likeEachotherUserListOuterLayout.setVisibility(View.VISIBLE);
            mPresenter.likeUnread(false);
        }
    }

    public void initPageData () {
//        mPresenter.likeEachotherList(false, 0, 3, 0);
        mPresenter.likeUnread(false);
        likeMeLayout.setOnClickListener(this);

        int likeMeCount = RedDotManager.getInstance().getLikeMeUnread();
        int likeEachOtherCount = RedDotManager.getInstance().getLikeEachOtherUnread();

        if (likeMeCount > 0) {
            likeMeLayout.setTag(true);
            likeMeLayout.setVisibility(View.VISIBLE);
            likeLogoIv.setBackgroundResource(R.drawable.msg_talk_interaction_title_img_bg);
            likeMeTv.setText(likeMeCount + getString(R.string.str_my_msg_interaction_like_me_text));
        } else {
            likeMeLayout.setTag(false);
            likeMeLayout.setVisibility(View.GONE);
        }

        if ((likeMeCount + likeEachOtherCount) <= 0) {
            likeMeLayout.setVisibility(View.VISIBLE);
            likeLogoIv.setBackgroundResource(R.drawable.msg_talk_interaction_title_img_bg_2);
            likeMeTv.setText(getString(R.string.str_my_msg_interaction_not_have_like_me_text));
        }

        showLikeMe(likeMeCount);

//        if (likeEachOtherCount > 0) {
//            likeEachotherUserListOuterLayout.setVisibility(View.VISIBLE);
////            initLikeEachother(likeUnreadListDto.getNewLikeEachOtherUserList());
//        } else {
//            likeEachotherUserListOuterLayout.setVisibility(View.GONE);
//        }

//        if (likeUnreadListDto != null) {
//            boolean hasNewLike = false;
//            if (likeUnreadListDto.getLikeToMeCount() > 0) {
//                hasNewLike = true;
//                likeMeLayout.setVisibility(View.VISIBLE);
//                likeLogoIv.setBackgroundResource(R.drawable.msg_talk_interaction_title_img_bg);
//                likeMeTv.setText(likeUnreadListDto.getLikeToMeCount() + getString(R.string.str_my_msg_interaction_like_me_text));
//                likeMeLayout.setOnClickListener(this);
//            } else {
//                likeMeLayout.setVisibility(View.GONE);
//            }
//            if (likeUnreadListDto.getNewLikeEachOtherUserList() != null && likeUnreadListDto.getNewLikeEachOtherUserList().size() > 0) {
//                hasNewLike = true;
//                likeEachotherUserListOuterLayout.setVisibility(View.VISIBLE);
//                initLikeEachother(likeUnreadListDto.getNewLikeEachOtherUserList());
//            } else {
//                likeEachotherUserListOuterLayout.setVisibility(View.GONE);
//            }
//
//            if (!hasNewLike) {
//                likeMeLayout.setVisibility(View.VISIBLE);
//                likeLogoIv.setBackgroundResource(R.drawable.msg_talk_interaction_title_img_bg_2);
//                likeMeTv.setText(getString(R.string.str_my_msg_interaction_not_have_like_me_text));
//            }
//        } else {
//            likeLogoIv.setBackgroundResource(R.drawable.msg_talk_interaction_title_img_bg_2);
//            likeMeTv.setText(getString(R.string.str_my_msg_interaction_not_have_like_me_text));
//        }
    }

    public void initView () {
        inflater = LayoutInflater.from(getActivityContext());

        likeSeeAllTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);


        animView.setAnimation(EmptyView4Common.EMPTY_DEFAULT_ANIM, LottieAnimationView.CacheStrategy.Weak);
        animView.setRepeatCount(LottieDrawable.INFINITE);
        animView.playAnimation();

        noDataTv.setText(getResources().getString(R.string.str_common_no_data_text_3));

//        ArrayList<String> likeEachotherList = new ArrayList<String>();
//        for (int i = 0;i < 5;i++) {
//            likeEachotherList.add(i + "");
//        }
//        initLikeEachother(likeEachotherList);

//        ArrayList<String> strList = new ArrayList<String>();
//        for (int i = 0;i < 15;i++) {
//            strList.add("" + i);
//        }
//        MsgNotifyAdapter adapter = new MsgNotifyAdapter(getActivityContext(), strList);
//        notifyLv.setAdapter(adapter);
    }

    @Override
    public MsgPresenter onPresenterCreate() {
        return new MsgPresenter();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.interaction_like_title_total_info_see_all_tv:
                LikeRelationActivity.startActivity(getActivityContext(), LikeRelationActivity.TAB_MUTUAL);
                break;
            case R.id.like_eachother_see_all_text:
                LikeRelationActivity.startActivity(getActivityContext(), LikeRelationActivity.TAB_MUTUAL);
                break;
            case R.id.like_each_other_user_list_outer_layout:
                LikeRelationActivity.startActivity(getActivityContext(), LikeRelationActivity.TAB_FROM_ME);
                break;
            case R.id.like_me_layout:
                boolean isHasLike = (boolean) view.getTag();
                if (isHasLike) {
                    LikeRelationActivity.startActivity(getActivityContext(), LikeRelationActivity.TAB_TO_ME);
                } else {
                    TimeLinePostActivity.startPostImageTextActivity(view.getContext());
                }
                break;
        }
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

    }

    @Override
    public void onProdGiftList(int count, List<GiftProd> giftProdList) {

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
//        initLikeEachother(listDto);
    }

    @Override
    public void onLikeUnread(LikeUnreadListDto likeUnreadListDto) {
        if (likeUnreadListDto != null) {
            if (likeUnreadListDto.getNewLikeEachOtherUserList() != null && likeUnreadListDto.getNewLikeEachOtherUserList().size() > 0) {
                likeEachotherUserListOuterLayout.setVisibility(View.VISIBLE);
                initLikeEachother(likeUnreadListDto.getNewLikeEachOtherUserList());
            } else {
                likeEachotherUserListOuterLayout.setVisibility(View.GONE);
            }
            int likeToMeCount = likeUnreadListDto.getLikeToMeCount();
            if (likeToMeCount > 0) {
                likeTitleTv.setText(getResources().getString(R.string.str_my_msg_interaction_like_title_text) + likeToMeCount);
            } else {
                likeTitleTv.setText(getResources().getString(R.string.str_my_msg_interaction_like_title_text));
            }
        }
//        if (likeUnreadListDto != null) {
//            boolean hasNewLike = false;
//            if (likeUnreadListDto.getLikeToMeCount() > 0) {
//                hasNewLike = true;
//                likeMeLayout.setVisibility(View.VISIBLE);
//                likeLogoIv.setBackgroundResource(R.drawable.msg_talk_interaction_title_img_bg);
//                likeMeTv.setText(likeUnreadListDto.getLikeToMeCount() + getString(R.string.str_my_msg_interaction_like_me_text));
//                likeMeLayout.setOnClickListener(this);
//            } else {
//                likeMeLayout.setVisibility(View.GONE);
//            }
//            if (likeUnreadListDto.getNewLikeEachOtherUserList() != null && likeUnreadListDto.getNewLikeEachOtherUserList().size() > 0) {
//                hasNewLike = true;
//                likeEachotherUserListOuterLayout.setVisibility(View.VISIBLE);
//                initLikeEachother(likeUnreadListDto.getNewLikeEachOtherUserList());
//            } else {
//                likeEachotherUserListOuterLayout.setVisibility(View.GONE);
//            }
//
//            if (!hasNewLike) {
//                likeMeLayout.setVisibility(View.VISIBLE);
//                likeLogoIv.setBackgroundResource(R.drawable.msg_talk_interaction_title_img_bg_2);
//                likeMeTv.setText(getString(R.string.str_my_msg_interaction_not_have_like_me_text));
//            }
//        } else {
//            likeLogoIv.setBackgroundResource(R.drawable.msg_talk_interaction_title_img_bg_2);
//            likeMeTv.setText(getString(R.string.str_my_msg_interaction_not_have_like_me_text));
//        }
    }

    @Override
    public void onGetNotifyList(boolean isRefresh, int count, List<TimeLineInfo> timelineList, List<NotifyListItem> list, long lastReadTime) {
        if (list != null && list.size() > 0) {
            Collections.sort(list);
            NotifyListItem lastNotifyItem = list.get(list.size() - 1);
            this.lastNotifyTime = lastNotifyItem.getNotifyTime();
            this.lastReadTime = lastReadTime;

            initNotifyListItem(list, timelineList);

            notifyListItemList.addAll(list);

            if (adapter == null) {
                adapter = new MsgNotifyAdapter(getActivityContext(), notifyLayout, notifyTitleTv, emptyLayout, notifyListItemList, new MsgNotifyAdapter.MsgNotifyAdapterListener() {
                    @Override
                    public void onFollowBtnClick(final MsgNotifyAdapter.AttentionViewHolder holder) {
                        final NotifyListItem notifyListItem = holder.curNofityListItem;
                        switch (notifyListItem.getNotifyBody().getFollowStatus()) {
                            case FollowStatus.STATUS_NO_FOLLOW:
                            case FollowStatus.STATUS_FOLLOW_ME:
                                mPresenter.follow(notifyListItem.getFromUserInfo().getUserId(), new ProfileContract.OnFollowListener() {
                                    @Override
                                    public void onFollow(int followStatus) {
                                        notifyListItem.getNotifyBody().setFollowStatus(followStatus);
                                        holder.userRelationBtn.setBackgroundResource(R.drawable.already_follow_bg);
                                    }
                                });
                                break;
                            case FollowStatus.STATUS_FOLLOW_YOU:
                            case FollowStatus.STATUS_FOLLOW_EACH:
                                mPresenter.unFollow(notifyListItem.getFromUserInfo().getUserId(), new ProfileContract.OnUnFollowListener() {
                                    @Override
                                    public void onUnFollow(int followStatus) {
                                        notifyListItem.getNotifyBody().setFollowStatus(followStatus);
                                        holder.userRelationBtn.setBackgroundResource(R.drawable.not_follow_bg);
                                    }
                                });
                                break;
                        }

                    }
                });
//                notifyLv.setAdapter(adapter);
            } else {
                if (isRefresh) {
                    adapter.refreshList();
                } else {
                    adapter.loadMore();
                }
            }
        } else {
            if (notifyLayout.getChildCount() > 0) {
                notifyTitleTv.setVisibility(View.VISIBLE);
                emptyLayout.setVisibility(View.VISIBLE);
            } else {
                notifyTitleTv.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.GONE);
            }
        }
        smartRefreshLayout.finishLoadMore();
        smartRefreshLayout.finishRefresh();

        if (isRefresh) {
            RedDotManager.getInstance().clearDot(RedDot.INTERACTION_NOTIFY);
        }
    }

    private void initNotifyListItem (List<NotifyListItem> notifyListItems, List<TimeLineInfo> timeLineInfos) {
        for (NotifyListItem notifyListItem: notifyListItems) {
            for (TimeLineInfo timeLineInfo : timeLineInfos) {
                if (notifyListItem.getNotifyBody().getTimelineId() == timeLineInfo.getTimelineId()) {
                    notifyListItem.setTimeLineInfo(timeLineInfo);
                    continue;
                }
            }
        }
    }

    public void initLikeEachother (List<LikeUserDto> listDto) {
        likeEachotherUserListContentLayout.removeAllViews();
//            List<LikeUserDto> likeList = listDto.getLikeList();
        for (int i = 0; i < listDto.size(); i++) {
            LikeUserDto likeUserDto = listDto.get(i);
            View convertView = inflater.inflate(R.layout.item_like_relation_user_list, null);
            RelativeLayout userRelationPhotoLayout = convertView.findViewById(R.id.user_relation_photo_layout);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) userRelationPhotoLayout.getLayoutParams();
            params.width = (int) (83 * ScreenUtil.WIDTH_RATIO);
            params.height = (int) (48 * ScreenUtil.WIDTH_RATIO);
            UserRelationPhotoLayout photoLayout = new UserRelationPhotoLayout(getActivityContext());
            params = new RelativeLayout.LayoutParams((int) (83 * ScreenUtil.WIDTH_RATIO), (int) (48 * ScreenUtil.WIDTH_RATIO));
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            photoLayout.setLayoutParams(params);
            photoLayout.setLayout(HereUser.getInstance().getUserInfo(), likeUserDto.getUserInfo(), UserRelationPhotoLayout.LIKE_EACHOTHER);
            userRelationPhotoLayout.addView(photoLayout);

            TextView userNameTv = convertView.findViewById(R.id.user_name_tv);
            userNameTv.setText(likeUserDto.getUserInfo().getNickname());

            TextView userInfoTv = convertView.findViewById(R.id.user_info_tv);
            userInfoTv.setText(GenderUtil.getGender(likeUserDto.getUserInfo().getSex()) + "， " + likeUserDto.getUserInfo().getAge() + "， " + likeUserDto.getUserInfo().getCity());

            TextView likeTimeTv = convertView.findViewById(R.id.like_time_tv);
            likeTimeTv.setText(((System.currentTimeMillis() - likeUserDto.getLikeTime()) / 3600000L) + getString(R.string.str_my_msg_hours_later));

            likeEachotherUserListContentLayout.addView(convertView);
        }


        eachotherSeeAllTv.setText(getResources().getString(R.string.str_my_msg_interaction_like_eachother_see_all_left_text)
                + listDto.size() + getResources().getString(R.string.str_my_msg_interaction_like_eachother_see_all_right_text));

        eachotherSeeAllTv.setOnClickListener(this);
    }

    public static int getBubbleCount () {
        return 0;
    }
}
