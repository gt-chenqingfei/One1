package com.oneone.modules.user.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class UserRoleSettingTagBean implements Cloneable, Parcelable {
    private List<String> systemTags = null;
    private List<String> customTags = null;
    private List<String> selectedTagQueueList = null;

    public List<String> getSystemTags() {
        return systemTags;
    }

    public void setSystemTags(List<String> systemTags) {
        this.systemTags = systemTags;
    }

    public List<String> getCustomTags() {
        return customTags;
    }

    public void setCustomTags(List<String> customTags) {
        this.customTags = customTags;
    }

    private void newSelectedTagQueueListIfNeeded() {
        if (selectedTagQueueList == null) {
            selectedTagQueueList = new ArrayList<>();
        }
    }

    private void newSystemTagsIfNeeded() {
        if (systemTags == null) {
            systemTags = new ArrayList<>();
        }
    }

    private void newCustomTagsIfNeeded() {
        if (customTags == null) {
            customTags = new ArrayList<>();
        }
    }

    public void addSystemTags(String tagStr) {
        newSelectedTagQueueListIfNeeded();
        newSystemTagsIfNeeded();
        selectedTagQueueList.add(tagStr);
        systemTags.add(tagStr);
    }

    public void addCustomTags(String tagStr) {
        newSelectedTagQueueListIfNeeded();
        newCustomTagsIfNeeded();
        selectedTagQueueList.add(tagStr);
        customTags.add(tagStr);
    }

    public void removeSystemTags(String tagStr) {
        if (selectedTagQueueList != null && !selectedTagQueueList.isEmpty()) {
            selectedTagQueueList.remove(tagStr);
        }
        if (systemTags != null && !systemTags.isEmpty()) {
            systemTags.remove(tagStr);
        }
    }

    public void removeCustomTags(String tagStr) {
        if (selectedTagQueueList != null && !selectedTagQueueList.isEmpty()) {
            selectedTagQueueList.remove(tagStr);
        }
        if (customTags != null && !customTags.isEmpty()) {
            customTags.remove(tagStr);
        }
    }

    public List<String> getSelectedTagQueueList() {
        return selectedTagQueueList;
    }

    public int getSelectedTagCount() {
        if (selectedTagQueueList == null) {
            return 0;
        }
        return selectedTagQueueList.size();
    }

    public int getTagCount() {
        int count = 0;
        if (this.systemTags != null)
            count += systemTags.size();
        if (this.customTags != null)
            count += customTags.size();

        return count;
    }

    public void clearTags() {
        if (customTags != null) {
            this.customTags.clear();
        }
        if (systemTags != null) {
            this.systemTags.clear();
        }
        if (selectedTagQueueList != null) {
            this.selectedTagQueueList.clear();
        }
    }

    public String randomTag() {
        ArrayList<String> randomTagList = new ArrayList<String>();
        if (systemTags != null && systemTags.size() > 0) {
            randomTagList.addAll(systemTags);
        }
        if (customTags != null && customTags.size() > 0) {
            randomTagList.addAll(customTags);
        }

        int index = (int) (Math.random() * randomTagList.size());
        return randomTagList.get(index);
    }

    @Override
    protected UserRoleSettingTagBean clone() throws CloneNotSupportedException {
        UserRoleSettingTagBean clone = (UserRoleSettingTagBean) super.clone();
        if (systemTags != null) {
            clone.systemTags = (List<String>) ((ArrayList) systemTags).clone();
        }
        if (customTags != null) {
            clone.customTags = (List<String>) ((ArrayList) customTags).clone();
        }
        if (selectedTagQueueList != null) {
            clone.selectedTagQueueList = (List<String>) ((ArrayList) selectedTagQueueList).clone();
        }
        return clone;

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.systemTags);
        dest.writeStringList(this.customTags);
        dest.writeStringList(this.selectedTagQueueList);
    }

    public UserRoleSettingTagBean() {
    }

    protected UserRoleSettingTagBean(Parcel in) {
        this.systemTags = in.createStringArrayList();
        this.customTags = in.createStringArrayList();
        this.selectedTagQueueList = in.createStringArrayList();
    }

    public static final Parcelable.Creator<UserRoleSettingTagBean> CREATOR = new Parcelable.Creator<UserRoleSettingTagBean>() {
        @Override
        public UserRoleSettingTagBean createFromParcel(Parcel source) {
            return new UserRoleSettingTagBean(source);
        }

        @Override
        public UserRoleSettingTagBean[] newArray(int size) {
            return new UserRoleSettingTagBean[size];
        }
    };
}
