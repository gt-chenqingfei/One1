package com.oneone.modules.upgrate;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import com.oneone.R;
import com.oneone.framework.android.environment.ExternalStorage;
import com.oneone.framework.android.environment.StorageType;
import com.oneone.framework.android.utils.AppUtils;
import com.oneone.framework.ui.upgrade.v2.AllenVersionChecker;
import com.oneone.framework.ui.upgrade.v2.builder.DownloadBuilder;
import com.oneone.framework.ui.upgrade.v2.builder.NotificationBuilder;
import com.oneone.framework.ui.upgrade.v2.builder.UIData;
import com.oneone.framework.ui.upgrade.v2.callback.CustomVersionDialogListener;
import com.oneone.framework.ui.upgrade.v2.callback.ForceUpdateListener;
import com.oneone.modules.upgrate.contract.UpgradeContract;
import com.oneone.modules.upgrate.dto.UpgrateDTO;
import com.oneone.modules.upgrate.presenter.UpgradePresenter;
import com.oneone.modules.upgrate.view.CustomUpdateDialog;
import com.oneone.utils.StringUtil;

/**
 * 版本升级工具类
 * <p>
 * Created by ZhaiDongyang on 2018/6/26
 * https://github.com/AlexLiuSheng/CheckVersionLib
 */
public class CheckVersionManager {

    private UpgradeContract.OnUpgrateResultListener mListener;

    private CheckVersionManager() {
    }

    private static class CheckVersionManagerHolder {
        private static final CheckVersionManager instance = new CheckVersionManager();
    }

    public static CheckVersionManager getInstance() {
        return CheckVersionManagerHolder.instance;
    }

    public void checkServerVersion(final Context context) {
        mListener = new UpgradeContract.OnUpgrateResultListener() {
            @Override
            public void onUpgrateResultListener(UpgrateDTO upgrateDTO) {
                String forceUpdateVersion = upgrateDTO.getMinVersion();// 小于等于此版本就强制更新
                String hintUpdateVersion = upgrateDTO.getHintVersion();// 小于等于此版本就提示更新
                if (!StringUtil.isNullOrEmpty(forceUpdateVersion)) {
                    if (AppUtils.needUpdate(forceUpdateVersion, AppUtils.getAppVersionName())) {
                        downloadApp(context, upgrateDTO, true);
                        return;
                    }
                }
                if (!StringUtil.isNullOrEmpty(hintUpdateVersion)) {
                    if (AppUtils.needUpdate(hintUpdateVersion, AppUtils.getAppVersionName())) {
                        downloadApp(context, upgrateDTO, false);
                        return;
                    }
                }
            }
        };
        new UpgradePresenter(context).getUpgrateInfo("Android", mListener);
    }

    private static void downloadApp(final Context context, final UpgrateDTO mUpgrateDTO, final boolean isForceUpdate) {
        String url = mUpgrateDTO.getDownloadUrl();
        // String url = "http://www.wandoujia.com/apps/com.taobao.taobao/binding?source=web_inner_referral_binded";
        if (StringUtil.isNullOrEmpty(url)) {
            return;
        }

        UIData uiData = UIData.create();
        uiData.setTitle(context.getResources().getString(R.string.version_update_new_title));
        uiData.setDownloadUrl(mUpgrateDTO.getDownloadUrl());
        uiData.setContent(mUpgrateDTO.getReleaseNote());

        DownloadBuilder builder = AllenVersionChecker.getInstance().downloadOnly(uiData);
        builder.setNotificationBuilder(
                NotificationBuilder.create()
                        .setRingtone(true)
                        .setIcon(R.drawable.app_logo_icon)
                        .setTicker("OneOne")
                        .setContentTitle("OneOne")
                        .setContentText(context.getString(R.string.versionchecklib_downloading))
        );
        // 默认：/storage/emulated/0/AllenVersionPath/
        builder.setDownloadAPKPath(ExternalStorage.getInstance().getDirectoryByDirType(StorageType.DOWNLOAD_WITH_APP));
        builder.setForceRedownload(true);// 本地有安装包也会下载，测试 apk 和 OneOne 包名不一样，所以这句话现在设置无效
        builder.setCustomVersionDialogListener(new CustomVersionDialogListener() {
            @Override
            public Dialog getCustomVersionDialog(Context context, UIData versionBundle) {
                return new CustomUpdateDialog(context, versionBundle, mUpgrateDTO.getVersionName(), isForceUpdate);
            }
        });

        // 从服务器判断是否是强制升级
        if (isForceUpdate) {
            builder.setForceUpdateListener(new ForceUpdateListener() {
                @Override
                public void onShouldForceUpdate() {
                    // 如果强制更新，安装后程序退出，解决用户只下载不安装的情况
                    ((Activity) context).finish();
                }
            });
            if (builder.getForceUpdateListener() != null) {
                builder.setShowNotification(false);
                builder.setShowDownloadingDialog(true);
            }
        } else {
            builder.setShowNotification(false);// 不显示通知栏
            builder.setShowDownloadingDialog(true);
        }
        builder.excuteMission(context);
    }

}
