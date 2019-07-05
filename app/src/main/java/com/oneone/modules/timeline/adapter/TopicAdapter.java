package com.oneone.modules.timeline.adapter;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.oneone.R;
import com.oneone.framework.ui.BaseRecyclerViewAdapter;
import com.oneone.framework.ui.BaseViewHolder;
import com.oneone.modules.timeline.adapter.holder.TimeLineHolder4Moment;
import com.oneone.modules.timeline.adapter.holder.TimeLineHolder4NewSingle;
import com.oneone.modules.timeline.bean.TimeLine;
import com.oneone.modules.timeline.bean.TimeLineDetail;
import com.oneone.modules.timeline.bean.TimeLineTopic;
import com.oneone.modules.timeline.contract.TimeLineContract;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.util.List;

import butterknife.BindView;

public class TopicAdapter extends BaseRecyclerViewAdapter<TimeLine> {

    private static final int TYPE_TOPIC_HEAD = 1;
    private static final int TYPE_NEW_MOMENT = 2;
    private static final int TYPE_NEW_SINGLE = 3;
    private LayoutInflater mLayoutInflater;
    private TimeLineTopic mOperationTagInfo;
    private String mTopicName;
    TimeLineContract.Presenter presenter;
    private boolean hasSendFailedData;

    public TopicAdapter(BaseViewHolder.ItemClickListener<TimeLine> listener,
                        TimeLineTopic operationTagInfo, String topicName, TimeLineContract.Presenter presenter) {
        super(listener);
        this.mOperationTagInfo = operationTagInfo;
        this.mTopicName = topicName;
        this.presenter = presenter;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return TYPE_TOPIC_HEAD;
        TimeLine timeLine = getItem(position);
        if (timeLine.getDetail() != null) {
            if (TimeLineDetail.TYPE_MOMENT.equals(timeLine.getDetail().getType())) {
                return TYPE_NEW_MOMENT;
            } else if (TimeLineDetail.TYPE_NEW_SINGLE.equals(timeLine.getDetail().getType())) {
                return TYPE_NEW_SINGLE;
            }
        }
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public BaseViewHolder<TimeLine> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mLayoutInflater = LayoutInflater.from(parent.getContext());
        View viewGroup;
        switch (viewType) {
            case TYPE_TOPIC_HEAD:
                viewGroup = mLayoutInflater.inflate(R.layout.item_topic_header, parent, false);
                return new HeaderViewHolder(viewGroup, mListener);
            case TYPE_NEW_MOMENT:
                viewGroup = mLayoutInflater.inflate(R.layout.item_timeline_moment_wrapper, parent, false);
                return new TimeLineHolder4Moment(viewGroup, mListener, presenter);
            case TYPE_NEW_SINGLE:
                viewGroup = mLayoutInflater.inflate(R.layout.item_timeline_new_single_wrapper, parent, false);
                return new TimeLineHolder4NewSingle(viewGroup, mListener, presenter);
        }
        return null;
    }

    class HeaderViewHolder extends BaseViewHolder<TimeLine> implements View.OnClickListener {
        @BindView(R.id.ll_resent)
        LinearLayout linearLayoutResent;
        @BindView(R.id.item_topic_header)
        RelativeLayout relativeLayoutHeader;
        @BindView(R.id.item_topic_header_rl_text)
        RelativeLayout relativeLayoutText;
        @BindView(R.id.item_topic_header_topic_resend)
        TextView topicHeaderTopicResend;
        @BindView(R.id.item_topic_header_topic)
        TextView topicHeaderTopic;
        @BindView(R.id.item_topic_header_title)
        TextView topicHeaderTitle;
        @BindView(R.id.item_topic_header_pic)
        ImageView topicHeaderPic;

        public HeaderViewHolder(View view, ItemClickListener<TimeLine> listener) {
            super(view, listener);
            relativeLayoutText.setOnClickListener(this);
            topicHeaderTopicResend.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener == null) return;
            mListener.onItemClick(null, v.getId(), getAdapterPosition());
        }

        @Override
        public void bind(TimeLine timeLine, int position) {
            super.bind(timeLine, position);
            if (mOperationTagInfo != null && !TextUtils.isEmpty(mOperationTagInfo.getBgImgUrl())) {
                relativeLayoutText.setVisibility(View.GONE);
                topicHeaderPic.setVisibility(View.VISIBLE);
                Glide.with(getContext()).load(mOperationTagInfo.getBgImgUrl()).into(topicHeaderPic);
            } else {
                relativeLayoutText.setVisibility(View.VISIBLE);
                topicHeaderPic.setVisibility(View.GONE);
                topicHeaderTitle.setText(mTopicName);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) relativeLayoutText.getLayoutParams();
                if (mTopicName.length() > 23) {
                    layoutParams.height = DensityUtil.dp2px(136);
                    relativeLayoutText.setLayoutParams(layoutParams);
                } else {
                    layoutParams.height = DensityUtil.dp2px(106);
                    relativeLayoutText.setLayoutParams(layoutParams);
                }
            }
            if (hasSendFailedData) {
                relativeLayoutHeader.setBackgroundResource(R.color.color_F5ABA1);
                linearLayoutResent.setVisibility(View.VISIBLE);
                topicHeaderTopic.setText(getContext().getResources().getString(R.string.str_timeline_send_error_tip));
                topicHeaderTopic.setTextSize(13);
            } else {
                relativeLayoutHeader.setBackgroundResource(R.color.color_796CF0);
                linearLayoutResent.setVisibility(View.GONE);
                topicHeaderTopic.setText(getContext().getResources().getString(R.string.timeline_topic_name));
                topicHeaderTopic.setTextSize(20);
            }
        }
    }

    public TimeLine findItemById(long timelineId) {
        List<TimeLine> timeLines = getList();
        if (timeLines == null || timeLines.isEmpty()) {
            return null;
        }
        for (TimeLine timeLine : timeLines) {
            if (timeLine.getTimelineId() == timelineId) {
                return timeLine;
            }
        }
        return null;
    }

    public void setHasSendFailedData(boolean hasSendFailedData) {
        this.hasSendFailedData = !hasSendFailedData;
    }

    public void setmOperationTagInfo(TimeLineTopic mOperationTagInfo) {
        this.mOperationTagInfo = mOperationTagInfo;
    }
}
