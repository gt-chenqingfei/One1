package com.oneone.modules.user.dialog;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.framework.ui.BasePopDialog;
import com.oneone.framework.ui.dialog.SheetItem;
import com.oneone.framework.ui.utils.Res;
import com.oneone.framework.ui.widget.wheelview.adapter.ArrayWheelAdapter;
import com.oneone.framework.ui.widget.wheelview.widget.WheelView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qingfei.chen
 * @since 2018/4/16.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class HeightDialog extends BasePopDialog implements View.OnClickListener {


    public interface HeightSelectedListener {
        void onHeightSelected(int id, String value);
    }

    private WheelView<SheetItem> mWheelView;
    private TextView mTvTitle;
    private TextView mTvPositive;
    private TextView mTvNegative;
    private List<SheetItem> mDataSet;
    private String mTitleLabel;
    private String mPositiveLabel;
    private String mNegativeLabel;
    private HeightSelectedListener listener;

    public HeightDialog(@NonNull Context context, HeightSelectedListener listener) {
        super(context);
        this.listener = listener;
        mDataSet = new ArrayList<>();

        for (int i = 145; i <= 200; i++) {
            String name = i + "cm";
            int value = i;
            SheetItem sheetItem = new SheetItem(name, value);
            mDataSet.add(sheetItem);
        }

        SheetItem sheetItem = new SheetItem("145cm" + Res.getString(R.string.str_above), 144);
        mDataSet.add(0, sheetItem);
        sheetItem = new SheetItem("200cm" + Res.getString(R.string.str_bellow), 201);
        mDataSet.add(sheetItem);
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
                this.listener.onHeightSelected(item.getId(), item.getValue());
            }
        }

        this.dismiss();
    }

    public HeightDialog setTitle(String title) {
        this.mTitleLabel = title;
        return this;
    }

    public HeightDialog setPositive(String positive) {
        this.mPositiveLabel = positive;
        return this;
    }

    public HeightDialog setNegative(String negative) {
        this.mNegativeLabel = negative;
        return this;
    }
}
