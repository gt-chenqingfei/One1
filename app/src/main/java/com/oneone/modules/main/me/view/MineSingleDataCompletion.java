package com.oneone.modules.main.me.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.framework.ui.BaseView;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.widget.MyHorizontalProgressBar;

import butterknife.BindView;

/**
 * Created by here on 18/4/11.
 */

@LayoutResource(R.layout.view_mine_single_data_completion_view)
public class MineSingleDataCompletion extends BaseView {
    @BindView(R.id.progress_view)
    MyHorizontalProgressBar progressView;
    @BindView(R.id.progress_tv)
    TextView progressTv;

    public MineSingleDataCompletion(Context context) {
        super(context);
    }

    public MineSingleDataCompletion(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onViewCreated() {
        progressView.setProgressTv(progressTv, "%");
    }

    public void setProgress(int value) {
        progressView.setProgress(value);
    }
}
