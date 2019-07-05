package com.oneone.modules.timeline.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oneone.R;
import com.oneone.framework.ui.BaseRecyclerViewAdapter;
import com.oneone.framework.ui.BaseViewHolder;
import com.oneone.modules.timeline.adapter.holder.TimeLineHolder4Moment;
import com.oneone.modules.timeline.adapter.holder.TimeLineHolder4NewSingle;
import com.oneone.modules.timeline.bean.TimeLine;
import com.oneone.modules.timeline.bean.TimeLineDetail;
import com.oneone.modules.timeline.contract.TimeLineContract;

import java.util.List;

/**
 * @author qingfei.chen
 * @since 2018/5/31.
 * @version V1.0.0
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class TimeLineAdapter extends BaseRecyclerViewAdapter<TimeLine> {
    private static final int TYPE_NEW_MOMENT = 1;
    private static final int TYPE_NEW_SINGLE = 2;
    private LayoutInflater mInflater;

    TimeLineContract.Presenter presenter;

    public TimeLineAdapter(BaseViewHolder.ItemClickListener<TimeLine> listener, Context context, TimeLineContract.Presenter presenter) {
        super(listener);
        mInflater = LayoutInflater.from(context);
        this.presenter = presenter;
    }

    @Override
    public int getItemViewType(int position) {
        TimeLine timeLine = getItem(position);
        if (timeLine.getDetail() != null) {
            if (TimeLineDetail.TYPE_MOMENT.equals(timeLine.getDetail().getType())) {
                return TYPE_NEW_MOMENT;
            } else if (TimeLineDetail.TYPE_NEW_SINGLE.equals(timeLine.getDetail().getType())) {
                return TYPE_NEW_SINGLE;
            }
        }

        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public BaseViewHolder<TimeLine> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewGroup;
        switch (viewType) {
            case TYPE_NEW_MOMENT:
                viewGroup = mInflater.inflate(R.layout.item_timeline_moment_wrapper, parent, false);
                return new TimeLineHolder4Moment(viewGroup, mListener, presenter);

            case TYPE_NEW_SINGLE:
                viewGroup = mInflater.inflate(R.layout.item_timeline_new_single_wrapper, parent, false);
                return new TimeLineHolder4NewSingle(viewGroup, mListener, presenter);
        }
        return null;
    }

    public TimeLine findItemById(long timelineId) {
        List<TimeLine> timeLines = getList();
        if (timeLines == null || timeLines.isEmpty()) {
            return null;
        }
        for (TimeLine timeLine : timeLines) {
            if (timeLine.getTimelineId() == timelineId) {
                return timeLine;
            }
        }
        return null;
    }
}
