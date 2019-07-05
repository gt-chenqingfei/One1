package com.oneone.framework.ui.navigation.utils;

import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.oneone.framework.ui.navigation.BottomNavigation;


public class LayoutParamsHelper {

    /**
     * this method provide tab icon layout params
     *
     * @return return appropriate layout params for tab icon
     */
    public static FrameLayout.LayoutParams getTabItemIconLayoutParams() {
        int iconSize = Util.dpToPx(Dimens.TAB_ICON_SIZE);
        int iconHeight = Util.dpToPx(Dimens.TAB_ICON_HEIGHT);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(iconSize, iconHeight);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        layoutParams.topMargin = Util.dpToPx(FixedDimens.TAB_PADDING_TOP_INACTIVE);
        return layoutParams;
    }

    /**
     * this provide tab label layout params
     *
     * @return return appropriate layout params for tab label
     */
    public static FrameLayout.LayoutParams getTabItemTextLayoutParams() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;

        layoutParams.bottomMargin = Util.dpToPx(FixedDimens.TAB_PADDING_BOTTOM);
        layoutParams.rightMargin = Util.dpToPx(FixedDimens.TAB_PADDING_RIGHT);
        layoutParams.leftMargin = Util.dpToPx(FixedDimens.TAB_PADDING_LEFT);
        layoutParams.topMargin = Util.dpToPx(FixedDimens.TAB_ICON_SIZE) + Util.dpToPx(FixedDimens.TAB_PADDING_TOP);
        return layoutParams;
    }

    /**
     * this provide tab label layout params
     *
     * @return return appropriate layout params for tab label
     */
    public static FrameLayout.LayoutParams getTabItemTextBubbleLayoutParams() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;

        layoutParams.bottomMargin = Util.dpToPx(FixedDimens.TAB_PADDING_BOTTOM);
//        layoutParams.rightMargin = Util.dpToPx(FixedDimens.TAB_PADDING_RIGHT);
        layoutParams.leftMargin = Util.dpToPx(FixedDimens.TAB_PADDING_LEFT);
        layoutParams.topMargin = Util.dpToPx(FixedDimens.TAB_BUBBLE_PADDING_TOP) ;
        return layoutParams;
    }

}
