package com.oneone.modules.timeline.adapter.holder;

import android.view.View;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.api.constants.ApiStatus;
import com.oneone.api.constants.TimelineStatus;
import com.oneone.framework.ui.BaseViewHolder;
import com.oneone.modules.timeline.activity.TimeLineUnSendActivity;
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
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class TimeLineHolder4Moment extends Holder4Moment {

    @BindView(R.id.item_timeline_moment_wrapper_header)
    View header;

    @BindView(R.id.item_timeline_post_error_tip)
    TextView timelinePostErrorTip;

    public TimeLineHolder4Moment(View v) {
        super(v);
    }

    public TimeLineHolder4Moment(View v, ItemClickListener<TimeLine> listener, TimeLineContract.Presenter presenter) {
        super(v, listener, presenter);
    }

    @Override
    public void bind(TimeLine timeLine, int position) {
        super.bind(timeLine, position);
        if (position == 0) {
            header.setVisibility(View.VISIBLE);
            if (timeLine.getStatus() == TimelineStatus.STATUS_SEND_FAILED) {
                timelinePostErrorTip.setVisibility(View.VISIBLE);
                timelinePostErrorTip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimeLineUnSendActivity.startActivity(v.getContext());
                    }
                });
            } else {
                timelinePostErrorTip.setVisibility(View.GONE);
            }
        } else {
            header.setVisibility(View.GONE);
            timelinePostErrorTip.setVisibility(View.GONE);
        }
    }
}
