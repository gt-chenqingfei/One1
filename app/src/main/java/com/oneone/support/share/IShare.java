package com.oneone.support.share;


/**
 * @author qingfei.chen
 * @since 2017/12/20.
 * Copyright © 2017 Here Technology Co.,Ltd. All rights reserved.
 */

public interface IShare {
    void share(ShareParams params);

    boolean getAuthorized();
}
