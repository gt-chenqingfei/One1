package com.oneone.modules.feedback.beans;

import java.util.List;

/**
 * Created by here on 18/6/8.
 */

public class Feedback {
    private String feedback;
    private List<String> imgs;
    private int feedbackReason;
    private String targetEntityId;
    private String targetEntityType;
    private int feedbackType;
    private String contactInfo;
    private String attachment;

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public int getFeedbackReason() {
        return feedbackReason;
    }

    public void setFeedbackReason(int feedbackReason) {
        this.feedbackReason = feedbackReason;
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

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
}
