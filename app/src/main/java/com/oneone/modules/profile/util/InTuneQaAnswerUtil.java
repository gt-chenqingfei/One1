package com.oneone.modules.profile.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.framework.ui.glide.GlideUtils;
import com.oneone.modules.qa.adapter.QaMyAnsweredAdapter;
import com.oneone.modules.qa.beans.Answer;
import com.oneone.modules.qa.beans.Question;
import com.oneone.modules.qa.beans.QuestionAnswerMeAndTargetUserBean;
import com.oneone.modules.qa.beans.QuestionData;
import com.oneone.modules.qa.beans.QuestionItem;
import com.oneone.modules.qa.dialog.ModifyQaDialog;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.utils.ImageHelper;
import com.oneone.widget.AvatarImageView;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.util.ArrayList;

/**
 * Created by here on 18/4/16.
 */

public class InTuneQaAnswerUtil {
    private AnswerQaListener listener;
    private UserInfo tarUserInfo;

    public interface AnswerQaListener {
        public void answerQa(String curQuestionId, String curAnswerId);
    }

    public InTuneQaAnswerUtil (UserInfo tarUserInfo, AnswerQaListener listener) {
        this.listener = listener;
        this.tarUserInfo = tarUserInfo;
    }

    private Context context;

    public ArrayList<View> getAnswerViewList (Context context, ArrayList<QuestionAnswerMeAndTargetUserBean> list) {
        this.context = context;
        ArrayList<View> viewList = new ArrayList<View>();
        LayoutInflater inflater = LayoutInflater.from(context);

        for (int i = 0;i < list.size();i++) {
            QuestionAnswerMeAndTargetUserBean questionAnswerMeAndTargetUserBean = list.get(i);
            if (questionAnswerMeAndTargetUserBean.getSelfAnswerId() != null) {
                viewList.add(getQaAlreadyAnswerView(i, list.get(i), inflater));
            } else {
                viewList.add(getQaNotAnswerView(i, list.get(i), inflater));
            }
        }

        return viewList;
    }

    public View getQaAlreadyAnswerView(int pos, final QuestionAnswerMeAndTargetUserBean questionAnswerMeAndTargetUserBean, LayoutInflater inflater) {
        View convertView = inflater.inflate(R.layout.item_qa_in_tune, null);
        QaAlreadyAnswerViewHolder holder = new QaAlreadyAnswerViewHolder();
        convertView.setTag(holder);
        RelativeLayout answeredLayout = convertView.findViewById(R.id.already_answer_layout);
        RelativeLayout notAnsweredLayout = convertView.findViewById(R.id.item_qa_not_answered_layout);
        answeredLayout.setVisibility(View.VISIBLE);
        notAnsweredLayout.setVisibility(View.GONE);
        holder.qaTitleTv = convertView.findViewById(R.id.qa_title_tv);
        holder.modifyQaBtn = convertView.findViewById(R.id.modify_qa_btn);
        holder.answerRltLayout = convertView.findViewById(R.id.answer_rlt_item_layout);

        holder.qaTitleTv.setText(questionAnswerMeAndTargetUserBean.getQuestion().getContent());

        String myAnswer = "";
        String otherAnswer = "";
        for (QuestionItem answer : questionAnswerMeAndTargetUserBean.getAnswerList()) {
            if (questionAnswerMeAndTargetUserBean.getSelfAnswerId().equals(answer.getAnswerId())) {
                myAnswer = answer.getContent();
            }
            if (questionAnswerMeAndTargetUserBean.getUserAnswerId().equals(answer.getAnswerId())) {
                otherAnswer = answer.getContent();
            }
        }

        final View myAnswerView = initQaAlreadyAnswerRltLayout(HereUser.getInstance().getUserInfo(), myAnswer, inflater);
        View otherAnswerView = initQaAlreadyAnswerRltLayout(tarUserInfo, otherAnswer, inflater);

        if (!questionAnswerMeAndTargetUserBean.getSelfAnswerId().equals(questionAnswerMeAndTargetUserBean.getUserAnswerId())) {
            TextView tv = myAnswerView.findViewById(R.id.answer_rlt_tv);
            tv.setTextColor(context.getResources().getColor(R.color.blue));
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = DensityUtil.dp2px(5);
        otherAnswerView.setLayoutParams(params);

        holder.answerRltLayout.addView(myAnswerView);
        holder.answerRltLayout.addView(otherAnswerView);

        holder.modifyQaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuestionData questionData = new QuestionData();
                questionData.setQuestion(questionAnswerMeAndTargetUserBean.getQuestion());
                questionData.setAnswerList(questionAnswerMeAndTargetUserBean.getAnswerList());
                questionData.setUserAnswerId(questionAnswerMeAndTargetUserBean.getSelfAnswerId());
                ModifyQaDialog qaDialog = new ModifyQaDialog(context, questionData, new ModifyQaDialog.ItemClickListener() {
                    @Override
                    public QuestionItem onItemClick(QuestionData questionData, QaMyAnsweredAdapter.QaViewHolder holder) {
                        questionAnswerMeAndTargetUserBean.setSelfAnswerId(questionData.getUserAnswerId());
                        TextView tv = myAnswerView.findViewById(R.id.answer_rlt_tv);
                        String myAnswer = "";
                        for (QuestionItem answer : questionAnswerMeAndTargetUserBean.getAnswerList()) {
                            if (questionAnswerMeAndTargetUserBean.getSelfAnswerId().equals(answer.getAnswerId())) {
                                myAnswer = answer.getContent();
                            }
                        }
                        tv.setText(myAnswer);
                        if (!questionAnswerMeAndTargetUserBean.getSelfAnswerId().equals(questionAnswerMeAndTargetUserBean.getUserAnswerId())) {
                            tv.setTextColor(context.getResources().getColor(R.color.blue));
                        } else {
                            tv.setTextColor(context.getResources().getColor(R.color.text_blue_1));
                        }

                        listener.answerQa(questionData.getQuestion().getQuestionId(), questionData.getUserAnswerId());
                        return null;
                    }
                });

                qaDialog.show();
            }
        });

        return convertView;
    }

    public View initQaAlreadyAnswerRltLayout (UserInfo userInfo, String itemText, LayoutInflater inflater) {
        String s = HereUser.getInstance().getUserInfo().getMyAvatar();
        View v = inflater.inflate(R.layout.item_show_answer_rlt, null);
        AvatarImageView iv = v.findViewById(R.id.user_photo_iv);
        iv.init(userInfo, true);
//        GlideUtils.loadCircleImage(photoUrl, iv, null);
        TextView tv = v.findViewById(R.id.answer_rlt_tv);
        tv.setTextColor(context.getResources().getColor(R.color.text_blue_1));
        tv.setText(itemText);
        return v;
    }

    class QaAlreadyAnswerViewHolder {
        TextView qaTitleTv;
        Button modifyQaBtn;
        LinearLayout answerRltLayout;
    }




    public View getQaNotAnswerView(int pos, final QuestionAnswerMeAndTargetUserBean questionAnswerMeAndTargetUserBean, final LayoutInflater inflater) {
        final View convertView = inflater.inflate(R.layout.item_qa_in_tune, null);
        QaNotAnswerViewHolder holder = new QaNotAnswerViewHolder();
        convertView.setTag(holder);

        final RelativeLayout answeredLayout = convertView.findViewById(R.id.already_answer_layout);
        final RelativeLayout notAnsweredLayout = convertView.findViewById(R.id.item_qa_not_answered_layout);
        answeredLayout.setVisibility(View.GONE);
        notAnsweredLayout.setVisibility(View.VISIBLE);

        holder.qaTitleTv = convertView.findViewById(R.id.qa_title_tv_2);
        holder.answerBtn = convertView.findViewById(R.id.qa_answer_btn);

        holder.qaTitleTv.setText(questionAnswerMeAndTargetUserBean.getQuestion().getContent());
        holder.answerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuestionData questionData = new QuestionData();
                questionData.setQuestion(questionAnswerMeAndTargetUserBean.getQuestion());
                questionData.setAnswerList(questionAnswerMeAndTargetUserBean.getAnswerList());
                questionData.setUserAnswerId("");
                ModifyQaDialog qaDialog = new ModifyQaDialog(context, questionData, new ModifyQaDialog.ItemClickListener() {
                    @Override
                    public QuestionItem onItemClick(QuestionData questionData, QaMyAnsweredAdapter.QaViewHolder holder) {
                        questionAnswerMeAndTargetUserBean.setSelfAnswerId(questionData.getUserAnswerId());

                        answeredLayout.setVisibility(View.VISIBLE);
                        notAnsweredLayout.setVisibility(View.GONE);


                        TextView qaTitleTv = convertView.findViewById(R.id.qa_title_tv);
                        Button modifyQaBtn = convertView.findViewById(R.id.modify_qa_btn);
                        LinearLayout answerRltLayout = convertView.findViewById(R.id.answer_rlt_item_layout);

                        qaTitleTv.setText(questionAnswerMeAndTargetUserBean.getQuestion().getContent());

                        String myAnswer = "";
                        String otherAnswer = "";
                        for (QuestionItem answer : questionAnswerMeAndTargetUserBean.getAnswerList()) {
                            if (questionAnswerMeAndTargetUserBean.getSelfAnswerId().equals(answer.getAnswerId())) {
                                myAnswer = answer.getContent().trim();
                            }
                            if (questionAnswerMeAndTargetUserBean.getUserAnswerId().equals(answer.getAnswerId())) {
                                otherAnswer = answer.getContent().trim();
                            }
                        }


                        final View myAnswerView = initQaAlreadyAnswerRltLayout(HereUser.getInstance().getUserInfo(), myAnswer, inflater);
                        View otherAnswerView = initQaAlreadyAnswerRltLayout(tarUserInfo, otherAnswer, inflater);

                        if (!questionAnswerMeAndTargetUserBean.getSelfAnswerId().equals(questionAnswerMeAndTargetUserBean.getUserAnswerId())) {
                            TextView tv = myAnswerView.findViewById(R.id.answer_rlt_tv);
                            tv.setTextColor(context.getResources().getColor(R.color.blue));
                        }

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.topMargin = DensityUtil.dp2px(10);
                        otherAnswerView.setLayoutParams(params);

                        answerRltLayout.addView(myAnswerView);
                        answerRltLayout.addView(otherAnswerView);

                        modifyQaBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                QuestionData questionData = new QuestionData();
                                questionData.setQuestion(questionAnswerMeAndTargetUserBean.getQuestion());
                                questionData.setAnswerList(questionAnswerMeAndTargetUserBean.getAnswerList());
                                questionData.setUserAnswerId(questionAnswerMeAndTargetUserBean.getSelfAnswerId());
                                ModifyQaDialog qaDialog = new ModifyQaDialog(context, questionData, new ModifyQaDialog.ItemClickListener() {
                                    @Override
                                    public QuestionItem onItemClick(QuestionData questionData, QaMyAnsweredAdapter.QaViewHolder holder) {
                                        questionAnswerMeAndTargetUserBean.setSelfAnswerId(questionData.getUserAnswerId());
                                        TextView tv = myAnswerView.findViewById(R.id.answer_rlt_tv);
                                        String myAnswer = "";
                                        for (QuestionItem answer : questionAnswerMeAndTargetUserBean.getAnswerList()) {
                                            if (questionAnswerMeAndTargetUserBean.getSelfAnswerId().equals(answer.getAnswerId())) {
                                                myAnswer = answer.getContent();
                                            }
                                        }
                                        tv.setText(myAnswer);
                                        if (!questionAnswerMeAndTargetUserBean.getSelfAnswerId().equals(questionAnswerMeAndTargetUserBean.getUserAnswerId())) {
                                            tv.setTextColor(context.getResources().getColor(R.color.blue));
                                        } else {
                                            tv.setTextColor(context.getResources().getColor(R.color.text_blue_1));
                                        }

                                        listener.answerQa(questionData.getQuestion().getQuestionId(), questionData.getUserAnswerId());
                                        return null;
                                    }
                                });

                                qaDialog.show();
                            }
                        });
                        listener.answerQa(questionData.getQuestion().getQuestionId(), questionData.getUserAnswerId());
                        return null;
                    }
                });

                qaDialog.show();
            }
        });
        return convertView;
    }

    class QaNotAnswerViewHolder {
        TextView qaTitleTv;
        Button answerBtn;
    }
}
