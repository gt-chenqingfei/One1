package com.oneone.modules.msg.event;

import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.oneone.modules.msg.beans.TalkBeans.MyRecentContact;

import java.util.List;

/**
 * Created by here on 18/5/21.
 */

public class ImContactListEvent {
    private List<MyRecentContact> contactList;

    public ImContactListEvent (List<MyRecentContact> contactList) {
        this.contactList = contactList;
    }

    public List<MyRecentContact> getContactList() {
        return contactList;
    }

    public void setContactList(List<MyRecentContact> contactList) {
        this.contactList = contactList;
    }
}
