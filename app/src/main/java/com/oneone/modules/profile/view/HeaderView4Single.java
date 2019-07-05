package com.oneone.modules.profile.view;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.oneone.R;
import com.oneone.api.constants.FollowStatus;
import com.oneone.api.constants.LikeStatus;
import com.oneone.api.constants.NoFeelStatus;
import com.oneone.api.constants.Role;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.dialog.SheetDialog;
import com.oneone.framework.ui.dialog.SheetItem;
import com.oneone.framework.ui.dialog.WarnDialog;
import com.oneone.framework.ui.utils.Res;
import com.oneone.framework.ui.utils.ScreenUtil;
import com.oneone.modules.find.adapter.DiscoverSuggestUserAdapter;
import com.oneone.modules.find.dto.FindPageUserInfoDTO;
import com.oneone.modules.msg.IMManager;
import com.oneone.modules.msg.activity.ImTalkActivity;
import com.oneone.modules.msg.beans.IMUserPrerelation;
import com.oneone.modules.profile.activity.IntersectionActivity;
import com.oneone.modules.profile.activity.MatchActivity;
import com.oneone.modules.profile.contract.ProfileContract;
import com.oneone.modules.profile.dialog.LikeAlreadyDialog;
import com.oneone.modules.profile.dialog.LoveDialog;
import com.oneone.modules.profile.presenter.ProfilePresenter;
import com.oneone.modules.qa.activity.MyQaMustActivity;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.modules.user.bean.UserInfoBase;
import com.oneone.modules.user.util.HereUserUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author qingfei.chen
 * @since 2018/5/12.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@LayoutResource(R.layout.layout_profile_header_view_4_single)
public class HeaderView4Single extends AbstractProfileView implements
        ProfileContract.OnFollowListener, ProfileContract.OnUnFollowListener
        , ProfileContract.OnLikeListener, ProfileContract.OnLikeCancelListener
        , ProfileContract.OnNoFeelListener, ProfileContract.OnNoFeelCancelListener {
    static boolean isFirstNoFeel = true;
    @BindView(R.id.layout_profile_header_view_4_single_ll_like_and_feel)
    ViewGroup mLLLikeAndFeel;
    @BindView(R.id.layout_profile_header_view_4_single_iv_feel)
    ImageView mIvNoFeel;
    @BindView(R.id.layout_profile_header_view_4_single_iv_like)
    LottieAnimationView mIvLike;

    @BindView(R.id.layout_profile_header_view_4_single_rl_profile)
    ViewGroup mRLProfile;

    @BindView(R.id.layout_profile_header_view_4_single_tv_name)
    TextView mTvName;
    @BindView(R.id.layout_profile_header_view_4_single_tv_summary)
    TextView mTvSummary;

    @BindView(R.id.layout_profile_header_view_4_single_ll_flow_and_chat)
    ViewGroup mLLFlowAndChat;
    @BindView(R.id.layout_profile_header_view_4_single_tv_flow)
    TextView mTvFollow;
    @BindView(R.id.layout_profile_header_view_4_single_tv_chat)
    TextView mTvChat;

    @BindView(R.id.layout_profile_header_view_4_single_ll_match_and_intersection)
    ViewGroup mLLMatchAndIntersection;

    @BindView(R.id.layout_profile_header_view_4_single_ll_match)
    ViewGroup mLLMatch;
    @BindView(R.id.layout_profile_header_view_4_single_tv_match)
    TextView mTvMatch;

    @BindView(R.id.layout_profile_header_view_4_single_ll_intersection)
    ViewGroup mLLIntersection;
    @BindView(R.id.layout_profile_header_view_4_single_tv_intersection)
    TextView mTvIntersection;
    private UserInfo mUserInfo;
    private ProfilePresenter mPresenter;

    public HeaderView4Single(Context context, ProfilePresenter presenter) {
        super(context);
        this.mPresenter = presenter;
        this.mPresenter.addFollowAndUnFollowListener(this, this);
    }

    @Override
    public void onViewCreated() {
    }

    @Override
    public void bindData(UserInfo userInfo, ProfilePresenter presenter) {
        this.mUserInfo = userInfo;
        refreshMatchUIState(userInfo);
        refreshUIState(userInfo);
//        setUIState();
        mTvName.setText(userInfo.getMyNickname());
        mTvSummary.setText(HereUserUtil.getSummary(userInfo));
    }

    @OnClick(R.id.layout_profile_header_view_4_single_tv_flow)
    public void onFollowClick(View view) {
        if (mUserInfo.getFollow() == FollowStatus.STATUS_NO_FOLLOW) {
            mPresenter.follow(mUserInfo.getUserId());
        } else if (mUserInfo.getFollow() == FollowStatus.STATUS_FOLLOW_YOU) {


            String title = String.format(Res.getString(R.string.str_unfollow_tip), mUserInfo.getNickname());

            new SheetDialog(getContext(), title)
                    .addSheetItem(Res.getString(R.string.str_unfollow), 0, 0)
                    .setListener(new SheetDialog.OnSheetItemClickListener() {
                        @Override
                        public void onItemClick(SheetItem item, int id) {
                            if (id == 0) {
                                mPresenter.unFollow(mUserInfo.getUserId());
                            }
                        }
                    }).show();


        }
    }

    @OnClick(R.id.layout_profile_header_view_4_single_tv_chat)
    public void onChatClick(View view) {
        mPresenter.getView().loading("");
        IMManager.getInstance().startConversationWithCallBack(getContext(), mUserInfo.getUserId(), new IMManager.ConversationListener() {
            @Override
            public void onUserRelation(IMUserPrerelation imUserPrerelation) {
                mPresenter.getView().loadingDismiss();
                ImTalkActivity.startActivity(getContext(), imUserPrerelation, mUserInfo);
            }
        });
    }

    @OnClick(R.id.layout_profile_header_view_4_single_ll_intersection)
    public void onIntersectionClick(View view) {
        IntersectionActivity.startActivity(getContext(), mUserInfo.getUserId());
    }

    @OnClick(R.id.layout_profile_header_view_4_single_ll_match)
    public void onMatchClick(View view) {
        boolean isAnswered = false;
        boolean isMeAnswered = false;
        if (HereUser.getInstance().getUserInfo().getQaAnswer() != null) {
            isMeAnswered = HereUser.getInstance().getUserInfo().getQaAnswer().getAnswered() > 0;
        }
        if (mUserInfo.getQaAnswer() != null) {
            isAnswered = mUserInfo.getQaAnswer().getAnswered() > 0;
        }

        if (isAnswered && !isMeAnswered) {
            MyQaMustActivity.startActivity(getContext());
        } else if (!isAnswered) {
            MatchActivity.startActivity(getContext(), mUserInfo);
        }

    }

    @OnClick(R.id.layout_profile_header_view_4_single_iv_feel)
    public void onNoFeelClick(View view) {
        if (isFirstNoFeel) {
            new WarnDialog(getContext(), R.string.str_profile_no_feel_dialog_message,
                    new WarnDialog.OnPositiveClickListener() {
                        @Override
                        public void onPositiveClick() {
                            performNoFeel();
                        }
                    }).show();
            return;
        }
        performNoFeel();
    }


    @OnClick(R.id.layout_profile_header_view_4_single_iv_like)
    public void onLikeClick(View view) {
        switch (mUserInfo.getLikeStatus()) {
            case LikeStatus.STATUS_LIKE_ME:
            case LikeStatus.STATUS_NO:
                mIvLike.setAnimation(DiscoverSuggestUserAdapter.ANIMATION_LIKE, LottieAnimationView.CacheStrategy.Weak);
                mIvLike.playAnimation();
                mIvLike.setSpeed(1.5f);
                mPresenter.like(mUserInfo.getUserId(), this);
                break;

            case LikeStatus.STATUS_LIKE_YOU:
            case LikeStatus.STATUS_LOVE:
                mIvLike.setImageResource(R.drawable.ic_like_normal);
                mPresenter.likeCancel(mUserInfo.getUserId(), HeaderView4Single.this);
                break;
        }
    }


    @Override
    public void onFollow(int followStatus) {
        refreshUIState(mUserInfo);
    }

    @Override
    public void onUnFollow(int followStatus) {
        refreshUIState(mUserInfo);
    }

    @Override
    public void onLike(boolean isOk) {
        if (!isOk) {
            return;
        }
        switch (mUserInfo.getLikeStatus()) {
            case LikeStatus.STATUS_LIKE_ME:
                //相互心动
                mUserInfo.setLikeStatus(LikeStatus.STATUS_LOVE);
                new LoveDialog(getContext(), mUserInfo).show();
                break;

            case LikeStatus.STATUS_NO:
                //单向心动对方
                mUserInfo.setLikeStatus(LikeStatus.STATUS_LIKE_YOU);
                new LikeAlreadyDialog(getContext(), new DiscoverSuggestUserAdapter.DiscoverSuggestUserAdapterListener() {
                    @Override
                    public void onRefreshAdapterClick() {

                    }

                    @Override
                    public void onNoFeelBtnClick(FindPageUserInfoDTO findPageUserInfo) {

                    }

                    @Override
                    public void onLikeBtnClick(FindPageUserInfoDTO findPageUserInfo) {

                    }

                    @Override
                    public void onCancelLikeBtnClick(FindPageUserInfoDTO findPageUserInfo) {

                    }

                    @Override
                    public void onGoQaBtnClick() {

                    }

                    @Override
                    public void onReloadAdapterClick() {

                    }

                    @Override
                    public void onItemClick(UserInfo userInfo) {

                    }

                    @Override
                    public void onOpenConvertionClick(final UserInfo mUserInfo) {
                        IMManager.getInstance().startConversationWithCallBack(getContext(), mUserInfo.getUserId(), new IMManager.ConversationListener() {
                            @Override
                            public void onUserRelation(IMUserPrerelation imUserPrerelation) {
                                ImTalkActivity.startActivity(getContext(), imUserPrerelation, mUserInfo);
                            }
                        });
                    }

                    @Override
                    public void reBuildAdapter(Fragment fragment, ArrayList<View> viewList, int expire) {

                    }
                }, mUserInfo).show();
                break;
        }
        refreshLikeState();
    }

    @Override
    public void onLikeCancel(boolean isOk) {
        if (!isOk) {
            return;
        }
        switch (mUserInfo.getLikeStatus()) {
            case LikeStatus.STATUS_LIKE_YOU:
                mUserInfo.setLikeStatus(LikeStatus.STATUS_NO);
                break;

            case LikeStatus.STATUS_LOVE:
                mUserInfo.setLikeStatus(LikeStatus.STATUS_LIKE_ME);
                break;
        }
        refreshLikeState();
    }

    @Override
    public void onNoFeel(boolean isOk) {
        if (!isOk) {
            return;
        }
        switch (mUserInfo.getNoFeelStatus()) {
            case NoFeelStatus.STATUS_HATE_ME:
                mUserInfo.setNoFeelStatus(NoFeelStatus.STATUS_EACH_HATE);
                break;

            case NoFeelStatus.STATUS_NO:
                mUserInfo.setNoFeelStatus(NoFeelStatus.STATUS_HATE_YOU);
                break;
        }
        refreshNoFeelState();
        ((Activity) getContext()).finish();
    }

    @Override
    public void onNoFeelCancel(boolean isOk) {
        if (!isOk) {
            return;
        }
        switch (mUserInfo.getNoFeelStatus()) {
            case NoFeelStatus.STATUS_HATE_YOU:
                mUserInfo.setNoFeelStatus(NoFeelStatus.STATUS_NO);
                break;
            case NoFeelStatus.STATUS_EACH_HATE:
                mUserInfo.setNoFeelStatus(NoFeelStatus.STATUS_HATE_ME);
                break;
        }
        refreshNoFeelState();
    }

    private void refreshUIState(UserInfoBase userInfo) {
        if (isMySelf(userInfo)) {
            mLLLikeAndFeel.setVisibility(GONE);
            mLLFlowAndChat.setVisibility(GONE);
            mLLMatchAndIntersection.setVisibility(GONE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRLProfile.getLayoutParams();
            params.topMargin = ScreenUtil.dip2px(25);
            mRLProfile.setLayoutParams(params);
        } else {
            refreshChatState();
            refreshFollowState();
            if (isMatcherMe()) {
                mLLLikeAndFeel.setVisibility(GONE);
                mLLMatchAndIntersection.setVisibility(GONE);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRLProfile.getLayoutParams();
                params.topMargin = ScreenUtil.dip2px(25);
                mRLProfile.setLayoutParams(params);
                return;
            }

            mLLLikeAndFeel.setVisibility(VISIBLE);
            mLLFlowAndChat.setVisibility(VISIBLE);
            mLLMatchAndIntersection.setVisibility(VISIBLE);
            refreshLikeState();
            refreshNoFeelState();
        }
    }

    private void refreshMatchUIState(UserInfo userInfo) {
        mTvMatch.setText(Res.getString(R.string.str_match) + (int) userInfo.getMatchValue() + "%");
        mTvIntersection.setText(userInfo.getIntersectionValue() + Res.getString(R.string.str_intersection));
        boolean isAnswered = false;
        boolean isMeAnswered = false;
        if (HereUser.getInstance().getUserInfo().getQaAnswer() != null) {
            isMeAnswered = HereUser.getInstance().getUserInfo().getQaAnswer().getAnswered() > 0;
        }
        if (userInfo.getQaAnswer() != null) {
            isAnswered = userInfo.getQaAnswer().getAnswered() > 0;
        }
        if (isAnswered && !isMeAnswered) {
            mTvMatch.setText(Res.getString(R.string.str_match) + "?");
            mLLMatch.setVisibility(View.VISIBLE);
        } else if (!isAnswered) {
            mLLMatch.setVisibility(View.GONE);
        } else {
            if (userInfo.getMatchValue() <= 0) {
                mLLMatch.setVisibility(View.GONE);
            } else {
                mLLMatch.setVisibility(View.VISIBLE);
            }
        }


        if (userInfo.getIntersectionValue() <= 0) {
            mLLIntersection.setVisibility(View.GONE);
        } else {
            mLLIntersection.setVisibility(View.VISIBLE);
        }
        requestLayout();
    }

    private void refreshFollowState() {
        if (mUserInfo.getFollow() == FollowStatus.STATUS_NO_FOLLOW) {
            mTvFollow.setText(R.string.str_follow);
            mTvFollow.setSelected(false);
            mTvFollow.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_add_follow, 0, 0, 0);

        } else {
            mTvFollow.setText(R.string.str_had_follow);
            mTvFollow.setSelected(true);
            mTvFollow.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
        }
    }

    private void refreshChatState() {
        if (mUserInfo.getFollow() == FollowStatus.STATUS_NO_FOLLOW || isMatcherMe()) {
            mTvChat.setVisibility(GONE);
        } else {
            mTvChat.setVisibility(VISIBLE);
        }
    }

    private void refreshLikeState() {
        mIvLike.setSelected(mUserInfo.getLikeStatus() == LikeStatus.STATUS_LIKE_YOU
                || mUserInfo.getLikeStatus() == LikeStatus.STATUS_LOVE);
    }

    private void refreshNoFeelState() {
        mIvNoFeel.setSelected(mUserInfo.getNoFeelStatus() == NoFeelStatus.STATUS_HATE_YOU
                || mUserInfo.getNoFeelStatus() == NoFeelStatus.STATUS_EACH_HATE);
    }

    private boolean isMatcherMe() {
        UserInfo info = HereUser.getInstance().getUserInfo();
        return info.getRole() == Role.MATCHER;
    }

    private boolean isMySelf(UserInfoBase userInfo) {
        return TextUtils.equals(HereUser.getUserId(), userInfo.getUserId());
    }

    private void performNoFeel() {
        switch (mUserInfo.getNoFeelStatus()) {
            case NoFeelStatus.STATUS_HATE_YOU:
            case NoFeelStatus.STATUS_EACH_HATE:
                mPresenter.noFeelCancel(mUserInfo.getUserId(), this);
                break;

            case NoFeelStatus.STATUS_HATE_ME:
            case LikeStatus.STATUS_NO:
                mPresenter.noFeel(mUserInfo.getUserId(), this);
                break;
        }
    }
}
