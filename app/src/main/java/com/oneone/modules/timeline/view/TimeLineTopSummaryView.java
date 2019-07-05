package com.oneone.modules.timeline.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneone.Constants;
import com.oneone.R;
import com.oneone.api.constants.Role;
import com.oneone.framework.android.utils.TimeUtils;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.dialog.SheetDialog;
import com.oneone.framework.ui.dialog.SheetItem;
import com.oneone.framework.ui.utils.ScreenUtil;
import com.oneone.modules.timeline.activity.TimeLineReportActivity;
import com.oneone.modules.timeline.bean.TimeLine;
import com.oneone.modules.timeline.contract.TimeLineContract;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.bean.UserInfoBase;
import com.oneone.utils.ToastUtil;
import com.oneone.widget.AvatarImageView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author qingfei.chen
 * @since 2018/5/30.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@LayoutResource(R.layout.view_timeline_summary)
public class TimeLineTopSummaryView extends AbstractTimeLineView<TimeLine> implements SheetDialog.OnSheetItemClickListener {
    public static final int ID_ITEM_REPORT = 0;
    public static final int ID_ITEM_DELETE = 1;
    @BindView(R.id.view_timeline_summary_iv_avatar)
    AvatarImageView ivAvatar;

    @BindView(R.id.view_timeline_summary_tv_display)
    TextView tvDisplay;

    @BindView(R.id.view_timeline_summary_tv_time)
    TextView tvTime;

    @BindView(R.id.view_timeline_summary_iv_menu_more)
    ImageView ivMenuMore;

    TimeLine timeLine;
    TimeLineContract.Presenter presenter;
    private int position;


    public TimeLineTopSummaryView(Context context) {
        super(context);
    }

    public TimeLineTopSummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onViewCreated() {

    }

    @Override
    public void bind(TimeLine o, TimeLineContract.Presenter presenter, int position) {
        this.presenter = presenter;
        this.timeLine = o;
        this.position = position;
        UserInfoBase userInfo = o.getUserInfo();
        if (userInfo != null) {
            ivAvatar.init(userInfo, true);
            if (userInfo.getUserId().equals(Constants.ID.ONE_ONE)) {
                tvDisplay.setText(R.string.str_display_name_oneone);
                tvDisplay.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_vip, 0);
                tvDisplay.setCompoundDrawablePadding(ScreenUtil.dip2px(5));
            }
            else if(userInfo.getRole() == Role.MATCHER){
                tvDisplay.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_mark_matcher, 0);
                tvDisplay.setCompoundDrawablePadding(ScreenUtil.dip2px(5));
                tvDisplay.setText(userInfo.getMyNickname());
            }
            else {
                tvDisplay.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
                tvDisplay.setText(userInfo.getMyNickname());
            }


            tvTime.setText(TimeUtils.getFriendlyTimeSpanByNow(o.getPostTime()));
        }
    }


    @OnClick(R.id.view_timeline_summary_iv_menu_more)
    public void onMenuMoreClick(View view) {
        int menuArray = R.array.sheet_timeline_menu_4_other;
        int id = ID_ITEM_REPORT;
        if (TextUtils.equals(timeLine.getUserInfo().getUserId(),
                HereUser.getInstance().getUserId())) {
            menuArray = R.array.sheet_timeline_menu_4_self;
            id = ID_ITEM_DELETE;
        }
        SheetDialog menuMoreDialog = new SheetDialog(getContext(), menuArray, id, this);
        menuMoreDialog.show();
    }

    @Override
    public void onItemClick(SheetItem item, int id) {
        if (id == ID_ITEM_REPORT) {
            TimeLineReportActivity.startActivity(getContext(), timeLine.getTimelineId());
        } else if (id == ID_ITEM_DELETE) {
            this.presenter.delete(timeLine, position);

        }
    }
}
