package com.oneone.framework.ui.imagepicker.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 设置横向 RecyclerView 的 item 之间的间距
 * <p>
 * Created by ZhaiDongyang on 2018/7/17
 */
public class RecyclerViewHorizontalItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpace;

    public RecyclerViewHorizontalItemDecoration(int space) {
        this.mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int pos = parent.getChildAdapterPosition(view);
        if (pos != 0) {
            outRect.left = mSpace;
        }
    }
}
