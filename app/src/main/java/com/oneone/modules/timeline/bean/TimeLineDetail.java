package com.oneone.modules.timeline.bean;

/**
 * @author qingfei.chen
 * @since 2018/5/30.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class TimeLineDetail {
    public static String TYPE_MOMENT = "moment";
    public static String TYPE_NEW_SINGLE = "newsingle";
    String type;
    TimeLineCompose detail;

    public TimeLineDetail(String type, TimeLineCompose detail) {
        this.type = type;
        this.detail = detail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TimeLineCompose getDetail() {
        return detail;
    }

    public void setDetail(TimeLineCompose detail) {
        this.detail = detail;
    }
}
