package com.oneone.modules.support.bean;

import com.oneone.BuildConfig;

/**
 * 分享到微信信息
 * <p>
 * Created by ZhaiDongyang on 2018/7/9
 */
public class ShareInfo {

    public static final String SHARE_KEY = "share_key";
    public static final String SHARE_INFO_JSON = "ShareInfo.json";
    public static final String KEY_SELF_USER_ID = "${SELF_USER_ID}";
    public static final String KEY_USER_ID = "${USER_ID}";
    public static final String KEY_SIGN = "${SIGN}";
    public static final String SHARE_INFO_MARCHER_NAME = "${NICKNAME}";
    public static final String SHARE_INVITE_SINGLE = "inviteSingle";
    public static final String SHARE_INVITE_MATCHER = "inviteMatcher";

    private ShareHost host;
    private SharePersonInfo matcherProfile;
    private SharePersonInfo selfMatcherProfile;
    private SharePersonInfo selfSingleProfile;
    private SharePersonInfo inviteMatcher;
    private SharePersonInfo singleProfile;
    private SharePersonInfo inviteSingle;


    public String getShareHost() {
        if (BuildConfig.DEBUG) {
            return host.getTest();
        } else {
            return host.getOnline();
        }
    }

    public ShareHost getHost() {
        return host;
    }

    public void setHost(ShareHost host) {
        this.host = host;
    }

    public SharePersonInfo getMatcherProfile() {
        return matcherProfile;
    }

    public void setMatcherProfile(SharePersonInfo matcherProfile) {
        this.matcherProfile = matcherProfile;
    }

    public SharePersonInfo getSelfMatcherProfile() {
        return selfMatcherProfile;
    }

    public void setSelfMatcherProfile(SharePersonInfo selfMatcherProfile) {
        this.selfMatcherProfile = selfMatcherProfile;
    }

    public SharePersonInfo getSelfSingleProfile() {
        return selfSingleProfile;
    }

    public void setSelfSingleProfile(SharePersonInfo selfSingleProfile) {
        this.selfSingleProfile = selfSingleProfile;
    }

    public SharePersonInfo getInviteMatcher() {
        return inviteMatcher;
    }

    public void setInviteMatcher(SharePersonInfo inviteMatcher) {
        this.inviteMatcher = inviteMatcher;
    }

    public SharePersonInfo getSingleProfile() {
        return singleProfile;
    }

    public void setSingleProfile(SharePersonInfo singleProfile) {
        this.singleProfile = singleProfile;
    }

    public SharePersonInfo getInviteSingle() {
        return inviteSingle;
    }

    public void setInviteSingle(SharePersonInfo inviteSingle) {
        this.inviteSingle = inviteSingle;
    }

    public class ShareHost {
        private String test;
        private String online;

        public String getTest() {
            return test;
        }

        public void setTest(String test) {
            this.test = test;
        }

        public String getOnline() {
            return online;
        }

        public void setOnline(String online) {
            this.online = online;
        }
    }

    public class SharePersonInfo {
        private String title;
        private String description;
        private String url;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

}
