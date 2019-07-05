package com.oneone.modules.qa.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.event.EventRefreshQaClassify;
import com.oneone.framework.ui.BasePresenterFragment;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.modules.qa.QaDataManager;
import com.oneone.modules.qa.adapter.MyQaClassifyPagerFragmentAdapter;
import com.oneone.modules.qa.adapter.QaQuestionAdapter;
import com.oneone.modules.qa.beans.MatchForClassify;
import com.oneone.modules.qa.beans.QuestionAnswerMeAndTargetUserBean;
import com.oneone.modules.qa.beans.QuestionClassify;
import com.oneone.modules.qa.beans.QuestionData;
import com.oneone.modules.qa.beans.QuestionItem;
import com.oneone.modules.qa.contract.QaContract;
import com.oneone.modules.qa.presenter.QaPresenter;
import com.oneone.utils.ImageHelper;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by here on 18/4/16.
 */

@LayoutResource(R.layout.frag_qa_already_answer)
public class AlreadyAnswerFragment extends BasePresenterFragment<QaPresenter, QaContract.View> implements QaContract.View, QaQuestionAdapter.QaNotAnswerAdapterListener {
    @BindView(R.id.root_layout)
    LinearLayout rootLayout;
    @BindView(R.id.block_outer_layout)
    LinearLayout blockOuterLayout;
    @BindView(R.id.question_classify_pager)
    ViewPager viewPager;

    ArrayList<AlreadyAnswerQuestionListFragment> classifyFragmentList;
    private ArrayList<View> blockList = new ArrayList<View>();

    FragmentManager fm;

    public void setFragmentManager(FragmentManager fm) {
        this.fm = fm;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);

        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    class BlockHolder {
        int pos;
        View blockView;
        ImageView iconIv;
        TextView titleTv;
        TextView answerCountTv;
        QuestionClassify classify;

        ListView blockLv;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventRefreshQaClassify event) {
        if (QaDataManager.getInstance(getActivityContext()).getQuestionClassifies() != null && QaDataManager.getInstance(getActivityContext()).getQuestionClassifies().size() > 0) {

            LayoutInflater inflater = LayoutInflater.from(getContext());
            for (int i = 0; i < QaDataManager.getInstance(getActivityContext()).getQuestionClassifies().size(); i++) {
                QuestionClassify classify = QaDataManager.getInstance(getActivityContext()).getQuestionClassifies().get(i);
                View block = blockList.get(i);
                BlockHolder holder = (BlockHolder) block.getTag();
                holder.answerCountTv.setText("(" + classify.getAnsweredCount() + "/" + classify.getQuestionCount() + ")");
            }
            int pos = viewPager.getCurrentItem();
            classifyFragmentList.get(pos).loadRefresh();
        }
    }

    private void initView() {
        if (QaDataManager.getInstance(getActivityContext()).getQuestionClassifies() != null && QaDataManager.getInstance(getActivityContext()).getQuestionClassifies().size() > 0) {
            classifyFragmentList = new ArrayList<AlreadyAnswerQuestionListFragment>();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            for (int i = 0; i < QaDataManager.getInstance(getActivityContext()).getQuestionClassifies().size(); i++) {
                QuestionClassify classify = QaDataManager.getInstance(getActivityContext()).getQuestionClassifies().get(i);
                View block = inflater.inflate(R.layout.item_qa_classify_block, null);
                BlockHolder holder = new BlockHolder();
                holder.classify = classify;
                holder.pos = i;
                holder.blockView = block;
                holder.iconIv = block.findViewById(R.id.block_bg_iv);
                holder.titleTv = block.findViewById(R.id.block_title_tv);
                holder.answerCountTv = block.findViewById(R.id.block_answer_count_tv);
                block.setTag(holder);
                ImageHelper.displayImage(getContext(), holder.iconIv, classify.getClassifyIcon());
                holder.titleTv.setText(classify.getClassifyName());
                holder.answerCountTv.setText("(" + classify.getAnsweredCount() + "/" + classify.getQuestionCount() + ")");

                block.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        BlockHolder holder = (BlockHolder) view.getTag();
                        blockSelected(holder.pos);
                        viewPager.setCurrentItem(holder.pos);
                        for (AlreadyAnswerQuestionListFragment frag : classifyFragmentList) {
                            if (frag.getIsFirstTime()) {
                                frag.setFirstTime(false);
                                classifyFragmentList.get(holder.pos).loadRefresh();
                            }
                        }
                    }
                });
                blockList.add(block);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dp2px(100), DensityUtil.dp2px(100));
                params.leftMargin = DensityUtil.dp2px(8);
                blockOuterLayout.addView(block, params);

                AlreadyAnswerQuestionListFragment fragment = new AlreadyAnswerQuestionListFragment();
                boolean firstTime = false;
                if (i == 0)
                    firstTime = true;
                fragment.setFragmentContent(holder.classify.getClassifyId(), firstTime);
                classifyFragmentList.add(fragment);
            }

            MyQaClassifyPagerFragmentAdapter qaClassifyPagerFragmentAdapter = new MyQaClassifyPagerFragmentAdapter(fm, classifyFragmentList);
            viewPager.setAdapter(qaClassifyPagerFragmentAdapter);
            blockSelected(99);
            viewPager.setCurrentItem(0);
        }

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                for (AlreadyAnswerQuestionListFragment frag : classifyFragmentList) {
//                    if (frag.getIsFirstTime()) {
//                        frag.setFirstTime(false);
//                        classifyFragmentList.get(position).loadRefresh();
//                    }
//                }
                classifyFragmentList.get(position).setFirstTime(false);
                classifyFragmentList.get(position).loadRefresh();
                blockSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void blockSelected(int pos) {
        int i = 0;
        for (View blockView : blockList) {
            BlockHolder otherHolder = (BlockHolder) blockView.getTag();
            if (pos == i) {
                otherHolder.blockView.setBackgroundResource(R.drawable.shap_stoke_pink_bg);
            } else {
                otherHolder.blockView.setBackgroundResource(R.drawable.shape_white_radius_3dp);
            }
            i++;
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

    }

    @Override
    public void qaMatchValue(List<MatchForClassify> matchForClassify, int matchForAll) {

    }

    @Override
    public void qaAnswersFortarget(int count, ArrayList<QuestionAnswerMeAndTargetUserBean> list) {

    }

    @Override
    public void OnQuestionSelected(QuestionItem questionItem) {

    }

    @Override
    public QaPresenter onPresenterCreate() {
        return null;
    }

}