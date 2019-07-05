package com.oneone.framework.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.HashMap;
import java.util.LinkedHashMap;


public class MeasureViewPager extends ViewPager {
    private int current;
    private int height = 0;
    private int minHeight = 0;
    /**
     * 保存position与对于的View
     */
    private HashMap<Integer, View> mChildrenViews = new LinkedHashMap<Integer, View>();

    private boolean scrollble = true;
    /* 是否是自适应 */
    private boolean isAdaptive = true;

    public MeasureViewPager(Context context) {
        super(context);
    }


    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
    }

    public void setAdaptive(boolean adaptive) {
        isAdaptive = adaptive;
    }

    public MeasureViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mChildrenViews.size() > current) {
            View child = mChildrenViews.get(current);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            height = child.getMeasuredHeight();
            if (!isAdaptive) {
                if (height < minHeight)
                    height = minHeight;
            }
        }


        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void resetHeight(int current) {
        this.current = current;
        if (mChildrenViews.size() > current) {

            if (getLayoutParams() instanceof RelativeLayout.LayoutParams) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
                if (layoutParams == null) {
                    if (isAdaptive) {
                        layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                                height);
                    } else {
                        layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.MATCH_PARENT);
                    }
                } else {
                    if (isAdaptive) {
                        layoutParams.height = height;
                    } else {
                        layoutParams.height = RelativeLayout.LayoutParams.MATCH_PARENT;
                    }
                }
                setLayoutParams(layoutParams);
            } else if (getLayoutParams() instanceof LinearLayout.LayoutParams) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getLayoutParams();
                if (layoutParams == null) {
                    if (isAdaptive) {
                        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
                    } else {
                        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    }
                } else {
                    if (isAdaptive) {
                        layoutParams.height = height;
                    } else {
                        layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT;
                    }
                }
                setLayoutParams(layoutParams);
            }


        }
    }

    /**
     * 保存position与对于的View
     */
    public void setObjectForPosition(View view, int position) {
        mChildrenViews.put(position, view);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!scrollble) {
            return true;
        }
        return super.onTouchEvent(ev);
    }


    public boolean isScrollble() {
        return scrollble;
    }

    public void setScrollble(boolean scrollble) {
        this.scrollble = scrollble;
    }
}
