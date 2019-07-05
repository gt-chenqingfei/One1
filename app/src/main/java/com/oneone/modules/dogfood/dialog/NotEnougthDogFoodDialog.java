package com.oneone.modules.dogfood.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.framework.ui.BaseDialog;

/**
 * Created by here on 18/7/11.
 */

public class NotEnougthDogFoodDialog extends BaseDialog implements View.OnClickListener {

    public NotEnougthDogFoodDialog(@NonNull Context context) {
        super(context, R.style.base_dialog);
        setContentView(R.layout.dialog_not_enougth_dog_food);

        TextView closeTv = findViewById(R.id.close_tv);
        closeTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close_tv:
                dismiss();
                break;
        }
    }
}
