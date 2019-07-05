package com.oneone.modules.main.me.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.framework.ui.BaseView;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.utils.ScreenUtil;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import butterknife.BindView;

/**
 * Created by here on 18/4/12.
 */

@LayoutResource(R.layout.view_mine_single_story_and_matcher_view)
public class MineSingleStoryAndMatcher extends BaseView implements View.OnClickListener {
    @BindView(R.id.left_layout)
    RelativeLayout leftLayout;
    @BindView(R.id.my_story_like_count_left_tv)
    TextView myStoryLikeCountLeftTv;

    @BindView(R.id.right_layout)
    RelativeLayout rightLayout;
    @BindView(R.id.my_matcher_count_tv_1)
    TextView myMatcherCountTv1;
    @BindView(R.id.my_matcher_count_tv_dot)
    TextView myMatcherCountTvDot;

    public interface StoryAndMatcherListener {
        void onStoryClick();

        void onMatcherClick();
    }

    public MineSingleStoryAndMatcher(Context context) {
        super(context);
    }

    public MineSingleStoryAndMatcher(Context context, AttributeSet attrs) {

        super(context, attrs);
    }

    public void setListener(StoryAndMatcherListener listener) {
        this.listener = listener;
    }

    private StoryAndMatcherListener listener;

    @Override
    public void onViewCreated() {
        rightLayout.setOnClickListener(this);
        leftLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (listener == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.left_layout:
                listener.onStoryClick();
                break;

            case R.id.right_layout:
                listener.onMatcherClick();
                break;
        }
    }

    public void setUnreadCount(int dot) {
        if (dot > 0) {
            myMatcherCountTvDot.setVisibility(View.VISIBLE);
            myMatcherCountTvDot.setText("+" + dot);
        } else {
            myMatcherCountTvDot.setVisibility(View.GONE);
        }
    }

    public void setMatcherCount(int count) {
        myMatcherCountTv1.setText(count+"");
    }


}
