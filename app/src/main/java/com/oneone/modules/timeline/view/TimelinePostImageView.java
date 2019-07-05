package com.oneone.modules.timeline.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.oneone.R;
import com.oneone.framework.ui.BaseView;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.imagepicker.DataHolder;
import com.oneone.framework.ui.imagepicker.ImagePicker;
import com.oneone.framework.ui.imagepicker.bean.ImageItem;
import com.oneone.framework.ui.imagepicker.ui.ImageGridActivity;
import com.oneone.framework.ui.imagepicker.ui.ImagePreviewActivity;
import com.oneone.framework.ui.utils.PermissionsUtil;
import com.oneone.framework.ui.utils.ScreenUtil;
import com.oneone.framework.ui.widget.FlowLayout;
import com.oneone.modules.timeline.activity.PermissionsWarnActivity;
import com.oneone.utils.ImageHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author qingfei.chen
 * @since 2018/6/21.
 * @version V1.0.0
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */

@LayoutResource(R.layout.view_timeline_post_image_container)
public class TimelinePostImageView extends BaseView implements View.OnClickListener {
    public static final int ID_ADD_NEW = 1000;
    public static final int ID_IMAGE_ITEM = 1001;
    public static final int ID_REMOVE = 1002;
    public static final int REQ_PICKER_PICTURE = 10001;
    public static final int REQ_PICKER_PREVIEW = 10002;
    public static final int IMAGE_SELECT_MAX_LIMIT = 9;

    public interface OnImageChangeListener {
        void onImageChange(boolean hasImage);
    }

    @BindView(R.id.activity_timeline_post_fl_image_container)
    FlowLayout mFlowLayout;
    private int mItemWidth;
    private List<ImageItem> mSelectedImages = new ArrayList<>();
    private OnImageChangeListener mOnImageChangeListener;

    private Context mContext;

    public void setOnImageChangeListener(OnImageChangeListener listener) {
        this.mOnImageChangeListener = listener;
    }

    public TimelinePostImageView(Context context) {
        super(context);
        this.mContext = context;
    }

    public TimelinePostImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    @Override
    public void onViewCreated() {
        int margin = ScreenUtil.dip2px(20 * 2);
        int spacing = ScreenUtil.dip2px(8 * 3);
        mItemWidth = (ScreenUtil.getDisplayWidth() - margin - spacing) / 4;
        generateAddImageView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case ID_ADD_NEW:
                if (!PermissionsUtil.checkPermissions(mContext, PermissionsUtil.READ_EXTERNAL_STORAGE) &&
                        !PermissionsUtil.checkPermissions(mContext, PermissionsUtil.WRITE_EXTERNAL_STORAGE)) {
                    PermissionsWarnActivity.startActivity(mContext, PermissionsWarnActivity.EXTERNAL_STORAGE);
                } else if (!PermissionsUtil.checkPermissions(mContext, PermissionsUtil.CAMERA)) {
                    PermissionsWarnActivity.startActivity(mContext, PermissionsWarnActivity.CAMERA);
                } else {
                    startImageSelect();
                }
                break;

            case ID_IMAGE_ITEM:
                if (v.getTag() == null) {
                    return;
                }
                int position = -1;
                String imageViewUrl = v.getTag().toString();
                for (int i = 0; i < mSelectedImages.size(); i++) {
                    if (mSelectedImages.get(i).path.equals(imageViewUrl)) {
                        position = i;
                        break;
                    }
                }
                startImagePreview(position);
                break;

            case ID_REMOVE:
                if (v.getTag() == null) {
                    return;
                }
                String path = v.getTag().toString();
                removeItem(path);
                if (mSelectedImages.size() == 0) {
                    if (mOnImageChangeListener != null) {
                        mOnImageChangeListener.onImageChange(false);
                    }
                }
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null || resultCode != ImagePicker.RESULT_CODE_ITEMS) {
            return;
        }
        if (requestCode == REQ_PICKER_PICTURE || requestCode == REQ_PICKER_PREVIEW) {
            setImageDataList((ArrayList<ImageItem>)
                    data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS));
        }
    }

    public void setImageDataList(List<ImageItem> selectedImages) {
        mSelectedImages = selectedImages;
        if (mSelectedImages == null || mSelectedImages.isEmpty()) {
            return;
        }
        fillImages();
    }

    public List<ImageItem> getSelectedImages() {
        return mSelectedImages;
    }

    private void removeItem(String path) {
        ImageItem item = findItemByUrl(path);
        View viewItem = mFlowLayout.findViewWithTag(path);
        if (item != null && viewItem != null) {
            mSelectedImages.remove(item);
            mFlowLayout.removeView(viewItem);
        }
    }

    private void startImagePreview(int position) {
        Intent intent = new Intent(getContext(), ImagePreviewActivity.class);
        intent.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
        DataHolder.getInstance().save(DataHolder.DH_CURRENT_IMAGE_FOLDER_ITEMS, mSelectedImages);
        intent.putExtra(ImagePreviewActivity.ISORIGIN, true);
        ((Activity) getContext()).startActivityForResult(intent, REQ_PICKER_PREVIEW);
    }

    private void startImageSelect() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setShowCamera(true);
        imagePicker.setSelectLimit(IMAGE_SELECT_MAX_LIMIT);
        imagePicker.setMultiMode(true);
        imagePicker.setFocusWidth(ScreenUtil.screenWidth);
        imagePicker.setFocusHeight(ScreenUtil.screenHeight);
        imagePicker.setCrop(true);
        imagePicker.setSaveRectangle(true);
        imagePicker.setSaveRectangle(false);
        imagePicker.setOutPutX(1000);
        imagePicker.setOutPutY(1000);
        Intent intent1 = new Intent(getContext(), ImageGridActivity.class);
        if (mSelectedImages != null && !mSelectedImages.isEmpty()) {
            intent1.putParcelableArrayListExtra(ImageGridActivity.EXTRAS_IMAGES, (ArrayList<? extends Parcelable>) mSelectedImages);
        }

        ((Activity) getContext()).startActivityForResult(intent1, REQ_PICKER_PICTURE);
    }

    private void fillImages() {
        if (mSelectedImages == null || mSelectedImages.isEmpty()) {
            if (mOnImageChangeListener != null) {
                mOnImageChangeListener.onImageChange(false);
            }
            return;
        }
        if (mOnImageChangeListener != null) {
            mOnImageChangeListener.onImageChange(true);
        }

        mFlowLayout.removeAllViews();
        for (int i = 0; i < mSelectedImages.size(); i++) {
            ViewGroup item = generateImageItem(mSelectedImages.get(i), i);
            mFlowLayout.addView(item, i);
        }

        generateAddImageView();
    }

    private ViewGroup generateImageItem(ImageItem item, int position) {
        RelativeLayout itemContainer = new RelativeLayout(getContext());
        ViewGroup.LayoutParams itemContainerParams = new LayoutParams(mItemWidth, mItemWidth);
        itemContainer.setLayoutParams(itemContainerParams);
        itemContainer.setTag(item.path);
        ImageView imageView = new ImageView(getContext());
        RelativeLayout.LayoutParams imageViewParams =
                new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(imageViewParams);
        imageView.setTag(item.path);
        imageView.setId(ID_IMAGE_ITEM);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setOnClickListener(this);
        ImageHelper.displayImage(getContext(), imageView, item.path);

        ImageView remove = new ImageView(getContext());
        remove.setImageResource(R.drawable.ic_delete);
        RelativeLayout.LayoutParams removeViewParams =
                new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        removeViewParams.topMargin = ScreenUtil.dip2px(2);
        removeViewParams.rightMargin = ScreenUtil.dip2px(2);
        removeViewParams.width = ScreenUtil.dip2px(20);
        removeViewParams.height = ScreenUtil.dip2px(20);
        removeViewParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        remove.setLayoutParams(removeViewParams);
        remove.setOnClickListener(this);
        remove.setTag(item.path);
        remove.setId(ID_REMOVE);

        itemContainer.addView(imageView);
        itemContainer.addView(remove);
        return itemContainer;
    }

    private void generateAddImageView() {
        ImageView imageView = new ImageView(getContext());
        imageView.setId(ID_ADD_NEW);
        ViewGroup.LayoutParams params = new LayoutParams(mItemWidth, mItemWidth);
        imageView.setImageResource(R.drawable.ic_camera_white);
        imageView.setBackgroundColor(getContext().getResources().getColor(R.color.color_8B99FF));
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        mFlowLayout.addView(imageView);
        imageView.setOnClickListener(this);
    }

    private ImageItem findItemByUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        if (mSelectedImages == null || mSelectedImages.isEmpty()) {
            return null;
        }

        for (ImageItem item : mSelectedImages) {
            if (url.equals(item.path)) {
                return item;
            }
        }
        return null;
    }
}
