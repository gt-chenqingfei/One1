package com.oneone.framework.ui.imagepicker.preview;

import android.view.View;

public interface PhotoViewClickListener {

    void OnPhotoTapListener(View view);

    void OnPhotoLongClickListener(View view, int position);
}