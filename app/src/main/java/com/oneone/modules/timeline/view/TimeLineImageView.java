package com.oneone.modules.timeline.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.oneone.R;
import com.oneone.framework.ui.IPreViewMenuListener;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.image.nine.GridImageView;
import com.oneone.framework.ui.image.nine.NineGridImageView;
import com.oneone.framework.ui.image.nine.NineGridImageViewAdapter;
import com.oneone.framework.ui.imagepicker.preview.PhotoBrowserPagerActivity;
import com.oneone.framework.ui.utils.ScreenUtil;
import com.oneone.modules.timeline.bean.TimeLineImage;
import com.oneone.modules.timeline.contract.TimeLineContract;
import com.oneone.utils.ImageHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author qingfei.chen
 * @since 2018/5/30.
 * @version V1.0.0
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */

@LayoutResource(R.layout.view_timeline_image_view)
public class TimeLineImageView extends AbstractTimeLineView<List<TimeLineImage>> {
    public static final int IMAGE_MAX_COUNT_LIMIT = 5;
    @BindView(R.id.view_timeline_image_view_nine_image)
    NineGridImageView<TimeLineImage> mNineGridImageView;

    private NineGridImageViewAdapter<TimeLineImage> mAdapter;

    public TimeLineImageView(Context context) {
        super(context);
    }

    public TimeLineImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onViewCreated() {
    }

    @Override
    public void bind(List<TimeLineImage> images, TimeLineContract.Presenter presenter, int position) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.getLayoutParams();
        if (images == null || images.isEmpty()) {
            mNineGridImageView.setImagesData(null, NineGridImageView.NO_SPAN, 0);
            params.topMargin =0;
            setLayoutParams(params);
            return;
        }
        params.topMargin = ScreenUtil.dip2px(15);
        setLayoutParams(params);

        if (mAdapter == null) {
            mAdapter = new MyNineGridImageViewAdapter();
            mNineGridImageView.setAdapter(mAdapter);
        }
        mNineGridImageView.setMaxSize(IMAGE_MAX_COUNT_LIMIT);
        mNineGridImageView.setImagesData(images, getSpanType(images.size()), images.size());
    }

    private int getSpanType(int size) {
        if (size == 1) {
            return NineGridImageView.NO_SPAN;
        } else if (size == 2) {
            return NineGridImageView.NO_SPAN;
        } else if (size == 3) {
            return NineGridImageView.NO_SPAN;
        } else if (size == 4) {
            return NineGridImageView.NO_SPAN;
        } else if (size >= 5) {
            return NineGridImageView.TOP_COL_SPAN;
        }
        return NineGridImageView.NO_SPAN;
    }

    class MyNineGridImageViewAdapter extends NineGridImageViewAdapter<TimeLineImage> {
        private void refreshDiffCount(int index, GridImageView imageView) {
            if (index == 4) {
                int difference = totalCount - IMAGE_MAX_COUNT_LIMIT;
                if (difference > 0) {
                    imageView.setText("+" + difference);
                } else {
                    imageView.setText("");
                }
            } else {
                imageView.setText("");
            }
        }

        @Override
        protected void onDisplayImage(Context context, GridImageView imageView, TimeLineImage s, int position, boolean changed) {
            if (changed) {
                ImageHelper.displayImage(context, imageView, s.getUrl(), false);
                refreshDiffCount(position, imageView);
            }
        }

        @Override
        protected GridImageView generateImageView(Context context, int index, int totalCount) {
            GridImageView imageView = super.generateImageView(context, index, totalCount);
            refreshDiffCount(index, imageView);
            return imageView;
        }

        @Override
        protected void onItemImageClick(Context context, ImageView imageView, int index, List<TimeLineImage> list) {
            ArrayList<String> thumbnailPaths = new ArrayList<>();
            ArrayList<String> normalPaths = new ArrayList<>();

            for (TimeLineImage image : list) {
                String url = ImageHelper.getImageUrl(image.getUrl());
                normalPaths.add(url);
                thumbnailPaths.add(url);
            }

            PhotoBrowserPagerActivity.launch(mContext, thumbnailPaths, normalPaths, index,imageView);
        }

        @Override
        protected boolean onItemImageLongClick(Context context, ImageView imageView, int index, List<TimeLineImage> list) {
            Toast.makeText(context, "image long click position is " + index, Toast.LENGTH_SHORT).show();
            return true;
        }
    }

}
