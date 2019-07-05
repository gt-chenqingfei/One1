package com.oneone.modules.msg.contract;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import com.netease.nimlib.sdk.InvocationFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;
import com.oneone.AppInitializer;
import com.oneone.OneOne;
import com.oneone.R;
import com.oneone.framework.android.utils.ActivityUtils;
import com.oneone.framework.ui.utils.ScreenUtil;
import com.oneone.modules.SplashActivity;
import com.oneone.modules.msg.IMManager;
import com.oneone.modules.msg.activity.ClickNotificationActivity;
import com.oneone.modules.msg.activity.ImTalkActivity;
import com.oneone.modules.msg.beans.GiftProd;
import com.oneone.modules.msg.beans.IMEmoji;
import com.oneone.modules.msg.beans.IMUser;
import com.oneone.modules.msg.beans.TalkBeans.EmojiMessage;
import com.oneone.modules.msg.beans.TalkBeans.GiftMessage;
import com.oneone.modules.msg.beans.TalkBeans.MyMessage;
import com.oneone.modules.msg.beans.TalkBeans.attachment.CustomAttachParser;
import com.oneone.modules.msg.beans.TalkBeans.attachment.CustomAttachmentType;
import com.oneone.modules.msg.beans.TalkBeans.attachment.EmojiAttachment;
import com.oneone.modules.msg.beans.TalkBeans.attachment.GiftAttachment;
import com.oneone.modules.msg.event.IMHistoryMessageEvent;
import com.oneone.modules.msg.event.IMMessageEvent;
import com.oneone.modules.msg.event.ImContactListEvent;
import com.oneone.modules.msg.event.SendMessageEvent;
import com.oneone.modules.user.HereUser;
import com.oneone.utils.AppUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jiguang.imui.commons.models.IMessage;

/**
 * Created by here on 18/5/9.
 */

public class NIMFunctional implements IMFunctionalInterface {
    private IMManager.ManagerListener listener;

    public NIMFunctional(Application application, IMUser imUser, IMManager.ManagerListener listener) {
        this.listener = listener;
        // SDK初始化（启动后台服务，若已经存在用户登录信息， SDK 将完成自动登录）
        NIMClient.init(application, loginInfo(imUser), options(application));

//        NIMClient.getService(MsgService.class).registerCustomAttachmentParser(new CustomAttachParser()); // 监听的注册，必须在主进程中。
    }

    public void resetListener(IMManager.ManagerListener listener) {
        this.listener = listener;
    }

    // 如果返回值为 null，则全部使用默认参数。
    private SDKOptions options(Context context) {
        SDKOptions options = new SDKOptions();

        // 如果将新消息通知提醒托管给 SDK 完成，需要添加以下配置。否则无需设置。
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        config.notificationEntrance = SplashActivity.class; // 点击通知栏跳转到该Activity
        config.notificationSmallIconId = R.drawable.view_setting_line_item_about_oneone_bg;
        // 呼吸灯配置
        config.ledARGB = Color.GREEN;
        config.ledOnMs = 1000;
        config.ledOffMs = 1500;
        // 通知铃声的uri字符串
        config.notificationSound = "android.resource://com.netease.nim.demo/raw/msg";
        options.statusBarNotificationConfig = config;

        // 配置保存图片，文件，log 等数据的目录
        // 如果 options 中没有设置这个值，SDK 会使用采用默认路径作为 SDK 的数据目录。
        // 该目录目前包含 log, file, image, audio, video, thumb 这6个目录。
        String sdkPath = getAppCacheDir(context) + "/nim"; // 可以不设置，那么将采用默认路径
        // 如果第三方 APP 需要缓存清理功能， 清理这个目录下面个子目录的内容即可。
        options.sdkStorageRootPath = sdkPath;

        // 配置是否需要预下载附件缩略图，默认为 true
        options.preloadAttach = true;

        // 配置附件缩略图的尺寸大小。表示向服务器请求缩略图文件的大小
        // 该值一般应根据屏幕尺寸来确定， 默认值为 Screen.width / 2
        options.thumbnailSize = ScreenUtil.screenWidth / 2;

        // 用户资料提供者, 目前主要用于提供用户资料，用于新消息通知栏中显示消息来源的头像和昵称
        options.userInfoProvider = new UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String account) {
                return null;
            }

            @Override
            public String getDisplayNameForMessageNotifier(String account, String sessionId,
                                                           SessionTypeEnum sessionType) {
                return null;
            }

            @Override
            public Bitmap getAvatarForMessageNotifier(SessionTypeEnum sessionType, String sessionId) {
                return null;
            }
        };
        return options;
    }

    /**
     * 配置 APP 保存图片/语音/文件/log等数据的目录
     * 这里示例用SD卡的应用扩展存储目录
     */
    static String getAppCacheDir(Context context) {
        String storageRootPath = null;
        try {
            // SD卡应用扩展存储区(APP卸载后，该目录下被清除，用户也可以在设置界面中手动清除)，请根据APP对数据缓存的重要性及生命周期来决定是否采用此缓存目录.
            // 该存储区在API 19以上不需要写权限，即可配置 <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" android:maxSdkVersion="18"/>
            if (context.getExternalCacheDir() != null) {
                storageRootPath = context.getExternalCacheDir().getCanonicalPath();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(storageRootPath)) {
            // SD卡应用公共存储区(APP卸载后，该目录不会被清除，下载安装APP后，缓存数据依然可以被加载。SDK默认使用此目录)，该存储区域需要写权限!
            storageRootPath = Environment.getExternalStorageDirectory() + "/" + context.getPackageName();
        }

        return storageRootPath;
    }

    // 如果已经存在用户登录信息，返回LoginInfo，否则返回null即可
    private LoginInfo loginInfo(IMUser imUser) {
        if (imUser != null)
            return new LoginInfo(imUser.getImUserId(), imUser.getToken()); // config...
        return null;
    }

    Observer<List<IMMessage>> incomingMessageObserver;
    Observer<List<RecentContact>> messageObserver;

    public void login(IMUser imUser) {
        LoginInfo info = new LoginInfo(imUser.getImUserId(), imUser.getToken()); // config...
//        LoginInfo info = new LoginInfo("597e283b007c40b2a8c392e0b8d3997d", "791965650d7eaa6eb651db90e92622b1");

        RequestCallback<LoginInfo> callback =
                new RequestCallback<LoginInfo>() {
                    @Override
                    public void onSuccess(LoginInfo param) {
                        System.out.println("login sucess !!!!!!!!!!!!!");
                        IMManager.IM_STATUS = IMManager.IM_STATUS_LOGIN;
//                        IMManager.NIM_FUNCTIONAL.sendTxtMsg("eb69bd4443e3490a845ff82652c8aec3", "hihihi");

                        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(
                                new Observer<StatusCode>() {
                                    public void onEvent(StatusCode status) {
                                        // 判断在线状态，如果为被其他端踢掉，做登出操作
                                        if (StatusCode.KICKOUT == status) {
                                            AppUtil.quit(OneOne.getInstance(), true);
                                        }
                                    }
                                }, true);

                        if (incomingMessageObserver != null) {
                            NIMClient.getService(MsgServiceObserve.class).observeReceiveMessage(incomingMessageObserver, false);
                        }
                        incomingMessageObserver =
                                new Observer<List<IMMessage>>() {
                                    @Override
                                    public void onEvent(List<IMMessage> messages) {
                                        // 处理新收到的消息，为了上传处理方便，SDK 保证参数 messages 全部来自同一个聊天对象。
//                                        listener.onReceiveImMessage(messages);
                                        EventBus.getDefault().post(new IMMessageEvent(messages));
//                                        for (IMMessage msg : messages) {
//                                            msg.getMsgType();
//                                            System.out.println(msg.getContent());
//                                        }
                                    }
                                };
                        NIMClient.getService(MsgServiceObserve.class).observeReceiveMessage(incomingMessageObserver, true);

                        NIMClient.getService(MsgService.class).queryRecentContacts()
                                .setCallback(new RequestCallbackWrapper<List<RecentContact>>() {
                                    @Override
                                    public void onResult(int code, List<RecentContact> recents, Throwable e) {
                                        // recents参数即为最近联系人列表（最近会话列表）
                                        listener.onInitIMContract(recents);
                                    }
                                });

                        //  创建观察者对象
                        if (messageObserver != null) {
                            NIMClient.getService(MsgServiceObserve.class)
                                    .observeRecentContact(messageObserver, false);
                        }
                        messageObserver =
                                new Observer<List<RecentContact>>() {
                                    @Override
                                    public void onEvent(List<RecentContact> messages) {
                                        listener.onIMContractChange(messages);
                                    }
                                };
                        //  注册/注销观察者
                        NIMClient.getService(MsgServiceObserve.class)
                                .observeRecentContact(messageObserver, true);

                        listener.onLoginRlt(IMManager.ON_IM_LOGIN_SUCESS);
                    }

                    @Override
                    public void onFailed(int code) {
                        IMManager.IM_STATUS = IMManager.IM_STATUS_INIT;
                        System.out.println("login failed : code -->" + code);
                        listener.onLoginRlt(IMManager.ON_IM_LOGIN_FAILED);
                    }

                    @Override
                    public void onException(Throwable exception) {
                        IMManager.IM_STATUS = IMManager.IM_STATUS_INIT;
                    }
                    // 可以在此保存LoginInfo到本地，下次启动APP做自动登录用
                };
        System.out.println("-------start login--------");
        NIMClient.getService(AuthService.class).login(info)
                .setCallback(callback);
    }

    @Override
    public void logout() {
        System.out.println("-------start logout--------");
        NIMClient.getService(AuthService.class).logout();
    }

    public List<IMMessage> queryMessageById(List<String> uuids) {
        return NIMClient.getService(MsgService.class).queryMessageListByUuidBlock(uuids);
    }

    public void deleteContact(String imUid) {
//        NIMClient.getService(MsgService.class).clearChattingHistory(imUid, SessionTypeEnum.P2P);
        NIMClient.getService(MsgService.class).deleteRecentContact2(imUid, SessionTypeEnum.P2P);
    }

    public void getHistoryMsg(IMMessage anchorMsg) {
        // 查询比 anchor时间更早的消息，查询20条，结果按照时间降序排列
        NIMClient.getService(MsgService.class).queryMessageListEx(anchorMsg, QueryDirectionEnum.QUERY_OLD,
                IMManager.HISTORY_COUNT_LIMIT, true).setCallback(new RequestCallbackWrapper<List<IMMessage>>() {
            @Override
            public void onResult(int code, List<IMMessage> result, Throwable exception) {
                EventBus.getDefault().post(new IMHistoryMessageEvent(result));
            }
        });
    }

    public void setMsgListener(InvocationFuture rlt, final MyMessage myMessage, final IMMessage imMessage) {
        rlt.setCallback(new RequestCallback() {
            @Override
            public void onSuccess(Object param) {
                System.out.println("====msg sucess!!!!");
                EventBus.getDefault().post(new SendMessageEvent(imMessage, true));
                listener.onMessageSendRlt(IMessage.MessageStatus.SEND_SUCCEED, myMessage);
            }

            @Override
            public void onFailed(int code) {
                System.out.println("====msg failed!!!!");
                EventBus.getDefault().post(new SendMessageEvent(imMessage, false));
                listener.onMessageSendRlt(IMessage.MessageStatus.SEND_FAILED, myMessage);
            }

            @Override
            public void onException(Throwable exception) {

            }
        });
    }

    public void sendGiftMsg(String targetImUserId, GiftMessage myMessage, boolean resend) {

        GiftAttachment attachment = new GiftAttachment(CustomAttachmentType.IM_TYPE_GIFT);
        attachment.setGift(myMessage.getGiftProd());
        IMMessage message = MessageBuilder.createCustomMessage(
                targetImUserId, SessionTypeEnum.P2P, "", attachment
        );
        Map<String, Object> map = new HashMap<String, Object>();
        if (HereUser.getInstance().getImUserInfo() != null) {
            map.put("userId", HereUser.getInstance().getUserId());
            map.put("imUserId", HereUser.getInstance().getImUserInfo().getImUserId());
            message.setPushPayload(map);
        }
        setMsgListener(NIMClient.getService(MsgService.class).sendMessage(message, resend), myMessage, message);
    }

    public void sendEmojiMsg(String targetImUserId, EmojiMessage myMessage, boolean resend) {
        EmojiAttachment attachment = new EmojiAttachment(CustomAttachmentType.IM_TYPE_EMOJI);
        attachment.setImEmoji(myMessage.getImEmoji());

        IMMessage message;
        if (resend) {
            message = myMessage.getImMessage();
        } else {
            message = MessageBuilder.createCustomMessage(targetImUserId, SessionTypeEnum.P2P, "", attachment);
            myMessage.setImMessage(message);

            Map<String, Object> map = new HashMap<String, Object>();
            if (HereUser.getInstance().getImUserInfo() != null) {
                map.put("userId", HereUser.getUserId());
                map.put("imUserId", HereUser.getInstance().getImUserInfo().getImUserId());
                message.setPushPayload(map);
            }

            message.setPushContent("[" + myMessage.getImEmoji().getMessage() + "]");
        }

        setMsgListener(NIMClient.getService(MsgService.class).sendMessage(message, resend), myMessage, message);
    }

    public IMMessage saveTxtMsg(String targetImUserId, MyMessage myMessage, IMessage.MessageStatus status) {
        // 以单聊类型为例
        SessionTypeEnum sessionType = SessionTypeEnum.P2P;
        // 创建一个文本消息
        IMMessage textMessage = MessageBuilder.createTextMessage(targetImUserId, sessionType, myMessage.getText());
        if (status == IMessage.MessageStatus.SEND_FAILED) {
            textMessage.setStatus(MsgStatusEnum.fail);
        } else {
            textMessage.setStatus(MsgStatusEnum.success);
        }

        myMessage.setImMessage(textMessage);

        NIMClient.getService(MsgService.class).saveMessageToLocal(textMessage, false);
        return textMessage;
    }

    public void sendTxtMsg(String targetImUserId, MyMessage myMessage, boolean resend) {
        // 以单聊类型为例
        SessionTypeEnum sessionType = SessionTypeEnum.P2P;
        // 创建一个文本消息
        IMMessage textMessage;
        if (resend) {
            textMessage = myMessage.getImMessage();
        } else {
            textMessage = MessageBuilder.createTextMessage(targetImUserId, sessionType, myMessage.getText());
            Map<String, Object> map = new HashMap<String, Object>();
            if (HereUser.getInstance().getImUserInfo() != null) {
                map.put("userId", HereUser.getInstance().getUserId());
                map.put("imUserId", HereUser.getInstance().getImUserInfo().getImUserId());
                textMessage.setPushPayload(map);
            }

            myMessage.setImMessage(textMessage);
        }
        setMsgListener(NIMClient.getService(MsgService.class).sendMessage(textMessage, resend), myMessage, textMessage);
    }

    public void sendImgMsg(String filePath, String targetImUserId) {
        // 以单聊类型为例
        SessionTypeEnum sessionType = SessionTypeEnum.P2P;
        // 示例图片，需要开发者在相应目录下有图片
        File file = new File(filePath);
        // 创建一个图片消息
        IMMessage message = MessageBuilder.createImageMessage(targetImUserId, sessionType, file, file.getName());

        // 发送给对方

        Map<String, Object> map = new HashMap<String, Object>();
        if (HereUser.getInstance().getImUserInfo() != null) {
            map.put("userId", HereUser.getUserId());
            map.put("imUserId", HereUser.getInstance().getImUserInfo().getImUserId());
            message.setPushPayload(map);
        }
        NIMClient.getService(MsgService.class).sendMessage(message, false);
    }

    public void sendSoundMsg(String filePath, String targetImUserId, long duration) {//duration (ms)
        // 以单聊类型为例
        SessionTypeEnum sessionType = SessionTypeEnum.P2P;
        // 示例音频，需要开发者在相应目录下有文件
        File audioFile = new File(filePath);
        // 创建音频消息
        IMMessage audioMessage = MessageBuilder.createAudioMessage(filePath, sessionType, audioFile, duration);

        Map<String, Object> map = new HashMap<String, Object>();
        if (HereUser.getInstance().getImUserInfo() != null) {
            map.put("userId", HereUser.getInstance().getUserId());
            map.put("imUserId", HereUser.getInstance().getImUserInfo().getImUserId());
            audioMessage.setPushPayload(map);
        }
        // 发送给对方
        NIMClient.getService(MsgService.class).sendMessage(audioMessage, false);
    }

    public void sendVideoMsg(String filePath, String targetImUserId, Context context) {
        // 以单聊类型为例
        SessionTypeEnum sessionType = SessionTypeEnum.P2P;
        // 示例视频，需要开发者在相应目录下有文件
        File file = new File(filePath);
        // 获取视频mediaPlayer
        MediaPlayer mediaPlayer = null;
        try {
            mediaPlayer = MediaPlayer.create(context, Uri.fromFile(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 视频文件持续时间
        long duration = mediaPlayer == null ? 0 : mediaPlayer.getDuration();
        // 视频高度
        int height = mediaPlayer == null ? 0 : mediaPlayer.getVideoHeight();
        // 视频宽度
        int width = mediaPlayer == null ? 0 : mediaPlayer.getVideoWidth();
        // 创建视频消息
        IMMessage message = MessageBuilder.createVideoMessage(targetImUserId, sessionType, file, duration, width, height, null);


        Map<String, Object> map = new HashMap<String, Object>();
        if (HereUser.getInstance().getImUserInfo() != null) {
            map.put("userId", HereUser.getUserId());
            map.put("imUserId", HereUser.getInstance().getImUserInfo().getImUserId());
            message.setPushPayload(map);
        }
        // 发送给对方
        NIMClient.getService(MsgService.class).sendMessage(message, false);
    }

    public void sendFileMsg(String filePath, String targetImUserId) {
        // 以单聊类型为例
        SessionTypeEnum sessionType = SessionTypeEnum.P2P;
        // 示例文件，需要开发者在相应目录下有文件
        File file = new File(filePath);
        // 创建文件消息
        IMMessage message = MessageBuilder.createFileMessage(targetImUserId, sessionType, file, file.getName());

        Map<String, Object> map = new HashMap<String, Object>();
        if (HereUser.getInstance().getImUserInfo() != null) {
            map.put("userId", HereUser.getInstance().getUserId());
            map.put("imUserId", HereUser.getInstance().getImUserInfo().getImUserId());
            message.setPushPayload(map);
        }
        // 发送给对方
        NIMClient.getService(MsgService.class).sendMessage(message, false);
    }

    public void sendLocationMsg(String targetImUserId, double lat, double lng, String addrDescStr) {
        // 以单聊类型为例
        SessionTypeEnum sessionType = SessionTypeEnum.P2P;
        // 创建地理位置信息
        IMMessage message = MessageBuilder.createLocationMessage(targetImUserId, sessionType, lat, lng, addrDescStr);

        Map<String, Object> map = new HashMap<String, Object>();
        if (HereUser.getInstance().getImUserInfo() != null) {
            map.put("userId", HereUser.getUserId());
            map.put("imUserId", HereUser.getInstance().getImUserInfo().getImUserId());
            message.setPushPayload(map);
        }
        // 发送给对方
        NIMClient.getService(MsgService.class).sendMessage(message, false);
    }
}
