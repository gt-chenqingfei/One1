package com.oneone.modules.msg.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.oneone.R;
import com.oneone.framework.ui.glide.GlideUtils;
import com.oneone.modules.msg.IMManager;
import com.oneone.modules.msg.beans.GiftProd;
import com.oneone.modules.msg.beans.TalkBeans.GiftMessage;
import com.oneone.modules.user.HereUser;
import com.oneone.utils.ImageHelper;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.messages.BaseMessageViewHolder;
import cn.jiguang.imui.messages.MessageListStyle;
import cn.jiguang.imui.messages.MsgListAdapter;

/**
 * Created by here on 18/5/12.
 */

public class MyImGiftViewHolder extends BaseMessageViewHolder<GiftMessage> implements MsgListAdapter.DefaultMessageViewHolder {
    private boolean isMe;
    private RelativeLayout contentLayout;
    private TextView contentTv;
    private ImageView giftIv;
    private RelativeLayout msgStatusLayout;
    private ImageView msgStatusIv;

    public MyImGiftViewHolder(View itemView, boolean isMe) {
        super(itemView);
        this.isMe = isMe;

        contentLayout = itemView.findViewById(R.id.content_layout);
        contentTv = itemView.findViewById(R.id.content_tv);
        giftIv = itemView.findViewById(R.id.gift_iv);

        msgStatusLayout = itemView.findViewById(R.id.im_msg_status_layout);
        msgStatusIv = itemView.findViewById(R.id.im_msg_status_failed_iv);
    }

    @Override
    public void onBind(final GiftMessage giftMessage) {
        if (giftMessage.getMessageStatus() == IMessage.MessageStatus.SEND_FAILED) {
            msgStatusIv.setVisibility(View.VISIBLE);
            msgStatusIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (IMManager.curToImUid != null)
                        IMManager.getInstance().reSendGiftMsg(IMManager.curToImUid, giftMessage);
                }
            });
        } else {
            msgStatusIv.setVisibility(View.GONE);
        }

        System.out.println("MyImGiftViewHolder====>" + giftMessage.getGiftProd().getMessage());
        GiftProd giftProd = giftMessage.getGiftProd();

//        if (curRecentContact.getRecentContact().getFromAccount().equals(HereUser.getInstance().getImUserInfo().getImUserId()))
        if (isMe)
            contentTv.setText(mContext.getResources().getString(R.string.im_send_gift_front_word) + giftProd.getMessage());
        else
            contentTv.setText(mContext.getResources().getString(R.string.im_receive_gift_front_word) + giftProd.getMessage());

        ImageHelper.displayImage(mContext, giftIv, giftProd.getImage());
//        GlideUtils.loadCircleImage(giftProd.getImage(), giftIv, new GlideUtils.ImageLoadListener<String, GlideDrawable>() {
//            @Override
//            public void onLoadingComplete(String uri, ImageView view, GlideDrawable resource) {
//
//            }
//
//            @Override
//            public void onLoadingError(String source, Exception e) {
//
//            }
//        });
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
