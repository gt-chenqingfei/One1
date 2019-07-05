package com.oneone.modules.profile.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oneone.Constants;
import com.oneone.R;
import com.oneone.api.constants.Role;
import com.oneone.framework.ui.BaseRecyclerViewAdapter;
import com.oneone.framework.ui.BaseViewHolder;
import com.oneone.framework.ui.imagepicker.preview.PhotoBrowserPagerActivity;
import com.oneone.framework.ui.utils.ScreenUtil;
import com.oneone.modules.matcher.relations.bean.ImageInfo;
import com.oneone.modules.matcher.relations.bean.SingleInfo;
import com.oneone.modules.profile.ProfileStater;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.modules.user.bean.UserInfoBase;
import com.oneone.modules.user.util.HereUserUtil;
import com.oneone.utils.ImageHelper;
import com.oneone.widget.AvatarImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * @author qingfei.chen
 * @since 2018/5/16.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class ProfileSinglesAdapter extends BaseRecyclerViewAdapter<SingleInfo> {
    static final int TYPE_HEADER = 1;
    private int totalCount = 0;
    int pictrureItemWidth;
    private UserInfo mUserInfo;

    public ProfileSinglesAdapter(BaseViewHolder.ItemClickListener<SingleInfo> listener, UserInfo userInfo) {
        super(listener);
        this.mUserInfo = userInfo;
        calculatePictureItemWidth();
    }

    private void calculatePictureItemWidth() {
        int pictureContainerMargin = ScreenUtil.dip2px(21) * 2;
        int space = ScreenUtil.dip2px(8) * 3;
        pictrureItemWidth = (ScreenUtil.getDisplayWidth() - pictureContainerMargin - space) / 4;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public BaseViewHolder<SingleInfo> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View viewGroup = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_singles_header, parent, false);
            return new HeaderViewHolder(viewGroup, mListener);
        }
        View viewGroup = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_singles, parent, false);
        return new SingleViewHolder(viewGroup, mListener);
    }

    class SingleViewHolder extends BaseViewHolder<SingleInfo> implements View.OnClickListener {
        static final int PICTURE_ID = 2018;
        static final String KEY_URL = "key_url";
        static final String KEY_POS = "key_pos";
        @BindView(R.id.item_profile_singles_iv_avatar)
        AvatarImageView mIvAvatar;
        @BindView(R.id.item_profile_singles_tv_nickname)
        TextView mTvNickName;
        @BindView(R.id.item_profile_singles_tv_new_status)
        TextView mIvIntersection;// 交集
        @BindView(R.id.item_profile_singles_tv_summary)
        TextView mTvSummary;
        @BindView(R.id.item_profile_singles_tv_match_score)
        TextView mTvMatchScore;// 合拍度
        @BindView(R.id.item_profile_singles_tv_occ)
        TextView mTvOcc;

        @BindView(R.id.item_profile_singles_ll_picture_container)
        LinearLayout mLLPictureContainer;

        private SingleViewHolder(View v, ItemClickListener<SingleInfo> listener) {
            super(v, listener);
            v.setOnClickListener(this);
        }

        @Override
        public void bind(SingleInfo singleInfo, int position) {
            super.bind(singleInfo, position);

            if (singleInfo == null) {
                return;
            }

            UserInfoBase userInfo = singleInfo.getUserInfo();
            if (userInfo != null) {
                mIvAvatar.init(userInfo, true);
                mTvNickName.setText(userInfo.getMyNickname());

                mTvSummary.setText(HereUserUtil.getSummary1(userInfo) + "，");
                mTvOcc.setText(userInfo.getOccupation());
            }
            if (mUserInfo.getRole() == Role.MATCHER) {
                mTvMatchScore.setVisibility(View.GONE);
            } else {
                mTvMatchScore.setVisibility(View.VISIBLE);
            }
            if (singleInfo.getMatchScore() != 0) {
                mTvMatchScore.setText(singleInfo.getMatchScore() + "");
            } else {
                mTvMatchScore.setVisibility(View.GONE);
            }

            if (singleInfo.getIntersectionCount() != 0) {
                mIvIntersection.setVisibility(View.VISIBLE);
                mIvIntersection.setText(singleInfo.getIntersectionCount() + "");
            } else {
                mIvIntersection.setVisibility(View.GONE);
            }

            if (singleInfo.getImgs() != null) {
                int count = singleInfo.getImgs().size();
                if (count > 4) {
                    count = 4;
                }
                mLLPictureContainer.removeAllViews();
                for (int i = 0; i < count; i++) {
                    final ImageInfo imageInfo = singleInfo.getImgs().get(i);
                    final ImageView picture = newImageView(getContext(), imageInfo.getUrl(), i);
                    picture.setTag(Constants.URL.QINIU_BASE_URL() + imageInfo.getUrl());
                    mLLPictureContainer.addView(picture);

                    final int finalI = i;
                    final int finalCount = count;
                    picture.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ArrayList<String> thumbnailPaths = new ArrayList<>();
                            ArrayList<String> normalPaths = new ArrayList<>();

                            for (int i = 0; i < finalCount; i++) {
                                String imageViewUrl = (String) mLLPictureContainer.getChildAt(i).getTag();
                                normalPaths.add(imageViewUrl);
                                thumbnailPaths.add(imageViewUrl);
                            }

                            PhotoBrowserPagerActivity.launch(getContext(), thumbnailPaths, normalPaths, finalI, picture);
                        }
                    });
                }
                mLLPictureContainer.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.item_singles_tv_matcher_relations_edit) {
                if (mListener != null) {
                    mListener.onItemClick(getData(), v.getId(), getAdapterPosition());
                }
            } else if (v.getId() == R.id.item_profile_singles_iv_avatar) {
                ProfileStater.startWithUserInfo(getContext(), getData().getUserInfo());
            }
        }

        ImageView newImageView(Context context, String url, int pos) {
            ImageView ivPicture = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    pictrureItemWidth, pictrureItemWidth);
            params.setMarginStart(ScreenUtil.dip2px(8));
            ivPicture.setLayoutParams(params);
            ivPicture.setId(PICTURE_ID);
            Map<String, String> param = new HashMap();
            param.put(KEY_POS, String.valueOf(pos));
            param.put(KEY_URL, url);
            ivPicture.setTag(param);
            ivPicture.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ImageHelper.displayImage(context, ivPicture, url);
            return ivPicture;
        }
    }


    class HeaderViewHolder extends BaseViewHolder {
        @BindView(R.id.item_profile_singles_header_title)
        TextView mTvTitle;

        private HeaderViewHolder(View v, ItemClickListener listener) {
            super(v, listener);
        }

        @Override
        public void bind(Object o, int position) {
            super.bind(o, position);
            mTvTitle.setText(getContext().getString(R.string.str_profile_singles_header_title, totalCount + ""));
        }
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
