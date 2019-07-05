package com.oneone.modules.user.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.oneone.BaseActivity;
import com.oneone.HereSingletonFactory;
import com.oneone.R;
import com.oneone.framework.android.utils.LocaleUtils;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;
import com.oneone.framework.ui.dialog.DatePickerDialog;
import com.oneone.framework.ui.dialog.SheetItem;
import com.oneone.framework.ui.dialog.WheelDialog;
import com.oneone.framework.ui.imagepicker.ImagePicker;
import com.oneone.framework.ui.imagepicker.bean.ImageItem;
import com.oneone.framework.ui.imagepicker.ui.ImageGridActivity;
import com.oneone.framework.ui.utils.PermissionsUtil;
import com.oneone.framework.ui.utils.ScreenUtil;
import com.oneone.modules.entry.beans.UploadTokenBean;
import com.oneone.modules.feedback.beans.Feedback;
import com.oneone.modules.support.activity.CitySelectActivity;
import com.oneone.modules.support.activity.OccupationSelectActivity;
import com.oneone.modules.support.bean.City;
import com.oneone.modules.support.bean.Occupation;
import com.oneone.modules.support.model.SupportModel;
import com.oneone.modules.support.qiniu.PhotoUploadListener;
import com.oneone.modules.support.qiniu.UploadParam;
import com.oneone.modules.timeline.activity.PermissionsWarnActivity;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.UserManager;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.modules.user.bean.UserProfileUpdateBean;
import com.oneone.modules.user.view.ModifyUserBasicLineItem;
import com.oneone.restful.ApiResult;
import com.oneone.support.qiniu.UploadImgUtil;
import com.oneone.support.qiniu.UploadObj;
import com.oneone.utils.ImageHelper;
import com.oneone.utils.ToastUtil;
import com.oneone.widget.AvatarImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by here on 18/5/7.
 */
@Route(path = "/profile/edit_basic")
@ToolbarResource(title = R.string.str_modify_matcher_user_basic_title)
@LayoutResource(R.layout.activity_modify_matcher_user_basic)
public class ModifyMatcherUserBasicActivity extends BaseActivity implements View.OnClickListener, WheelDialog.WheelSelectedListener {
    @BindView(R.id.user_photo_iv)
    AvatarImageView userPhotoIv;
    @BindView(R.id.user_name_tv)
    TextView userNameTv;
    @BindView(R.id.birthdate_item)
    ModifyUserBasicLineItem birthdateItem;
    @BindView(R.id.ususally_live_item)
    ModifyUserBasicLineItem usuallyLiveItem;
    @BindView(R.id.sex_item)
    ModifyUserBasicLineItem sexItem;
    @BindView(R.id.occupation_item)
    ModifyUserBasicLineItem occupationItem;
    @BindView(R.id.company_item)
    ModifyUserBasicLineItem companyItem;


    private static final int USUALLY_LIVE_CITY = 999;
    public static final int NICK_NAME = 996;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mImmersionBar
                .keyboardEnable(true).init();

        getRightTextMenu().setTextColor(getResources().getColor(R.color.blue));
        getRightTextMenu().setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

        setRightTextMenu(R.string.str_menu_save);

        initView();
    }

    public void initView() {
        userPhotoIv.init(HereUser.getInstance().getUserInfo(), false);
        userNameTv.setText(HereUser.getInstance().getUserInfo().getNickname());

        userPhotoIv.setOnClickListener(this);
        userNameTv.setOnClickListener(this);

        birthdateItem.setOnClickListener(this);
        usuallyLiveItem.setOnClickListener(this);
        sexItem.setOnClickListener(this);
        occupationItem.setOnClickListener(this);
        companyItem.setOnClickListener(this);

        birthdateItem.setItemContent(R.string.str_modify_user_basic_line_item_title_birthdate, HereUser.getInstance().getUserInfo().getBirthdate(), R.string.str_modify_user_basic_line_item_val_birthdate, -1);

        usuallyLiveItem.setItemContent(R.string.str_modify_user_basic_line_item_title_usually_live, HereUser.getInstance().getUserInfo().getProvince() + " " + HereUser.getInstance().getUserInfo().getCity(), R.string.str_modify_user_basic_line_item_val_usually_live, -1);

        sexItem.setItemContent(R.string.str_modify_user_basic_line_item_title_sex, R.array.sex_array, HereUser.getInstance().getUserInfo().getSex(), R.string.str_modify_user_basic_line_item_val_sex);

        occupationItem.setItemContent(R.string.str_modify_user_occupation_and_school_basic_line_item_title_occupation, HereUser.getInstance().getUserInfo().getIndustry() + "/" + HereUser.getInstance().getUserInfo().getOccupation(), R.string.str_modify_user_occupation_and_school_basic_line_item_val_occupation, -1);
        companyItem.setItemContent(R.string.str_modify_user_occupation_and_school_basic_line_item_title_company, HereUser.getInstance().getUserInfo().getCompany(), R.string.str_modify_user_occupation_and_school_basic_line_item_val_company);
        companyItem.setLimit(10);
        companyItem.itemRightArrow.setVisibility(View.GONE);
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ModifyMatcherUserBasicActivity.class));
    }

    @Override
    public void onRightTextMenuClick(View view) {
        super.onRightTextMenuClick(view);

        UserProfileUpdateBean updateBean = new UserProfileUpdateBean();

        if (birthdateItem.getTag() != null) {
            String dateStr = (String) birthdateItem.getTag();
            System.out.println("save birthday : " + dateStr);
            updateBean.setBirthdate(dateStr);
        }

        UserManager manager = HereSingletonFactory.getInstance().getUserManager();
        if (usuallyLiveItem.getTag() != null) {
            City[] cities = (City[]) usuallyLiveItem.getTag();
            updateBean.setCity(cities[0].getName());
            updateBean.setCityCode(cities[0].getCode());
            updateBean.setProvince(cities[1].getName());
            updateBean.setProvinceCode(cities[1].getCode());
        }

        String countryCode = HereSingletonFactory.getInstance()
                .getUserManager().getCountryCode();

        if (TextUtils.isEmpty(countryCode)) {
            countryCode = "+" + LocaleUtils.getCountryCode(this);
        }
        updateBean.setCountryCode(countryCode);
        updateBean.setHometownCountryCode(countryCode);
        if (sexItem.getTag() != null) {
            Integer sex = (Integer) sexItem.getTag();
            updateBean.setSex(sex);
        }

        if (occupationItem.getTag() != null) {
            Occupation[] occupationArr = (Occupation[]) occupationItem.getTag();
            updateBean.setIndustryCode(occupationArr[0].getCode());
            updateBean.setIndustry(occupationArr[0].getName());
            updateBean.setOccupationCode(occupationArr[1].getCode());
            updateBean.setOccupation(occupationArr[1].getName());
        }

        String companyStr = companyItem.getEtStr();
        if (companyStr != null && !companyStr.equals("")) {
            updateBean.setCompany(companyStr);
        }

        manager.updateUserInfo(new UserManager.UserUpdateListener() {
            @Override
            public void onUserUpdate(UserInfo userInfo, boolean isOk, String message) {
                if (isOk) {
                    ToastUtil.show(getActivityContext(), getResources().getString(R.string.str_app_save_request_ok));
                    ModifyMatcherUserBasicActivity.this.finish();
                } else {
                    ToastUtil.show(getActivityContext(), getResources().getString(R.string.str_app_save_request_fail));
                }
            }
        }, updateBean);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_photo_iv:
                if (!PermissionsUtil.checkPermissions(mContext, PermissionsUtil.READ_EXTERNAL_STORAGE) &&
                        !PermissionsUtil.checkPermissions(mContext, PermissionsUtil.WRITE_EXTERNAL_STORAGE)) {
                    PermissionsWarnActivity.startActivity(mContext, PermissionsWarnActivity.EXTERNAL_STORAGE);
                } else if (!PermissionsUtil.checkPermissions(mContext, PermissionsUtil.CAMERA)) {
                    PermissionsWarnActivity.startActivity(mContext, PermissionsWarnActivity.CAMERA);
                } else {
                    startSelectImage(1, 99);
                }
                break;
            case R.id.user_name_tv:
                ModifySingleUserNameActivity.startActivity4Rlt(ModifyMatcherUserBasicActivity.this, NICK_NAME);
                break;
            case R.id.occupation_item:
                OccupationSelectActivity.startActivity4Result(ModifyMatcherUserBasicActivity.this);
                break;
            case R.id.birthdate_item:
                new DatePickerDialog(getActivityContext(), new DatePickerDialog.OnDatePickListener() {
                    @Override
                    public void onDateSelected(String date) {
                        birthdateItem.setItemContent(R.string.str_modify_user_basic_line_item_title_birthdate, date, -1, -1);
                        birthdateItem.setTag(date);
                    }
                }).show();
                break;
            case R.id.ususally_live_item:
                CitySelectActivity.startActivity4Result(getActivityContext(), USUALLY_LIVE_CITY);
                break;
            case R.id.sex_item:
                new WheelDialog(this, view.getId(), R.array.sex_array, this)
                        .setPositive(getString(R.string.str_ok)).show();
                break;
        }
    }

    @Override
    public void onWheelSelected(int id, SheetItem item) {
        switch (id) {
            case R.id.birthdate_item:

                break;
            case R.id.ususally_live_item:
                break;
            case R.id.sex_item:
                int itemVal = Integer.valueOf(item.getId());
                sexItem.setItemContent(R.string.str_modify_user_basic_line_item_title_sex, R.array.sex_array, itemVal, R.string.str_modify_user_basic_line_item_val_sex);
                sexItem.setTag(itemVal);
                break;
        }
    }

    private ImageItem currentImgItem;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        City city = null;
        City cityParent = null;
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
            } else if (requestCode == USUALLY_LIVE_CITY) {
                city = data.getParcelableExtra(CitySelectActivity.EXTRA_CITY);
                cityParent = data.getParcelableExtra(CitySelectActivity.EXTRA_CITY_PARENT);
                System.out.println("====USUALLY_LIVE_CITY");
                System.out.println(city);
                System.out.println(cityParent);
                System.out.println("====USUALLY_LIVE_CITY");
                usuallyLiveItem.setTag(new City[]{city, cityParent});

                usuallyLiveItem.setItemContent(R.string.str_modify_user_basic_line_item_title_usually_live, cityParent.getName() + " " + city.getName(), -1, -1);
            } else if (requestCode == OccupationSelectActivity.REQ_CODE_OCCUPATION_SELECT) {
                Occupation occupation = data.getParcelableExtra(OccupationSelectActivity.EXTRA_OCCUPATION);
                Occupation occupationParent = data.getParcelableExtra(OccupationSelectActivity.EXTRA_OCCUPATION_PARENT);
                System.out.println("-------------------->");
                System.out.println(occupation);
                System.out.println(occupationParent);

                occupationItem.setItemContent(R.string.str_modify_user_occupation_and_school_basic_line_item_title_occupation, occupationParent.getName() + "/" + occupation.getName(), R.string.str_modify_user_occupation_and_school_basic_line_item_val_occupation, -1);
                occupationItem.setTag(new Occupation[]{occupationParent, occupation});
            } else if (requestCode == NICK_NAME) {
                String signStr = data.getStringExtra(ModifySingleUserNameActivity.EXTRA_NICKNAME);
                userNameTv.setText(signStr);
            }
        }
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
}
