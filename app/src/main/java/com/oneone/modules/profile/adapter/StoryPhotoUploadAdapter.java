package com.oneone.modules.profile.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.framework.ui.BaseRecyclerViewAdapter;
import com.oneone.framework.ui.BaseViewHolder;
import com.oneone.modules.mystory.bean.StoryImg;
import com.oneone.utils.ImageHelper;

import java.util.List;

import butterknife.BindView;

/**
 * @author qingfei.chen
 * @since 2018/7/16.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class StoryPhotoUploadAdapter extends BaseRecyclerViewAdapter<StoryImg> {

    LayoutInflater inflater;

    public StoryPhotoUploadAdapter(Context context, BaseViewHolder.ItemClickListener<StoryImg> listener) {
        super(listener);
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public BaseViewHolder<StoryImg> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewGroup = inflater.inflate(R.layout.rv_item_story_photo_upload, parent, false);
        return new PhotoViewHolder(viewGroup, mListener);
    }


    public StoryImg getStoryImageByPath(String path) {
        List<StoryImg> list = getList();
        for (int i = 0; i < list.size(); i++) {
            StoryImg item = list.get(i);
            if (item.getUrl().equals(path)) {
                return item;
            }
        }
        return null;
    }

    class PhotoViewHolder extends BaseViewHolder<StoryImg> implements View.OnClickListener, TextWatcher {
        @BindView(R.id.rv_item_story_photo_upload_edit)
        EditText editText;

        @BindView(R.id.rv_item_story_photo_upload_iv_camera)
        ImageView ivCamera;

        @BindView(R.id.rv_item_story_photo_upload_iv_photo)
        ImageView ivPhoto;

        @BindView(R.id.rv_item_story_photo_upload_tv_index)
        TextView tvIndex;


        protected PhotoViewHolder(View v) {
            super(v);
        }

        protected PhotoViewHolder(View v, ItemClickListener<StoryImg> listener) {
            super(v, listener);
        }

        @Override
        public void bind(StoryImg storyImg, int position) {
            super.bind(storyImg, position);
            ImageHelper.displayImage(getContext(), ivPhoto, storyImg.getUrl());
            tvIndex.setText(storyImg.getOrderIndex() + "");
            ivCamera.setOnClickListener(this);
            editText.addTextChangedListener(this);
            editText.setText(storyImg.getCaption());
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(getData(), view.getId(), getAdapterPosition());
            }
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            getData().setCaption(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
}
