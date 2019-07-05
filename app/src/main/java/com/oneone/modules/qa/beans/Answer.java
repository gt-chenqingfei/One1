package com.oneone.modules.qa.beans;

/**
 * Created by here on 18/4/18.
 */

public class Answer {
    private String questionId;
    private String answerId;
    private String content;

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
