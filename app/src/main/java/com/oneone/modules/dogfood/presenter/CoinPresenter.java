package com.oneone.modules.dogfood.presenter;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.oneone.BasePresenter;
import com.oneone.event.EventRefreshCoin;
import com.oneone.modules.dogfood.beans.Balance;
import com.oneone.modules.dogfood.beans.CoinRecord;
import com.oneone.modules.dogfood.contract.CoinContract;
import com.oneone.modules.dogfood.dto.CoinRecordDto;
import com.oneone.modules.dogfood.model.CoinModel;
import com.oneone.restful.ApiResult;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by here on 18/5/2.
 */

public class CoinPresenter extends BasePresenter<CoinContract.View> implements CoinContract.Presenter {
    private CoinModel coinModel;

    @Override
    public void onAttachView(CoinContract.View view) {
        super.onAttachView(view);

        coinModel = new CoinModel(view.getActivityContext());
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void coinGetcoinbalance() {
        enqueue(new AsyncTask<Object, Void, ApiResult<JSONObject>>() {

            @Override
            protected ApiResult<JSONObject> doInBackground(Object... voids) {
                return coinModel.coinGetcoinbalance();
            }

            @Override
            protected void onPostExecute(ApiResult<JSONObject> result) {
                super.onPostExecute(result);
                if (result != null && result.getData() != null) {
                    getView().onCoinGetcoinbalance(result.getData().optInt("balance"));
                    EventBus.getDefault().post(new EventRefreshCoin());
                }
            }
        });
    }

//    @Override
    public List<CoinRecord> getCoinRecords(int skip, int pageCount) {
        ApiResult<CoinRecordDto> coinRecordList = coinModel.coinRecords(skip, pageCount);
        if (coinRecordList != null && coinRecordList.getData() != null) {
            return coinRecordList.getData().getList();
        }
        return null;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void coinRecords(final int skip, final int pageCount) {
        enqueue(new AsyncTask<Object, Void, ApiResult<CoinRecordDto>>() {

            @Override
            protected ApiResult<CoinRecordDto> doInBackground(Object... voids) {
                return coinModel.coinRecords(skip, pageCount);
            }

            @Override
            protected void onPostExecute(ApiResult<CoinRecordDto> result) {
                super.onPostExecute(result);
                if (result != null && result.getData() != null) {
                    getView().onCoinRecords(result.getData().getList(), result.getData().getCount());
                }
            }
        });
    }
}
