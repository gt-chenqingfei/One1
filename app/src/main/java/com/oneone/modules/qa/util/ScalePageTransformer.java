package com.oneone.modules.qa.util;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.oneone.modules.qa.adapter.QaQuestionAdapter;

/**
 * Created by HanHailong on 15/9/27.
 */
public class ScalePageTransformer implements ViewPager.PageTransformer {

    public static final float MAX_SCALE = 1.0f;
    public static final float MIN_SCALE = 0.8f;

    private View savedNextView;
//    private boolean isPrepareRemoveOne = false;
//    public void prepareRemoveOne () {
//        isPrepareRemoveOne = true;
//    }

    @Override
    public void transformPage(View page, float position) {
//        if (isPrepareRemoveOne) {
//            isPrepareRemoveOne = false;
//            position = 0;
//        }

        if (position < -1) {
            position = -1;
        } else if (position > 1) {
            position = 1;
        }

        float tempScale = position < 0 ? 1 + position : 1 - position;

        float slope = (MAX_SCALE - MIN_SCALE) / 1;
        //一个公式
        float scaleValue = MIN_SCALE + tempScale * slope;
        page.setScaleX(scaleValue);
        page.setScaleY(scaleValue);
    }

    class TransformHolder implements Comparable<TransformHolder> {
        float position;

        @Override
        public int compareTo(@NonNull TransformHolder transformHolder) {
            return 0;
        }
    }
}