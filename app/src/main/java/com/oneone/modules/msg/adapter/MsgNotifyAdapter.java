package com.oneone.modules.msg.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oneone.Constants;
import com.oneone.R;
import com.oneone.api.constants.FollowStatus;
import com.oneone.framework.ui.IPreViewMenuListener;
import com.oneone.framework.ui.imagepicker.preview.PhotoBrowserPagerActivity;
import com.oneone.modules.msg.beans.NotifyListItem;
import com.oneone.modules.msg.beans.TimeLineImgs;
import com.oneone.modules.msg.beans.TimeLineInfo;
import com.oneone.utils.ImageHelper;
import com.oneone.utils.MyTextUtil;
import com.oneone.utils.TimeUtil;
import com.oneone.widget.AvatarImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by here on 18/4/28.
 */

public class MsgNotifyAdapter extends BaseAdapter {
    public static final int NOTIFY_TYPE_ACTIVE = 1;
    public static final int NOTIFY_TYPE_ATTENTION = 2;

    private Context context;
    private HashMap<Integer, View> mapView = new HashMap<Integer, View>();
    List<NotifyListItem> notifyListItemList;
    private TextView notifyTitleTv;
    private LinearLayout layout;

    private LayoutInflater inflater;

    private RelativeLayout emptyLayout;

    public interface MsgNotifyAdapterListener {
        void onFollowBtnClick(AttentionViewHolder holder);
    }

    private MsgNotifyAdapterListener listener;

    public void clearMap() {
        mapView.clear();
    }

    public MsgNotifyAdapter(Context context, LinearLayout layout, TextView notifyTitleTv, RelativeLayout emptyLayout, List<NotifyListItem> notifyListItemList, MsgNotifyAdapterListener listener) {
        this.notifyListItemList = notifyListItemList;
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.listener = listener;
        this.layout = layout;
        this.notifyTitleTv = notifyTitleTv;
        this.emptyLayout = emptyLayout;

        System.out.println("=========>" + notifyListItemList.size());
        refreshList();
    }

    public void refreshList() {
        layout.removeAllViews();
        mapView.clear();
        int i = 0;
        for (NotifyListItem notifyListItem : notifyListItemList) {
            createItem(notifyListItem, i);
            i++;
        }

        if (layout.getChildCount() > 0) {
            notifyTitleTv.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.VISIBLE);
        } else {
            notifyTitleTv.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.GONE);
        }
    }

    public void loadMore() {
        int i = 0;
        for (NotifyListItem notifyListItem : notifyListItemList) {
            if (mapView.get(i) == null) {
                createItem(notifyListItem, i);
            }
            i++;
        }

        if (layout.getChildCount() > 0) {
            notifyTitleTv.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.VISIBLE);
        } else {
            notifyTitleTv.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.GONE);
        }
    }

    public void createItem(NotifyListItem notifyListItem, int position) {
        View convertView = null;
        if (notifyListItem.getNotifyType() == NOTIFY_TYPE_ACTIVE) {
            convertView = inflater.inflate(R.layout.item_msg_page_notice_type_interaction, null);
            ActiveViewHolder activeViewHolder = initInteractionView(convertView, notifyListItem);
            convertView.setTag(activeViewHolder);
        } else if (notifyListItem.getNotifyType() == NOTIFY_TYPE_ATTENTION) {
            convertView = inflater.inflate(R.layout.item_msg_page_notice_type_attention, null);
            AttentionViewHolder attentionViewHolder = initAttentionView(convertView, notifyListItem);
            convertView.setTag(attentionViewHolder);
        }

        if (convertView != null) {
            mapView.put(position, convertView);
            layout.addView(convertView);
        }
    }

    @Override
    public int getCount() {
        return notifyListItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return notifyListItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup viewGroup) {
        convertView = mapView.get(pos);
        if (convertView == null) {
            System.out.println("=====>" + pos);
            NotifyListItem notifyListItem = notifyListItemList.get(pos);
            if (notifyListItem.getNotifyType() == NOTIFY_TYPE_ACTIVE) {
                convertView = inflater.inflate(R.layout.item_msg_page_notice_type_interaction, null);
                ActiveViewHolder activeViewHolder = initInteractionView(convertView, notifyListItem);
                convertView.setTag(activeViewHolder);
            } else if (notifyListItem.getNotifyType() == NOTIFY_TYPE_ATTENTION) {
                convertView = inflater.inflate(R.layout.item_msg_page_notice_type_attention, null);
                AttentionViewHolder attentionViewHolder = initAttentionView(convertView, notifyListItem);
                convertView.setTag(attentionViewHolder);
            }
            mapView.put(pos, convertView);
        }
//        else {
//            NotifyListItem notifyListItem = notifyListItemList.get(pos);
//            final List<TimeLineImgs> imgList = notifyListItem.getTimeLineInfo().getDetail().getDetail().getTimelineImgs();
//            if (notifyListItem.getNotifyType() == NOTIFY_TYPE_ACTIVE && imgList != null && imgList.size() > 0) {
//                System.out.println("PPPPPPPPPPPPPPP");
//                ActiveViewHolder holder = (ActiveViewHolder) convertView.getTag();
//                holder.picGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int k, long l) {
//                        System.out.println("CLICK PIC!!!!!!");
//                        NotifyInteractionPicItemAdapter.ViewHolder holder = (NotifyInteractionPicItemAdapter.ViewHolder) view.getTag();
//
//                        int[] location = new int[2];
//                        view.getLocationOnScreen(location);
//
//                        ArrayList<String> thumbnailPaths = new ArrayList<>();
//                        ArrayList<String> normalPaths = new ArrayList<>();
//
//                        for (int i = 0; i < imgList.size(); i++) {
//                            normalPaths.add(Constants.URL.QINIU_BASE_URL + imgList.get(i).getUrl());
//                            thumbnailPaths.add(Constants.URL.QINIU_BASE_URL + imgList.get(i).getUrl());
//                        }
//
//                        PhotoBrowserPagerActivity.launch(context, thumbnailPaths, normalPaths, k, location[0], location[1],
//                                holder.picIv.getWidth(), holder.picIv.getHeight(), new IPreViewMenuListener() {
//                                    @Override
//                                    public void onSaveImageToAlbum(String normalPath) {
//                                    }
//
//                                    @Override
//                                    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//                                    }
//                                });
//                    }
//                });
//            }
//        }
        return convertView;
    }

    public class ActiveViewHolder {
        public ImageView newInteractionIv;
        public AvatarImageView userPhotoIv;
        public TextView userNameTv;
        public TextView userActionTv;
        public TextView timeTv;
        public TextView myInteractionContentTv;
        public GridView picGv;
    }

    private ActiveViewHolder initInteractionView(View convertView, NotifyListItem notifyListItem) {
        ActiveViewHolder holder = new ActiveViewHolder();
        holder.newInteractionIv = convertView.findViewById(R.id.new_interaction_iv);
        holder.userPhotoIv = convertView.findViewById(R.id.user_photo_iv);
        holder.userNameTv = convertView.findViewById(R.id.user_name_tv);
        holder.userActionTv = convertView.findViewById(R.id.user_action_tv);
        holder.timeTv = convertView.findViewById(R.id.time_tv);
        holder.myInteractionContentTv = convertView.findViewById(R.id.my_interaction_content_tv);
        holder.picGv = convertView.findViewById(R.id.content_pic_gv);

        if (notifyListItem.getUnread() == 1) {
            holder.newInteractionIv.setVisibility(View.VISIBLE);
        } else {
            holder.newInteractionIv.setVisibility(View.GONE);
        }

        holder.userPhotoIv.init(notifyListItem.getFromUserInfo(), true);
        ImageHelper.displayImage(context, holder.userPhotoIv, notifyListItem.getFromUserInfo().getAvatar());
        holder.userNameTv.setText(MyTextUtil.getLimitEllipseText(notifyListItem.getFromUserInfo().getNickname(), 5));
        holder.timeTv.setText(TimeUtil.getNotifyListTime(notifyListItem.getNotifyTime()));

        TimeLineInfo timeLineInfo = notifyListItem.getTimeLineInfo();
        if (timeLineInfo != null) {
            if (timeLineInfo.getDetail() != null && timeLineInfo.getDetail().getDetail() != null) {
                holder.myInteractionContentTv.setText(MyTextUtil.getLimitEllipseText(timeLineInfo.getDetail().getDetail().getContent(), 20));
                final List<TimeLineImgs> imgList = timeLineInfo.getDetail().getDetail().getTimelineImgs();
                if (imgList != null && imgList.size() > 0) {
                    System.out.println("--------------->");
                    System.out.println(timeLineInfo.getDetail().getDetail().getContent());
                    System.out.println("--------------->");
                    holder.picGv.setVisibility(View.VISIBLE);
                    NotifyInteractionPicItemAdapter adapter = new NotifyInteractionPicItemAdapter(context, imgList);
                    holder.picGv.setAdapter(adapter);
                    holder.picGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int k, long l) {

                            NotifyInteractionPicItemAdapter.ViewHolder holder = (NotifyInteractionPicItemAdapter.ViewHolder) view.getTag();

                            ArrayList<String> thumbnailPaths = new ArrayList<>();
                            ArrayList<String> normalPaths = new ArrayList<>();

                            for (int i = 0; i < imgList.size(); i++) {
                                normalPaths.add(Constants.URL.QINIU_BASE_URL() + imgList.get(i).getUrl());
                                thumbnailPaths.add(Constants.URL.QINIU_BASE_URL() + imgList.get(i).getUrl());
                            }

                            PhotoBrowserPagerActivity.launch(context, thumbnailPaths, normalPaths, k, holder.picIv);
                        }
                    });
                } else {
                    holder.picGv.setVisibility(View.GONE);
                }
            } else {
                holder.picGv.setVisibility(View.GONE);
            }
        } else {
            holder.myInteractionContentTv.setText(context.getResources().getString(R.string.str_my_msg_interaction_notice_user_action_deleted));
        }

        return holder;
    }


    public class AttentionViewHolder {
        public ImageView newAttentionIv;
        public AvatarImageView userPhotoIv;
        public TextView userNameTv;
        public TextView userActionTv;
        public Button userRelationBtn;

        public NotifyListItem curNofityListItem;
    }

    private AttentionViewHolder initAttentionView(View convertView, NotifyListItem notifyListItem) {
        AttentionViewHolder holder = new AttentionViewHolder();
        holder.newAttentionIv = convertView.findViewById(R.id.new_attention_iv);
        holder.userPhotoIv = convertView.findViewById(R.id.user_photo_iv);
        holder.userNameTv = convertView.findViewById(R.id.user_name_tv);
        holder.userActionTv = convertView.findViewById(R.id.user_action_tv);
        holder.userRelationBtn = convertView.findViewById(R.id.user_relation_btn);

        holder.curNofityListItem = notifyListItem;

        if (notifyListItem.getUnread() == 1) {
            holder.newAttentionIv.setVisibility(View.VISIBLE);
        } else {
            holder.newAttentionIv.setVisibility(View.GONE);
        }
        holder.userPhotoIv.init(notifyListItem.getFromUserInfo(), true);
        ImageHelper.displayImage(context, holder.userPhotoIv, notifyListItem.getFromUserInfo().getAvatar());
        holder.userNameTv.setText(MyTextUtil.getLimitEllipseText(notifyListItem.getFromUserInfo().getNickname(), 5));

        switch (notifyListItem.getNotifyBody().getFollowStatus()) {
            case FollowStatus.STATUS_NO_FOLLOW:
                holder.userRelationBtn.setBackgroundResource(R.drawable.not_follow_bg);
                break;
            case FollowStatus.STATUS_FOLLOW_YOU:
                holder.userRelationBtn.setBackgroundResource(R.drawable.already_follow_bg);
                break;
            case FollowStatus.STATUS_FOLLOW_ME:
                holder.userRelationBtn.setBackgroundResource(R.drawable.not_follow_bg);
                break;
            case FollowStatus.STATUS_FOLLOW_EACH:
                holder.userRelationBtn.setBackgroundResource(R.drawable.follow_eachother_bg);
                break;
        }
        holder.userRelationBtn.setTag(holder);
        holder.userRelationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("userRelationBtn click!!!!!!!!!!");
                if (listener != null) {
                    AttentionViewHolder clickHolder = (AttentionViewHolder) view.getTag();
                    listener.onFollowBtnClick(clickHolder);
                }
            }
        });

        return holder;
    }
}
