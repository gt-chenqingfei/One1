package com.oneone.modules.timeline.view;

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
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;
import com.melnykov.fab.ScrollDirectionListener;
import com.oneone.R;
import com.oneone.framework.ui.utils.ScreenUtil;


/**
 * <p>
 * 发布动态 悬浮按钮
 * <ul>
 * <li>
 * {@link #toggle()} 切换菜单的展开收缩状态<br/>
 * </li>
 * <li>
 * </li>
 * </ul>
 * </p>
 * <p>
 * 可以通过调用 {@link #addItem(View)} 和 {@link #addItem(View)} 来动态增减按钮数量。
 *
 * @author qingfei.chen
 * </p>
 */
public class TimeLineFloatingMenu extends ViewGroup implements ScrollDirectionListener {

    public final static int FLOATING_DIRECTION_UP = 0;
    public final static int FLOATING_DIRECTION_LEFT = 1;
    public final static int FLOATING_DIRECTION_DOWN = 2;
    public final static int FLOATING_DIRECTION_RIGHT = 3;

    private static final int SHADOW_OFFSET = 20;
    private int floatingShadowSize = SHADOW_OFFSET;

    private FloatingActionButton floatingButton;
    private AnimatorSet showAnimation;
    private AnimatorSet hideAnimation;

    private float buttonInterval;
    private ColorStateList backgroundTint;
    private boolean isExpanded;
    private boolean isHided;
    private int floatingDirection;

    public TimeLineFloatingMenu(Context context) {
        this(context, null);
    }

    public TimeLineFloatingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        initMenu(context, attrs);
    }

    public TimeLineFloatingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initMenu(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TimeLineFloatingMenu(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initMenu(context, attrs);
    }

    private void initMenu(Context context, AttributeSet attrs) {
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.FloatingMenu, 0, 0);
        buttonInterval = attr.getDimension(R.styleable.FloatingMenu_fm_button_interval, 4);
        buttonInterval = ScreenUtil.dip2px(buttonInterval);
        backgroundTint = attr.getColorStateList(R.styleable.FloatingMenu_fm_backgroundTint);
        floatingDirection = attr.getInteger(R.styleable.FloatingMenu_fm_floating_direction, 0);
        attr.recycle();
        createRootButton(context);
        addScrollAnimation();
    }

    private void addScrollAnimation() {
        showAnimation = new AnimatorSet().setDuration(ANIMATION_DURATION);
        showAnimation.play(ObjectAnimator.ofFloat(this, View.ALPHA, 0f, 1f));
        showAnimation.setInterpolator(alphaExpandInterpolator);
        showAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                setVisibility(VISIBLE);
            }
        });

        hideAnimation = new AnimatorSet().setDuration(ANIMATION_DURATION);
        hideAnimation.play(ObjectAnimator.ofFloat(this, View.ALPHA, 1f, 0f));
        hideAnimation.setInterpolator(alphaExpandInterpolator);
        hideAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setVisibility(INVISIBLE);
            }
        });
    }

    private void createRootButton(Context context) {
        floatingButton = new FloatingActionButton(context);
        floatingButton.setColorNormal(context.getResources().getColor(R.color.color_7879FF));
        floatingButton.setColorPressed(context.getResources().getColor(R.color.color_685AED));
        floatingButton.setShadow(true);
        floatingButton.setType(FloatingActionButton.TYPE_NORMAL);
        floatingButton.setImageResource(R.drawable.ic_timeline_new);
        floatingButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        addView(floatingButton, super.generateDefaultLayoutParams());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        switch (floatingDirection) {
            case FLOATING_DIRECTION_UP:
            case FLOATING_DIRECTION_DOWN:
                onMeasureVerticalDirection();
                break;
            case FLOATING_DIRECTION_LEFT:
            case FLOATING_DIRECTION_RIGHT:
                onMeasureHorizontalDirection();
                break;
        }
    }

    /**
     * 计算竖向排列时需要的大小
     */
    private void onMeasureVerticalDirection() {
        int width = 0;
        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE)
                continue;
            width = Math.max(child.getMeasuredWidth(), width);
            height += child.getMeasuredHeight();
        }
        width += SHADOW_OFFSET * 2;
        height += SHADOW_OFFSET * 2;
        height += buttonInterval * (getChildCount() - 1);
        height = adjustShootLength(height);
        setMeasuredDimension(width, height);
    }

    /**
     * 计算横向排列时需要的大小
     */
    private void onMeasureHorizontalDirection() {
        int width = 0;
        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE)
                continue;
            height = Math.max(child.getMeasuredHeight(), height);
            width += child.getMeasuredWidth();
        }
        width += SHADOW_OFFSET * 2;
        height += SHADOW_OFFSET * 2;
        width += buttonInterval * (getChildCount() - 1);
        width = adjustShootLength(width);
        setMeasuredDimension(width, height);
    }

    private int adjustShootLength(int length) {
        return length * 12 / 10;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        switch (floatingDirection) {
            case FLOATING_DIRECTION_UP:
                onUpDirectionLayout(l, t, r, b);
                break;
            case FLOATING_DIRECTION_DOWN:
                onDownDirectionLayout(l, t, r, b);
                break;
            case FLOATING_DIRECTION_LEFT:
                onLeftDirectionLayout(l, t, r, b);
                break;
            case FLOATING_DIRECTION_RIGHT:
                onRightDirectionLayout(l, t, r, b);
                break;
        }
    }

    /**
     * 摆放朝上展开方向的子控件位置
     */
    private void onUpDirectionLayout(int l, int t, int r, int b) {
        int centerX = (r - l) / 2;
        int offsetY = b - t - floatingShadowSize;

        for (int i = getChildCount() - 1; i >= 0; i--) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE)
                continue;
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            child.layout(centerX - width / 2, offsetY - height, centerX + width / 2, offsetY);

            //排除根按钮，添加动画
            if (i != getChildCount() - 1) {
                float collapsedTranslation = b - t - SHADOW_OFFSET - offsetY;
                float expandedTranslation = 0f;
                child.setTranslationY(isExpanded ? expandedTranslation : collapsedTranslation);
                child.setAlpha(isExpanded ? 1f : 0f);

                MenuLayoutParams params = (MenuLayoutParams) child.getLayoutParams();
                params.collapseDirAnim.setFloatValues(expandedTranslation, collapsedTranslation);
                params.expandDirAnim.setFloatValues(collapsedTranslation, expandedTranslation);
                params.collapseDirAnim.setProperty(View.TRANSLATION_Y);
                params.expandDirAnim.setProperty(View.TRANSLATION_Y);
                params.setAnimationsTarget(child);
            }
            offsetY -= height + buttonInterval;
        }
    }

    /**
     * 摆放朝下展开方向的子控件位置
     */
    private void onDownDirectionLayout(int l, int t, int r, int b) {
        int centerX = (r - l) / 2;
        int offsetY = SHADOW_OFFSET;
        View rootView = getChildAt(getChildCount() - 1);
        rootView.layout(centerX - rootView.getMeasuredWidth() / 2, offsetY, centerX + rootView.getMeasuredWidth() / 2, offsetY + rootView.getMeasuredHeight());
        offsetY += rootView.getMeasuredHeight() + buttonInterval;

        for (int i = 0; i < getChildCount() - 1; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE)
                continue;
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            child.layout(centerX - width / 2, offsetY, centerX + width / 2, offsetY + height);

            float collapsedTranslation = -offsetY;
            float expandedTranslation = 0f;
            child.setTranslationY(isExpanded ? expandedTranslation : collapsedTranslation);
            child.setAlpha(isExpanded ? 1f : 0f);

            MenuLayoutParams params = (MenuLayoutParams) child.getLayoutParams();
            params.collapseDirAnim.setFloatValues(expandedTranslation, collapsedTranslation);
            params.expandDirAnim.setFloatValues(collapsedTranslation, expandedTranslation);
            params.collapseDirAnim.setProperty(View.TRANSLATION_Y);
            params.expandDirAnim.setProperty(View.TRANSLATION_Y);
            params.setAnimationsTarget(child);

            offsetY += height + buttonInterval;
        }
    }

    /**
     * 摆放朝左展开方向的子控件位置
     */
    private void onLeftDirectionLayout(int l, int t, int r, int b) {
        int centerY = (b - t) / 2;
        int offsetX = r - l - SHADOW_OFFSET;

        for (int i = getChildCount() - 1; i >= 0; i--) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE)
                continue;
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            child.layout(offsetX - width, centerY - height / 2, offsetX, centerY + height / 2);

            //排除根按钮，添加动画
            if (i != getChildCount() - 1) {
                float collapsedTranslation = r - l - SHADOW_OFFSET - offsetX;
                float expandedTranslation = 0f;
                child.setTranslationX(isExpanded ? expandedTranslation : collapsedTranslation);
                child.setAlpha(isExpanded ? 1f : 0f);

                MenuLayoutParams params = (MenuLayoutParams) child.getLayoutParams();
                params.collapseDirAnim.setFloatValues(expandedTranslation, collapsedTranslation);
                params.expandDirAnim.setFloatValues(collapsedTranslation, expandedTranslation);
                params.collapseDirAnim.setProperty(View.TRANSLATION_X);
                params.expandDirAnim.setProperty(View.TRANSLATION_X);
                params.setAnimationsTarget(child);
            }
            offsetX -= width + buttonInterval;
        }
    }

    /**
     * 摆放朝右展开方向的子控件位置
     */
    private void onRightDirectionLayout(int l, int t, int r, int b) {
        int centerY = (b - t) / 2;
        int offsetX = SHADOW_OFFSET;
        View rootView = getChildAt(getChildCount() - 1);
        rootView.layout(offsetX, centerY - rootView.getMeasuredHeight() / 2, offsetX + rootView.getMeasuredWidth(), centerY + rootView.getMeasuredHeight() / 2);
        offsetX += rootView.getMeasuredWidth() + buttonInterval;

        for (int i = 0; i < getChildCount() - 1; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE)
                continue;
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            child.layout(offsetX, centerY - height / 2, offsetX + width, centerY + height / 2);

            float collapsedTranslation = -offsetX;
            float expandedTranslation = 0f;
            child.setTranslationX(isExpanded ? expandedTranslation : collapsedTranslation);
            child.setAlpha(isExpanded ? 1f : 0f);

            MenuLayoutParams params = (MenuLayoutParams) child.getLayoutParams();
            params.collapseDirAnim.setFloatValues(expandedTranslation, collapsedTranslation);
            params.expandDirAnim.setFloatValues(collapsedTranslation, expandedTranslation);
            params.collapseDirAnim.setProperty(View.TRANSLATION_X);
            params.expandDirAnim.setProperty(View.TRANSLATION_X);
            params.setAnimationsTarget(child);

            offsetX += width + buttonInterval;
        }
    }

    public void setButtonInterval(float buttonInterval) {
        this.buttonInterval = buttonInterval;
        requestLayout();
    }

    public void addItem(View item) {
        addView(item, 0);
        requestLayout();
    }

    public void addItemAtLast(View item) {
        addView(item, getChildCount() - 1);
        requestLayout();
    }

    public void removeItem(View item) {
        removeView(item);
        requestLayout();
    }

    public void setFloatingDirection(int floatingDirection) {
        this.floatingDirection = floatingDirection;
        postInvalidate();
    }

    public void attachToListView(ListView recyclerView) {
        floatingButton.attachToListView(recyclerView);
    }

    public void attachToListView(@NonNull AbsListView listView) {
        attachToListView(listView, null, null);
    }

    public void attachToListView(@NonNull AbsListView listView,
                                 ScrollDirectionListener scrollDirectionListener) {
        attachToListView(listView, scrollDirectionListener, null);
    }

    public void attachToRecyclerView(@NonNull RecyclerView recyclerView) {
        attachToRecyclerView(recyclerView, this, null);
    }

    public void attachToRecyclerView(@NonNull RecyclerView recyclerView,
                                     ScrollDirectionListener scrollDirectionListener) {
        attachToRecyclerView(recyclerView, scrollDirectionListener, null);
    }

    public void attachToScrollView(@NonNull ObservableScrollView scrollView) {
        attachToScrollView(scrollView, null, null);
    }

    public void attachToScrollView(@NonNull ObservableScrollView scrollView,
                                   ScrollDirectionListener scrollDirectionListener) {
        attachToScrollView(scrollView, scrollDirectionListener, null);
    }

    public void attachToListView(@NonNull AbsListView listView,
                                 ScrollDirectionListener scrollDirectionListener,
                                 AbsListView.OnScrollListener onScrollListener) {
        floatingButton.attachToListView(listView, scrollDirectionListener, onScrollListener);
    }

    public void attachToRecyclerView(@NonNull RecyclerView recyclerView,
                                     ScrollDirectionListener scrollDirectionlistener,
                                     RecyclerView.OnScrollListener onScrollListener) {
        floatingButton.attachToRecyclerView(recyclerView, scrollDirectionlistener, onScrollListener);
    }

    public void attachToScrollView(@NonNull ObservableScrollView scrollView,
                                   ScrollDirectionListener scrollDirectionListener,
                                   ObservableScrollView.OnScrollChangedListener onScrollChangedListener) {
        floatingButton.attachToScrollView(scrollView, scrollDirectionListener, onScrollChangedListener);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MenuLayoutParams(super.generateDefaultLayoutParams());
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MenuLayoutParams(super.generateLayoutParams(attrs));
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MenuLayoutParams(super.generateLayoutParams(p));
    }

    private static final int ANIMATION_DURATION = 300;
    private static final float COLLAPSED_PLUS_ROTATION = 0f;
    private static final float EXPANDED_PLUS_ROTATION = 90f + 45f;

    private AnimatorSet mExpandAnimation = new AnimatorSet().setDuration(ANIMATION_DURATION);
    private AnimatorSet mCollapseAnimation = new AnimatorSet().setDuration(ANIMATION_DURATION);

    private static Interpolator expandInterpolator = new OvershootInterpolator();
    private static Interpolator collapseInterpolator = new DecelerateInterpolator(3f);
    private static Interpolator alphaExpandInterpolator = new DecelerateInterpolator();

    @Override
    public void onScrollDown() {
        floatingShadowSize = SHADOW_OFFSET;
        requestLayout();
        collapse();
    }

    @Override
    public void onScrollUp() {
        floatingShadowSize = 0;
        requestLayout();
        collapse();
    }

    private class MenuLayoutParams extends LayoutParams {

        private ObjectAnimator expandDirAnim = new ObjectAnimator();
        private ObjectAnimator expandAlphaAnim = new ObjectAnimator();
        private ObjectAnimator collapseDirAnim = new ObjectAnimator();
        private ObjectAnimator collapseAlphaAnim = new ObjectAnimator();

        private boolean animationsSetToPlay;

        public MenuLayoutParams(LayoutParams source) {
            super(source);

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

            // Now that the animations have targets, set them to be played
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

    public void collapseImmediately() {
        collapse(true);
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

    public void hide() {
        if (!isHided) {
            isHided = true;
            hideAnimation.start();
            showAnimation.cancel();
        }
    }

    public void show() {
        if (isHided) {
            isHided = false;
            showAnimation.start();
            hideAnimation.cancel();
        }
    }

    public float dp2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }


}
