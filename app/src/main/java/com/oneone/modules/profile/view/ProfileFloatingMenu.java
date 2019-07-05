package com.oneone.modules.profile.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;
import com.melnykov.fab.ScrollDirectionListener;
import com.oneone.R;
import com.oneone.framework.ui.utils.ScreenUtil;


/**
 * <p>
 * 个人资料 悬浮按钮
 *
 * @author qingfei.chen
 * </p>
 */
public class ProfileFloatingMenu extends LinearLayout implements ScrollDirectionListener {
    private static final int ANIMATION_DURATION = 300;

    private AnimatorSet mExpandAnimation = new AnimatorSet().setDuration(ANIMATION_DURATION);
    private AnimatorSet mCollapseAnimation = new AnimatorSet().setDuration(ANIMATION_DURATION);

    private static Interpolator expandInterpolator = new OvershootInterpolator();
    private static Interpolator collapseInterpolator = new DecelerateInterpolator(3f);
    private static Interpolator alphaExpandInterpolator = new DecelerateInterpolator();

    private boolean isExpanded =true;

    public ProfileFloatingMenu(Context context) {
        this(context, null);
    }

    public ProfileFloatingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        createRootButton(context);
    }

    public ProfileFloatingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createRootButton(context);
    }

    private void createRootButton(Context context) {

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        MenuLayoutParams params = new MenuLayoutParams();
        params.setAnimationsTarget(this);
    }


    @Override
    public void onScrollDown() {
        requestLayout();
        collapse();
    }

    @Override
    public void onScrollUp() {
        requestLayout();
        collapse();
    }

    private class MenuLayoutParams {

        private ObjectAnimator expandDirAnim = new ObjectAnimator();
        private ObjectAnimator expandAlphaAnim = new ObjectAnimator();
        private ObjectAnimator collapseDirAnim = new ObjectAnimator();
        private ObjectAnimator collapseAlphaAnim = new ObjectAnimator();

        private boolean animationsSetToPlay;

        public MenuLayoutParams() {

            expandDirAnim.setInterpolator(expandInterpolator);
            expandAlphaAnim.setInterpolator(alphaExpandInterpolator);
            collapseDirAnim.setInterpolator(collapseInterpolator);
            collapseAlphaAnim.setInterpolator(collapseInterpolator);

            collapseAlphaAnim.setProperty(View.ALPHA);
            collapseAlphaAnim.setFloatValues(1f, 0f);

            expandAlphaAnim.setProperty(View.ALPHA);
            expandAlphaAnim.setFloatValues(0f, 1f);
        }

        public void setAnimationsTarget(View view) {
            collapseAlphaAnim.setTarget(view);
            collapseDirAnim.setTarget(view);
            expandDirAnim.setTarget(view);
            expandAlphaAnim.setTarget(view);

            // Now that the animations have  targets, set them to be played
            if (!animationsSetToPlay) {
                addLayerTypeListener(expandDirAnim, view);
                addLayerTypeListener(collapseDirAnim, view);

                mCollapseAnimation.play(collapseAlphaAnim);
                mCollapseAnimation.play(collapseDirAnim);
                mExpandAnimation.play(expandAlphaAnim);
                mExpandAnimation.play(expandDirAnim);
                animationsSetToPlay = true;
            }
        }

        private void addLayerTypeListener(Animator animator, final View view) {
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setLayerType(LAYER_TYPE_NONE, null);
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    view.setLayerType(LAYER_TYPE_HARDWARE, null);
                }
            });
        }
    }

    public void collapse() {
        collapse(false);
    }

    private void collapse(boolean immediately) {
        if (isExpanded) {
            isExpanded = false;
            mCollapseAnimation.setDuration(immediately ? 0 : ANIMATION_DURATION);
            mCollapseAnimation.start();
            mExpandAnimation.cancel();
        }
    }

    public void toggle() {
        if (isExpanded) {
            collapse();
        } else {
            expand();
        }
    }

    public void expand() {
        if (!isExpanded) {
            isExpanded = true;
            mCollapseAnimation.cancel();
            mExpandAnimation.start();
        }
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public float dp2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }


}
