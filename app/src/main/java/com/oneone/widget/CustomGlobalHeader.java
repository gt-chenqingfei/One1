package com.oneone.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.oneone.R;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.internal.InternalAbstract;

import java.text.DecimalFormat;

/**
 * 自定义全局刷新 Header，使用统一的刷新动画，可以在布局中设置其背景颜色 phPrimaryColor
 * <p>
 * Created by ZhaiDongyang on 2018/6/28
 */
public class CustomGlobalHeader extends InternalAbstract implements RefreshHeader {

    private static final String REFRESH_HEADER_PULLING = "head_pull.json";
    private static final String REFRESH_HEADER_RELEASE = "head_release.json";
    private static final String REFRESH_HEADER_REFRESHING = "head_refreshing.json";
    LottieAnimationView mAnimationView;
    private TypedArray ta;
    private int headerHeight;
    private float progressFormat;
    private int mColor = 0;

    public CustomGlobalHeader(Context context) {
        this(context, null);
    }

    public CustomGlobalHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomGlobalHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ta = context.obtainStyledAttributes(attrs, R.styleable.CustomGlobalHeader);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_refresh_head, this);
        mAnimationView = view.findViewById(R.id.lottie);
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        switch (newState) {
            case None:
            case PullDownToRefresh:
                // 刚往下拉的时候-提示：下拉可以刷新
                mAnimationView.setAnimation(REFRESH_HEADER_PULLING);
                mAnimationView.setRepeatCount(0);
                mAnimationView.playAnimation();
                break;
            case Refreshing:
            case RefreshReleased:
                // 正在刷新
                mAnimationView.setAnimation(REFRESH_HEADER_REFRESHING);
                mAnimationView.setRepeatCount(LottieDrawable.INFINITE);
                mAnimationView.playAnimation();
                break;
            case ReleaseToRefresh:
                // 拖到一定程度的时候-提示：释放刷新
                break;
        }
    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {
        headerHeight = height;
        if (ta.hasValue(R.styleable.CustomGlobalHeader_phPrimaryColor)) {
            kernel.requestDrawBackgroundFor(this, ta.getColor(R.styleable.CustomGlobalHeader_phPrimaryColor, 0));
        }
        if (mColor != 0) {
            kernel.requestDrawBackgroundFor(this, mColor);
        } else {
            kernel.requestDrawBackgroundFor(this, getResources().getColor(R.color.color_796CF0));
        }
    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {
        super.onMoving(isDragging, percent, offset, height, maxDragHeight);
        if (offset <= headerHeight) {
            float progress = (float) offset / headerHeight;
            progressFormat = Float.parseFloat(new DecimalFormat("0.00").format(progress));
            mAnimationView.setProgress(progressFormat);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mAnimationView.cancelAnimation();
    }

    public void setBackgroundColor(int color) {
        this.mColor = color;
    }

}
