package com.oneone.modules.msg.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.oneone.R;
import com.oneone.framework.ui.glide.GlideUtils;
import com.oneone.modules.msg.IMManager;
import com.oneone.modules.msg.beans.TalkBeans.EmojiMessage;
import com.oneone.utils.ImageHelper;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.messages.BaseMessageViewHolder;
import cn.jiguang.imui.messages.MessageListStyle;
import cn.jiguang.imui.messages.MsgListAdapter;

/**
 * Created by here on 18/5/12.
 */

public class MyImEmojiViewHolder extends BaseMessageViewHolder<EmojiMessage> implements MsgListAdapter.DefaultMessageViewHolder {
    private boolean isMe;

    private ImageView emojiIv;

    private RelativeLayout msgStatusLayout;
    private ImageView msgStatusIv;

    public MyImEmojiViewHolder(View itemView, boolean isMe) {
        super(itemView);
        this.isMe = isMe;
        emojiIv = itemView.findViewById(R.id.emoji_iv);

        msgStatusLayout = itemView.findViewById(R.id.im_msg_status_layout);
        msgStatusIv = itemView.findViewById(R.id.im_msg_status_failed_iv);
    }

    @Override
    public void onBind(final EmojiMessage emojiMessage) {
        if (emojiMessage.getMessageStatus() == IMessage.MessageStatus.SEND_FAILED) {
            msgStatusIv.setVisibility(View.VISIBLE);
            msgStatusIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (IMManager.curToImUid != null)
                        IMManager.getInstance().reSendEmoji(IMManager.curToImUid, emojiMessage);
                }
            });
        } else {
            msgStatusIv.setVisibility(View.GONE);
        }

        ImageHelper.displayImage(mContext, emojiIv, emojiMessage.getImEmoji().getImage());
//        GlideUtils.loadCircleImage(emojiMessage.getImEmoji().getImage(), emojiIv, new GlideUtils.ImageLoadListener<String, GlideDrawable>() {
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
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) emojiIv.getLayoutParams();
        if (isMe) {
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            params = (RelativeLayout.LayoutParams) msgStatusLayout.getLayoutParams();
            params.addRule(RelativeLayout.LEFT_OF, emojiIv.getId());
            params.rightMargin = DensityUtil.dp2px(8);
        } else {
            msgStatusIv.setVisibility(View.GONE);

            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            params = (RelativeLayout.LayoutParams) msgStatusLayout.getLayoutParams();
            params.addRule(RelativeLayout.RIGHT_OF, emojiIv.getId());
            params.leftMargin = DensityUtil.dp2px(8);
        }
    }
}
