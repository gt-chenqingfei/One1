package com.oneone.modules.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.oneone.AppInitializer;
import com.oneone.BaseActivity;
import com.oneone.R;
import com.oneone.api.constants.Role;
import com.oneone.framework.android.utils.ActivityUtils;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;
import com.oneone.HereSingletonFactory;
import com.oneone.modules.entry.activity.LoginActivity;
import com.oneone.modules.msg.IMManager;
import com.oneone.modules.entry.activity.OpenSingleRelationActivity;
import com.oneone.modules.setting.views.SettingLineItem;
import com.oneone.modules.user.HereUser;
import com.oneone.utils.AppUtil;

import butterknife.BindView;

/**
 * Created by here on 18/4/12.
 */

@ToolbarResource(title = R.string.str_setting_page_title_text)
@LayoutResource(R.layout.activity_setting_page)
public class SettingActivity extends BaseActivity implements SettingLineItem.SettingLineItemListener {
    @BindView(R.id.setting_open_oneone_item)
    SettingLineItem openOneoneItem;
    @BindView(R.id.setting_about_oneone_item)
    SettingLineItem aboutOneoneItem;
    @BindView(R.id.setting_logout_item)
    SettingLineItem logoutItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openOneoneItem.setListener(this);
        aboutOneoneItem.setListener(this);
        logoutItem.setListener(this);

        openOneoneItem.setItemType(SettingLineItem.TYPE_OPEN_SINGLE);
        aboutOneoneItem.setItemType(SettingLineItem.TYPE_ABOUT_ONEONE);
        logoutItem.setItemType(SettingLineItem.TYPE_LOGOUT);

        if (HereUser.getInstance().getUserInfo().getRole() == Role.MATCHER) {
            openOneoneItem.setVisibility(View.VISIBLE);
        } else {
            openOneoneItem.setVisibility(View.GONE);
        }
    }


    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, SettingActivity.class));
    }

    @Override
    public void itemOpenSingleClick() {
        OpenSingleRelationActivity.startActivity(this, true);
    }

    @Override
    public void itemAboutOneoneClick() {
        AboutActivity.startActivity(SettingActivity.this);
    }

    @Override
    public void itemLogoutClick() {
        AppUtil.quit(this);
    }
}
