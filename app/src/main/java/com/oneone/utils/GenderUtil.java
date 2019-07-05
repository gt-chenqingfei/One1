package com.oneone.utils;

import com.oneone.R;
import com.oneone.api.constants.Gender;
import com.oneone.framework.ui.utils.Res;
import com.qiniu.android.utils.StringUtils;

/**
 * @author qingfei.chen
 * @since 2018/4/20.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class GenderUtil {
    public static String getGender(int gender) {
        switch (gender) {
            case Gender.FEMALE:
                return Res.getString(R.string.str_gender_female);
            case Gender.MALE:
                return Res.getString(R.string.str_gender_male);
            case Gender.UNKNOWN:
                return Res.getString(R.string.str_gender_unknown);
        }
        return Res.getString(R.string.str_gender_unknown);
    }
}
