package com.oneone.framework.ui;

/**
 * @author qingfei.chen
 * @since 2017/12/23.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */

public interface PullRefreshListener<K> {
    void loadNormal();

    void loadMore(K maxId);

    void loadRefreshMore(K maxId, long lastModify);

    boolean shouldRefreshingHeaderOnStart();
}
