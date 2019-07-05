package com.oneone.framework.ui.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * https://github.com/snalopainen/DampingScrollView
 */

public class DampingScrollView extends ScrollView {


    private OnScrollListener onScrollListener;
    private OnRefreshListener onRefreshListener;
    private OnLoadMoreListener onLoadMoreListener;
    private static final int SCROLLER_DURATION_MAX = 500;
    private static final int LEN = 0xc8;
    private static final int DURATION = 500;
    private static final int MAX_DY = 200;
    private Scroller mScroller;
    private TouchTool tool;
    private int left, top;
    private float startX, startY, currentX, currentY;
    private int imageViewH;
    private int rootW, rootH;
    private ImageView imageView;
    private boolean scrollerType;
    private long computerScrollerTimeMillis = 0;
    private long scrollerChangeTimeMillis = 0;


    public DampingScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public DampingScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }

    public DampingScrollView(Context context) {
        super(context);

    }


    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public void resetScrollView() {
        if (onScrollListener != null) {
            onScrollListener.onScroll(getScrollY());
        }
    }

    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {

        return 0;
    }

    private int[] li = new int[2];
    private int[] li2 = new int[2];
    private float lastLy;
    private boolean startIsTop = true;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        super.dispatchTouchEvent(event);
        int action = event.getAction();
        if (!mScroller.isFinished()) {
            return super.onTouchEvent(event);
        }
        currentX = event.getX();
        currentY = event.getY();
        imageView.getLocationInWindow(li);
        getLocationOnScreen(li2);
        imageView.getTop();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (li[1] != li2[1]) {// 判断开始触摸时，imageview和窗口顶部对齐没
                    startIsTop = false;
                }
                left = imageView.getLeft();
                top = imageView.getBottom();
                rootW = getWidth();
                rootH = getHeight();
                imageViewH = imageView.getHeight();
                startX = currentX;
                startY = currentY;
                tool = new TouchTool(imageView.getLeft(), imageView.getBottom(), imageView.getLeft(),
                        imageView.getBottom() + LEN);
                break;
            case MotionEvent.ACTION_MOVE:
                if (!startIsTop && li[1] == li2[1]) {
                    startY = currentY;
                    startIsTop = true;
                }
                if (imageView.isShown() && imageView.getTop() >= 0) {
                    if (tool != null) {
                        int t = tool.getScrollY(currentY - startY);
                        if (!scrollerType && currentY < lastLy && imageView.getHeight() > imageViewH) {
                            scrollTo(0, 0);
                            imageView.getLocationInWindow(li);
                            getLocationOnScreen(li2);
                            android.view.ViewGroup.LayoutParams params = imageView.getLayoutParams();
                            params.height = t;
                            if (onScrollListener != null)
                                onScrollListener.onScroll(getScrollY() - (t - imageViewH));
                            imageView.setLayoutParams(params);
                            if (imageView.getHeight() == imageViewH && li[1] == li2[1]) {
                                scrollerType = true;
                            }
                            if (startIsTop && li[1] != li2[1]) {
                                startIsTop = false;
                            }
                        }
                        if (t >= top && t <= imageView.getBottom() + LEN && li[1] == li2[1] && currentY > lastLy) {
                            android.view.ViewGroup.LayoutParams params = imageView.getLayoutParams();
                            params.height = t;
                            if (onScrollListener != null)
                                onScrollListener.onScroll(getScrollY() - (t - imageViewH));
                            imageView.setLayoutParams(params);
                        }
                    }
                    scrollerType = false;
                }

                lastLy = currentY;
                break;
            case MotionEvent.ACTION_UP:
                if (li[1] == li2[1]) {
                    scrollerType = true;
                    mScroller.startScroll(imageView.getLeft(), imageView.getBottom(), 0 - imageView.getLeft(),
                            imageViewH - imageView.getBottom(), DURATION);
                    invalidate();
                }
                startIsTop = true;
                break;

            default:
                break;
        }

        return true;
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();
            imageView.layout(0, 0, x + imageView.getWidth(), y);
            invalidate();
            if (!mScroller.isFinished() && scrollerType && y > MAX_DY) {
                android.view.ViewGroup.LayoutParams params = imageView.getLayoutParams();
                params.height = y;
                int scrollY = getScrollY() - (y - imageViewH);
                if (onScrollListener != null) {
                    onScrollListener.onScroll(scrollY);
                }
                if (scrollY == 0) {
                    if (System.currentTimeMillis() - computerScrollerTimeMillis < SCROLLER_DURATION_MAX) {
                        return;
                    }
                    computerScrollerTimeMillis = System.currentTimeMillis();
                    if (onRefreshListener != null) {
                        onRefreshListener.onPullRefresh();
                    }
                }

                imageView.setLayoutParams(params);
            }
        }
    }

    public class TouchTool {

        private int startX, startY;

        public TouchTool(int startX, int startY, int endX, int endY) {
            super();
            this.startX = startX;
            this.startY = startY;
        }

        public int getScrollX(float dx) {
            return (int) (startX + dx / 2.5F);
        }

        public int getScrollY(float dy) {
            return (int) (startY + dy / 2.5F);
        }
    }


    /**
     * 设置滚动接口
     *
     * @param onScrollListener
     */
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public void setOnPullRefreshListener(OnRefreshListener listener) {
        this.onRefreshListener = listener;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.onLoadMoreListener = listener;
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        Log.e("onScrollChanged", "l:" + l + ",t:" + t + ",oldl:" + oldl + ",oldt:" + oldt);
        if (onScrollListener != null) {
            onScrollListener.onScroll(t);
        }

        View view = getChildAt(getChildCount() - 1);
        int d = view.getBottom();
        d -= (getHeight() + getScrollY());
        if (d == 0) {
            //you are at the end of the list in scrollview
            //do what you wanna do here
            if (System.currentTimeMillis() - scrollerChangeTimeMillis < SCROLLER_DURATION_MAX) {
                return;
            }
            scrollerChangeTimeMillis = System.currentTimeMillis();
            if (onLoadMoreListener != null) {
                onLoadMoreListener.onLoadMore();
            }
        }

    }


    /**
     * 滚动的回调接口
     */
    public interface OnScrollListener {
        /**
         * 回调方法， 返回MyScrollView滑动的Y方向距离
         */
        void onScroll(int scrollY);
    }

    public interface OnRefreshListener {
        void onPullRefresh();
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
