package com.oneone.modules.user.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.event.EventProfileUpdateByRole;
import com.oneone.framework.android.analytics.annotation.Alias;
import com.oneone.framework.ui.BaseFragment;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.dialog.DatePickerDialog;
import com.oneone.modules.entry.presenter.OpenRelationPresenter;
import com.oneone.modules.user.bean.UserProfileUpdateBean;
import com.oneone.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * @author qingfei.chen
 * @since 2018/4/25.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@Alias("生日")
@LayoutResource(R.layout.frag_profile_birthday_edit)
public class ProfileBirthdayEditFrag extends BaseFragment implements
        View.OnClickListener, DatePickerDialog.OnDatePickListener {
    @BindView(R.id.step_4_birthday_layout)
    RelativeLayout mRlBirthday;

    @BindView(R.id.step_4_show_date_tv)
    TextView mTvBirthday;

    @BindView(R.id.step_4_confirm_btn)
    Button mBtnConfirm;

    private String birthday;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBtnConfirm.setOnClickListener(this);
        mRlBirthday.setOnClickListener(this);

        birthday = OpenRelationPresenter.getTempUserInfo().getBirthdate();
        if (!TextUtils.isEmpty(birthday)) {
            mTvBirthday.setText(birthday);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.step_4_birthday_layout:
                new DatePickerDialog(getContext(), this).show();
                break;

            case R.id.step_4_confirm_btn:
                if (TextUtils.isEmpty(birthday)) {
                    ToastUtil.show(getContext(),
                            getString(R.string.str_set_single_flow_page_step_4_input_birthday_notice));
                    return;
                }

                EventBus.getDefault().post(new EventProfileUpdateByRole(new UserProfileUpdateBean().setBirthdate(birthday)));
                break;
        }
    }


    @Override
    public void onDateSelected(String date) {
        birthday = date;
        mTvBirthday.setText(date);
    }
}
