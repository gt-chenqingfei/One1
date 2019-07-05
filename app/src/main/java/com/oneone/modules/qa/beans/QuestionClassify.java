package com.oneone.modules.qa.beans;

/**
 * Created by here on 18/4/18.
 */

public class QuestionClassify {
    private String classifyId;
    private String classifyName;
    private String classifyIcon;
    private int position;
    private int questionCount;
    private int answeredCount;

    public String getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(String classifyId) {
        this.classifyId = classifyId;
    }

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }

    public String getClassifyIcon() {
        return classifyIcon;
    }

    public void setClassifyIcon(String classifyIcon) {
        this.classifyIcon = classifyIcon;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

    public int getAnsweredCount() {
        return answeredCount;
    }

    public void setAnsweredCount(int answeredCount) {
        this.answeredCount = answeredCount;
    }
}
