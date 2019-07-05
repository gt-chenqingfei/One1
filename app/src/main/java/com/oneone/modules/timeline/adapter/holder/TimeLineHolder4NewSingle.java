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
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class TimeLineHolder4NewSingle extends Holder4NewSingle {
    @BindView(R.id.item_timeline_new_single_wrapper_header)
    View header;

    public TimeLineHolder4NewSingle(View v, ItemClickListener<TimeLine> listener, TimeLineContract.Presenter presenter) {
        super(v, listener, presenter);
    }

    @Override
    public void bind(TimeLine timeLine, int position) {
        super.bind(timeLine, position);
        if (position == 0) {
            header.setVisibility(View.VISIBLE);
        } else {
            header.setVisibility(View.GONE);
        }
    }
}
