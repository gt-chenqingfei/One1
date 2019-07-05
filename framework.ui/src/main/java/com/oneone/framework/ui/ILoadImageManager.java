package com.oneone.framework.ui;

import android.content.Context;
import android.widget.ImageView;

/**
 * @author qingfei.chen
 * @since 18/2/6.
 * Copyright © 2018 ZheLi Technology Co.,Ltd. All rights reserved.
 */

public interface ILoadImageManager {


    void withUser(Context context, ImageView imageView, long userId);

    void withUser(Context context, ImageView imageView, long userId, boolean isLoadNew);

    void withGroup(Context context, ImageView imageView, long groupId);

    void withGroup(Context context, ImageView imageView, long groupId, boolean isLoadNew);

    void withUserAvatar(Context context, ImageView imageView, long userId);

    String withNormalPic(String normalPicPath, String picStyle);

    String withRemoveHost(String normalPath);

}
