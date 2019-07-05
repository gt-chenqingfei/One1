package com.oneone.modules.qa.dto;

import com.oneone.modules.qa.beans.QuestionAnswerMeAndTargetUserBean;

import java.util.ArrayList;

/**
 * Created by here on 18/4/18.
 */

public class QaAnswersFortargetDto {
    private int count;
    private ArrayList<QuestionAnswerMeAndTargetUserBean> list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<QuestionAnswerMeAndTargetUserBean> getList() {
        return list;
    }

    public void setList(ArrayList<QuestionAnswerMeAndTargetUserBean> list) {
        this.list = list;
    }
}
