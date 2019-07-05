package com.oneone.framework.ui.upgrade.v2.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.oneone.framework.ui.R;
import com.oneone.framework.ui.upgrade.utils.ALog;
import com.oneone.framework.ui.upgrade.utils.AllenEventBusUtil;
import com.oneone.framework.ui.upgrade.utils.AppUtils;
import com.oneone.framework.ui.upgrade.v2.AllenVersionChecker;
import com.oneone.framework.ui.upgrade.v2.builder.UIData;
import com.oneone.framework.ui.upgrade.v2.eventbus.AllenEventType;
import com.oneone.framework.ui.upgrade.v2.eventbus.CommonEvent;

import java.io.File;

public class UIActivity extends AllenBaseActivity implements DialogInterface.OnCancelListener {
    private Dialog versionDialog;
    private boolean isDestroy = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ALog.e("version activity create");

        showVersionDialog();

    }

    @Override
    protected void onDestroy() {
        isDestroy = true;
        ALog.e("version activity destroy");
        super.onDestroy();
    }

    @Override
    public void receiveEvent(CommonEvent commonEvent) {
        switch (commonEvent.getEventType()) {
            case AllenEventType.SHOW_VERSION_DIALOG:
                showVersionDialog();
                break;
        }

    }

    @Override
    public void showDefaultDialog() {
        UIData uiData = VersionService.builder.getVersionBundle();
        String title = "", content = "";
        if (uiData != null) {
            title = uiData.getTitle();
            content = uiData.getContent();
        }
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this).setTitle(title).setMessage(content).setPositiveButton(getString(R.string.versionchecklib_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dealVersionDialogCommit();

            }
        });
        if (getVersionBuilder().getForceUpdateListener() == null) {
            alertBuilder.setNegativeButton(getString(R.string.versionchecklib_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onCancel(versionDialog);
                }
            });
            alertBuilder.setCancelable(false);


        } else {
            alertBuilder.setCancelable(false);
        }
        versionDialog = alertBuilder.create();
        versionDialog.setCanceledOnTouchOutside(false);
        versionDialog.show();
    }

    @Override
    public void showCustomDialog() {
        ALog.e("show customization dialog");
        versionDialog = getVersionBuilder().getCustomVersionDialogListener().getCustomVersionDialog(this, VersionService.builder.getVersionBundle());
        try {
            //自定义dialog，commit button 必须存在
            final View view = versionDialog.findViewById(R.id.versionchecklib_version_dialog_commit);
            if (view != null) {
                ALog.e("view not null");

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ALog.e("click");
                        dealVersionDialogCommit();
                    }
                });

            } else {
                throwWrongIdsException();
            }
            //如果有取消按钮，id也必须对应
            View cancelView = versionDialog.findViewById(R.id.versionchecklib_version_dialog_cancel);
            if (cancelView != null) {
                cancelView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onCancel(versionDialog);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            throwWrongIdsException();
        }
        versionDialog.show();
    }

    private void showVersionDialog() {
        if (getVersionBuilder().getCustomVersionDialogListener() != null) {
            showCustomDialog();
        } else {
            showDefaultDialog();
        }
        versionDialog.setOnCancelListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (versionDialog != null && versionDialog.isShowing())
            versionDialog.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (versionDialog != null && !versionDialog.isShowing())
            versionDialog.show();
    }

    private void dealVersionDialogCommit() {
        //如果是静默下载直接安装
        if (getVersionBuilder().isSilentDownload()) {
            String downloadPath = getVersionBuilder().getDownloadAPKPath() + getString(R.string.versionchecklib_download_apkname, getPackageName());
            AppUtils.installApk(this, new File(downloadPath));
            checkForceUpdate();
            //否定开始下载
        } else {
            AllenEventBusUtil.sendEventBus(AllenEventType.START_DOWNLOAD_APK);
        }
        finish();
    }


    @Override
    public void onCancel(DialogInterface dialogInterface) {
        cancelHandler();
        checkForceUpdate();
        AllenVersionChecker.getInstance().cancelAllMission(this);
        finish();
    }

}
