package com.oneone.modules.find.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.framework.ui.BaseDialog;

/**
 * Created by here on 18/7/19.
 */

public class NoFeelDialog extends BaseDialog implements View.OnClickListener {
    private TextView cancelTv;
    private RelativeLayout confirmLayout;

    public interface NoFeelDialogListener {
        void onNoFeelSelected ();
    }

    private NoFeelDialogListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirm_no_feel);
        cancelTv = findViewById(R.id.cancel_tv);
        confirmLayout = findViewById(R.id.confirm_layout);

        cancelTv.setOnClickListener(this);
        confirmLayout.setOnClickListener(this);
    }

    public NoFeelDialog(@NonNull Context context, NoFeelDialogListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_tv:
                break;
            case R.id.confirm_layout:
                listener.onNoFeelSelected();
                break;
        }
        this.dismiss();
    }
}
