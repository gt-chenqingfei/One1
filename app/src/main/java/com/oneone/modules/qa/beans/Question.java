package com.oneone.modules.qa.beans;

/**
 * Created by here on 18/4/18.
 */

public class Question {
    private String questionId;
    private String priority;
    private String classifyId;
    private String content;
    private String orderForPriority;
    private String orderForClassify;

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(String classifyId) {
        this.classifyId = classifyId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOrderForPriority() {
        return orderForPriority;
    }

    public void setOrderForPriority(String orderForPriority) {
        this.orderForPriority = orderForPriority;
    }

    public String getOrderForClassify() {
        return orderForClassify;
    }

    public void setOrderForClassify(String orderForClassify) {
        this.orderForClassify = orderForClassify;
    }

    @Override
    public String toString() {
        return "Question{" +
                "questionId='" + questionId + '\'' +
                ", priority='" + priority + '\'' +
                ", classifyId='" + classifyId + '\'' +
                ", content='" + content + '\'' +
                ", orderForPriority='" + orderForPriority + '\'' +
                ", orderForClassify='" + orderForClassify + '\'' +
                '}';
    }
}
