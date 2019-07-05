package com.oneone.framework.ui.imagepicker.preview;

import android.app.Activity;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.oneone.framework.ui.glide.progress.ProgressTarget;
import com.oneone.framework.ui.utils.ScreenUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * @author qingfei.chen
 * @since 17/12/20.
 * Copyright © 2017 HereTech Technology Co.,Ltd. All rights reserved.
 */

public class PhotoBrowserPageAdapter extends PagerAdapter {

    ArrayList<String> thumbnailPaths = new ArrayList<>();
    ArrayList<String> normalPaths = new ArrayList<>();
    private Activity mActivity;
    private PhotoViewClickListener listener;
    ProgressImageView progressImageView;
    private String imageDefaultUrl;

    public PhotoBrowserPageAdapter(Activity activity,
                                   ArrayList<String> thumbnailPaths,
                                   ArrayList<String> NormalPaths) {
        this.mActivity = activity;
        this.thumbnailPaths = thumbnailPaths;
        this.normalPaths = NormalPaths;
    }

    public void setNormalPaths(ArrayList<String> normalPaths, ArrayList<String> thumbnailPaths) {
        this.normalPaths = normalPaths;
        this.thumbnailPaths = thumbnailPaths;
        notifyDataSetChanged();
    }

    public void setPhotoViewClickListener(PhotoViewClickListener listener1) {
        this.listener = listener1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        progressImageView = new ProgressImageView(mActivity);

        progressImageView.setTag(normalPaths.get(position));

        displayImage(normalPaths.get(position), progressImageView);

        /* 设置点击事件 */
        progressImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.OnPhotoTapListener(v);
            }
        });

        // 设置 ImageView 的单击事件
        progressImageView.getImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.OnPhotoTapListener(v);
                }
            }
        });

        progressImageView.getImageView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (listener != null)
                    listener.OnPhotoLongClickListener(v, position);
                return true;
            }
        });

        container.addView(progressImageView);


        return progressImageView;
    }

    @Override
    public int getCount() {
        return normalPaths == null ? 0 : normalPaths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public ImageView getImageView() {
        return progressImageView.getImageView();
    }

    private static class ProgressHandler extends Handler {

        private final WeakReference<Activity> mActivity;
        private final ProgressImageView mProgressImageView;

        public ProgressHandler(Activity activity, ProgressImageView progressImageView) {
            super(Looper.getMainLooper());
            mActivity = new WeakReference<>(activity);
            mProgressImageView = progressImageView;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final Activity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case 1:
                        int percent = msg.arg1 * 100 / msg.arg2;
                        mProgressImageView.setProgress(percent);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private boolean isFullUrl(String uri) {
        return !TextUtils.isEmpty(uri) &&
                (uri.contains("http")
                        || uri.contains("https")
                        || uri.contains(Environment.getDataDirectory().getPath())
                        || uri.contains(Environment.getExternalStorageDirectory().getPath()));
    }

    private String getImageUrl(String path) {
        return imageDefaultUrl + path;
    }

    private void displayImage(String url, ProgressImageView progressTarget) {
        String displayUrl = url;
        if (!isFullUrl(url)) {
            displayUrl = getImageUrl(url);
        }
        Glide.with(mActivity)
                .load(displayUrl)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(getImageViewProgress(progressTarget));
    }

    private ProgressTarget getImageViewProgress(final ProgressImageView imageView) {
        ProgressTarget<String, GlideDrawable> progressTarget =
                new ProgressTarget<String, GlideDrawable>((String)imageView.getTag(),
                        new GlideDrawableImageViewTarget(imageView.getImageView())) {

                    @Override
                    protected void onStartDownload() {
                        imageView.setProgress(0);
                    }

                    @Override
                    protected void onDownloading(long bytesRead, long expectedLength) {
                        imageView.setProgress((int) (bytesRead * 100 / expectedLength));
                    }

                    @Override
                    protected void onDownloaded() {
                        imageView.getProgressPieView().setVisibility(View.GONE);
                    }

                    @Override
                    protected void onDelivered(int status) {
                    }

                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                        super.onResourceReady(resource, animation);
                        imageView.getImageView().setImageDrawable(resource);
                    }
                };
        return progressTarget;
    }
}
