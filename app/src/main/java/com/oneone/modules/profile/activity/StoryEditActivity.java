package com.oneone.modules.profile.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.oneone.BaseActivity;
import com.oneone.HereSingletonFactory;
import com.oneone.R;
import com.oneone.event.EventProfileStoryUpdate;
import com.oneone.framework.android.utils.SoftKeyBoardUtil;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;
import com.oneone.framework.ui.imagepicker.ImagePicker;
import com.oneone.framework.ui.imagepicker.bean.ImageItem;
import com.oneone.framework.ui.imagepicker.ui.ImageGridActivity;
import com.oneone.framework.ui.utils.ScreenUtil;
import com.oneone.modules.mystory.bean.MyStoryPreviewBean;
import com.oneone.modules.mystory.bean.StoryImg;
import com.oneone.modules.mystory.contract.StoryContract;
import com.oneone.modules.mystory.dto.MyStoryImgDTO;
import com.oneone.modules.mystory.presenter.StoryPresenter;
import com.oneone.modules.support.qiniu.PhotoUploadListener;
import com.oneone.modules.support.qiniu.UploadParam;
import com.oneone.utils.ImageHelper;
import com.oneone.widget.InputTextWatcher;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author qingfei.chen
 * @since 2018/7/16.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@Route(path = "/profile/story/edit")
@ToolbarResource(title = R.string.str_edit_mystory, background = R.color.transparent, navigationIcon = R.drawable.ic_btn_back_dark)
@LayoutResource(R.layout.activity_mystory_edit)
public class StoryEditActivity extends BaseActivity<StoryPresenter, StoryContract.View>
        implements StoryContract.View, StoryContract.OnEditStoryListener, PhotoUploadListener {
    public static final int REQ_CODE_IMAGE_REPLACE = 2002;
    public static final int UPLOAD_GROUP_ID = 1;

    public static void startActivity(Context context, MyStoryPreviewBean storyBean, int groupIndex) {
        ARouter.getInstance().build("/profile/story/edit")
                .withInt("groupIndex", groupIndex)
                .withParcelable("storyBean", storyBean)
                .navigation(context);
    }

    @BindView(R.id.activity_mystory_edit_iv_photo)
    ImageView mIvPhoto;

    @BindView(R.id.activity_mystory_edit_iv_photo_camera)
    ImageView mIvPhotoCamera;

    @BindView(R.id.activity_mystory_edit)
    EditText mEtContent;
    @BindView(R.id.activity_mystory_tv_max_limit)
    TextView mEtContentLimitNum;

    @Autowired
    MyStoryPreviewBean storyBean;

    @Autowired
    int groupIndex;

    StoryImg mStoryImg;

    ImageItem mSelectedImage;

    @Override
    public StoryPresenter onCreatePresenter() {
        return new StoryPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);

        mStoryImg = mPresenter.findStoryByGroupIndex(storyBean, groupIndex);
        if (mStoryImg == null) {
            finish();
            return;
        }
        setRightTextMenu(R.string.str_menu_save)
                .setTextColor(getResources().getColorStateList(R.color.color_selector_menu_text_blue));

        mEtContent.addTextChangedListener(new InputTextWatcher(this, mEtContent, InputTextWatcher.STORY_MAX_LENGTH,
                0, new InputTextWatcher.CurrentInputTextListener() {
            @Override
            public void onCurrentInputTextListener(int num) {
                mEtContentLimitNum.setText(num + "");
                refreshSaveBtn();
            }
        }));
        initView();
    }


    @Override
    public void onRightTextMenuClick(View view) {
        super.onRightTextMenuClick(view);
        SoftKeyBoardUtil.hideSoftInput(this);
        if (mStoryImg == null) {
            this.finish();
            SoftKeyBoardUtil.hideSoftInput(this);
            return;
        }

        if (mSelectedImage == null) {
            postUpdate(null);
            return;
        }

        UploadParam param = new UploadParam(UPLOAD_GROUP_ID, mSelectedImage.path, this);
        HereSingletonFactory.getInstance().getPhotoUploadManager().enqueue(param);
    }

    private void initView() {
        if (mStoryImg == null) {
            return;
        }

        ImageHelper.displayImage(this, mIvPhoto, mStoryImg.getUrl());
        mEtContent.setText(mStoryImg.getCaption());
        if (!TextUtils.isEmpty(mStoryImg.getCaption())) {
            mEtContent.setSelection(mStoryImg.getCaption().length());
            mEtContentLimitNum.setText(mStoryImg.getCaption().length() + "");
        }
    }

    @Override
    public void onStoryUpdate(MyStoryPreviewBean bean) {
        EventBus.getDefault().post(new EventProfileStoryUpdate());
        SoftKeyBoardUtil.hideSoftInput(this);
        this.finish();
    }

    @OnClick(R.id.activity_mystory_edit_iv_photo_camera)
    public void onCameraClick(View view) {
        startSelectImage(1, REQ_CODE_IMAGE_REPLACE, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }

        ArrayList<ImageItem> resultList = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
        if (resultList == null || resultList.isEmpty()) {
            return;
        }

        if (requestCode == REQ_CODE_IMAGE_REPLACE) {
            mSelectedImage = resultList.get(0);
            if (mSelectedImage != null) {
                ImageHelper.displayImage(this, mIvPhoto, mSelectedImage.path);
            }
            refreshSaveBtn();
        }

    }

    @Override
    public void onUploadCompleted(UploadParam param, List<UploadParam> group, boolean isAllEnd) {
        if (TextUtils.equals(param.filePath, mSelectedImage.path)) {
            postUpdate(param);
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

    private void postUpdate(UploadParam uploadParam) {
        String caption = mEtContent.getText().toString();
        if (!TextUtils.equals(mStoryImg.getCaption(), caption)) {
            mStoryImg.setCaption(caption);
        }

        if (uploadParam != null && !TextUtils.equals(uploadParam.getRemotePath(), mStoryImg.getUrl())) {
            mStoryImg.setUrl(uploadParam.getRemotePath());
            mStoryImg.setWidth(uploadParam.getWidth());
            mStoryImg.setHeight(uploadParam.getHeight());
        }

        Gson gson = new Gson();
        MyStoryImgDTO storyRequest = new MyStoryImgDTO();
        storyRequest.setPhotos(storyBean.getImgs());
        mPresenter.updateStory(gson.toJson(storyRequest), this);
    }

    private void startSelectImage(int limit, int reqCode, ArrayList<ImageItem> selectedImages) {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setShowCamera(true);
        imagePicker.setSaveRectangle(true);
        imagePicker.setCrop(true);
        imagePicker.setSelectLimit(limit);
        imagePicker.setMultiMode(true);
        imagePicker.setFocusWidth(ScreenUtil.screenWidth);
        imagePicker.setFocusHeight(ScreenUtil.screenHeight);
        imagePicker.setOutPutX(1000);
        imagePicker.setOutPutY(1000);
        Intent intent1 = new Intent(getActivityContext(), ImageGridActivity.class);
        if (selectedImages != null && !selectedImages.isEmpty()) {
            intent1.putParcelableArrayListExtra(ImageGridActivity.EXTRAS_IMAGES, selectedImages);
        }
        startActivityForResult(intent1, reqCode);
    }

    private void refreshSaveBtn() {
        String caption = mEtContent.getText().toString();
        if (!TextUtils.equals(mStoryImg.getCaption(), caption) || mSelectedImage != null) {
            getRightTextMenu().setEnabled(true);
        } else {
            getRightTextMenu().setEnabled(false);
        }
    }

}
