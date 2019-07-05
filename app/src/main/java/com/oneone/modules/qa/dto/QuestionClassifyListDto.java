package com.oneone.modules.qa.dto;

import com.oneone.modules.qa.beans.QuestionClassify;

import java.util.ArrayList;

/**
 * Created by here on 18/4/18.
 */

public class QuestionClassifyListDto {
    private ArrayList<QuestionClassify> list;

    public ArrayList<QuestionClassify> getList() {
        return list;
    }

    public void setList(ArrayList<QuestionClassify> list) {
        this.list = list;
    }
}
