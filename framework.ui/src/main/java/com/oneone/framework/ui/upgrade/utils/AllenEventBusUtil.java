package com.oneone.framework.ui.upgrade.utils;

import com.oneone.framework.ui.upgrade.v2.eventbus.CommonEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by allenliu on 2018/1/18.
 */

public class AllenEventBusUtil {
    public static void sendEventBus(int eventType) {
        CommonEvent commonEvent = new CommonEvent();
        commonEvent.setSuccessful(true);
        commonEvent.setEventType(eventType);
        EventBus.getDefault().post(commonEvent);
    }
}
