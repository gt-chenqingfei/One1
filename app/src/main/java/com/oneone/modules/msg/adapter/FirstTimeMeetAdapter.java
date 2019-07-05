package com.oneone.modules.msg.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.oneone.R;
import com.oneone.framework.ui.BaseRecyclerViewAdapter;
import com.oneone.framework.ui.BaseViewHolder;
import com.oneone.framework.ui.utils.ScreenUtil;
import com.oneone.modules.msg.IMManager;
import com.oneone.modules.msg.activity.ImTalkActivity;
import com.oneone.modules.msg.beans.IMFirstRelation;
import com.oneone.modules.msg.beans.IMUserPrerelation;
import com.oneone.modules.msg.beans.TalkBeans.MyRecentContact;
import com.oneone.modules.msg.beans.TalkBeans.attachment.EmojiAttachment;
import com.oneone.modules.msg.beans.TalkBeans.attachment.GiftAttachment;
import com.oneone.modules.msg.widget.UserRelationPhotoLayout;
import com.oneone.modules.user.HereUser;
import com.oneone.utils.TimeUtil;

import butterknife.BindView;

/**
 * Created by here on 18/5/15.
 */

public class FirstTimeMeetAdapter extends BaseRecyclerViewAdapter<IMFirstRelation> {
    private Context context;

    private FirstTimeMeetAdapterListener myLis;

    public abstract static class FirstTimeMeetAdapterListener implements BaseViewHolder.ItemClickListener {
        public void onDeleteClick (MyRecentContact myRecentContact) {

        }
    }

    public FirstTimeMeetAdapter(FirstTimeMeetAdapter.FirstTimeMeetAdapterListener listener, Context context) {
        super(listener);
        this.context = context;
        this.myLis = listener;
    }

    @NonNull
    @Override
    public BaseViewHolder<IMFirstRelation> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewGroup = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_first_time_meet, parent, false);
        return new FirstTimeMeetAdapter.FirstTimeMeetingViewHolder(viewGroup);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    class FirstTimeMeetingViewHolder extends BaseViewHolder<IMFirstRelation> implements View.OnClickListener {
        @BindView(R.id.root_layout)
        RelativeLayout rootLayout;
        @BindView(R.id.user_relation_photo_layout)
        RelativeLayout userRelationPhotoLayout;
        @BindView(R.id.user_name_tv)
        TextView userNameTv;
        @BindView(R.id.message_content_tv)
        TextView msgContentTv;
        @BindView(R.id.new_point_view)
        View newPointView;
        @BindView(R.id.msg_time_tv)
        TextView msgTimeTv;
        @BindView(R.id.unread_count_tv)
        TextView unReadCountTv;

        @BindView(R.id.delete_btn_layout)
        RelativeLayout deleteBtnLayout;

        private MyRecentContact myRecentContact;

        protected FirstTimeMeetingViewHolder(View v) {
            super(v);
        }

        @Override
        public void bind(IMFirstRelation imFirstRelation, int position) {
            super.bind(imFirstRelation, position);

            if (imFirstRelation == null) {
                return;
            }

            if (IMManager.myRecentContactList != null) {
                for (MyRecentContact myContact : IMManager.myRecentContactList) {
                    if (myContact.getMsgMetaDto() == null) {
                        if (myContact.getMyTargetId().equals(imFirstRelation.getOtherUserImUid())) {
                            myRecentContact = myContact;
                            break;
                        }
                    }
                }
            }

            if (myRecentContact == null) {
                myRecentContact = new MyRecentContact();
                myRecentContact.setFirstRelation(imFirstRelation);
            }

            createItem(this, myRecentContact);

            rootLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(final View v) {
            switch (v.getId()) {
                case R.id.root_layout:
                    IMManager.clearMsgCount(myRecentContact.getFirstRelation().getOtherUserImUid(), SessionTypeEnum.P2P);
                    notifyDataSetChanged();
                    IMUserPrerelation imUserPrerelation = new IMUserPrerelation(false, true, myRecentContact);
                    ImTalkActivity.startActivity(context, imUserPrerelation, null);
                    break;
            }
        }
    }

    public void createItem (FirstTimeMeetAdapter.FirstTimeMeetingViewHolder holder, final MyRecentContact myRecentContact) {
        UserRelationPhotoLayout photoLayout = new UserRelationPhotoLayout(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) (74 * ScreenUtil.WIDTH_RATIO), (int) (42 * ScreenUtil.WIDTH_RATIO));
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        photoLayout.setLayoutParams(params);
        holder.userRelationPhotoLayout.addView(photoLayout);

        if (myRecentContact.getFirstRelation().getRelationType() == 2) {
            photoLayout.setLayout(HereUser.getInstance().getUserInfo(), myRecentContact.getFirstRelation().getUserInfo(), UserRelationPhotoLayout.LIKE_EACHOTHER);
        } else {
            photoLayout.setLayout(myRecentContact.getFirstRelation().getUserInfo(), myRecentContact.getFirstRelation().getGiftInfoObj().getImage(), UserRelationPhotoLayout.OTHER);
        }

        holder.userNameTv.setText(myRecentContact.getFirstRelation().getUserInfo().getNickname());
        if (myRecentContact.getRecentContact() != null) {
                if (myRecentContact.getRecentContact().getAttachment() != null && myRecentContact.getRecentContact().getAttachment() instanceof GiftAttachment) {
                    GiftAttachment attachment = (GiftAttachment) myRecentContact.getRecentContact().getAttachment();
                    holder.msgContentTv.setText(attachment.getGift().getMessage());
                } else if (myRecentContact.getRecentContact().getAttachment() != null && myRecentContact.getRecentContact().getAttachment() instanceof EmojiAttachment) {
                    EmojiAttachment emojiAttachment = (EmojiAttachment) myRecentContact.getRecentContact().getAttachment();
                    holder.msgContentTv.setText("[" + emojiAttachment.getImEmoji().getMessage() + "]");
                } else {
                    holder.msgContentTv.setText(myRecentContact.getRecentContact().getContent());
                }

                int unread = myRecentContact.getRecentContact().getUnreadCount();
                if (unread > 0) {
                    holder.unReadCountTv.setVisibility(View.VISIBLE);
                    if (unread > 99)
                        holder.unReadCountTv.setText("99+");
                    else
                        holder.unReadCountTv.setText(unread + "");
                } else {
                    holder.unReadCountTv.setVisibility(View.GONE);
                }

                holder.msgTimeTv.setText(TimeUtil.getContactTime(myRecentContact.getRecentContact().getTime(), context));
        } else {
            if (myRecentContact.getFirstRelation() != null && myRecentContact.getFirstRelation().getGiftInfoObj() != null)
                holder.msgContentTv.setText(myRecentContact.getFirstRelation().getGiftInfoObj().getMessage());
            else
                holder.msgContentTv.setText("");

            holder.unReadCountTv.setVisibility(View.GONE);

            holder.msgTimeTv.setText(TimeUtil.getContactTime(myRecentContact.getFirstRelation().getApplyTime(), context));
        }

        holder.deleteBtnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myLis.onDeleteClick(myRecentContact);
            }
        });
    }
}