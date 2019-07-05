package com.oneone.modules.profile.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oneone.R;
import com.oneone.framework.ui.BaseRecyclerViewAdapter;
import com.oneone.framework.ui.BaseViewHolder;
import com.oneone.modules.user.bean.UserAvatarInfo;
import com.oneone.utils.ImageHelper;
import com.oneone.widget.AvatarImageView;

import butterknife.BindView;

public class AvatarImageViewAdapter extends BaseRecyclerViewAdapter<UserAvatarInfo> {

    public AvatarImageViewAdapter(BaseViewHolder.ItemClickListener<UserAvatarInfo> listener) {
        super(listener);
    }

    @NonNull
    @Override
    public BaseViewHolder<UserAvatarInfo> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_avatar_imageview, parent, false);
        return new AvatarImageViewViewHolder(view, mListener);
    }

    class AvatarImageViewViewHolder extends BaseViewHolder<UserAvatarInfo> implements View.OnClickListener {

        @BindView(R.id.iv_avatar)
        AvatarImageView avatarImageView;

        protected AvatarImageViewViewHolder(View v, ItemClickListener<UserAvatarInfo> listener) {
            super(v, listener);
            avatarImageView.setOnClickListener(this);
        }

        @Override
        public void bind(UserAvatarInfo userAvatarInfo, int position) {
            super.bind(userAvatarInfo, position);
            ImageHelper.displayAvatar(getContext(), avatarImageView, userAvatarInfo.getAvatar());
        }

        @Override
        public void onClick(View v) {
            if (mListener == null) return;
            mListener.onItemClick(getData(), v.getId(), getAdapterPosition());
        }
    }
}
