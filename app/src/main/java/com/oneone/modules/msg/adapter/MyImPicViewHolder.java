package com.oneone.modules.msg.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.oneone.R;
import com.oneone.framework.ui.IPreViewMenuListener;
import com.oneone.framework.ui.imagepicker.preview.PhotoBrowserPagerActivity;
import com.oneone.modules.msg.beans.TalkBeans.PicMessage;
import com.oneone.utils.ImageHelper;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.util.ArrayList;

import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.messages.BaseMessageViewHolder;
import cn.jiguang.imui.messages.MessageListStyle;
import cn.jiguang.imui.messages.MsgListAdapter;

/**
 * Created by here on 18/6/12.
 */

public class MyImPicViewHolder extends BaseMessageViewHolder<PicMessage> implements MsgListAdapter.DefaultMessageViewHolder {
    private boolean isMe;

    private RelativeLayout contentLayout;
    private ImageView picIv;
    private RelativeLayout msgStatusLayout;
    private ImageView msgStatusIv;

    public MyImPicViewHolder(View itemView, boolean isMe) {
        super(itemView);
        this.isMe = isMe;
        picIv = itemView.findViewById(R.id.content_iv);
        contentLayout = itemView.findViewById(R.id.content_layout);
        msgStatusLayout = itemView.findViewById(R.id.im_msg_status_layout);
        msgStatusIv = itemView.findViewById(R.id.im_msg_status_failed_iv);
    }

    @Override
    public void onBind(PicMessage picMessage) {
        if (picMessage.getMessageStatus() == IMessage.MessageStatus.SEND_FAILED) {
            msgStatusIv.setVisibility(View.VISIBLE);
        } else {
            msgStatusIv.setVisibility(View.GONE);
        }


        ImageHelper.displayImage(mContext, picIv, picMessage.getFilePath());

        picIv.setTag(picMessage);
        picIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PicMessage picMessageTag = (PicMessage) view.getTag();
                ArrayList<String> thumbnailPaths = new ArrayList<>();
                ArrayList<String> normalPaths = new ArrayList<>();

                normalPaths.add(picMessageTag.getFilePath());
                thumbnailPaths.add(picMessageTag.getFilePath());

                PhotoBrowserPagerActivity.launch(mContext, thumbnailPaths, normalPaths, 0,picIv);
            }
        });
    }

    @Override
    public void applyStyle(MessageListStyle style) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) contentLayout.getLayoutParams();
        if (isMe) {
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            contentLayout.setBackgroundResource(R.drawable.shape_blue_except_top_right_radius_14dp);

            params = (RelativeLayout.LayoutParams) msgStatusLayout.getLayoutParams();
            params.addRule(RelativeLayout.LEFT_OF, contentLayout.getId());
            params.rightMargin = DensityUtil.dp2px(8);
        } else {
            msgStatusIv.setVisibility(View.GONE);

            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            contentLayout.setBackgroundResource(R.drawable.shape_white_except_top_left_radius_14dp);

            params = (RelativeLayout.LayoutParams) msgStatusLayout.getLayoutParams();
            params.addRule(RelativeLayout.RIGHT_OF, contentLayout.getId());
            params.leftMargin = DensityUtil.dp2px(8);
        }
    }
}
