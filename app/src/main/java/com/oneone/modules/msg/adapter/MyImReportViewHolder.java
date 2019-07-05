package com.oneone.modules.msg.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oneone.Constants;
import com.oneone.R;
import com.oneone.framework.ui.IPreViewMenuListener;
import com.oneone.framework.ui.imagepicker.preview.PhotoBrowserPagerActivity;
import com.oneone.modules.feedback.adapter.FeedbackItemPicAdapter;
import com.oneone.modules.feedback.beans.FeedbackListItem;
import com.oneone.modules.msg.IMManager;
import com.oneone.modules.msg.beans.TalkBeans.ReportMessage;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.messages.BaseMessageViewHolder;
import cn.jiguang.imui.messages.MessageListStyle;
import cn.jiguang.imui.messages.MsgListAdapter;

/**
 * Created by here on 18/6/12.
 */

public class MyImReportViewHolder extends BaseMessageViewHolder<ReportMessage> implements MsgListAdapter.DefaultMessageViewHolder {
    private boolean isMe;

    private RelativeLayout contentLayout;
    private TextView reportTv;
    private GridView picGv;
    private RelativeLayout msgStatusLayout;
    private ImageView msgStatusIv;

    public MyImReportViewHolder(View itemView, boolean isMe) {
        super(itemView);
        this.isMe = isMe;
        reportTv = itemView.findViewById(R.id.content_tv);
        picGv = itemView.findViewById(R.id.content_pic_gv);
        contentLayout = itemView.findViewById(R.id.content_layout);
        msgStatusLayout = itemView.findViewById(R.id.im_msg_status_layout);
        msgStatusIv = itemView.findViewById(R.id.im_msg_status_failed_iv);
    }

    @Override
    public void onBind(ReportMessage reportMessage) {
        final FeedbackListItem feedbackListItem = reportMessage.getFeedbackListItem();

        if (reportMessage.getMessageStatus() == IMessage.MessageStatus.SEND_FAILED) {
            msgStatusIv.setVisibility(View.VISIBLE);
        } else {
            msgStatusIv.setVisibility(View.GONE);
        }

        reportTv.setText(feedbackListItem.getFeedback());
        FeedbackItemPicAdapter adapter = new FeedbackItemPicAdapter(mContext, feedbackListItem.getImgList());
        picGv.setAdapter(adapter);

        picGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                FeedbackItemPicAdapter.ViewHolder holder = (FeedbackItemPicAdapter.ViewHolder) view.getTag();

                ArrayList<String> thumbnailPaths = new ArrayList<>();
                ArrayList<String> normalPaths = new ArrayList<>();

                List<String> imgList = feedbackListItem.getImgList();
                for (int i = 0; i < imgList.size(); i++) {
                    normalPaths.add(Constants.URL.QINIU_BASE_URL() + imgList.get(i));
                    thumbnailPaths.add(Constants.URL.QINIU_BASE_URL() + imgList.get(i));
                }

                PhotoBrowserPagerActivity.launch(mContext, thumbnailPaths, normalPaths, pos,holder.picIv);
            }
        });
    }

    @Override
    public void applyStyle(MessageListStyle style) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) contentLayout.getLayoutParams();
        if (isMe) {
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            contentLayout.setBackgroundResource(R.drawable.shape_blue_except_top_right_radius_14dp);
            reportTv.setTextColor(IMManager.TEXT_COLOR_1);

            params = (RelativeLayout.LayoutParams) msgStatusLayout.getLayoutParams();
            params.addRule(RelativeLayout.LEFT_OF, contentLayout.getId());
            params.rightMargin = DensityUtil.dp2px(8);
        } else {
            msgStatusIv.setVisibility(View.GONE);

            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            contentLayout.setBackgroundResource(R.drawable.shape_white_except_top_left_radius_14dp);
            reportTv.setTextColor(IMManager.TEXT_COLOR_2);

            params = (RelativeLayout.LayoutParams) msgStatusLayout.getLayoutParams();
            params.addRule(RelativeLayout.RIGHT_OF, contentLayout.getId());
            params.leftMargin = DensityUtil.dp2px(8);
        }
    }
}
