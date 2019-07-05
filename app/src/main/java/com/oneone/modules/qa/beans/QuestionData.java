package com.oneone.modules.qa.beans;

import com.oneone.modules.qa.beans.Question;
import com.oneone.modules.qa.beans.QuestionItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by here on 18/4/18.
 */

public class QuestionData {
    private Question question;
    private List<QuestionItem> answerList;
    private String userAnswerId;

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public List<QuestionItem> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<QuestionItem> answerList) {
        this.answerList = answerList;
    }

    public String getUserAnswerId() {
        return userAnswerId;
    }

    public void setUserAnswerId(String userAnswerId) {
        this.userAnswerId = userAnswerId;
    }

    @Override
    public boolean equals(Object obj) {
        QuestionData other = (QuestionData) obj;
        return this.question.getQuestionId().equals(other.getQuestion().getQuestionId());
    }
}
