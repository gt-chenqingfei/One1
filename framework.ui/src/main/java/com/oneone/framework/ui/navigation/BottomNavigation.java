package com.oneone.framework.ui.navigation;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.oneone.framework.ui.BaseMainFragment;
import com.oneone.framework.ui.R;
import com.oneone.framework.ui.navigation.events.OnSelectedItemChangeListener;
import com.oneone.framework.ui.navigation.events.OnTabSelectedListener;
import com.oneone.framework.ui.widget.ScrollableViewPager;

import java.util.ArrayList;
import java.util.List;

public class BottomNavigation extends LinearLayout implements OnTabSelectedListener,
        ScrollableViewPager.OnPageChangeListener {

    /**
     * bottom navigationIcon in phone mode, is horizontal and will aligned bottom of activity
     *
     * @see BottomNavigation#mode
     */
    public static final int MODE_PHONE = 0;

    /**
     * bottom navigationIcon in tablet mode, is vertical and will aligned left or right(based on local)
     *
     * @see BottomNavigation#mode
     */
    public static final int MODE_TABLET = 1;

    /**
     * describe default tab item that must be selected by default, it will be selected when method
     * {@link BottomNavigation#setOnTabSelectedListener(OnTabSelectedListener)}  called.
     *
     * @see OnSelectedItemChangeListener
     */
    private int defaultItem = 0;

    /**
     * this variable hold position of selected tab item.
     */
    private int selectedItemPosition = defaultItem;

    /**
     * List of bottom navigationIcon tab items
     */
    List<TabItem> tabItems = new ArrayList<>();

    List<BaseMainFragment> fragItems = new ArrayList<>();

    /**
     * typeface used for tab item labels
     */
    private Typeface typeface;

    /**
     * used for specify how bottom navigationIcon must show to user.
     * <p/>
     * bottom navigationIcon currently has two modes:
     * 1- {@link BottomNavigation#MODE_PHONE}
     * 2- {@link BottomNavigation#MODE_TABLET}
     */
    private int mode = MODE_PHONE;

    /**
     * @see OnTabSelectedListener
     */
    private OnTabSelectedListener onTabSelectedListener;

    private ScrollableViewPager viewPager;

    public BottomNavigation(Context context) {
        super(context);
        if (!isInEditMode()) {
            setup(null);
        }
    }

    public BottomNavigation(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            setup(attrs);
        }
    }

    public BottomNavigation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) {
            setup(attrs);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BottomNavigation(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        if (!isInEditMode()) {
            setup(attrs);
        }
    }

    /**
     * this method setup necessary attributes and behavior of bottom navigationIcon
     *
     * @param attributeSet used for setup xml custom attributes
     */
    private void setup(AttributeSet attributeSet) {
        parseAttributes(attributeSet);
        switch (mode) {
            case MODE_TABLET:
                setOrientation(VERTICAL);
                setGravity(Gravity.CENTER_HORIZONTAL);
                break;
            default:
            case MODE_PHONE:
                setOrientation(HORIZONTAL);
                setGravity(Gravity.LEFT);
                break;
        }
        setMinimumHeight(getContext().getResources().getDimensionPixelSize(R.dimen.bottom_navigation_min_width));
    }

    /**
     * we call {@link #setupChildren()} in this method, because bottom navigationIcon children are drew in this
     * state and aren't null
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setupChildren();
    }

    /**
     * this function is set {@link TabItem}s
     */
    public void setTabItems(List<TabItem> items) {
        if (items == null) {
            return;
        }

        removeAllViews();
        for (TabItem item : items) {
            this.addView(item);
        }
        setupChildren();
        if (viewPager != null) {
            viewPager.setCurrentItem(selectedItemPosition, false);
        }
    }

    public void bindViewPager(ScrollableViewPager viewPager, List<BaseMainFragment> fragItems) {
        this.viewPager = viewPager;
        this.fragItems = fragItems;
        viewPager.setOffscreenPageLimit(fragItems.size());
        this.viewPager.addOnPageChangeListener(this);
    }

    /**
     * this function setup {@link TabItem}s
     */
    private void setupChildren() {
        if (getChildCount() > 0) {
            if (getChildCount() >= 3 && getChildCount() <= 5) {
                for (int i = 0; i < getChildCount(); i++) {
                    if (!(getChildAt(i) instanceof TabItem)) {
                        throw new RuntimeException("Bottom navigationIcon only accept tab item as child.");
                    } else {
                        final TabItem tabItem = (TabItem) getChildAt(i);
                        tabItem.setPosition(i);
                        tabItem.checkParent();
                        tabItems.add(tabItem);
                        tabItem.setOnTabItemClickListener(this);
                    }
                }
            } else {
                throw new RuntimeException("Bottom navigationIcon child count must between 3 to 5 only.");
            }
        } else {
//            throw new RuntimeException("Bottom navigationIcon can't be empty!");
        }
    }

    /**
     * this function used to manage which tab item must selected or which item must deselect
     */
    private void onSelectedItemChanged() {
        for (int i = 0; i < tabItems.size(); i++) {
            if (tabItems.get(i).getPosition() == selectedItemPosition) {
                tabItems.get(i).setSelected(true);
            } else {
                tabItems.get(i).setSelected(false);
            }
        }
    }

    private void onFragmentItemChanged(int position) {
        if (fragItems == null || fragItems.isEmpty()) {
            return;
        }
        fragItems.get(position).onAppear();
        fragItems.get(selectedItemPosition).onDisAppear();
    }

    /**
     * @see OnTabSelectedListener
     */
    @Override
    public void onTabSelected(final int position) {
        if (position != selectedItemPosition) {
            onFragmentItemChanged(position);
            selectedItemPosition = position;
            onSelectedItemChanged();
            if (onTabSelectedListener != null) {
                onTabSelectedListener.onTabSelected(position);
            }
            if (viewPager != null) {
                viewPager.setCurrentItem(position, false);
            }
        }
    }

    @Override
    public void onDoubleTap(int position) {
        if (fragItems == null || fragItems.isEmpty()) {
            return;
        }
        fragItems.get(position).onTabDoubleTap();
    }

    @Override
    public void onPageChange(final int position) {
        onTabSelected(position);
    }

    /**
     * this function get xml custom attributes and parse it to instance variables
     *
     * @param attributeSet used for retrieve custom values
     */
    private void parseAttributes(AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.BottomNavigation);
            try {
                mode = typedArray.getInteger(R.styleable.BottomNavigation_mode, MODE_PHONE);
            } finally {
                typedArray.recycle();
            }
        }
    }

    public void setDefaultItem(int position) {
        this.defaultItem = position;
        this.selectedItemPosition = position;
    }

    public int getDefaultItem() {
        return defaultItem;
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public void setTypeface(final Typeface typeface) {
        this.typeface = typeface;
        for (int i = 0; i < tabItems.size(); i++) {
            final TabItem tabItem = tabItems.get(i);
            tabItem.post(new Runnable() {
                @Override
                public void run() {
                    tabItem.setTypeface(typeface);
                }
            });
        }
    }

    public void setOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener) {
        this.onTabSelectedListener = onTabSelectedListener;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getSelectedItem() {
        return selectedItemPosition;
    }

    public void setSelectedItem(int position) {
        onTabSelected(position);
    }


}
