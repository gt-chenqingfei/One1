package com.oneone.framework.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.oneone.framework.ui.BaseDialog;
import com.oneone.framework.ui.R;


/**
 * @author qingfei.chen
 * @since 2018/2/5.
 * Copyright © 2017 HereTech Technology Co.,Ltd. All rights reserved.
 */
public class WarnDialog extends BaseDialog implements View.OnClickListener {

    TextView mBtnNegative;
    TextView mBtnPositive;
    TextView mTvMessage;

    private String mPositiveLabel;
    private String mNegativeLabel;

    private String mMessage;
    private OnPositiveClickListener mPositiveClickListener;
    private DialogInterface.OnClickListener mOnNegativeButtonClickListener;

    public interface OnPositiveClickListener {
        void onPositiveClick();
    }

    public WarnDialog(Context context, String message, OnPositiveClickListener listener) {
        super(context, R.style.warn_dialog);
        this.mMessage = message;
        this.mPositiveClickListener = listener;
    }


    public WarnDialog(Context context, @StringRes int message,
                      OnPositiveClickListener listener) {
        this(context, context.getString(message), listener);

        mPositiveLabel = getContext().getResources().getString(R.string.str_ok);
        mNegativeLabel = getContext().getResources().getString(R.string.str_cancel);
    }


    public WarnDialog setOnPositiveClickListener(OnPositiveClickListener listener) {
        this.mPositiveClickListener = listener;
        return this;
    }

    public WarnDialog setNegativeButton(String negativeLabel) {
        this.mNegativeLabel = negativeLabel;
        return this;
    }

    public WarnDialog setPositiveButton(String positive) {
        this.mPositiveLabel = positive;
        return this;
    }

    public WarnDialog setNegativeButton(String negativeLabel, DialogInterface.OnClickListener listener) {
        this.mNegativeLabel = negativeLabel;
        this.mOnNegativeButtonClickListener = listener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_warn);
        mBtnNegative = findViewById(R.id.dialog_warn_btn_negative);
        mBtnPositive = findViewById(R.id.dialog_warn_btn_positive);
        mTvMessage = findViewById(R.id.dialog_warn_tv_message);
        if (!TextUtils.isEmpty(mPositiveLabel)) {
            mBtnPositive.setText(mPositiveLabel);
        } else {
            mBtnPositive.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(mNegativeLabel)) {
            mBtnNegative.setText(mNegativeLabel);
        } else {
            mBtnNegative.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(mMessage)) {
            mTvMessage.setText(mMessage);
            mTvMessage.setVisibility(View.VISIBLE);
        }

        mBtnPositive.setOnClickListener(this);
        mBtnNegative.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.dialog_warn_btn_positive) {
            if (mPositiveClickListener != null) {
                mPositiveClickListener.onPositiveClick();
            }
            this.cancel();
        } else if (v.getId() == R.id.dialog_warn_btn_negative) {
            this.cancel();
            if (this.mOnNegativeButtonClickListener != null) {
                this.mOnNegativeButtonClickListener.onClick(this, v.getId());
            }
        }
    }

}
