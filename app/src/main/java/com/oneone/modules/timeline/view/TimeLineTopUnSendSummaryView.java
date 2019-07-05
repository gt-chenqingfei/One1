package com.oneone.modules.timeline.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.framework.android.utils.TimeUtils;
import com.oneone.framework.ui.BaseViewHolder;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.dialog.SheetDialog;
import com.oneone.framework.ui.dialog.SheetItem;
import com.oneone.modules.timeline.activity.TimeLineReportActivity;
import com.oneone.modules.timeline.bean.TimeLine;
import com.oneone.modules.timeline.contract.TimeLineContract;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.bean.UserInfoBase;
import com.oneone.widget.AvatarImageView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author qingfei.chen
 * @since 2018/5/30.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@LayoutResource(R.layout.view_timeline_un_send_summary)
public class TimeLineTopUnSendSummaryView extends AbstractTimeLineView<TimeLine> {
    @BindView(R.id.view_timeline_summary_iv_avatar)
    AvatarImageView ivAvatar;

    @BindView(R.id.view_timeline_summary_tv_display)
    TextView tvDisplay;

    @BindView(R.id.view_timeline_summary_tv_time)
    TextView tvTime;

    @BindView(R.id.view_timeline_summary_tv_re_send)
    TextView tvRenSend;

    TimeLine timeLine;
    private int position;

    private BaseViewHolder.ItemClickListener<TimeLine> listener;

    public TimeLineTopUnSendSummaryView(Context context) {
        super(context);
    }

    public TimeLineTopUnSendSummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onViewCreated() {

    }

    @Override
    public void bind(TimeLine o, TimeLineContract.Presenter presenter, int position) {
        this.timeLine = o;
        this.position = position;
        UserInfoBase userInfo = o.getUserInfo();
        if (userInfo != null) {
            ivAvatar.init(userInfo, true);
            tvDisplay.setText(userInfo.getMyNickname());
            tvTime.setText(TimeUtils.getFriendlyTimeSpanByNow(o.getPostTime()));
        }
    }


    @OnClick(R.id.view_timeline_summary_tv_re_send)
    public void onReSendClick(View view) {
        if (listener != null) {
            listener.onItemClick(timeLine, view.getId(), this.position);
        }
    }

    public void setOnItemClickListener(BaseViewHolder.ItemClickListener<TimeLine> listener) {
        this.listener = listener;
    }
}
