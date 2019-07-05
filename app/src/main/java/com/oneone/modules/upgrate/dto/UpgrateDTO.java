package com.oneone.modules.upgrate.dto;

/**
 * 版本升级返回结果实体类
 *
 * Created by ZhaiDongyang on 2018/6/26
 */
public class UpgrateDTO {
    private String minVersion;// 更新提醒版本
    private String hintVersion;// 强制升级最低版本
    private String downloadUrl;// 下载地址
    private String versionName;// 当前版本
    private String releaseNote;// 说明
    private String platform;// Android

    public String getMinVersion() {
        return minVersion;
    }

    public void setMinVersion(String minVersion) {
        this.minVersion = minVersion;
    }

    public String getHintVersion() {
        return hintVersion;
    }

    public void setHintVersion(String hintVersion) {
        this.hintVersion = hintVersion;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getReleaseNote() {
        return releaseNote;
    }

    public void setReleaseNote(String releaseNote) {
        this.releaseNote = releaseNote;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
