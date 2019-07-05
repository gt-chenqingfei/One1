package com.oneone.modules.user.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.oneone.BaseActivity;
import com.oneone.HereSingletonFactory;
import com.oneone.R;
import com.oneone.framework.android.preference.DefaultSP;
import com.oneone.framework.android.utils.SoftKeyBoardUtil;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;
import com.oneone.framework.ui.imagepicker.ImagePicker;
import com.oneone.framework.ui.imagepicker.bean.ImageItem;
import com.oneone.framework.ui.imagepicker.ui.ImageGridActivity;
import com.oneone.framework.ui.utils.PermissionsUtil;
import com.oneone.framework.ui.utils.ScreenUtil;
import com.oneone.modules.entry.beans.UploadTokenBean;
import com.oneone.modules.support.model.SupportModel;
import com.oneone.modules.support.qiniu.PhotoUploadListener;
import com.oneone.modules.support.qiniu.UploadParam;
import com.oneone.modules.timeline.activity.PermissionsWarnActivity;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.UserManager;
import com.oneone.modules.user.UserSP;
import com.oneone.modules.user.bean.UserProfileUpdateBean;
import com.oneone.modules.user.util.HereUserUtil;
import com.oneone.restful.ApiResult;
import com.oneone.support.qiniu.UploadImgUtil;
import com.oneone.support.qiniu.UploadObj;
import com.oneone.utils.ImageHelper;
import com.oneone.utils.MyTextUtil;
import com.oneone.widget.AvatarImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.jiguang.imui.chatinput.emoji.EmoticonsKeyboardUtils;

/**
 * Created by here on 18/4/13.
 */

@Route(path = "/profile/edit")
@ToolbarResource(title = R.string.str_modify_single_user_main_title)
@LayoutResource(R.layout.activity_modify_single_user_main)
public class ModifySingleUserMainActivity extends BaseActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ModifySingleUserMainActivity.class));
    }

    @BindView(R.id.user_photo_iv)
    AvatarImageView userPhotoIv;
    @BindView(R.id.user_name_tv)
    TextView userNameTv;

    @BindView(R.id.user_basic_block_layout)
    View userBasicBlockView;
    @BindView(R.id.user_basic_title_tv)
    TextView userBasicTitleTv;
    @BindView(R.id.user_basic_icon_iv)
    ImageView userBasicIconIv;
    @BindView(R.id.user_basic_fan_view)
    View userBasicFanView;
    @BindView(R.id.user_basic_modify_icon)
    ImageView userBasicModifyIcon;

    @BindView(R.id.user_life_hibit_block_layout)
    View userHabitBlockView;
    @BindView(R.id.user_life_hibit_title_tv)
    TextView userHabitTitleTv;
    @BindView(R.id.user_life_hibit_icon_iv)
    ImageView userHabitIconIv;
    @BindView(R.id.user_life_habit_fan_view)
    View userLifeHabitFanView;
    @BindView(R.id.user_life_habit_modify_icon)
    ImageView userLifeHabitModifyIcon;

    @BindView(R.id.user_occupation_and_school_block_layout)
    View userOccupationAndSchoolBlockView;
    @BindView(R.id.user_occupation_and_school_title_tv)
    TextView userOccupationAndSchoolTitleTv;
    @BindView(R.id.user_occupation_and_school_icon_iv)
    ImageView userOccupationAndSchoolIconIv;
    @BindView(R.id.user_occupation_fan_view)
    View userOccupationFanView;
    @BindView(R.id.user_occupation_modify_icon)
    ImageView userOccupationModifyIcon;

    @BindView(R.id.user_pet_block_layout)
    View userPetBlockView;
    @BindView(R.id.user_pet_title_tv)
    TextView userPetTitleTv;
    @BindView(R.id.user_pet_icon_iv)
    ImageView userPetIconIv;
    @BindView(R.id.user_pet_fan_view)
    View userPetFanView;
    @BindView(R.id.user_pet_modify_icon)
    ImageView userPetModifyIcon;

    @BindView(R.id.user_story_block_layout)
    View userStoryBlockView;
    @BindView(R.id.user_story_title_tv)
    TextView userStoryTitleTv;
    @BindView(R.id.user_story_icon_iv)
    ImageView userStoryIconIv;

    private int blockWidth;
    private int blockHeight;
    private int blockGapWidth;
    private int blockTextSize;

    public static final int NICK_NAME = 996;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DefaultSP.getInstance().registerListener(this, this);
        initView();
        initClick();
        HereSingletonFactory.getInstance().getUserManager().fetchUserInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DefaultSP.getInstance().unregisterListener(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (HereUserUtil.checkBaseComplete(HereUser.getInstance().getUserInfo())) {
            userBasicFanView.setVisibility(View.GONE);
            userBasicModifyIcon.setVisibility(View.GONE);
        } else {
            userBasicFanView.setVisibility(View.VISIBLE);
            userBasicModifyIcon.setVisibility(View.VISIBLE);
        }
        if (HereUserUtil.checkLifeHabitComplete(HereUser.getInstance().getUserInfo())) {
            userLifeHabitFanView.setVisibility(View.GONE);
            userLifeHabitModifyIcon.setVisibility(View.GONE);
        } else {
            userLifeHabitFanView.setVisibility(View.VISIBLE);
            userLifeHabitModifyIcon.setVisibility(View.VISIBLE);
        }
        if (HereUserUtil.checkOccupationAndSchoolComplete(HereUser.getInstance().getUserInfo())) {
            userOccupationFanView.setVisibility(View.GONE);
            userOccupationModifyIcon.setVisibility(View.GONE);
        } else {
            userOccupationFanView.setVisibility(View.VISIBLE);
            userOccupationModifyIcon.setVisibility(View.VISIBLE);
        }
        if (HereUserUtil.checkPetComplete(HereUser.getInstance().getUserInfo())) {
            userPetFanView.setVisibility(View.GONE);
            userPetModifyIcon.setVisibility(View.GONE);
        } else {
            userPetFanView.setVisibility(View.VISIBLE);
            userPetModifyIcon.setVisibility(View.VISIBLE);
        }
    }

    public void initView() {
        userPhotoIv.init(HereUser.getInstance().getUserInfo(), false);
        userNameTv.setText(MyTextUtil.getLimitEllipseText(HereUser.getInstance().getUserInfo().getMyNickname(), 10));

        initBlockViews();
    }

    public void initClick() {
        userPhotoIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!PermissionsUtil.checkPermissions(mContext, PermissionsUtil.READ_EXTERNAL_STORAGE) &&
                        !PermissionsUtil.checkPermissions(mContext, PermissionsUtil.WRITE_EXTERNAL_STORAGE)) {
                    PermissionsWarnActivity.startActivity(mContext, PermissionsWarnActivity.EXTERNAL_STORAGE);
                } else if (!PermissionsUtil.checkPermissions(mContext, PermissionsUtil.CAMERA)) {
                    PermissionsWarnActivity.startActivity(mContext, PermissionsWarnActivity.CAMERA);
                } else {
                    startSelectImage(1, 99);
                }
            }
        });

        userNameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModifySingleUserNameActivity.startActivity4Rlt(ModifySingleUserMainActivity.this, NICK_NAME);
            }
        });

        userBasicBlockView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModifySingleUserBasicActivity.startActivity(ModifySingleUserMainActivity.this);
            }
        });

        userHabitBlockView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModifySingleLiveHabitActivity.startActivity(ModifySingleUserMainActivity.this);
            }
        });

        userOccupationAndSchoolBlockView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModifySingleOccupationAndSchoolActivity.startActivity(ModifySingleUserMainActivity.this);
            }
        });

        userPetBlockView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModifySinglePetActivity.startActivity(ModifySingleUserMainActivity.this);
            }
        });

        userStoryBlockView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyStoryTagsActivity.startActivity(ModifySingleUserMainActivity.this);
            }
        });
    }

    public void initBlockViews() {
        blockTextSize = (int) (16 * ScreenUtil.WIDTH_RATIO);
        blockWidth = (int) (153 * ScreenUtil.WIDTH_RATIO);
        blockHeight = (int) (138 * ScreenUtil.WIDTH_RATIO);
        blockGapWidth = (int) (23 * ScreenUtil.WIDTH_RATIO);

        AbsoluteLayout.LayoutParams abParams;
        abParams = (AbsoluteLayout.LayoutParams) userBasicBlockView.getLayoutParams();
        abParams.width = blockWidth;
        abParams.height = blockHeight;
        abParams.x = (int) (ScreenUtil.WIDTH_RATIO * 23);

        abParams = (AbsoluteLayout.LayoutParams) userBasicTitleTv.getLayoutParams();
        userBasicTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 16 * ScreenUtil.WIDTH_RATIO);
        abParams.width = AbsoluteLayout.LayoutParams.WRAP_CONTENT;
        abParams.height = AbsoluteLayout.LayoutParams.WRAP_CONTENT;
        abParams.x = (int) (ScreenUtil.WIDTH_RATIO * 43);
        abParams.y = (int) (ScreenUtil.WIDTH_RATIO * 23);

        abParams = (AbsoluteLayout.LayoutParams) userBasicIconIv.getLayoutParams();
        abParams.width = (int) (ScreenUtil.WIDTH_RATIO * 141);
        abParams.height = (int) (ScreenUtil.WIDTH_RATIO * 92);
        abParams.x = (int) (ScreenUtil.WIDTH_RATIO * 43);
        abParams.y = (int) (ScreenUtil.WIDTH_RATIO * 54);

        abParams = (AbsoluteLayout.LayoutParams) userBasicFanView.getLayoutParams();
        abParams.width = (int) (ScreenUtil.WIDTH_RATIO * 39);
        abParams.height = (int) (ScreenUtil.WIDTH_RATIO * 39);
        abParams.x = (int) (ScreenUtil.WIDTH_RATIO * 137);
        abParams.y = (int) (ScreenUtil.WIDTH_RATIO * 0);

        abParams = (AbsoluteLayout.LayoutParams) userBasicModifyIcon.getLayoutParams();
        abParams.width = (int) (ScreenUtil.WIDTH_RATIO * 13);
        abParams.height = (int) (ScreenUtil.WIDTH_RATIO * 14);
        abParams.x = (int) (ScreenUtil.WIDTH_RATIO * 153);
        abParams.y = (int) (ScreenUtil.WIDTH_RATIO * 8);


        abParams = (AbsoluteLayout.LayoutParams) userHabitBlockView.getLayoutParams();
        abParams.width = blockWidth;
        abParams.height = blockHeight;
        abParams.x = (int) (ScreenUtil.WIDTH_RATIO * 199);

        abParams = (AbsoluteLayout.LayoutParams) userHabitTitleTv.getLayoutParams();
        userHabitTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 16 * ScreenUtil.WIDTH_RATIO);
        abParams.width = AbsoluteLayout.LayoutParams.WRAP_CONTENT;
        abParams.height = AbsoluteLayout.LayoutParams.WRAP_CONTENT;
        abParams.x = (int) (ScreenUtil.WIDTH_RATIO * 219);
        abParams.y = (int) (ScreenUtil.WIDTH_RATIO * 23);

        abParams = (AbsoluteLayout.LayoutParams) userHabitIconIv.getLayoutParams();
        abParams.width = (int) (ScreenUtil.WIDTH_RATIO * 92);
        abParams.height = (int) (ScreenUtil.WIDTH_RATIO * 92);
        abParams.x = (int) (ScreenUtil.WIDTH_RATIO * 251);
        abParams.y = (int) (ScreenUtil.WIDTH_RATIO * 53);

        abParams = (AbsoluteLayout.LayoutParams) userLifeHabitFanView.getLayoutParams();
        abParams.width = (int) (ScreenUtil.WIDTH_RATIO * 39);
        abParams.height = (int) (ScreenUtil.WIDTH_RATIO * 39);
        abParams.x = (int) (ScreenUtil.WIDTH_RATIO * 313);
        abParams.y = (int) (ScreenUtil.WIDTH_RATIO * 0);

        abParams = (AbsoluteLayout.LayoutParams) userLifeHabitModifyIcon.getLayoutParams();
        abParams.width = (int) (ScreenUtil.WIDTH_RATIO * 13);
        abParams.height = (int) (ScreenUtil.WIDTH_RATIO * 14);
        abParams.x = (int) (ScreenUtil.WIDTH_RATIO * 329);
        abParams.y = (int) (ScreenUtil.WIDTH_RATIO * 8);


        abParams = (AbsoluteLayout.LayoutParams) userOccupationAndSchoolBlockView.getLayoutParams();
        abParams.width = blockWidth;
        abParams.height = blockHeight;
        abParams.x = (int) (ScreenUtil.WIDTH_RATIO * 23);
        abParams.y = (int) (ScreenUtil.WIDTH_RATIO * 158);

        abParams = (AbsoluteLayout.LayoutParams) userOccupationAndSchoolTitleTv.getLayoutParams();
        userOccupationAndSchoolTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 16 * ScreenUtil.WIDTH_RATIO);
        abParams.width = AbsoluteLayout.LayoutParams.WRAP_CONTENT;
        abParams.height = AbsoluteLayout.LayoutParams.WRAP_CONTENT;
        abParams.x = (int) (ScreenUtil.WIDTH_RATIO * 42);
        abParams.y = (int) (ScreenUtil.WIDTH_RATIO * 175);

        abParams = (AbsoluteLayout.LayoutParams) userOccupationAndSchoolIconIv.getLayoutParams();
        abParams.width = (int) (ScreenUtil.WIDTH_RATIO * 147);
        abParams.height = (int) (ScreenUtil.WIDTH_RATIO * 67);
        abParams.x = (int) (ScreenUtil.WIDTH_RATIO * 12);
        abParams.y = (int) (ScreenUtil.WIDTH_RATIO * 210);

        abParams = (AbsoluteLayout.LayoutParams) userOccupationFanView.getLayoutParams();
        abParams.width = (int) (ScreenUtil.WIDTH_RATIO * 39);
        abParams.height = (int) (ScreenUtil.WIDTH_RATIO * 39);
        abParams.x = (int) (ScreenUtil.WIDTH_RATIO * 137);
        abParams.y = (int) (ScreenUtil.WIDTH_RATIO * 158);

        abParams = (AbsoluteLayout.LayoutParams) userOccupationModifyIcon.getLayoutParams();
        abParams.width = (int) (ScreenUtil.WIDTH_RATIO * 13);
        abParams.height = (int) (ScreenUtil.WIDTH_RATIO * 14);
        abParams.x = (int) (ScreenUtil.WIDTH_RATIO * 153);
        abParams.y = (int) (ScreenUtil.WIDTH_RATIO * 166);


        abParams = (AbsoluteLayout.LayoutParams) userPetBlockView.getLayoutParams();
        abParams.width = blockWidth;
        abParams.height = blockHeight;
        abParams.x = (int) (ScreenUtil.WIDTH_RATIO * 199);
        abParams.y = (int) (ScreenUtil.WIDTH_RATIO * 158);

        abParams = (AbsoluteLayout.LayoutParams) userPetTitleTv.getLayoutParams();
        userPetTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 16 * ScreenUtil.WIDTH_RATIO);
        abParams.width = AbsoluteLayout.LayoutParams.WRAP_CONTENT;
        abParams.height = AbsoluteLayout.LayoutParams.WRAP_CONTENT;
        abParams.x = (int) (ScreenUtil.WIDTH_RATIO * 219);
        abParams.y = (int) (ScreenUtil.WIDTH_RATIO * 175);

        abParams = (AbsoluteLayout.LayoutParams) userPetIconIv.getLayoutParams();
        abParams.width = (int) (ScreenUtil.WIDTH_RATIO * 80);
        abParams.height = (int) (ScreenUtil.WIDTH_RATIO * 103);
        abParams.x = (int) (ScreenUtil.WIDTH_RATIO * 251);
        abParams.y = (int) (ScreenUtil.WIDTH_RATIO * 201);

        abParams = (AbsoluteLayout.LayoutParams) userPetFanView.getLayoutParams();
        abParams.width = (int) (ScreenUtil.WIDTH_RATIO * 39);
        abParams.height = (int) (ScreenUtil.WIDTH_RATIO * 39);
        abParams.x = (int) (ScreenUtil.WIDTH_RATIO * 313);
        abParams.y = (int) (ScreenUtil.WIDTH_RATIO * 158);

        abParams = (AbsoluteLayout.LayoutParams) userPetModifyIcon.getLayoutParams();
        abParams.width = (int) (ScreenUtil.WIDTH_RATIO * 13);
        abParams.height = (int) (ScreenUtil.WIDTH_RATIO * 14);
        abParams.x = (int) (ScreenUtil.WIDTH_RATIO * 329);
        abParams.y = (int) (ScreenUtil.WIDTH_RATIO * 167);


        abParams = (AbsoluteLayout.LayoutParams) userStoryBlockView.getLayoutParams();
        abParams.width = blockWidth;
        abParams.height = blockHeight;
        abParams.x = (int) (ScreenUtil.WIDTH_RATIO * 23);
        abParams.y = (int) (ScreenUtil.WIDTH_RATIO * 314);

        abParams = (AbsoluteLayout.LayoutParams) userStoryTitleTv.getLayoutParams();
        userStoryTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 16 * ScreenUtil.WIDTH_RATIO);
        abParams.width = AbsoluteLayout.LayoutParams.WRAP_CONTENT;
        abParams.height = AbsoluteLayout.LayoutParams.WRAP_CONTENT;
        abParams.x = (int) (ScreenUtil.WIDTH_RATIO * 42);
        abParams.y = (int) (ScreenUtil.WIDTH_RATIO * 331);

        abParams = (AbsoluteLayout.LayoutParams) userStoryIconIv.getLayoutParams();
        abParams.width = (int) (ScreenUtil.WIDTH_RATIO * 145);
        abParams.height = (int) (ScreenUtil.WIDTH_RATIO * 93);
        abParams.x = (int) (ScreenUtil.WIDTH_RATIO * 39);
        abParams.y = (int) (ScreenUtil.WIDTH_RATIO * 364);
    }


    private void startSelectImage(int limit, int reqCode) {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setShowCamera(true);
        imagePicker.setSaveRectangle(true);
        imagePicker.setCrop(true);
        imagePicker.setSelectLimit(limit);
        imagePicker.setMultiMode(limit > 1);
        imagePicker.setFocusWidth(ScreenUtil.screenWidth);
        imagePicker.setFocusHeight(ScreenUtil.screenHeight);
        imagePicker.setOutPutX(1000);
        imagePicker.setOutPutY(1000);
        Intent intent1 = new Intent(getActivityContext(), ImageGridActivity.class);

        startActivityForResult(intent1, reqCode);
    }

    private ImageItem currentImgItem;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (requestCode == 99) {
                List<ImageItem> items = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                ImageItem item = items.get(0);
                if (item != null) {
                    currentImgItem = item;
                    ImageHelper.displayAvatar(getActivityContext(), userPhotoIv, item.path);

                    UploadParam uploadParam = new UploadParam(0, currentImgItem.path, new PhotoUploadListener() {
                        @Override
                        public void onUploadCompleted(UploadParam param, List<UploadParam> group, boolean isAllEnd) {
                            dismissLoading();
                            UserProfileUpdateBean updateBean = new UserProfileUpdateBean();
                            UserManager manager = HereSingletonFactory.getInstance().getUserManager();
                            updateBean.setAvatar(param.getRemotePath());
                            manager.updateUserInfo(null, updateBean);
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
                    });
                    HereSingletonFactory.getInstance().getPhotoUploadManager().enqueue(uploadParam);
                }
            } else if (requestCode == NICK_NAME) {
                String signStr = data.getStringExtra(ModifySingleUserNameActivity.EXTRA_NICKNAME);
                userNameTv.setText(signStr);
            }
            super.onActivityResult(requestCode, resultCode, data);
        }

    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(HereUser.class.getSimpleName())) {
            initView();
        }
    }
}
