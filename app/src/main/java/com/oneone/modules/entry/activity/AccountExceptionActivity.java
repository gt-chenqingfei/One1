package com.oneone.modules.entry.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneone.BaseActivity;
import com.oneone.R;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.modules.user.HereUser;
import com.oneone.utils.ImageHelper;

import butterknife.BindView;

/**
 * Created by here on 18/4/9.
 */

@LayoutResource(R.layout.activity_my_account_exception_page)
public class AccountExceptionActivity extends BaseActivity {
    @BindView(R.id.user_photo_iv)
    ImageView userPhotoIv;
    @BindView(R.id.user_name_tv)
    TextView userNameTv;
    @BindView(R.id.connect_custom_service_btn)
    Button connectCustomServiceBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        startActivity(new Intent(AccountExceptionActivity.this, AccountOtherExceptionActivity.class));
    }

    public void initView() {
        connectCustomServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        if (HereUser.getInstance() != null) {
            ImageHelper.displayAvatar(this, userPhotoIv, HereUser.getInstance().getUserInfo().getAvatar());
        }
    }
}
