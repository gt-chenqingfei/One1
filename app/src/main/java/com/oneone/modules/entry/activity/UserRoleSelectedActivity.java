package com.oneone.modules.entry.activity;

import com.oneone.AppInitializer;
import com.oneone.BaseActivity;
import com.oneone.R;
import com.oneone.api.constants.Role;
import com.oneone.event.EventFinishActivity;
import com.oneone.framework.android.analytics.annotation.Alias;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.utils.ScreenUtil;
import com.oneone.modules.entry.contract.OpenRelationContract;
import com.oneone.modules.entry.presenter.OpenRelationPresenter;
import com.oneone.modules.entry.adapter.SingleSuggestAdapter;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.bean.ShowCaseUserInfo;
import com.oneone.utils.GenderUtil;
import com.oneone.utils.MyTextUtil;
import com.oneone.widget.BubbleLayout;
import com.oneone.widget.AvatarImageView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

@Alias("用户身份选择")
@LayoutResource(R.layout.activity_user_role_select)
public class UserRoleSelectedActivity extends BaseActivity<OpenRelationPresenter, OpenRelationContract.View> implements
        OpenRelationContract.View, OpenRelationContract.OnUserShowcaseListListener, OpenRelationContract.OnOpenRoleListener {

    public static void startActivity(Context context) {
        Intent it = new Intent(context, UserRoleSelectedActivity.class);
        context.startActivity(it);
    }

    @BindView(R.id.choose_single_layout)
    RelativeLayout chooseSingleLayout;
    @BindView(R.id.choose_single_inner_bg_layout)
    RelativeLayout chooseSingleInnerBgLayout;
    @BindView(R.id.choose_matcher_layout)
    RelativeLayout chooseMatcherLayout;
    @BindView(R.id.choose_matcher_inner_bg_layout)
    RelativeLayout chooseMatcherInnerBgLayout;

    @BindView(R.id.welcome_page_bubble_outer_layout)
    LinearLayout welcomeBubbleOuterLayout;
    @BindView(R.id.welcome_page_bubble_left_layout)
    RelativeLayout welcomeBubbleLeftLayout;
    @BindView(R.id.welcome_page_bubble_right_layout)
    RelativeLayout getWelcomeBubbleRightLayout;

    @BindView(R.id.welcome_page_bubble_bg_layout)
    BubbleLayout bubbleLayout;
    @BindView(R.id.confirm_user_role_btn)
    Button confirmUserRoleBtn;

    @BindView(R.id.welcome_page_sv)
    ScrollView welcomeSv;
    private GridView singleSuggestGv;
    private HorizontalScrollView horizontalScrollView;
    private LinearLayout matcherHoriLayout;
    private int matcherHoriMargin;

    private int roleType = Role.UNKNOWN;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public OpenRelationPresenter onCreatePresenter() {
        return new OpenRelationPresenter();
    }

    @OnClick(R.id.choose_matcher_layout)
    public void onRelationMatcherClick(View view) {
        roleType = Role.MATCHER;
        bubbleLayout.setArrowPosition(ScreenUtil.dip2px(234));
        confirmUserRoleBtn.setText(R.string.str_welcome_page_confirm_matcher_role);
        welcomeBubbleLeftLayout.setVisibility(View.GONE);
        getWelcomeBubbleRightLayout.setVisibility(View.VISIBLE);

        chooseSingleLayout.setBackgroundColor(Color.TRANSPARENT);
        chooseSingleInnerBgLayout.setBackgroundResource(R.drawable.shap_dark_blue_bg);
        chooseMatcherLayout.setBackgroundResource(R.drawable.shape_white_radius_40dp);
        chooseMatcherInnerBgLayout.setBackgroundResource(R.drawable.shap_purple_bg);
        performRelationChoose();
        mPresenter.getUserShowcaseList(Role.MATCHER, UserRoleSelectedActivity.this);
    }

    @OnClick(R.id.choose_single_layout)
    public void onRelationSingleClick(View view) {
        roleType = Role.SINGLE;
        bubbleLayout.setArrowPosition(ScreenUtil.dip2px(75));
        confirmUserRoleBtn.setText(R.string.str_welcome_page_confirm_single_role);
        welcomeBubbleLeftLayout.setVisibility(View.VISIBLE);
        getWelcomeBubbleRightLayout.setVisibility(View.GONE);
        chooseSingleLayout.setBackgroundResource(R.drawable.shape_white_radius_40dp);
        chooseSingleInnerBgLayout.setBackgroundResource(R.drawable.shap_purple_bg);
        chooseMatcherLayout.setBackgroundColor(Color.TRANSPARENT);
        chooseMatcherInnerBgLayout.setBackgroundResource(R.drawable.shap_dark_blue_bg);
        performRelationChoose();
        mPresenter.getUserShowcaseList(Role.SINGLE, UserRoleSelectedActivity.this);
    }

    @OnClick(R.id.confirm_user_role_btn)
    public void onConfirmClick(View view) {
        mPresenter.chooseRole(roleType, new OpenRelationContract.OnRoleChooseListener() {
            @Override
            public void onRoleChoose(boolean isOk, int role) {
                if (!isOk) {
                    return;
                }
                switch (role) {
                    case Role.MATCHER:
                        if (HereUser.getInstance().getLoginInfo().isAlreadyBindWechat()) {
                            mPresenter.openRole(role, UserRoleSelectedActivity.this);
                        } else {
                            OpenMatcherRelationActivity.startActivity(UserRoleSelectedActivity.this);
                            UserRoleSelectedActivity.this.finish();
                        }
                        break;

                    case Role.SINGLE:
                        OpenSingleRelationActivity.startActivity(UserRoleSelectedActivity.this, false);
                        UserRoleSelectedActivity.this.finish();
                        break;
                }

            }
        });
    }

    private void performRelationChoose() {
        boolean isFirstTime = false;
        if (welcomeBubbleOuterLayout.getVisibility() == View.GONE) {
            isFirstTime = true;
        }
        welcomeBubbleOuterLayout.setVisibility(View.VISIBLE);
        if (isFirstTime) {
            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    welcomeSv.smoothScrollTo(0, welcomeSv.getChildAt(0).getHeight());
                }
            }, 200);
        }
    }

    public void initSingleSuggest(List<ShowCaseUserInfo> userInfoList) {
        singleSuggestGv = findViewById(R.id.welcome_page_bubble_left_single_suggest_gv);
        SingleSuggestAdapter singleSuggestAdapter = new SingleSuggestAdapter(UserRoleSelectedActivity.this, userInfoList);
        singleSuggestGv.setAdapter(singleSuggestAdapter);
    }

    public void initMatcherSuggest(List<ShowCaseUserInfo> userInfoList) {
        matcherHoriMargin = (ScreenUtil.getDisplayWidth() - ScreenUtil.dip2px(
                324)) / 2
                + ScreenUtil.dip2px(24);

        horizontalScrollView = findViewById(R.id.welcome_page_bubble_right_matcher_suggest_hsv);
        matcherHoriLayout = findViewById(R.id.welcome_page_bubble_right_matcher_suggest_lv);

        matcherSuggestItem(userInfoList);
    }

    public void matcherSuggestItem(List<ShowCaseUserInfo> userInfoList) {
        if (userInfoList == null) {
            return;
        }
        matcherHoriLayout.removeAllViews();
        for (int i = 0; i < userInfoList.size(); i++) {
            ShowCaseUserInfo userInfo = userInfoList.get(i);
            View convertView = LayoutInflater.from(UserRoleSelectedActivity.this).inflate(
                    R.layout.adapter_matcher_suggest, null);

            ViewHolder holder = new ViewHolder();
            holder.userPhotoIv = convertView.findViewById(R.id.user_photo_iv);
            holder.userPhotoIv.init(userInfo.getUserInfo(), false);
            holder.userNameTv = convertView.findViewById(R.id.user_name_tv);
            holder.userTitleTv = convertView.findViewById(R.id.user_title_tv);
            holder.singleCountTv = convertView.findViewById(R.id.user_single_count_tv);

            convertView.setTag(holder);

            holder.userNameTv.setText(userInfo.getUserInfo().getNickname());
            holder.userTitleTv.setText(MyTextUtil.getLimitEllipseText(GenderUtil.getGender(userInfo.getUserInfo().getSex()) + "," + userInfo.getUserInfo().getAge() + "," + userInfo.getUserInfo().getProvince(), 8));
            holder.singleCountTv.setText(userInfo.getSingleCount() + getResources().getString(R.string.str_welcome_page_matcher_single_count_text));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ScreenUtil.dip2px(
                    128), ScreenUtil.dip2px(156));
            if (i == 0) {
                params.leftMargin = matcherHoriMargin;
            } else if (i == userInfoList.size() - 1) {
                params.leftMargin = ScreenUtil.dip2px(9);
                params.rightMargin = matcherHoriMargin;
            } else {
                params.leftMargin = ScreenUtil.dip2px(9);
            }
            convertView.setLayoutParams(params);
            matcherHoriLayout.addView(convertView);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFinishActivity(EventFinishActivity event) {
        finish();
    }

    @Override
    public void onGetUserShowcaseList(int role, List<ShowCaseUserInfo> userInfoList) {
        if (role == Role.SINGLE) {
            initSingleSuggest(userInfoList);
        } else {
            initMatcherSuggest(userInfoList);
        }
    }

    @Override
    public void onOpenRole(boolean isOk) {
        this.finish();
        AppInitializer.getInstance().startMainAndLoadPreData(this);
    }

    class ViewHolder {
        AvatarImageView userPhotoIv;
        TextView userNameTv;
        TextView userTitleTv;
        TextView singleCountTv;
    }
}