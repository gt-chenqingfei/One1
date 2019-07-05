package com.oneone.framework.ui.navigation.utils;

import android.animation.ValueAnimator;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneone.framework.ui.navigation.BottomNavigation;


public class AnimationHelper {
    /**
     * this variable hold {@link BottomNavigation}
     *
     * @see BottomNavigation
     */
    private int type;

    /**
     * this constant used for specify animations duration, also used for determine delay time for send
     * {@link com.oneone.framework.ui.navigation.events.OnSelectedItemChangeListener} event
     */
    public static int ANIMATION_DURATION = 100;

    public static float ALPHA_TO = 1f;
    public static float ALPHA_FROM = 1f;


    public static float SCALE_FROM_X = 1f;
    public static float SCALE_FROM_Y = 1f;
    public static float SCALE_TO_X = 1f;
    public static float SCALE_TO_Y = 1f;

    public static float PIVOT_Y = 1f;
    public static float PIVOT_X = 1f;


    /**
     * animate tab item when it deselected
     */
    public void animateDeactivate(final TextView tabText, final ImageView tabIcon) {
        AlphaAnimation fAlphaAnimation = new AlphaAnimation(ALPHA_TO, ALPHA_FROM);
        fAlphaAnimation.setDuration(ANIMATION_DURATION);
        fAlphaAnimation.setFillAfter(true);
        ValueAnimator paddingAnimator = ValueAnimator.ofInt(
                Util.dpToPx(FixedDimens.TAB_PADDING_TOP_ACTIVE)
                , Util.dpToPx(FixedDimens.TAB_PADDING_TOP_INACTIVE)
        );
        paddingAnimator.setDuration(ANIMATION_DURATION);
        paddingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                FrameLayout.LayoutParams tabIconLayoutParams = (FrameLayout.LayoutParams) tabIcon.getLayoutParams();
                tabIconLayoutParams.topMargin = (int) valueAnimator.getAnimatedValue();
                tabIcon.setLayoutParams(tabIconLayoutParams);
            }
        });
        ScaleAnimation fScaleAnimation = new ScaleAnimation(SCALE_FROM_X, SCALE_TO_X, SCALE_TO_Y,
                SCALE_FROM_Y, Animation.RELATIVE_TO_SELF, PIVOT_X, Animation.RELATIVE_TO_SELF, PIVOT_Y);

        AnimationSet fAnimationSet = new AnimationSet(true);
        fAnimationSet.setDuration(ANIMATION_DURATION);
        fAnimationSet.addAnimation(fScaleAnimation);
        fAnimationSet.addAnimation(fAlphaAnimation);
        fAnimationSet.setFillAfter(true);

        tabIcon.startAnimation(fAlphaAnimation);
        tabText.startAnimation(fAnimationSet);
        paddingAnimator.start();
    }

    /**
     * animate tab item when it selected
     */
    public void animateActivate(final TextView tabText, final ImageView tabIcon) {
        AlphaAnimation fAlphaAnimation = new AlphaAnimation(ALPHA_FROM, ALPHA_FROM);
        fAlphaAnimation.setDuration(ANIMATION_DURATION);
        fAlphaAnimation.setFillAfter(true);

        ValueAnimator paddingAnimator = ValueAnimator.ofInt(
                Util.dpToPx(FixedDimens.TAB_PADDING_TOP_INACTIVE)
                , Util.dpToPx(FixedDimens.TAB_PADDING_TOP_ACTIVE)
        );
        paddingAnimator.setDuration(ANIMATION_DURATION);
        paddingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                FrameLayout.LayoutParams tabIconLayoutParams = (FrameLayout.LayoutParams) tabIcon.getLayoutParams();
                tabIconLayoutParams.topMargin = (int) valueAnimator.getAnimatedValue();
                tabIcon.setLayoutParams(tabIconLayoutParams);

            }
        });

        ScaleAnimation fTextScaleAnimation = new ScaleAnimation(SCALE_FROM_X, SCALE_TO_X, SCALE_TO_Y,
                SCALE_FROM_Y, Animation.RELATIVE_TO_SELF, PIVOT_X, Animation.RELATIVE_TO_SELF, PIVOT_Y);
        AnimationSet fAnimationSet = new AnimationSet(true);
        fAnimationSet.setDuration(ANIMATION_DURATION);
        fAnimationSet.addAnimation(fAlphaAnimation);
        fAnimationSet.addAnimation(fTextScaleAnimation);
        fAnimationSet.setFillAfter(true);

        tabIcon.startAnimation(fAlphaAnimation);
        tabText.startAnimation(fAnimationSet);
        paddingAnimator.start();
    }

    public void animateActivate(final TextView tabText, final ImageView tabIcon,
                                int unselectedTextColor, int selectedTextColor,
                                Drawable selectedTabIcon, Drawable unselectedTabIcon) {
        tabText.setTextColor(selectedTextColor);

        Drawable[] tabIcons = new Drawable[]{unselectedTabIcon, selectedTabIcon};
        TransitionDrawable iconTransition = new TransitionDrawable(tabIcons);
        tabIcon.setImageDrawable(iconTransition);
        iconTransition.startTransition(ANIMATION_DURATION);
        ValueAnimator paddingAnimator = ValueAnimator.ofInt(
                Util.dpToPx(FixedDimens.TAB_PADDING_TOP_INACTIVE)
                , Util.dpToPx(FixedDimens.TAB_PADDING_TOP_ACTIVE)
        );
        paddingAnimator.setDuration(ANIMATION_DURATION);
        paddingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                FrameLayout.LayoutParams tabIconLayoutParams = (FrameLayout.LayoutParams) tabIcon.getLayoutParams();
                tabIconLayoutParams.topMargin = (int) valueAnimator.getAnimatedValue();
                tabIcon.setLayoutParams(tabIconLayoutParams);
            }
        });

        ScaleAnimation fTextScaleAnimation = new ScaleAnimation(SCALE_FROM_X, SCALE_TO_X, SCALE_TO_Y,
                SCALE_FROM_Y, Animation.RELATIVE_TO_SELF, PIVOT_X, Animation.RELATIVE_TO_SELF, PIVOT_Y);
        AnimationSet fAnimationSet = new AnimationSet(true);
        fAnimationSet.setDuration(ANIMATION_DURATION);
        fAnimationSet.addAnimation(fTextScaleAnimation);
        fAnimationSet.setFillAfter(true);

        tabText.startAnimation(fAnimationSet);
        paddingAnimator.start();

    }

    /**
     * animate tab item when it deselected
     */
    public void animateDeactivate(final TextView tabText, final ImageView tabIcon,
                                  int unselectedTabTextColor, int selectedTextColor,
                                  Drawable selectedTabIcon, Drawable unselectedTabIcon) {

        Drawable[] tabIcons = new Drawable[]{selectedTabIcon, unselectedTabIcon};
        TransitionDrawable iconTransition = new TransitionDrawable(tabIcons);
        tabIcon.setImageDrawable(iconTransition);
        iconTransition.startTransition(ANIMATION_DURATION);

        ValueAnimator paddingAnimator = ValueAnimator.ofInt(
                Util.dpToPx(FixedDimens.TAB_PADDING_TOP_ACTIVE)
                , Util.dpToPx(FixedDimens.TAB_PADDING_TOP_INACTIVE)
        );
        paddingAnimator.setDuration(ANIMATION_DURATION);
        paddingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                FrameLayout.LayoutParams tabIconLayoutParams = (FrameLayout.LayoutParams) tabIcon.getLayoutParams();
                tabIconLayoutParams.topMargin = (int) valueAnimator.getAnimatedValue();
                tabIcon.setLayoutParams(tabIconLayoutParams);
            }
        });
        ScaleAnimation fScaleAnimation = new ScaleAnimation(SCALE_FROM_X, SCALE_TO_X, SCALE_TO_Y,
                SCALE_FROM_Y, Animation.RELATIVE_TO_SELF, PIVOT_X, Animation.RELATIVE_TO_SELF, PIVOT_Y);

        AnimationSet fAnimationSet = new AnimationSet(true);
        fAnimationSet.setDuration(ANIMATION_DURATION);
        fAnimationSet.addAnimation(fScaleAnimation);
        fAnimationSet.setFillAfter(true);

        tabText.startAnimation(fAnimationSet);
        paddingAnimator.start();
        tabText.setTextColor(unselectedTabTextColor);
    }
}
