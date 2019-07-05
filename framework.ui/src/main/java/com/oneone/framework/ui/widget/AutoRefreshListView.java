package com.oneone.framework.ui.widget;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class AutoRefreshListView extends ListView {

    public enum State {
        REFRESHING,
        RESET,
    }

    public enum Mode {
        START,
        END,
        BOTH,
    }

    public interface OnRefreshListener {
        public void onRefreshFromStart();

        public void onRefreshFromEnd();
    }

    private OnRefreshListener refreshListener;
    private List<OnScrollListener> scrollListeners = new ArrayList<OnScrollListener>();

    private State state = State.RESET;
    private Mode mode = Mode.START;
    private Mode currentMode = Mode.START;

    private boolean refreshableStart = true;
    private boolean refreshableEnd = true;

    private ViewGroup refreshHeader;
    private ViewGroup refreshFooter;

    private int offsetY;

    public AutoRefreshListView(Context context) {
        super(context);
    }

    public AutoRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoRefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void setOnRefreshListener(OnRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        // replaced by addOnScrollListener
        throw new UnsupportedOperationException("Use addOnScrollListener instead!");
    }

    public void addOnScrollListener(OnScrollListener l) {
        scrollListeners.add(l);
    }

    public void removeOnScrollListener(OnScrollListener l) {
        scrollListeners.remove(l);
    }

    public void init(Context context, @LayoutRes int headerLayout, @LayoutRes int footerLayout) {
        addRefreshView(context, headerLayout, footerLayout);

        super.setOnScrollListener(new OnScrollListener() {
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                for (OnScrollListener listener : scrollListeners) {
                    listener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                }
            }

            public void onScrollStateChanged(AbsListView view, int scrollState) {
                for (OnScrollListener listener : scrollListeners) {
                    listener.onScrollStateChanged(view, scrollState);
                }
            }
        });

        initRefreshListener();

        state = State.RESET;
    }

    private void addRefreshView(Context context, @LayoutRes int headerLayout, @LayoutRes int footerLayout) {
        refreshHeader = (ViewGroup) View.inflate(context, headerLayout, null);
        addHeaderView(refreshHeader, null, false);
        refreshFooter = (ViewGroup) View.inflate(context, footerLayout, null);
        addFooterView(refreshFooter, null, false);
    }

    private void initRefreshListener() {
        OnScrollListener listener = new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE && state == State.RESET) {
                    boolean reachTop = (getFirstVisiblePosition() < getHeaderViewsCount() && getCount() > getHeaderViewsCount());
                    if (reachTop) {
                        onRefresh(true, false);
                    } else {
                        boolean reachBottom = getLastVisiblePosition() >= getCount() - 1;
                        if (reachBottom) {
                            onRefresh(false, true);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        };

        addOnScrollListener(listener);
    }

    private void onRefresh(boolean start, boolean end) {
        if (refreshListener != null) {
            View firstVisibleChild = getChildAt(getHeaderViewsCount());
            if (firstVisibleChild != null) {
                offsetY = firstVisibleChild.getTop();
            }

            if (start && refreshableStart && mode != Mode.END) {
                currentMode = Mode.START;
                state = State.REFRESHING;
                refreshListener.onRefreshFromStart();
            } else if (end && refreshableEnd && mode != Mode.START) {
                currentMode = Mode.END;
                state = State.REFRESHING;
                refreshListener.onRefreshFromEnd();
            }
            updateRefreshView();
        }
    }

    private void updateRefreshView() {
        switch (state) {
            case REFRESHING:
                getRefreshView().getChildAt(0).setVisibility(View.VISIBLE);
                break;
            case RESET:
                if (currentMode == Mode.START) {
                    refreshHeader.getChildAt(0).setVisibility(View.GONE);
                } else {
                    refreshFooter.getChildAt(0).setVisibility(View.GONE);
                }
                break;
        }
    }

    private ViewGroup getRefreshView() {
        switch (currentMode) {
            case END:
                return refreshFooter;
            case START:
            default:
                return refreshHeader;
        }
    }

    public void onRefreshStart(Mode mode) {
        state = State.REFRESHING;
        currentMode = mode;
    }

    public State getRefreshState() {
        return state;
    }

    /**
     * 加载完成
     */
    public void onRefreshComplete(int count, int requestCount, boolean needOffset) {
        state = State.RESET;
        resetRefreshView(count, requestCount);
        if (!needOffset) {
            return;
        }

        if (currentMode == Mode.START) {
            setSelectionFromTop(count + getHeaderViewsCount(), refreshableStart ? offsetY : 0);
        }
    }

    public void onRefreshComplete() {
        state = State.RESET;
        updateRefreshView();
    }

    private void resetRefreshView(int count, int requestCount) {
        if (currentMode == Mode.START) {
            /** 如果是第一次加载，如果count<requestCount, 表示没有数据了。
             * 如果是后面的加载，为了列表稳定，只有count>0, 就保留header的高度
             */
            if (getCount() == count + getHeaderViewsCount() + getFooterViewsCount()) {
                refreshableStart = (count == requestCount);
            } else {
                refreshableStart = (count > 0);
            }
        } else {
            refreshableEnd = (count > 0);
        }
        updateRefreshView();
    }

    /**
     * handle over scroll when no more data
     */
    private boolean isBeingDragged = false;
    private int startY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            return onTouchEventInternal(event);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean onTouchEventInternal(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onTouchBegin(event);
                break;
            case MotionEvent.ACTION_MOVE:
                onTouchMove(event);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                onTouchEnd();
                break;
        }
        return super.onTouchEvent(event);
    }

    private void onTouchBegin(MotionEvent event) {
        int firstItemIndex = getFirstVisiblePosition();
        if (!refreshableStart && firstItemIndex <= getHeaderViewsCount() && !isBeingDragged) {
            isBeingDragged = true;
            startY = (int) event.getY();
        }
    }

    private void onTouchMove(MotionEvent event) {
        /** check state again */
        onTouchBegin(event);
        if (!isBeingDragged) {
            return;
        }

        /** scroll to dragged position */
        int offsetY = (int) (event.getY() - startY);
        offsetY = Math.max(offsetY, 0) / 2;
        refreshHeader.setPadding(0, offsetY, 0, 0);
    }

    private void onTouchEnd() {
        if (isBeingDragged) {
            refreshHeader.setPadding(0, 0, 0, 0);
        }

        isBeingDragged = false;
    }
}