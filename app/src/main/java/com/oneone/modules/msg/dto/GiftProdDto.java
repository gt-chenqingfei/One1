package com.oneone.modules.msg.dto;

import com.oneone.modules.msg.beans.GiftProd;

import java.util.List;

/**
 * Created by here on 18/5/10.
 */

public class GiftProdDto {
    private int count;
    private List<GiftProd> list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<GiftProd> getList() {
        return list;
    }

    public void setList(List<GiftProd> list) {
        this.list = list;
    }
}
