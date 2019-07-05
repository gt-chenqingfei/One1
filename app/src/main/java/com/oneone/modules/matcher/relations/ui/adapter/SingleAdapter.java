package com.oneone.modules.matcher.relations.ui.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.framework.ui.BaseRecyclerViewAdapter;
import com.oneone.framework.ui.BaseViewHolder;
import com.oneone.modules.matcher.relations.bean.SingleInfo;
import com.oneone.modules.user.bean.UserInfoBase;
import com.oneone.utils.GenderUtil;
import com.oneone.widget.AvatarImageView;

import butterknife.BindView;

/**
 * @author qingfei.chen
 * @since 2018/4/19.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class SingleAdapter extends BaseRecyclerViewAdapter<SingleInfo> {
    static final int TYPE_HEADER = 1;
    private int mCount;

    public SingleAdapter(BaseViewHolder.ItemClickListener<SingleInfo> listener) {
        super(listener);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        return super.getItemViewType(position);
    }

    @Override
    public boolean isContainsHeader() {
        return true;
    }

    @Override
    public BaseViewHolder<SingleInfo> onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View viewGroup = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_singles_header, parent, false);
            return new HeaderViewHolder(viewGroup, mListener);
        }
        View viewGroup = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_singles, parent, false);
        return new SingleViewHolder(viewGroup, mListener);
    }

    class SingleViewHolder extends BaseViewHolder<SingleInfo> implements View.OnClickListener {
        @BindView(R.id.item_singles_iv_avatar)
        AvatarImageView mIvAvatar;
        @BindView(R.id.item_singles_tv_nickname)
        TextView mTvNickName;
        @BindView(R.id.item_singles_iv_new_state)
        ImageView mIvNewState;
        @BindView(R.id.item_singles_tv_summary)
        TextView mTvSummary;
        @BindView(R.id.item_singles_tv_matcher_said)
        TextView mTvMatcherSaid;
        @BindView(R.id.item_singles_tv_wechat_nickname)
        TextView mTvWechatName;
        @BindView(R.id.item_singles_tv_relationship)
        TextView mTvRelationship;
        @BindView(R.id.item_singles_tv_matcher_relations_edit)
        TextView mTvMatcherRelationEdit;

        private SingleViewHolder(View v, ItemClickListener<SingleInfo> listener) {
            super(v, listener);
            mTvMatcherRelationEdit.setOnClickListener(this);
        }

        @Override
        public void bind(SingleInfo singleInfo, int position) {
            super.bind(singleInfo, position);

            if (singleInfo == null) {
                return;
            }

            UserInfoBase userInfo = singleInfo.getUserInfo();
            mIvAvatar.init(userInfo, true);
            mTvNickName.setText(userInfo.getMyNickname());
            mTvSummary.setText(GenderUtil.getGender(userInfo.getSex()) + "， " + userInfo.getAge() + "， " +
                    userInfo.getProvince() + " " + userInfo.getCity());

            if (singleInfo.getMatcherSaidFlg() != 0) {
                mTvMatcherSaid.setVisibility(View.VISIBLE);
                mTvMatcherSaid.setText(singleInfo.getMatcherSaid());
                mTvMatcherRelationEdit.setText(R.string.str_singles_edit_matcher_relations);
            } else {
                mTvMatcherSaid.setVisibility(View.GONE);
                mTvMatcherRelationEdit.setText(R.string.str_singles_add_matcher_relations);
            }

            if (TextUtils.isEmpty(userInfo.getWechatNickname())) {
                mTvWechatName.setVisibility(View.GONE);
            } else {
                mTvWechatName.setText(getContext().getString(R.string.str_wechat_name) + " " + userInfo.getWechatNickname());
                mTvWechatName.setVisibility(View.VISIBLE);
            }
            mTvRelationship.setText(singleInfo.getRelationship());

            if (singleInfo.getNewRelationFlg() != 0) {
                mIvNewState.setVisibility(View.VISIBLE);
            } else {
                mIvNewState.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            if (mListener == null) {
                return;
            }
            if (v.getId() == R.id.item_singles_tv_matcher_relations_edit) {
                mListener.onItemClick(getData(), v.getId(), getAdapterPosition());
            }
        }
    }

    class HeaderViewHolder extends BaseViewHolder<SingleInfo> implements View.OnClickListener {
        @BindView(R.id.item_singles_header_btn_invite)
        LinearLayout linearLayout;

        @BindView(R.id.item_singles_header_count)
        TextView mTvCount;

        private HeaderViewHolder(View v, ItemClickListener listener) {
            super(v, listener);
            linearLayout.setOnClickListener(this);
        }

        @Override
        public void bind(SingleInfo o, int position) {
            super.bind(o, position);
            mTvCount.setText(getContext().getString(R.string.str_my_singles_count, mCount + ""));
        }

        @Override
        public void onClick(View v) {
            if (mListener == null) {
                return;
            }
            if (v.getId() == R.id.item_singles_header_btn_invite) {
                mListener.onItemClick(null, v.getId(), getAdapterPosition());
            }
        }
    }

    public void setCount(int count) {
        this.mCount = count;
    }
}
