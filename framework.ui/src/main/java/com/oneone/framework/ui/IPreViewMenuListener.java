package com.oneone.framework.ui;

import android.content.Intent;
import android.os.Parcelable;

/**
 * @author qingfei.chen
 * @since 18/2/22.
 * Copyright © 2018 ZheLi Technology Co.,Ltd. All rights reserved.
 */

public interface IPreViewMenuListener {

    void onSaveImageToAlbum(String normalPath);

    void onActivityResult(int requestCode, int resultCode, Intent data);
}
