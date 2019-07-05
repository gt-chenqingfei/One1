package com.oneone.modules.qa.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.oneone.R;
import com.oneone.framework.ui.BasePresenterFragment;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.utils.ScreenUtil;
import com.oneone.modules.qa.adapter.QaQuestionAdapter;
import com.oneone.modules.qa.beans.MatchForClassify;
import com.oneone.modules.qa.beans.QuestionAnswerMeAndTargetUserBean;
import com.oneone.modules.qa.beans.QuestionClassify;
import com.oneone.modules.qa.beans.QuestionData;
import com.oneone.modules.qa.beans.QuestionItem;
import com.oneone.modules.qa.contract.QaContract;
import com.oneone.modules.qa.presenter.QaPresenter;
import com.oneone.modules.qa.util.ScalePageTransformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by here on 18/4/16.
 */

@LayoutResource(R.layout.frag_qa_not_answer)
public class NotAnswerFragment extends BasePresenterFragment<QaPresenter, QaContract.View> implements QaContract.View, QaQuestionAdapter.QaNotAnswerAdapterListener {
    @BindView(R.id.root_layout)
    RelativeLayout rootLayout;
    @BindView(R.id.viewpager)
    ViewPager pager;
    @BindView(R.id.qa_answer_complete_layout)
    RelativeLayout answerCompleteLayout;

    private QaQuestionAdapter adapter;
    private ScalePageTransformer transformer;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) pager.getLayoutParams();
        params.height = (int) (463 * ScreenUtil.WIDTH_RATIO);

        transformer = new ScalePageTransformer();

        pager.setOffscreenPageLimit(3);
        pager.setPageTransformer(false, transformer);
        rootLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return pager.dispatchTouchEvent(event);
            }
        });
        mPresenter.qaUnAnswerAll();

        params = (RelativeLayout.LayoutParams) answerCompleteLayout.getLayoutParams();
        params.width = (int) (ScreenUtil.WIDTH_RATIO * 280);
        params.height = (int) (ScreenUtil.WIDTH_RATIO * 463);

        answerCompleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

    @Override
    public void qaUnAnswerClassify(List<QuestionData> list) {

    }

    @Override
    public void qaUnAnswerAll(int count, List<QuestionData> list) {
        if (count > 0) {
            adapter = new QaQuestionAdapter(getContext(), list, this);
            pager.setAdapter(adapter);
        } else {
            answerCompleteLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void qaAnswered(boolean isLoadMore, int count, List<QuestionData> list) {

    }

    @Override
    public void qaAnswer(String questionId) {
        pager.setCurrentItem(pager.getCurrentItem() + 1);
    }

    @Override
    public void qaMatchValue(List<MatchForClassify> matchForClassify, int matchForAll) {

    }

    @Override
    public void qaAnswersFortarget(int count, ArrayList<QuestionAnswerMeAndTargetUserBean> list) {

    }

    @Override
    public QaPresenter onPresenterCreate() {
        return new QaPresenter();
    }

    @Override
    public void OnQuestionSelected(QuestionItem questionItem) {
        mPresenter.qaAnswer(questionItem.getQuestionId(), questionItem.getAnswerId());
    }
}
