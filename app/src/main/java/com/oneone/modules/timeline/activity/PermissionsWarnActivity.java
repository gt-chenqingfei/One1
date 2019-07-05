package com.oneone.modules.timeline.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.oneone.BaseActivity;
import com.oneone.R;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;
import com.oneone.framework.ui.utils.PermissionsUtil;
import butterknife.BindView;
import butterknife.OnClick;

@ToolbarResource(navigationIcon = R.drawable.ic_btn_back_dark, title = R.string.permission_title, background = R.color.transparent)
@LayoutResource(R.layout.activity_permissions_warn)
public class PermissionsWarnActivity extends BaseActivity {

    public static final String CAMERA = "CAMERA";
    public static final String RECORD_AUDIO = "RECORD_AUDIO";
    public static final String EXTERNAL_STORAGE = "EXTERNAL_STORAGE";
    public static final String ACCESS_FINE_LOCATION = "ACCESS_FINE_LOCATION";
    public static final String ACCESS_COARSE_LOCATION = "ACCESS_COARSE_LOCATION";

    private static final String PERMISSON = "permission";
    private String permission;

    @BindView(R.id.permissions_tv_title)
    TextView permissionTitle;
    @BindView(R.id.permissions_tv_content)
    TextView permissionContent;

    public static void startActivity(Context context, String permission) {
        Intent intent = new Intent(context, PermissionsWarnActivity.class);
        intent.putExtra(PERMISSON, permission);
        context.startActivity(intent);
    }

    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);
        permission = getIntent().getExtras().getString(PERMISSON);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.overridePendingTransition(R.anim.activity_in_from_bottom, R.anim.activity_none);
        if (permission.equals(EXTERNAL_STORAGE)) {
            permissionTitle.setText(getString(R.string.permission_storage_title));
            permissionContent.setText(getString(R.string.permission_storage_content));
        } else if (permission.equals(CAMERA)) {
            permissionTitle.setText(getString(R.string.permission_camera_title));
            permissionContent.setText(getString(R.string.permission_camera_content));
        } else if (permission.equals(RECORD_AUDIO)) {
            permissionTitle.setText(getString(R.string.permission_audio_title));
            permissionContent.setText(getString(R.string.permission_audio_content));
        }
    }

    @OnClick(R.id.permissions_bt_confirm)
    public void confirm() {
        PermissionsUtil.openSystemPermissionsSetting(mContext);
    }

    @Override
    public void finish() {
        super.finish();
        super.overridePendingTransition(R.anim.activity_none, R.anim.activity_out_to_bottom);
    }
}
