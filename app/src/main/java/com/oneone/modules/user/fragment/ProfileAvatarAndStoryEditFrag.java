package com.oneone.modules.user.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.oneone.Constants;
import com.oneone.HereSingletonFactory;
import com.oneone.R;
import com.oneone.event.EventNextStep;
import com.oneone.event.EventProfileUpdateByRole;
import com.oneone.framework.android.analytics.annotation.Alias;
import com.oneone.framework.ui.BasePresenterFragment;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.imagepicker.ImagePicker;
import com.oneone.framework.ui.imagepicker.bean.ImageItem;
import com.oneone.framework.ui.imagepicker.ui.ImageGridActivity;
import com.oneone.framework.ui.utils.PermissionsUtil;
import com.oneone.framework.ui.utils.ScreenUtil;
import com.oneone.modules.mystory.bean.MyStoryPreviewBean;
import com.oneone.modules.mystory.bean.StoryImg;
import com.oneone.modules.mystory.contract.StoryContract;
import com.oneone.modules.mystory.dto.MyStoryImgDTO;
import com.oneone.modules.mystory.presenter.StoryPresenter;
import com.oneone.modules.support.qiniu.PhotoUploadListener;
import com.oneone.modules.support.qiniu.UploadParam;
import com.oneone.modules.timeline.activity.PermissionsWarnActivity;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.UserSP;
import com.oneone.modules.user.bean.UserProfileUpdateBean;
import com.oneone.support.qiniu.UploadObj;
import com.oneone.utils.ImageHelper;
import com.oneone.utils.ToastUtil;
import com.oneone.widget.InputTextWatcher;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import butterknife.BindView;


@Alias("头像和生活照")
@LayoutResource(R.layout.frag_profile_avatar_and_story_edit)
public class ProfileAvatarAndStoryEditFrag extends BasePresenterFragment<StoryPresenter, StoryContract.View> implements PhotoUploadListener, StoryContract.View {

    public static final int REQ_PICKER_AVATAR = 10000;
    public static final int REQ_PICKER_PICTURE = 10001;
    public static final int REQ_PICKER_PICTURE_MODIFY = 10002;
    public static final int GROUP_ID_STORY_IMG = 1000;
    public static final int GROUP_ID_AVATAR = 1001;

    @BindView(R.id.upload_pic_bottom_step_1_choose_pic_layout)
    RelativeLayout uploadPicBottomStep1ChoosePicLayout;
    @BindView(R.id.upload_pic_bottom_step_1_choose_pic_tv)
    TextView uploadPicBottomStep1ChoosePicTv;
    @BindView(R.id.upload_pic_bottom_step_2_choose_pic_layout)
    RelativeLayout uploadPicBottomStep2ChoosePicLayout;
    @BindView(R.id.upload_pic_bottom_step_2_choose_pic_tv)
    TextView uploadPicBottomStep2ChoosePicTv;

    @BindView(R.id.upload_pic_bottom_step_1_pic_demo_layout)
    LinearLayout mLLAvatarDemo;
    @BindView(R.id.upload_pic_bottom_step_1_pic_content_iv)
    ImageView mIvAvatar;

    @BindView(R.id.upload_pic_bottom_step_2_pic_demo_layout)
    LinearLayout uploadPicBottomStep2PicDemoLayout;
    @BindView(R.id.upload_pic_bottom_step_2_pic_item_content_layout)
    LinearLayout uploadPicBottomStep2PicContentLayout;

    private TreeSet<StoryImgHolder> uploadImgHolderSet = new TreeSet<StoryImgHolder>();
//    private ArrayList<StoryImgHolder> uploadFinishHolderList = new ArrayList<StoryImgHolder>();

    private StoryImgHolder currentStoryImgHolder;
    private StoryImgHolder mAvatarStoryImgHolder;
    MyStoryImgDTO myStoryImgRequest = new MyStoryImgDTO();
    public static final int LIFE_IMG_LIMIT_COUNT = 9;
    UserProfileUpdateBean profileUpdateBean;

    private List<ImageItem> mPictureSelected = new ArrayList<>();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        checkPermissions();
        EventBus.getDefault().register(this);
        uploadPicBottomStep1ChoosePicLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!PermissionsUtil.checkPermissions(getContext(), PermissionsUtil.READ_EXTERNAL_STORAGE) &&
                        !PermissionsUtil.checkPermissions(getContext(), PermissionsUtil.WRITE_EXTERNAL_STORAGE)) {
                    PermissionsWarnActivity.startActivity(getContext(), PermissionsWarnActivity.EXTERNAL_STORAGE);
                } else if (!PermissionsUtil.checkPermissions(getContext(), PermissionsUtil.CAMERA)) {
                    PermissionsWarnActivity.startActivity(getContext(), PermissionsWarnActivity.CAMERA);
                } else {
                    startSelectImage(1, REQ_PICKER_AVATAR, true);
                }
            }
        });

        uploadPicBottomStep2ChoosePicLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (uploadPicBottomStep2PicContentLayout.getChildCount() < LIFE_IMG_LIMIT_COUNT) {
                    if (!PermissionsUtil.checkPermissions(getContext(), PermissionsUtil.READ_EXTERNAL_STORAGE) &&
                            !PermissionsUtil.checkPermissions(getContext(), PermissionsUtil.WRITE_EXTERNAL_STORAGE)) {
                        PermissionsWarnActivity.startActivity(getContext(), PermissionsWarnActivity.EXTERNAL_STORAGE);
                    } else if (!PermissionsUtil.checkPermissions(getContext(), PermissionsUtil.CAMERA)) {
                        PermissionsWarnActivity.startActivity(getContext(), PermissionsWarnActivity.CAMERA);
                    } else {
                        startSelectImage(LIFE_IMG_LIMIT_COUNT, REQ_PICKER_PICTURE, false);
                    }
                } else {
                    ToastUtil.show(getContext(), getString(R.string.str_set_single_flow_page_step_11_max_pic_upload_notice));
                }
            }
        });

//        restoreAvatar();
//        restoreStory();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            EventBus.getDefault().register(this);
        } else {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public StoryPresenter onPresenterCreate() {
        return new StoryPresenter();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void uploadStoryImage() {
        if (uploadImgHolderSet.size() > 0) {
            List<UploadParam> params = new ArrayList<UploadParam>();
            for (StoryImgHolder storyImgHolder : uploadImgHolderSet) {
                UploadParam uploadParam = new UploadParam(GROUP_ID_STORY_IMG, storyImgHolder.getUploadUri().getPath(), this);
                params.add(uploadParam);
            }
            HereSingletonFactory.getInstance().getPhotoUploadManager().enqueueWithGroup(params);
        }
    }

    private void uploadAvatarAndStoryImage() {
        if (mIvAvatar.getVisibility() == View.VISIBLE) {
            UploadParam photoParam = new UploadParam(GROUP_ID_AVATAR, mAvatarStoryImgHolder.getUploadUri().getPath(), this);
            showLoading("");
            HereSingletonFactory.getInstance().getPhotoUploadManager().enqueue(photoParam);
            uploadStoryImage();
        } else {
            ToastUtil.show(getContext(), "请上传用户头像");
        }
    }

    private void updateStoryImage(List<UploadParam> group) {
        if (uploadImgHolderSet.size() > 0) {
            myStoryImgRequest.setPhotos(new ArrayList<StoryImg>());
            int i = 0;
            for (StoryImgHolder sImgItem : uploadImgHolderSet) {
                String captionStr = sImgItem.storyEt.getText().toString();
                sImgItem.storyImg.setCaption(captionStr);
                sImgItem.storyImg.setUrl(group.get(i).getRemoteUrl());
                myStoryImgRequest.getPhotos().add(sImgItem.storyImg);
                i++;
            }
        } else {
            myStoryImgRequest.setPhotos(null);
        }
        myStoryImgRequest.setPreview(true);
        myStoryImgRequest.setOpenSingle(true);
        mPresenter.updateStory(new Gson().toJson(myStoryImgRequest), new StoryContract.OnEditStoryListener() {
            @Override
            public void onStoryUpdate(MyStoryPreviewBean storyPreviewBean) {
                loadingDismiss();
                String json = new Gson().toJson(storyPreviewBean);
                UserSP.putAndCommit(getContext(), Constants.PREF.PREF_STORY_PREVIEW_BEAN, json);
                EventBus.getDefault().post(new EventProfileUpdateByRole(profileUpdateBean));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNextStepEvent(EventNextStep event) {
        uploadAvatarAndStoryImage();
    }

    @Override
    public void onUploadCompleted(UploadParam param, List<UploadParam> group, boolean isAllEnd) {
        if (param.groupId == GROUP_ID_AVATAR) {
            profileUpdateBean = new UserProfileUpdateBean();
            profileUpdateBean.setAvatar(param.getRemotePath());
            if (uploadImgHolderSet.size() <= 0) {
                updateStoryImage(null);
            }
        } else if (param.groupId == GROUP_ID_STORY_IMG && isAllEnd) {
            updateStoryImage(group);
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
        if (param.groupId == GROUP_ID_STORY_IMG) {
            showLoading("");
        }

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
        Intent intent1 = new Intent(getContext(), ImageGridActivity.class);

        if (reqCode == REQ_PICKER_PICTURE) {
//        /* 设置已经选择的图片 */
            if (mPictureSelected != null && !mPictureSelected.isEmpty()) {
                intent1.putParcelableArrayListExtra(ImageGridActivity.EXTRAS_IMAGES, (ArrayList<? extends Parcelable>) mPictureSelected);
            }
        }
        startActivityForResult(intent1, reqCode);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        if (requestCode == REQ_PICKER_PICTURE) {

            if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
                mPictureSelected = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            }

            if (mPictureSelected == null || mPictureSelected.isEmpty()) {
                return;
            }

            refreshStory();
        } else if (requestCode == REQ_PICKER_PICTURE_MODIFY) {
            List<ImageItem> items = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            if (items != null && !items.isEmpty()) {
                ImageItem item = items.get(0);
                currentStoryImgHolder.setUploadUri(Uri.parse(item.path));
                replaceSelectedItem(item);
                fillPic(currentStoryImgHolder);
            }

        } else if (requestCode == REQ_PICKER_AVATAR) {
            List<ImageItem> items = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            ImageItem item = items.get(0);
            refreshAvatar(item);
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void refreshAvatar(ImageItem item) {
        if (item == null) {
            return;
        }

        mAvatarStoryImgHolder = convertStoryImageFromImageItem(item, 0);
        mIvAvatar.setVisibility(View.VISIBLE);
        ImageHelper.displayImage(getContext(), mIvAvatar, item.path);
        mLLAvatarDemo.setVisibility(View.GONE);

        uploadPicBottomStep1ChoosePicTv.setText(R.string.str_set_single_flow_page_photo_pic_choosed_notice);
    }

    private void refreshStory() {
        int i = 1;
        for (ImageItem item : mPictureSelected) {
            StoryImgHolder holder = convertStoryImageFromImageItem(item, i);
            fillPic(holder);
            i++;
        }
    }

    private void restoreStory() {
        String json = UserSP.getString(getContext(), Constants.PREF.PREF_STORY_PREVIEW_BEAN, "");
        if (!TextUtils.isEmpty(json)) {
            MyStoryPreviewBean bean1 = new Gson().fromJson(json, MyStoryPreviewBean.class);
            ArrayList<StoryImg> storyImgs = bean1.getImgs();
            if (storyImgs == null) {
                return;
            }
            for (StoryImg storyImg : storyImgs) {
                ImageItem item = convertImageItemFromStoryImage(storyImg);
                mPictureSelected.add(item);
            }
        }
        refreshStory();
    }

    private void restoreAvatar() {
        String avatar = HereUser.getInstance().getUserInfo().getMyAvatar();
        ImageItem item = new ImageItem();
        item.path = avatar;
        refreshAvatar(item);
    }

    private ImageItem convertImageItemFromStoryImage(StoryImg storyImg) {

        ImageItem imageItem = new ImageItem();
        imageItem.width = storyImg.getWidth();
        imageItem.height = storyImg.getHeight();
        imageItem.path = storyImg.getUrl();

        return imageItem;
    }

    private StoryImgHolder convertStoryImageFromImageItem(ImageItem imageItem, int index) {

        StoryImgHolder storyImgHolder = new StoryImgHolder();
        StoryImg storyImg = new StoryImg();
        storyImg.setWidth(imageItem.width);
        storyImg.setHeight(imageItem.height);
        storyImg.setGroupIndex(index);
//        storyImg.setOrderIndex(index);
        storyImgHolder.setUploadUri(Uri.parse(imageItem.path));
        storyImgHolder.storyImg = storyImg;
        return storyImgHolder;
    }


    private void fillPic(StoryImgHolder storyImgHolder) {
        StoryImg storyImg = storyImgHolder.storyImg;
        int groupIndex = storyImg.getGroupIndex();
        uploadImgHolderSet.add(storyImgHolder);

        uploadPicBottomStep2PicContentLayout.setVisibility(View.VISIBLE);
        View itemLayout = uploadPicBottomStep2PicContentLayout.getChildAt(groupIndex - 1);
        if (itemLayout == null) {
            uploadPicBottomStep2PicContentLayout.addView(createLifeStoryPicItem(groupIndex, storyImgHolder));
        } else {
            LifeImgItemHolder holder = (LifeImgItemHolder) itemLayout.getTag();

            ImageHelper.displayImage(getContext(), holder.itemImg, storyImgHolder.getUploadUri().getPath());
//            holder.itemImg.setImageBitmap(storyImgHolder.bitmap);
        }
        uploadPicBottomStep2PicDemoLayout.setVisibility(View.GONE);

        int childCount = uploadPicBottomStep2PicContentLayout.getChildCount();
        if (childCount > 0) {
            if (childCount < LIFE_IMG_LIMIT_COUNT) {
                uploadPicBottomStep2ChoosePicLayout.setVisibility(View.VISIBLE);
                uploadPicBottomStep2ChoosePicTv.setText(R.string.str_set_single_flow_page_live_pic_choosed_notice_2);
            } else
                uploadPicBottomStep2ChoosePicLayout.setVisibility(View.GONE);
        } else {
            uploadPicBottomStep2ChoosePicTv.setText(R.string.str_set_single_flow_page_live_pic_choosed_notice_1);
        }

    }

    public RelativeLayout createLifeStoryPicItem(int position, final StoryImgHolder storyImgHolder) {
        LifeImgItemHolder holder = new LifeImgItemHolder();

        final RelativeLayout itemLayout = new RelativeLayout(getContext());
        itemLayout.setTag(holder);
        LinearLayout.LayoutParams itemLayoutParams = new LinearLayout.LayoutParams(ScreenUtil.dip2px(270), ScreenUtil.dip2px(214));
        itemLayoutParams.topMargin = ScreenUtil.dip2px(18);
        itemLayout.setLayoutParams(itemLayoutParams);

        ImageView itemImg = new ImageView(getContext());
        itemImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        RelativeLayout.LayoutParams itemParams = new RelativeLayout.LayoutParams(ScreenUtil.dip2px(270), ScreenUtil.dip2px(122));
        itemParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        itemImg.setLayoutParams(itemParams);
//        itemImg.setImageBitmap(storyImgHolder.bitmap);
        ImageHelper.displayImage(getContext(), itemImg, storyImgHolder.getUploadUri().getPath());
        holder.itemImg = itemImg;
        itemLayout.addView(itemImg);

        TextView itemCountTv = new TextView(getContext());
        itemCountTv.setGravity(Gravity.CENTER);
        itemCountTv.setTextColor(getResources().getColor(R.color.white));
        itemCountTv.setText(position + "");
        itemCountTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        itemCountTv.setBackgroundResource(R.drawable.circle_user_life_pic_num_bg);
        itemParams = new RelativeLayout.LayoutParams(ScreenUtil.dip2px(24), ScreenUtil.dip2px(24));
        itemParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        itemParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        itemParams.leftMargin = ScreenUtil.dip2px(4);
        itemParams.topMargin = ScreenUtil.dip2px(4);
        itemCountTv.setLayoutParams(itemParams);
        itemLayout.addView(itemCountTv);

        ImageView modifyPicIv = new ImageView(getContext());
        itemParams = new RelativeLayout.LayoutParams(ScreenUtil.dip2px(40), ScreenUtil.dip2px(40));
        itemParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        itemParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        itemParams.rightMargin = ScreenUtil.dip2px(8);
        itemParams.topMargin = ScreenUtil.dip2px(83);
        modifyPicIv.setLayoutParams(itemParams);

        modifyPicIv.setBackgroundResource(R.drawable.life_img_modify_img_bg);
        modifyPicIv.setTag(position);
        modifyPicIv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!PermissionsUtil.checkPermissions(getContext(), PermissionsUtil.READ_EXTERNAL_STORAGE) &&
                        !PermissionsUtil.checkPermissions(getContext(), PermissionsUtil.WRITE_EXTERNAL_STORAGE)) {
                    PermissionsWarnActivity.startActivity(getContext(), PermissionsWarnActivity.EXTERNAL_STORAGE);
                } else if (!PermissionsUtil.checkPermissions(getContext(), PermissionsUtil.CAMERA)) {
                    PermissionsWarnActivity.startActivity(getContext(), PermissionsWarnActivity.CAMERA);
                } else {
                    int pos = (int) v.getTag();
                    currentStoryImgHolder = storyImgHolder;
//                currentUseImage = pos;
                    startSelectImage(1, REQ_PICKER_PICTURE_MODIFY, false);
                }
            }
        });


        ImageView remveIv = new ImageView(getContext());
        itemParams = new RelativeLayout.LayoutParams(ScreenUtil.dip2px(27), ScreenUtil.dip2px(27));
        itemParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        itemParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        itemParams.rightMargin = ScreenUtil.dip2px(56);
        itemParams.topMargin = ScreenUtil.dip2px(89);
        remveIv.setLayoutParams(itemParams);
        remveIv.setBackgroundResource(R.drawable.ic_story_delete);
        remveIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImageItem item = findImageItem(storyImgHolder.getUploadUri().getPath());
                if (item != null) {
                    mPictureSelected.remove(item);
                }
                uploadImgHolderSet.clear();
                uploadPicBottomStep2PicContentLayout.removeAllViews();
                refreshStory();
            }
        });

        itemLayout.addView(remveIv);
        itemLayout.addView(modifyPicIv);

        EditText itemEt = new EditText(getContext());
        storyImgHolder.storyEt = itemEt;
        holder.itemEt = itemEt;
        itemEt.setBackgroundColor(getResources().getColor(R.color.green_blue));
        itemEt.setPadding(ScreenUtil.dip2px(14), ScreenUtil.dip2px(12), ScreenUtil.dip2px(14), ScreenUtil.dip2px(12));
        itemEt.setGravity(Gravity.LEFT | Gravity.TOP);
        itemEt.setHint(R.string.str_set_single_flow_page_step_11_pic_story);
        itemEt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        itemEt.addTextChangedListener(new InputTextWatcher(getContext(), itemEt, InputTextWatcher.STORY_MAX_LENGTH, 0));
        itemEt.setTextColor(getResources().getColor(R.color.text_wx_auth_page_tag_1));
        itemParams = new RelativeLayout.LayoutParams(ScreenUtil.dip2px(270), ScreenUtil.dip2px(84));
        itemParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        itemParams.topMargin = ScreenUtil.dip2px(130);
        itemEt.setLayoutParams(itemParams);
        itemLayout.addView(itemEt);

        return itemLayout;
    }


    public class StoryImgHolder extends UploadObj implements Comparable {
        public EditText storyEt;
        public StoryImg storyImg;
        public Bitmap bitmap;

        @Override
        public boolean equals(Object obj) {
            StoryImgHolder otherStory = (StoryImgHolder) obj;
            return storyImg.getGroupIndex() == otherStory.storyImg.getGroupIndex();
        }

        @Override
        public int compareTo(@NonNull Object o) {
            StoryImgHolder otherHolder = (StoryImgHolder) o;
            return storyImg.getGroupIndex() - otherHolder.storyImg.getGroupIndex();
        }
    }

    class LifeImgItemHolder {
        ImageView itemImg;
        EditText itemEt;
    }

    private void replaceSelectedItem(ImageItem item) {
        for (ImageItem item1 : mPictureSelected) {
            if (item1.path.equals(currentStoryImgHolder.getUploadUri().getPath())) {
                item1.path = item.path;
                item1.size = item.size;
                item1.height = item.height;
                item1.width = item.width;
                item1.mimeType = item.mimeType;
            }
        }
    }

    private void checkPermissions() {
        if (!AndPermission.hasPermissions(getActivityContext(), Permission.Group.LOCATION, Permission.Group.CAMERA,
                Permission.Group.STORAGE)) {
            AndPermission.with(getActivityContext())
                    .runtime()
                    .permission(Permission.Group.LOCATION)
                    .start();
        }
    }


    private ImageItem findImageItem(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        for (ImageItem item : mPictureSelected) {
            if (path.equals(item.path)) {
                return item;
            }
        }
        return null;
    }
}
