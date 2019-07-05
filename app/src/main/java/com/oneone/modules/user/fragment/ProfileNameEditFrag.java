package com.oneone.modules.user.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.oneone.R;
import com.oneone.event.EventProfileUpdateByRole;
import com.oneone.framework.android.analytics.annotation.Alias;
import com.oneone.framework.ui.BaseFragment;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.widget.ZzHorizontalProgressBar;
import com.oneone.modules.entry.presenter.OpenRelationPresenter;
import com.oneone.modules.user.bean.UserProfileUpdateBean;
import com.oneone.utils.ToastUtil;
import com.oneone.widget.InputTextWatcher;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * @author qingfei.chen
 * @since 2018/4/25.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@Alias("昵称")
@LayoutResource(R.layout.frag_profile_name_edit)
public class ProfileNameEditFrag extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.step_2_nickname_et)
    EditText mEtNickName;

    @BindView(R.id.step_2_confirm_btn)
    Button mBtnConfirm;

    @BindView(R.id.frag_profile_gender_edit_pb)
    ZzHorizontalProgressBar mPB;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBtnConfirm.setOnClickListener(this);
        UserProfileUpdateBean bean = OpenRelationPresenter.getTempUserInfo();
        String name =bean.getNickname();
        if (!TextUtils.isEmpty(name)) {
            mEtNickName.setText(name);
        }

        if (getArguments() != null) {
            boolean isMatcher = getArguments().getBoolean("isMatcher");
            if (isMatcher) {
                mPB.setMax(2);
                mPB.setProgress(1);
            }
        }
        mEtNickName.addTextChangedListener(new InputTextWatcher(getActivity(), mEtNickName, InputTextWatcher.USER_NAME_LENGTH_MAX, 1));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.step_2_confirm_btn) {
            String name = mEtNickName.getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                ToastUtil.show(getContext(),
                        getString(R.string.str_set_single_flow_page_step_2_input_nickname_notice));
                return;
            }
            if (name.length() < 2) {
                ToastUtil.show(getActivity(), getResources().getString(R.string.str_modify_user_name_page_title_text_two));
                return;
            }

            UserProfileUpdateBean userInfo = new UserProfileUpdateBean();
            userInfo.setNickname(name);
            EventBus.getDefault().post(new EventProfileUpdateByRole(userInfo));
        }
    }
}
