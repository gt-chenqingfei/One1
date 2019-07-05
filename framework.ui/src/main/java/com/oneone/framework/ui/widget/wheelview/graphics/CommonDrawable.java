/*
 * Copyright (C) 2016 venshine.cn@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.oneone.framework.ui.widget.wheelview.graphics;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;

import com.oneone.framework.ui.widget.wheelview.common.WheelConstants;
import com.oneone.framework.ui.widget.wheelview.widget.WheelView;

/**
 * common滚轮样式
 *
 * @author venshine
 */
public class CommonDrawable extends WheelDrawable {


    private Paint mCommonBgPaint, mCommonPaint, mCommonDividerPaint, mCommonBorderPaint;

    private int mWheelSize, mItemH;

    public CommonDrawable(int width, int height, WheelView.WheelViewStyle style, int wheelSize, int itemH) {
        super(width, height, style);
        mWheelSize = wheelSize;
        mItemH = itemH;
        init();
    }

    private void init() {
        mCommonBgPaint = new Paint();
        mCommonBgPaint.setColor(mStyle.backgroundColor != -1 ? mStyle.backgroundColor : WheelConstants
                .WHEEL_SKIN_COMMON_BG);

        mCommonPaint = new Paint();
        mCommonPaint.setColor(WheelConstants.WHEEL_SKIN_COMMON_COLOR);

        mCommonDividerPaint = new Paint();
        mCommonDividerPaint.setColor(WheelConstants.WHEEL_SKIN_COMMON_DIVIDER_COLOR);
        mCommonDividerPaint.setStrokeWidth(0);

        mCommonBorderPaint = new Paint();
        mCommonBorderPaint.setStrokeWidth(0);
        mCommonBorderPaint.setColor(WheelConstants.WHEEL_SKIN_COMMON_BORDER_COLOR);

    }

    @Override
    public void draw(Canvas canvas) {
        // draw background
        canvas.drawRect(0, 0, mWidth, mHeight, mCommonBgPaint);
        // draw select border
        if (mItemH != 0) {
            canvas.drawRect(0, mItemH * (mWheelSize / 2), mWidth, mItemH
                    * (mWheelSize / 2 + 1), mCommonPaint);
            canvas.drawLine(0, mItemH * (mWheelSize / 2), mWidth, mItemH
                    * (mWheelSize / 2), mCommonDividerPaint);
            canvas.drawLine(0, mItemH * (mWheelSize / 2 + 1), mWidth, mItemH
                    * (mWheelSize / 2 + 1), mCommonDividerPaint);

            canvas.drawLine(0, 0, 0, mHeight, mCommonBorderPaint);
            canvas.drawLine(mWidth, 0, mWidth, mHeight, mCommonBorderPaint);
        }
    }
}
