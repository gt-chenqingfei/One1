package com.oneone.modules.msg.event;

import com.oneone.modules.msg.beans.TalkBeans.MyRecentContact;

/**
 * Created by here on 18/5/22.
 */

public class ImContactChangeEvent {
    private MyRecentContact myRecentContact;

    public ImContactChangeEvent (MyRecentContact myRecentContact) {
        this.myRecentContact = myRecentContact;
    }

    public MyRecentContact getMyRecentContact() {
        return myRecentContact;
    }

    public void setMyRecentContact(MyRecentContact myRecentContact) {
        this.myRecentContact = myRecentContact;
    }
}
