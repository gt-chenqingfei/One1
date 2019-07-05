package com.oneone.modules.msg.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.modules.msg.IMManager;
import com.oneone.modules.msg.activity.ImTalkActivity;
import com.oneone.modules.msg.beans.TalkBeans.MyMessage;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.messages.BaseMessageViewHolder;
import cn.jiguang.imui.messages.MessageListStyle;
import cn.jiguang.imui.messages.MsgListAdapter;

/**
 * Created by here on 18/5/1.
 */

public class MyImTextViewHolder extends BaseMessageViewHolder<MyMessage> implements MsgListAdapter.DefaultMessageViewHolder {
    private boolean isMe;

    private RelativeLayout contentLayout;
    private TextView contentTv;
    private RelativeLayout msgStatusLayout;
    private ImageView msgStatusIv;

    public MyImTextViewHolder(View itemView, boolean isMe) {
        super(itemView);
//        this.mContext = context;
        this.isMe = isMe;
        System.out.println("================" + isMe);
//
        contentLayout = itemView.findViewById(R.id.content_layout);
        contentTv = itemView.findViewById(R.id.content_tv);
        msgStatusLayout = itemView.findViewById(R.id.im_msg_status_layout);
        msgStatusIv = itemView.findViewById(R.id.im_msg_status_failed_iv);
    }

    @Override
    public void onBind(final MyMessage message) {
        if (message.getMessageStatus() == IMessage.MessageStatus.SEND_FAILED) {
            msgStatusIv.setVisibility(View.VISIBLE);
            msgStatusIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (IMManager.curToImUid != null) {
                        IMManager.getInstance().reSendTxt(IMManager.curToImUid, message);
                    }
                }
            });
        } else {
            msgStatusIv.setVisibility(View.GONE);
        }

        contentTv.setText(message.getText());
    }

    @Override
    public void applyStyle(MessageListStyle style) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) contentLayout.getLayoutParams();
        if (isMe) {
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            contentLayout.setBackgroundResource(R.drawable.shape_blue_except_top_right_radius_14dp);
            contentTv.setTextColor(IMManager.TEXT_COLOR_1);

            params = (RelativeLayout.LayoutParams) msgStatusLayout.getLayoutParams();
            params.addRule(RelativeLayout.LEFT_OF, contentLayout.getId());
            params.rightMargin = DensityUtil.dp2px(8);
        } else {
            msgStatusIv.setVisibility(View.GONE);

            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            contentLayout.setBackgroundResource(R.drawable.shape_white_except_top_left_radius_14dp);
            contentTv.setTextColor(IMManager.TEXT_COLOR_2);

            params = (RelativeLayout.LayoutParams) msgStatusLayout.getLayoutParams();
            params.addRule(RelativeLayout.RIGHT_OF, contentLayout.getId());
            params.leftMargin = DensityUtil.dp2px(8);
        }
    }
}
