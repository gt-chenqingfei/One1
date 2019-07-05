package com.oneone.framework.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oneone.framework.android.utils.SoftKeyBoardUtil;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;
import com.oneone.framework.ui.ibase.IBaseView;
import com.oneone.framework.ui.ibase.IPresenter;
import com.oneone.framework.ui.immersionbar.ImmersionBar;
import com.oneone.framework.ui.widget.LoadingDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author qingfei.chen
 * @since 2017/12/11.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */

public abstract class AbstractBaseActivity<P extends IPresenter<V>, V extends IBaseView> extends AppCompatActivity {

    protected P mPresenter;
    protected Dialog mLoadingDialog;
    protected Context mContext;
    private Unbinder mUnBinder;
    private Toolbar mToolbar;
    protected ImmersionBar mImmersionBar;
    private boolean titleAlignCenter = true;
    private int mToolbarBackgroundColor;


    public abstract P onCreatePresenter();

    public void onInitListener() {
    }

    public void handleIntent(Intent intent) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        handleIntent(getIntent());
        mContext = this;
        final Class<?> clazz = getClass();
        final LayoutResource layout = clazz.getAnnotation(LayoutResource.class);

        if (layout != null) {
            ViewGroup contentView = (ViewGroup) LayoutInflater.from(this).inflate(layout.value(), null);
            setContentView(contentView);
            toolbarApply(clazz, contentView);
        }

        mPresenter = onCreatePresenter();
        if (mPresenter != null) {
            mPresenter.onAttachView((V) this);
        }

        mUnBinder = ButterKnife.bind(this);
        if (isImmersionBarEnabled()) {
            initImmersionBar();
        }
        onInitListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        if (mPresenter != null) {
            mPresenter.onDetachView();
            mPresenter = null;
        }
        if (mImmersionBar != null) {
            mImmersionBar.destroy();
        }
        dismissLoading();
    }

    protected void showLoading(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mLoadingDialog == null) {
                    mLoadingDialog = LoadingDialog.createLoadingDialog(mContext, msg);
                }

                mLoadingDialog.setCanceledOnTouchOutside(false);
                if (!mLoadingDialog.isShowing()) {
                    mLoadingDialog.show();
                }
            }
        });
    }

    protected void dismissLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                    mLoadingDialog.dismiss();
                }
            }
        });
    }

    /***
     * 查找id 省的强转了
     *
     * @param resId
     * @param <T>
     * @return
     */
    protected <T> T findView(int resId) {
        return (T) findViewById(resId);
    }

    protected void setToolBar(Toolbar toolbar, String title, Drawable icon) {

        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(icon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void toolbarApply(Class<?> clazz, ViewGroup container) {
        final ToolbarResource toolbarResource = clazz.getAnnotation(ToolbarResource.class);
        if (toolbarResource != null) {
            int toolbarLayout = toolbarResource.layout();
            if (toolbarLayout <= 0) {
                toolbarLayout = R.layout.layout_toolbar;
            }
            View toolbarContainer = LayoutInflater.from(this).inflate(toolbarLayout, container, false);
            if (container instanceof LinearLayout) {
                container.addView(toolbarContainer, 0);
            } else {
                container.addView(toolbarContainer, container.getChildCount());
            }

            titleAlignCenter = toolbarResource.titleAlignCenter();

            mToolbar = toolbarContainer.findViewById(R.id.widget_toolbar);
            mToolbar.setTitle("");

            setTitle(toolbarResource.title());

            if (toolbarResource.subtitle() > 0) {
                setSubTitle(toolbarResource.subtitle());
            }

            int colorIdRes = toolbarResource.background();
            if (colorIdRes > 0) {
                mToolbarBackgroundColor = getResources().getColor(colorIdRes);
                mToolbar.setBackgroundColor(mToolbarBackgroundColor);
            }

            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!onNavigationClick(view)) {
                        SoftKeyBoardUtil.hideSoftInput(AbstractBaseActivity.this);
                        finish();
                    }
                }
            });

            int navigation = toolbarResource.navigationIcon();
            if (navigation == -1) {
                mToolbar.setNavigationIcon(null);
            } else {
                if (navigation == 0) {
                    setNavigation(getResources().getDrawable(R.drawable.ic_btn_back_dark));
                } else {
                    mToolbar.setNavigationIcon(toolbarResource.navigationIcon());
                }
            }

            ImmersionBar.setTitleBar(this, mToolbar);
        }
    }

    public void setNavigation(Drawable drawable) {
        mToolbar.setNavigationIcon(drawable);
    }

    public void setNavigation(@DrawableRes int navigationIcon) {
        Drawable drawable = null;
        drawable = getResources().getDrawable(navigationIcon);
        setNavigation(drawable);
    }

    public void setTitle(String title) {
        if (titleAlignCenter) {
            setMiddleTitle(title);
        } else {
            setLeftTitle(title);
        }
    }

    public TextView getTitleView() {
        if (titleAlignCenter) {
            return getMiddleTitle();
        } else {
            return getLeftTitle();
        }
    }

    public void setTitle(int title) {
        if (titleAlignCenter) {
            setMiddleTitle(title);
        } else {
            setLeftTitle(title);
        }
    }

    public void setTitleString(String title) {
        if (titleAlignCenter) {
            setMiddleTitle(title);
        } else {
            setLeftTitle(title);
        }
    }

    private void setLeftTitle(@StringRes int title) {
        setLeftTitle(getString(title));
    }


    private void setLeftTitle(String title) {
        TextView tvTitle = getLeftTitle();
        if (tvTitle != null) {
            tvTitle.setText(title);
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onTitleClick(view);
                }
            });
        }
    }

    protected void setSubTitle(@StringRes int subTitle) {
        setSubTitle(getString(subTitle));
    }

    protected void setSubTitle(String title) {
        TextView tvTitle = getSubTitleView();
        if (tvTitle != null) {
            tvTitle.setText(title);
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onTitleClick(view);
                }
            });
        }
    }

    private TextView setMiddleTitle(String title) {
        TextView tvTitle = getMiddleTitle();
        if (tvTitle != null) {
            tvTitle.setText(title);
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onMiddleTitleClick(v);
                }
            });
        }
        return tvTitle;
    }

    private TextView setMiddleTitle(@StringRes int title) {
        return setMiddleTitle(getString(title));
    }

    public TextView setRightTextMenu(@StringRes int textRes) {
        return setRightTextMenu(getString(textRes));
    }

    public TextView setRightTextMenu(String menuText) {
        TextView tvMenu = getRightTextMenu();
        if (tvMenu != null) {
            tvMenu.setText(menuText);
            tvMenu.setVisibility(View.VISIBLE);
            tvMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRightTextMenuClick(v);
                }
            });
        }
        return tvMenu;
    }

    public ImageView setRightIconMenu(@DrawableRes int menuRes) {
        return setRightIconMenu(getResources().getDrawable(menuRes));
    }

    public ImageView setRightIconMenu(Drawable menuDrawable) {
        ImageView ivMenu = getRightIconMenu();
        if (ivMenu != null) {
            ivMenu.setVisibility(View.VISIBLE);
            ivMenu.setImageDrawable(menuDrawable);
            ivMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRightIconMenuClick(v);
                }
            });
        }
        return ivMenu;
    }

    public ImageView setRightMostIconMenu(@DrawableRes int menuRes) {
        return setRightMostIconMenu(getResources().getDrawable(menuRes));
    }

    public ImageView setRightMostIconMenu(Drawable menuDrawable) {
        ImageView ivMenu = getRightMostIconMenu();
        if (ivMenu != null) {
            ivMenu.setVisibility(View.VISIBLE);
            ivMenu.setImageDrawable(menuDrawable);
            ivMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRightMostIconMenuClick(v);
                }
            });
        }

        return ivMenu;
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public TextView getLeftTitle() {
        return (TextView) findViewById(R.id.widget_toolbar_title);
    }

    public TextView getSubTitleView() {
        return (TextView) findViewById(R.id.widget_toolbar_subtitle);
    }

    public TextView getMiddleTitle() {
        return (TextView) findViewById(R.id.widget_toolbar_center);
    }

    public TextView getRightTextMenu() {
        return (TextView) findViewById(R.id.widget_toolbar_menu);
    }

    public ImageView getRightIconMenu() {
        return (ImageView) findViewById(R.id.widget_toolbar_menu_image);
    }

    public ImageView getRightMostIconMenu() {
        return (ImageView) findViewById(R.id.widget_toolbar_two_image);
    }


    public void onRightMostIconMenuClick(View view) {
    }

    public void onRightIconMenuClick(View view) {
    }

    public void onRightTextMenuClick(View view) {
    }

    public void onMiddleTitleClick(View view) {
    }

    public void onTitleClick(View view) {
    }

    public boolean onNavigationClick(View view) {

        return false;
    }


    /**
     * Is immersion bar enabled boolean.
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    protected void initImmersionBar() {
        this.mImmersionBar = ImmersionBar.with(this);
        this.mImmersionBar.statusBarDarkFont(true).navigationBarColor(R.color.color_black).init();
    }

    public ImmersionBar getImmersionBar() {
        return mImmersionBar;
    }

    public int getToolbarBackgroundColor() {
        return mToolbarBackgroundColor;
    }
}
