package com.oneone.framework.ui.imagepicker.preview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import java.util.Locale;

import com.oneone.framework.ui.R;
import com.oneone.framework.ui.imagepicker.ProgressPieView;
import com.oneone.framework.ui.imagepicker.view.CustomPhotoView;
import com.oneone.framework.ui.utils.ScreenUtil;

/**
 * @author qingfei.chen
 * @since 17/12/20.
 * Copyright © 2017 HereTech Technology Co.,Ltd. All rights reserved.
 */

public class ProgressImageView extends RelativeLayout {

    private CustomPhotoView photoView;
    ProgressPieView progressPieView;
    private int mProgress;

    public ProgressImageView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        photoView = new CustomPhotoView(context);
        LayoutParams ivLp = new LayoutParams(ScreenUtil.screenWidth,
                ScreenUtil.screenHeight);
        ivLp.addRule(CENTER_IN_PARENT);
        photoView.setLayoutParams(ivLp);
        addView(photoView);


        progressPieView = new ProgressPieView(context);
        LayoutParams tvLp =
                new LayoutParams((int)
                        getResources().getDimension(R.dimen.dimen_dp_50),
                        (int) getResources().getDimension(R.dimen.dimen_dp_50));
        progressPieView.setTextSize(13);
        progressPieView.setStrokeWidth(1);
        progressPieView.setTextColor(Color.WHITE);
        progressPieView.setProgressFillType(ProgressPieView.FILL_TYPE_RADIAL);
        progressPieView.setBackgroundColor(Color.TRANSPARENT);
        progressPieView.setProgressColor(Color.parseColor("#BBFFFFFF"));
        progressPieView.setStrokeColor(Color.WHITE);
        progressPieView.setLayoutParams(tvLp);
        tvLp.addRule(CENTER_IN_PARENT);
        progressPieView.setLayoutParams(tvLp);
        progressPieView.setVisibility(GONE);
        addView(progressPieView);
    }

    public ProgressImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ProgressImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ProgressImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    public void setProgress(int progress) {
        progressPieView.setVisibility(VISIBLE);
        this.mProgress = progress;
        progressPieView.setProgress(mProgress);
        progressPieView.setText(String.format(Locale.getDefault(), "%d%%", progress));
    }

    public ImageView getImageView() {
        return photoView;
    }

    public ProgressPieView getProgressPieView() {
        return progressPieView;
    }
}
