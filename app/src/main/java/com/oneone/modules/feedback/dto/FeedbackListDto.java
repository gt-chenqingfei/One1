package com.oneone.modules.feedback.dto;

import com.oneone.modules.feedback.beans.Feedback;
import com.oneone.modules.feedback.beans.FeedbackListItem;

import java.util.List;

/**
 * Created by here on 18/6/8.
 */

public class FeedbackListDto {
    private int count;
    private List<FeedbackListItem> list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<FeedbackListItem> getList() {
        return list;
    }

    public void setList(List<FeedbackListItem> list) {
        this.list = list;
    }
}
