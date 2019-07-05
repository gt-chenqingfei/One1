package com.oneone.modules.profile.adapter;

import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.framework.ui.BaseRecyclerViewAdapter;
import com.oneone.framework.ui.BaseViewHolder;
import com.oneone.framework.ui.utils.ScreenUtil;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class IntersectionTagAdapter extends BaseRecyclerViewAdapter<String> {

    public IntersectionTagAdapter() {
        super(null);
    }

    @NonNull
    @Override
    public BaseViewHolder<String> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_textview, parent, false);
        return new IntersectionTagViewHolder(view);
    }

    class IntersectionTagViewHolder extends BaseViewHolder<String> {

        @BindView(R.id.item_textview)
        TextView textView;

        protected IntersectionTagViewHolder(View v) {
            super(v);
        }

        @Override
        public void bind(String s, int position) {
            super.bind(s, position);
            textView.setText(s);
            textView.setWidth(DensityUtil.dp2px(147));
            textView.setTextColor(getContext().getResources().getColor(getRandomColor()));
            textView.setTextSize(getRandomSize());
            textView.setGravity(getRandomPosition());
        }

        public int getRandomColor() {
            List<Integer> colorList = new ArrayList<>();
            colorList.add(R.color.single_flow_content_bg_5);
            colorList.add(R.color.single_flow_content_bg_6);
            colorList.add(R.color.single_flow_content_bg_7);
            colorList.add(R.color.single_flow_content_bg_8);
            colorList.add(R.color.single_flow_content_bg_9);
            colorList.add(R.color.single_flow_content_bg_10);
            return colorList.get((int) (Math.random() * colorList.size()));
        }

        public int getRandomSize() {
            List<Integer> colorList = new ArrayList<>();
            colorList.add(ScreenUtil.spTopx(4.5f));
            colorList.add(ScreenUtil.spTopx(5.5f));
            colorList.add(ScreenUtil.spTopx(6.5f));
            return colorList.get((int) (Math.random() * colorList.size()));
        }

        public int getRandomPosition() {
            List<Integer> colorList = new ArrayList<>();
            colorList.add(Gravity.CENTER);
            colorList.add(Gravity.LEFT);
            colorList.add(Gravity.RIGHT);
            return colorList.get((int) (Math.random() * colorList.size()));
        }
    }

}
