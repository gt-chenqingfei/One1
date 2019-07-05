package com.oneone.modules.user.dialog;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.oneone.framework.ui.BasePopDialog;
import com.oneone.framework.ui.R;
import com.oneone.framework.ui.dialog.SheetItem;
import com.oneone.framework.ui.widget.wheelview.adapter.ArrayWheelAdapter;
import com.oneone.framework.ui.widget.wheelview.widget.WheelView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qingfei.chen
 * @since 2018/4/16.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class InComeDialog extends BasePopDialog implements View.OnClickListener {


    public interface InComeSelectedListener {
        void onWheelSelected(int max, int min, String value);
    }

    private WheelView<SheetItem> mWheelView;
    private TextView mTvTitle;
    private TextView mTvPositive;
    private TextView mTvNegative;
    private List<SheetItem> mDataSet;
    private String mTitleLabel;
    private String mPositiveLabel;
    private String mNegativeLabel;
    private InComeSelectedListener listener;


    public InComeDialog(@NonNull Context context, InComeSelectedListener listener) {
        super(context);
        this.listener = listener;

        String[] stringArray = context.getResources().getStringArray(com.oneone.R.array.income_array);
        mDataSet = new ArrayList<>();
        for (String item : stringArray) {
            String[] value = item.split(",");
            SheetItem sheetItem = new SheetItem(value[0], Integer.parseInt(value[1]), Integer.parseInt(value[2]));
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

        mWheelView.setSelection(2);
        mWheelView.setWheelSize(5);

    }

    @Override
    public void onClick(View v) {
        if (v == mTvPositive) {
            SheetItem item = mWheelView.getSelectionItem();
            if (this.listener != null) {
                this.listener.onWheelSelected(item.getArg0(), item.getId(), item.getValue());
            }
        }

        this.dismiss();
    }

    public InComeDialog setTitle(String title) {
        this.mTitleLabel = title;
        return this;
    }

    public InComeDialog setPositive(String positive) {
        this.mPositiveLabel = positive;
        return this;
    }

    public InComeDialog setNegative(String negative) {
        this.mNegativeLabel = negative;
        return this;
    }
}
