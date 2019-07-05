package com.oneone.modules.timeline.adapter.holder;

import android.view.View;

import com.oneone.R;
import com.oneone.framework.ui.BaseViewHolder;
import com.oneone.modules.timeline.bean.TimeLine;
import com.oneone.modules.timeline.contract.TimeLineContract;
import com.oneone.modules.timeline.view.TimeLineBottomLikeView;
import com.oneone.modules.timeline.view.TimeLineNewSingleView;
import com.oneone.modules.timeline.view.TimeLineTextContentView;
import com.oneone.modules.timeline.view.TimeLineTopSummaryView;

import butterknife.BindView;

/**
 * @author qingfei.chen
 * @since 2018/5/31.
 * @version V1.0.0
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class Holder4NewSingle extends BaseViewHolder<TimeLine> {
    @BindView(R.id.item_timeline_moment_summary)
    TimeLineTopSummaryView mTimeLineTopSummaryView;

    @BindView(R.id.item_timeline_moment_new_single)
    TimeLineNewSingleView mTimeLineNewSingleView;

    @BindView(R.id.item_timeline_moment_like_view)
    TimeLineBottomLikeView mTimeLineBottomLikeView;

    TimeLineContract.Presenter presenter;

    public Holder4NewSingle(View v, ItemClickListener<TimeLine> listener,
                            TimeLineContract.Presenter presenter) {
        super(v, listener);
        this.presenter = presenter;
    }

    @Override
    public void bind(TimeLine timeLine, int position) {
        super.bind(timeLine, position);
        if (timeLine == null) {
            return;
        }
        mTimeLineTopSummaryView.bind(timeLine, presenter, position);
        mTimeLineNewSingleView.bind(timeLine.getDetail().getDetail().getUserInfo(), presenter, position);
        mTimeLineBottomLikeView.bind(timeLine, presenter, position);
    }
}
