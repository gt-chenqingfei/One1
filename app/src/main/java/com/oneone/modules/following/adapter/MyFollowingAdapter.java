package com.oneone.modules.following.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.api.constants.Role;
import com.oneone.framework.ui.BaseRecyclerViewAdapter;
import com.oneone.framework.ui.BaseViewHolder;
import com.oneone.modules.following.beans.FollowListItem;
import com.oneone.modules.msg.adapter.likerelation.LikeRelationAdapter;
import com.oneone.modules.user.util.HereUserUtil;
import com.oneone.widget.AvatarImageView;

import butterknife.BindView;

/**
 * Created by here on 18/4/26.
 */

public class MyFollowingAdapter extends BaseRecyclerViewAdapter<FollowListItem> {
    public static final int TYPE_MY_FOLLOW = 0;
    public static final int TYPE_MY_ATTENTION = 1;
    private Context context;
    private MyFollowingAdapterListener listener;
    private int type;

    public MyFollowingAdapter(MyFollowingAdapterListener listener, Context context, int type) {
        super(listener);
        this.listener = listener;
        this.context = context;
        this.type = type;
    }

    public abstract static class MyFollowingAdapterListener implements BaseViewHolder.ItemClickListener {
        public void itemBtnClick(FollowListItem followListItem, View btnView) {
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1 && isShowNoMoreView()) {
            return VIEW_TYPE_NO_MORE_ITEM;
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public BaseViewHolder<FollowListItem> onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_NO_MORE_ITEM) {
            View viewGroup = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_no_more, parent, false);
            return new NoMoreViewHolder(viewGroup, parent.getContext().getString(R.string.str_list_default_no_more_text_1));
        } else {
            View viewGroup = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_following, parent, false);
            return new MyFollowingViewHolder(viewGroup, type);
        }
    }

    public class NoMoreViewHolder extends BaseViewHolder {
        @BindView(R.id.no_more_tv)
        TextView noMoreTv;

        private String noMoreText;

        protected NoMoreViewHolder(View v, String noMoreText) {
            super(v);
            this.noMoreText = noMoreText;
        }

        @Override
        public void bind(Object o, int position) {
            super.bind(o, position);

            noMoreTv.setText(noMoreText);
        }
    }

    public class MyFollowingViewHolder extends BaseViewHolder<FollowListItem> implements View.OnClickListener {
        @BindView(R.id.user_photo_iv)
        AvatarImageView userPhotoIv;
        @BindView(R.id.matcher_icon)
        ImageView matcherIconIv;
        @BindView(R.id.item_btn)
        Button itemBtn;
        @BindView(R.id.user_name_tv)
        TextView userNameTv;
        @BindView(R.id.user_base_info_tv)
        TextView userBaseInfoTv;
        @BindView(R.id.user_relation_layout)
        LinearLayout userRelationLayout;
        @BindView(R.id.user_relation_tv)
        TextView userRelationTv;
        @BindView(R.id.match_score_iv)
        ImageView matchScoreIv;

        private int type;


        protected MyFollowingViewHolder(View v, int type) {
            super(v);
            itemBtn.setOnClickListener(this);
            this.type = type;
        }

        @Override
        public void bind(FollowListItem followListItem, int position) {
            super.bind(followListItem, position);

            if (followListItem == null) {
                return;
            }

            itemBtn.setTag(followListItem);
            int followStatus = followListItem.getFollowStatus();
            if (followStatus == 0 || followStatus == 2) {
                itemBtn.setBackgroundResource(R.drawable.not_follow_bg);
            } else if (followStatus == 1) {
                itemBtn.setBackgroundResource(R.drawable.already_follow_bg);
            } else if (followStatus == 3) {
                itemBtn.setBackgroundResource(R.drawable.follow_eachother_bg);
            }
            itemBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FollowListItem followListItem = (FollowListItem) view.getTag();
                    listener.itemBtnClick(followListItem, view);
                }
            });

            userPhotoIv.init(followListItem.getUserInfo(), true);
            userNameTv.setText(followListItem.getUserInfo().getNickname());
            if (followListItem.getUserInfo().getRole() == Role.SINGLE) {
                matcherIconIv.setVisibility(View.GONE);
            } else {
                matcherIconIv.setVisibility(View.VISIBLE);
            }

            String userBaseInfoStr = "";
            if (!HereUserUtil.getGenderStr(followListItem.getUserInfo().getSex()).equals("")) {
                userBaseInfoStr += HereUserUtil.getGenderStr(followListItem.getUserInfo().getSex()) + ",";
            }
            if (followListItem.getUserInfo().getAge() > 0) {
                userBaseInfoStr += followListItem.getUserInfo().getAge() + ",";
            }
            if (!followListItem.getUserInfo().getProvince().equals("")) {
                userBaseInfoStr += followListItem.getUserInfo().getProvince() + " ";
            }
            if (!followListItem.getUserInfo().getCity().equals("")) {
                userBaseInfoStr += followListItem.getUserInfo().getCity() + ",";
            }
            if (!followListItem.getUserInfo().getOccupation().equals("")) {
                userBaseInfoStr += followListItem.getUserInfo().getOccupation();
            }
            while (userBaseInfoStr.endsWith(",")) {
                userBaseInfoStr = userBaseInfoStr.substring(0, userBaseInfoStr.length() - 1);
            }
            userBaseInfoTv.setText(userBaseInfoStr);

            if (type == TYPE_MY_FOLLOW) {
                userRelationLayout.setVisibility(View.GONE);
            } else if (type == TYPE_MY_ATTENTION) {
                userRelationLayout.setVisibility(View.VISIBLE);
                int intersectionVal = followListItem.getIntersectionCount();
                int matchVal = followListItem.getMatchScore();
                String intersectionValStr = "";
                String matchValStr = "";

                if (intersectionVal > 0 && matchVal > 0) {
                    matchScoreIv.setVisibility(View.VISIBLE);
                    intersectionValStr = intersectionVal + context.getResources().getString(R.string.str_my_following_match_text) + ",";
                    matchValStr = matchVal + context.getResources().getString(R.string.str_my_following_intersection_text);
                } else {
                    if (intersectionVal > 0) {
                        intersectionValStr = intersectionVal + context.getResources().getString(R.string.str_my_following_match_text);
                        matchScoreIv.setVisibility(View.VISIBLE);
                    } else {
                        matchScoreIv.setVisibility(View.GONE);
                    }
                    if (matchVal > 0) {
                        matchValStr = matchVal + context.getResources().getString(R.string.str_my_following_intersection_text);
                    }

                    if (intersectionVal <=0 && matchVal <= 0) {
                        userRelationLayout.setVisibility(View.GONE);
                    }
                }
                userRelationTv.setText(intersectionValStr + matchValStr);
            }
        }

        @Override
        public void onClick(final View v) {
            switch (v.getId()) {
                case R.id.modify_qa_btn:
                    final FollowListItem followListItem = (FollowListItem) v.getTag();
                    //create modify dialog
                    break;
            }
        }
    }


}
