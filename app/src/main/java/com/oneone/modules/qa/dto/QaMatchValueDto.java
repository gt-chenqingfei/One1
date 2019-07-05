package com.oneone.modules.qa.dto;

import com.oneone.modules.qa.beans.MatchForClassify;

import java.util.List;

/**
 * Created by here on 18/4/18.
 */

public class QaMatchValueDto {
    private List<MatchForClassify> matchForClassify;
    private int matchForAll;

    public List<MatchForClassify> getMatchForClassify() {
        return matchForClassify;
    }

    public void setMatchForClassify(List<MatchForClassify> matchForClassify) {
        this.matchForClassify = matchForClassify;
    }

    public int getMatchForAll() {
        return matchForAll;
    }

    public void setMatchForAll(int matchForAll) {
        this.matchForAll = matchForAll;
    }
}
