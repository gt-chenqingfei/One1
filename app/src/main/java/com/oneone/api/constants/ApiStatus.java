package com.oneone.api.constants;

/**
 * @author qingfei.chen
 * @since 2018/4/12.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public interface ApiStatus {
    int OK = 0;
    int NO_BIND_PHONE = 203005;
    int VALID_CODE_ERROR = 203003;
    int MOBILE_HAS_BIND_OTHER_ACCOUNT = 203006;
    int THIRD_ACCOUNT_HAS_BIND_PHONE = 203007;
    int USER_NOT_EXIST = 203999;
    int MOBILE_ERROR = 203002;
    int COUNTRY_CODE_ERROR = 203001;
    int TIMELINE_REPORT_NOTEXIST_OR_INVALID = 205001;
    int TIMELINE_REPORTED = 205002;
}
