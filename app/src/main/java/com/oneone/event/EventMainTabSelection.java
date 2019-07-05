package com.oneone.event;


/**
 * Created by here on 18/7/4.
 * 通过EventBus 通知主页面tab切换
 */

public class EventMainTabSelection {
    public static final String TAB_TIMELINE = "timeline";
    public static final String TAB_ME = "me";
    public static final String TAB_SINGLES = "singles";
    public static final String TAB_EXPLORE = "explore";
    public static final String TAB_MESSAGE = "message";
    private String tabSelection;

    public EventMainTabSelection(String tabSelection) {
        this.tabSelection = tabSelection;
    }

    public String getTabSelection() {
        return tabSelection;
    }

    public void setTabSelection(String tabSelection) {
        this.tabSelection = tabSelection;
    }

}
