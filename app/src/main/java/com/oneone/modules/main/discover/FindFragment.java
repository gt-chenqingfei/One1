package com.oneone.modules.main.discover;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.oneone.Constants;
import com.oneone.R;
import com.oneone.event.EventFindCountdownEnd;
import com.oneone.event.EventNoFeel;
import com.oneone.event.EventRefreshQaCountInfo;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.BaseMainFragment;
import com.oneone.framework.ui.utils.ScreenUtil;
import com.oneone.modules.find.activity.MatchPersonActivity;
import com.oneone.modules.find.adapter.DiscoverSuggestUserAdapter;
import com.oneone.modules.find.beans.FindCondition;
import com.oneone.modules.find.contract.FindContract;
import com.oneone.modules.find.dto.FindPageUserInfoDTO;
import com.oneone.modules.find.presenter.FindPresenter;
import com.oneone.modules.main.GuideActivity;
import com.oneone.modules.msg.IMManager;
import com.oneone.modules.msg.activity.ImTalkActivity;
import com.oneone.modules.msg.beans.IMUserPrerelation;
import com.oneone.modules.profile.ProfileStater;
import com.oneone.modules.qa.QaDataManager;
import com.oneone.modules.qa.activity.MyQaActivity;
import com.oneone.modules.qa.activity.MyQaMustActivity;
import com.oneone.modules.reddot.RedDotManager;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.UserSP;
import com.oneone.modules.user.bean.UserInfo;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author qingfei.chen
 * @version v1.0.0
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 * @since 2018/3/31.
 */
@LayoutResource(R.layout.frag_find)
public class FindFragment extends BaseMainFragment<FindPresenter, FindContract.View> implements FindContract.View, DiscoverSuggestUserAdapter.DiscoverSuggestUserAdapterListener {
    @BindView(R.id.discover_pager)
    ViewPager pager;
    @BindView(R.id.frag_find_test_view)
    View titleView;
    @BindView(R.id.discover_filter_btn)
    Button discoverFilterBtn;

    @BindView(R.id.item_discover_suggest_loading_layout)
    RelativeLayout itemLoadingLayout;
    @BindView(R.id.item_discover_suggest_loading_view)
    LottieAnimationView itemLoadingView;

    public static final String ANIMATION_LOADING = "skateboard.json";

    private DiscoverSuggestUserAdapter adapter;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventRefreshQaCountInfo event) {
        if (adapter != null) {
            int count = 0;
            if (HereUser.getInstance().getUserInfo().getQaAnswer() != null) {
                count = HereUser.getInstance().getUserInfo().getQaAnswer().getCount();
            }
            if (count <= 0) {
                adapter.setLastConfirmBtnVisiable(false);
            } else {
                adapter.setLastConfirmBtnVisiable(true);
            }
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        mPresenter.findRecommend();
        EventBus.getDefault().register(this);
        boolean isFirstRegister = UserSP.getBoolean(getContext(), Constants.PREF.IS_FIRST_REGISTER, true);
        if (isFirstRegister) {
            GuideActivity.startActivity(getContext());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) (ScreenUtil.screenWidth * (330f / 375f)), DensityUtil.dp2px(560));
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        itemLoadingLayout.setLayoutParams(params);

        itemLoadingView.setAnimation(ANIMATION_LOADING, LottieAnimationView.CacheStrategy.Weak);
        itemLoadingView.loop(true);
        itemLoadingView.playAnimation();

        discoverFilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MatchPersonActivity.class);
                startActivityForResult(intent, MatchPersonActivity.REQ_MATCH_PERSON);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        showDot(RedDotManager.getInstance().isShowFindDot());
    }

    @Override
    public void onTabDoubleTap() {
        super.onTabDoubleTap();
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) itemLoadingLayout.getLayoutParams();
        if (adapter != null && adapter.getOnlyOnePage()) {
            params.removeRule(RelativeLayout.CENTER_VERTICAL);
            params.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
        } else {
            params.removeRule(RelativeLayout.CENTER_IN_PARENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.addRule(RelativeLayout.CENTER_VERTICAL);
        }
        itemLoadingLayout.setLayoutParams(params);
        itemLoadingLayout.setVisibility(View.VISIBLE);
        mPresenter.findRecommend();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCuntdownEndEvent(EventFindCountdownEnd event) {
        showDot(RedDotManager.getInstance().isShowFindDot());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MatchPersonActivity.REQ_MATCH_PERSON) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) itemLoadingLayout.getLayoutParams();
            if (adapter != null && adapter.getOnlyOnePage()) {
                params.removeRule(RelativeLayout.CENTER_VERTICAL);
                params.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
            } else {
                params.removeRule(RelativeLayout.CENTER_IN_PARENT);
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                params.addRule(RelativeLayout.CENTER_VERTICAL);
            }
            itemLoadingLayout.setLayoutParams(params);
            itemLoadingLayout.setVisibility(View.VISIBLE);
            mPresenter.findRecommend();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View getTitleView() {
        return titleView;
    }


    @Override
    public void onFindRecommend(List<FindPageUserInfoDTO> userList, int expire, int recommandSize) {
        adapter = new DiscoverSuggestUserAdapter(this, userList, expire, recommandSize, this);
        pager.setAdapter(adapter);

        itemLoadingLayout.setVisibility(View.GONE);
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
    public FindPresenter onPresenterCreate() {
        return new FindPresenter();
    }

    @Override
    public void onRefreshAdapterClick() {

    }

    @Override
    public void onNoFeelBtnClick(FindPageUserInfoDTO findPageUserInfo) {
        mPresenter.likeSetNoFeel(findPageUserInfo.getUserInfo().getUserId());
    }

    @Override
    public void onLikeBtnClick(FindPageUserInfoDTO findPageUserInfo) {
        mPresenter.likeSetLike(findPageUserInfo.getUserInfo().getUserId());
    }

    @Override
    public void onCancelLikeBtnClick(FindPageUserInfoDTO findPageUserInfo) {
        mPresenter.likeCancelLike(findPageUserInfo.getUserInfo().getUserId());
    }

    @Override
    public void onGoQaBtnClick() {
        if (QaDataManager.getInstance(getContext()).getQuestions() != null && QaDataManager.getInstance(getContext()).getQuestions().size() > 0) {
            MyQaMustActivity.startActivity(getContext());
        } else {
            MyQaActivity.startActivity(getContext(), 0);
        }
    }

    @Override
    public void onReloadAdapterClick() {
        mPresenter.findRecommend();
    }

    @Override
    public void onItemClick(UserInfo userInfo) {
        ProfileStater.startWithUserInfo(getActivity(), userInfo);
    }

    @Override
    public void onOpenConvertionClick(final UserInfo mUserInfo) {
        if (mUserInfo != null) {
            mPresenter.getView().loading("");
            IMManager.getInstance().startConversationWithCallBack(getContext(), mUserInfo.getUserId(), new IMManager.ConversationListener() {
                @Override
                public void onUserRelation(IMUserPrerelation imUserPrerelation) {
                    mPresenter.getView().loadingDismiss();
                    ImTalkActivity.startActivity(getContext(), imUserPrerelation, mUserInfo);
                }
            });
        }
    }

    @Override
    public void reBuildAdapter(Fragment fragment, ArrayList<View> viewList, int expire) {
//        DiscoverSuggestUserAdapter adapter = new DiscoverSuggestUserAdapter(this, viewList, expire, this);
//        pager.setAdapter(adapter);
//        adapter.notifyDataSetChanged();

        mPresenter.findRecommend();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventNoFeel(EventNoFeel noFeel) {
        mPresenter.findRecommend();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            onResume();
        } else {
            onPause();
        }
    }
}
