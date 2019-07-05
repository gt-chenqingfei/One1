package com.oneone.modules.timeline.adapter.holder;

import android.view.View;

import com.oneone.R;
import com.oneone.framework.ui.BaseViewHolder;
import com.oneone.modules.timeline.bean.TimeLine;
import com.oneone.modules.timeline.contract.TimeLineContract;
import com.oneone.modules.timeline.view.TimeLineBottomLikeView;
import com.oneone.modules.timeline.view.TimeLineImageView;
import com.oneone.modules.timeline.view.TimeLineTextContentView;
import com.oneone.modules.timeline.view.TimeLineTopSummaryView;
import com.oneone.modules.timeline.view.TimeLineTopUnSendSummaryView;

import butterknife.BindView;

/**
 * @author qingfei.chen
 * @since 2018/5/31.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class UnSendHolder4Moment extends BaseViewHolder<TimeLine> {

    @BindView(R.id.item_timeline_moment_summary)
    TimeLineTopUnSendSummaryView mTimeLineTopSummaryView;

    @BindView(R.id.item_timeline_moment_content_text)
    TimeLineTextContentView mTimeLineTextContentView;

    @BindView(R.id.item_timeline_moment_nine_image_view)
    TimeLineImageView mTimeLineImageView;
    TimeLineContract.Presenter presenter;

    public UnSendHolder4Moment(View v) {
        super(v);
    }

    public UnSendHolder4Moment(View v, ItemClickListener<TimeLine> listener, TimeLineContract.Presenter presenter) {
        super(v, listener);
        this.presenter = presenter;
        mTimeLineTopSummaryView.setOnItemClickListener(listener);
    }

    @Override
    public void bind(TimeLine timeLine, int position) {
        super.bind(timeLine, position);
        if (timeLine == null) {
            return;
        }
        mTimeLineTopSummaryView.bind(timeLine, presenter, position);
        mTimeLineTextContentView.bind(timeLine);
        mTimeLineImageView.setTag(position);
        mTimeLineImageView.bind(timeLine.getDetail().getDetail().getTimelineImgs(), presenter, position);
    }
}
