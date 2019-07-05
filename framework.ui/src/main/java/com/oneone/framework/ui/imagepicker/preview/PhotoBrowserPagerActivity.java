package com.oneone.framework.ui.imagepicker.preview;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;
import com.oneone.framework.android.utils.Toasts;
import com.oneone.framework.ui.AbstractBaseActivity;
import com.oneone.framework.ui.IPreViewMenuListener;
import com.oneone.framework.ui.R;
import com.oneone.framework.ui.dialog.SheetDialog;
import com.oneone.framework.ui.dialog.SheetItem;
import com.oneone.framework.ui.ibase.IPresenter;
import com.oneone.framework.ui.imagepicker.view.OptionsPopupDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


/**
 * @author qingfei.chen
 * @since 17/12/20.
 * Copyright © 2017 HereTech Technology Co.,Ltd. All rights reserved.
 */
public class PhotoBrowserPagerActivity extends AbstractBaseActivity
        implements PhotoViewClickListener, ViewPager.OnPageChangeListener {

    public static final String EXTRA_THUMBNAIL_PATH = "extra_thumbnail_path";
    public static final String EXTRA_PATH = "extra_path";
    public static final String EXTRA_POSITION = "extra_position";
    public static final String EXTRA_IS_SHOW_NUMBERS = "extra_is_show_numbers";
    public static final String EXTRA_CUSTOM_MESSAGE = "extra_custom_message";

    public static final String EXTRA_CUSTOM_WIDTH = "extra_custom_width";
    public static final String EXTRA_CUSTOM_HEIGHT = "extra_custom_height";
    public static final String EXTRA_CUSTOM_LOCATION_X = "extra_custom_location_x";
    public static final String EXTRA_CUSTOM_LOCATION_Y = "extra_custom_location_y";

    // 图片保存路径
    private String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures/";

    RelativeLayout mPreviewRelativeLayout;
    ViewPager mPreViewPager;
    TextView mPreViewSize;
    Toolbar mToolbar;
    PhotoBrowserPageAdapter mImagePageAdapter;

    /* 缩略图集合 和 全部的集合 */
    ArrayList<String> thumbnailPaths = new ArrayList<>();
    ArrayList<String> normalPaths = new ArrayList<>();
    /* 当前的位置 */
    int firstEnterPosition;
    /* 所有的数量 */
    int allPathsSize;

    /* 是否展示数字显示 */
    boolean isShowNumber = true;

    /* 弹出菜单的点击事件 */
    String[] menuTitleArray;
    protected static IPreViewMenuListener preViewMenuListener;
    Parcelable mCustomMessageContent;

    private int imageViewWidth;
    private int imageViewHeight;
    private int imageViewLocationX;
    private int imageViewLocationY;
    private int mLeft;
    private int mTop;
    private float mScaleX;
    private float mScaleY;
    private Drawable mBackground;
    private int currentSelectedPosition = -1;

    private Thread thread;

    @Override
    public IPresenter onCreatePresenter() {
        return null;
    }

    public static void launch(Context mContext, ArrayList<String> thumbnailPaths,
                              ArrayList<String> normalPaths, int currentPosition,
                              View imageView) {
        preViewMenuListener = null;
        int[] location = new int[2];
        imageView.getLocationOnScreen(location);
        Intent intent = new Intent(mContext, PhotoBrowserPagerActivity.class);
        intent.putExtra(EXTRA_THUMBNAIL_PATH, thumbnailPaths);
        intent.putExtra(EXTRA_PATH, normalPaths);
        intent.putExtra(EXTRA_IS_SHOW_NUMBERS, true);
        intent.putExtra(EXTRA_POSITION, currentPosition);
        intent.putExtra(EXTRA_CUSTOM_LOCATION_X, location[0]);
        intent.putExtra(EXTRA_CUSTOM_LOCATION_Y, location[1]);
        intent.putExtra(EXTRA_CUSTOM_WIDTH, imageView.getWidth());
        intent.putExtra(EXTRA_CUSTOM_HEIGHT, imageView.getHeight());
        mContext.startActivity(intent);
        ((Activity) mContext).overridePendingTransition(0, 0);
    }

    private void launch(Context mContext, String normalPath,
                        String thumbnailPath, IPreViewMenuListener iPreViewMenuListener) {
        Intent intent = new Intent(mContext, PhotoBrowserPagerActivity.class);
        ArrayList<String> thumbnailPaths = new ArrayList<>();
        ArrayList<String> normalPaths = new ArrayList<>();
        thumbnailPaths.add(thumbnailPath);
        preViewMenuListener = iPreViewMenuListener;
        normalPaths.add(normalPath);
        intent.putExtra(EXTRA_THUMBNAIL_PATH, normalPaths);
        intent.putExtra(EXTRA_PATH, thumbnailPaths);
        intent.putExtra(EXTRA_IS_SHOW_NUMBERS, true);
        mContext.startActivity(intent);
    }


    private void launch(Context mContext, String normalPath,
                        String thumbnailPath, IPreViewMenuListener iPreViewMenuListener,
                        Parcelable customMessageContent) {
        Intent intent = new Intent(mContext, PhotoBrowserPagerActivity.class);
        ArrayList<String> thumbnailPaths = new ArrayList<>();
        ArrayList<String> normalPaths = new ArrayList<>();
        thumbnailPaths.add(thumbnailPath);
        preViewMenuListener = iPreViewMenuListener;
        normalPaths.add(normalPath);
        intent.putExtra(EXTRA_THUMBNAIL_PATH, normalPaths);
        intent.putExtra(EXTRA_CUSTOM_MESSAGE, customMessageContent);
        intent.putExtra(EXTRA_PATH, thumbnailPaths);
        intent.putExtra(EXTRA_IS_SHOW_NUMBERS, false);
        mContext.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_activity_normal);

        initView();
        initPresenter();
        parseIntent();
        setEnterAnimation(mPreviewRelativeLayout);
    }

    private void initView() {
        mPreViewPager = (ViewPager) findView(R.id.preview_view_pager);
        mPreviewRelativeLayout = (RelativeLayout) findView(R.id.rl_preview_layout);
        mPreViewSize = (TextView) findView(R.id.preview_view_size);
        mToolbar = (Toolbar) findView(R.id.widget_toolbar);
        setToolBar(mToolbar, "",
                getResources().getDrawable(R.drawable.preview_icon_wo_cloes));

        mImagePageAdapter = new PhotoBrowserPageAdapter(this, thumbnailPaths, normalPaths);
        mPreViewPager.setAdapter(mImagePageAdapter);
        mToolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        menuTitleArray = new String[]{getResources().getString(R.string.str_menu_save_local)};
    }

    private void initPresenter() {
        mImagePageAdapter.setPhotoViewClickListener(this);
        mPreViewPager.addOnPageChangeListener(this);
    }

    private void parseIntent() {
        if (getIntent() != null) {
            thumbnailPaths = getIntent().getStringArrayListExtra(EXTRA_THUMBNAIL_PATH);
            normalPaths = getIntent().getStringArrayListExtra(EXTRA_PATH);
            /* 是否展示文字信息 */
            isShowNumber = getIntent().getBooleanExtra(EXTRA_IS_SHOW_NUMBERS, true);
            if (getIntent().hasExtra(EXTRA_POSITION))
                firstEnterPosition = getIntent().getIntExtra(EXTRA_POSITION, 0);
            if (normalPaths != null) {
                allPathsSize = normalPaths.size();
            }
            if (getIntent().hasExtra(EXTRA_CUSTOM_MESSAGE)) {
                mCustomMessageContent = getIntent().getParcelableExtra(EXTRA_CUSTOM_MESSAGE);
            }

            imageViewWidth = getIntent().getIntExtra(EXTRA_CUSTOM_WIDTH, 0);
            imageViewHeight = getIntent().getIntExtra(EXTRA_CUSTOM_HEIGHT, 0);
            imageViewLocationX = getIntent().getIntExtra(EXTRA_CUSTOM_LOCATION_X, 0);
            imageViewLocationY = getIntent().getIntExtra(EXTRA_CUSTOM_LOCATION_Y, 0);
        }
        refreshUi();
    }

    private void refreshUi() {
        mImagePageAdapter.setNormalPaths(normalPaths, thumbnailPaths);
        mPreViewPager.setCurrentItem(firstEnterPosition);

        /* 是否显示文字数据 */
        if (isShowNumber) {
            if (normalPaths.size() > 1)
                mPreViewSize.setText(String.format(getResources().getString(R.string.str_album_preview_normal_size),
                        String.valueOf(firstEnterPosition + 1), String.valueOf(allPathsSize)));
            else
                mPreViewSize.setVisibility(View.GONE);
        } else {
            mPreViewSize.setVisibility(View.GONE);
        }
        // 关闭按钮默认不显示
        mToolbar.setVisibility(View.GONE);
    }

    private void setEnterAnimation(final View view) {
        mBackground = new ColorDrawable(Color.BLACK);
        view.setBackground(mBackground);

        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                view.getViewTreeObserver().removeOnPreDrawListener(this);
                int location[] = new int[2];
                view.getLocationOnScreen(location);
                mLeft = imageViewLocationX - location[0];
                mTop = imageViewLocationY - location[1];
                mScaleX = imageViewWidth * 1.0f / view.getWidth();
                mScaleY = imageViewHeight * 1.0f / view.getHeight();
                enterAnimation(view);
                return true;
            }
        });
    }

    private void enterAnimation(View view) {
        view.setPivotX(0);
        view.setPivotY(0);
        view.setScaleX(mScaleX);
        view.setScaleY(mScaleY);
        view.setTranslationX(mLeft);
        view.setTranslationY(mTop);
        view.animate().scaleX(1).scaleY(1).translationX(0).translationY(0).
                setDuration(300).setInterpolator(new DecelerateInterpolator()).start();
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(mBackground, "alpha", 0, 255);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.setDuration(300);
        objectAnimator.start();
    }

    private void exitAnimation() {
        exitAnimation(new Runnable() {
            @Override
            public void run() {
                finish();
                overridePendingTransition(0, 0);
            }
        });
    }

    private void exitAnimation(Runnable runnable) {
//        if (currentSelectedPosition == firstEnterPosition || currentSelectedPosition == -1) {
        if (normalPaths.size() == 0) {// size 不会 <=0，不设置返回效果
            mPreviewRelativeLayout.setPivotX(0);
            mPreviewRelativeLayout.setPivotY(0);
            // 有返回效果
            mPreviewRelativeLayout.animate().scaleX(mScaleX).scaleY(mScaleY).translationX(mLeft).translationY(mTop).
                    withEndAction(runnable).setDuration(100).setInterpolator(new DecelerateInterpolator()).start();
            ObjectAnimator objectAnimator = ObjectAnimator.ofInt(mBackground, "alpha", 100, 0);
            objectAnimator.setInterpolator(new DecelerateInterpolator());
            objectAnimator.setDuration(100);
            objectAnimator.start();
        } else {
            // 没有返回效果
            mPreviewRelativeLayout.animate().withEndAction(runnable).setDuration(100).
                    setInterpolator(new DecelerateInterpolator()).start();
            ObjectAnimator objectAnimatorImageView = ObjectAnimator.ofInt(mImagePageAdapter.getImageView(), "alpha", 250, 250);
            objectAnimatorImageView.setInterpolator(new DecelerateInterpolator());
            objectAnimatorImageView.setDuration(100);
            objectAnimatorImageView.start();
        }
    }

    @Override
    public void OnPhotoTapListener(View view) {
        exitAnimation();
    }

    @Override
    public void OnPhotoLongClickListener(View view, int position) {
        if (position == RecyclerView.NO_POSITION) {
            return;
        }
        final String normalPath = normalPaths.get(position);
        savePicture2LocalPath(normalPath);

        if (preViewMenuListener == null) {
            return;
        }
        OptionsPopupDialog.newInstance(view.getContext(),
                menuTitleArray)
                .setOptionsPopupDialogListener(new OptionsPopupDialog.OnOptionsItemClickedListener() {
                    @Override
                    public void onOptionsItemClicked(int which) {
                        switch (which) {
                            /* 保存到本地 */
                            case 0:
                                if (preViewMenuListener != null) {
                                    preViewMenuListener.onSaveImageToAlbum(normalPath);
                                }
                                break;
                        }
                    }
                }).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        this.currentSelectedPosition = position;
        /* 是否显示文字数据 */
        if (isShowNumber)
            mPreViewSize.setText(String.format(getResources().getString(R.string.str_album_preview_normal_size),
                    String.valueOf(position + 1), String.valueOf(allPathsSize)));
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (preViewMenuListener != null) {
            preViewMenuListener.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        preViewMenuListener = null;
        if (thread != null) {
            thread = null;
        }
    }

    @Override
    public void onBackPressed() {
        exitAnimation();
    }

    private void savePicture2LocalPath(final String picturePicUrl) {
        final Intent intentBroadcast = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        String[] menuArray = new String[]{getResources().getString(R.string.picture_save)};
        SheetDialog menuMoreDialog = new SheetDialog(this, menuArray, 0, new SheetDialog.OnSheetItemClickListener() {
            @Override
            public void onItemClick(SheetItem item, int id) {
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        InputStream inputStream = null;
                        FileOutputStream fileOutputStream = null;
                        try {
                            FutureTarget<File> fileFutureTarget = Glide.with(PhotoBrowserPagerActivity.this).
                                    load(picturePicUrl).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
                            File file = fileFutureTarget.get();
                            String absolutePath = file.getAbsolutePath();
                            String[] pathName = absolutePath.split("/");
                            String pathPicName = getResources().getString(R.string.app_name) + "-" + pathName[pathName.length - 1];

                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inJustDecodeBounds = true;
                            BitmapFactory.decodeFile(absolutePath, options);
                            String type = options.outMimeType;
                            if (TextUtils.isEmpty(type)) {
                                type = ".jpg";
                            } else {
                                String[] split = type.split("/");
                                type = split[split.length - 1];
                            }

                            String imagePath = path + pathPicName + "." + type;
                            File filePath = new File(path);
                            if (!filePath.exists()) filePath.mkdirs();
                            for (File files : filePath.listFiles()) {
                                if (files.isFile()) {
                                    if (files.getCanonicalPath().equals(new File(imagePath).getCanonicalPath())) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toasts.show(PhotoBrowserPagerActivity.this, getResources().getString(R.string.picture_save_complete));
                                            }
                                        });
                                        return;
                                    }
                                }
                            }

                            File oldPath = new File(absolutePath);
                            if (!oldPath.exists()) return;
                            inputStream = new FileInputStream(oldPath);
                            fileOutputStream = new FileOutputStream(imagePath);
                            byte[] bytes = new byte[1024];
                            int lin = -1;
                            while ((lin = inputStream.read(bytes)) != -1) {
                                fileOutputStream.write(bytes, 0, lin);
                            }
                            fileOutputStream.flush();

                            intentBroadcast.setData(Uri.fromFile(new File(imagePath)));
                            sendBroadcast(intentBroadcast);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toasts.show(PhotoBrowserPagerActivity.this, getResources().getString(R.string.picture_save_complete));
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (inputStream != null) {
                                    inputStream.close();
                                }
                                if (fileOutputStream != null) {
                                    fileOutputStream.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                thread.start();
            }
        }, 0);
        menuMoreDialog.show();
    }

}
