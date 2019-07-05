package com.oneone.modules.main.me.contract;

import com.oneone.framework.ui.ibase.IBaseView;
import com.oneone.modules.user.bean.UserStatisticInfo;

/**
 * @author qingfei.chen
 * @since 2018/4/10.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public interface MineContract {

    interface Model {
    }

    interface View extends IBaseView {
    }

    interface Presenter {
        void fetchQAData();

        void getCoinBalance();

        void getStatisticUserQueryInfo(CoinBalanceListener listener);

        void taskLoginReceiveAward(MineContract.CoinBalanceListener listener);
    }

    interface CoinBalanceListener {
        void onStatisticUserQuery(UserStatisticInfo userStatisticInfo);

        void onTaskLoginReceiveAward(int taskAward, int received);
    }
}
