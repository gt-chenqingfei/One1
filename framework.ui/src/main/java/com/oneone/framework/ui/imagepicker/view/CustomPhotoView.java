package com.oneone.framework.ui.imagepicker.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.oneone.framework.ui.imagepicker.photoview.PhotoView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author qingfei.chen
 * @since 2018/3/12.
 * Copyright © 2018 HereTech Technology Co.,Ltd. All rights reserved.
 */

public class CustomPhotoView extends PhotoView {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public CustomPhotoView(Context context) {
        super(context);
    }

    public CustomPhotoView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    public CustomPhotoView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            logger.error("IllegalArgumentException " + ex);
        }
        return false;
    }

}
