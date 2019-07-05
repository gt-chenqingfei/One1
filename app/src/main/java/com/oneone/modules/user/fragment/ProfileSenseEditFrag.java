package com.oneone.modules.user.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.event.EventNextStep;
import com.oneone.event.EventProfileUpdateByRole;
import com.oneone.framework.android.analytics.annotation.Alias;
import com.oneone.framework.ui.BasePresenterFragment;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.widget.ZzHorizontalProgressBar;
import com.oneone.modules.entry.presenter.OpenRelationPresenter;
import com.oneone.modules.mystory.bean.StoryTag;
import com.oneone.modules.mystory.contract.StoryContract;
import com.oneone.modules.mystory.presenter.StoryPresenter;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.bean.UserProfileUpdateBean;
import com.oneone.modules.user.bean.UserRoleSettingTagBean;
import com.oneone.utils.TagStrUtil;
import com.oneone.utils.ToastUtil;
import com.oneone.widget.AutoFlowView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.oneone.modules.user.fragment.ProfileOccupationEditFrag.EXTRA_SHOW_PROGRESS;

/**
 * @author qingfei.chen
 * @since 2018/4/25.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@Alias("人生信条，价值观")
@LayoutResource(R.layout.frag_profile_sense_edit)
public class ProfileSenseEditFrag extends
        BasePresenterFragment<StoryPresenter, StoryContract.View>
        implements StoryContract.View, AutoFlowView.AutoFlowItemClickListener, StoryContract.OnTagsGetListener {

    @BindView(R.id.frag_profile_sense_afv)
    AutoFlowView mFlowLayout;

    @BindView(R.id.step_9_top_tag_collection_inner_layout)
    RelativeLayout stepTagCollectionTopLayout;
    @BindView(R.id.step_9_top_tag_collection_tv)
    TextView stepTagCollectionTopTv;
    @BindView(R.id.step_9_tv_tag_2)
    TextView stepTvTag2;
    @BindView(R.id.frag_profile_pb)
    ZzHorizontalProgressBar mPb;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        List<String> preSelected = new ArrayList<>();
        UserRoleSettingTagBean tagBean = null;
        boolean isShow = getActivity().getIntent().getBooleanExtra(EXTRA_SHOW_PROGRESS, true);
        if (!isShow) {
            mPb.setVisibility(View.GONE);
            tagBean = HereUser.getInstance().getUserInfo().getSenseOfWorthTags();
        } else {
            tagBean = OpenRelationPresenter.getTempUserInfo().getSenseOfWorthTags();
        }

        if (tagBean != null) {
            preSelected.addAll(tagBean.getCustomTags());
            preSelected.addAll(tagBean.getSystemTags());
        }
        mFlowLayout.setPreSelectedArray(preSelected);

        mFlowLayout.setLimit(10)
                .setHintText(R.string.str_set_single_flow_page_input_max_count_10)
                .setItemLayoutRes(R.layout.view_profile_step_tag_text)
                .setAddEditTextBackground(R.drawable.selector_step_9_tag_bg)
                .setAddEditTextTextColor(R.color.color_selector_step_9_text)
                .setMaxSelected(3)
                .setAddBtnText(R.string.str_set_single_flow_page_custom_tag)
                .setAddBtnColor(R.color.single_flow_content_bg_9).setListener(this);
        EventBus.getDefault().register(this);
        mPresenter.getTags(StoryTag.SENSE, this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            EventBus.getDefault().register(this);
        } else {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public StoryPresenter onPresenterCreate() {
        return new StoryPresenter();
    }

    @Override
    public void onTagsGet(List<String> tags) {
        mFlowLayout.notifyDataChange(tags);
    }

    @Override
    public void onSelected(List<String> tags) {
        RelativeLayout.LayoutParams step6Params =
                (RelativeLayout.LayoutParams) stepTvTag2.getLayoutParams();
        if (tags == null || tags.isEmpty()) {
            stepTagCollectionTopLayout.setVisibility(View.GONE);
            step6Params.removeRule(RelativeLayout.ABOVE);
            step6Params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            return;
        }

        step6Params.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        step6Params.addRule(RelativeLayout.ABOVE, R.id.step_9_top_tag_collection_inner_layout);
        stepTagCollectionTopLayout.setVisibility(View.VISIBLE);
        stepTagCollectionTopTv.setText(TagStrUtil.buildText(tags));
    }

    @Override
    public void onSelectOverflow() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNextStepEvent(EventNextStep event) {

        List<String> selectedValue = mFlowLayout.getSelectedValue();
        if (selectedValue == null || selectedValue.isEmpty()) {
            ToastUtil.show(getContext(), String.format(getString(R.string.str_set_single_flow_page_min_count_tags), 1 + ""));
            return;
        }

        UserRoleSettingTagBean bean = new UserRoleSettingTagBean();
        bean.setCustomTags(mFlowLayout.getCustomerSelectedValue());
        bean.setSystemTags(mFlowLayout.getSystemSelectedValue());
        UserProfileUpdateBean userInfo = new UserProfileUpdateBean();
        userInfo.setSenseOfWorthTags(bean);

        EventBus.getDefault().post(new EventProfileUpdateByRole(userInfo));
    }
}
