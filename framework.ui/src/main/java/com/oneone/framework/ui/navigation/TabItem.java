package com.oneone.framework.ui.navigation;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oneone.framework.ui.R;
import com.oneone.framework.ui.navigation.events.OnTabSelectedListener;
import com.oneone.framework.ui.navigation.utils.AnimationHelper;
import com.oneone.framework.ui.navigation.utils.LayoutParamsHelper;
import com.oneone.framework.ui.navigation.utils.Util;

public class TabItem extends FrameLayout {
    private OnTabSelectedListener onTabItemClickListener;
    private int position;
    private String fagmentTag;
    GestureDetector gestureDetector;

    //Attributes
    private String text;
    private String bubble = "20";
    private Drawable selectedTabIcon;
    private int selectedTabTextColor;

    private Drawable unselectedTabIcon;
    private int unselectedTabTextColor;

    //Views
    private TextView tvLabel;
    private TextView tvBubble;
    private ImageView ivIcon;

    private boolean isActive = false;
    private AnimationHelper animationHelper;
    private BottomNavigation bottomNavigation;

    public TabItem(Context context,String fragmentTag) {
        super(context);
        this.fagmentTag = fragmentTag;
        parseCustomAttributes(null);
    }

    public TabItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseCustomAttributes(attrs);
    }

    public TabItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseCustomAttributes(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TabItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        parseCustomAttributes(attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        checkParent();
    }

    public void checkParent() {
        if (getParent() instanceof BottomNavigation) {
            bottomNavigation = (BottomNavigation) getParent();
            setupView();
        } else {
            throw new RuntimeException("TabItem parent must be BottomNavigation!");
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private void setupView() {
        setDefaultLayoutParams();
//        setOnClickListener(this);
        gestureDetector = new GestureDetector(getContext(), new GestureListener());
        if (bottomNavigation.getMode() == BottomNavigation.MODE_PHONE) {
            setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
        } else {
            setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Util.dpToPx(56)));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setForeground(getResources().getDrawable(R.drawable.tab_forground, null));
        }

        animationHelper = new AnimationHelper();

        tvLabel = new TextView(getContext());
        tvLabel.setTextColor(selectedTabTextColor);
        tvLabel.setText(text);
        tvLabel.setGravity(Gravity.CENTER);
        tvLabel.setLayoutParams(LayoutParamsHelper.getTabItemTextLayoutParams());
        tvLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);


        tvBubble = new TextView(getContext());
        tvBubble.setTextColor(getResources().getColor(R.color.color_white));
        tvBubble.setText(bubble);
        tvBubble.setBackground(getResources().getDrawable(R.drawable.shape_message_unread_bg));
        tvBubble.setGravity(Gravity.CENTER);
        tvBubble.setLayoutParams(LayoutParamsHelper.getTabItemTextBubbleLayoutParams());
        tvBubble.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        tvBubble.setVisibility(View.GONE);


        ivIcon = new ImageView(getContext());
        ivIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ivIcon.setImageDrawable(selectedTabIcon);
        ivIcon.setLayoutParams(LayoutParamsHelper.getTabItemIconLayoutParams());

        if (position == bottomNavigation.getDefaultItem()) {
            isActive = true;
            if (unselectedTabIcon == null || unselectedTabTextColor == 0) {
                animationHelper.animateActivate(tvLabel, ivIcon);
            } else {
                animationHelper.animateActivate(tvLabel, ivIcon, selectedTabTextColor, selectedTabTextColor, selectedTabIcon, unselectedTabIcon);
            }
        } else {
            if (unselectedTabIcon == null || unselectedTabTextColor == 0) {
                animationHelper.animateDeactivate(tvLabel, ivIcon);
            } else {
                animationHelper.animateDeactivate(tvLabel, ivIcon, unselectedTabTextColor, selectedTabTextColor, selectedTabIcon, unselectedTabIcon);
            }
        }

        addView(ivIcon);
        addView(tvLabel);
        addView(tvBubble);
    }

    private void parseCustomAttributes(AttributeSet attributeSet) {
        if (attributeSet != null) {
            //get xml attributes
            TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.TabItem, 0, 0);
            try {
                text = typedArray.getString(R.styleable.TabItem_tab_text);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    selectedTabTextColor = typedArray.getColor(R.styleable.TabItem_tab_text_color, getResources().getColor(R.color.color_white, null));
                    unselectedTabTextColor = typedArray.getColor(R.styleable.TabItem_unselected_tab_text_color, 0);
                } else {
                    selectedTabTextColor = typedArray.getColor(R.styleable.TabItem_tab_text_color, getResources().getColor(R.color.color_white));
                    unselectedTabTextColor = typedArray.getColor(R.styleable.TabItem_unselected_tab_text_color, 0);
                }
                selectedTabIcon = typedArray.getDrawable(R.styleable.TabItem_tab_icon);
                unselectedTabIcon = typedArray.getDrawable(R.styleable.TabItem_unselected_tab_icon);
            } finally {
                typedArray.recycle();
            }
        }
    }

    public void setSelected(boolean isActive) {
        if (this.isActive != isActive) {
            notifyChange();
            this.isActive = isActive;
        }
    }

    public void setOnTabItemClickListener(OnTabSelectedListener onTabItemClickListener) {
        this.onTabItemClickListener = onTabItemClickListener;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return this.position;
    }

//    @Override
//    public void onClick(View view) {
//        if (onTabItemClickListener != null) {
//            onTabItemClickListener.onTabSelected(position);
//        }
//    }

    private void notifyChange() {
        if (unselectedTabIcon == null || unselectedTabTextColor == 0) {
            if (isActive) {
                animationHelper.animateDeactivate(tvLabel, ivIcon);
            } else {
                animationHelper.animateActivate(tvLabel, ivIcon);
            }
        } else {
            if (isActive) {
                animationHelper.animateDeactivate(tvLabel, ivIcon, unselectedTabTextColor, selectedTabTextColor, selectedTabIcon, unselectedTabIcon);
            } else {
                animationHelper.animateActivate(tvLabel, ivIcon, unselectedTabTextColor, selectedTabTextColor, selectedTabIcon, unselectedTabIcon);
            }
        }
    }

    public void setTypeface(Typeface typeface) {
        if (tvLabel != null) {
            tvLabel.setTypeface(typeface);
        }
    }

    private void setDefaultLayoutParams() {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = LayoutParams.WRAP_CONTENT;
        layoutParams.width = LayoutParams.MATCH_PARENT;
        setLayoutParams(layoutParams);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getBubble() {
        return bubble;
    }

    public void setBubble(String bubble) {
        this.bubble = bubble;
        if (this.tvBubble == null) {
            return;
        }
        if (!TextUtils.isEmpty(bubble)) {
            this.tvBubble.setText(bubble);
            this.tvBubble.setVisibility(View.VISIBLE);
        } else {
            this.tvBubble.setText("");
            this.tvBubble.setVisibility(View.GONE);
        }
    }

    public void showDot(boolean isShow) {
        this.bubble = bubble;
        if (this.tvBubble == null) {
            return;
        }
        if (isShow) {
            this.tvBubble.setVisibility(View.VISIBLE);
            this.tvBubble.setBackground(null);
            this.tvBubble.setText("");
            this.tvBubble.setCompoundDrawablesRelativeWithIntrinsicBounds(0,R.drawable.dot_red_bg,0,0);
        } else {
            this.tvBubble.setVisibility(View.GONE);
        }

    }

    public String getFagmentTag() {
        return fagmentTag;
    }

    public Drawable getSelectedTabIcon() {
        return selectedTabIcon;
    }

    public void setSelectedTabIcon(Drawable selectedTabIcon) {
        this.selectedTabIcon = selectedTabIcon;
    }

    public int getSelectedTabTextColor() {
        return selectedTabTextColor;
    }

    public void setSelectedTabTextColor(int selectedTabTextColor) {
        this.selectedTabTextColor = selectedTabTextColor;
    }

    public Drawable getUnselectedTabIcon() {
        return unselectedTabIcon;
    }

    public void setUnselectedTabIcon(Drawable unselectedTabIcon) {
        this.unselectedTabIcon = unselectedTabIcon;
    }

    public int getUnselectedTabTextColor() {
        return unselectedTabTextColor;
    }

    public void setUnselectedTabTextColor(int unselectedTabTextColor) {
        this.unselectedTabTextColor = unselectedTabTextColor;
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            if (onTabItemClickListener != null) {
                onTabItemClickListener.onTabSelected(position);
            }
            return true;
        }

        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if(position == bottomNavigation.getSelectedItem()){
                if (onTabItemClickListener != null) {
                    onTabItemClickListener.onDoubleTap(position);
                }
            }
            return true;
        }

    }
}
