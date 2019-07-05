package com.oneone.modules.msg.beans.TalkBeans;

import com.oneone.modules.feedback.beans.Feedback;

/**
 * Created by here on 18/6/12.
 */

public class PicMessage extends MyMessage {
    private String filePath;

    public PicMessage(String text, int type) {
        super(text, type);
    }
    public void init(String path) {
        this.filePath = path;
    }

    public String getFilePath () {
        return filePath;
    }
}
