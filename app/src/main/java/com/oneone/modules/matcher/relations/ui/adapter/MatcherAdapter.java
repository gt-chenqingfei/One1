package com.oneone.modules.matcher.relations.ui.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.framework.ui.BaseRecyclerViewAdapter;
import com.oneone.framework.ui.BaseViewHolder;
import com.oneone.modules.matcher.relations.bean.MatcherInfo;
import com.oneone.modules.user.bean.UserInfoBase;
import com.oneone.modules.user.util.HereUserUtil;
import com.oneone.utils.GenderUtil;
import com.oneone.utils.ImageHelper;
import com.oneone.widget.AvatarImageView;

import butterknife.BindView;

/**
 * @author qingfei.chen
 * @since 2018/4/19.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class MatcherAdapter extends BaseRecyclerViewAdapter<MatcherInfo> {


    public static final int TYPE_HEADER = 1;
    public static final int TYPE_FOOTER = 2;
    LayoutInflater inflater;
    private boolean hasFooterAndHeader = true;

    public MatcherAdapter(BaseViewHolder.ItemClickListener<MatcherInfo> listener) {
        super(listener);
    }

    public MatcherAdapter(BaseViewHolder.ItemClickListener<MatcherInfo> listener, boolean hasFooterAndHeader) {
        this(listener);
        this.hasFooterAndHeader = hasFooterAndHeader;
    }

    @Override
    public int getItemViewType(int position) {
        if (!hasFooterAndHeader) {
            return super.getItemViewType(position);
        }
        if (position == 0) {
            return TYPE_HEADER;
        } else if (position == getItemCount() - 1) {
            return TYPE_FOOTER;
        }
        return super.getItemViewType(position);
    }

    @Override
    public BaseViewHolder<MatcherInfo> onCreateViewHolder(ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }

        if (viewType == TYPE_HEADER) {
            View viewGroup = inflater.inflate(R.layout.item_matcher_header, parent, false);
            return new MatcherAdapter.MatcherHeaderHolder(viewGroup, mListener);
        }

        if (viewType == TYPE_FOOTER) {
            View viewGroup = inflater.inflate(R.layout.item_matcher_footer, parent, false);
            return new MatcherAdapter.MatcherFooterHolder(viewGroup, mListener);
        }

        View viewGroup = inflater.inflate(R.layout.item_matcher, parent, false);
        return new MatcherAdapter.MatcherHolder(viewGroup, mListener);
    }


    class MatcherHolder extends BaseViewHolder<MatcherInfo> implements View.OnClickListener {
        @BindView(R.id.item_matcher_iv_avatar)
        AvatarImageView mIvAvatar;
        @BindView(R.id.item_matcher_tv_nickname)
        TextView mTvNickName;
        @BindView(R.id.item_matcher_iv_new_state)
        ImageView mIvNewState;
        @BindView(R.id.item_matcher_tv_summary)
        TextView mTvSummary;
        @BindView(R.id.item_matcher_tv_matcher_said)
        TextView mTvMatcherSaid;
        @BindView(R.id.item_matcher_tv_wechat_nickname)
        TextView mTvWechatName;
        @BindView(R.id.item_matcher_tv_relationship)
        TextView mTvRelationship;
        @BindView(R.id.item_matcher_rl_matcher_relation_edit_invite)
        RelativeLayout mRLMatcherRelationEditInvite;
        @BindView(R.id.invite_ta_to_write_btn)
        Button mBtnMatcherRelationEditInvite;

        private MatcherHolder(View v, ItemClickListener<MatcherInfo> listener) {
            super(v, listener);
            mBtnMatcherRelationEditInvite.setOnClickListener(this);
        }

        @Override
        public void bind(MatcherInfo singleInfo, int position) {
            super.bind(singleInfo, position);

            if (singleInfo == null) {
                return;
            }

            UserInfoBase userInfo = singleInfo.getUserInfo();
            mIvAvatar.init(singleInfo.getUserInfo(), true);
            mTvNickName.setText(userInfo.getMyNickname());
            mTvSummary.setText(HereUserUtil.getCompanAndOccupation(userInfo));

            if (!TextUtils.isEmpty(singleInfo.getMatcherSaid())) {
                mTvMatcherSaid.setVisibility(View.VISIBLE);
                mTvMatcherSaid.setText(singleInfo.getMatcherSaid());
                mRLMatcherRelationEditInvite.setVisibility(View.GONE);
            } else {
                mTvMatcherSaid.setVisibility(View.GONE);
                mRLMatcherRelationEditInvite.setVisibility(View.VISIBLE);
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

            mListener.onItemClick(getData(), v.getId(), getAdapterPosition());
        }
    }


    class MatcherFooterHolder extends BaseViewHolder implements View.OnClickListener {

        @BindView(R.id.item_matcher_footer_btn)
        Button mBtnMore;

        private MatcherFooterHolder(View v, ItemClickListener listener) {
            super(v, listener);
            mBtnMore.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener == null) {
                return;
            }
            mListener.onItemClick(null, v.getId(), getAdapterPosition());
        }
    }

    class MatcherHeaderHolder extends BaseViewHolder implements View.OnClickListener {

        @BindView(R.id.item_matcher_header_btn_your_matcher)
        Button mBtn;
        @BindView(R.id.item_matcher_header_ll_desc)
        LinearLayout mLLDesc;

        private MatcherHeaderHolder(View v, ItemClickListener listener) {
            super(v, listener);
            mBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mLLDesc.getVisibility() != View.VISIBLE) {
                mBtn.setBackgroundResource(R.drawable.white_up_arrow);
                mLLDesc.setVisibility(View.VISIBLE);
            } else {
                mBtn.setBackgroundResource(R.drawable.white_down_arrow);
                mLLDesc.setVisibility(View.GONE);
            }

        }
    }
}
