package com.oneone.modules.timeline.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.oneone.BaseActivity;
import com.oneone.R;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;
import com.oneone.modules.timeline.contract.TimeLineContract;
import com.oneone.modules.timeline.presenter.TimelineReportPresenter;
import com.oneone.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

@ToolbarResource(navigationIcon = R.drawable.ic_btn_back_dark, background = R.color.white, title = R.string.im_bottom_report_text)
@LayoutResource(R.layout.activity_report)
public class TimeLineReportActivity extends BaseActivity<TimelineReportPresenter, TimeLineContract.View>
        implements TimeLineContract.View, TimeLineContract.OnReportListener {

    private static final String TIMELINE_ID = "timeline_id";
    private long timelineID;

    @BindView(R.id.rg_report)
    RadioGroup radioGroup;

    public static void startActivity(Context context, long timelineID) {
        Intent intent = new Intent(context, TimeLineReportActivity.class);
        intent.putExtra(TIMELINE_ID, timelineID);
        context.startActivity(intent);
    }

    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);
        timelineID = getIntent().getLongExtra(TIMELINE_ID, 0);
    }

    @Override
    public TimelineReportPresenter onCreatePresenter() {
        return new TimelineReportPresenter();
    }

    @OnClick(R.id.bt_report)
    public void sendReport(View view) {
        RadioButton radioButton = (RadioButton) findView(radioGroup.getCheckedRadioButtonId());
        if (radioButton == null) {
            ToastUtil.show(TimeLineReportActivity.this, getString(R.string.timeline_report_reason_title));
            return;
        }
        String tag = radioButton.getTag().toString();
        mPresenter.report(timelineID, Integer.parseInt(tag), this);
    }

    @Override
    public void onReport(boolean isOK) {
        if (isOK) {
            ToastUtil.show(TimeLineReportActivity.this, getString(R.string.timeline_reported));
            finish();
        }
    }
}
