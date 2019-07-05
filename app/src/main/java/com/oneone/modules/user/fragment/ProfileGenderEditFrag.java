package com.oneone.modules.user.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.api.constants.CharacterSetting;
import com.oneone.api.constants.Gender;
import com.oneone.event.EventProfileUpdateByRole;
import com.oneone.framework.android.analytics.annotation.Alias;
import com.oneone.framework.ui.BaseFragment;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.modules.entry.presenter.OpenRelationPresenter;
import com.oneone.modules.user.bean.UserProfileUpdateBean;
import com.oneone.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author qingfei.chen
 * @since 2018/4/25.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@Alias("性别选择")
@LayoutResource(R.layout.frag_profile_gender_edit)
public class ProfileGenderEditFrag extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.step_1_confirm_btn)
    Button mBtnConfirm;

    @BindView(R.id.frag_profile_gender_edit_ll_group)
    LinearLayout llGroup;

    List<TextView> mArray = new ArrayList<>();


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBtnConfirm.setOnClickListener(this);


        genderCharacterInit(llGroup);
        UserProfileUpdateBean bean = OpenRelationPresenter.getTempUserInfo();
        int characterSetting = 0;
        if (bean.getCharacterSetting() != null) {
            characterSetting=bean.getCharacterSetting();
        }
        preSelectTag(characterSetting);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.step_1_confirm_btn) {
            performBtnConfirmClick();
            return;
        }
        performGenderTextClick(v);
    }

    private void performBtnConfirmClick() {
        int character = getSelectedCharacter();
        if (character == CharacterSetting.UNKNOWN) {
            ToastUtil.show(getContext(),
                    getString(R.string.str_set_single_flow_page_step_1_please_choose));
            return;
        }

        int gender = getGenderByCharacter(character);
        UserProfileUpdateBean userInfo = new UserProfileUpdateBean();
        userInfo.setSex(gender);
        userInfo.setCharacterSetting(character);
        EventBus.getDefault().post(new EventProfileUpdateByRole(userInfo));
    }

    private void performGenderTextClick(View v) {
        for (TextView tv : mArray) {
            if (tv.getId() == v.getId()) {
                tv.setSelected(true);
            } else {
                tv.setSelected(false);
            }
        }
    }

    private void genderCharacterInit(LinearLayout llGroup) {
        for (int i = 0; i < llGroup.getChildCount(); i++) {
            View child = llGroup.getChildAt(i);
            if (child instanceof LinearLayout) {
                LinearLayout group = (LinearLayout) child;
                genderCharacterInit(group);
            } else if (child instanceof TextView) {
                child.setOnClickListener(this);
                mArray.add((TextView) child);
            }
        }
    }


    private int getSelectedCharacter() {
        for (TextView tv : mArray) {
            if (tv.isSelected()) {
                String tag = (String) tv.getTag();
                return Integer.parseInt(tag);
            }
        }

        return CharacterSetting.UNKNOWN;
    }

    private void preSelectTag(int characterSetting) {
        for (TextView tv : mArray) {
            String tag = (String) tv.getTag();
            int character = Integer.parseInt(tag);

            if (character == characterSetting) {
                tv.setSelected(true);
                return;
            }
        }
    }

    private int getGenderByCharacter(int character) {
        switch (character) {
            case CharacterSetting.DA_SU:
            case CharacterSetting.XIAN_ROU:
            case CharacterSetting.XIAO_GE_GE:
                return Gender.MALE;

            case CharacterSetting.XIAO_JIE_JIE:
            case CharacterSetting.JIE_JIE:
            case CharacterSetting.SAO_NV:
                return Gender.FEMALE;

            default:
                return Gender.UNKNOWN;
        }
    }

}
