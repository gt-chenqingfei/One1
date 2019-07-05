package com.oneone.modules.matcher.relations.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.framework.android.utils.SoftKeyBoardUtil;
import com.oneone.framework.ui.BaseDialog;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.utils.ToastUtil;

import butterknife.BindView;

/**
 * @author qingfei.chen
 * @since 2018/4/23.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */

@LayoutResource(R.layout.dialog_customer_relation)
public class AutoFlowTagAddDialog extends BaseDialog implements View.OnClickListener {
    public interface OnDialogListener {
        void onDialogOkClick(String content);
    }

    @BindView(R.id.dialog_customer_relation_et_relation)
    EditText mEditText;

    @BindView(R.id.dialog_customer_relation_btn_ok)
    TextView mTvOk;

    private int limit;
    private int hintText;
    private int toastText;
    private OnDialogListener listener;

    public AutoFlowTagAddDialog(@NonNull Context context, OnDialogListener listener,
                                int limit, int hintText, int toastText) {
        super(context, R.style.base_dialog);
        this.limit = limit;
        this.hintText = hintText;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTvOk.setOnClickListener(this);
        mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(limit)});
        if (hintText > 0) {
            mEditText.setHint(hintText);
        }
    }

    @Override
    public void onClick(View v) {
        if (this.listener == null) {
            return;
        }
        String content = mEditText.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            if (toastText <= 0) {
                return;
            }
            ToastUtil.show(getContext(), getContext().getString(toastText));
            return;
        }

        if (content.contains(" ")) {
            ToastUtil.show(getContext(), getContext().getString(R.string.str_set_single_flow_page_custom_tag_not_null_notice_space));
            return;
        }

        this.listener.onDialogOkClick(content);
        SoftKeyBoardUtil.hideSoftInput(mEditText);
        this.dismiss();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        SoftKeyBoardUtil.hideSoftInput(mEditText);
        return super.onTouchEvent(event);
    }
}
