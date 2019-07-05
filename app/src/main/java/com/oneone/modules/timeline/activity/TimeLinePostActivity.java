package com.oneone.modules.timeline.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.oneone.BaseActivity;
import com.oneone.HereSingletonFactory;
import com.oneone.R;
import com.oneone.framework.android.analytics.annotation.Alias;
import com.oneone.framework.android.utils.SoftKeyBoardUtil;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;
import com.oneone.framework.ui.imagepicker.bean.ImageItem;
import com.oneone.modules.timeline.bean.TimeLine;
import com.oneone.modules.timeline.bean.TimeLineCompose;
import com.oneone.modules.timeline.bean.TimeLineImage;
import com.oneone.modules.timeline.contract.TimeLineContract;
import com.oneone.modules.timeline.view.TimelinePostImageView;
import com.oneone.schema.SchemaUtil;
import com.oneone.utils.StringUtil;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author qingfei.chen
 * @version V1.0.0
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 * @since 2018/6/21.
 */
@Route(path = "/timeline/post")
@Alias("动态发布")
@LayoutResource(R.layout.activity_timeline_post)
@ToolbarResource(navigationIcon = R.drawable.ic_btn_back_dark, background = R.color.transparent, title = R.string.empty_str)
public class TimeLinePostActivity extends BaseActivity
        implements TextWatcher, TimelinePostImageView.OnImageChangeListener {
    public static final String EXTRA_POST_TYPE = "post_type";
    public static final String EXTRA_TOPIC = "topic";
    public static final int POST_TYPE_TEXT = 0;
    public static final int POST_TYPE_IMAGE_TEXT = 1;
    public static final int TEXT_MAX_LIMIT = 140;

    public static void startPostTextActivity(Context context) {
        Intent intent = new Intent(context, TimeLinePostActivity.class);
        intent.putExtra(EXTRA_POST_TYPE, POST_TYPE_TEXT);
        context.startActivity(intent);
    }

    public static void startPostImageTextActivity(Context context) {
        Intent intent = new Intent(context, TimeLinePostActivity.class);
        intent.putExtra(EXTRA_POST_TYPE, POST_TYPE_IMAGE_TEXT);
        context.startActivity(intent);
    }

    public static void startPostTextWithTopicActivity(Context context, String topic) {
        Intent intent = new Intent(context, TimeLinePostActivity.class);
        intent.putExtra(EXTRA_POST_TYPE, POST_TYPE_TEXT);
        intent.putExtra(EXTRA_TOPIC, topic);
        context.startActivity(intent);
    }

    public static void startPostImageTextWithTopicActivity(Context context, String topic) {
        Intent intent = new Intent(context, TimeLinePostActivity.class);
        intent.putExtra(EXTRA_POST_TYPE, POST_TYPE_IMAGE_TEXT);
        intent.putExtra(EXTRA_TOPIC, topic);
        context.startActivity(intent);
    }

    @BindView(R.id.activity_timeline_post_image_view)
    TimelinePostImageView mPostImageView;

    @BindView(R.id.activity_timeline_post_edit)
    EditText mEtContent;

    @BindView(R.id.activity_timeline_post_tv_text_max_limit)
    TextView mTvLimit;

    private int mType;
    private String mTopic;
    private boolean hasImage;
    private TimeLineContract.INewTimeLineManager mINewTimeLineManager;

    private TimeLine unSendTimeLine;

    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);
        mType = intent.getIntExtra(EXTRA_POST_TYPE, POST_TYPE_IMAGE_TEXT);
        mTopic = intent.getStringExtra(EXTRA_TOPIC);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRightTextMenu(R.string.str_timeline_post).setEnabled(false);
        mImmersionBar.statusBarDarkFont(true)
                .navigationBarColor(R.color.color_black)
                .keyboardEnable(true).init();
        initViewByType();
        mEtContent.addTextChangedListener(this);
        mPostImageView.setOnImageChangeListener(this);
        setLeftCount();
        restoreUnSendTimeline();
        checkPermissions();
    }

    private void restoreUnSendTimeline() {
        mINewTimeLineManager = HereSingletonFactory.getInstance().getNewTimeLineManager();
        switch (mType) {
            case POST_TYPE_TEXT:
                unSendTimeLine = mINewTimeLineManager.getTimeLine4Text();
                break;
            case POST_TYPE_IMAGE_TEXT:
                unSendTimeLine = mINewTimeLineManager.getTimeLine4ImageText();
                break;
        }

        if (unSendTimeLine == null) {
            insertInput(mTopic);
            return;
        }

        TimeLineCompose timelineDetail = unSendTimeLine.getDetail().getDetail();
        if (timelineDetail == null) {
            return;
        }

        mEtContent.setText(timelineDetail.getContent());
        if (timelineDetail.getTimelineImgs() != null) {
            List<ImageItem> selectedImageItem = new ArrayList<>();
            for (TimeLineImage image : timelineDetail.getTimelineImgs()) {
                ImageItem item = new ImageItem();
                item.path = image.getUrl();
                item.height = image.getHeight();
                item.width = image.getWidth();
                selectedImageItem.add(item);
            }
            mPostImageView.setImageDataList(selectedImageItem);
        }
    }

    @Override
    public void onRightTextMenuClick(View view) {
        super.onRightTextMenuClick(view);
        mINewTimeLineManager.newTimeLine(mEtContent.getText().toString(), getTimeLineImage());
        SoftKeyBoardUtil.hideSoftInput(this);
        SchemaUtil.doRouter(this, "oneone://m.oneone.com/home/tab?tabCurrent=timeline");
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TimeLineTopicSearchActivity.CODE_TOPIC_SELECTED) {
            if (data == null) {
                return;
            }

            String topic = data.getStringExtra(TimeLineTopicSearchActivity.CODE_TOPIC_RESULT);
            insertInput(topic);
            return;
        }

        mPostImageView.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onImageChange(boolean hasImage) {
        this.hasImage = hasImage;
        refreshMenu();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        setLeftCount();
        refreshMenu();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @OnClick(R.id.activity_timeline_post_tv_topic)
    public void onTopicClick(View view) {
        TimeLineTopicSearchActivity.startActivityForResult(this);
    }

    private void initViewByType() {
        switch (mType) {
            case POST_TYPE_TEXT:
                setTitleString(getString(R.string.str_timeline_post_text));
                mPostImageView.setVisibility(View.GONE);
                break;

            case POST_TYPE_IMAGE_TEXT:
                setTitleString(getString(R.string.str_timeline_post_image_text));
                mPostImageView.setVisibility(View.VISIBLE);
                break;
        }
    }

    private int getInputCount() {
        return StringUtil.getLength(mEtContent.getText().toString());
    }

    private void setLeftCount() {
        mTvLimit.setText(TEXT_MAX_LIMIT - getInputCount() + "");
    }

    private void insertInput(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        Editable editable = mEtContent.getText();
        int index = mEtContent.getSelectionStart();
        editable.insert(index, "#" + text + "#");
    }

    private void refreshMenu() {
        if (hasImage || (getInputCount() <= TEXT_MAX_LIMIT)) {
            this.getRightTextMenu().setEnabled(true);
            mTvLimit.setTextColor(getResources().getColor(R.color.color_C4CFE1));
            if (getInputCount() > TEXT_MAX_LIMIT) {
                this.getRightTextMenu().setEnabled(false);
                mTvLimit.setTextColor(getResources().getColor(R.color.red));
            }
        } else {
            this.getRightTextMenu().setEnabled(false);
            mTvLimit.setTextColor(getResources().getColor(R.color.red));
        }
    }

    private List<TimeLineImage> getTimeLineImage() {
        if (mPostImageView.getSelectedImages().isEmpty()) {
            return null;
        }

        List<TimeLineImage> timeLineImages = new ArrayList<>();
        for (int i = 0; i < mPostImageView.getSelectedImages().size(); i++) {
            TimeLineImage timeLineImage = new TimeLineImage();
            timeLineImage.setOrderIndex(i);
            timeLineImage.setUrl(mPostImageView.getSelectedImages().get(i).path);
            timeLineImage.setHeight(mPostImageView.getSelectedImages().get(i).height);
            timeLineImage.setWidth(mPostImageView.getSelectedImages().get(i).width);
            timeLineImages.add(timeLineImage);
        }

        return timeLineImages;
    }

    private void checkPermissions() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.Group.STORAGE, Permission.Group.CAMERA)
                .start();
    }

}
