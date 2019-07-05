package com.oneone.modules.setting.views;

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
@LayoutResource(R.layout.view_setting_line_item_view)
public class SettingLineItem extends BaseView {
    @BindView(R.id.item_tag_iv)
    ImageView itemTagIv;
    @BindView(R.id.item_desc_tv)
    TextView itemDescTv;
    @BindView(R.id.item_notice_content_tv)
    TextView itemNoticeContentTv;


    public static final int TYPE_OPEN_SINGLE = 1;
    public static final int TYPE_ABOUT_ONEONE = 2;
    public static final int TYPE_LOGOUT = 3;

    private int itemType;

    public void setItemType(int type) {
        this.itemType = type;

        switch (itemType) {
            case TYPE_OPEN_SINGLE:
                itemTagIv.setBackgroundResource(R.drawable.view_setting_line_item_open_single_bg);
                itemDescTv.setText(R.string.str_setting_page_open_oneone_item_text);
                break;
            case TYPE_ABOUT_ONEONE:
                itemTagIv.setBackgroundResource(R.drawable.view_setting_line_item_about_oneone_bg);
                itemDescTv.setText(R.string.str_setting_page_about_oneone_item_text);
                break;
            case TYPE_LOGOUT:
                itemTagIv.setBackgroundResource(R.drawable.view_setting_line_item_logout_bg);
                itemDescTv.setText(R.string.str_setting_page_logout_item_text);
                break;
        }
    }

    private SettingLineItemListener listener;

    public void setListener(SettingLineItemListener listener) {
        this.listener = listener;
    }

    public interface SettingLineItemListener {
        void itemOpenSingleClick();

        void itemAboutOneoneClick();

        void itemLogoutClick();
    }

    public SettingLineItem(Context context) {
        super(context);
    }

    public SettingLineItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onViewCreated() {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (itemType) {
                    case TYPE_OPEN_SINGLE:
                        listener.itemOpenSingleClick();
                        break;
                    case TYPE_ABOUT_ONEONE:
                        listener.itemAboutOneoneClick();
                        break;
                    case TYPE_LOGOUT:
                        listener.itemLogoutClick();
                        break;
                }
            }
        });
    }
}
