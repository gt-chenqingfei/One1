package com.oneone.support.push;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.gson.Gson;
import com.oneone.R;
import com.oneone.framework.android.utils.ChannelUtil;
import com.oneone.modules.support.model.SupportModel;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author qingfei.chen
 * @since 2018/7/11.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class PushHandler extends UmengMessageHandler implements IUmengRegisterCallback {
    private static Logger logger = LoggerFactory.getLogger("PushHandler");
    private static final String UPDATE_STATUS_ACTION = "com.umeng.message.example.action.UPDATE_STATUS";
    private static Handler handler;
    private static Context mContext;

    public static void init(final Context context) {
        mContext = context;
        logger.error("initPush");
        UMConfigure.setLogEnabled(true);
        String channel = ChannelUtil.getChannel(context);
        UMConfigure.init(context, "5b39a599f43e4808c30001ff", channel, UMConfigure.DEVICE_TYPE_PHONE, "d17d4e52ddc1554bbb03f70e948fcd28");

        PushAgent mPushAgent = PushAgent.getInstance(context);
        handler = new Handler(context.getMainLooper());

        //sdk开启通知声音
        mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
        // sdk关闭通知声音
        // mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
        // 通知声音由服务端控制
        // mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SERVER);

        // mPushAgent.setNotificationPlayLights(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
        // mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);

        PushHandler messageHandler = new PushHandler();
        mPushAgent.setMessageHandler(messageHandler);

        /**
         * 自定义行为的回调处理，参考文档：高级功能-通知的展示及提醒-自定义通知打开动作
         * UmengNotificationClickHandler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * */
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {

            @Override
            public void launchApp(Context context, UMessage msg) {
                super.launchApp(context, msg);
            }

            @Override
            public void openUrl(Context context, UMessage msg) {

                super.openUrl(context, msg);
            }

            @Override
            public void openActivity(Context context, UMessage msg) {
                super.openActivity(context, msg);
            }

            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
            }
        };
        //使用自定义的NotificationHandler
        mPushAgent.setNotificationClickHandler(notificationClickHandler);

        //注册推送服务 每次调用register都会回调该接口
        mPushAgent.register(messageHandler);
    }

    /**
     * 通知的回调方法（通知送达时会回调）
     */
    @Override
    public void dealWithNotificationMessage(Context context, UMessage msg) {
        //调用super，会展示通知，不调用super，则不展示通知。
        super.dealWithNotificationMessage(context, msg);
    }

    /**
     * 自定义消息的回调方法
     */
    @Override
    public void dealWithCustomMessage(final Context context,  UMessage msg) {
        String url = "";
        try {
            JSONObject jsonObject  = new JSONObject(msg.custom);
            url = jsonObject.optString("linkUrl");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        msg.url = url;
        dealWithNotificationMessage(context,msg);
    }

    /**
     * 自定义通知栏样式的回调方法
     */
    @Override
    public Notification getNotification(Context context, UMessage msg) {
        switch (msg.builder_id) {
            case 1:
                Notification.Builder builder = new Notification.Builder(context);
                RemoteViews myNotificationView = new RemoteViews(context.getPackageName(),
                        R.layout.notification_view);
                myNotificationView.setTextViewText(R.id.notification_title, msg.title);
                myNotificationView.setTextViewText(R.id.notification_text, msg.text);
                myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
                myNotificationView.setImageViewResource(R.id.notification_small_icon,
                        getSmallIconId(context, msg));
                builder.setContent(myNotificationView)
                        .setSmallIcon(getSmallIconId(context, msg))
                        .setTicker(msg.ticker)
                        .setAutoCancel(true);

                return builder.getNotification();
            default:
                //默认为0，若填写的builder_id并不存在，也使用默认。
                return super.getNotification(context, msg);
        }
    }

    @Override
    public void onSuccess(String deviceToken) {
        logger.error("initPush success deviceToken:" + deviceToken);
        mContext.sendBroadcast(new Intent(UPDATE_STATUS_ACTION));
        if(!TextUtils.isEmpty(deviceToken)){
            new SupportModel(mContext).postDeviceInfo(deviceToken, ChannelUtil.getChannel(mContext));
        }
    }

    @Override
    public void onFailure(String s, String s1) {
        logger.error("initPush Failure error:" + s + s1);
        mContext.sendBroadcast(new Intent(UPDATE_STATUS_ACTION));
    }
}
