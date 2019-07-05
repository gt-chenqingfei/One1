package com.oneone.modules.msg.beans.TalkBeans;

import com.oneone.framework.ui.imagepicker.bean.ImageItem;
import com.oneone.modules.feedback.beans.FeedbackListItem;

/**
 * Created by here on 18/6/11.
 */

public class ReportMessage extends MyMessage {
    private FeedbackListItem feedbackListItem;

    public ReportMessage(String text, int type) {
        super(text, type);
    }
    public void init(FeedbackListItem feedbackListItem) {
        this.feedbackListItem = feedbackListItem;
    }

    public FeedbackListItem getFeedbackListItem () {
        return feedbackListItem;
    }

}
