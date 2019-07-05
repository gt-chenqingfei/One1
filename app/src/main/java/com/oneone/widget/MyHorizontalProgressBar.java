package com.oneone.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

import com.oneone.framework.ui.widget.ZzHorizontalProgressBar;

/**
 * Created by here on 18/4/12.
 */

public class MyHorizontalProgressBar extends ZzHorizontalProgressBar {
    private TextView progressTv;
    private String progressUnitStr = "";

    public void setProgressTv(TextView progressTv) {
        this.progressTv = progressTv;
    }

    public void setProgressTv(TextView progressTv, String progressUnitStr) {
        this.progressTv = progressTv;
        if (progressUnitStr != null)
            this.progressUnitStr = progressUnitStr;
    }

    @Override
    public void setProgress(int progress) {
        super.setProgress(progress);

        if (progressTv != null)
            progressTv.setText(progress + (progressUnitStr != null ? progressUnitStr : ""));
    }

    public MyHorizontalProgressBar(Context context) {
        super(context);
    }

    public MyHorizontalProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyHorizontalProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
