package com.oneone.modules.user.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oneone.BaseActivity;
import com.oneone.R;
import com.oneone.event.EventProfileUpdateByRole;
import com.oneone.event.EventProfileUpdated;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;
import com.oneone.framework.ui.utils.ScreenUtil;
import com.oneone.modules.mystory.contract.StoryContract;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.utils.TagStrUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author qingfei.chen
 * @since 2018/5/2.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@ToolbarResource(title = R.string.title_my_story)
@LayoutResource(R.layout.activity_my_story_tags)
public class MyStoryTagsActivity extends BaseActivity implements View.OnClickListener {
    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, MyStoryTagsActivity.class));
    }

    @BindView(R.id.activity_story_tags_1)
    ViewGroup vg1;

    @BindView(R.id.activity_story_tags_2)
    ViewGroup vg2;

    @BindView(R.id.activity_story_tags_3)
    ViewGroup vg3;

    @BindView(R.id.activity_story_tags_4)
    ViewGroup vg4;

    @BindView(R.id.activity_story_tags_5)
    ViewGroup vg5;

    @BindView(R.id.activity_story_tags_6)
    ViewGroup vg6;

    @BindView(R.id.step_5_top_tag_collection_tv)
    TextView step5TagCollectionTopTv;

    @BindView(R.id.step_5_tv_tag_1)
    TextView step5TvTag1;

    @BindView(R.id.step_5_tv_tag_2)
    TextView step5TvTag2;

    @BindView(R.id.step_5_top_tag_collection_inner_layout)
    ViewGroup step5TagCollectionTopLayout;

    @BindView(R.id.step_6_top_tag_collection_tv)
    TextView step6TagCollectionTopTv;

    @BindView(R.id.step_6_tv_tag_1)
    TextView step6TvTag1;
    @BindView(R.id.step_6_tv_tag_2)
    TextView step6TvTag2;

    @BindView(R.id.step_6_top_tag_collection_inner_layout)
    ViewGroup step6TagCollectionTopLayout;


    @BindView(R.id.step_7_top_tag_collection_tv)
    TextView step7TagCollectionTopTv;

    @BindView(R.id.step_7_tv_tag_1)
    TextView step7TvTag1;
    @BindView(R.id.step_7_tv_tag_2)
    TextView step7TvTag2;

    @BindView(R.id.step_7_top_tag_collection_inner_layout)
    ViewGroup step7TagCollectionTopLayout;


    @BindView(R.id.step_8_top_tag_collection_tv)
    TextView step8TagCollectionTopTv;

    @BindView(R.id.step_8_tv_tag_1)
    TextView step8TvTag1;
    @BindView(R.id.step_8_tv_tag_2)
    TextView step8TvTag2;

    @BindView(R.id.step_8_top_tag_collection_inner_layout)
    ViewGroup step8TagCollectionTopLayout;


    @BindView(R.id.step_9_top_tag_collection_tv)
    TextView step9TagCollectionTopTv;

    @BindView(R.id.step_9_tv_tag_1)
    TextView step9TvTag1;
    @BindView(R.id.step_9_tv_tag_2)
    TextView step9TvTag2;

    @BindView(R.id.step_9_top_tag_collection_inner_layout)
    ViewGroup step9TagCollectionTopLayout;


    @BindView(R.id.step_10_top_tag_collection_tv)
    TextView step10TagCollectionTopTv;

    @BindView(R.id.step_10_tv_tag_1)
    TextView step10TvTag1;

    @BindView(R.id.step_10_tv_tag_2)
    TextView step10TvTag2;

    @BindView(R.id.step_10_top_tag_collection_inner_layout)
    ViewGroup step10TagCollectionTopLayout;

    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfo = HereUser.getInstance().getUserInfo();
        ViewGroup.LayoutParams params = vg1.getLayoutParams();
        params.height = ScreenUtil.getDisplayWidth() - (ScreenUtil.dip2px(46));

        vg1.setLayoutParams(params);
        vg2.setLayoutParams(params);
        vg3.setLayoutParams(params);
        vg4.setLayoutParams(params);
        vg5.setLayoutParams(params);
        vg6.setLayoutParams(params);

        vg1.setOnClickListener(this);
        vg2.setOnClickListener(this);
        vg3.setOnClickListener(this);
        vg4.setOnClickListener(this);
        vg5.setOnClickListener(this);
        vg6.setOnClickListener(this);

        EventBus.getDefault().register(this);
        refreshTags();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventProfileUpdate(EventProfileUpdated event) {
        userInfo = HereUser.getInstance().getUserInfo();
        refreshTags();
    }

    private void refreshTags() {
        initTagCharacter();
        initTagExperience();
        initTagOccupation();
        initTagSense();
        initTagSkill();
        initTagSpouse();
    }


    private void initTagOccupation() {
        List<String> tags = new ArrayList<>(userInfo.getOccupationTags().getSystemTags());
        if (!tags.isEmpty()) {
            tags.addAll(userInfo.getOccupationTags().getCustomTags());
        } else {
            tags = userInfo.getOccupationTags().getCustomTags();
        }
        RelativeLayout.LayoutParams params =
                (RelativeLayout.LayoutParams) step5TvTag1.getLayoutParams();
        if (tags == null || tags.isEmpty()) {
            step5TagCollectionTopLayout.setVisibility(View.GONE);
            params.removeRule(RelativeLayout.ABOVE);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            step5TvTag2.setVisibility(View.VISIBLE);
            return;
        }

        step5TvTag2.setVisibility(View.INVISIBLE);
        params.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.ABOVE, R.id.step_5_top_tag_collection_inner_layout);
        step5TagCollectionTopLayout.setVisibility(View.VISIBLE);
        step5TagCollectionTopTv.setText(TagStrUtil.buildText(tags));
    }

    private void initTagSkill() {
        List<String> tags = new ArrayList<>(userInfo.getSkillTags().getSystemTags());
        if (!tags.isEmpty()) {
            tags.addAll(userInfo.getSkillTags().getCustomTags());
        } else {
            tags = userInfo.getSkillTags().getCustomTags();
        }
        RelativeLayout.LayoutParams params =
                (RelativeLayout.LayoutParams) step6TvTag1.getLayoutParams();
        if (tags == null || tags.isEmpty()) {
            step6TagCollectionTopLayout.setVisibility(View.GONE);
            params.removeRule(RelativeLayout.ABOVE);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            step6TvTag2.setVisibility(View.VISIBLE);
            return;
        }

        step6TvTag2.setVisibility(View.INVISIBLE);
        params.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.ABOVE, R.id.step_6_top_tag_collection_inner_layout);
        step6TagCollectionTopLayout.setVisibility(View.VISIBLE);
        step6TagCollectionTopTv.setText(TagStrUtil.buildText(tags));
    }

    private void initTagCharacter() {
        List<String> tags = new ArrayList<>(userInfo.getCharacterTags().getSystemTags());
        if (!tags.isEmpty()) {
            tags.addAll(userInfo.getCharacterTags().getCustomTags());
        } else {
            tags = userInfo.getCharacterTags().getCustomTags();
        }
        RelativeLayout.LayoutParams params =
                (RelativeLayout.LayoutParams) step7TvTag1.getLayoutParams();
        if (tags == null || tags.isEmpty()) {
            step7TagCollectionTopLayout.setVisibility(View.GONE);
            params.removeRule(RelativeLayout.ABOVE);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            step7TvTag2.setVisibility(View.VISIBLE);
            return;

        }

        step7TvTag2.setVisibility(View.INVISIBLE);

        params.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.ABOVE, R.id.step_7_top_tag_collection_inner_layout);
        step7TagCollectionTopLayout.setVisibility(View.VISIBLE);
        step7TagCollectionTopTv.setText(TagStrUtil.buildText(tags));
    }

    private void initTagExperience() {
        List<String> tags = new ArrayList<>(userInfo.getExperienceTags().getSystemTags());
        if (!tags.isEmpty()) {
            tags.addAll(userInfo.getExperienceTags().getCustomTags());
        } else {
            tags = userInfo.getExperienceTags().getCustomTags();
        }
        RelativeLayout.LayoutParams params =
                (RelativeLayout.LayoutParams) step8TvTag1.getLayoutParams();
        if (tags == null || tags.isEmpty()) {
            step8TagCollectionTopLayout.setVisibility(View.GONE);
            params.removeRule(RelativeLayout.ABOVE);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            step8TvTag2.setVisibility(View.VISIBLE);
            return;
        }

        step8TvTag2.setVisibility(View.INVISIBLE);
        params.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.ABOVE, R.id.step_8_top_tag_collection_inner_layout);
        step8TagCollectionTopLayout.setVisibility(View.VISIBLE);
        step8TagCollectionTopTv.setText(TagStrUtil.buildText(tags));
    }

    private void initTagSense() {
        List<String> tags = new ArrayList<>(userInfo.getSenseOfWorthTags().getSystemTags());
        if (!tags.isEmpty()) {
            tags.addAll(userInfo.getSenseOfWorthTags().getCustomTags());
        } else {
            tags = userInfo.getSenseOfWorthTags().getCustomTags();
        }
        RelativeLayout.LayoutParams params =
                (RelativeLayout.LayoutParams) step9TvTag1.getLayoutParams();
        if (tags == null || tags.isEmpty()) {
            step9TagCollectionTopLayout.setVisibility(View.GONE);
            params.removeRule(RelativeLayout.ABOVE);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            step9TvTag2.setVisibility(View.VISIBLE);
            return;
        }
        step9TvTag2.setVisibility(View.INVISIBLE);
        params.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.ABOVE, R.id.step_9_top_tag_collection_inner_layout);
        step9TagCollectionTopLayout.setVisibility(View.VISIBLE);
        step9TagCollectionTopTv.setText(TagStrUtil.buildText(tags));
    }

    private void initTagSpouse() {
        List<String> tags = new ArrayList<>(userInfo.getSpousePrefsTags().getSystemTags());
        if (!tags.isEmpty()) {
            tags.addAll(userInfo.getSpousePrefsTags().getCustomTags());
        } else {
            tags = userInfo.getSpousePrefsTags().getCustomTags();
        }
        RelativeLayout.LayoutParams params =
                (RelativeLayout.LayoutParams) step10TvTag1.getLayoutParams();
        if (tags == null || tags.isEmpty()) {
            step10TagCollectionTopLayout.setVisibility(View.GONE);
            params.removeRule(RelativeLayout.ABOVE);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            step10TvTag2.setVisibility(View.VISIBLE);
            return;
        }

        step10TvTag2.setVisibility(View.INVISIBLE);

        params.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.ABOVE, R.id.step_10_top_tag_collection_inner_layout);
        step10TagCollectionTopLayout.setVisibility(View.VISIBLE);
        step10TagCollectionTopTv.setText(TagStrUtil.buildText(tags));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_story_tags_1:
                MyStoryTagsDetailsActivity.startActivity(this, MyStoryTagsDetailsActivity.TYPE_OCCUPATION);
                break;
            case R.id.activity_story_tags_2:
                MyStoryTagsDetailsActivity.startActivity(this, MyStoryTagsDetailsActivity.TYPE_SKILL);
                break;
            case R.id.activity_story_tags_3:
                MyStoryTagsDetailsActivity.startActivity(this, MyStoryTagsDetailsActivity.TYPE_CHARACTER);
                break;
            case R.id.activity_story_tags_4:
                MyStoryTagsDetailsActivity.startActivity(this, MyStoryTagsDetailsActivity.TYPE_EXPERIENCE);
                break;
            case R.id.activity_story_tags_5:
                MyStoryTagsDetailsActivity.startActivity(this, MyStoryTagsDetailsActivity.TYPE_SENSE);
                break;
            case R.id.activity_story_tags_6:
                MyStoryTagsDetailsActivity.startActivity(this, MyStoryTagsDetailsActivity.TYPE_SPOUSE);
                break;
        }
    }


}
