package com.oneone.modules.qa.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.oneone.BaseActivity;
import com.oneone.R;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;
import com.oneone.modules.qa.adapter.MyQaPagerFragmentAdapter;
import com.oneone.modules.qa.beans.MatchForClassify;
import com.oneone.modules.qa.beans.QuestionAnswerMeAndTargetUserBean;
import com.oneone.modules.qa.beans.QuestionClassify;
import com.oneone.modules.qa.beans.QuestionData;
import com.oneone.modules.qa.contract.QaContract;
import com.oneone.modules.qa.fragment.AlreadyAnswerFragment;
import com.oneone.modules.qa.fragment.NotAnswerFragment;
import com.oneone.modules.qa.presenter.QaPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by here on 18/4/16.
 */

@ToolbarResource(title = R.string.str_my_qa_page_title_text, background = R.color.blue, navigationIcon = R.drawable.white_left_arrow)
@LayoutResource(R.layout.activity_my_qa)
public class MyQaActivity extends BaseActivity<QaPresenter, QaContract.View> implements QaContract.View {
    @BindView(R.id.qa_view_pager)
    ViewPager qaViewPager;

    @BindView(R.id.left_navigator_tv)
    TextView leftNavigatorTv;
    @BindView(R.id.right_navigator_tv)
    TextView rightNavigatorTv;
    @BindView(R.id.left_navigator)
    View leftNavigator;
    @BindView(R.id.right_navigator)
    View rightNavigator;

    @Override
    public QaPresenter onCreatePresenter() {
        return new QaPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        int navigatorIndex = 0;
        if (bundle != null) {
            navigatorIndex = bundle.getInt("navigatorIndex");
        }

        getTitleView().setTextColor(Color.WHITE);

        initView(navigatorIndex);
    }

    public void initView (int navigatorIndex) {
        ArrayList list = new ArrayList<>();
        list.add(new NotAnswerFragment());
        AlreadyAnswerFragment frag = new AlreadyAnswerFragment();
        FragmentManager manager = getSupportFragmentManager();
        frag.setFragmentManager(manager);
        list.add(frag);
        MyQaPagerFragmentAdapter adapter = new MyQaPagerFragmentAdapter(getSupportFragmentManager(), list);
        qaViewPager.setAdapter(adapter);

        qaViewPager.setCurrentItem(navigatorIndex);
    }

    @Override
    public void onInitListener() {
        super.onInitListener();

        leftNavigatorTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigatorSelected(0);

                qaViewPager.setCurrentItem(0);
            }
        });

        rightNavigatorTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigatorSelected(1);

                qaViewPager.setCurrentItem(1);
            }
        });

        qaViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                navigatorSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void navigatorSelected (int pos) {
        if (pos == 0) {
            leftNavigatorTv.setTextColor(getResources().getColor(R.color.white));
            leftNavigator.setBackgroundColor(getResources().getColor(R.color.text_pink_1));
            rightNavigatorTv.setTextColor(getResources().getColor(R.color.text_gray_1));
            rightNavigator.setBackgroundColor(Color.TRANSPARENT);
        } else {
            mPresenter.qaCountInfo();
            rightNavigatorTv.setTextColor(getResources().getColor(R.color.white));
            rightNavigator.setBackgroundColor(getResources().getColor(R.color.text_pink_1));
            leftNavigatorTv.setTextColor(getResources().getColor(R.color.text_gray_1));
            leftNavigator.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    public static void startActivity (Context context, int navigatorIndex) {
        Intent it = new Intent(context, MyQaActivity.class);
        it.putExtra("navigatorIndex", navigatorIndex);
        context.startActivity(it);
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
    protected void onDestroy() {
        mPresenter.qaAnswered();
        super.onDestroy();
    }
}
