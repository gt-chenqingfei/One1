package com.oneone.modules.qa.fragment;

import android.os.Bundle;
import android.os.UserManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.oneone.HereSingletonFactory;
import com.oneone.R;
import com.oneone.framework.ui.AbstractPullListFragment;
import com.oneone.framework.ui.BaseRecyclerViewAdapter;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.widget.SimplePullRecyclerView;
import com.oneone.modules.qa.adapter.QaMyAnsweredAdapter;
import com.oneone.modules.qa.beans.MatchForClassify;
import com.oneone.modules.qa.beans.QuestionAnswerMeAndTargetUserBean;
import com.oneone.modules.qa.beans.QuestionClassify;
import com.oneone.modules.qa.beans.QuestionData;
import com.oneone.modules.qa.contract.QaContract;
import com.oneone.modules.qa.presenter.QaPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by here on 18/4/20.
 */

@LayoutResource(R.layout.frag_question_answer)
public class AlreadyAnswerQuestionListFragment extends AbstractPullListFragment<QuestionData, QaPresenter, QaContract.View> implements QaContract.View {
    public boolean isFirstTime;

    public static final int PAGE_COUNT = 20;

    @BindView(R.id.frag_question_answer_recycler)
    SimplePullRecyclerView<QuestionData> mSimpleRecyclerView;
    private String classifyId;

    public void setFragmentContent(String classifyId, boolean isFirstTime) {
        this.classifyId = classifyId;
        this.isFirstTime = isFirstTime;
    }

    public void setFirstTime (boolean isFirstTime) {
        this.isFirstTime = isFirstTime;
    }

    public boolean getIsFirstTime () {
        return isFirstTime;
    }

    @Override
    public QaPresenter onPresenterCreate() {
        return new QaPresenter();
    }

    @NonNull
    @Override
    public BaseRecyclerViewAdapter<QuestionData> adapterToDisplay() {
        return new QaMyAnsweredAdapter(new QaMyAnsweredAdapter.QaMyAnsweredAdapterListener() {
            @Override
            public void onItemClick(Object o, int id, int position) {

            }

            @Override
            public void modifyQuestion(QuestionData questionData, QaMyAnsweredAdapter.QaViewHolder holder) {
                super.modifyQuestion(questionData, holder);

                mPresenter.qaAnswer(questionData.getQuestion().getQuestionId(), questionData.getUserAnswerId());
            }
        });
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadRefresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @NonNull
    @Override
    public SimplePullRecyclerView<QuestionData> getDisplayView() {
        return mSimpleRecyclerView;
    }


    @Override
    public void loadMore(int pageIndex) {
        super.loadMore(pageIndex);

        if (isFirstTime) {
            mPresenter.qaAnswered(true, pageIndex * PAGE_COUNT, PAGE_COUNT, "");
        } else {
            mPresenter.qaAnswered(true, pageIndex * PAGE_COUNT, PAGE_COUNT, classifyId);
        }
    }

    @Override
    public void loadRefresh() {
        super.loadRefresh();

        if (isFirstTime) {
            mPresenter.qaAnswered(false, 0, PAGE_COUNT, "");
        } else {
            mPresenter.qaAnswered(false, 0, PAGE_COUNT, classifyId);
        }
    }

    @Override
    public void qaCountForall(int count, int answered) {

    }

    @Override
    public void qaCountInfo(List<QuestionClassify> questionClassifies) {
//        onloa
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
        if (list == null) {
            onLoadFailed("");
        } else {
            if (isLoadMore) {
                onLoadMore(list);
            } else {
                onLoadCompleted(list);
            }
        }
    }

    @Override
    public void qaAnswer(String questionId) {

    }

    @Override
    public void qaMatchValue(List<MatchForClassify> matchForClassify, int matchForAll) {

    }

    @Override
    public void qaAnswersFortarget(int count, ArrayList<QuestionAnswerMeAndTargetUserBean> list) {

    }


}
