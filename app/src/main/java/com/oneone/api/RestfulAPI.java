package com.oneone.api;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;


import com.oneone.Constants;
import com.oneone.framework.android.utils.ChannelUtil;
import com.oneone.framework.android.utils.PackageUtils;
import com.oneone.framework.android.utils.TelephonyUtils;
import com.oneone.modules.user.HereUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class RestfulAPI {
    public static final String BASE_API_URL = Constants.URL.API_URL() + "/api";

    public RestfulAPI() {
    }

    public static Map<String, String> getParams(Context context) {
        final Map<String, String> params = new TreeMap<>();
        params.put("systemName", Build.FINGERPRINT);
        params.put("systemVersion", Build.VERSION.RELEASE);
        params.put("appVersion", PackageUtils.getVersionName(context));
        params.put("deviceModel", Build.MODEL);
        params.put("platform", "Android");
        params.put("language", Locale.getDefault().getLanguage());
        params.put("channel", ChannelUtil.getChannel(context));
        params.put("systemVersionCode", PackageUtils.getVersionCode(context) + "");
        if (HereUser.getInstance() != null) {
            params.put("Authorization", "Bearer " + HereUser.getInstance().getToken());
        }
        return params;
    }


    /**
     * 获取application中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值 ， 或者异常)，则返回值为空
     */
    private static String getAppMetaData(Context ctx, String key) {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(),
                        PackageManager.GET_META_DATA);
                if (applicationInfo != null && applicationInfo.metaData != null) {
                    resultData = applicationInfo.metaData.getString(key);
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return resultData;
    }

    /**
     * 设置webkit cookie
     */
    public static void cookieSync() {
        android.webkit.CookieManager cookieManager = android.webkit.CookieManager.getInstance();
//        if (AVUser.getCurrentUser() != null) {
//            StringBuilder cookieBuilder = new StringBuilder("sessionid=");
//            cookieBuilder.append(AVUser.getCurrentUser().getSessionToken()).append(";");
//            cookieBuilder.append("domain=.speedx.com;");
//            cookieManager.setCookie(".speedx.com", cookieBuilder.toString());
//        }
    }
}
