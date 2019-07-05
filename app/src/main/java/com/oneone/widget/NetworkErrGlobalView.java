package com.oneone.widget;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.oneone.R;
import com.oneone.framework.ui.BaseView;
import com.oneone.framework.ui.annotation.LayoutResource;

import butterknife.BindView;

/**
 * Created by here on 18/7/9.
 */

@LayoutResource(R.layout.network_err_global_view)
public class NetworkErrGlobalView extends BaseView implements View.OnClickListener {
    public static final String NET_ERR_DEFAULT_ANIM = "empty_default.json";

    @BindView(R.id.err_iv)
    LottieAnimationView errIv;
    @BindView(R.id.err_tv)
    TextView errTv;
    @BindView(R.id.reload_btn)
    Button reloadBtn;
    @BindView(R.id.solution_tv)
    TextView solutionTv;

    public NetworkErrGlobalView(Context context) {
        super(context);

        errIv.setAnimation(NET_ERR_DEFAULT_ANIM, LottieAnimationView.CacheStrategy.Weak);
        errIv.setRepeatCount(LottieDrawable.INFINITE);
        errIv.playAnimation();

        solutionTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        solutionTv.setOnClickListener(this);
        reloadBtn.setOnClickListener(this);
    }

    @Override
    public void onViewCreated() {

    }

    @Override
    public void onClick(View view) {

    }
}
