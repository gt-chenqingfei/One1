package com.oneone.modules.qa.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oneone.BaseActivity;
import com.oneone.R;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;
import com.oneone.framework.ui.utils.ScreenUtil;
import com.oneone.modules.qa.QaDataManager;
import com.oneone.modules.qa.beans.MatchForClassify;
import com.oneone.modules.qa.beans.QuestionAnswerMeAndTargetUserBean;
import com.oneone.modules.qa.beans.QuestionClassify;
import com.oneone.modules.qa.beans.QuestionData;
import com.oneone.modules.qa.beans.QuestionItem;
import com.oneone.modules.qa.contract.QaContract;
import com.oneone.modules.qa.presenter.QaPresenter;
import com.oneone.utils.ImageHelper;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by here on 18/4/19.
 */

@ToolbarResource(title = R.string.str_my_qa_page_title_text, background = R.color.blue, navigationIcon = R.drawable.white_left_arrow)
@LayoutResource(R.layout.activity_my_qa_must)
public class MyQaMustActivity extends BaseActivity<QaPresenter, QaContract.View> implements QaContract.View {
    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, MyQaMustActivity.class));
    }

    @BindView(R.id.question_layout)
    RelativeLayout questionLayout;
    @BindView(R.id.border_layout)
    RelativeLayout borderLayout;
    @BindView(R.id.page_count_tv)
    TextView pageCountTv;
    @BindView(R.id.qa_theme_iv)
    ImageView qaThemeIv;
    @BindView(R.id.qa_theme_tv)
    TextView qaThemeTv;
    @BindView(R.id.qa_title_tv)
    TextView qaTitleTv;
    @BindView(R.id.qa_choose_item_layout)
    LinearLayout qaChooseItemLayout;
    @BindView(R.id.previous_question_tv)
    TextView previousQuestionTv;

    @BindView(R.id.finish_layout)
    RelativeLayout finishLayout;
    @BindView(R.id.qa_finish_tv)
    TextView finishTv;
    @BindView(R.id.continue_btn)
    Button continueBtn;
    @BindView(R.id.see_my_answered_tv)
    TextView seeMyAnsweredTv;

    //    private List<QuestionClassify> questionClassifies;
//    private List<QuestionData> unAnswerQuestionList;
    private int answeredCount = 0;
    private int currentQuestionIndex = 0;

    private String curQuestionId = "";
    private String curAnswerId = "";

    private List<QuestionData> answeredQuestionList = new ArrayList<QuestionData>();

    private boolean chooseLock = false;

    @Override
    public QaPresenter onCreatePresenter() {
        return new QaPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTitleView().setTextColor(getResources().getColor(R.color.color_white));

        initView();

        initQuestion(QaDataManager.getInstance(getActivityContext()).getQuestions().get(currentQuestionIndex));
    }

    public void initView() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) borderLayout.getLayoutParams();
        params.width = (int) (ScreenUtil.WIDTH_RATIO * 280);
        params.height = (int) (ScreenUtil.WIDTH_RATIO * 463);

        params = (RelativeLayout.LayoutParams) finishLayout.getLayoutParams();
        params.width = (int) (ScreenUtil.WIDTH_RATIO * 280);
        params.height = (int) (ScreenUtil.WIDTH_RATIO * 463);

        previousQuestionTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentQuestionIndex--;
                if (currentQuestionIndex >= 0) {
                    initQuestion(QaDataManager.getInstance(getActivityContext()).getQuestions().get(currentQuestionIndex));
                } else {
                    currentQuestionIndex = 0;
                }
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyQaActivity.startActivity(MyQaMustActivity.this, 0);
                MyQaMustActivity.this.finish();
            }
        });

        seeMyAnsweredTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyQaActivity.startActivity(MyQaMustActivity.this, 1);
                MyQaMustActivity.this.finish();
            }
        });
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

    private void initNextQuestion() {
        if (currentQuestionIndex >= (QaDataManager.getInstance(getActivityContext()).getQuestions().size() - 1)) {
            questionLayout.setVisibility(View.GONE);
            finishLayout.setVisibility(View.VISIBLE);
        } else {
            currentQuestionIndex++;
            if (currentQuestionIndex >= QaDataManager.getInstance(getActivityContext()).getQuestions().size()) {
                currentQuestionIndex = 0;
            }

            initQuestion(QaDataManager.getInstance(getActivityContext()).getQuestions().get(currentQuestionIndex));
        }
    }

    private void initQuestion(QuestionData questionData) {
        if (currentQuestionIndex != 0)
            previousQuestionTv.setVisibility(View.VISIBLE);
        else
            previousQuestionTv.setVisibility(View.GONE);

        QuestionClassify classify = QaDataManager.getInstance(this).getClassifyById(questionData.getQuestion().getClassifyId());

        curQuestionId = questionData.getQuestion().getQuestionId();
        curAnswerId = "";
        qaChooseItemLayout.removeAllViews();
        qaChooseItemLayout.setTag(questionData);

        if (classify != null) {
            qaThemeTv.setText(classify.getClassifyName());
            ImageHelper.displayImage(MyQaMustActivity.this, qaThemeIv, classify.getClassifyIcon());
        }
        pageCountTv.setText(Html.fromHtml(currentQuestionIndex + 1 + "/" + QaDataManager.getInstance(getActivityContext()).getQuestions().size()));
        qaTitleTv.setText(questionData.getQuestion().getContent());

        for (int i = 0; i < questionData.getAnswerList().size(); i++) {
            addChooseItem(MyQaMustActivity.this, questionData.getAnswerList().get(i), qaChooseItemLayout);
        }
    }

    public void addChooseItem(Context context, QuestionItem questionItem, final LinearLayout parentLayout) {
        int childPos = parentLayout.getChildCount();

        TextView tv = new TextView(context);
        if (questionItem.getAnsweredTag()) {
            curAnswerId = questionItem.getAnswerId();
            tv.setTextColor(context.getResources().getColor(R.color.white));
            tv.setBackgroundResource(R.drawable.shap_blue_bg_3);
        } else {
            tv.setTextColor(context.getResources().getColor(R.color.text_blue_1));
            tv.setBackgroundResource(R.drawable.shap_green_blue_bg_2);
        }
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setPadding(DensityUtil.dp2px(18), 0, 0, 0);
        char c = (char) (childPos + 65);
        tv.setText(c + " " + questionItem.getContent());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (ScreenUtil.WIDTH_RATIO * 46));
        if (childPos != 0)
            params.topMargin = DensityUtil.dp2px(8);
        tv.setLayoutParams(params);

        tv.setTag(questionItem);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!chooseLock) {
                    chooseLock = true;
                    chooseItem((TextView) view, parentLayout);
                }
            }
        });

        parentLayout.addView(tv);
    }

    private void chooseItem(TextView itemTv, LinearLayout parentLayout) {
        for (int i = 0; i < parentLayout.getChildCount(); i++) {
            TextView otherItem = (TextView) parentLayout.getChildAt(i);
            if (otherItem != itemTv) {
                otherItem.setTextColor(getActivityContext().getResources().getColor(R.color.text_blue_1));
                otherItem.setBackgroundResource(R.drawable.shap_green_blue_bg_2);
                QuestionItem item = (QuestionItem) otherItem.getTag();
                item.setAnsweredTag(false);
            }
        }

        itemTv.setTextColor(getResources().getColor(R.color.white));
        itemTv.setBackgroundResource(R.drawable.shap_blue_bg_3);
        QuestionItem item = (QuestionItem) itemTv.getTag();
        curAnswerId = item.getAnswerId();
        item.setAnsweredTag(true);

        mPresenter.qaAnswer(curQuestionId, curAnswerId);
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
        for (QuestionData questionData : QaDataManager.getInstance(getActivityContext()).getQuestions()) {
            if (questionData.getQuestion().getQuestionId().equals(questionId)) {
                if (!answeredQuestionList.contains(questionData))
                    answeredQuestionList.add(questionData);
            }
        }
        initNextQuestion();

        chooseLock = false;
    }

    @Override
    public void qaMatchValue(List<MatchForClassify> matchForClassify, int matchForAll) {

    }

    @Override
    public void qaAnswersFortarget(int count, ArrayList<QuestionAnswerMeAndTargetUserBean> list) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        QaDataManager.getInstance(getActivityContext()).getQuestions().removeAll(answeredQuestionList);
    }
}
