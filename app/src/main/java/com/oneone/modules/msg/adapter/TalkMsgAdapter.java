package com.oneone.modules.msg.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.oneone.R;
import com.oneone.api.constants.RedDot;
import com.oneone.api.constants.Role;
import com.oneone.modules.msg.IMManager;
import com.oneone.modules.msg.activity.ClientAndSystemMsgActivity;
import com.oneone.modules.msg.activity.ImTalkActivity;
import com.oneone.modules.msg.beans.IMUserPrerelation;
import com.oneone.modules.msg.beans.TalkBeans.MyRecentContact;
import com.oneone.modules.msg.beans.TalkBeans.attachment.EmojiAttachment;
import com.oneone.modules.msg.beans.TalkBeans.attachment.GiftAttachment;
import com.oneone.modules.reddot.RedDotManager;
import com.oneone.modules.user.HereUser;
import com.oneone.utils.TimeUtil;
import com.oneone.widget.AvatarImageView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by here on 18/4/28.
 */

public class TalkMsgAdapter extends BaseAdapter {
    private HashMap<Integer, View> mapView = new HashMap<Integer, View>();
//    private List<MyRecentContact> recentContactList;

    private LinearLayout layout;

    private Activity context;
    private LayoutInflater inflater;

    public interface TalkMsgAdapterListener {
        void onContactDelete(MyRecentContact myRecentContact);
        void onSystenMsgDelete();
    }

    private TalkMsgAdapterListener listener;

    private float startX;
    private float endX;

    public TalkMsgAdapter (Activity context, LinearLayout layout, TalkMsgAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.layout = layout;
        inflater = LayoutInflater.from(context);

        resetView ();
    }

    public void resetView () {
        Collections.sort(IMManager.myRecentContactList);
        layout.removeAllViews();
        int pos = 0;
        for (MyRecentContact recentContact : IMManager.myRecentContactList) {
            if (!IMManager.checkInFirstRelation(recentContact)) {
                ViewHolder holder = new ViewHolder();
                holder.recentContact = recentContact;
                View convertView;
                if (holder.recentContact.getMsgMetaDto() != null) {
                    convertView = inflater.inflate(R.layout.item_msg_page_talk_no_delete, null);
                    convertView.setTag(holder);

                    holder.userPhotoIv = convertView.findViewById(R.id.user_photo_iv);
                    holder.msgTimeTv = convertView.findViewById(R.id.msg_time_tv);
                    holder.userNameTv = convertView.findViewById(R.id.user_name_tv);
                    holder.userMatcherTagIv = convertView.findViewById(R.id.user_matcher_tag_iv);
                    holder.talkMsgContentTv = convertView.findViewById(R.id.talk_msg_content_tv);
                    holder.unreadCountTv = convertView.findViewById(R.id.unread_count_tv);

//            holder.recentContact.
//            holder.userPhotoIv.ini


                    setSystemMsgContent(holder);
                } else {
                    convertView = inflater.inflate(R.layout.item_msg_page_talk, null);
                    convertView.setTag(holder);
//            mapView.put(pos, convertView);
                    holder.deleteBtnLayout = convertView.findViewById(R.id.delete_btn_layout);
                    holder.userPhotoIv = convertView.findViewById(R.id.user_photo_iv);
                    holder.msgTimeTv = convertView.findViewById(R.id.msg_time_tv);
                    holder.userNameTv = convertView.findViewById(R.id.user_name_tv);
                    holder.userMatcherTagIv = convertView.findViewById(R.id.user_matcher_tag_iv);
                    holder.talkMsgContentTv = convertView.findViewById(R.id.talk_msg_content_tv);
                    holder.unreadCountTv = convertView.findViewById(R.id.unread_count_tv);


                    setMsgContent(holder);
                }

                convertView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        switch (motionEvent.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                startX = motionEvent.getX();
                                break;
                            case MotionEvent.ACTION_UP:
                                endX = motionEvent.getX();
                                break;
                        }
                        return false;
                    }
                });
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Math.abs(startX - endX) < 30) {
                            TalkMsgAdapter.ViewHolder holder = (TalkMsgAdapter.ViewHolder) view.getTag();
                            if (holder.recentContact.getMsgMetaDto() == null) {
//                        adapter.modifyItem(holder.recentContact);
                                IMUserPrerelation imUserPrerelation = new IMUserPrerelation(false, true, holder.recentContact);
                                ImTalkActivity.startActivity(context, imUserPrerelation, null);
                            } else {
                                holder.recentContact.getMsgMetaDto().setUnread(0);
//                        adapter.modifyItem(holder.recentContact);
                                ClientAndSystemMsgActivity.startActivity(context);
                            }
                        }
                    }
                });

                layout.addView(convertView);
                mapView.put(pos, convertView);
                pos ++;
            }
        }

        if (layout.getChildCount() <= 0) {
            layout.setVisibility(View.GONE);
        } else {
            layout.setVisibility(View.VISIBLE);
        }
    }

    public void clearMap () {
        mapView.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (IMManager.myRecentContactList != null)
            return IMManager.myRecentContactList.size();
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (IMManager.myRecentContactList != null)
            return IMManager.myRecentContactList.get(i);
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void modifyItem (MyRecentContact myRecentContact) {
        if (myRecentContact.getMsgMetaDto() != null) {
            boolean isModify = false;
            Set<Integer> keys = mapView.keySet();
            for (int key : keys) {
                View convertView = mapView.get(key);
                ViewHolder holder = (ViewHolder) convertView.getTag();
                MyRecentContact recentContactTag = holder.recentContact;
                if (recentContactTag.getMsgMetaDto() == null)
                    continue ;
//                if (recentContactTag.getMsgMetaDto().getMsgMeta().getUserId() == null) {
//                    continue ;
//                } else {
                    if (myRecentContact.getMsgMetaDto().getMsgMeta().getUserId().equals(recentContactTag.getMsgMetaDto().getMsgMeta().getUserId())) {
                        isModify = true;
                        holder.recentContact = myRecentContact;
                        setSystemMsgContent(holder);
                        break;
                    }
//                }
            }
//
            if (!isModify) {
                if (!IMManager.myRecentContactList.contains(myRecentContact))
                    IMManager.myRecentContactList.add(myRecentContact);

            }
        } else {
//            boolean isModify = false;
            Set<Integer> keys = mapView.keySet();
            for (int key : keys) {
                View convertView = mapView.get(key);
                ViewHolder holder = (ViewHolder) convertView.getTag();
                MyRecentContact recentContactTag = holder.recentContact;
                if (recentContactTag.getRecentContact() != null)
                    System.out.println("UUUUUUUU:" +recentContactTag.getRecentContact().getContactId());
                System.out.println("KUUUUUUUU:" +myRecentContact.getRecentContact().getContactId());

                if (recentContactTag.getRecentContact() != null && recentContactTag.getRecentContact().getContactId().equals(myRecentContact.getRecentContact().getContactId())) {
//                    isModify = true;
                    System.out.println("JUUUUUUUUU:" + myRecentContact.getRecentContact().getContent());
                    holder.recentContact = myRecentContact;
                    setMsgContent(holder);

                    break;
                }
            }
//            if (!isModify)
//                IMManager.myRecentContactList.add(myRecentContact);
        }
        Collections.sort(IMManager.myRecentContactList);
        mapView.clear();
//        notifyDataSetChanged();
        resetView();
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup viewGroup) {
        convertView = mapView.get(pos);
        if (convertView == null) {
            ViewHolder holder = new ViewHolder();
            holder.recentContact = IMManager.myRecentContactList.get(pos);

            if (holder.recentContact.getMsgMetaDto() != null) {
                convertView = inflater.inflate(R.layout.item_msg_page_talk, null);
                convertView.setTag(holder);
                mapView.put(pos, convertView);
                holder.userPhotoIv = convertView.findViewById(R.id.user_photo_iv);
                holder.msgTimeTv = convertView.findViewById(R.id.msg_time_tv);
                holder.userNameTv = convertView.findViewById(R.id.user_name_tv);
                holder.userMatcherTagIv = convertView.findViewById(R.id.user_matcher_tag_iv);
                holder.talkMsgContentTv = convertView.findViewById(R.id.talk_msg_content_tv);
                holder.unreadCountTv = convertView.findViewById(R.id.unread_count_tv);

                setSystemMsgContent(holder);
            } else {
                convertView = inflater.inflate(R.layout.item_msg_page_talk_no_delete, null);
                convertView.setTag(holder);
                mapView.put(pos, convertView);
                holder.deleteBtnLayout = convertView.findViewById(R.id.delete_btn_layout);
                holder.userPhotoIv = convertView.findViewById(R.id.user_photo_iv);
                holder.msgTimeTv = convertView.findViewById(R.id.msg_time_tv);
                holder.userNameTv = convertView.findViewById(R.id.user_name_tv);
                holder.userMatcherTagIv = convertView.findViewById(R.id.user_matcher_tag_iv);
                holder.talkMsgContentTv = convertView.findViewById(R.id.talk_msg_content_tv);
                holder.unreadCountTv = convertView.findViewById(R.id.unread_count_tv);

                setMsgContent(holder);
            }

//            convertView = inflater.inflate(R.layout.item_msg_page_talk, null);
//            convertView.setTag(holder);
//            mapView.put(pos, convertView);
//            holder.deleteBtnLayout = convertView.findViewById(R.id.delete_btn_layout);
//            holder.userPhotoIv = convertView.findViewById(R.id.user_photo_iv);
//            holder.msgTimeTv = convertView.findViewById(R.id.msg_time_tv);
//            holder.userNameTv = convertView.findViewById(R.id.user_name_tv);
//            holder.userMatcherTagIv = convertView.findViewById(R.id.user_matcher_tag_iv);
//            holder.talkMsgContentTv = convertView.findViewById(R.id.talk_msg_content_tv);
//            holder.unreadCountTv = convertView.findViewById(R.id.unread_count_tv);

//            holder.recentContact.
//            holder.userPhotoIv.ini

//            if (holder.recentContact.getMsgMetaDto() != null) {
//                setSystemMsgContent(holder);
//            } else {
//                setMsgContent(holder);
//            }



        }
        return convertView;
    }

    public void setSystemMsgContent (ViewHolder holder) {
        holder.userPhotoIv.setBackgroundResource(R.drawable.sys_msg_photo_bg);
//        holder.userNameTv.setText(holder.recentContact.getMsgMetaDto().getMsgMeta().getMetaTitle());
        holder.userNameTv.setText(context.getString(R.string.str_display_name_oneone));
        holder.userMatcherTagIv.setVisibility(View.GONE);

        int unread = holder.recentContact.getMsgMetaDto().getUnread();
        if (unread > 0) {
            holder.unreadCountTv.setVisibility(View.VISIBLE);
            if (unread > 99)
                holder.unreadCountTv.setText("99+");
            else
                holder.unreadCountTv.setText(unread + "");
        } else {
            holder.unreadCountTv.setVisibility(View.GONE);
        }

        if (holder.recentContact.getMsgMetaDto().getMsgMeta().getTimestamp() != null && holder.recentContact.getMsgMetaDto().getMsgMeta().getTimestamp() > 0) {
            holder.msgTimeTv.setVisibility(View.VISIBLE);
            holder.msgTimeTv.setText(TimeUtil.getContactTime(holder.recentContact.getMsgMetaDto().getMsgMeta().getTimestamp(), context));
            holder.talkMsgContentTv.setText(holder.recentContact.getMsgMetaDto().getMsgMeta().getMetaValue());
        } else {
            holder.msgTimeTv.setVisibility(View.GONE);
        }

//        holder.deleteBtnLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                System.out.println("delete sys contact item!!!!!!");
//                listener.onSystenMsgDelete();
//            }
//        });
    }

    public void setMsgContent (final ViewHolder holder) {
        if (holder.recentContact.getFirstRelation() != null) {
            holder.userPhotoIv.init(holder.recentContact.getFirstRelation().getUserInfo(), false);
            holder.userNameTv.setText(holder.recentContact.getFirstRelation().getUserInfo().getNickname());
            if (holder.recentContact.getFirstRelation().getUserInfo().getRole() == Role.MATCHER) {
                holder.userMatcherTagIv.setVisibility(View.VISIBLE);
            } else {
                holder.userMatcherTagIv.setVisibility(View.GONE);
            }
        }

        if (holder.recentContact.getRecentContact() != null) {
            System.out.println("UUUUU");
            System.out.println(holder.recentContact.getRecentContact().getAttachment());
            System.out.println("UUUUU");
            int unread = holder.recentContact.getRecentContact().getUnreadCount();
            if (unread > 0) {
                holder.unreadCountTv.setVisibility(View.VISIBLE);
                if (unread > 99)
                    holder.unreadCountTv.setText("99+");
                else
                    holder.unreadCountTv.setText(unread + "");
            } else {
                holder.unreadCountTv.setVisibility(View.GONE);
            }

            holder.msgTimeTv.setText(TimeUtil.getContactTime(holder.recentContact.getRecentContact().getTime(), context));
            if (holder.recentContact != null && holder.recentContact.getRecentContact() != null) {
                if (holder.recentContact.getRecentContact().getAttachment() instanceof GiftAttachment) {
                    String textStr = "";
                    if (holder.recentContact.getRecentContact().getFromAccount().equals(HereUser.getInstance().getImUserInfo().getImUserId()))
                        textStr += context.getResources().getString(R.string.im_send_gift_front_word);
                    else
                        textStr += context.getResources().getString(R.string.im_receive_gift_front_word);
//                    holder.talkMsgContentTv.setText(holder.recentContact.getFirstRelation().getGiftInfoObj().getMessage());

//                    if (holder.recentContact.getFirstRelation() != null) {
//                        textStr += holder.recentContact.getFirstRelation().getGiftInfoObj().getMessage();
//                    }
                    GiftAttachment attachment = (GiftAttachment) holder.recentContact.getRecentContact().getAttachment();
                    textStr += "[" + attachment.getGift().getMessage() + "]";
                    holder.talkMsgContentTv.setText(textStr);
                } else if (holder.recentContact.getRecentContact().getAttachment() instanceof EmojiAttachment) {
                    EmojiAttachment emojiAttachment = (EmojiAttachment) holder.recentContact.getRecentContact().getAttachment();
                    holder.talkMsgContentTv.setText("[" + emojiAttachment.getImEmoji().getMessage() + "]");
                } else {
                    holder.talkMsgContentTv.setText(holder.recentContact.getRecentContact().getContent());
                }
            } else {
                holder.talkMsgContentTv.setText("");
            }
//            holder.talkMsgContentTv.setText(holder.recentContact.getRecentContact().getContent());
        } else {
            holder.unreadCountTv.setVisibility(View.GONE);
            holder.msgTimeTv.setText("");
            holder.talkMsgContentTv.setText("");
        }

        if (holder.deleteBtnLayout != null) {
            holder.deleteBtnLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("delete contact item!!!!!!");
                    listener.onContactDelete(holder.recentContact);
                }
            });
        }
    }

    public class ViewHolder {
        public MyRecentContact recentContact;

        RelativeLayout deleteBtnLayout;
        AvatarImageView userPhotoIv;
        TextView msgTimeTv;
        TextView userNameTv;
        ImageView userMatcherTagIv;
        TextView talkMsgContentTv;
        TextView unreadCountTv;

    }
}
