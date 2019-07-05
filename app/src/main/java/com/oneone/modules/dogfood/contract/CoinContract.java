package com.oneone.modules.dogfood.contract;

import com.oneone.framework.ui.ibase.IBaseView;
import com.oneone.modules.dogfood.beans.CoinRecord;

import java.util.List;

/**
 * Created by here on 18/5/2.
 */

public interface CoinContract {
    interface View extends IBaseView {
        void onCoinGetcoinbalance(int balance);
        void onCoinRecords(List<CoinRecord> record, int count);
    }

    interface Presenter {
        void coinGetcoinbalance();
        void coinRecords(int skip, int pageCount);
    }
}
