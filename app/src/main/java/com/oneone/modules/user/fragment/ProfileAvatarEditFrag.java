package com.oneone.modules.user.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.oneone.HereSingletonFactory;
import com.oneone.R;
import com.oneone.event.EventProfileUpdateByRole;
import com.oneone.framework.android.analytics.annotation.Alias;
import com.oneone.framework.ui.BaseFragment;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.imagepicker.ImagePicker;
import com.oneone.framework.ui.imagepicker.bean.ImageItem;
import com.oneone.framework.ui.imagepicker.ui.ImageGridActivity;
import com.oneone.framework.ui.utils.PermissionsUtil;
import com.oneone.framework.ui.utils.ScreenUtil;
import com.oneone.framework.ui.widget.CircleImageView;
import com.oneone.modules.support.qiniu.PhotoUploadListener;
import com.oneone.modules.support.qiniu.UploadParam;
import com.oneone.modules.timeline.activity.PermissionsWarnActivity;
import com.oneone.modules.user.bean.UserProfileUpdateBean;
import com.oneone.utils.ImageHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author qingfei.chen
 * @since 2018/4/25.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@Alias("头像")
@LayoutResource(R.layout.frag_profile_avatar_edit)
public class ProfileAvatarEditFrag extends BaseFragment implements PhotoUploadListener {
    public static final int REQ_PICKER_AVATAR = 10000;
    @BindView(R.id.frag_profile_avatar_edit_iv_avatar)
    CircleImageView mIvAvatar;

    @BindView(R.id.frag_profile_avatar_edit_btn_confirm)
    Button mBtnConfirm;

    @BindView(R.id.frag_profile_avatar_edit_tv_avatar_tip)
    TextView mTvAvatarTip;

    private UploadParam avatarParam;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @OnClick(R.id.frag_profile_avatar_edit_btn_confirm)
    public void onConfirmClick(View view) {
        if (avatarParam == null) {
            return;
        }

        HereSingletonFactory.getInstance().getPhotoUploadManager().enqueue(avatarParam);
    }


    @OnClick(R.id.frag_profile_avatar_edit_iv_avatar)
    public void onAvatarClick(View view) {
        if (!PermissionsUtil.checkPermissions(getContext(), PermissionsUtil.READ_EXTERNAL_STORAGE) &&
                !PermissionsUtil.checkPermissions(getContext(), PermissionsUtil.WRITE_EXTERNAL_STORAGE)) {
            PermissionsWarnActivity.startActivity(getContext(), PermissionsWarnActivity.EXTERNAL_STORAGE);
        } else if (!PermissionsUtil.checkPermissions(getContext(), PermissionsUtil.CAMERA)) {
            PermissionsWarnActivity.startActivity(getContext(), PermissionsWarnActivity.CAMERA);
        } else {
            startSelectImage(1, REQ_PICKER_AVATAR);
        }
    }

    private void startSelectImage(int limit, int reqCode) {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setShowCamera(true);
        imagePicker.setSelectLimit(1);
        imagePicker.setMultiMode(false);
        imagePicker.setFocusWidth(ScreenUtil.screenWidth);
        imagePicker.setFocusHeight(ScreenUtil.screenHeight);
        imagePicker.setCrop(true);
        imagePicker.setSaveRectangle(false);
        imagePicker.setOutPutX(1000);
        imagePicker.setOutPutY(1000);
        Intent intent1 = new Intent(getContext(), ImageGridActivity.class);
        startActivityForResult(intent1, reqCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_PICKER_AVATAR) {
            List<ImageItem> items = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            ImageItem item = items.get(0);
            avatarParam = new UploadParam(REQ_PICKER_AVATAR, item.path, this);
            refreshAvatar(item);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void refreshAvatar(ImageItem item) {
        if (item == null) {
            return;
        }
        mTvAvatarTip.setVisibility(View.INVISIBLE);
        ImageHelper.displayImage(getContext(), mIvAvatar, item.path);
    }

    @Override
    public void onUploadCompleted(UploadParam param, List<UploadParam> group, boolean isAllEnd) {
        hideLoading();
        UserProfileUpdateBean info = new UserProfileUpdateBean();
        info.setAvatar(param.getRemotePath());
        EventBus.getDefault().post(new EventProfileUpdateByRole(info));
    }

    @Override
    public void onUploadError(UploadParam param, Throwable e) {
        hideLoading();
    }

    @Override
    public void onUploadProgress(UploadParam param, double percent) {

    }

    @Override
    public void onUploadStart(UploadParam param) {
        showLoading("");
    }
}
