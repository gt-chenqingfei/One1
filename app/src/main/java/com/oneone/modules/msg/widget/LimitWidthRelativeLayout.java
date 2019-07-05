package com.oneone.modules.msg.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.scwang.smartrefresh.layout.util.DensityUtil;

/**
 * Created by here on 18/5/11.
 */

public class LimitWidthRelativeLayout extends RelativeLayout {
    public LimitWidthRelativeLayout(Context context) {
        super(context);
    }

    public LimitWidthRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LimitWidthRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMaxSpec = MeasureSpec.makeMeasureSpec(DensityUtil.dp2px(270), MeasureSpec.AT_MOST);
        super.onMeasure(widthMaxSpec, heightMeasureSpec);
    }
}
