package com.oneone.modules.feedback.beans;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by here on 18/6/11.
 */

public class FeedbackListItem implements Comparable<FeedbackListItem> {
    private String feedback;
    private int feedbackReason;
    private List<String> imgList;
    private String targetEntityId;
    private String targetEntityType;
    private int feedbackType;
    private int direction;
    private String attachment;
    private long gmtCreate;

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public int getFeedbackReason() {
        return feedbackReason;
    }

    public void setFeedbackReason(int feedbackReason) {
        this.feedbackReason = feedbackReason;
    }

    public List<String> getImgList() {
        return imgList;
    }

    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
    }

    public String getTargetEntityId() {
        return targetEntityId;
    }

    public void setTargetEntityId(String targetEntityId) {
        this.targetEntityId = targetEntityId;
    }

    public String getTargetEntityType() {
        return targetEntityType;
    }

    public void setTargetEntityType(String targetEntityType) {
        this.targetEntityType = targetEntityType;
    }

    public int getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(int feedbackType) {
        this.feedbackType = feedbackType;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }


    @Override
    public int compareTo(@NonNull FeedbackListItem feedbackListItem) {
        if (this.gmtCreate > feedbackListItem.getGmtCreate()) {
            return 1;
        }
        return -1;
    }
}
