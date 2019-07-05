package com.oneone.modules.qa.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.framework.ui.BaseRecyclerViewAdapter;
import com.oneone.framework.ui.BaseViewHolder;
import com.oneone.modules.qa.beans.QuestionData;
import com.oneone.modules.qa.beans.QuestionItem;
import com.oneone.modules.qa.dialog.ModifyQaDialog;
import com.scwang.smartrefresh.layout.util.DensityUtil;


import butterknife.BindView;

/**
 * Created by here on 18/4/17.
 */

public class QaMyAnsweredAdapter extends BaseRecyclerViewAdapter <QuestionData> {
    private QaMyAnsweredAdapterListener listener;

    public QaMyAnsweredAdapter(QaMyAnsweredAdapterListener listener) {
        super(listener);
        this.listener = listener;
    }

    public abstract static class QaMyAnsweredAdapterListener implements BaseViewHolder.ItemClickListener {
        public void modifyQuestion (QuestionData questionData, QaViewHolder holder) {

        }
    }

    @Override
    public BaseViewHolder<QuestionData> onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewGroup = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_qa_my_already_answered, parent, false);
        return new QaViewHolder(viewGroup);
    }


    public class QaViewHolder extends BaseViewHolder<QuestionData> implements View.OnClickListener {
        @BindView(R.id.qa_title_tv)
        TextView titleTv;
        @BindView(R.id.modify_qa_btn)
        Button modifyBtn;
        @BindView(R.id.answer_rlt_item_layout)
        LinearLayout answerLayout;


        protected QaViewHolder(View v) {
            super(v);
            modifyBtn.setOnClickListener(this);
        }

        @Override
        public void bind(QuestionData questionData, int position) {
            super.bind(questionData, position);

            if (questionData == null) {
                return;
            }

            refreshQuestionItem(this, questionData);
        }

        @Override
        public void onClick(final View v) {
            switch (v.getId()) {
                case R.id.modify_qa_btn:
                    QuestionData questionData = (QuestionData) v.getTag();
                    ModifyQaDialog dialog = new ModifyQaDialog(this, questionData, new ModifyQaDialog.ItemClickListener() {
                        @Override
                        public QuestionItem onItemClick(QuestionData questionData, QaViewHolder holder) {
                            listener.modifyQuestion(questionData, holder);
                            refreshQuestionItem(holder, questionData);
                            return null;
                        }
                    });
                    dialog.show();
                    break;
            }
        }
    }

    public void refreshQuestionItem (QaViewHolder holder, QuestionData questionData) {
        holder.modifyBtn.setTag(questionData);
        holder.titleTv.setText(questionData.getQuestion().getContent());
        holder.answerLayout.removeAllViews();
        TextView answeredTv = null;
        for (QuestionItem item : questionData.getAnswerList()) {
            TextView tv = createAnswerItem(holder.getContext(), holder.answerLayout, item, questionData.getUserAnswerId());
            if (tv != null)
                answeredTv = tv;
        }
        if (answeredTv != null)
            holder.answerLayout.addView(answeredTv, 0);
    }

    public TextView createAnswerItem (Context context, LinearLayout parentLayout,QuestionItem item, String answerId) {
        TextView itemTv = new TextView(context);
        itemTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        itemTv.setText(item.getContent());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = DensityUtil.dp2px(12);
        itemTv.setLayoutParams(params);
        if (item.getAnswerId().equals(answerId)) {
            itemTv.setTextColor(context.getResources().getColor(R.color.blue));
            return itemTv;
        } else {
            itemTv.setTextColor(context.getResources().getColor(R.color.text_blue_1));
            itemTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }

        parentLayout.addView(itemTv);
        return null;
    }
}
