package com.oneone.modules.main.contract;

import com.oneone.framework.ui.ibase.IBaseView;

/**
 * Created by here on 18/5/2.
 */

public interface MainContract {

    interface View extends IBaseView {
        void onTaskLoginReceiveAward(int taskAward, int received);
    }

    interface Presenter {
        void taskLoginReceiveAward();
    }
}
