package com.oneone.modules.matcher.relations.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.oneone.BaseActivity;
import com.oneone.R;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;
import com.oneone.framework.ui.utils.ScreenUtil;
import com.oneone.modules.matcher.relations.bean.SingleInfo;
import com.oneone.modules.matcher.relations.contract.SinglesContract;
import com.oneone.modules.matcher.relations.presenter.SinglesPresenter;
import com.oneone.utils.ToastUtil;
import com.oneone.widget.AutoFlowView;
import com.oneone.widget.InputTextWatcher;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * @author qingfei.chen
 * @since 2018/4/23.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@Route(path = "/endorsements/edit")
@ToolbarResource(navigationIcon = R.drawable.ic_btn_back_light, background = R.color.color_796CF0, title = R.string.empty_str)
@LayoutResource(R.layout.activity_matcher_relation_set)
public class MatcherRelationSetActivity extends BaseActivity<SinglesPresenter, SinglesContract.View>
        implements View.OnClickListener, SinglesContract.View
        ,SinglesContract.OnMatcherSaidListener,SinglesContract.OnMatcherRelationListener,SinglesContract.OnGetMatherRelationTagsListener{
    public static final String EXTRA_USER_ID = "userId";
    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_MATCHER_SAID = "matcher_said";
    public static final String EXTRA_RELATION = "relation";
    public static final int STEP_RELATION = 1;
    public static final int STEP_MATCHER_SAID = 2;
    private final int INPUT_MAX_NUM = 300;

    public static void launch(Context context, String userId, String name, String matcherSaid,
                              String relation) {
        Intent intent = new Intent(context, MatcherRelationSetActivity.class);
        intent.putExtra(EXTRA_USER_ID, userId);
        intent.putExtra(EXTRA_NAME, name);
        intent.putExtra(EXTRA_MATCHER_SAID, matcherSaid);
        intent.putExtra(EXTRA_RELATION, relation);
        context.startActivity(intent);
    }

    @BindView(R.id.activity_matcher_relation_set_tv_title)
    TextView mTvTitle;

    @BindView(R.id.activity_matcher_relation_set_button_next_and_complete)
    Button mBtnNextAntComplete;

    @BindView(R.id.activity_matcher_relation_set_et_matcher_said)
    EditText mEtMatcherSaid;
    @BindView(R.id.tv_matcher_relation_input_num_hint)
    TextView mTvInputNumHint;

    @BindView(R.id.activity_matcher_relation_set_ll_relation)
    RelativeLayout mLLRelation;

    @BindView(R.id.activity_matcher_relation_set_ll_matcher_said)
    LinearLayout mLLMatcherSaid;

    @BindView(R.id.activity_matcher_relation_set_progress_view)
    View mProgress;

    @BindView(R.id.activity_matcher_relation_set_flow_layout_tags_container)
    AutoFlowView mFlowLayout;

    @Autowired
    String userId;
    private String mName;
    private String mRelation;
    private String mMatcherSaid;
    private int mCurrentStep = STEP_RELATION;

    @Override
    public SinglesPresenter onCreatePresenter() {
        return new SinglesPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);

        mFlowLayout.setLimit(5)
                .setHintText(R.string.str_dialog_customer_relation_et_hint)
                .setToastText(R.string.toast_matcher_relation_set_relation_add_tip);

//        mFlowLayout.setLimit(10)
//                .setHintText(R.string.str_set_single_flow_page_input_max_count_10)
//                .setItemLayoutRes(R.layout.view_profile_step_tag_text)
//                .setAddEditTextBackground(R.drawable.selector_step_5_tag_bg)
//                .setAddEditTextTextColor(R.color.color_selector_step_5_text)
//                .setMaxSelected(3)
//                .setAddBtnText(R.string.str_set_single_flow_page_custom_tag)
//                .setAddBtnColor(R.color.single_flow_content_bg_5).setListener(this);

        mTvTitle.setText(String.format(getString(R.string.str_matcher_relations_set_title_format), mName));
        mEtMatcherSaid.setText(mMatcherSaid);
        if (!TextUtils.isEmpty(mMatcherSaid)) mTvInputNumHint.setText(mMatcherSaid.length() + "");
        mEtMatcherSaid.addTextChangedListener(new InputTextWatcher(this, mEtMatcherSaid, INPUT_MAX_NUM, 0,
                new InputTextWatcher.CurrentInputTextListener() {
            @Override
            public void onCurrentInputTextListener(int num) {
                mTvInputNumHint.setText(num + "");
            }
        }));
        refreshViewByStep();
        mPresenter.getMatcherRelationTag(userId,this);
    }

    @Override
    public void onInitListener() {
        super.onInitListener();
        mBtnNextAntComplete.setOnClickListener(this);
    }

    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);
        userId = intent.getStringExtra(EXTRA_USER_ID);
        mName = intent.getStringExtra(EXTRA_NAME);
        mRelation = intent.getStringExtra(EXTRA_RELATION);
        mMatcherSaid = intent.getStringExtra(EXTRA_MATCHER_SAID);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_matcher_relation_set_button_next_and_complete:
                performNextBtn();
                break;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() != KeyEvent.ACTION_UP) {
            return super.dispatchKeyEvent(event);
        }
        if (event.getKeyCode() != KeyEvent.KEYCODE_BACK) {
            return super.dispatchKeyEvent(event);
        }

        if (mCurrentStep == STEP_RELATION) {
            return super.dispatchKeyEvent(event);
        }

        performPreStep();
        return true;
    }

    @Override
    public void onMatcherSaid() {
        this.finish();
    }

    @Override
    public void onMatcherRelation() {
        performNextStep();
    }

    @Override
    public void onMatherRelationTags(List<String> relationTags) {
        fillRelation2Display(relationTags);
    }

    private void performNextStep() {
        mCurrentStep++;
        refreshViewByStep();
    }

    private void performPreStep() {
        mCurrentStep--;
        refreshViewByStep();
    }


    private void fillRelation2Display(List<String> relationTags) {
        if (relationTags == null) {
            return;
        }
        List<String> preSelected = null;
        if (!TextUtils.isEmpty(mRelation)) {
            preSelected = Arrays.asList(new String[]{mRelation});
        }
        mFlowLayout.setPreSelectedArray(preSelected);
        mFlowLayout.notifyDataChange(relationTags);
    }

    private void refreshViewByStep() {
        switch (mCurrentStep) {
            case STEP_RELATION:
                mLLRelation.setVisibility(View.VISIBLE);
                mLLMatcherSaid.setVisibility(View.GONE);
                mBtnNextAntComplete.setText(R.string.str_next);
                mProgress.getLayoutParams().width = ScreenUtil.getDisplayWidth() / 2;
                mProgress.setLayoutParams(mProgress.getLayoutParams());
                break;

            case STEP_MATCHER_SAID:
                mLLRelation.setVisibility(View.GONE);
                mLLMatcherSaid.setVisibility(View.VISIBLE);
                mBtnNextAntComplete.setText(R.string.str_completed);
                mProgress.getLayoutParams().width = ScreenUtil.getDisplayWidth();
                mProgress.setLayoutParams(mProgress.getLayoutParams());
                break;
        }
    }

    private void performNextBtn() {
        switch (mCurrentStep) {
            case STEP_RELATION:
                List<String> selectedRelation = mFlowLayout.getSelectedValue();

                if (selectedRelation == null || selectedRelation.isEmpty()) {
                    ToastUtil.show(this,
                            getString(R.string.toast_matcher_relation_set_relation_add_tip));
                    return;
                }

                String selected = selectedRelation.get(0);

                if (TextUtils.equals(selected, mRelation)) {
                    performNextStep();
                    return;
                }
                mPresenter.matcherRelation(userId, selected,this);
                break;

            case STEP_MATCHER_SAID:
                String matcherSaid = mEtMatcherSaid.getText().toString();
                if (TextUtils.isEmpty(matcherSaid)) {
                    ToastUtil.show(this, getString(R.string.toast_matcher_relation_set_not_null_tip));
                    return;
                }
                mPresenter.matcherSaid(userId, matcherSaid,this);
                break;
        }
    }

}
