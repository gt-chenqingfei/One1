package com.oneone.modules.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.oneone.BaseActivity;
import com.oneone.Constants;
import com.oneone.R;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.modules.user.UserSP;

import butterknife.OnClick;

/**
 * @author qingfei.chen
 * @since 2018/7/24.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@LayoutResource(R.layout.activity_main_guide)
public class GuideActivity extends BaseActivity {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, GuideActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserSP.putAndApply(this, Constants.PREF.IS_FIRST_REGISTER, false);
    }

    @OnClick(R.id.guide_floor)
    public void onClick(View view) {
        this.finish();
    }
}
