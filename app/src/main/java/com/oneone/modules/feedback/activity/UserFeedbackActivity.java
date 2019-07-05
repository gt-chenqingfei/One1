package com.oneone.modules.feedback.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.google.gson.Gson;
import com.oneone.BaseActivity;
import com.oneone.HereSingletonFactory;
import com.oneone.R;
import com.oneone.api.constants.RedDot;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;
import com.oneone.framework.ui.glide.GlideUtils;
import com.oneone.framework.ui.imagepicker.ImagePicker;
import com.oneone.framework.ui.imagepicker.bean.ImageItem;
import com.oneone.framework.ui.imagepicker.ui.ImageGridActivity;
import com.oneone.framework.ui.utils.PermissionsUtil;
import com.oneone.framework.ui.utils.ScreenUtil;
import com.oneone.modules.feedback.beans.Feedback;
import com.oneone.modules.feedback.beans.FeedbackListItem;
import com.oneone.modules.feedback.contract.FeedbackContract;
import com.oneone.modules.feedback.presenter.FeedbackPresenter;
import com.oneone.modules.msg.EmojiClickListener;
import com.oneone.modules.msg.IMManager;
import com.oneone.modules.msg.activity.BrowserImageActivity;
import com.oneone.modules.msg.activity.VideoActivity;
import com.oneone.modules.msg.beans.IMEmoji;
import com.oneone.modules.msg.beans.TalkBeans.DefaultUser;
import com.oneone.modules.msg.beans.TalkBeans.EmojiMessage;
import com.oneone.modules.msg.beans.TalkBeans.MyMessage;
import com.oneone.modules.msg.beans.TalkBeans.PicMessage;
import com.oneone.modules.msg.beans.TalkBeans.ReportMessage;
import com.oneone.modules.msg.widget.ChatView;
import com.oneone.modules.msg.widget.MyChatInputView;
import com.oneone.modules.reddot.RedDotManager;
import com.oneone.modules.support.qiniu.PhotoUploadListener;
import com.oneone.modules.support.qiniu.UploadParam;
import com.oneone.modules.timeline.activity.PermissionsWarnActivity;
import com.oneone.restful.ApiResult;
import com.oneone.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import cn.jiguang.imui.chatinput.listener.OnCameraCallbackListener;
import cn.jiguang.imui.chatinput.listener.OnMenuClickListener;
import cn.jiguang.imui.chatinput.listener.RecordVoiceListener;
import cn.jiguang.imui.chatinput.model.FileItem;
import cn.jiguang.imui.chatinput.model.VideoItem;
import cn.jiguang.imui.commons.ImageLoader;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.messages.MessageList;
import cn.jiguang.imui.messages.MsgListAdapter;
import cn.jiguang.imui.messages.ptr.PtrHandler;
import cn.jiguang.imui.messages.ptr.PullToRefreshLayout;

/**
 * Created by here on 18/6/8.
 */

@ToolbarResource(title = R.string.str_feed_back_title)
@LayoutResource(R.layout.activity_user_feedback)
public class UserFeedbackActivity extends BaseActivity<FeedbackPresenter, FeedbackContract.View> implements FeedbackContract.View, View.OnTouchListener,
        SensorEventListener, View.OnClickListener, PhotoUploadListener {
    @BindView(R.id.chat_view)
    ChatView mChatView;
    @BindView(R.id.chat_input)
    MyChatInputView mChatInputView;
    @BindView(R.id.talk_message_list)
    MessageList talkMessageList;
    @BindView(R.id.pull_to_refresh_layout)
    PullToRefreshLayout ptrLayout;

    private final static String TAG = "UserFeedbackActivity";
    private final int RC_RECORD_VOICE = 0x0001;
    private final int RC_CAMERA = 0x0002;
    private final int RC_PHOTO = 0x0003;


    private MsgListAdapter mAdapter;
    private HeadsetDetectReceiver mReceiver;
    private InputMethodManager mImm;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock;
    private Window mWindow;

    private ArrayList<String> mPathList = new ArrayList<>();
    private ArrayList<String> mMsgIdList = new ArrayList<>();

    private long currentListTime;
    private static final int PAGE_SIZE = 10;

    private Gson gson;

    private boolean firstLoad = true;

    public static void startActivity(Context context) {
        Intent it = new Intent(context, UserFeedbackActivity.class);
        context.startActivity(it);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImmersionBar.statusBarDarkFont(true)
                .navigationBarColor(R.color.color_black)
                .keyboardEnable(true).init();

        gson = new Gson();

        initChatView();

        currentListTime = System.currentTimeMillis();
        mPresenter.feedbackList(currentListTime, PAGE_SIZE);

    }

    public void initChatView() {
        this.mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mWindow = getWindow();
        registerProximitySensorListener();
        mChatView = (ChatView) findViewById(R.id.chat_view);
        mChatView.initModule();
        mChatView.setEmojiClickListener(new EmojiClickListener() {
            @Override
            public void onImEmojiClick(IMEmoji imEmoji) {
                EmojiMessage message = new EmojiMessage("", IMManager.MY_MSG_TYPE_EMOJI_SENDER);
                message.init(imEmoji);
                message.setUserInfo(new DefaultUser("0", "", "R.drawable.ic_launcher"));
                message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                mAdapter.addToStart(message, true);

                sendFeedBack(null, imEmoji, null);
                //**find send msg
//                IMManager.getInstance().sendEmoji(imUserPrerelation.getToImUserId(), message);
            }
        });
        initMsgAdapter();
        mReceiver = new HeadsetDetectReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(mReceiver, intentFilter);
        mChatView.setOnTouchListener(this);
        mChatView.setMenuClickListener(new OnMenuClickListener() {
            @Override
            public boolean onSendTextMessage(CharSequence input) {
                if (input == null || input.length() == 0) {
                    return false;
                }
                String inputStr = input.toString();

                MyMessage message = new MyMessage(inputStr, IMManager.MY_MSG_TYPE_TXT_SENDER);
                message.setUserInfo(new DefaultUser("1", "Ironman", "R.drawable.ironman"));
                message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                message.setMessageStatus(IMessage.MessageStatus.SEND_GOING);
                mAdapter.addToStart(message, true);

                sendFeedBack(inputStr, null, null);

                //**find send msg
//                mPresenter.imMsgCheck(imUserPrerelation.getToImUserId(), IMManager.ONEONE_IM_TYPE_TEXT, inputStr, message);
                return true;
            }

            @Override
            public void onSendFiles(List<FileItem> list) {
                if (list == null || list.isEmpty()) {
                    return;
                }

                MyMessage message;
                for (FileItem item : list) {
                    if (item.getType() == FileItem.Type.Image) {
                        message = new MyMessage(null, IMessage.MessageType.SEND_IMAGE.ordinal());
                        mPathList.add(item.getFilePath());
                        mMsgIdList.add(message.getMsgId());
                    } else if (item.getType() == FileItem.Type.Video) {
                        message = new MyMessage(null, IMessage.MessageType.SEND_VIDEO.ordinal());
                        message.setDuration(((VideoItem) item).getDuration());

                    } else {
                        throw new RuntimeException("Invalid FileItem type. Must be Type.Image or Type.Video");
                    }

                    message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                    message.setMediaFilePath(item.getFilePath());
                    message.setUserInfo(new DefaultUser("1", "Ironman", "R.drawable.ironman"));

                    final MyMessage fMsg = message;
                    UserFeedbackActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.addToStart(fMsg, true);
                        }
                    });
                }
            }

            @Override
            public boolean switchToMicrophoneMode() {
                if (!PermissionsUtil.checkPermissions(mContext, PermissionsUtil.READ_EXTERNAL_STORAGE) &&
                        !PermissionsUtil.checkPermissions(mContext, PermissionsUtil.WRITE_EXTERNAL_STORAGE)) {
                    PermissionsWarnActivity.startActivity(mContext, PermissionsWarnActivity.EXTERNAL_STORAGE);
                } else if (!PermissionsUtil.checkPermissions(mContext, PermissionsUtil.CAMERA)) {
                    PermissionsWarnActivity.startActivity(mContext, PermissionsWarnActivity.CAMERA);
                } else if (!PermissionsUtil.checkPermissions(mContext, PermissionsUtil.RECORD_AUDIO)) {
                    PermissionsWarnActivity.startActivity(mContext, PermissionsWarnActivity.RECORD_AUDIO);
                } else {
                    scrollToBottom();
                }
                return true;
            }

            @Override
            public boolean switchToGalleryMode() {
                if (!PermissionsUtil.checkPermissions(mContext, PermissionsUtil.READ_EXTERNAL_STORAGE) &&
                        !PermissionsUtil.checkPermissions(mContext, PermissionsUtil.WRITE_EXTERNAL_STORAGE)) {
                    PermissionsWarnActivity.startActivity(mContext, PermissionsWarnActivity.EXTERNAL_STORAGE);
                } else if (!PermissionsUtil.checkPermissions(mContext, PermissionsUtil.CAMERA)) {
                    PermissionsWarnActivity.startActivity(mContext, PermissionsWarnActivity.CAMERA);
                } else {
                    scrollToBottom();
                }

                // If you call updateData, select photo view will try to update data(Last update over 30 seconds.)
                //!!!版本过低
                return true;
            }

            @Override
            public boolean switchToCameraMode() {
                if (!PermissionsUtil.checkPermissions(mContext, PermissionsUtil.READ_EXTERNAL_STORAGE) &&
                        !PermissionsUtil.checkPermissions(mContext, PermissionsUtil.WRITE_EXTERNAL_STORAGE)) {
                    PermissionsWarnActivity.startActivity(mContext, PermissionsWarnActivity.EXTERNAL_STORAGE);
                } else if (!PermissionsUtil.checkPermissions(mContext, PermissionsUtil.CAMERA)) {
                    PermissionsWarnActivity.startActivity(mContext, PermissionsWarnActivity.CAMERA);
                } else {
                    scrollToBottom();
                    startSelectImage(1, REQ_PICKER_PICTURE, false);
                }
                return true;
            }

            @Override
            public boolean switchToEmojiMode() {
                scrollToBottom();
                return true;
            }
        });

        mChatView.setRecordVoiceListener(new RecordVoiceListener() {
            @Override
            public void onStartRecord() {
                // set voice file path, after recording, audio file will save here
                String path = Environment.getExternalStorageDirectory().getPath() + "/voice";
                File destDir = new File(path);
                if (!destDir.exists()) {
                    destDir.mkdirs();
                }
                mChatView.setRecordVoiceFile(destDir.getPath(), DateFormat.format("yyyy-MM-dd-hhmmss",
                        Calendar.getInstance(Locale.CHINA)) + "");
            }

            @Override
            public void onFinishRecord(File voiceFile, int duration) {
                MyMessage message = new MyMessage(null, IMessage.MessageType.SEND_VOICE.ordinal());
                message.setUserInfo(new DefaultUser("1", "Ironman", "R.drawable.ironman"));
                message.setMediaFilePath(voiceFile.getPath());
                message.setDuration(duration);
                message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                mAdapter.addToStart(message, true);
            }

            @Override
            public void onCancelRecord() {

            }

            /**
             * In preview record voice layout, fires when click cancel button
             * Add since chatinput v0.7.3
             */
            @Override
            public void onPreviewCancel() {

            }

            /**
             * In preview record voice layout, fires when click send button
             * Add since chatinput v0.7.3
             */
            @Override
            public void onPreviewSend() {

            }
        });

        mChatView.setOnCameraCallbackListener(new OnCameraCallbackListener() {
            @Override
            public void onTakePictureCompleted(String photoPath) {
                final MyMessage message = new MyMessage(null, IMessage.MessageType.SEND_IMAGE.ordinal());
                message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                message.setMediaFilePath(photoPath);
                mPathList.add(photoPath);
                mMsgIdList.add(message.getMsgId());
                message.setUserInfo(new DefaultUser("1", "Ironman", "R.drawable.ironman"));
                UserFeedbackActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.addToStart(message, true);
                    }
                });
            }

            @Override
            public void onStartVideoRecord() {

            }

            @Override
            public void onFinishVideoRecord(String videoPath) {

            }

            @Override
            public void onCancelVideoRecord() {

            }
        });

        mChatView.getChatInputView().getInputView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scrollToBottom();
                return false;
            }
        });

        mChatView.getSelectAlbumBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show(UserFeedbackActivity.this, "OnClick select album button");
            }
        });
    }

    public void sendFeedBack(String feedbackContent, IMEmoji imEmoji, List<UploadParam> uploadParamList) {
        Feedback feedback = new Feedback();
        feedback.setFeedbackType(1);
        feedback.setTargetEntityId("");
        feedback.setTargetEntityType("");
        feedback.setFeedbackReason(0);
        if (feedbackContent != null) {
            feedback.setFeedback(feedbackContent);
        } else {
            if (imEmoji == null && (uploadParamList != null || uploadParamList.size() <= 0)) {
                feedback.setFeedback("未知消息类型");
            }
        }

        if (imEmoji != null) {
            feedback.setAttachment(gson.toJson(imEmoji));
            feedback.setFeedback(imEmoji.getMessage());
        }
        feedback.setContactInfo("");

        if (uploadParamList != null && uploadParamList.size() > 0) {
            ArrayList<String> imgList = new ArrayList<String>();
            for (UploadParam uploadParam : uploadParamList) {
                imgList.add(uploadParam.getRemoteUrl());
            }
            feedback.setImgs(imgList);
        }

        String feedbackJsonStr = gson.toJson(feedback, Feedback.class);
        mPresenter.feedbackSend(feedbackJsonStr);
    }


    private void initMsgAdapter() {
        final float density = getResources().getDisplayMetrics().density;
        final float MIN_WIDTH = 60 * density;
        final float MAX_WIDTH = 200 * density;
        final float MIN_HEIGHT = 60 * density;
        final float MAX_HEIGHT = 200 * density;
        ImageLoader imageLoader = new ImageLoader() {
            @Override
            public void loadAvatarImage(ImageView avatarImageView, String string) {
                // You can use other image load libraries.
                if (string.contains("R.drawable")) {
                    Integer resId = getResources().getIdentifier(string.replace("R.drawable.", ""),
                            "drawable", getPackageName());

                    avatarImageView.setImageResource(resId);
                } else {
                    GlideUtils.loadImage(R.drawable.aurora_headicon_default, avatarImageView, new GlideUtils.ImageLoadListener<Integer, GlideDrawable>() {
                        @Override
                        public void onLoadingComplete(Integer uri, ImageView view, GlideDrawable resource) {
                        }

                        @Override
                        public void onLoadingError(Integer source, Exception e) {
                        }
                    });
                }
            }

            /**
             * Load image message
             * @param imageView Image message's ImageView.
             * @param string A file path, or a uri or url.
             */
            @Override
            public void loadImage(final ImageView imageView, String string) {
                // You can use other image load libraries.
            }

            /**
             * Load video message
             * @param imageCover Video message's image cover
             * @param uri Local path or url.
             */
            @Override
            public void loadVideo(ImageView imageCover, String uri) {
                long interval = 5000 * 1000;
            }
        };

        // Use default layout
        MsgListAdapter.HoldersConfig holdersConfig = new MsgListAdapter.HoldersConfig();
        mAdapter = new MsgListAdapter<>("0", holdersConfig, imageLoader);

        mAdapter.setOnMsgClickListener(new MsgListAdapter.OnMsgClickListener<MyMessage>() {
            @Override
            public void onMessageClick(MyMessage message) {
                // do something
                if (message.getType() == IMessage.MessageType.RECEIVE_VIDEO.ordinal()
                        || message.getType() == IMessage.MessageType.SEND_VIDEO.ordinal()) {
                    if (!TextUtils.isEmpty(message.getMediaFilePath())) {
                        Intent intent = new Intent(UserFeedbackActivity.this, VideoActivity.class);
                        intent.putExtra(VideoActivity.VIDEO_PATH, message.getMediaFilePath());
                        startActivity(intent);
                    }
                } else if (message.getType() == IMessage.MessageType.RECEIVE_IMAGE.ordinal()
                        || message.getType() == IMessage.MessageType.SEND_IMAGE.ordinal()) {
                    Intent intent = new Intent(UserFeedbackActivity.this, BrowserImageActivity.class);
                    intent.putExtra("msgId", message.getMsgId());
                    intent.putStringArrayListExtra("pathList", mPathList);
                    intent.putStringArrayListExtra("idList", mMsgIdList);
                    startActivity(intent);
                } else {
                    ToastUtil.show(UserFeedbackActivity.this, "R.string.message_click_hint");
                }
            }
        });

        mAdapter.setMsgLongClickListener(new MsgListAdapter.OnMsgLongClickListener<MyMessage>() {
            @Override
            public void onMessageLongClick(View view, MyMessage message) {
                ToastUtil.show(UserFeedbackActivity.this, "R.string.message_long_click_hint");
                // do something
            }
        });

        mAdapter.setOnAvatarClickListener(new MsgListAdapter.OnAvatarClickListener<MyMessage>() {
            @Override
            public void onAvatarClick(MyMessage message) {
                DefaultUser userInfo = (DefaultUser) message.getFromUser();
                ToastUtil.show(UserFeedbackActivity.this, "R.string.avatar_click_hint");
                // do something
            }
        });

        mAdapter.setMsgStatusViewClickListener(new MsgListAdapter.OnMsgStatusViewClickListener<MyMessage>() {
            @Override
            public void onStatusViewClick(MyMessage message) {
                // message status view click, resend or download here
            }
        });

        PullToRefreshLayout layout = mChatView.getPtrLayout();
        layout.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PullToRefreshLayout layout) {
                Log.i("MessageListActivity", "Loading next page");
                loadNextPage();
            }
        });

        mChatView.setAdapter(mAdapter);
        mAdapter.getLayoutManager().scrollToPosition(0);
    }

    private void loadNextPage() {
        mPresenter.feedbackList(currentListTime, PAGE_SIZE);
    }

    private void scrollToBottom() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mChatView.getMessageListView().smoothScrollToPosition(0);
            }
        }, 200);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onUploadCompleted(UploadParam param, List<UploadParam> group, boolean isAllEnd) {
        if (isAllEnd) {
            sendFeedBack(null, null, group);
        }
    }

    @Override
    public void onUploadError(UploadParam param, Throwable e) {

    }

    @Override
    public void onUploadProgress(UploadParam param, double percent) {

    }

    @Override
    public void onUploadStart(UploadParam param) {

    }

    private class HeadsetDetectReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
                if (intent.hasExtra("state")) {
                    int state = intent.getIntExtra("state", 0);
                    mAdapter.setAudioPlayByEarPhone(state);
                }
            }
        }
    }

    private void registerProximitySensorListener() {
        try {
            mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
            mWakeLock = mPowerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, TAG);
            mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public FeedbackPresenter onCreatePresenter() {
        return new FeedbackPresenter();
    }

    @Override
    public void onFeedbackSend(ApiResult result) {

    }

    @Override
    public void onFeedbackList(int count, List<FeedbackListItem> feedbackList) {
        RedDotManager.getInstance().clearDot(RedDot.FEEDBACK);
        if (feedbackList.size() > 0) {
            Collections.sort(feedbackList);
            if (!firstLoad) {
                Collections.reverse(feedbackList);
            }
            for (FeedbackListItem feedbackListItem : feedbackList) {
                if (feedbackListItem.getFeedbackType() == 1) {
                    String attachmentStr = feedbackListItem.getAttachment();
                    if (attachmentStr != null && !attachmentStr.equals("")) {
                        try {
                            JSONObject jObj = new JSONObject(attachmentStr);
                            String type = jObj.getString("type");
                            if (type.equals("emoji")) {
                                IMEmoji imEmoji = gson.fromJson(attachmentStr, IMEmoji.class);
                                EmojiMessage emojiMsg = null;
                                if (feedbackListItem.getDirection() == 2) {
                                    emojiMsg = new EmojiMessage("", IMManager.MY_MSG_TYPE_EMOJI_RECEIVER);
                                } else {
                                    emojiMsg = new EmojiMessage("", IMManager.MY_MSG_TYPE_EMOJI_SENDER);
                                }
                                emojiMsg.init(imEmoji);
                                emojiMsg.setUserInfo(new DefaultUser("0", "", "R.drawable.ic_launcher"));
                                emojiMsg.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                                if (firstLoad)
                                    mAdapter.addToStart(emojiMsg, true);
                                else {
                                    List<MyMessage> msgList = new ArrayList<>();
                                    msgList.add(emojiMsg);
                                    mAdapter.addToEnd(msgList);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (feedbackListItem.getImgList() != null && feedbackListItem.getImgList().size() > 0) {
                        PicMessage picMessage = null;
                        if (feedbackListItem.getDirection() == 2) {
                            picMessage = new PicMessage("", IMManager.MY_MSG_TYPE_PIC_RECEIVER);
                        } else {
                            picMessage = new PicMessage("", IMManager.MY_MSG_TYPE_PIC_SENDER);
                        }
                        picMessage.init(feedbackListItem.getImgList().get(0));
                        picMessage.setUserInfo(new DefaultUser("0", "", "R.drawable.ic_launcher"));
                        picMessage.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                        if (firstLoad)
                            mAdapter.addToStart(picMessage, true);
                        else {
                            List<MyMessage> msgList = new ArrayList<>();
                            msgList.add(picMessage);
                            mAdapter.addToEnd(msgList);
                        }
                    } else {
                        MyMessage txtMessage = null;
                        if (feedbackListItem.getDirection() == 2) {
                            txtMessage = new MyMessage(feedbackListItem.getFeedback(), IMManager.MY_MSG_TYPE_TXT_RECEIVER);
                        } else {
                            txtMessage = new PicMessage(feedbackListItem.getFeedback(), IMManager.MY_MSG_TYPE_TXT_SENDER);
                        }
                        txtMessage.setUserInfo(new DefaultUser("0", "", "R.drawable.ic_launcher"));
                        txtMessage.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                        if (firstLoad)
                            mAdapter.addToStart(txtMessage, true);
                        else {
                            List<MyMessage> msgList = new ArrayList<>();
                            msgList.add(txtMessage);
                            mAdapter.addToEnd(msgList);
                        }
                    }


                } else if (feedbackListItem.getFeedbackType() == 2) {
                    ReportMessage message = null;
                    if (feedbackListItem.getDirection() == 2) {
                        message = new ReportMessage(feedbackListItem.getFeedback(), IMManager.MY_MSG_TYPE_REPORT_RECEIVER);
                    } else {
                        message = new ReportMessage(feedbackListItem.getFeedback(), IMManager.MY_MSG_TYPE_REPORT_SENDER);
                    }
                    if (message != null) {
                        message.init(feedbackListItem);
                        message.setUserInfo(new DefaultUser("1", "Ironman", "R.drawable.ironman"));
                        message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                        message.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
                        if (firstLoad)
                            mAdapter.addToStart(message, true);
                        else {
                            List<MyMessage> msgList = new ArrayList<>();
                            msgList.add(message);
                            mAdapter.addToEnd(msgList);
//                            mAdapter.addToStart(message, false);
                        }
                    }
                }
            }

            if (firstLoad) {
                currentListTime = feedbackList.get(0).getGmtCreate();
            } else {
                currentListTime = feedbackList.get(feedbackList.size() - 1).getGmtCreate();
            }
            if (firstLoad)
                firstLoad = false;
//            ptrLayout.refreshComplete();
        }
        ptrLayout.refreshComplete();
    }

    public static final int REQ_PICKER_PICTURE = 10001;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_PICKER_PICTURE) {
            ArrayList<ImageItem> newItemList = null;
            if (data != null && resultCode == ImagePicker.RESULT_CODE_ITEMS) {
                newItemList = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            }

            if (newItemList == null || newItemList.isEmpty()) {
                return;
            }

            PicMessage picMessage = new PicMessage("", IMManager.MY_MSG_TYPE_PIC_SENDER);
            picMessage.init(newItemList.get(0).path);
            picMessage.setUserInfo(new DefaultUser("0", "", "R.drawable.ic_launcher"));
            picMessage.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
            mAdapter.addToStart(picMessage, true);

            List<UploadParam> params = new ArrayList<>();
            UploadParam param = new UploadParam(999, newItemList.get(0).path, this);
            params.add(param);

            HereSingletonFactory.getInstance().getPhotoUploadManager().enqueueWithGroup(params);
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void startSelectImage(int limit, int reqCode, boolean isCrop) {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setShowCamera(true);
        imagePicker.setSelectLimit(limit);
        imagePicker.setMultiMode(limit > 1);
        imagePicker.setFocusWidth(ScreenUtil.screenWidth);
        imagePicker.setFocusHeight(ScreenUtil.screenHeight);
        imagePicker.setCrop(isCrop);
        imagePicker.setSaveRectangle(true);
        if (isCrop) {
            imagePicker.setSaveRectangle(false);
            imagePicker.setOutPutX(1000);
            imagePicker.setOutPutY(1000);
        }
        Intent intent1 = new Intent(getActivityContext(), ImageGridActivity.class);

        startActivityForResult(intent1, reqCode);
    }
}
