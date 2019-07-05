package com.oneone.modules.msg.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.oneone.BaseActivity;
import com.oneone.R;
import com.oneone.event.EventRefreshMsgBubble;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.glide.GlideUtils;
import com.oneone.modules.dogfood.dialog.NotEnougthDogFoodDialog;
import com.oneone.modules.feedback.activity.ReportUserActivity;
import com.oneone.modules.find.beans.FindCondition;
import com.oneone.modules.find.dto.FindPageUserInfoDTO;
import com.oneone.modules.find.dto.LikeListDto;
import com.oneone.modules.find.dto.LikeUnreadListDto;
import com.oneone.modules.msg.EmojiClickListener;
import com.oneone.modules.msg.IMManager;
import com.oneone.modules.msg.beans.GiftProd;
import com.oneone.modules.msg.beans.IMEmoji;
import com.oneone.modules.msg.beans.IMFirstRelation;
import com.oneone.modules.msg.beans.IMUser;
import com.oneone.modules.msg.beans.IMUserPrerelation;
import com.oneone.modules.msg.beans.MsgMeta;
import com.oneone.modules.msg.beans.NotifyListItem;
import com.oneone.modules.msg.beans.TalkBeans.DefaultUser;
import com.oneone.modules.msg.beans.TalkBeans.EmojiMessage;
import com.oneone.modules.msg.beans.TalkBeans.GiftMessage;
import com.oneone.modules.msg.beans.TalkBeans.MyMessage;
import com.oneone.modules.msg.beans.TalkBeans.attachment.CustomAttachParser;
import com.oneone.modules.msg.beans.TalkBeans.attachment.EmojiAttachment;
import com.oneone.modules.msg.beans.TalkBeans.attachment.GiftAttachment;
import com.oneone.modules.msg.beans.TimeLineInfo;
import com.oneone.modules.msg.contract.MsgContract;
import com.oneone.modules.msg.dialog.IMBottomDialog;
import com.oneone.modules.msg.dto.MsgMetaDto;
import com.oneone.modules.msg.event.IMHistoryMessageEvent;
import com.oneone.modules.msg.event.IMMessageEvent;
import com.oneone.modules.msg.event.SendMessageEvent;
import com.oneone.modules.msg.presenter.MsgPresenter;
import com.oneone.modules.msg.widget.ChatView;
import com.oneone.modules.msg.widget.MyChatInputView;
import com.oneone.modules.profile.ProfileStater;
import com.oneone.modules.task.dto.LoginReceiveAwardDTO;
import com.oneone.modules.task.model.TaskModel;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.restful.ApiResult;
import com.oneone.utils.ImageHelper;
import com.oneone.utils.MyTextUtil;
import com.oneone.utils.ToastUtil;
import com.oneone.widget.AvatarImageView;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import cn.jiguang.imui.messages.ViewHolderController;
import cn.jiguang.imui.messages.ptr.PtrHandler;
import cn.jiguang.imui.messages.ptr.PullToRefreshLayout;

/**
 * Created by here on 18/5/1.
 */

@Route(path = "/im/chat")
@LayoutResource(R.layout.activity_im_talk)
public class ImTalkActivity extends BaseActivity<MsgPresenter, MsgContract.View> implements MsgContract.View, View.OnTouchListener,
        SensorEventListener, View.OnClickListener {
    @BindView(R.id.user_name_tv)
    TextView userNameTv;
    @BindView(R.id.user_photo_iv)
    AvatarImageView userPhotoIv;
    @BindView(R.id.close_btn)
    Button closeBtn;
    @BindView(R.id.more_set_btn)
    Button moreSetBtn;

    @BindView(R.id.first_time_gift_layout)
    LinearLayout firstTimeGiftLayout;
    @BindView(R.id.first_time_gift_hori_layout)
    LinearLayout firstTimeGiftHoriLayout;
    @BindView(R.id.gift_open_im_tv)
    TextView giftOpenImTv;

    @BindView(R.id.chat_view)
    ChatView mChatView;
    @BindView(R.id.chat_input)
    MyChatInputView mChatInputView;
    @BindView(R.id.talk_message_list)
    MessageList talkMessageList;
    @BindView(R.id.pull_to_refresh_layout)
    PullToRefreshLayout ptrLayout;

    private final static String TAG = "ImTalkActivity";
    public static IMUserPrerelation imUserPrerelation;
    private RecentContact recentContact;
    private IMFirstRelation firstRelation;

    private UserInfo userInfo;

    private MsgListAdapter mAdapter;
    private HeadsetDetectReceiver mReceiver;
    private InputMethodManager mImm;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock;
    private Window mWindow;

    private IMBottomDialog imBottomDialog;

    private ArrayList<String> mPathList = new ArrayList<>();
    private ArrayList<String> mMsgIdList = new ArrayList<>();

    private final int RC_RECORD_VOICE = 0x0001;
    private final int RC_CAMERA = 0x0002;
    private final int RC_PHOTO = 0x0003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImmersionBar.statusBarDarkFont(true)
                .navigationBarColor(R.color.color_black)
                .keyboardEnable(true).init();

        super.overridePendingTransition(R.anim.activity_in_from_bottom, R.anim.activity_none);
        NIMClient.getService(MsgService.class).registerCustomAttachmentParser(new CustomAttachParser()); // 监听的注册，必须在主进程中。

        imUserPrerelation = getIntent().getExtras().getParcelable("pre_relation");

        IMManager.curToImUid = imUserPrerelation.getToImUserId();

        if (imUserPrerelation.isRelation()) {
            recentContact = (RecentContact) getIntent().getExtras().getSerializable("im_contact");
            firstRelation = getIntent().getExtras().getParcelable("oneone_contact");
        }

        if (firstRelation != null && firstRelation.getUserInfo() != null) {
            userInfo = firstRelation.getUserInfo();
        } else if (imUserPrerelation.getUserInfo() != null) {
            userInfo = imUserPrerelation.getUserInfo();
        }

        EventBus.getDefault().register(this);
        new AsyncTask<Object, Void, ApiResult<LoginReceiveAwardDTO>>() {

            @Override
            protected ApiResult<LoginReceiveAwardDTO> doInBackground(Object... voids) {
                TaskModel taskModel = new TaskModel(getActivityContext());
                return taskModel.taskLoginReceiveAward();
            }

            @Override
            protected void onPostExecute(ApiResult<LoginReceiveAwardDTO> result) {
                super.onPostExecute(result);
            }
        }.execute();
        initView();
        initChatView();
        initFirstTimeGift();
        getHistoryMsg();
        setListener();
    }

    private List<IMMessage> talkMsgList = new ArrayList<IMMessage>();

    public void getHistoryMsg() {
        if (talkMsgList.size() > 0)
            IMManager.getInstance().getHistory(talkMsgList.get(0));
        else
            IMManager.getInstance().getHistory(MessageBuilder.createEmptyMessage(imUserPrerelation.getToImUserId(), SessionTypeEnum.P2P, System.currentTimeMillis()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SendMessageEvent event) {
        if (event != null && event.isRlt()) {
            talkMsgList.add(event.getImMessage());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(IMHistoryMessageEvent event) {
        List<IMMessage> hisMsgList = event.getMsgList();
        if (hisMsgList != null && hisMsgList.size() > 0) {
            talkMsgList.addAll(0, hisMsgList);
            List<MyMessage> myMsgList = new ArrayList<MyMessage>();
            for (IMMessage imMessage : hisMsgList) {
                if (imMessage.getMsgType() == MsgTypeEnum.custom) {
                    MsgAttachment msgAttachment = imMessage.getAttachment();
                    if (msgAttachment instanceof GiftAttachment) {
                        GiftAttachment giftAttachment = (GiftAttachment) msgAttachment;
                        GiftMessage message;
                        if (imMessage.getFromAccount().equals(HereUser.getInstance().getImUserInfo().getImUserId())) {
                            message = new GiftMessage("", IMManager.MY_MSG_TYPE_GIFT_SENDER);
                        } else {
                            message = new GiftMessage("", IMManager.MY_MSG_TYPE_GIFT_RECEIVER);
                        }
                        message.init(giftAttachment.getGift());
                        message.setUserInfo(new DefaultUser("0", "", "R.drawable.ic_launcher"));
                        message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));

                        message.setImMessage(imMessage);
                        myMsgList.add(message);
                    } else if (msgAttachment instanceof EmojiAttachment) {
                        EmojiAttachment emojiAttachment = (EmojiAttachment) msgAttachment;
                        EmojiMessage message;
                        if (imMessage.getFromAccount().equals(HereUser.getInstance().getImUserInfo().getImUserId())) {
                            message = new EmojiMessage("", IMManager.MY_MSG_TYPE_EMOJI_SENDER);
                        } else {
                            message = new EmojiMessage("", IMManager.MY_MSG_TYPE_EMOJI_RECEIVER);
                        }
                        message.init(emojiAttachment.getImEmoji());
                        message.setUserInfo(new DefaultUser("0", "", "R.drawable.ic_launcher"));
                        message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                        message.setImMessage(imMessage);
                        myMsgList.add(message);
                    }

                } else {
                    if (imMessage.getMsgType() == MsgTypeEnum.text) {
                        MyMessage message;
                        if (imMessage.getFromAccount().equals(HereUser.getInstance().getImUserInfo().getImUserId())) {
                            message = new MyMessage(imMessage.getContent(), IMManager.MY_MSG_TYPE_TXT_SENDER);
                            if (imMessage.getStatus() == MsgStatusEnum.fail) {
                                message.setMessageStatus(IMessage.MessageStatus.SEND_FAILED);
                            } else {
                                message.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
                            }
                        } else {
                            message = new MyMessage(imMessage.getContent(), IMManager.MY_MSG_TYPE_TXT_RECEIVER);
                            message.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
                        }
                        message.setUserInfo(new DefaultUser("1", "Ironman", "R.drawable.ironman"));
                        message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));

                        System.out.println("TTTTTTT=======" + message + "--" + message.getText() + "==" + message.getMsgId() + "=>" + message.getId());

                        message.setImMessage(imMessage);

                        myMsgList.add(message);
                    }
                }
            }

            if (myMsgList.size() > 0) {
                mAdapter.addToEndChronologically(myMsgList);
//                mAdapter.addToEnd(myMsgList);
            }
        }

        mChatView.getPtrLayout().refreshComplete();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(IMMessageEvent event) {
        List<IMMessage> msgList = event.getMessageList();
        Log.i(TAG, "message is " + msgList.get(0).getContent());
        if (msgList != null && msgList.size() > 0) {
            talkMsgList.addAll(event.getMessageList());
            // 更新界面
            for (IMMessage imMessage : event.getMessageList()) {
                if (imMessage.getMsgType() == MsgTypeEnum.custom) {
                    MsgAttachment msgAttachment = imMessage.getAttachment();
                    if (msgAttachment instanceof GiftAttachment) {
                        GiftAttachment giftAttachment = (GiftAttachment) msgAttachment;
                        System.out.println("GIFT ========>" + giftAttachment.getGift());
                        GiftMessage message = new GiftMessage("", IMManager.MY_MSG_TYPE_GIFT_RECEIVER);
                        message.init(giftAttachment.getGift());
                        message.setUserInfo(new DefaultUser("0", "", "R.drawable.ic_launcher"));
                        message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                        mAdapter.addToStart(message, true);
                    } else if (msgAttachment instanceof EmojiAttachment) {
                        EmojiAttachment emojiAttachment = (EmojiAttachment) msgAttachment;
                        System.out.println("EMOJI -----==>" + emojiAttachment.getImEmoji());
                        EmojiMessage message = new EmojiMessage("", IMManager.MY_MSG_TYPE_EMOJI_RECEIVER);
                        message.init(emojiAttachment.getImEmoji());
                        message.setUserInfo(new DefaultUser("0", "", "R.drawable.ic_launcher"));
                        message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                        mAdapter.addToStart(message, true);
                    }

                } else {
                    if (imMessage.getMsgType() == MsgTypeEnum.text) {
                        MyMessage message = new MyMessage(imMessage.getContent(), IMManager.MY_MSG_TYPE_TXT_RECEIVER);
                        message.setUserInfo(new DefaultUser("1", "Ironman", "R.drawable.ironman"));
                        message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                        message.setMessageStatus(IMessage.MessageStatus.SEND_GOING);
                        mAdapter.addToStart(message, true);
                    }
                }
            }
        }
    }

    public void setListener() {
        closeBtn.setOnClickListener(this);
        moreSetBtn.setOnClickListener(this);
    }

    private GiftLayoutHolder currentSelectGift;
    private ArrayList<View> firstTimeGiftList = new ArrayList<View>();

    class GiftLayoutHolder {
        private ImageView borderIv;
        private ImageView giftIv;
        private TextView giftPriceTv;
        private boolean isSelect = false;
        private GiftProd giftProd;
    }

    public void initFirstTimeGift() {
        if (!imUserPrerelation.isRelation()) {
            mChatInputView.setVisibility(View.GONE);
            firstTimeGiftLayout.setVisibility(View.VISIBLE);
            if (IMManager.GIFT_PROD_LIST != null && IMManager.GIFT_PROD_LIST.size() > 0) {
                firstTimeGiftHoriLayout.removeAllViews();
                LayoutInflater inflater = LayoutInflater.from(getActivityContext());
                for (GiftProd giftProd : IMManager.GIFT_PROD_LIST) {
                    RelativeLayout giftLayout = (RelativeLayout) inflater.inflate(R.layout.item_im_first_time_gift, null);
                    GiftLayoutHolder holder = new GiftLayoutHolder();
                    holder.giftProd = giftProd;
                    holder.giftPriceTv = giftLayout.findViewById(R.id.gift_price_tv);
                    holder.borderIv = giftLayout.findViewById(R.id.gift_select_iv);
                    holder.giftIv = giftLayout.findViewById(R.id.gift_bg_iv);

                    holder.giftPriceTv.setText("" + giftProd.getAmount());
                    holder.giftPriceTv.setVisibility(View.GONE);
                    holder.borderIv.setBackgroundResource(R.drawable.shap_stoke_blue_circle_bg);
                    ImageHelper.displayImage(getActivityContext(), holder.giftIv, giftProd.getImage());
                    giftLayout.setTag(holder);
                    firstTimeGiftList.add(giftLayout);
                    giftLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            selectGift((RelativeLayout) view);
                        }
                    });
                    firstTimeGiftHoriLayout.addView(giftLayout);
                }

                giftOpenImTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (currentSelectGift != null) {
                            mPresenter.imUserApply(imUserPrerelation.getUserInfo().getUserId(), currentSelectGift.giftProd.getProdCode());
                        }
                    }
                });
            }
        }
    }

    private void selectGift(RelativeLayout giftLayout) {
        for (View v : firstTimeGiftList) {
            GiftLayoutHolder holder = (GiftLayoutHolder) v.getTag();
            if (holder.isSelect) {
                holder.isSelect = false;
                holder.borderIv.setVisibility(View.INVISIBLE);
            }
        }

        GiftLayoutHolder holder = (GiftLayoutHolder) giftLayout.getTag();
        holder.isSelect = true;
        holder.borderIv.setVisibility(View.VISIBLE);
        currentSelectGift = holder;
    }

    public void initChatView() {
        this.mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mWindow = getWindow();
        registerProximitySensorListener();
        mChatView = (ChatView) findViewById(R.id.chat_view);
        mChatView.initModule();
        mChatView.getChatInputView().hideMenuItem(MyChatInputView.MENU_ITEM_CAREMA);
        mChatView.setEmojiClickListener(new EmojiClickListener() {
            @Override
            public void onImEmojiClick(IMEmoji imEmoji) {
                EmojiMessage message = new EmojiMessage("", IMManager.MY_MSG_TYPE_EMOJI_SENDER);
                message.init(imEmoji);
                message.setUserInfo(new DefaultUser("0", "", "R.drawable.ic_launcher"));
                message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                mAdapter.addToStart(message, true);

                IMManager.getInstance().sendEmoji(imUserPrerelation.getToImUserId(), message);
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
                mPresenter.imMsgCheck(imUserPrerelation.getToImUserId(), IMManager.ONEONE_IM_TYPE_TEXT, inputStr, message);
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
                    ImTalkActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.addToStart(fMsg, true);
                        }
                    });
                }
            }

            @Override
            public boolean switchToMicrophoneMode() {
                scrollToBottom();
                AndPermission.with(ImTalkActivity.this)
                        .runtime()
                        .permission(Permission.Group.STORAGE, Permission.Group.MICROPHONE)
                        .start();
                return true;
            }

            @Override
            public boolean switchToGalleryMode() {
                scrollToBottom();
                AndPermission.with(ImTalkActivity.this)
                        .runtime()
                        .permission(Permission.Group.STORAGE)
                        .start();
                // If you call updateData, select photo view will try to update data(Last update over 30 seconds.)
                //!!!版本过低
//                mChatView.getChatInputView().getSelectPhotoView().updateData();
                return true;
            }

            @Override
            public boolean switchToCameraMode() {
                scrollToBottom();
                if (!AndPermission.hasPermissions(ImTalkActivity.this, Permission.Group.STORAGE,
                        Permission.Group.CAMERA, Permission.Group.MICROPHONE)) {
                    AndPermission.with(ImTalkActivity.this)
                            .runtime()
                            .permission(Permission.Group.STORAGE, Permission.Group.CAMERA, Permission.Group.MICROPHONE)
                            .start();
                } else {
                    File rootDir = getFilesDir();
                    String fileDir = rootDir.getAbsolutePath() + "/photo";
                    mChatView.setCameraCaptureFile(fileDir, new SimpleDateFormat("yyyy-MM-dd-hhmmss",
                            Locale.getDefault()).format(new Date()));
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
                ImTalkActivity.this.runOnUiThread(new Runnable() {
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
                ToastUtil.show(ImTalkActivity.this, "OnClick select album button");
            }
        });
    }

    public void initView() {
        imBottomDialog = new IMBottomDialog(getActivityContext(), new IMBottomDialog.IMBottomDialogListener() {
            @Override
            public void onSeeUserClick() {
                if (userInfo != null) {
                    ProfileStater.startWithUserInfo(getActivityContext(), userInfo);
                    imBottomDialog.dismiss();
                }
            }

            @Override
            public void onReportClick() {
                if (userInfo != null) {
                    ReportUserActivity.startActivity(getActivityContext(), userInfo);
                    imBottomDialog.dismiss();
                }
            }

            @Override
            public void onDeleteContactClick() {
//                ToastUtil.show(getActivityContext(), "onDeleteContactClick");
                IMManager.deleteRecentContact(imUserPrerelation.getToImUserId());
                mPresenter.imUserDelrealation(imUserPrerelation.getToImUserId());
                ImTalkActivity.this.finish();
            }
        });

        if (userInfo != null) {
            userNameTv.setText(MyTextUtil.getLimitEllipseText(userInfo.getNickname(), 5));
            userPhotoIv.init(userInfo, true);
        }
    }

    public static void startActivity(Context context, IMUserPrerelation imUserPrerelation, UserInfo userInfo) {
        IMManager.clearMsgCount(imUserPrerelation.getToImUserId(), SessionTypeEnum.P2P);
        EventBus.getDefault().post(new EventRefreshMsgBubble());
        Intent it = new Intent(context, ImTalkActivity.class);
        if (userInfo != null)
            imUserPrerelation.setUserInfo(userInfo);
        it.putExtra("pre_relation", imUserPrerelation);
        if (imUserPrerelation.getMyRecentContact() != null) {
            it.putExtra("im_contact", imUserPrerelation.getMyRecentContact().getRecentContact());
            it.putExtra("oneone_contact", imUserPrerelation.getMyRecentContact().getFirstRelation());
        }
        context.startActivity(it);
    }

    @Override
    public MsgPresenter onCreatePresenter() {
        return new MsgPresenter();
    }

    @Override
    public void onFindRecommend(List<FindPageUserInfoDTO> userList, int expire) {
    }

    @Override
    public void onFindSetCondition() {
    }

    @Override
    public void onFindGetCondition(FindCondition findCondition) {
    }

    @Override
    public void onSetLike() {
    }

    @Override
    public void onCancelLike() {
    }

    @Override
    public void onSetNoFeel() {
    }

    @Override
    public void onCancelNoFeel() {
    }

    @Override
    public void onImUserGettoken(IMUser imUser) {
    }

    @Override
    public void onImRefreshToken(IMUser imUser) {
    }

    @Override
    public void onImUserIsrelation(boolean relation) {
    }

    @Override
    public void onImUserPrerelation(IMUserPrerelation imUserPrerelation) {
        this.imUserPrerelation = imUserPrerelation;
    }

    @Override
    public void onImUserApply(int status, String toImUserId) {
        if (status == 0) {
            mChatInputView.setVisibility(View.VISIBLE);
            firstTimeGiftLayout.setVisibility(View.GONE);

            if (imUserPrerelation != null)
                imUserPrerelation.setToImUserId(toImUserId);

            GiftMessage message = new GiftMessage("", IMManager.MY_MSG_TYPE_GIFT_SENDER);
            message.init(currentSelectGift.giftProd);
            message.setUserInfo(new DefaultUser("0", "DeadPool", "R.drawable.deadpool"));
            message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
            mAdapter.addToStart(message, true);

            IMManager.getInstance().sendGiftMsg(toImUserId, message);
        } else if (status == MsgPresenter.DOG_FOOD_NOT_ENOUGH) {
            new NotEnougthDogFoodDialog(getActivityContext()).show();
        }
    }

    @Override
    public void onImUserMsgreply(ApiResult rlt) {
    }

    @Override
    public void onImUserDelrealation(ApiResult rlt) {
    }

    @Override
    public void onImUserFirstRelationList(boolean isLoadMore, int oldCount, int newCount, int count, List<IMFirstRelation> imFirstRelationList) {
    }

    @Override
    public void onImUserRelationList(int count, List<IMFirstRelation> imFirstRelationList) {
    }

    @Override
    public void onImMsgCheck(ApiResult<String> rlt, MyMessage myMessage) {
        if (rlt != null) {
            if (rlt.getStatus() != 0) {
                ToastUtil.show(getActivityContext(), rlt.getMessage());
                myMessage.setMessageStatus(IMessage.MessageStatus.SEND_FAILED);
                IMManager.getInstance().saveTxt(imUserPrerelation.getToImUserId(), myMessage, IMessage.MessageStatus.SEND_FAILED);
//                myMessage.setImMessage(saveMsg);
//                System.out.println("######send" + saveMsg + ";;;;" + saveMsg.getUuid());
                mAdapter.addToStart(myMessage, true);
            } else {
                mAdapter.addToStart(myMessage, true);
                IMManager.getInstance().sendTxt(imUserPrerelation.getToImUserId(), myMessage);
            }
        } else {
            myMessage.setMessageStatus(IMessage.MessageStatus.SEND_FAILED);
            IMManager.getInstance().saveTxt(imUserPrerelation.getToImUserId(), myMessage, IMessage.MessageStatus.SEND_FAILED);
//            System.out.println("######send" + myMessage + ";;;;" + myMessage.getId());
            mAdapter.addToStart(myMessage, true);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onImMsgListEmoji(List<IMEmoji> imEmojiList) {
    }

    @Override
    public void onProdGiftList(int count, List<GiftProd> giftProdList) {
    }

    @Override
    public void onMsgNoteListmeta(boolean isLoadMore, List<MsgMeta> msgBeanList) {
    }

    @Override
    public void onMsgNoteLastmeta(MsgMetaDto msgMetaDto) {
    }

    @Override
    public void onMyLikeList(boolean isLoadMore, LikeListDto listDto) {

    }

    @Override
    public void onLikeMeList(boolean isLoadMore, LikeListDto listDto) {

    }

    @Override
    public void onLikeEachother(boolean isLoadMore, LikeListDto listDto) {

    }

    @Override
    public void onLikeUnread(LikeUnreadListDto likeUnreadListDto) {
    }

    @Override
    public void onGetNotifyList(boolean isRefresh, int count, List<TimeLineInfo> timelineList, List<NotifyListItem> list, long lastReadTime) {

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
    public void onSensorChanged(SensorEvent event) {
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        try {
            if (audioManager.isBluetoothA2dpOn() || audioManager.isWiredHeadsetOn()) {
                return;
            }
            if (mAdapter.getMediaPlayer().isPlaying()) {
                float distance = event.values[0];
                if (distance >= mSensor.getMaximumRange()) {
                    mAdapter.setAudioPlayByEarPhone(0);
                    setScreenOn();
                } else {
                    mAdapter.setAudioPlayByEarPhone(2);
                    ViewHolderController.getInstance().replayVoice();
                    setScreenOff();
                }
            } else {
                if (mWakeLock != null && mWakeLock.isHeld()) {
                    mWakeLock.release();
                    mWakeLock = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setScreenOn() {
        if (mWakeLock != null) {
            mWakeLock.setReferenceCounted(false);
            mWakeLock.release();
            mWakeLock = null;
        }
    }

    private void setScreenOff() {
        if (mWakeLock == null) {
            mWakeLock = mPowerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, TAG);
        }
        mWakeLock.acquire();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close_btn:
                mChatView.closeSoftKeyBoard();
                ImTalkActivity.this.finish();
                break;
            case R.id.more_set_btn:
                imBottomDialog.show();
                break;
            case R.id.gift_open_im_tv:
                mPresenter.imUserApply("95f7e543577a4b7a888dfc38c4ce6ea0", "M01");
                break;
        }
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
//                    ImageHelper.displayImage(getActivityContext(), avatarImageView, );
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
        // If you want to customise your layout, try to create custom ViewHolder:
        // holdersConfig.setSenderTxtMsg(CustomViewHolder.class, layoutRes);
        // holdersConfig.setReceiverTxtMsg(CustomViewHolder.class, layoutRes);
        // CustomViewHolder must extends ViewHolders defined in MsgListAdapter.
        // Current ViewHolders are TxtViewHolder, VoiceViewHolder.

        mAdapter.setOnMsgClickListener(new MsgListAdapter.OnMsgClickListener<MyMessage>() {
            @Override
            public void onMessageClick(MyMessage message) {
                // do something
                if (message.getType() == IMessage.MessageType.RECEIVE_VIDEO.ordinal()
                        || message.getType() == IMessage.MessageType.SEND_VIDEO.ordinal()) {
                    if (!TextUtils.isEmpty(message.getMediaFilePath())) {
                        Intent intent = new Intent(ImTalkActivity.this, VideoActivity.class);
                        intent.putExtra(VideoActivity.VIDEO_PATH, message.getMediaFilePath());
                        startActivity(intent);
                    }
                } else if (message.getType() == IMessage.MessageType.RECEIVE_IMAGE.ordinal()
                        || message.getType() == IMessage.MessageType.SEND_IMAGE.ordinal()) {
                    Intent intent = new Intent(ImTalkActivity.this, BrowserImageActivity.class);
                    intent.putExtra("msgId", message.getMsgId());
                    intent.putStringArrayListExtra("pathList", mPathList);
                    intent.putStringArrayListExtra("idList", mMsgIdList);
                    startActivity(intent);
                } else {
                    ToastUtil.show(ImTalkActivity.this, "R.string.message_click_hint");
                }
            }
        });

        mAdapter.setMsgLongClickListener(new MsgListAdapter.OnMsgLongClickListener<MyMessage>() {
            @Override
            public void onMessageLongClick(View view, MyMessage message) {
                ToastUtil.show(ImTalkActivity.this, "R.string.message_long_click_hint");
                // do something
            }
        });

        mAdapter.setOnAvatarClickListener(new MsgListAdapter.OnAvatarClickListener<MyMessage>() {
            @Override
            public void onAvatarClick(MyMessage message) {
                DefaultUser userInfo = (DefaultUser) message.getFromUser();
                ToastUtil.show(ImTalkActivity.this, "R.string.avatar_click_hint");
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
        // Deprecated, should use onRefreshBegin to load next page
        mAdapter.setOnLoadMoreListener(new MsgListAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore(int page, int totalCount) {
//                Log.i("MessageListActivity", "Loading next page");
//                loadNextPage();
            }
        });

        mChatView.setAdapter(mAdapter);
//        mAdapter.getLayoutManager().scrollToPosition(0);
        scrollToBottom();

        IMManager.getInstance().setAdapter(mAdapter);
    }

    private void loadNextPage() {
        getHistoryMsg();
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
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                MyChatInputView chatInputView = mChatView.getChatInputView();
                if (chatInputView.getMenuState() == View.VISIBLE) {
                    chatInputView.dismissMenuLayout();
                }
                try {
                    View v = getCurrentFocus();
                    if (mImm != null && v != null) {
                        mImm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                        view.clearFocus();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case MotionEvent.ACTION_UP:
                view.performClick();
                break;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        IMManager.clearMsgCount(imUserPrerelation.getToImUserId(), SessionTypeEnum.P2P);
        unregisterReceiver(mReceiver);
        mSensorManager.unregisterListener(this);
    }
}
