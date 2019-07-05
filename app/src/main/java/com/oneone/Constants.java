package com.oneone;

/**
 * @author qingfei.chen
 * @since 2018/3/26.
 */

public class Constants {
    public interface PREF {
        String PREF_USER_JWTOKEN = "user_jwtoken";
        String PREF_STORY_PREVIEW_BEAN = "MY_STORY_PREVIEW_BEAN";

        String PREF_UPDATE_USER_INFO_BY_ROLE = "USER_INFO_BY_ROLE";
        String PREF_MATCHER_GROUP_COUNT = "MATCHER_GROUP_COUNT";
        String IS_FIRST_REGISTER = "IS_FIRST_REGISTER";
        String PREF_WECHAT_DEBUG_TOKEN = "WECHAT_DEBUG_TOKEN";
    }

    public static class URL {
        static final String SCHEMA = "https://";
        static final String QINIU_BASE_URL_DEBUG = "http://img-demo.oneone.com/";
        static final String QINIU_BASE_URL_RELEASE = "https://img.oneone.com/";
        static final String PROTOCOL_URL_DEBUG = "https://zlpic.1meipo.com/oneone/oneone_agreement.htm";
        static final String PROTOCOL_URL_RELEASE = "https://zlpic.1meipo.com/oneone/oneone_agreement.htm";
        static final String HOST_DOMAIN_RELEASE = SCHEMA + "api.oneone.com";
        static final String HOST_DOMAIN_DEBUG = SCHEMA + "api-demo.oneone.com";

        public static String QINIU_BASE_URL() {
            if (BuildConfig.DEBUG) {
                return QINIU_BASE_URL_DEBUG;
            }
            return QINIU_BASE_URL_RELEASE;
        }

        public static String PROTOCOL_URL() {
            if (BuildConfig.DEBUG) {
                return PROTOCOL_URL_DEBUG;
            }
            return PROTOCOL_URL_RELEASE;
        }

        public static String API_URL() {
            if (BuildConfig.DEBUG) {
                return HOST_DOMAIN_DEBUG;
            }
            return HOST_DOMAIN_RELEASE;
        }
    }

    public interface ID {
        String ONE_ONE = "oneone";
    }

    public interface URL_SCHEMA {
        String SCHEMA = "oneone";
        String HOST = "m.oneone.com";
    }

    public interface STORY {
        int PHOTO_UPLOAD_MAX = 9;
    }
}
