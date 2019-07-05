package com.oneone.modules.profile.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.oneone.Constants;
import com.oneone.R;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.imagepicker.preview.PhotoBrowserPagerActivity;
import com.oneone.modules.profile.presenter.ProfilePresenter;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.utils.ImageHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author qingfei.chen
 * @since 2018/5/12.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@LayoutResource(R.layout.layout_profile_dumping_image_view)
public class DumpingView4Single extends AbstractProfileView {

    @BindView(R.id.activity_profile_dumping_image)
    ImageView mIvDumpling;

    UserInfo userInfo;

    public DumpingView4Single(Context context) {
        super(context);
    }

    @Override
    public void onViewCreated() {

    }

    @Override
    public void bindData(UserInfo userInfo, ProfilePresenter presenter) {
        this.userInfo = userInfo;
        if (HereUser.getUserId().equals(userInfo.getUserId())) {
            ImageHelper.displayAvatar(getContext(), mIvDumpling, userInfo.getMyAvatar(), R.drawable.default_avatar_single_big);
        } else {
            ImageHelper.displayAvatar(getContext(), mIvDumpling, userInfo.getAvatar(), R.drawable.default_avatar_single_big);
        }
    }

    @OnClick(R.id.activity_profile_dumping_image)
    public void onDumpImageClick() {
        sendAnimationParams(mIvDumpling);
    }

    private void sendAnimationParams(View view) {
        if (TextUtils.isEmpty(userInfo.getMyAvatar())) return;// 非网络图片不能查看大图
        String path = Constants.URL.QINIU_BASE_URL() + userInfo.getMyAvatar();
        ArrayList<String> thumbnailPaths = new ArrayList<>();
        ArrayList<String> normalPaths = new ArrayList<>();
        thumbnailPaths.add(path);
        normalPaths.add(path);
        PhotoBrowserPagerActivity.launch(mContext, thumbnailPaths, normalPaths, 0, view);
    }

}
