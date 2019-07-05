package com.oneone.modules.dogfood.dto;

import com.oneone.modules.dogfood.beans.CoinRecord;

import java.util.List;

/**
 * Created by here on 18/5/2.
 */

public class CoinRecordDto {
    private int count;
    private List<CoinRecord> list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<CoinRecord> getList() {
        return list;
    }

    public void setList(List<CoinRecord> list) {
        this.list = list;
    }
}
