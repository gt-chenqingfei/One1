package com.oneone.modules.dogfood.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.framework.ui.BaseDialog;
import com.oneone.framework.ui.BasePopDialog;

/**
 * Created by here on 18/5/2.
 */

public class WhatIsDogFoodDialog extends BaseDialog {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_what_is_dog_food);

        TextView closeTv = findViewById(R.id.close_tv);
        closeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("========>click   ");
                WhatIsDogFoodDialog.this.dismiss();
            }
        });
    }

    public WhatIsDogFoodDialog(@NonNull Context context) {
        super(context);
    }

    public WhatIsDogFoodDialog(@NonNull Context context, int style) {
        super(context, style);
    }
}
