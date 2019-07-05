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
import com.oneone.modules.timeline.adapter.holder.UnSendHolder4Moment;
import com.oneone.modules.timeline.bean.TimeLine;
import com.oneone.modules.timeline.bean.TimeLineDetail;
import com.oneone.modules.timeline.contract.TimeLineContract;

import java.util.List;

/**
 * @author qingfei.chen
 * @since 2018/5/31.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class TimeLineUnSendAdapter extends BaseRecyclerViewAdapter<TimeLine> {
    private LayoutInflater mInflater;

    TimeLineContract.Presenter presenter;

    public TimeLineUnSendAdapter(BaseViewHolder.ItemClickListener<TimeLine> listener, Context context, TimeLineContract.Presenter presenter) {
        super(listener);
        mInflater = LayoutInflater.from(context);
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public BaseViewHolder<TimeLine> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewGroup = mInflater.inflate(R.layout.item_timeline_unsend_moment, parent, false);
        return new UnSendHolder4Moment(viewGroup, mListener, presenter);
    }
}
