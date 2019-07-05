package com.oneone.modules.msg.adapter.likerelation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.api.constants.LikeStatus;
import com.oneone.api.constants.Role;
import com.oneone.framework.ui.BaseRecyclerViewAdapter;
import com.oneone.framework.ui.BaseViewHolder;
import com.oneone.framework.ui.utils.ScreenUtil;
import com.oneone.modules.find.dto.LikeUserDto;
import com.oneone.modules.msg.fragment.likerelation.LikeRelationFragment;
import com.oneone.modules.msg.widget.UserRelationPhotoLayout;
import com.oneone.modules.profile.ProfileStater;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.util.HereUserUtil;
import com.oneone.widget.AvatarImageView;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import butterknife.BindView;

/**
 * Created by here on 18/5/25.
 */

public class LikeRelationAdapter extends BaseRecyclerViewAdapter<LikeUserDto> {
    public static final String TYPE_LIKE_ME = "like_me";
    public static final String TYPE_MY_LIKE = "my_like";
    public static final String TYPE_LIKE_EACHOTHER = "like_eachother";

    private String type;
    private Context context;

    public LikeRelationAdapter (LikeRelationAdapterListener listener, String type, Context context, String noMoreText) {
        super(listener);
        this.type = type;
        this.context = context;
        this.noMoreText = noMoreText;
    }

    public abstract static class LikeRelationAdapterListener implements BaseViewHolder.ItemClickListener {
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1 && isShowNoMoreView()) {
            return VIEW_TYPE_NO_MORE_ITEM;
        } else {
            return super.getItemViewType(position);
        }
    }

    @NonNull
    @Override
    public BaseViewHolder<LikeUserDto> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_NO_MORE_ITEM) {
            View viewGroup = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_no_more, parent, false);
            return new NoMoreViewHolder(viewGroup, noMoreText);
        } else {
            View viewGroup = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_like_relation, parent, false);
            return new LikeRelationAdapter.LikeRelationViewHolder(viewGroup, type);
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

    public class LikeRelationViewHolder extends BaseViewHolder<LikeUserDto> implements View.OnClickListener {
        @BindView(R.id.user_relation_photo_layout)
        RelativeLayout userRelationPhotoLayout;
        @BindView(R.id.user_photo_iv)
        AvatarImageView userPhotoIv;
        @BindView(R.id.user_name_tv)
        TextView userNameTv;
        @BindView(R.id.matcher_icon)
        ImageView matcherIconIv;
        @BindView(R.id.user_base_info_tv)
        TextView userBaseInfoTv;
        @BindView(R.id.user_interaction_tv)
        TextView userInteractionTv;
        @BindView(R.id.new_point_view)
        View newPointView;
        @BindView(R.id.user_relation_layout)
        LinearLayout userRelationLayout;
        @BindView(R.id.match_score_iv)
        ImageView matchScoreIv;

        public String type;

        private int position;

        protected LikeRelationViewHolder(View v, String type) {
            super(v);
            this.type = type;
        }

        @Override
        public void bind(LikeUserDto likeUserDto, int position) {
            super.bind(likeUserDto, position);
            this.position = position;

            if (likeUserDto == null) {
                return;
            }



            createItem(this, likeUserDto, position);
        }

        @Override
        public void onClick(final View v) {
//            switch (v.getId()) {
//            }
            mListener.onItemClick(getData(), position, position);
        }


    }

    public void createItem (LikeRelationViewHolder holder, final LikeUserDto likeUserDto, int position) {
//        if (holder.type.equals(TYPE_LIKE_EACHOTHER)) {
        if (likeUserDto.getLikeStatus() == LikeStatus.STATUS_LOVE) {
            holder.userRelationPhotoLayout.setVisibility(View.VISIBLE);
            holder.userPhotoIv.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) (69 * ScreenUtil.WIDTH_RATIO), (int) (40 * ScreenUtil.WIDTH_RATIO));
            params.leftMargin = DensityUtil.dp2px(18);
            holder.userRelationPhotoLayout.setLayoutParams(params);

            UserRelationPhotoLayout photoLayout = new UserRelationPhotoLayout(context);
            params = new RelativeLayout.LayoutParams((int) (69 * ScreenUtil.WIDTH_RATIO), (int) (40 * ScreenUtil.WIDTH_RATIO));
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            photoLayout.setLayoutParams(params);
            photoLayout.setLayout(HereUser.getInstance().getUserInfo(), likeUserDto.getUserInfo(), UserRelationPhotoLayout.LIKE_EACHOTHER);
            holder.userRelationPhotoLayout.addView(photoLayout);
            holder.userRelationPhotoLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProfileStater.startWithUserInfo(context, likeUserDto.getUserInfo());
                }
            });
        } else {
            holder.userRelationPhotoLayout.setVisibility(View.GONE);
            holder.userPhotoIv.setVisibility(View.VISIBLE);

            holder.userPhotoIv.init(likeUserDto.getUserInfo(), true);
        }

        if (likeUserDto.getUnread() == 1) {
            holder.newPointView.setVisibility(View.VISIBLE);
        } else {
            holder.newPointView.setVisibility(View.GONE);
        }
        holder.userNameTv.setText(likeUserDto.getUserInfo().getNickname());
        if (likeUserDto.getUserInfo().getRole() == Role.SINGLE) {
            holder.matcherIconIv.setVisibility(View.GONE);
        } else {
            holder.matcherIconIv.setVisibility(View.VISIBLE);
        }

        String userBaseInfoStr = "";
        if (!HereUserUtil.getGenderStr(likeUserDto.getUserInfo().getSex()).equals("")) {
            userBaseInfoStr += HereUserUtil.getGenderStr(likeUserDto.getUserInfo().getSex()) + ",";
        }
        if (likeUserDto.getUserInfo().getAge() > 0) {
            userBaseInfoStr += likeUserDto.getUserInfo().getAge() + ",";
        }
        if (!likeUserDto.getUserInfo().getProvince().equals("")) {
            userBaseInfoStr += likeUserDto.getUserInfo().getProvince() + " ";
        }
        if (!likeUserDto.getUserInfo().getCity().equals("")) {
            userBaseInfoStr += likeUserDto.getUserInfo().getCity() + ",";
        }
        if (!likeUserDto.getUserInfo().getOccupation().equals("")) {
            userBaseInfoStr += likeUserDto.getUserInfo().getOccupation();
        }
        while (userBaseInfoStr.endsWith(",")) {
            userBaseInfoStr = userBaseInfoStr.substring(0, userBaseInfoStr.length() - 1);
        }
        holder.userBaseInfoTv.setText(userBaseInfoStr);

        int intersectionVal = likeUserDto.getIntersectionValue();
        int matchVal = likeUserDto.getMatchValue();
        String intersectionValStr = "";
        String matchValStr = "";

        if (intersectionVal > 0 && matchVal > 0) {
            intersectionValStr = intersectionVal + context.getResources().getString(R.string.str_my_following_match_text) + ",";
            matchValStr = matchVal + context.getResources().getString(R.string.str_my_following_intersection_text);
        } else {
            if (intersectionVal > 0) {
                intersectionValStr = intersectionVal + context.getResources().getString(R.string.str_my_following_match_text);
            } else {
                holder.matchScoreIv.setVisibility(View.GONE);
            }
            if (matchVal > 0) {
                matchValStr = matchVal + context.getResources().getString(R.string.str_my_following_intersection_text);
            }

            if (intersectionVal <=0 && matchVal <= 0) {
                holder.userRelationLayout.setVisibility(View.GONE);
            }
        }
        holder.userInteractionTv.setText(intersectionValStr + matchValStr);
    }

}
