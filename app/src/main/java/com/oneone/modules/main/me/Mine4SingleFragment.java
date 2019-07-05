package com.oneone.modules.main.me;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.oneone.Constants;
import com.oneone.R;
import com.oneone.api.constants.RedDot;
import com.oneone.event.EventFollowChanged;
import com.oneone.event.EventTimelineDelete;
import com.oneone.event.EventTimelinePost;
import com.oneone.framework.android.preference.DefaultSP;
import com.oneone.framework.ui.BaseMainFragment;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.modules.dogfood.activity.MyDogFoodActivity;
import com.oneone.modules.dogfood.dialog.DailyReceiveDogFoodDialog;
import com.oneone.modules.feedback.activity.UserFeedbackActivity;
import com.oneone.modules.main.me.contract.MineContract;
import com.oneone.modules.main.me.presenter.MinePresenter;
import com.oneone.modules.main.me.view.MineLineItem;
import com.oneone.modules.main.me.view.MineSingleDataCompletion;
import com.oneone.modules.main.me.view.MineSingleGroup;
import com.oneone.modules.main.me.view.MineSingleStoryAndMatcher;
import com.oneone.modules.main.me.view.MineSummary;
import com.oneone.modules.matcher.relations.ui.MatcherGroupActivity;
import com.oneone.modules.profile.ProfileStater;
import com.oneone.modules.qa.QaDataManager;
import com.oneone.modules.qa.activity.MyQaActivity;
import com.oneone.modules.qa.activity.MyQaMustActivity;
import com.oneone.modules.matcher.relations.ui.SinglesActivity;
import com.oneone.modules.reddot.RedDotManager;
import com.oneone.modules.setting.SettingActivity;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.UserSP;
import com.oneone.modules.user.activity.ModifySingleUserMainActivity;
import com.oneone.modules.user.bean.UserStatisticInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * @author qingfei.chen
 * @since 2018/3/31.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@LayoutResource(R.layout.frag_mine_for_single)
public class Mine4SingleFragment extends BaseMainFragment<MinePresenter, MineContract.View>
        implements MineSummary.MineSummaryOnClickListener, MineSingleStoryAndMatcher.StoryAndMatcherListener,
        MineContract.View, View.OnClickListener, MineContract.CoinBalanceListener,
        SharedPreferences.OnSharedPreferenceChangeListener {
    @BindView(R.id.frag_mine_for_single_mine_summary)
    MineSummary mSummary;

    @BindView(R.id.frag_mine_for_single_mine_single_data_completion)
    MineSingleDataCompletion mSingleDataCompletion;
    @BindView(R.id.frag_mine_for_single_mine_single_group)
    MineSingleGroup mSingleGroup;
    @BindView(R.id.frag_mine_for_single_mine_story_and_matcher)
    MineSingleStoryAndMatcher mStoryAndMatcher;

    @BindView(R.id.frag_mine_for_single_mine_love_qa)
    MineLineItem loveQaItem;
    @BindView(R.id.frag_mine_for_single_mine_dog_food)
    MineLineItem dogFoodItem;
    @BindView(R.id.frag_mine_for_single_mine_feedback)
    MineLineItem feedbackItem;
    @BindView(R.id.frag_mine_for_single_mine_setting)
    MineLineItem settingItem;
    UserStatisticInfo mUserStatisticInfo;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        EventBus.getDefault().register(this);
        DefaultSP.getInstance().registerListener(getContext(), this);
        UserSP.getInstance().registerListener(getContext(), this);
        mSummary.setClickListener(this);
        loveQaItem.setItem(R.string.str_mine_page_item_love_qa_text, "", R.drawable.view_mine_line_item_my_love_qa_bg);
        dogFoodItem.setItem(R.string.str_mine_page_item_my_dog_food_text, getResources().getString(R.string.dog_food_unit), R.drawable.view_mine_line_item_my_dog_food_bg);
        feedbackItem.setItem(R.string.str_mine_page_item_my_feedback_text, "", R.drawable.view_mine_line_item_my_feedback_bg);
        settingItem.setItem(R.string.str_mine_page_item_setting_text, "", R.drawable.view_mine_line_item_my_setting_bg);

        loveQaItem.setOnClickListener(this);
        dogFoodItem.setOnClickListener(this);
        feedbackItem.setOnClickListener(this);
        settingItem.setOnClickListener(this);
        mStoryAndMatcher.setListener(this);
        mSingleDataCompletion.setOnClickListener(this);
        mSingleGroup.setOnClickListener(this);

        mPresenter.fetchQAData();
//        mPresenter.getCoinBalance();
        mPresenter.getStatisticUserQueryInfo(this);
        refreshTabBubble();

        mPresenter.taskLoginReceiveAward(this);


    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.getCoinBalance();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        DefaultSP.getInstance().unregisterListener(getContext(), this);
        UserSP.getInstance().unregisterListener(getContext(), this);
    }

    @Override
    public MinePresenter onPresenterCreate() {
        return new MinePresenter();
    }

    @Override
    public View getTitleView() {
        return mSummary;
    }


    @Override
    public void onEditClick() {
        ModifySingleUserMainActivity.startActivity(getContext());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frag_mine_for_single_mine_single_group:
                int count = 0;
                if (mUserStatisticInfo != null) {
                    count = mUserStatisticInfo.getCountMySingles().getCount();
                }
                SinglesActivity.startActivity(getActivity(), count);
                break;

            case R.id.frag_mine_for_single_mine_love_qa:
                if (QaDataManager.getInstance(getActivityContext()).getQuestions() != null
                        && QaDataManager.getInstance(getActivityContext()).getQuestions().size() > 0) {
                    MyQaMustActivity.startActivity(getContext());
                } else {
                    MyQaActivity.startActivity(getContext(), 0);
                }
                break;

            case R.id.frag_mine_for_single_mine_dog_food:
                MyDogFoodActivity.startActivity(getContext());
                break;

            case R.id.frag_mine_for_single_mine_feedback:
                UserFeedbackActivity.startActivity(getActivityContext());
                break;

            case R.id.frag_mine_for_single_mine_setting:
                SettingActivity.startActivity(getContext());
                break;

            case R.id.frag_mine_for_single_mine_single_data_completion:
                ModifySingleUserMainActivity.startActivity(getContext());
                break;
        }
    }

    @Override
    public void onStoryClick() {
        ProfileStater.startWithUserInfo(getContext(), HereUser.getInstance().getUserInfo());
    }

    @Override
    public void onMatcherClick() {
        int count = 0;
        if (mUserStatisticInfo != null) {
            count = mUserStatisticInfo.getCountMyMatchers().getCount();
        }
        MatcherGroupActivity.startActivity(getContext(), count);
    }

    @Override
    public void onStatisticUserQuery(UserStatisticInfo userStatisticInfo) {
        if (userStatisticInfo == null) {
            return;
        }
        UserSP.putAndApply(getContext(), Constants.PREF.PREF_MATCHER_GROUP_COUNT,
                userStatisticInfo.getCountMyMatchers().getCount());

        mUserStatisticInfo = userStatisticInfo;
        mSummary.setUserStatisticInfo(userStatisticInfo);
        if (userStatisticInfo.getCountMySingles() != null) {
            mSingleGroup.setPersionCount(userStatisticInfo.getCountMySingles().getCount());
        }
        if (userStatisticInfo.getCountMyMatchers() != null) {
            mStoryAndMatcher.setMatcherCount(userStatisticInfo.getCountMyMatchers().getCount());
        }
        setUserCompleteScore(userStatisticInfo.getCountUserCompletedScore().getCount());
    }

    @Override
    public void onTaskLoginReceiveAward(int taskAward, int received) {
        if (received == 0)
            new DailyReceiveDogFoodDialog(getActivityContext(), taskAward).show();
    }

    private void setUserCompleteScore(int userInfoWeight) {
        if (userInfoWeight >= 100) {
            mSingleDataCompletion.setVisibility(View.GONE);
            mSummary.modifyBtnEnable(true);
        } else {
            mSingleDataCompletion.setProgress(userInfoWeight);
            mSummary.modifyBtnEnable(false);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(HereUser.class.getSimpleName())) {
            mPresenter.getStatisticUserQueryInfo(this);
        } else if (key.equals(RedDotManager.PREF_DOT + RedDot.FEEDBACK)) {
            refreshTabBubble();
        } else if (key.equals(RedDotManager.PREF_DOT + RedDot.NEW_SINGLES)) {
            refreshTabBubble();
        } else if (key.equals(RedDotManager.PREF_DOT + RedDot.NEW_MATCHERS)) {
            refreshTabBubble();
        } else if (key.equals(UserSP.DOG_FOOT_BALANCE)) {
            refreshBalance();
        }
    }

    private void refreshBalance() {
        int balance = UserSP.getInt(getContext(), UserSP.DOG_FOOT_BALANCE, 0);
        dogFoodItem.setItemValue(balance + getResources().getString(R.string.dog_food_unit));
    }

    private void refreshTabBubble() {
        int feedbackUnread = RedDotManager.getInstance().getFeedbackUnread();
        feedbackItem.setUnreadCount(feedbackUnread);
        int newSingleUnread = RedDotManager.getInstance().getNewSinglesUnread();
        mSingleGroup.setUnreadCount(newSingleUnread);
        int newMatcherUnread = RedDotManager.getInstance().getNewMatchersUnread();
        mStoryAndMatcher.setUnreadCount(newMatcherUnread);

        refreshBalance();
        int totalRedDot = newMatcherUnread + newSingleUnread;
        if (feedbackUnread > 0) {
            setBubble(feedbackUnread);
        } else {
            showDot(totalRedDot > 0);
        }

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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventTimelinePost(EventTimelinePost event) {
        if (mPresenter != null) {
            mPresenter.getStatisticUserQueryInfo(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventFollowChanged(EventFollowChanged event) {
        if (mPresenter != null) {
            mPresenter.getStatisticUserQueryInfo(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventTimeDelete(EventTimelineDelete event) {
        if (mPresenter != null) {
            mPresenter.getStatisticUserQueryInfo(this);
        }
    }

}
