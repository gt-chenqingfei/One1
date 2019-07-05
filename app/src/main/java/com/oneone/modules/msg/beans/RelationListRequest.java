package com.oneone.modules.msg.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by here on 18/5/31.
 */

public class RelationListRequest {
    private List<String> targetImUserIds = new ArrayList<String>();

    public List<String> getTargetImUserIds() {
        return targetImUserIds;
    }

    public void setTargetImUserIds(List<String> targetImUserIds) {
        this.targetImUserIds = targetImUserIds;
    }
}
