package com.oneone.modules.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.oneone.BaseActivity;
import com.oneone.R;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;
import com.oneone.utils.VersionUtil;

import butterknife.BindView;

/**
 * Created by here on 18/4/12.
 */

@ToolbarResource(title = R.string.str_about_oneone_title_text)
@LayoutResource(R.layout.activity_about_oneone_page)
public class AboutActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.version_tv)
    TextView versionTv;
    @BindView(R.id.user_protocol_tv)
    TextView protocolTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    public void initView () {
        versionTv.setText(getResources().getString(R.string.str_about_oneone_version_name_text) + VersionUtil.getLocalVersionName(getActivityContext()));

        protocolTv.setOnClickListener(this);
    }

    public static void startActivity (Context context) {
        context.startActivity(new Intent(context, AboutActivity.class));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_protocol_tv:
                ProtocolActivity.startActivity(this);
                break;
        }
    }
}
