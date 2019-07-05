package com.oneone.modules.qa.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.framework.ui.BaseDialog;
import com.oneone.framework.ui.utils.ScreenUtil;
import com.oneone.modules.qa.adapter.QaMyAnsweredAdapter;
import com.oneone.modules.qa.beans.QuestionData;
import com.oneone.modules.qa.beans.QuestionItem;
import com.scwang.smartrefresh.layout.util.DensityUtil;

/**
 * Created by here on 18/4/18.
 */

public class ModifyQaDialog extends BaseDialog {
    private TextView titleTv;
    private LinearLayout answerItemLayout;
    private ItemClickListener listener;

    private QuestionData questionData;
    private QaMyAnsweredAdapter.QaViewHolder holder;

    public interface ItemClickListener {
        QuestionItem onItemClick(QuestionData questionData, QaMyAnsweredAdapter.QaViewHolder holder);
    }

    public ModifyQaDialog(Context context, QuestionData questionData, ItemClickListener listener) {
        super(context, com.oneone.framework.ui.R.style.base_dialog);
        this.questionData = questionData;
        this.listener = listener;
    }

    public ModifyQaDialog(QaMyAnsweredAdapter.QaViewHolder holder, QuestionData questionData, ItemClickListener listener) {
        super(holder.getContext(), com.oneone.framework.ui.R.style.base_dialog);
        this.questionData = questionData;
        this.listener = listener;
        this.holder = holder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_qa_modify);

        titleTv = findViewById(R.id.qa_title_tv);
        answerItemLayout = findViewById(R.id.qa_answer_item_layout);

        initView();
    }

    private void initView () {
        if (questionData != null) {
            titleTv.setText(questionData.getQuestion().getContent());

            answerItemLayout.removeAllViews();
            for (QuestionItem questionItem : questionData.getAnswerList()) {
                addQuestionItem(questionData, questionItem);
            }
        }
    }

    private void addQuestionItem (final QuestionData questionData, final QuestionItem questionItem) {
        int childPos = answerItemLayout.getChildCount();

        TextView itemTv = new TextView(getContext());
        if (questionData.getUserAnswerId().equals(questionItem.getAnswerId())) {
            itemTv.setTextColor(getContext().getResources().getColor(R.color.white));
            itemTv.setBackgroundResource(R.drawable.shap_blue_bg_3);
        } else {
            itemTv.setTextColor(getContext().getResources().getColor(R.color.text_blue_1));
            itemTv.setBackgroundResource(R.drawable.shap_green_blue_bg_2);
        }
        itemTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        itemTv.setGravity(Gravity.CENTER_VERTICAL);
        itemTv.setPadding(DensityUtil.dp2px(18),0,0,0);
        char c = (char) (childPos + 65);
        itemTv.setText(c + " " + questionItem.getContent());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (ScreenUtil.WIDTH_RATIO * 46));
        if (childPos != 0)
            params.topMargin = DensityUtil.dp2px(8);
        itemTv.setLayoutParams(params);
        itemTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questionData.setUserAnswerId(questionItem.getAnswerId());
                listener.onItemClick(questionData, holder);
                ModifyQaDialog.this.dismiss();
            }
        });
        answerItemLayout.addView(itemTv);
    }

}
