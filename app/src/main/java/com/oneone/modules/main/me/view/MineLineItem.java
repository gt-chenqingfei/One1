package com.oneone.modules.main.me.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.framework.ui.BaseView;
import com.oneone.framework.ui.annotation.LayoutResource;

import butterknife.BindView;

/**
 * Created by here on 18/4/12.
 */

@LayoutResource(R.layout.view_mine_line_item_view)
public class MineLineItem extends BaseView {
    @BindView(R.id.item_tag_iv)
    ImageView mIvItemIcon;
    @BindView(R.id.item_desc_tv)
    TextView mTvItemName;
    @BindView(R.id.item_notice_content_tv)
    TextView mTvItemValue;

    public MineLineItem(Context context) {
        super(context);
    }

    public MineLineItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onViewCreated() {

    }

    public void setItemName(int itemNameRes) {
        mTvItemName.setText(itemNameRes);
    }

    public void setItemValue(int itemValueRes) {
        mTvItemValue.setText(itemValueRes);
    }

    public void setItemIcon(int iconRes) {
        mIvItemIcon.setImageResource(iconRes);
    }

    public void setItemName(String itemName) {
        mTvItemName.setText(itemName);
    }

    public void setItemValue(String itemValue) {
        mTvItemValue.setText(itemValue);
    }

    public void setItem(String itemName, String itemValue, int itemIcon) {
        setItemName(itemName);
        setItemValue(itemValue);
        setItemIcon(itemIcon);
    }

    public void setItem(int itemNameRes, String itemValue, int itemIcon) {
        setItemName(itemNameRes);
        setItemValue(itemValue);
        setItemIcon(itemIcon);
    }

    public void setUnreadCount(int count) {
        if (count > 0) {
            mTvItemValue.setBackgroundResource(com.oneone.framework.ui.R.drawable.shape_message_unread_bg);
            mTvItemValue.setTextColor(getResources().getColor(R.color.color_white));
            mTvItemValue.setText(count + "");
            mTvItemValue.setVisibility(View.VISIBLE);
        } else {
            mTvItemValue.setVisibility(View.GONE);
        }
    }

}
