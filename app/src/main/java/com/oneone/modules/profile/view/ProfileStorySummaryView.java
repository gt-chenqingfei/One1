package com.oneone.modules.profile.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.framework.ui.BaseView;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.modules.user.util.HereUserUtil;
import com.oneone.utils.ArraysUtil;

import butterknife.BindView;

/**
 * @author qingfei.chen
 * @since 2018/5/14.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@LayoutResource(R.layout.layout_profile_story_summary)
public class ProfileStorySummaryView extends BaseView {
    @BindView(R.id.layout_my_story_summary_tv_height)
    TextView mTvHeight;

    @BindView(R.id.layout_my_story_summary_tv_weight)
    TextView mTvWeight;

    @BindView(R.id.layout_my_story_summary_tv_city)
    TextView mTvCity;

    @BindView(R.id.layout_my_story_summary_tv_university)
    TextView mTvUniversity;

    @BindView(R.id.layout_my_story_summary_tv_occ)
    TextView mTvOcc;

    @BindView(R.id.layout_my_story_summary_tv_income)
    TextView mTvInCome;

    @BindView(R.id.layout_my_story_summary_tv_constellation)
    TextView mTvConstellation;

    @BindView(R.id.layout_my_story_summary_tv_sense)
    TextView mTvSense;
    @BindView(R.id.layout_my_story_summary_iv_sense)
    ImageView mIvSense;

    public ProfileStorySummaryView(Context context) {
        super(context);
    }

    public ProfileStorySummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onViewCreated() {

    }

    public void bindUserInfo(UserInfo userInfo) {
        if (userInfo == null) {
            return;
        }
        if (userInfo.getHeight() == 0) {
            mTvHeight.setText("-");
        } else {
            mTvHeight.setText(HereUserUtil.getHeightDisplay(userInfo.getHeight()));
        }
        if (userInfo.getBodilyForm() == 0) {
            mTvWeight.setText("-");
        } else {
            mTvWeight.setText(HereUserUtil.getBodilyForm(getContext(), userInfo));
        }
        if (TextUtils.isEmpty(userInfo.getHometownProvince())) {
            mTvCity.setText("-");
        } else {
            mTvCity.setText(userInfo.getHometownProvince() + "\n" + userInfo.getHometownCity());
        }

        if (TextUtils.isEmpty(userInfo.getCollegeName())) {
            mTvUniversity.setText("-");
        } else {
            mTvUniversity.setText(userInfo.getCollegeName() + "\n" + ArraysUtil.getValueByKey(mContext, com.oneone.R.array.education_habits_array, userInfo.getDegree()));
        }
        if (TextUtils.isEmpty(userInfo.getOccupation())) {
            mTvOcc.setText("-");
        } else {
            mTvOcc.setText(userInfo.getIndustry() + "\n" + userInfo.getOccupation());
        }
        if (TextUtils.isEmpty(userInfo.getAnnualIncomeCurrency())) {
            mTvInCome.setText("-");
        } else {
            mTvInCome.setText(mContext.getResources().getString(R.string.str_modify_user_occupation_and_school_basic_line_item_title_year_income)
                    + "\n" + ArraysUtil.getValueByKey(mContext, com.oneone.R.array.income_array, userInfo.getAnnualIncomeMin()));
        }
        if (TextUtils.isEmpty(userInfo.getConstellation())) {
            mTvConstellation.setText("-");
        } else {
            mTvConstellation.setText(userInfo.getConstellation());
        }
        if (userInfo.getReligion() < 0) {
            mTvSense.setText("-");
            mIvSense.setImageResource(R.drawable.ic_sense);
        } else {
            mTvSense.setText(HereUserUtil.getReligionStr(userInfo.getReligion()));
            mIvSense.setImageResource(HereUserUtil.getReligionRes(userInfo.getReligion()));
        }
    }

}
