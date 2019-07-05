package com.oneone.modules.profile.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.oneone.HereSingletonFactory;
import com.oneone.R;
import com.oneone.event.EventProfileStoryUpdate;
import com.oneone.framework.ui.BaseListActivity;
import com.oneone.framework.ui.BaseRecyclerViewAdapter;
import com.oneone.framework.ui.BaseViewHolder;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;
import com.oneone.framework.ui.imagepicker.ImagePicker;
import com.oneone.framework.ui.imagepicker.bean.ImageItem;
import com.oneone.framework.ui.imagepicker.ui.ImageGridActivity;
import com.oneone.framework.ui.utils.ScreenUtil;
import com.oneone.framework.ui.widget.SimpleRecyclerView;
import com.oneone.modules.mystory.bean.MyStoryPreviewBean;
import com.oneone.modules.mystory.bean.StoryImg;
import com.oneone.modules.mystory.contract.StoryContract;
import com.oneone.modules.mystory.dto.MyStoryImgDTO;
import com.oneone.modules.mystory.presenter.StoryPresenter;
import com.oneone.modules.profile.adapter.StoryPhotoUploadAdapter;
import com.oneone.modules.support.qiniu.PhotoUploadListener;
import com.oneone.modules.support.qiniu.UploadParam;

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
@Route(path = "/profile/story/photo/upload")
@LayoutResource(R.layout.activity_story_photo_upload)
@ToolbarResource(title = R.string.str_edit_mystory, background = R.color.transparent, navigationIcon = R.drawable.ic_btn_back_dark)
public class StoryPhotoUploadActivity extends BaseListActivity<StoryImg, StoryPresenter, StoryContract.View>
        implements StoryContract.View, StoryContract.OnEditStoryListener, BaseViewHolder.ItemClickListener<StoryImg>
        , PhotoUploadListener {
    public static final int PHOTO_COUNT_MAX_LIMIT = 9;
    public static final int REQ_CODE_IMAGE_SELECT_INIT = 2000;
    public static final int REQ_CODE_IMAGE_SELECT_CONTINUE = 2001;
    public static final int REQ_CODE_IMAGE_REPLACE = 2002;
    public static final int UPLOAD_GROUP_ID = 1;

    public static void startActivity(Context context, MyStoryPreviewBean storyPreviewBean) {
        ARouter.getInstance().build("/profile/story/photo/upload")
                .withParcelable("storyPreviewBean", storyPreviewBean)
                .navigation(context);
    }

    @BindView(R.id.activity_story_photo_upload_recycler_view)
    SimpleRecyclerView<StoryImg> mSimpleRecyclerView;

    @BindView(R.id.activity_story_photo_upload_btn)
    Button mBtnContinue;

    int preCount;

    @Autowired
    MyStoryPreviewBean storyPreviewBean;
    List<StoryImg> prePhotoList;

    private int updatePosition;

    private ArrayList<ImageItem> mSelectedImageItems = null;

    @Override
    public StoryPresenter onCreatePresenter() {
        return new StoryPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        setRightTextMenu(R.string.str_menu_save)
                .setTextColor(getResources().getColor(R.color.color_685AED));
        mImmersionBar.keyboardEnable(true).init();
        if (storyPreviewBean != null) {
            prePhotoList = storyPreviewBean.getImgs();
            if (prePhotoList != null) {
                preCount = prePhotoList.size();
            }
        }
        int limit = PHOTO_COUNT_MAX_LIMIT - preCount;
        startSelectImage(limit, REQ_CODE_IMAGE_SELECT_INIT, null);
    }

    @NonNull
    @Override
    public BaseRecyclerViewAdapter<StoryImg> adapterToDisplay() {
        return new StoryPhotoUploadAdapter(this, this);
    }

    @NonNull
    @Override
    public SimpleRecyclerView<StoryImg> getDisplayView() {
        return mSimpleRecyclerView;
    }

    @Override
    public void onRightTextMenuClick(View view) {
        super.onRightTextMenuClick(view);
        List<StoryImg> data = getAdapter().getList();
        if (data == null || data.isEmpty()) {
            return;
        }
        List<UploadParam> uploadParams = new ArrayList<>();
        for (StoryImg img : data) {
            UploadParam param = new UploadParam(UPLOAD_GROUP_ID, img.getUrl(), this);
            uploadParams.add(param);
        }
        showLoading("");
        HereSingletonFactory.getInstance().getPhotoUploadManager().enqueueWithGroup(uploadParams);
    }

    @Override
    public void onStoryUpdate(MyStoryPreviewBean bean) {
        loadingDismiss();
        EventBus.getDefault().post(new EventProfileStoryUpdate());
        this.finish();
    }

    @OnClick(R.id.activity_story_photo_upload_btn)
    public void onBtnContinueClick(View view) {
        int limit = PHOTO_COUNT_MAX_LIMIT - preCount;
        startSelectImage(limit, REQ_CODE_IMAGE_SELECT_CONTINUE, mSelectedImageItems);
    }

    @Override
    public void onItemClick(StoryImg storyImg, int id, int position) {
        updatePosition = position;
        ImageItem item = mSelectedImageItems.get(position);
        ArrayList<ImageItem> selectedItems = new ArrayList<>();
        selectedItems.add(item);
        startSelectImage(1, REQ_CODE_IMAGE_REPLACE, selectedItems);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            if (requestCode == REQ_CODE_IMAGE_SELECT_INIT) {
                finish();
            }
            return;
        }

        ArrayList<ImageItem> resultList = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
        if (resultList == null || resultList.isEmpty()) {
            if (requestCode == REQ_CODE_IMAGE_SELECT_INIT) {
                finish();
            }
            return;
        }

        if (requestCode == REQ_CODE_IMAGE_REPLACE) {
            mSelectedImageItems.remove(updatePosition);
            ImageItem item = resultList.get(0);
            mSelectedImageItems.add(updatePosition, item);
        } else {
            mSelectedImageItems = resultList;
        }

        List<StoryImg> result = convertImageList(mSelectedImageItems);
        mSimpleRecyclerView.setData(result);
        int limit = PHOTO_COUNT_MAX_LIMIT - preCount;
        if (limit == mSelectedImageItems.size()) {
            mBtnContinue.setVisibility(View.INVISIBLE);
        } else {
            mBtnContinue.setVisibility(View.VISIBLE);
        }
    }

    private List<StoryImg> convertImageList(List<ImageItem> list) {
        if (list == null) {
            return null;
        }
        //groupIndex 计算
        int groupIndex = mPresenter.findMaxGroupIndex(storyPreviewBean);
        int orderIndex = 0;
        List<StoryImg> result = new ArrayList<>();
        for (ImageItem item : list) {
            groupIndex++;
            orderIndex++;
            StoryImg storyImg = new StoryImg();
            storyImg.setUrl(item.path);
            storyImg.setHeight(item.height);
            storyImg.setWidth(item.width);
            storyImg.setGroupIndex(groupIndex);
            storyImg.setOrderIndex(orderIndex);
            result.add(storyImg);
        }
        return result;
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

    @Override
    public void onUploadCompleted(UploadParam param, List<UploadParam> group, boolean isAllEnd) {
        if (param.groupId == UPLOAD_GROUP_ID && isAllEnd) {

            ArrayList<StoryImg> imgs = new ArrayList<>();
            for (UploadParam up : group) {
                StoryImg img = ((StoryPhotoUploadAdapter) getAdapter()).getStoryImageByPath(up.filePath);
                if (img != null) {
                    img.setUrl(up.getRemotePath());
                    imgs.add(img);
                }
            }
            if (prePhotoList == null) {
                prePhotoList = new ArrayList<>();
            }
            prePhotoList.addAll(imgs);

            MyStoryImgDTO dto = new MyStoryImgDTO();
            dto.setPhotos((ArrayList<StoryImg>) prePhotoList);

            Gson gson = new Gson();
            String json = gson.toJson(dto);
            mPresenter.updateStory(json, this);
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
}
