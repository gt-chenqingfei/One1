package com.oneone.modules.dogfood.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.framework.ui.BaseDialog;

/**
 * Created by here on 18/5/2.
 */

public class DailyReceiveDogFoodDialog extends BaseDialog implements View.OnClickListener {
    private int coinCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_daily_receive_dog_food);

        TextView earnValTv = findViewById(R.id.dog_food_earn_val_tv);
        earnValTv.setText("+" + coinCount);

        Button closeBtn = findViewById(R.id.close_btn);
        closeBtn.setOnClickListener(this);
    }

    public DailyReceiveDogFoodDialog(@NonNull Context context, int coinCount) {

        super(context, R.style.base_dialog);
        this.coinCount = coinCount;
    }

    public DailyReceiveDogFoodDialog(@NonNull Context context, int coinCount, int style) {
        super(context, style);
        this.coinCount = coinCount;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close_btn:
                dismiss();
                break;
        }
    }
}
