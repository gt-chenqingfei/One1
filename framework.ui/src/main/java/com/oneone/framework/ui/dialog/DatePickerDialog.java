package com.oneone.framework.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.oneone.framework.android.utils.TimeUtils;
import com.oneone.framework.ui.BasePopDialog;
import com.oneone.framework.ui.R;

import java.util.Calendar;


public class DatePickerDialog extends BasePopDialog implements View.OnClickListener {
    public static final long DAY_TIME = 86400000;

    private TextView mTvConfirm;
    private TextView mTvCancel;
    private DatePicker mTvDatePicker;

    private Context mContext;
    private OnDatePickListener onDatePickListener;

    public DatePickerDialog(Context context, OnDatePickListener listener) {
        super(context);
        this.mContext = context;
        this.onDatePickListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_date_picker);
        mTvConfirm = findViewById(R.id.date_confirm_tv);
        mTvCancel = findViewById(R.id.date_cancel_tv);
        mTvDatePicker = findViewById(R.id.date_picker);

        Calendar toDayCalendar = Calendar.getInstance();
        toDayCalendar.setTimeInMillis(System.currentTimeMillis());
        toDayCalendar.set(toDayCalendar.get(Calendar.YEAR) - 18, toDayCalendar.get(Calendar.MONTH), toDayCalendar.get(Calendar.DAY_OF_MONTH));
        mTvDatePicker.setMaxDate(toDayCalendar.getTimeInMillis());
        toDayCalendar.set(toDayCalendar.get(Calendar.YEAR) - 72, toDayCalendar.get(Calendar.MONTH), toDayCalendar.get(Calendar.DAY_OF_MONTH));
        mTvDatePicker.setMinDate(toDayCalendar.getTimeInMillis());
        mTvDatePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);


        mTvCancel.setOnClickListener(this);
        mTvConfirm.setOnClickListener(this);
    }

    public DatePickerDialog builder() {
        return this;
    }

    public DatePickerDialog setCanCancelable(boolean cancel) {
        this.setCancelable(cancel);
        return this;
    }

    public DatePickerDialog setOnTouchOutside(boolean touchOutside) {
        this.setCanceledOnTouchOutside(touchOutside);
        return this;
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.date_confirm_tv) {
            if (onDatePickListener != null) {
                String date = mTvDatePicker.getYear()
                        + "-" + ((mTvDatePicker.getMonth() + 1) < 10 ? ("0" + (mTvDatePicker.getMonth() + 1)) : (mTvDatePicker.getMonth() + 1))
                        + "-" + (mTvDatePicker.getDayOfMonth() < 10 ? ("0" + mTvDatePicker.getDayOfMonth()) : mTvDatePicker.getDayOfMonth());
                onDatePickListener.onDateSelected(date);
            }
        }

        this.dismiss();
    }

    public interface OnDatePickListener {
        void onDateSelected(String date);
    }


}
