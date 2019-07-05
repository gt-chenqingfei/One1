package com.oneone.modules.user.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.oneone.BaseActivity;
import com.oneone.R;
import com.oneone.api.constants.CharacterSetting;
import com.oneone.api.constants.Gender;
import com.oneone.framework.android.utils.LocaleUtils;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;
import com.oneone.framework.ui.dialog.DatePickerDialog;
import com.oneone.framework.ui.dialog.SheetItem;
import com.oneone.framework.ui.dialog.WheelDialog;
import com.oneone.HereSingletonFactory;
import com.oneone.modules.user.bean.UserProfileUpdateBean;
import com.oneone.modules.user.dialog.HeightDialog;
import com.oneone.modules.user.view.ModifyUserBasicLineItem;
import com.oneone.modules.support.activity.CitySelectActivity;
import com.oneone.modules.support.bean.City;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.UserManager;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.modules.user.util.HereUserUtil;
import com.oneone.utils.GenderUtil;
import com.oneone.utils.ToastUtil;

import butterknife.BindView;

/**
 * Created by here on 18/4/13.
 */
@Route(path = "/profile/edit_basic")
@ToolbarResource(title = R.string.str_modify_user_basic_title)
@LayoutResource(R.layout.activity_modify_single_user_basic)
public class ModifySingleUserBasicActivity extends BaseActivity implements View.OnClickListener, WheelDialog.WheelSelectedListener {

    @BindView(R.id.birthdate_item)
    ModifyUserBasicLineItem birthdateItem;
    @BindView(R.id.person_set_item)
    ModifyUserBasicLineItem personSetItem;
    @BindView(R.id.ususally_live_item)
    ModifyUserBasicLineItem usuallyLiveItem;
    @BindView(R.id.hometown_item)
    ModifyUserBasicLineItem hometownItem;
    @BindView(R.id.body_height_item)
    ModifyUserBasicLineItem bodyHeightItem;
    @BindView(R.id.body_shap_item)
    ModifyUserBasicLineItem bodyShapItem;
    @BindView(R.id.family_situation_item)
    ModifyUserBasicLineItem familySituationItem;
    @BindView(R.id.faith_item)
    ModifyUserBasicLineItem faithItem;
    @BindView(R.id.sign_item)
    ModifyUserBasicLineItem signItem;

    private static final int USUALLY_LIVE_CITY = 999;
    private static final int HOME_TOWN_CITY = 998;
    private static final int SIGN = 997;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getRightTextMenu().setTextColor(getResources().getColor(R.color.blue));
        getRightTextMenu().setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

        setRightTextMenu(R.string.str_menu_save);

        initView();
    }

    public void initView() {
        birthdateItem.setOnClickListener(this);
        personSetItem.setOnClickListener(this);
        usuallyLiveItem.setOnClickListener(this);
        hometownItem.setOnClickListener(this);
        bodyHeightItem.setOnClickListener(this);
        bodyShapItem.setOnClickListener(this);
        familySituationItem.setOnClickListener(this);
        faithItem.setOnClickListener(this);
        signItem.setOnClickListener(this);

        birthdateItem.setItemContent(R.string.str_modify_user_basic_line_item_title_birthdate, HereUser.getInstance().getUserInfo().getBirthdate(), R.string.str_modify_user_basic_line_item_val_birthdate, -1);
        personSetItem.setItemContent(R.string.str_modify_user_basic_line_item_title_person_set, R.array.character_setting_array, HereUser.getInstance().getUserInfo().getCharacterSetting(), -1, HereUserUtil.getCharacterIdByCharacterVal(HereUser.getInstance().getUserInfo().getCharacterSetting()));

        usuallyLiveItem.setItemContent(R.string.str_modify_user_basic_line_item_title_usually_live, HereUser.getInstance().getUserInfo().getProvince() + " " + HereUser.getInstance().getUserInfo().getCity(), R.string.str_modify_user_basic_line_item_val_usually_live, -1);
        hometownItem.setItemContent(R.string.str_modify_user_basic_line_item_title_hometown, HereUser.getInstance().getUserInfo().getHometownProvince() + " " + HereUser.getInstance().getUserInfo().getHometownCity(), R.string.str_modify_user_basic_line_item_val_hometown, -1);


        String heightDisplay = HereUserUtil.getHeightDisplay(HereUser.getInstance().getUserInfo().getHeight());
        bodyHeightItem.setItemContent(R.string.str_modify_user_basic_line_item_title_body_height, heightDisplay, R.string.str_modify_user_basic_line_item_val_body_height, -1);
        bodyShapItem.setItemContent(R.string.str_modify_user_basic_line_item_title_body_shap, R.array.bodily_form_array, HereUser.getInstance().getUserInfo().getBodilyForm(), R.string.str_modify_user_basic_line_item_val_body_shap);
        familySituationItem.setItemContent(R.string.str_modify_user_basic_line_item_title_family_situation, R.array.family_info_array, HereUser.getInstance().getUserInfo().getFamilyInfo(), R.string.str_modify_user_basic_line_item_val_family_situation);
        faithItem.setItemContent(R.string.str_modify_user_basic_line_item_title_faith, R.array.religion_array, HereUser.getInstance().getUserInfo().getReligion(), R.string.str_modify_user_basic_line_item_val_faith);
        signItem.setItemContent(R.string.str_modify_user_basic_line_item_title_sign, HereUser.getInstance().getUserInfo().getMyMonologue(), R.string.str_modify_user_basic_line_item_val_sign, -1);
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ModifySingleUserBasicActivity.class));
    }

    @Override
    public void onRightTextMenuClick(View view) {
        super.onRightTextMenuClick(view);

        UserManager manager = HereSingletonFactory.getInstance().getUserManager();
        UserProfileUpdateBean updateBean = new UserProfileUpdateBean();
        if (birthdateItem.getTag() != null) {
            String dateStr = (String) birthdateItem.getTag();
//            System.out.println("---------------birt : " + dateStr);
            updateBean.setBirthdate(dateStr);
        }
        if (personSetItem.getTag() != null) {
            int personSet = (int) personSetItem.getTag();
            updateBean.setSex(getGenderByCharacter(personSet));
            updateBean.setCharacterSetting(personSet);
        }

        if (bodyHeightItem.getTag() != null) {
            int bodyHeight = (int) bodyHeightItem.getTag();
            updateBean.setHeight(bodyHeight);
        }

        if (bodyShapItem.getTag() != null) {
            int bodyShap = (int) bodyShapItem.getTag();
            updateBean.setBodilyForm(bodyShap);
        }

        if (familySituationItem.getTag() != null) {
            int familySityation = (int) familySituationItem.getTag();
            updateBean.setFamilyInfo(familySityation);
        }

        if (faithItem.getTag() != null) {
            int faithVal = (int) faithItem.getTag();
            updateBean.setReligion(faithVal);
        }

        if (usuallyLiveItem.getTag() != null) {
            City[] cities = (City[]) usuallyLiveItem.getTag();
            updateBean.setCity(cities[0].getName());
            updateBean.setCityCode(cities[0].getCode());
            updateBean.setProvince(cities[1].getName());
            updateBean.setProvinceCode(cities[1].getCode());

        }

        if (hometownItem.getTag() != null) {
            City[] cities = (City[]) hometownItem.getTag();
            updateBean.setHometownCity(cities[0].getName());
            updateBean.setHometownCityCode(cities[0].getCode());
            updateBean.setHometownProvince(cities[1].getName());
            updateBean.setHometownProvinceCode(cities[1].getCode());
        }

        String countryCode = HereSingletonFactory.getInstance()
                .getUserManager().getCountryCode();

        if (TextUtils.isEmpty(countryCode)) {
            countryCode = "+" + LocaleUtils.getCountryCode(this);
        }
        updateBean.setCountryCode(countryCode);
        updateBean.setHometownCountryCode(countryCode);
        if (signItem.getTag() != null) {
            String signStr = (String) signItem.getTag();
            updateBean.setMonologue(signStr);
        }

        manager.updateUserInfo(new UserManager.UserUpdateListener() {
            @Override
            public void onUserUpdate(UserInfo userInfo, boolean isOk, String message) {
                if (isOk) {
                    ToastUtil.show(getActivityContext(), getResources().getString(R.string.str_app_save_request_ok));
                    ModifySingleUserBasicActivity.this.finish();
                } else {
                    ToastUtil.show(getActivityContext(), getResources().getString(R.string.str_app_save_request_fail));
                }
            }
        }, updateBean);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.birthdate_item:
                new DatePickerDialog(getActivityContext(), new DatePickerDialog.OnDatePickListener() {
                    @Override
                    public void onDateSelected(String date) {
                        birthdateItem.setTag(date);
                        birthdateItem.setItemContent(R.string.str_modify_user_basic_line_item_title_birthdate, date, -1, -1);
                    }
                }).show();
                break;
            case R.id.person_set_item:
                if (GenderUtil.getGender(HereUser.getInstance().getUserInfo().getSex()).equals("男")) {
                    new WheelDialog(this, view.getId(), R.array.character_male_setting_array, this)
                            .setPositive(getString(R.string.str_ok)).show();
//                    personSetItem.setItemContent(R.string.str_modify_user_basic_line_item_title_person_set, R.array.character_male_setting_array, HereUser.getInstance().getUserInfo().getCharacterSetting(), -1, HereUserUtil.getCharacterIdByCharacterVal(HereUser.getInstance().getUserInfo().getCharacterSetting()));
                } else {
                    new WheelDialog(this, view.getId(), R.array.character_female_setting_array, this)
                            .setPositive(getString(R.string.str_ok)).show();
//                    personSetItem.setItemContent(R.string.str_modify_user_basic_line_item_title_person_set, R.array.character_female_setting_array, HereUser.getInstance().getUserInfo().getCharacterSetting(), -1, HereUserUtil.getCharacterIdByCharacterVal(HereUser.getInstance().getUserInfo().getCharacterSetting()));
                }

                break;
            case R.id.ususally_live_item:
                CitySelectActivity.startActivity4Result(getActivityContext(), USUALLY_LIVE_CITY);
                break;
            case R.id.hometown_item:
                CitySelectActivity.startActivity4Result(getActivityContext(), HOME_TOWN_CITY);
                break;
            case R.id.body_height_item:
                new HeightDialog(getActivityContext(), new HeightDialog.HeightSelectedListener() {
                    @Override
                    public void onHeightSelected(int id, String value) {

                        bodyHeightItem.setTag(id);

                        String display = HereUserUtil.getHeightDisplay(id);
                        bodyHeightItem.setItemContent(R.string.str_modify_user_basic_line_item_title_body_height, display, R.string.str_modify_user_basic_line_item_val_body_height, -1);
                    }
                }).setPositive(getString(R.string.str_ok)).show();
                break;
            case R.id.body_shap_item:
                if (GenderUtil.getGender(HereUser.getInstance().getUserInfo().getSex()).equals("男")) {
                    new WheelDialog(this, view.getId(), R.array.bodily_form_male_array, this)
                            .setPositive(getString(R.string.str_ok)).show();
                } else {
                    new WheelDialog(this, view.getId(), R.array.bodily_form_female_array, this)
                            .setPositive(getString(R.string.str_ok)).show();
                }
                break;
            case R.id.family_situation_item:
                new WheelDialog(this, view.getId(), R.array.family_info_array, this)
                        .setPositive(getString(R.string.str_ok)).show();
                break;
            case R.id.faith_item:
                new WheelDialog(this, view.getId(), R.array.religion_array, this)
                        .setPositive(getString(R.string.str_ok)).show();
                break;
            case R.id.sign_item:
                ModifySingleUserSignActivity.startActivity4Rlt(ModifySingleUserBasicActivity.this, SIGN);
                break;
        }
    }


    @Override
    public void onWheelSelected(int id, SheetItem item) {
        switch (id) {
            case R.id.birthdate_item:
                birthdateItem.setTag(item.getValue());
                break;
            case R.id.person_set_item:
                personSetItem.setTag(item.getId());
                personSetItem.setItemContent(R.string.str_modify_user_basic_line_item_title_person_set, R.array.character_setting_array, item.getId(), -1, HereUserUtil.getCharacterIdByCharacterVal(item.getId()));
                break;
            case R.id.ususally_live_item:
                break;
            case R.id.hometown_item:
                break;
            case R.id.body_height_item:

                break;
            case R.id.body_shap_item:
                bodyShapItem.setTag(item.getId());
                bodyShapItem.setItemContent(R.string.str_modify_user_basic_line_item_title_body_shap, R.array.bodily_form_array, item.getId(), R.string.str_modify_user_basic_line_item_val_body_shap);
                break;
            case R.id.family_situation_item:
                familySituationItem.setTag(item.getId());
                familySituationItem.setItemContent(R.string.str_modify_user_basic_line_item_title_family_situation, R.array.family_info_array, item.getId(), R.string.str_modify_user_basic_line_item_val_family_situation);
                break;
            case R.id.faith_item:
                faithItem.setTag(item.getId());
                faithItem.setItemContent(R.string.str_modify_user_basic_line_item_title_faith, R.array.religion_array, item.getId(), R.string.str_modify_user_basic_line_item_val_faith);
                break;
            case R.id.sign_item:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        City city = null;
        City cityParent = null;
        if (requestCode == USUALLY_LIVE_CITY) {
            if (data != null) {
                city = data.getParcelableExtra(CitySelectActivity.EXTRA_CITY);
                cityParent = data.getParcelableExtra(CitySelectActivity.EXTRA_CITY_PARENT);
//                System.out.println("====USUALLY_LIVE_CITY");
//                System.out.println(city);
//                System.out.println(cityParent);
//                System.out.println("====USUALLY_LIVE_CITY");
                usuallyLiveItem.setTag(new City[]{city, cityParent});

                usuallyLiveItem.setItemContent(R.string.str_modify_user_basic_line_item_title_usually_live, cityParent.getName() + " " + city.getName(), -1, -1);
            }
        } else if (requestCode == HOME_TOWN_CITY) {
            if (data != null) {
                city = data.getParcelableExtra(CitySelectActivity.EXTRA_CITY);
                cityParent = data.getParcelableExtra(CitySelectActivity.EXTRA_CITY_PARENT);
                hometownItem.setTag(new City[]{city, cityParent});

                hometownItem.setItemContent(R.string.str_modify_user_basic_line_item_title_hometown, cityParent.getName() + " " + city.getName(), R.string.str_modify_user_basic_line_item_val_hometown, -1);
            }
        } else if (requestCode == SIGN) {
            if (data != null) {
                String signStr = data.getStringExtra(ModifySingleUserSignActivity.EXTRA_SIGN);
                signItem.setTag(signStr);
                signItem.setItemContent(R.string.str_modify_user_basic_line_item_title_sign, signStr, R.string.str_modify_user_basic_line_item_val_sign, -1);
            }
        }
    }

    private int getGenderByCharacter(int character) {
        switch (character) {
            case CharacterSetting.DA_SU:
            case CharacterSetting.XIAN_ROU:
            case CharacterSetting.XIAO_GE_GE:
                return Gender.MALE;

            case CharacterSetting.XIAO_JIE_JIE:
            case CharacterSetting.JIE_JIE:
            case CharacterSetting.SAO_NV:
                return Gender.FEMALE;

            default:
                return Gender.UNKNOWN;
        }
    }
}