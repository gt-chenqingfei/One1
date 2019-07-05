package com.oneone.modules.timeline.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.oneone.HereSingletonFactory;
import com.oneone.R;
import com.oneone.event.EventTimelineDelete;
import com.oneone.framework.ui.BaseListActivity;
import com.oneone.framework.ui.BaseRecyclerViewAdapter;
import com.oneone.framework.ui.BaseViewHolder;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;
import com.oneone.framework.ui.dialog.WarnDialog;
import com.oneone.framework.ui.widget.SimpleRecyclerView;
import com.oneone.modules.timeline.adapter.TimeLineUnSendAdapter;
import com.oneone.modules.timeline.bean.TimeLine;
import com.oneone.modules.timeline.contract.TimeLineContract;
import com.oneone.modules.timeline.presenter.TimeLineBasePresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

/**
 * @author qingfei.chen
 * @since 2018/6/27.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@ToolbarResource(navigationIcon = R.drawable.ic_btn_back_dark, title = R.string.title_timeline_un_send, background = R.color.transparent)
@LayoutResource(R.layout.activity_timeline_unsend)
public class TimeLineUnSendActivity extends BaseListActivity<TimeLine, TimeLineBasePresenter,
        TimeLineContract.View> implements TimeLineContract.View, BaseViewHolder.ItemClickListener<TimeLine> {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, TimeLineUnSendActivity.class));
    }

    @BindView(R.id.activity_timeline_unsend_recycler_view)
    SimpleRecyclerView<TimeLine> simplePullRecyclerView;

    private TimeLineContract.INewTimeLineManager iNewTimeLineManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iNewTimeLineManager = HereSingletonFactory.getInstance().getNewTimeLineManager();
        setRightTextMenu(R.string.str_clear);
    }

    @NonNull
    @Override
    public BaseRecyclerViewAdapter<TimeLine> adapterToDisplay() {
        return new TimeLineUnSendAdapter(this, this, mPresenter);
    }

    @NonNull
    @Override
    public SimpleRecyclerView<TimeLine> getDisplayView() {
        return simplePullRecyclerView;
    }

    @Override
    public void onRightTextMenuClick(View view) {
        super.onRightTextMenuClick(view);
        new WarnDialog(this, R.string.str_dialog_clean_unsend_timeline,
                new WarnDialog.OnPositiveClickListener() {
                    @Override
                    public void onPositiveClick() {
                        iNewTimeLineManager.clear();
                        load();
                        EventBus.getDefault().post(new EventTimelineDelete(true));
                    }
                })
                .show();

    }

    @Override
    public void onItemClick(TimeLine o, int id, int position) {
        iNewTimeLineManager.reSendTimeLine(o);
        load();
    }

    @Override
    protected boolean autoLoad() {
        return true;
    }

    @Override
    public List<TimeLine> doLoad() {
        return iNewTimeLineManager.getTimeLineUnSendList();
    }
}
