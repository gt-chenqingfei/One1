package com.oneone.modules.timeline.adapter.holder;

import android.view.View;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.api.constants.TimelineStatus;
import com.oneone.framework.ui.BaseViewHolder;
import com.oneone.modules.timeline.bean.TimeLine;
import com.oneone.modules.timeline.contract.TimeLineContract;
import com.oneone.modules.timeline.view.TimeLineBottomLikeView;
import com.oneone.modules.timeline.view.TimeLineImageView;
import com.oneone.modules.timeline.view.TimeLineTextContentView;
import com.oneone.modules.timeline.view.TimeLineTopSummaryView;

import butterknife.BindView;

/**
 * @author qingfei.chen
 * @since 2018/5/31.
 * @version V1.0.0
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class Holder4Moment extends BaseViewHolder<TimeLine> {

    @BindView(R.id.item_timeline_moment_summary)
    TimeLineTopSummaryView mTimeLineTopSummaryView;

    @BindView(R.id.item_timeline_moment_content_text)
    TimeLineTextContentView mTimeLineTextContentView;

    @BindView(R.id.item_timeline_moment_nine_image_view)
    TimeLineImageView mTimeLineImageView;

    @BindView(R.id.item_timeline_moment_like_view)
    TimeLineBottomLikeView mTimeLineBottomLikeView;

    @BindView(R.id.view_timeline_bottom_tv_post_status)
    TextView mTvPostStatus;
    TimeLineContract.Presenter presenter;

    public Holder4Moment(View v) {
        super(v);
    }

    public Holder4Moment(View v, ItemClickListener<TimeLine> listener, TimeLineContract.Presenter presenter) {
        super(v, listener);
        this.presenter = presenter;
    }

    @Override
    public void bind(TimeLine timeLine, int position) {
        super.bind(timeLine, position);
        if (timeLine == null) {
            return;
        }
        mTimeLineTopSummaryView.bind(timeLine, presenter,position);
        mTimeLineTextContentView.bind(timeLine);
        mTimeLineImageView.setTag(position);
        mTimeLineImageView.bind(timeLine.getDetail().getDetail().getTimelineImgs(), presenter,position);
        if(timeLine.getStatus() == TimelineStatus.STATUS_NOT_APPROVED){
            mTvPostStatus.setVisibility(View.VISIBLE);
            mTimeLineBottomLikeView.setVisibility(View.GONE);
        }
        else {
            mTimeLineBottomLikeView.setVisibility(View.VISIBLE);
            mTvPostStatus.setVisibility(View.GONE);
            mTimeLineBottomLikeView.bind(timeLine, presenter, position);
        }

    }
}
