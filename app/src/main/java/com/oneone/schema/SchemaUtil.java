package com.oneone.schema;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.netease.nimlib.sdk.NimIntent;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.oneone.Constants;
import com.oneone.modules.msg.IMManager;
import com.oneone.modules.msg.activity.ImTalkActivity;
import com.oneone.modules.msg.activity.SystemMsgWebviewActivity;
import com.oneone.modules.msg.beans.IMUserPrerelation;
import com.oneone.modules.msg.beans.TalkBeans.MyRecentContact;

import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.util.ArrayList;

/**
 * @author qingfei.chen
 * @since 2018/7/9.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class SchemaUtil {
    public static void doRouter(Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Uri uri = Uri.parse(url);

        if (Constants.URL_SCHEMA.SCHEMA.equals(uri.getScheme()) && Constants.URL_SCHEMA.HOST.equals(uri.getHost())) {
            ARouter.getInstance().build(uri).navigation(context);
        } else {
            LoggerFactory.getLogger("UrlSchema").error("This schema is not yet!");
        }
    }

    public static void doFilter(Intent intent, Context context) {
        Uri uri = intent.getData();
        if (uri != null) {
            ARouter.getInstance().build(uri).navigation(context);
        } else {
            ArrayList<IMMessage> list = (ArrayList<IMMessage>) intent.getSerializableExtra(NimIntent.EXTRA_NOTIFY_CONTENT);
            if (list == null || list.isEmpty()) {
                return;
            }
            IMMessage firstMsg = list.get(0);
            MyRecentContact myRecentContact = IMManager.findMyRecentContact(firstMsg.getFromAccount());
            IMUserPrerelation imUserPrerelation = new IMUserPrerelation(false, true, myRecentContact);
            ImTalkActivity.startActivity(context, imUserPrerelation, null);
        }
    }

}
