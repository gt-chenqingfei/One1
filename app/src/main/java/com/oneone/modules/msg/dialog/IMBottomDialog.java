package com.oneone.modules.msg.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.framework.ui.BasePopDialog;

import org.w3c.dom.Text;

/**
 * Created by here on 18/6/7.
 */

public class IMBottomDialog extends BasePopDialog implements OnClickListener {
    private IMBottomDialogListener listener;

    public interface IMBottomDialogListener {
        void onSeeUserClick ();
        void onReportClick ();
        void onDeleteContactClick ();
    }

    public IMBottomDialog(@NonNull Context context, IMBottomDialogListener listener) {
        super(context);
        this.listener = listener;
    }

    private TextView seeUserTv;
    private TextView reportTv;
    private TextView deleteContactTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_im_bottom);

        seeUserTv = findViewById(R.id.see_user_tv);
        reportTv = findViewById(R.id.report_tv);
        deleteContactTv = findViewById(R.id.delete_contact_tv);

        seeUserTv.setOnClickListener(this);
        reportTv.setOnClickListener(this);
        deleteContactTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.see_user_tv:
                listener.onSeeUserClick();
                break;
            case R.id.report_tv:
                listener.onReportClick();
                break;
            case R.id.delete_contact_tv:
                listener.onDeleteContactClick();
                break;
        }
    }
}
