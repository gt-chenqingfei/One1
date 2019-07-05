package com.oneone.modules.timeline.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.oneone.R;
import com.oneone.framework.ui.utils.ScreenUtil;
import com.oneone.modules.timeline.bean.LikeType;

import java.util.List;

/**
 * @author qingfei.chen
 * @since 2018/6/19.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class LikeTypesView extends RelativeLayout {
    private int[] typeDrawableIds = {
            R.drawable.ic_like_type_smile, R.drawable.ic_like_type_surprise,
            R.drawable.ic_like_type_love_it, R.drawable.ic_like_type_like_it};


    public LikeTypesView(Context context) {
        super(context);
    }

    public LikeTypesView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void bind(List<LikeType> items) {
        if (items == null || items.isEmpty()) {
            return;
        }
        this.removeAllViews();
        int index = 0;
        for (LikeType type : items) {

            ImageView imageView = newImageItem(type, index, items);

            int imageRes = typeDrawableIds[type.getLikeType() - 1];
            imageView.setImageResource(imageRes);
            this.addView(imageView);
            index++;
        }
    }

    private ImageView newImageItem(LikeType type, int position, List<LikeType> items) {
        ImageView ivItem = new ImageView(getContext());
        LayoutParams params;
        if (position == 0) {
            params = getImageViewLayoutParams4FirstPosition();
        } else {
            params = getImageViewLayoutParams(items.get(position - 1).getLikeType());
        }
        ivItem.setLayoutParams(params);
        ivItem.setId(type.getLikeType());
        return ivItem;
    }

    private LayoutParams getImageViewLayoutParams4FirstPosition() {
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        return params;
    }

    private LayoutParams getImageViewLayoutParams(int id) {
        LayoutParams params = getImageViewLayoutParams4FirstPosition();
        params.addRule(RelativeLayout.RIGHT_OF, id);
        params.leftMargin = ScreenUtil.dip2px(-5);
        return params;
    }

}
