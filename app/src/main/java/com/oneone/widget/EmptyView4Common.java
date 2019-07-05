package com.oneone.widget;

import android.content.Context;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.oneone.R;
import com.oneone.framework.ui.BaseView;
import com.oneone.framework.ui.annotation.LayoutResource;

import butterknife.BindView;

/**
 * @author qingfei.chen
 * @since 2018/6/14.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */

@LayoutResource(R.layout.empty_view_4_common)
public class EmptyView4Common extends BaseView {
    public static final String EMPTY_DEFAULT_ANIM = "empty_default.json";

    @BindView(R.id.no_data_iv)
    LottieAnimationView animView;
    @BindView(R.id.no_data_tv)
    TextView noDataTv;

    public EmptyView4Common(Context context, String noDataTvStr) {
        super(context);

        animView.setAnimation(EMPTY_DEFAULT_ANIM, LottieAnimationView.CacheStrategy.Weak);
        animView.setRepeatCount(LottieDrawable.INFINITE);
        animView.playAnimation();

        noDataTv.setText(getResources().getString(R.string.str_common_no_data_text) + noDataTvStr);
    }

    public EmptyView4Common(Context context, String emptyAnimJson, String noDataTvStr) {
        super(context);

        animView.setAnimation(emptyAnimJson, LottieAnimationView.CacheStrategy.Weak);
        animView.playAnimation();

        noDataTv.setText(getResources().getString(R.string.str_common_no_data_text) + noDataTvStr);
    }

    @Override
    public void onViewCreated() {

    }
}
