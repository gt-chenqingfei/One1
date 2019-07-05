package com.oneone.framework.ui.upgrade.v2.ui;

import android.content.Context;

import com.oneone.framework.ui.R;
import com.oneone.framework.ui.upgrade.core.DownloadManager;
import com.oneone.framework.ui.upgrade.utils.ALog;
import com.oneone.framework.ui.upgrade.v2.AllenVersionChecker;
import com.oneone.framework.ui.upgrade.v2.builder.DownloadBuilder;

import java.io.File;

/**
 * Created by allenliu on 2018/1/18.
 */

public class BuilderHelper {
    private DownloadBuilder builder;
    private Context context;

    public BuilderHelper(Context context, DownloadBuilder builder) {
        this.context = context;
        this.builder = builder;
    }

    /**
     * 验证安装包是否存在，并且在安装成功情况下删除安装包
     */
    public void checkAndDeleteAPK() {
        //判断versioncode与当前版本不一样的apk是否存在，存在删除安装包
        try {
            String downloadPath = builder.getDownloadAPKPath() + context.getString(R.string.versionchecklib_download_apkname, context.getPackageName());
            if (!DownloadManager.checkAPKIsExists(context, downloadPath)) {
                ALog.e("删除本地apk");
                new File(downloadPath).delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkForceUpdate() {
        if (builder.getForceUpdateListener() != null) {
            builder.getForceUpdateListener().onShouldForceUpdate();
            AllenVersionChecker.getInstance().cancelAllMission(context);
        }
    }
}
