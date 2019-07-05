package com.oneone.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.oneone.Constants;
import com.oneone.R;
import com.oneone.framework.ui.glide.GlideUtils;
import com.oneone.framework.ui.imagepicker.bean.ImageItem;
import com.oneone.framework.ui.utils.ScreenUtil;

/**
 * @author qingfei.chen
 * @since 2018/4/11.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class ImageHelper {
    private static final String PARAM_WIDTH = "?imageView2/0/w/";
    private static final String PARAM_WIDTH_HEIGHT = "?imageView2/1/w/";
    private static final String PARAM_HEIGHT = "/h/";

    public static void displayAvatar(Context context, ImageView imageView, String uri) {
        displayAvatar(context, imageView, uri, R.drawable.imagepicker_ic_default_image);
    }

    public static void displayAvatar(Context context, ImageView imageView, String uri, int defaultRes) {
        if (imageView == null) {
            return;
        }
        String path = getImageUrl(uri, imageView.getLayoutParams().width);
        if (isFullUrl(uri)) {
            path = uri;
        }
        GlideUtils.loadImage(context, path,
                imageView, null,
                defaultRes,
                defaultRes);
    }

    public static void displayImage(Context context, ImageView imageView, String path, boolean isOriginalSize) {
        if (imageView == null) {
            return;
        }
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        if (params == null) {
            return;
        }
        int width = imageView.getLayoutParams().width;
        int height = imageView.getLayoutParams().height;
        if (!isOriginalSize) {
            width = width / 2;
            height = height / 2;
        }
        String uri = getImageUrl(path, width, height);
        if (isFullUrl(path)) {
            uri = path;
        }

        GlideUtils.loadImage(context, uri, imageView, null
                , R.drawable.imagepicker_ic_default_image, R.drawable.imagepicker_ic_default_image);
    }

    public static void displayImage(Context context, ImageView imageView, String path) {
        displayImage(context, imageView, path, true);
    }

    public static void displayCircleImage(Context context, ImageView imageView, String path) {
        int width = imageView.getLayoutParams().width;
        int height = imageView.getLayoutParams().height;
        String uri = getImageUrl(path, width, height);
        if (isFullUrl(path)) {
            uri = path;
        }

        GlideUtils.loadCircleImage(uri, imageView, null);
    }

    public static void displayGif(int resId, ImageView imageView) {
        GlideUtils.loadImageGif(resId, imageView);
    }

    public static void displayRoundImage(int radius, ImageView imageView, String path) {
        if (imageView == null) {
            return;
        }
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        if (params == null) {
            return;
        }
        int width = imageView.getLayoutParams().width;
        int height = imageView.getLayoutParams().height;

        width = width / 2;
        height = height / 2;

        String uri = getImageUrl(path, width, height);
        if (isFullUrl(path)) {
            uri = path;
        }

        GlideUtils.loadRoundImage(uri, imageView, null, radius
                , R.drawable.imagepicker_ic_default_image, R.drawable.imagepicker_ic_default_image);
    }

    public static boolean isFullUrl(String uri) {
        return !TextUtils.isEmpty(uri) &&
                (uri.contains("http")
                        || uri.contains("https")
                        || uri.contains(Environment.getDataDirectory().getPath())
                        || uri.contains(Environment.getExternalStorageDirectory().getPath()));
    }

    public static String getImageUrl(String path, int width) {
        if (width <= 0) {
            width = ScreenUtil.getDisplayWidth();
        }
        return Constants.URL.QINIU_BASE_URL() + path + PARAM_WIDTH + width;
    }

    public static String getImageUrl(String path, int width, int height) {
        if (width <= 0) {
            width = ScreenUtil.getDisplayWidth();
        }
        if (height <= 0) {
            height = ScreenUtil.getDisplayWidth();
        }
        return Constants.URL.QINIU_BASE_URL() + path + PARAM_WIDTH_HEIGHT + width + PARAM_HEIGHT + height;
    }

    public static String getImageUrl(String path) {
        if (isFullUrl(path)) {
            return path;
        }
        return Constants.URL.QINIU_BASE_URL() + path;
    }
}
