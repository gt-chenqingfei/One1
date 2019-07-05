package com.oneone.modules.user.util;

import android.content.Context;
import android.text.TextUtils;

import com.oneone.R;
import com.oneone.api.constants.CharacterSetting;
import com.oneone.api.constants.Gender;
import com.oneone.api.constants.Religion;
import com.oneone.framework.ui.utils.Res;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.modules.user.bean.UserInfoBase;
import com.oneone.utils.ArraysUtil;

/**
 * Created by here on 18/5/3.
 */

public class HereUserUtil {
    public static int getCharacterIdByCharacterVal(int characterVal) {
        switch (characterVal) {
            case CharacterSetting.XIAN_ROU:
                return R.drawable.emoji_male_3;
            case CharacterSetting.XIAO_GE_GE:
                return R.drawable.emoji_male_2;
            case CharacterSetting.DA_SU:
                return R.drawable.emoji_male_1;
            case CharacterSetting.SAO_NV:
                return R.drawable.emoji_female_3;
            case CharacterSetting.XIAO_JIE_JIE:
                return R.drawable.emoji_female_2;
            case CharacterSetting.JIE_JIE:
                return R.drawable.emoji_female_1;
        }

        return -1;
    }

    public static int getProfileWeight(UserInfo info) {
        int total = 0;
        int characterSetting = info.getCharacterSetting() == 0 ? 0 : 2;
        int gender = info.getSex() == 0 ? 0 : 1;
        int nickname = TextUtils.isEmpty(info.getMyNickname()) ? 0 : 10;
        int city = TextUtils.isEmpty(info.getCity()) ? 0 : 4;
        int birthday = TextUtils.isEmpty(info.getBirthdate()) ? 0 : 3;
        int avatar = TextUtils.isEmpty(info.getMyAvatar()) ? 0 : 20;
        int hometown = TextUtils.isEmpty(info.getHometownCity()) ? 0 : 2;
        int petSpecies = TextUtils.isEmpty(info.getPetSpecies()) ? 0 : 2;
        int petName = TextUtils.isEmpty(info.getPetName()) ? 0 : 2;
        int monologue = TextUtils.isEmpty(info.getMonologue()) ? 0 : 3;
        int height = info.getHeight() == 0 ? 0 : 3;
        int bodilyForm = info.getBodilyForm() == 0 ? 0 : 2;
        int annualIncome = info.getAnnualIncomeDisplay() == 0 ? 0 : 2;
        int familyInfo = info.getFamilyInfo() == 0 ? 0 : 2;
        int senseOfWorth = 0;
        if (info.getSenseOfWorthTags() != null) {
            senseOfWorth = info.getSenseOfWorthTags().getTagCount() == 0 ? 0 : 2;
        }

        int eatingHabits = info.getEatingHabits() == 0 ? 0 : 2;
        int lifeHabits = info.getLifeHabits() == 0 ? 0 : 2;
        int smokingHabits = info.getSmokingHabits() == 0 ? 0 : 2;
        int drinkingHabits = info.getDrinkingHabits() == 0 ? 0 : 3;


        int industry = TextUtils.isEmpty(info.getIndustry()) ? 0 : 2;
        int company = TextUtils.isEmpty(info.getCompany()) ? 0 : 2;
        int college = TextUtils.isEmpty(info.getCollegeName()) ? 0 : 3;
        int degree = info.getDegree() == 0 ? 0 : 4;
        int story = 20;

        total = characterSetting + gender + nickname + city + birthday + avatar + hometown
                + petSpecies + petName + monologue + height + bodilyForm + annualIncome
                + familyInfo + senseOfWorth + eatingHabits + lifeHabits
                + smokingHabits + drinkingHabits + industry + company + college + degree + story;

        return total;
    }

    public static String getGenderStr(int gender) {
        if (gender == Gender.MALE) {
            return Res.getString(R.string.str_gender_male);
        } else if (gender == Gender.FEMALE) {
            return Res.getString(R.string.str_gender_female);
        } else {
            return Res.getString(R.string.str_gender_unknown);
        }
    }

    public static boolean checkBaseComplete(UserInfo info) {
        if (TextUtils.isEmpty(info.getBirthdate()))
            return false;
        if (info.getCharacterSetting() == 0)
            return false;
        if (TextUtils.isEmpty(info.getCity()))
            return false;
        if (TextUtils.isEmpty(info.getHometownCity()))
            return false;
        if (info.getHeight() == 0)
            return false;
        if (info.getBodilyForm() == 0)
            return false;
        if (info.getFamilyInfo() == 0)
            return false;
        if (info.getReligion() == 0)
            return false;
        if (TextUtils.isEmpty(info.getMonologue()))
            return false;
        return true;
    }

    public static boolean checkLifeHabitComplete(UserInfo info) {
        if (info.getEatingHabits() == 0)
            return false;
        if (info.getLifeHabits() == 0)
            return false;
        if (info.getSmokingHabits() == 0)
            return false;
        if (info.getDrinkingHabits() == 0)
            return false;
        return true;
    }

    public static boolean checkOccupationAndSchoolComplete(UserInfo info) {
        if (TextUtils.isEmpty(info.getIndustry()))
            return false;
        if (TextUtils.isEmpty(info.getCompany()))
            return false;
        if (TextUtils.isEmpty(info.getCollegeName()))
            return false;
        if (info.getDegree() == 0)
            return false;
        if (info.getAnnualIncomeDisplay() == 0)
            return false;

        return true;
    }

    public static boolean checkPetComplete(UserInfo info) {
        if (TextUtils.isEmpty(info.getPetSpecies()))
            return false;
        if (TextUtils.isEmpty(info.getPetName()))
            return false;
        return true;
    }

    public static String getReligionStr(int religion) {
        String religionStr = "";
        switch (religion) {
            case Religion.NO_RELIGION:
                religionStr = Res.getString(R.string.str_religion_no);
                break;
            case Religion.BUDDHISM:
                religionStr = Res.getString(R.string.str_religion_buddhism);
                break;
            case Religion.CATHOLICISM:
                religionStr = Res.getString(R.string.str_religion_catholicism);
                break;
            case Religion.CHRISTIAN:
                religionStr = Res.getString(R.string.str_religion_christian);
                break;
            case Religion.MOHAMMEDANISM:
                religionStr = Res.getString(R.string.str_religion_mohammedanism);
                break;
            case Religion.OTHER:
                religionStr = Res.getString(R.string.str_religion_other);
                break;
            case Religion.TAOISM:
                religionStr = Res.getString(R.string.str_religion_taoism);
                break;
            default:
                religionStr = Res.getString(R.string.str_religion_no);
                break;
        }

        return religionStr;
    }

    public static int getReligionRes(int religion) {
        int religionDrawableRes;
        switch (religion) {
            case Religion.NO_RELIGION:
                religionDrawableRes = R.drawable.ic_sense;
                break;
            case Religion.BUDDHISM:
                religionDrawableRes = R.drawable.ic_religion_buddhism;
                break;
            case Religion.CATHOLICISM:
                religionDrawableRes = R.drawable.ic_religion_catholicism;
                break;
            case Religion.CHRISTIAN:
                religionDrawableRes = R.drawable.ic_religion_christian;
                break;
            case Religion.MOHAMMEDANISM:
                religionDrawableRes = R.drawable.ic_religion_mohammedanism;
                break;
            case Religion.OTHER:
                religionDrawableRes = R.drawable.ic_religion_other;
                break;
            case Religion.TAOISM:
                religionDrawableRes = R.drawable.ic_religion_taoism;
                break;
            default:
                religionDrawableRes = R.drawable.ic_sense;
                break;
        }

        return religionDrawableRes;
    }

    public static UserInfo cloneUserInfoBase2UserInfo(UserInfoBase base) {
        UserInfo userInfo = new UserInfo();
        userInfo.setRole(base.getRole());
        userInfo.setUserId(base.getUserId());
        userInfo.setNickname(base.getNickname());
        userInfo.setNicknamePending(base.getNicknamePending());
        userInfo.setAvatar(base.getAvatar());
        userInfo.setAvatarPending(base.getAvatarPending());
        userInfo.setSex(base.getSex());
        userInfo.setAge(base.getAge());
        userInfo.setProvince(base.getProvince());
        userInfo.setOccupation(base.getOccupation());
        userInfo.setCity(base.getCity());
        userInfo.setWechatNickname(base.getWechatNickname());
        userInfo.setWechatAvatar(base.getWechatAvatar());
        userInfo.setCompany(base.getCompany());
        userInfo.setIndustry(base.getIndustry());
        return userInfo;
    }

    public static String getSummary(UserInfoBase userInfo) {
        if (userInfo == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(userInfo.getAge()).append(Res.getString(R.string.str_age_diaplay));
        if (!TextUtils.isEmpty(userInfo.getProvince())) {
            builder.append("，");
            builder.append(userInfo.getProvince());
            builder.append(" ");
        }
        if (!TextUtils.isEmpty(userInfo.getCity())) {
            builder.append(userInfo.getCity());
        }
        if (!TextUtils.isEmpty(userInfo.getOccupation())) {
            builder.append("，");
            builder.append(userInfo.getOccupation());
        }
        return builder.toString();
    }

    public static String getSummary1(UserInfoBase userInfo) {
        if (userInfo == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(getGenderStr(userInfo.getSex()));
        builder.append("，");
        builder.append(userInfo.getAge()).append(Res.getString(R.string.str_age_diaplay));
        if (!TextUtils.isEmpty(userInfo.getProvince())) {
            builder.append("，");
            builder.append(userInfo.getProvince());
            builder.append(" ");
        }
        if (!TextUtils.isEmpty(userInfo.getCity())) {
            builder.append(userInfo.getCity());
        }
        return builder.toString();
    }

    public static String getCompanAndOccupation(UserInfoBase userInfo) {
        if (userInfo == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        if (!TextUtils.isEmpty(userInfo.getCompany())) {
            builder.append(userInfo.getCompany());
            builder.append(",");
        }
        if (!TextUtils.isEmpty(userInfo.getOccupation())) {

            builder.append(userInfo.getOccupation());
        }
        return builder.toString();
    }

    public static String getBodilyForm(Context context, UserInfo userInfo) {
        String bodily = "";
        if (userInfo.getSex() == Gender.FEMALE) {
            bodily = ArraysUtil.getValueByKey(context, com.oneone.R.array.bodily_form_female_array, userInfo.getBodilyForm());
        } else if (userInfo.getSex() == Gender.MALE) {
            bodily = ArraysUtil.getValueByKey(context, com.oneone.R.array.bodily_form_male_array, userInfo.getBodilyForm());
        }

        return bodily;
    }

    public static String getHeightDisplay(int height) {

        if (height <= 0) {
            return "";
        }

        String display = height + "cm";

        if (height > 200) {
            display = "200cm" + Res.getString(R.string.str_bellow);
        } else if (height < 145) {
            display = "145cm" + Res.getString(R.string.str_above);
        }
        return display;
    }
}
