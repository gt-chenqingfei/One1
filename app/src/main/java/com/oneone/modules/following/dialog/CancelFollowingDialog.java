package com.oneone.modules.following.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.framework.ui.BasePopDialog;
import com.oneone.modules.following.beans.FollowListItem;
import com.oneone.modules.user.bean.UserInfoBase;

/**
 * Created by here on 18/4/26.
 */

public class CancelFollowingDialog extends BasePopDialog implements View.OnClickListener {
    public interface CancelFollowingDialogListener {
        void onCancelSelected (FollowListItem followListItem);
    }

    private TextView titleTv;
    private TextView confirmTv;
    private TextView cancelTv;

    private FollowListItem followListItem;

    private CancelFollowingDialogListener listener;

    public CancelFollowingDialog(@NonNull Context context, FollowListItem followListItem, CancelFollowingDialogListener listener) {
        super(context);
        this.followListItem = followListItem;
        this.listener = listener;
    }

    public CancelFollowingDialog(@NonNull Context context, int style) {
        super(context, style);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_cancel_following);
        titleTv = findViewById(R.id.title_tv);
        confirmTv = findViewById(R.id.confirm_tv);
        cancelTv = findViewById(R.id.cancel_tv);

        titleTv.setText(Html.fromHtml("<font color=#6F7C91>" + getContext().getResources().getString(R.string.str_my_following_cancel_follow_dialog_title_left_text) + "<font/>"
                + "<font color=#000000>" + followListItem.getUserInfo().getNickname() + "<font/>"
                + "<font color=#6F7C91>" + getContext().getResources().getString(R.string.str_my_following_cancel_follow_dialog_title_right_text) + "<font/>"
        ));

        confirmTv.setOnClickListener(this);
        cancelTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirm_tv:
                listener.onCancelSelected(followListItem);
                this.dismiss();
                break;
            case R.id.cancel_tv:
                this.dismiss();
                break;
        }
    }
}
