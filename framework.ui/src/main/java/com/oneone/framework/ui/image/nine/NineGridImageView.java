package com.oneone.framework.ui.image.nine;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.oneone.framework.ui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jaeger on 16/2/24.
 * <p>
 * Email: chjie.jaeger@gamil.com
 * GitHub: https://github.com/laobie
 */
public class NineGridImageView<T> extends ViewGroup {
    public final static int STYLE_GRID = 0;     // 宫格布局
    public final static int STYLE_FILL = 1;     // 全填充布局
    ///////////////////////////////////////////////////////////////////////////
    // 跨行跨列的类型
    ///////////////////////////////////////////////////////////////////////////
    public final static int NO_SPAN = 0;         // 不跨行也不跨列
    public final static int TOP_COL_SPAN = 2;     // 首行跨列
    public final static int BOTTOM_COL_SPAN = 3;  // 末行跨列
    public final static int LEFT_ROW_SPAN = 4;    // 首列跨行

    private int mRowCount;                      // 行数
    private int mColumnCount;                   // 列数

    private int mMaxSize;                       // 最大图片数
    private int mShowStyle;                     // 显示风格
    private int mGap;                           // 宫格间距
    private int mSingleImgSize;                 // 单张图片时的尺寸
    private int mGridSize;                      // 宫格大小,即图片大小
    private int mSpanType;                      // 跨行跨列的类型

    private int totalCount = 0;

    private List<T> mImgDataList;

    private NineGridImageViewAdapter<T> mAdapter;
    private boolean forceChange = false;

    public NineGridImageView(Context context) {
        this(context, null);
    }

    public NineGridImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NineGridImageView);
        this.mGap = (int) typedArray.getDimension(R.styleable.NineGridImageView_NineGridImageViewImgGap, 0);
        this.mSingleImgSize = typedArray.getDimensionPixelSize(R.styleable.NineGridImageView_NineGridImageViewSingleImgSize, -1);
        this.mShowStyle = typedArray.getInt(R.styleable.NineGridImageView_NineGridImageViewShowStyle, STYLE_GRID);
        this.mMaxSize = typedArray.getInt(R.styleable.NineGridImageView_NineGridImageViewMaxSize, 9);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int totalWidth = width - getPaddingLeft() - getPaddingRight();
        if (mImgDataList != null && mImgDataList.size() > 0) {
            if (mImgDataList.size() == 1 && mSingleImgSize != -1) {
                mGridSize = mSingleImgSize > totalWidth ? totalWidth : mSingleImgSize;
            } else {
                mGridSize = (totalWidth - mGap * (mColumnCount - 1)) / mColumnCount;
            }
            height = mGridSize * mRowCount + mGap * (mRowCount - 1) + getPaddingTop() + getPaddingBottom();

            int showChildrenCount = getNeedShowCount(mImgDataList.size());
            switch (showChildrenCount) {
                case 5:
                    height = measure4FiveChildViewTopColSpan(totalWidth);
                    break;
            }
        }
        setMeasuredDimension(width, height);
    }

    /**
     * 针对5个child view 大小都要方形显示
     *
     * @param totalWidth total width
     * @return after measure height
     */
    private int measure4FiveChildViewTopColSpan(int totalWidth) {
        int height = mGridSize * mRowCount + mGap * (mRowCount - 1) + getPaddingTop() + getPaddingBottom();

        int showChildrenCount = getNeedShowCount(mImgDataList.size());
        //对不跨行不跨列的进行排版布局,单张或者2张默认进行普通排版
        if (mSpanType == TOP_COL_SPAN || showChildrenCount >= 5) {
            int twoLineHeight = (totalWidth - mGridSize * 2 + mGap) / 2;
            height = mGridSize * mRowCount + mGap * (mRowCount - 1) + getPaddingTop() + getPaddingBottom() - twoLineHeight;
        }
        return height;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layoutChildrenView(changed || forceChange);
        forceChange = false;
    }

    /**
     * 根据照片数量和span类型来对子视图进行动态排版布局
     */
    private void layoutChildrenView(boolean changed) {
        if (mImgDataList == null) return;
        int showChildrenCount = getNeedShowCount(mImgDataList.size());
        //对不跨行不跨列的进行排版布局,单张或者2张默认进行普通排版
        if (mSpanType == NO_SPAN || showChildrenCount <= 2) {
            layout4NoSpanChildrenView(showChildrenCount, changed);
            return;
        }
        switch (showChildrenCount) {
            case 3:
                layout4ThreeChildrenView(showChildrenCount, changed);
                break;
            case 4:
                layout4FourChildrenView(showChildrenCount, changed);
                break;
            case 5:
                layout4FiveChildrenView(showChildrenCount, changed);
                break;
            case 6:
                layout4SixChildrenView(showChildrenCount, changed);
                break;
            default:
                layout4NoSpanChildrenView(showChildrenCount, changed);
                break;
        }
    }

    private void layout4NoSpanChildrenView(int childrenCount, boolean changed) {
        if (childrenCount <= 0) return;
        int row, column, left, top, right, bottom;
        for (int i = 0; i < childrenCount; i++) {
            GridImageView childrenView = (GridImageView) getChildAt(i);
            row = i / mColumnCount;
            column = i % mColumnCount;
            left = (mGridSize + mGap) * column + getPaddingLeft();
            top = (mGridSize + mGap) * row + getPaddingTop();
            right = left + mGridSize;
            bottom = top + mGridSize;
            childrenView.layout(left, top, right, bottom);
            if (mAdapter != null) {
                mAdapter.onDisplayImage(getContext(), childrenView, mImgDataList.get(i), i, changed);
            }
        }
    }

    private void layout4ThreeChildrenView(int childrenCount, boolean changed) {
        int left, top, right, bottom;
        for (int i = 0; i < childrenCount; i++) {
            GridImageView childrenView = (GridImageView) getChildAt(i);
            switch (mSpanType) {
                case TOP_COL_SPAN:    //2行2列,首行跨列
                    if (i == 0) {
                        left = getPaddingLeft();
                        top = getPaddingTop();
                        right = left + mGridSize * 2 + mGap;
                        bottom = top + mGridSize;
                    } else if (i == 1) {
                        left = getPaddingLeft();
                        top = getPaddingTop() + mGridSize + mGap;
                        right = left + mGridSize;
                        bottom = top + mGridSize;
                    } else {
                        left = getPaddingLeft() + mGridSize + mGap;
                        top = getPaddingTop() + mGridSize + mGap;
                        right = left + mGridSize;
                        bottom = top + mGridSize;
                    }
                    childrenView.layout(left, top, right, bottom);
                    break;
                case BOTTOM_COL_SPAN: //2行2列,末行跨列
                    if (i == 0) {
                        left = getPaddingLeft();
                        top = getPaddingTop();
                        right = left + mGridSize;
                        bottom = top + mGridSize;
                    } else if (i == 1) {
                        left = getPaddingLeft() + mGridSize + mGap;
                        top = getPaddingTop();
                        right = left + mGridSize;
                        bottom = top + mGridSize;
                    } else {
                        left = getPaddingLeft();
                        top = getPaddingTop() + mGridSize + mGap;
                        right = left + mGridSize * 2 + mGap;
                        bottom = top + mGridSize;
                    }
                    childrenView.layout(left, top, right, bottom);
                    break;
                case LEFT_ROW_SPAN:   //2行2列,首列跨行
                    if (i == 0) {
                        left = getPaddingLeft();
                        top = getPaddingTop();
                        right = left + mGridSize;
                        bottom = top + mGridSize * 2 + mGap;
                    } else if (i == 1) {
                        left = getPaddingLeft() + mGridSize + mGap;
                        top = getPaddingTop();
                        right = left + mGridSize;
                        bottom = top + mGridSize;
                    } else {
                        left = getPaddingLeft() + mGridSize + mGap;
                        top = getPaddingTop() + mGridSize + mGap;
                        right = left + mGridSize;
                        bottom = top + mGridSize;
                    }
                    childrenView.layout(left, top, right, bottom);
                    break;
                default:
                    break;
            }
            if (mAdapter != null) {
                mAdapter.onDisplayImage(getContext(), childrenView, mImgDataList.get(i), i, changed);
            }
        }
    }

    private void layout4FourChildrenView(int childrenCount, boolean changed) {
        int left, top, right, bottom;
        for (int i = 0; i < childrenCount; i++) {
            GridImageView childrenView = (GridImageView) getChildAt(i);
            switch (mSpanType) {
                case TOP_COL_SPAN:    //3行3列,首行跨2行3列
                    if (i == 0) {
                        left = getPaddingLeft();
                        top = getPaddingTop();
                        right = left + mGridSize * 3 + mGap * 2;
                        bottom = top + mGridSize * 2 + mGap;
                    } else if (i == 1) {
                        left = getPaddingLeft();
                        top = getPaddingTop() + mGridSize * 2 + mGap * 2;
                        right = left + mGridSize;
                        bottom = top + mGridSize;
                    } else if (i == 2) {
                        left = getPaddingLeft() + mGridSize + mGap;
                        top = getPaddingTop() + mGridSize * 2 + mGap * 2;
                        right = left + mGridSize;
                        bottom = top + mGridSize;
                    } else {
                        left = getPaddingLeft() + mGridSize * 2 + mGap * 2;
                        top = getPaddingTop() + mGridSize * 2 + mGap * 2;
                        right = left + mGridSize;
                        bottom = top + mGridSize;
                    }
                    childrenView.layout(left, top, right, bottom);
                    break;
                case BOTTOM_COL_SPAN: //3行3列,末行跨2行3列
                    if (i == 0) {
                        left = getPaddingLeft();
                        top = getPaddingTop();
                        right = left + mGridSize;
                        bottom = top + mGridSize;
                    } else if (i == 1) {
                        left = getPaddingLeft() + mGridSize + mGap;
                        top = getPaddingTop();
                        right = left + mGridSize;
                        bottom = top + mGridSize;
                    } else if (i == 2) {
                        left = getPaddingLeft() + mGridSize * 2 + mGap * 2;
                        top = getPaddingTop();
                        right = left + mGridSize;
                        bottom = top + mGridSize;
                    } else {
                        left = getPaddingLeft();
                        top = getPaddingTop() + mGridSize + mGap;
                        right = left + mGridSize * 3 + mGap * 2;
                        bottom = top + mGridSize * 2 + mGap;
                    }
                    childrenView.layout(left, top, right, bottom);
                    break;
                case LEFT_ROW_SPAN:   //3行3列,首列跨3行2列
                    if (i == 0) {
                        left = getPaddingLeft();
                        top = getPaddingTop();
                        right = left + mGridSize * 2 + mGap;
                        bottom = top + mGridSize * 3 + mGap * 2;
                    } else if (i == 1) {
                        left = getPaddingLeft() + mGridSize * 2 + mGap * 2;
                        top = getPaddingTop();
                        right = left + mGridSize;
                        bottom = top + mGridSize;
                    } else if (i == 2) {
                        left = getPaddingLeft() + mGridSize * 2 + mGap * 2;
                        top = getPaddingTop() + mGridSize + mGap;
                        right = left + mGridSize;
                        bottom = top + mGridSize;
                    } else {
                        left = getPaddingLeft() + mGridSize * 2 + mGap * 2;
                        top = getPaddingTop() + mGridSize * 2 + mGap * 2;
                        right = left + mGridSize;
                        bottom = top + mGridSize;
                    }
                    childrenView.layout(left, top, right, bottom);
                    break;
                default:
                    break;
            }
            if (mAdapter != null) {
                mAdapter.onDisplayImage(getContext(), childrenView, mImgDataList.get(i), i, changed);
            }
        }
    }

    private void layout4FiveChildrenView(int childrenCount, boolean changed) {
        int left, top, right, bottom;
        for (int i = 0; i < childrenCount; i++) {
            GridImageView childrenView = (GridImageView) getChildAt(i);
            switch (mSpanType) {
                case TOP_COL_SPAN:    //3行3列,首行跨2行,2列跨3列
                    if (i == 0) {
                        left = getPaddingLeft();
                        top = getPaddingTop();
                        right = left + (mGridSize * 3 + mGap) / 2;
                        bottom = top + (mGridSize * 3 + mGap) / 2;
                    } else if (i == 1) {
                        left = getPaddingLeft() + (mGridSize * 3 + mGap) / 2 + mGap;
                        top = getPaddingTop();
                        right = left + (mGridSize * 3 + mGap) / 2;
                        bottom = top + (mGridSize * 3 + mGap) / 2;
                    } else if (i == 2) {
                        left = getPaddingLeft();
                        top = getPaddingTop() + (mGridSize * 3 + mGap) / 2 + mGap;
                        right = left + mGridSize;
                        bottom = top + mGridSize;
                    } else if (i == 3) {
                        left = getPaddingLeft() + mGridSize + mGap;
                        top = getPaddingTop() + (mGridSize * 3 + mGap) / 2 + mGap;
                        right = left + mGridSize;
                        bottom = top + mGridSize;
                    } else {
                        left = getPaddingLeft() + mGridSize * 2 + mGap * 2;
                        top = getPaddingTop() + (mGridSize * 3 + mGap) / 2 + mGap;
                        right = left + mGridSize;
                        bottom = top + mGridSize;
                    }
                    childrenView.layout(left, top, right, bottom);
                    break;
                case BOTTOM_COL_SPAN: //3行3列,末行跨2行,2列跨3列
                    if (i == 0) {
                        left = getPaddingLeft();
                        top = getPaddingTop();
                        right = left + mGridSize;
                        bottom = top + mGridSize;
                    } else if (i == 1) {
                        left = getPaddingLeft() + mGridSize + mGap;
                        top = getPaddingTop();
                        right = left + mGridSize;
                        bottom = top + mGridSize;
                    } else if (i == 2) {
                        left = getPaddingLeft() + mGridSize * 2 + mGap * 2;
                        top = getPaddingTop();
                        right = left + mGridSize;
                        bottom = top + mGridSize;
                    } else if (i == 3) {
                        left = getPaddingLeft();
                        top = getPaddingTop() + mGridSize + mGap;
                        right = left + (mGridSize * 3 + mGap) / 2;
                        bottom = top + mGridSize * 2 + mGap;
                    } else {
                        left = getPaddingLeft() + (mGridSize * 3 + mGap) / 2 + mGap;
                        top = getPaddingTop() + mGridSize + mGap;
                        right = left + (mGridSize * 3 + mGap) / 2;
                        bottom = top + mGridSize * 2 + mGap;
                    }
                    childrenView.layout(left, top, right, bottom);
                    break;
                case LEFT_ROW_SPAN:   //3行3列,2行跨3行，1列
                    if (i == 0) {
                        left = getPaddingLeft();
                        top = getPaddingTop();
                        right = left + mGridSize * 2 + mGap;
                        bottom = top + (mGridSize * 3 + mGap) / 2;
                    } else if (i == 1) {
                        left = getPaddingLeft();
                        top = getPaddingTop() + (mGridSize * 3 + mGap) / 2 + mGap;
                        right = left + mGridSize * 2 + mGap;
                        bottom = top + (mGridSize * 3 + mGap) / 2;
                    } else if (i == 2) {
                        left = getPaddingLeft() + mGridSize * 2 + mGap * 2;
                        top = getPaddingTop();
                        right = left + mGridSize;
                        bottom = top + mGridSize;
                    } else if (i == 3) {
                        left = getPaddingLeft() + mGridSize * 2 + mGap * 2;
                        top = getPaddingTop() + mGridSize + mGap;
                        right = left + mGridSize;
                        bottom = top + mGridSize;
                    } else {
                        left = getPaddingLeft() + mGridSize * 2 + mGap * 2;
                        top = getPaddingTop() + mGridSize * 2 + mGap * 2;
                        right = left + mGridSize;
                        bottom = top + mGridSize;
                    }
                    childrenView.layout(left, top, right, bottom);
                    break;
                default:
                    break;
            }
            if (mAdapter != null) {
                mAdapter.onDisplayImage(getContext(), childrenView, mImgDataList.get(i), i, changed);
            }
        }
    }

    private void layout4SixChildrenView(int childrenCount, boolean changed) {
        int left, top, right, bottom;
        for (int i = 0; i < childrenCount; i++) {
            GridImageView childrenView = (GridImageView) getChildAt(i);
            switch (mSpanType) {
                case TOP_COL_SPAN:    //3行3列,第一张跨2行2列
                    if (i == 0) {
                        left = getPaddingLeft();
                        top = getPaddingTop();
                        right = left + mGridSize * 2 + mGap;
                        bottom = top + mGridSize * 2 + mGap;
                    } else if (i == 1) {
                        left = getPaddingLeft() + mGridSize * 2 + mGap * 2;
                        top = getPaddingTop();
                        right = left + mGridSize;
                        bottom = top + mGridSize;
                    } else if (i == 2) {
                        left = getPaddingLeft() + mGridSize * 2 + mGap * 2;
                        top = getPaddingTop() + mGridSize + mGap;
                        right = left + mGridSize;
                        bottom = top + mGridSize;
                    } else if (i == 3) {
                        left = getPaddingLeft();
                        top = getPaddingTop() + mGridSize * 2 + mGap * 2;
                        right = left + mGridSize;
                        bottom = top + mGridSize;
                    } else if (i == 4) {
                        left = getPaddingLeft() + mGridSize + mGap;
                        top = getPaddingTop() + mGridSize * 2 + mGap * 2;
                        right = left + mGridSize;
                        bottom = top + mGridSize;
                    } else {
                        left = getPaddingLeft() + mGridSize * 2 + mGap * 2;
                        top = getPaddingTop() + mGridSize * 2 + mGap * 2;
                        right = left + mGridSize;
                        bottom = top + mGridSize;
                    }
                    childrenView.layout(left, top, right, bottom);
                    break;
                case BOTTOM_COL_SPAN: //3行3列,第4张跨2行2列
                    if (i == 0) {
                        left = getPaddingLeft();
                        top = getPaddingTop();
                        right = left + mGridSize;
                        bottom = top + mGridSize;
                    } else if (i == 1) {
                        left = getPaddingLeft() + mGridSize + mGap;
                        top = getPaddingTop();
                        right = left + mGridSize;
                        bottom = top + mGridSize;
                    } else if (i == 2) {
                        left = getPaddingLeft() + mGridSize * 2 + mGap * 2;
                        top = getPaddingTop();
                        right = left + mGridSize;
                        bottom = top + mGridSize;
                    } else if (i == 3) {
                        left = getPaddingLeft();
                        top = getPaddingTop() + mGridSize + mGap;
                        right = left + mGridSize * 2 + mGap;
                        bottom = top + mGridSize * 2 + mGap;
                    } else if (i == 4) {
                        left = getPaddingLeft() + mGridSize * 2 + mGap * 2;
                        top = getPaddingTop() + mGridSize + mGap;
                        right = left + mGridSize;
                        bottom = top + mGridSize;
                    } else {
                        left = getPaddingLeft() + mGridSize * 2 + mGap * 2;
                        top = getPaddingTop() + mGridSize * 2 + mGap * 2;
                        right = left + mGridSize;
                        bottom = top + mGridSize;
                    }
                    childrenView.layout(left, top, right, bottom);
                    break;
                case LEFT_ROW_SPAN:   //3行3列,第2张跨2行2列
                    if (i == 0) {
                        left = getPaddingLeft();
                        top = getPaddingTop();
                        right = left + mGridSize;
                        bottom = top + mGridSize;
                    } else if (i == 1) {
                        left = getPaddingLeft() + mGridSize + mGap;
                        top = getPaddingTop();
                        right = left + mGridSize * 2 + mGap;
                        bottom = top + mGridSize * 2 + mGap;
                    } else if (i == 2) {
                        left = getPaddingLeft();
                        top = getPaddingTop() + mGridSize + mGap;
                        right = left + mGridSize;
                        bottom = top + mGridSize;
                    } else if (i == 3) {
                        left = getPaddingLeft();
                        top = getPaddingTop() + mGridSize * 2 + mGap * 2;
                        right = left + mGridSize;
                        bottom = top + mGridSize;
                    } else if (i == 4) {
                        left = getPaddingLeft() + mGridSize + mGap;
                        top = getPaddingTop() + mGridSize * 2 + mGap * 2;
                        right = left + mGridSize;
                        bottom = top + mGridSize;
                    } else {
                        left = getPaddingLeft() + mGridSize * 2 + mGap * 2;
                        top = getPaddingTop() + mGridSize * 2 + mGap * 2;
                        right = left + mGridSize;
                        bottom = top + mGridSize;
                    }
                    childrenView.layout(left, top, right, bottom);
                    break;
                default:
                    break;
            }
            if (mAdapter != null) {
                mAdapter.onDisplayImage(getContext(), childrenView, mImgDataList.get(i), i, changed);
            }
        }
    }

    /**
     * 根据跨行跨列的类型，以及图片数量，来确定单元格的行数和列数
     *
     * @param imagesSize 图片数量
     * @param gridParam  单元格的行数和列数
     */
    private void generateUnitRowAndColumnForSpanType(int imagesSize, int[] gridParam) {
        if (imagesSize <= 2) {
            gridParam[0] = 1;
            gridParam[1] = imagesSize;
        } else if (imagesSize == 3) {
            switch (mSpanType) {
                case TOP_COL_SPAN:    //2行2列,首行跨列
                case BOTTOM_COL_SPAN: //2行2列,末行跨列
                case LEFT_ROW_SPAN:   //2行2列,首列跨行
                    gridParam[0] = 2;
                    gridParam[1] = 2;
                    break;
                case NO_SPAN:    //1行3列
                default:
                    gridParam[0] = 1;
                    gridParam[1] = 3;
                    break;
            }
        } else if (imagesSize <= 6) {
            switch (mSpanType) {
                case TOP_COL_SPAN:    //3行3列,首行跨列
                case BOTTOM_COL_SPAN: //3行3列,末行跨列
                case LEFT_ROW_SPAN:   //3行3列,首列跨行
                    gridParam[0] = 3;
                    gridParam[1] = 3;
                    break;
                case NO_SPAN:    //2行
                default:
                    gridParam[0] = 2;
                    gridParam[1] = imagesSize / 2 + imagesSize % 2;
                    break;
            }
        } else {
            gridParam[0] = imagesSize / 3 + (imagesSize % 3 == 0 ? 0 : 1);
            gridParam[1] = 3;
        }
    }

    /**
     * 设置图片数据
     *
     * @param lists    图片数据集合
     * @param spanType 跨行跨列排版类型
     */
    public void setImagesData(List<T> lists, int spanType, int totalCount) {
        this.totalCount = totalCount;
        if (lists == null || lists.isEmpty()) {
            this.setVisibility(GONE);
            return;
        } else {
            this.setVisibility(VISIBLE);
        }
        this.mSpanType = spanType;
        int newShowCount = getNeedShowCount(lists.size());

        int[] gridParam = calculateGridParam(newShowCount, mShowStyle);
        mRowCount = gridParam[0];
        mColumnCount = gridParam[1];
        mImgDataList = lists;
        forceChange = true;
        removeAllViews();
        for (int i = 0; i < newShowCount; i++) {
            GridImageView iv = getImageView(i);
            if (iv == null) {
                continue;
            }
            addView(iv);
        }
    }

    private int getNeedShowCount(int size) {
        if (mMaxSize > 0 && size > mMaxSize) {
            return mMaxSize;
        } else {
            return size;
        }
    }

    /**
     * 获得 ImageView
     * 保证了 ImageView 的重用
     *
     * @param position 位置
     */
    private GridImageView getImageView(final int position) {
        if (mAdapter != null) {
            GridImageView imageView = mAdapter.generateImageView(getContext(), position, totalCount);
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapter.onItemImageClick(getContext(), (ImageView) v, position, mImgDataList);
                }
            });
            return imageView;
        } else {
            Log.e("NineGirdImageView", "Your must set a NineGridImageViewAdapter for NineGirdImageView");
            return null;
        }
    }

    /**
     * 设置 宫格参数
     *
     * @param imagesSize 图片数量
     * @param showStyle  显示风格
     * @return 宫格参数 gridParam[0] 宫格行数 gridParam[1] 宫格列数
     */
    protected int[] calculateGridParam(int imagesSize, int showStyle) {
        int[] gridParam = new int[2];
        switch (showStyle) {
            case STYLE_FILL:
                generateUnitRowAndColumnForSpanType(imagesSize, gridParam);
                break;
            default:
            case STYLE_GRID:
                gridParam[0] = imagesSize / 3 + (imagesSize % 3 == 0 ? 0 : 1);
                gridParam[1] = 3;
        }
        return gridParam;
    }

    /**
     * 设置适配器
     *
     * @param adapter 适配器
     */
    public void setAdapter(NineGridImageViewAdapter adapter) {
        mAdapter = adapter;
    }

    /**
     * 设置宫格间距
     *
     * @param gap 宫格间距 px
     */
    public void setGap(int gap) {
        mGap = gap;
    }

    /**
     * 设置显示风格
     *
     * @param showStyle 显示风格
     */
    public void setShowStyle(int showStyle) {
        mShowStyle = showStyle;
    }

    /**
     * 设置只有一张图片时的尺寸大小
     *
     * @param singleImgSize 单张图片的尺寸大小
     */
    public void setSingleImgSize(int singleImgSize) {
        mSingleImgSize = singleImgSize;
    }

    /**
     * 设置最大图片数
     *
     * @param maxSize 最大图片数
     */
    public void setMaxSize(int maxSize) {
        mMaxSize = maxSize;
    }

}