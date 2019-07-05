package com.oneone.framework.ui.upgrade.v2.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.oneone.framework.ui.R;
import com.oneone.framework.ui.upgrade.utils.ALog;
import com.oneone.framework.ui.upgrade.utils.AllenEventBusUtil;
import com.oneone.framework.ui.upgrade.v2.AllenVersionChecker;
import com.oneone.framework.ui.upgrade.v2.eventbus.AllenEventType;

public class DownloadFailedActivity extends AllenBaseActivity implements DialogInterface.OnCancelListener {
    private Dialog downloadFailedDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showDowloadFailedDialog();
    }

    @Override
    public void showDefaultDialog() {
        downloadFailedDialog = new AlertDialog.Builder(this).setMessage(getString(R.string.versionchecklib_download_fail_retry)).setPositiveButton(getString(R.string.versionchecklib_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                retryDownload();
            }
        }).setNegativeButton(getString(R.string.versionchecklib_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onCancel(downloadFailedDialog);
            }
        }).create();
        downloadFailedDialog.setCanceledOnTouchOutside(false);
        downloadFailedDialog.setCancelable(true);
        downloadFailedDialog.show();
    }

    @Override
    public void showCustomDialog() {
        downloadFailedDialog = getVersionBuilder().getCustomDownloadFailedListener().getCustomDownloadFailed(this,getVersionBuilder().getVersionBundle());
        View retryView = downloadFailedDialog.findViewById(0);
        if (retryView != null) {
            retryView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    retryDownload();
                }
            });
        }
        View cancelView = downloadFailedDialog.findViewById(0);
        if (cancelView != null) {
            cancelView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onCancel(downloadFailedDialog);
                }
            });
        }
        downloadFailedDialog.show();
    }

    private void showDowloadFailedDialog() {
        AllenEventBusUtil.sendEventBus(AllenEventType.CLOSE_DOWNLOADING_ACTIVITY);

        if (getVersionBuilder().getCustomDownloadFailedListener() != null) {
            ALog.e("show customization failed dialog");
            showCustomDialog();
        } else {
            ALog.e("show default failed dialog");
            showDefaultDialog();
        }
        downloadFailedDialog.setOnCancelListener(this);
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        ALog.e("on cancel" +
                "");
        cancelHandler();
        checkForceUpdate();
        AllenVersionChecker.getInstance().cancelAllMission(this);
        finish();
    }

    private void retryDownload() {
        AllenEventBusUtil.sendEventBus(AllenEventType.START_DOWNLOAD_APK);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(downloadFailedDialog!=null&&downloadFailedDialog.isShowing())
            downloadFailedDialog.dismiss();
//        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(downloadFailedDialog!=null&&!downloadFailedDialog.isShowing())
            downloadFailedDialog.show();
    }
}
