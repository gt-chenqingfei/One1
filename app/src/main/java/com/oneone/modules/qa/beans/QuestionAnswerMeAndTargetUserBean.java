package com.oneone.modules.qa.beans;

import com.oneone.modules.qa.beans.Answer;
import com.oneone.modules.qa.beans.Question;

import java.util.ArrayList;

/**
 * Created by here on 18/4/18.
 */

public class QuestionAnswerMeAndTargetUserBean {
    private Question question;
    private ArrayList<QuestionItem> answerList;
    private String userAnswerId;
    private String selfAnswerId;

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public ArrayList<QuestionItem> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(ArrayList<QuestionItem> answerList) {
        this.answerList = answerList;
    }

    public String getUserAnswerId() {
        return userAnswerId;
    }

    public void setUserAnswerId(String userAnswerId) {
        this.userAnswerId = userAnswerId;
    }

    public String getSelfAnswerId() {
        return selfAnswerId;
    }

    public void setSelfAnswerId(String selfAnswerId) {
        this.selfAnswerId = selfAnswerId;
    }
}
