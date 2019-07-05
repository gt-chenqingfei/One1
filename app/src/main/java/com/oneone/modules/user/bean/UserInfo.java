package com.oneone.modules.user.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.io.Serializable;

public class UserInfo extends UserInfoBase implements Cloneable, Parcelable {
    private long finishTime;
    private long avaliableTime;
    private long gmtCreate;
    private long gmtModified;
    private int characterSetting;
    private int nicknameStatus;
    private String country;
    private String countryCode;
    private String cityCode;
    private String provinceCode;
    private String birthdate;
    private int infoCompletedScore;
    private int avatarStatus;
    private String hometownCountry;
    private String hometownCountryCode;
    private String hometownProvince;
    private String hometownProvinceCode;
    private String hometownCity;
    private String hometownCityCode;
    private int hometownDisplay;
    private String monologue;
    private String monologueDisplay;
    private String monologuePending;
    private int height;
    private int heightDisplay;
    private int bodilyForm;
    private int bodilyFormDisplay;
    private int familyInfo;
    private int familyInfoDisplay;
    private int religion;
    private int religionDisplay;
    private int eatingHabits;
    private int eatingHabitsDisplay;
    private int lifeHabits;
    private int lifeHabitsDisplay;
    private int smokingHabits;
    private int smokingHabitsDisplay;
    private int drinkingHabits;
    private int drinkingHabitsDisplay;
    private String occupationCode;
    private Integer industryDisplay = 0;
    private String industryCode;
    private int companyDisplay;
    private int annualIncomeMin;
    private int annualIncomeMax;
    private String annualIncomeCurrency;
    private int annualIncomeDisplay;
    private String collegeName;
    private int collegeNameDisplay;
    private int degree;
    private int degreeDisplay;
    private String petSpecies;
    private String petName;
    private String constellation;
    private UserRoleSettingTagBean occupationTags;
    private UserRoleSettingTagBean skillTags;
    private UserRoleSettingTagBean characterTags;
    private UserRoleSettingTagBean experienceTags;
    private UserRoleSettingTagBean senseOfWorthTags;
    private UserRoleSettingTagBean spousePrefsTags;
    private int likeStatus;
    private int noFeelStatus;
    private float matchValue;
    private int intersectionValue;
    private int follow;
    private QaAnswer qaAnswer;

    public String getIndustryCode() {
        return industryCode;
    }

    public void setIndustryCode(String industryCode) {
        this.industryCode = industryCode;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getHometownProvinceCode() {
        return hometownProvinceCode;
    }

    public void setHometownProvinceCode(String hometownProvinceCode) {
        this.hometownProvinceCode = hometownProvinceCode;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }

    public long getAvaliableTime() {
        return avaliableTime;
    }

    public void setAvaliableTime(long avaliableTime) {
        this.avaliableTime = avaliableTime;
    }

    public long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public long getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(long gmtModified) {
        this.gmtModified = gmtModified;
    }


    public int getCharacterSetting() {
        return characterSetting;
    }

    public void setCharacterSetting(int characterSetting) {
        this.characterSetting = characterSetting;
    }


    public int getNicknameStatus() {
        return nicknameStatus;
    }

    public void setNicknameStatus(int nicknameStatus) {
        this.nicknameStatus = nicknameStatus;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }


    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }


    public int getInfoCompletedScore() {
        return infoCompletedScore;
    }

    public void setInfoCompletedScore(int infoCompletedScore) {
        this.infoCompletedScore = infoCompletedScore;
    }

    public int getAvatarStatus() {
        return avatarStatus;
    }

    public void setAvatarStatus(int avatarStatus) {
        this.avatarStatus = avatarStatus;
    }

    public String getHometownCountry() {
        return hometownCountry;
    }

    public void setHometownCountry(String hometownCountry) {
        this.hometownCountry = hometownCountry;
    }

    public String getHometownCountryCode() {
        return hometownCountryCode;
    }

    public void setHometownCountryCode(String hometownCountryCode) {
        this.hometownCountryCode = hometownCountryCode;
    }

    public String getHometownProvince() {
        return hometownProvince;
    }

    public void setHometownProvince(String hometownProvince) {
        this.hometownProvince = hometownProvince;
    }

    public String getHometownCity() {
        return hometownCity;
    }

    public void setHometownCity(String hometownCity) {
        this.hometownCity = hometownCity;
    }

    public String getHometownCityCode() {
        return hometownCityCode;
    }

    public void setHometownCityCode(String hometownCityCode) {
        this.hometownCityCode = hometownCityCode;
    }

    public int getHometownDisplay() {
        return hometownDisplay;
    }

    public void setHometownDisplay(int hometownDisplay) {
        this.hometownDisplay = hometownDisplay;
    }

    public String getMonologue() {
        return monologue;
    }

    public void setMonologue(String monologue) {
        this.monologue = monologue;
    }

    public String getMonologueDisplay() {
        return monologueDisplay;
    }

    public void setMonologueDisplay(String monologueDisplay) {
        this.monologueDisplay = monologueDisplay;
    }

    public String getMyMonologue() {
        if (!TextUtils.isEmpty(monologuePending)) {
            return monologuePending;
        } else {
            return monologue;
        }
    }

    public String getMonologuePending() {
        return monologuePending;
    }

    public void setMonologuePending(String monologuePending) {
        this.monologuePending = monologuePending;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeightDisplay() {
        return heightDisplay;
    }

    public void setHeightDisplay(int heightDisplay) {
        this.heightDisplay = heightDisplay;
    }

    public int getBodilyForm() {
        return bodilyForm;
    }

    public void setBodilyForm(int bodilyForm) {
        this.bodilyForm = bodilyForm;
    }

    public int getBodilyFormDisplay() {
        return bodilyFormDisplay;
    }

    public void setBodilyFormDisplay(int bodilyFormDisplay) {
        this.bodilyFormDisplay = bodilyFormDisplay;
    }

    public int getFamilyInfo() {
        return familyInfo;
    }

    public void setFamilyInfo(int familyInfo) {
        this.familyInfo = familyInfo;
    }

    public int getFamilyInfoDisplay() {
        return familyInfoDisplay;
    }

    public void setFamilyInfoDisplay(int familyInfoDisplay) {
        this.familyInfoDisplay = familyInfoDisplay;
    }

    public int getReligion() {
        return religion;
    }

    public void setReligion(int religion) {
        this.religion = religion;
    }

    public int getReligionDisplay() {
        return religionDisplay;
    }

    public void setReligionDisplay(int religionDisplay) {
        this.religionDisplay = religionDisplay;
    }

    public int getEatingHabits() {
        return eatingHabits;
    }

    public void setEatingHabits(int eatingHabits) {
        this.eatingHabits = eatingHabits;
    }

    public int getEatingHabitsDisplay() {
        return eatingHabitsDisplay;
    }

    public void setEatingHabitsDisplay(int eatingHabitsDisplay) {
        this.eatingHabitsDisplay = eatingHabitsDisplay;
    }

    public int getLifeHabits() {
        return lifeHabits;
    }

    public void setLifeHabits(int lifeHabits) {
        this.lifeHabits = lifeHabits;
    }

    public int getLifeHabitsDisplay() {
        return lifeHabitsDisplay;
    }

    public void setLifeHabitsDisplay(int lifeHabitsDisplay) {
        this.lifeHabitsDisplay = lifeHabitsDisplay;
    }

    public int getSmokingHabits() {
        return smokingHabits;
    }

    public void setSmokingHabits(int smokingHabits) {
        this.smokingHabits = smokingHabits;
    }

    public int getSmokingHabitsDisplay() {
        return smokingHabitsDisplay;
    }

    public void setSmokingHabitsDisplay(int smokingHabitsDisplay) {
        this.smokingHabitsDisplay = smokingHabitsDisplay;
    }

    public int getDrinkingHabits() {
        return drinkingHabits;
    }

    public void setDrinkingHabits(int drinkingHabits) {
        this.drinkingHabits = drinkingHabits;
    }

    public int getDrinkingHabitsDisplay() {
        return drinkingHabitsDisplay;
    }

    public void setDrinkingHabitsDisplay(int drinkingHabitsDisplay) {
        this.drinkingHabitsDisplay = drinkingHabitsDisplay;
    }

    public String getOccupationCode() {
        return occupationCode;
    }

    public void setOccupationCode(String occupationCode) {
        this.occupationCode = occupationCode;
    }

    public Integer getIndustryDisplay() {
        return industryDisplay;
    }

    public void setIndustryDisplay(Integer industryDisplay) {
        this.industryDisplay = industryDisplay;
    }

    public int getCompanyDisplay() {
        return companyDisplay;
    }

    public void setCompanyDisplay(int companyDisplay) {
        this.companyDisplay = companyDisplay;
    }

    public int getAnnualIncomeMin() {
        return annualIncomeMin;
    }

    public void setAnnualIncomeMin(int annualIncomeMin) {
        this.annualIncomeMin = annualIncomeMin;
    }

    public int getAnnualIncomeMax() {
        return annualIncomeMax;
    }

    public void setAnnualIncomeMax(int annualIncomeMax) {
        this.annualIncomeMax = annualIncomeMax;
    }

    public String getAnnualIncomeCurrency() {
        return annualIncomeCurrency;
    }

    public void setAnnualIncomeCurrency(String annualIncomeCurrency) {
        this.annualIncomeCurrency = annualIncomeCurrency;
    }

    public int getAnnualIncomeDisplay() {
        return annualIncomeDisplay;
    }

    public void setAnnualIncomeDisplay(int annualIncomeDisplay) {
        this.annualIncomeDisplay = annualIncomeDisplay;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public int getCollegeNameDisplay() {
        return collegeNameDisplay;
    }

    public void setCollegeNameDisplay(int collegeNameDisplay) {
        this.collegeNameDisplay = collegeNameDisplay;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public int getDegreeDisplay() {
        return degreeDisplay;
    }

    public void setDegreeDisplay(int degreeDisplay) {
        this.degreeDisplay = degreeDisplay;
    }

    public String getPetSpecies() {
        return petSpecies;
    }

    public void setPetSpecies(String petSpecies) {
        this.petSpecies = petSpecies;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public UserRoleSettingTagBean getOccupationTags() {
        return occupationTags;
    }

    public void setOccupationTags(UserRoleSettingTagBean occupationTags) {
        this.occupationTags = occupationTags;
    }

    public UserRoleSettingTagBean getSkillTags() {
        return skillTags;
    }

    public void setSkillTags(UserRoleSettingTagBean skillTags) {
        this.skillTags = skillTags;
    }

    public UserRoleSettingTagBean getCharacterTags() {
        return characterTags;
    }

    public void setCharacterTags(UserRoleSettingTagBean characterTags) {
        this.characterTags = characterTags;
    }

    public UserRoleSettingTagBean getExperienceTags() {
        return experienceTags;
    }

    public void setExperienceTags(UserRoleSettingTagBean experienceTags) {
        this.experienceTags = experienceTags;
    }

    public UserRoleSettingTagBean getSenseOfWorthTags() {
        return senseOfWorthTags;
    }

    public void setSenseOfWorthTags(UserRoleSettingTagBean senseOfWorthTags) {
        this.senseOfWorthTags = senseOfWorthTags;
    }

    public UserRoleSettingTagBean getSpousePrefsTags() {
        return spousePrefsTags;
    }

    public void setSpousePrefsTags(UserRoleSettingTagBean spousePrefsTags) {
        this.spousePrefsTags = spousePrefsTags;
    }

    public int getLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(int likeStatus) {
        this.likeStatus = likeStatus;
    }

    public int getNoFeelStatus() {
        return noFeelStatus;
    }

    public void setNoFeelStatus(int noFeelStatus) {
        this.noFeelStatus = noFeelStatus;
    }

    public float getMatchValue() {
        return matchValue;
    }

    public void setMatchValue(float matchValue) {
        this.matchValue = matchValue;
    }

    public int getIntersectionValue() {
        return intersectionValue;
    }

    public void setIntersectionValue(int intersectionValue) {
        this.intersectionValue = intersectionValue;
    }

    public int getFollow() {
        return follow;
    }

    public void setFollow(int follow) {
        this.follow = follow;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    @Override
    public UserInfo clone() throws CloneNotSupportedException {
        UserInfo clone = (UserInfo) super.clone();
        if (occupationTags != null) {
            clone.occupationTags = occupationTags.clone();
        }
        if (skillTags != null) {
            clone.skillTags = skillTags.clone();
        }
        if (characterTags != null) {
            clone.characterTags = characterTags.clone();
        }
        if (experienceTags != null) {
            clone.experienceTags = experienceTags.clone();
        }
        if (senseOfWorthTags != null) {
            clone.senseOfWorthTags = senseOfWorthTags.clone();
        }
        if (spousePrefsTags != null) {
            clone.spousePrefsTags = spousePrefsTags.clone();
        }
        return clone;
    }

    public QaAnswer getQaAnswer() {
        return qaAnswer;
    }

    public void setQaAnswer(QaAnswer qaAnswer) {
        this.qaAnswer = qaAnswer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(this.finishTime);
        dest.writeLong(this.avaliableTime);
        dest.writeLong(this.gmtCreate);
        dest.writeLong(this.gmtModified);
        dest.writeInt(this.characterSetting);
        dest.writeInt(this.nicknameStatus);
        dest.writeString(this.country);
        dest.writeString(this.countryCode);
        dest.writeString(this.cityCode);
        dest.writeString(this.provinceCode);
        dest.writeString(this.birthdate);
        dest.writeInt(this.infoCompletedScore);
        dest.writeInt(this.avatarStatus);
        dest.writeString(this.hometownCountry);
        dest.writeString(this.hometownCountryCode);
        dest.writeString(this.hometownProvince);
        dest.writeString(this.hometownProvinceCode);
        dest.writeString(this.hometownCity);
        dest.writeString(this.hometownCityCode);
        dest.writeInt(this.hometownDisplay);
        dest.writeString(this.monologue);
        dest.writeString(this.monologueDisplay);
        dest.writeString(this.monologuePending);
        dest.writeInt(this.height);
        dest.writeInt(this.heightDisplay);
        dest.writeInt(this.bodilyForm);
        dest.writeInt(this.bodilyFormDisplay);
        dest.writeInt(this.familyInfo);
        dest.writeInt(this.familyInfoDisplay);
        dest.writeInt(this.religion);
        dest.writeInt(this.religionDisplay);
        dest.writeInt(this.eatingHabits);
        dest.writeInt(this.eatingHabitsDisplay);
        dest.writeInt(this.lifeHabits);
        dest.writeInt(this.lifeHabitsDisplay);
        dest.writeInt(this.smokingHabits);
        dest.writeInt(this.smokingHabitsDisplay);
        dest.writeInt(this.drinkingHabits);
        dest.writeInt(this.drinkingHabitsDisplay);
        dest.writeString(this.occupationCode);
        dest.writeInt(this.industryDisplay);
        dest.writeString(this.industryCode);
        dest.writeInt(this.companyDisplay);
        dest.writeInt(this.annualIncomeMin);
        dest.writeInt(this.annualIncomeMax);
        dest.writeString(this.annualIncomeCurrency);
        dest.writeInt(this.annualIncomeDisplay);
        dest.writeString(this.collegeName);
        dest.writeInt(this.collegeNameDisplay);
        dest.writeInt(this.degree);
        dest.writeInt(this.degreeDisplay);
        dest.writeString(this.petSpecies);
        dest.writeString(this.petName);
        dest.writeString(this.constellation);
        dest.writeParcelable(this.occupationTags, flags);
        dest.writeParcelable(this.skillTags, flags);
        dest.writeParcelable(this.characterTags, flags);
        dest.writeParcelable(this.experienceTags, flags);
        dest.writeParcelable(this.senseOfWorthTags, flags);
        dest.writeParcelable(this.spousePrefsTags, flags);
        dest.writeInt(this.likeStatus);
        dest.writeInt(this.noFeelStatus);
        dest.writeFloat(this.matchValue);
        dest.writeInt(this.intersectionValue);
        dest.writeInt(this.follow);
    }

    public UserInfo() {
    }

    protected UserInfo(Parcel in) {
        super(in);
        this.finishTime = in.readLong();
        this.avaliableTime = in.readLong();
        this.gmtCreate = in.readLong();
        this.gmtModified = in.readLong();
        this.characterSetting = in.readInt();
        this.nicknameStatus = in.readInt();
        this.country = in.readString();
        this.countryCode = in.readString();
        this.cityCode = in.readString();
        this.provinceCode = in.readString();
        this.birthdate = in.readString();
        this.infoCompletedScore = in.readInt();
        this.avatarStatus = in.readInt();
        this.hometownCountry = in.readString();
        this.hometownCountryCode = in.readString();
        this.hometownProvince = in.readString();
        this.hometownProvinceCode = in.readString();
        this.hometownCity = in.readString();
        this.hometownCityCode = in.readString();
        this.hometownDisplay = in.readInt();
        this.monologue = in.readString();
        this.monologueDisplay = in.readString();
        this.monologuePending = in.readString();
        this.height = in.readInt();
        this.heightDisplay = in.readInt();
        this.bodilyForm = in.readInt();
        this.bodilyFormDisplay = in.readInt();
        this.familyInfo = in.readInt();
        this.familyInfoDisplay = in.readInt();
        this.religion = in.readInt();
        this.religionDisplay = in.readInt();
        this.eatingHabits = in.readInt();
        this.eatingHabitsDisplay = in.readInt();
        this.lifeHabits = in.readInt();
        this.lifeHabitsDisplay = in.readInt();
        this.smokingHabits = in.readInt();
        this.smokingHabitsDisplay = in.readInt();
        this.drinkingHabits = in.readInt();
        this.drinkingHabitsDisplay = in.readInt();
        this.occupationCode = in.readString();
        this.industryDisplay = in.readInt();
        this.industryCode = in.readString();
        this.companyDisplay = in.readInt();
        this.annualIncomeMin = in.readInt();
        this.annualIncomeMax = in.readInt();
        this.annualIncomeCurrency = in.readString();
        this.annualIncomeDisplay = in.readInt();
        this.collegeName = in.readString();
        this.collegeNameDisplay = in.readInt();
        this.degree = in.readInt();
        this.degreeDisplay = in.readInt();
        this.petSpecies = in.readString();
        this.petName = in.readString();
        this.constellation = in.readString();
        this.occupationTags = in.readParcelable(UserRoleSettingTagBean.class.getClassLoader());
        this.skillTags = in.readParcelable(UserRoleSettingTagBean.class.getClassLoader());
        this.characterTags = in.readParcelable(UserRoleSettingTagBean.class.getClassLoader());
        this.experienceTags = in.readParcelable(UserRoleSettingTagBean.class.getClassLoader());
        this.senseOfWorthTags = in.readParcelable(UserRoleSettingTagBean.class.getClassLoader());
        this.spousePrefsTags = in.readParcelable(UserRoleSettingTagBean.class.getClassLoader());
        this.likeStatus = in.readInt();
        this.noFeelStatus = in.readInt();
        this.matchValue = in.readFloat();
        this.intersectionValue = in.readInt();
        this.follow = in.readInt();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel source) {
            return new UserInfo(source);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    @Override
    public String toString() {
        return "UserInfo{" +
                "nickname=" + getNickname() +
                '}';
    }
}
