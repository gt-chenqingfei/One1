package com.oneone.framework.ui.dialog;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.oneone.framework.ui.BasePopDialog;
import com.oneone.framework.ui.R;
import com.oneone.framework.ui.widget.wheelview.adapter.ArrayWheelAdapter;
import com.oneone.framework.ui.widget.wheelview.widget.WheelView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qingfei.chen
 * @since 2018/4/16.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class WheelDialog extends BasePopDialog implements View.OnClickListener {


    public interface WheelSelectedListener {
        void onWheelSelected(int id, SheetItem item);
    }

    private WheelView<SheetItem> mWheelView;
    private TextView mTvTitle;
    private TextView mTvPositive;
    private TextView mTvNegative;
    private List<SheetItem> mDataSet;
    private String mTitleLabel;
    private String mPositiveLabel;
    private String mNegativeLabel;
    private WheelSelectedListener listener;
    private int id;

    public WheelDialog(@NonNull Context context, int id, List<SheetItem> dataSet, WheelSelectedListener listener) {
        super(context);
        this.mDataSet = dataSet;
        this.listener = listener;
        this.id = id;
    }

    public WheelDialog(@NonNull Context context, int id, int arrayResId, WheelSelectedListener listener) {
        super(context);
        this.listener = listener;
        this.id = id;

        String[] stringArray = context.getResources().getStringArray(arrayResId);
        mDataSet = new ArrayList<>();
        for (String item : stringArray) {
            String[] value = item.split(",");
            SheetItem sheetItem = new SheetItem(value[0], Integer.parseInt(value[1]));
            mDataSet.add(sheetItem);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_wheel);
        mWheelView = findViewById(R.id.dialog_wheel_wheel_view);
        mTvTitle = findViewById(R.id.dialog_wheel_title_tv);
        mTvNegative = findViewById(R.id.dialog_wheel_negative_tv);
        mTvPositive = findViewById(R.id.dialog_wheel_positive_tv);

        if (!TextUtils.isEmpty(mTitleLabel)) {
            mTvTitle.setText(mTitleLabel);
            mTvTitle.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(mNegativeLabel)) {
            mTvNegative.setText(mNegativeLabel);
            mTvNegative.setVisibility(View.VISIBLE);
            mTvNegative.setOnClickListener(this);
        }

        if (!TextUtils.isEmpty(mPositiveLabel)) {
            mTvPositive.setText(mPositiveLabel);
            mTvPositive.setVisibility(View.VISIBLE);
            mTvPositive.setOnClickListener(this);
        }

        mWheelView.setWheelAdapter(new ArrayWheelAdapter(getContext()));
        mWheelView.setSkin(WheelView.Skin.Common);
        mWheelView.setWheelData(mDataSet);

        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.backgroundColor = Color.TRANSPARENT;
        style.textColor = Color.parseColor("#7E94BB");
        style.selectedTextColor = Color.WHITE;
        style.selectedTextSize = 18;
        style.textSize = 17;
        style.holoBorderColor = Color.TRANSPARENT;

        mWheelView.setStyle(style);
        mWheelView.setWheelSize(5);
        mWheelView.setSelection(2);

    }

    @Override
    public void onClick(View v) {
        if (v == mTvPositive) {
            SheetItem item = mWheelView.getSelectionItem();
            if (this.listener != null) {
                this.listener.onWheelSelected(id, item);
            }
        }

        this.dismiss();
    }

    public WheelDialog setTitle(String title) {
        this.mTitleLabel = title;
        return this;
    }

    public WheelDialog setPositive(String positive) {
        this.mPositiveLabel = positive;
        return this;
    }

    public WheelDialog setNegative(String negative) {
        this.mNegativeLabel = negative;
        return this;
    }
}
