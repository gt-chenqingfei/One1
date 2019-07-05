package com.oneone.modules.qa.dto;

import com.oneone.modules.qa.beans.QuestionData;

import java.util.List;

/**
 * Created by here on 18/4/18.
 */

public class QuestionListDto {
    private int count;
    private List<QuestionData> list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<QuestionData> getList() {
        return list;
    }

    public void setList(List<QuestionData> list) {
        this.list = list;
    }
}
