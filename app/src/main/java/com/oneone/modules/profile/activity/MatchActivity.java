package com.oneone.modules.profile.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oneone.BaseActivity;
import com.oneone.R;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;
import com.oneone.modules.profile.util.InTuneQaAnswerUtil;
import com.oneone.modules.qa.beans.MatchForClassify;
import com.oneone.modules.qa.beans.QuestionAnswerMeAndTargetUserBean;
import com.oneone.modules.qa.beans.QuestionClassify;
import com.oneone.modules.qa.beans.QuestionData;
import com.oneone.modules.qa.contract.QaContract;
import com.oneone.modules.qa.presenter.QaPresenter;
import com.oneone.modules.user.bean.UserInfo;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by here on 18/4/15.
 */

@ToolbarResource(navigationIcon = R.drawable.white_left_arrow, title = R.string.empty_str, background = R.color.color_transparent)
@LayoutResource(R.layout.activity_match)
public class MatchActivity extends BaseActivity<QaPresenter, QaContract.View> implements QaContract.View {
    public static void startActivity(Context context, UserInfo tarUserInfo) {
        Intent it = new Intent(context, MatchActivity.class);
        it.putExtra("tarUserInfo", tarUserInfo);
        context.startActivity(it);
    }

    @BindView(R.id.in_tune_val_tv)
    TextView inTuneValTv;

    @BindView(R.id.view_point_of_love_val_layout)
    RelativeLayout viewPointOfLoveValLayout;
    @BindView(R.id.view_point_of_love_val_tv)
    TextView viewPointOfLoveValTv;
    @BindView(R.id.view_point_of_love_val_iv)
    ImageView viewPointOfLoveValIv;

    @BindView(R.id.view_point_of_character_val_layout)
    RelativeLayout viewPointOfCharacterValLayout;
    @BindView(R.id.view_point_of_character_val_tv)
    TextView viewPointOfCharacterValTv;
    @BindView(R.id.view_point_of_character_val_iv)
    ImageView viewPointOfCharacterValIv;

    @BindView(R.id.view_point_of_moral_val_layout)
    RelativeLayout viewPointOfMoralValLayout;
    @BindView(R.id.view_point_of_moral_val_tv)
    TextView viewPointOfMoralValTv;
    @BindView(R.id.view_point_of_moral_val_iv)
    ImageView viewPointOfMoralValIv;

    @BindView(R.id.view_point_of_live_habit_val_layout)
    RelativeLayout viewPointOfLiveHabitValLayout;
    @BindView(R.id.view_point_of_live_habit_val_tv)
    TextView viewPointOfLiveHabitValTv;
    @BindView(R.id.view_point_of_live_habit_val_iv)
    ImageView viewPointOfLiveHabitValIv;

    @BindView(R.id.answer_ll)
    LinearLayout answerLayout;
    @BindView(R.id.already_answer_lv)
    ListView alreadyAnswerLv;
    @BindView(R.id.not_answer_lv)
    ListView notAnswerLv;

    private static final int MAX_VAL_HEIGHT = DensityUtil.dp2px(135);
    private static final int FROM_VAL = DensityUtil.dp2px(32);
    private static final float PER_HEIGHT = (float) MAX_VAL_HEIGHT / 100f;

    private InTuneQaAnswerUtil qaUtil;

    private UserInfo tarUserInfo;

    @Override
    public QaPresenter onCreatePresenter() {
        return new QaPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tarUserInfo = (UserInfo) getIntent().getExtras().get("tarUserInfo");

        mPresenter.qaMatchValue(tarUserInfo.getUserId());
        mPresenter.qaAnswersFortarget(tarUserInfo.getUserId(), 0, 500);
    }

    public void initView(List<MatchForClassify> matchForClassifyList, int matchForAll) {
        inTuneValTv.setText(matchForAll + "%");

        for (int i = 0;i < matchForClassifyList.size();i++) {
            MatchForClassify matchForClassify = matchForClassifyList.get(i);
            initInTuneVal(matchForClassify.getMatchValue(), i);
            initInTuneVal(matchForClassify.getMatchValue(), i);
            initInTuneVal(matchForClassify.getMatchValue(), i);
            initInTuneVal(matchForClassify.getMatchValue(), i);
        }
    }

    public void initInTuneVal(int val, int type) {
        switch (type) {
            case 0:
                viewPointOfLoveValLayout.getLayoutParams().height = FROM_VAL + (int) (val * PER_HEIGHT);
                viewPointOfLoveValTv.setText(val + "%");
                initValImg(viewPointOfLoveValIv, val);
                break;
            case 1:
                viewPointOfCharacterValLayout.getLayoutParams().height = FROM_VAL + (int) (val * PER_HEIGHT);
                viewPointOfCharacterValTv.setText(val + "%");
                initValImg(viewPointOfCharacterValIv, val);
                break;
            case 2:
                viewPointOfMoralValLayout.getLayoutParams().height = FROM_VAL + (int) (val * PER_HEIGHT);
                viewPointOfMoralValTv.setText(val + "%");
                initValImg(viewPointOfMoralValIv, val);
                break;
            case 3:
                viewPointOfLiveHabitValLayout.getLayoutParams().height = FROM_VAL + (int) (val * PER_HEIGHT);
                viewPointOfLiveHabitValTv.setText(val + "%");
                initValImg(viewPointOfLiveHabitValIv, val);
                break;
        }
    }

    public void initValImg(ImageView iv, int val) {
        if (val >= 0) {
            if (val <= 40) {
                iv.setBackgroundResource(R.drawable.in_tune_val_lv_1);
            } else if (val <= 70) {
                iv.setBackgroundResource(R.drawable.in_tune_val_lv_2);
            } else if (val <= 85) {
                iv.setBackgroundResource(R.drawable.in_tune_val_lv_3);
            } else {
                iv.setBackgroundResource(R.drawable.in_tune_val_lv_4);
            }
        }
    }

    public void initAnswerFortarget (int count, ArrayList<QuestionAnswerMeAndTargetUserBean> list) {
        qaUtil = new InTuneQaAnswerUtil(tarUserInfo, new InTuneQaAnswerUtil.AnswerQaListener() {
            @Override
            public void answerQa(String curQuestionId, String curAnswerId) {
                mPresenter.qaAnswer(curQuestionId, curAnswerId);
            }
        });
        ArrayList<View> viewList = qaUtil.getAnswerViewList(MatchActivity.this, list);
        for (View v : viewList) {
            answerLayout.addView(v);
        }
    }


    @Override
    public void qaCountForall(int count, int answered) {

    }

    @Override
    public void qaCountInfo(List<QuestionClassify> questionClassifies) {

    }

    @Override
    public void qaUnAnswerMust(int count, List<QuestionData> list) {

    }

    @Override
    public void qaUnAnswerClassify(List<QuestionData> list) {

    }

    @Override
    public void qaUnAnswerAll(int count, List<QuestionData> list) {

    }

    @Override
    public void qaAnswered(boolean isLoadMore, int count, List<QuestionData> list) {

    }

    @Override
    public void qaAnswer(String questionId) {
        mPresenter.qaMatchValue(tarUserInfo.getUserId());
    }

    @Override
    public void qaMatchValue(List<MatchForClassify> matchForClassifyList, int matchForAll) {
        initView(matchForClassifyList, matchForAll);
    }

    @Override
    public void qaAnswersFortarget(int count, ArrayList<QuestionAnswerMeAndTargetUserBean> list) {
        initAnswerFortarget(count, list);
    }
}
