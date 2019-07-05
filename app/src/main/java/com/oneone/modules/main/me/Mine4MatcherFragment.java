package com.oneone.modules.main.me;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.oneone.R;
import com.oneone.api.constants.RedDot;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.BaseMainFragment;
import com.oneone.framework.ui.dialog.WarnDialog;
import com.oneone.modules.feedback.activity.UserFeedbackActivity;
import com.oneone.modules.reddot.RedDotManager;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.UserSP;
import com.oneone.modules.user.activity.ModifyMatcherUserBasicActivity;
import com.oneone.modules.entry.activity.OpenSingleRelationActivity;
import com.oneone.modules.main.me.contract.MineContract;
import com.oneone.modules.main.me.presenter.MinePresenter;
import com.oneone.modules.main.me.view.MineLineItem;
import com.oneone.modules.main.me.view.MineMatcherOpen;
import com.oneone.modules.main.me.view.MineSummary;
import com.oneone.modules.setting.SettingActivity;
import com.oneone.modules.user.bean.UserStatisticInfo;

import butterknife.BindView;

/**
 * @author qingfei.chen
 * @since 2018/3/31.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@LayoutResource(R.layout.frag_mine_for_matcher)
public class Mine4MatcherFragment extends BaseMainFragment<MinePresenter, MineContract.View>
        implements MineSummary.MineSummaryOnClickListener, MineContract.View
        , MineMatcherOpen.MineMatcherOpenSummaryOnClickListener
        , View.OnClickListener, MineContract.CoinBalanceListener
, SharedPreferences.OnSharedPreferenceChangeListener{

    @BindView(R.id.frag_mine_for_matcher_mine_summary)
    MineSummary mSummary;
    @BindView(R.id.frag_mine_for_matcher_open_single)
    MineMatcherOpen mOpenSummary;
    @BindView(R.id.frag_mine_for_matcher_mine_feedback)
    MineLineItem feedbackItem;
    @BindView(R.id.frag_mine_for_matcher_mine_setting)
    MineLineItem settingItem;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSummary.setClickListener(this);
        mOpenSummary.setListener(this);

        feedbackItem.setItem(R.string.str_mine_page_item_my_feedback_text, "", R.drawable.view_mine_line_item_my_feedback_bg);
        settingItem.setItem(R.string.str_mine_page_item_setting_text, "", R.drawable.view_mine_line_item_my_setting_bg);

        feedbackItem.setOnClickListener(this);
        settingItem.setOnClickListener(this);

        mPresenter.getStatisticUserQueryInfo(this);
        refreshTabBubble();
        UserSP.getInstance().registerListener(getContext(),this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UserSP.getInstance().unregisterListener(getContext(),this);
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
    public void onCloseBtnClick() {
        WarnDialog dialog1 = new WarnDialog(getActivityContext(), getResources().getString(R.string.str_setting_single_data_dialog_title_text), new WarnDialog.OnPositiveClickListener() {
            @Override
            public void onPositiveClick() {
                mOpenSummary.setVisibility(View.GONE);
            }
        });
        dialog1.setPositiveButton(getResources().getString(R.string.str_setting_single_data_dialog_nagative_text));
        dialog1.setNegativeButton(getResources().getString(R.string.str_setting_single_data_dialog_positive_text), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                OpenSingleRelationActivity.startActivity(getContext(),true);
                mOpenSummary.setVisibility(View.GONE);
            }
        });
        dialog1.show();

    }

    @Override
    public void onOpenSingleBtnClick() {
        OpenSingleRelationActivity.startActivity(getContext(),true);
    }


    @Override
    public void onEditClick() {
        // matcher nothing to do
        ModifyMatcherUserBasicActivity.startActivity(getContext());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frag_mine_for_single_mine_setting:
                SettingActivity.startActivity(getContext());
                break;

            case R.id.frag_mine_for_matcher_mine_setting:
                SettingActivity.startActivity(getActivity());
                break;
            case R.id.frag_mine_for_matcher_mine_feedback:
                UserFeedbackActivity.startActivity(getActivityContext());
                break;
        }
    }

    @Override
    public void onStatisticUserQuery(UserStatisticInfo userStatisticInfo) {
        mSummary.setUserStatisticInfo(userStatisticInfo);
    }

    @Override
    public void onTaskLoginReceiveAward(int taskAward, int received) {

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(RedDotManager.PREF_DOT + RedDot.FEEDBACK)){
            refreshTabBubble();
        }
    }

    private void refreshTabBubble(){
        int count = RedDotManager.getInstance().getFeedbackUnread();
        feedbackItem.setUnreadCount(count);
        setBubble(count);
    }
}
