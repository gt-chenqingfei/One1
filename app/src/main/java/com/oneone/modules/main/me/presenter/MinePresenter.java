package com.oneone.modules.main.me.presenter;


import android.os.AsyncTask;

import com.oneone.BasePresenter;
import com.oneone.modules.dogfood.model.CoinModel;
import com.oneone.modules.main.me.contract.MineContract;
import com.oneone.modules.qa.QaDataManager;
import com.oneone.modules.task.dto.LoginReceiveAwardDTO;
import com.oneone.modules.task.model.TaskModel;
import com.oneone.modules.user.UserSP;
import com.oneone.modules.user.bean.UserStatisticInfo;
import com.oneone.modules.user.model.UserModel;
import com.oneone.restful.ApiResult;

import org.json.JSONObject;

/**
 * @author qingfei.chen
 * @since 2018/3/31.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class MinePresenter extends BasePresenter<MineContract.View> implements
        MineContract.Presenter {
    UserModel manager;
    private TaskModel taskModel;

    @Override
    public void onAttachView(MineContract.View view) {
        super.onAttachView(view);
        manager = new UserModel(getView().getActivityContext());
        taskModel = new TaskModel(view.getActivityContext());
    }

    @Override
    public void fetchQAData() {

        enqueue(new AsyncTask<Object, Void, Object>() {

            @Override
            protected Object doInBackground(Object... voids) {
                QaDataManager manager = QaDataManager.getInstance(getView().getActivityContext());
                manager.fetchQAUnAnswerMust();
                manager.fetchQACountInfo();
                return null;
            }

            @Override
            protected void onPostExecute(Object result) {
                super.onPostExecute(result);

            }
        });
    }

    @Override
    public void getCoinBalance() {
        AsyncTask task = new AsyncTask<Object, Void, ApiResult<JSONObject>>() {

            @Override
            protected ApiResult<JSONObject> doInBackground(Object... voids) {
                CoinModel coinModel = new CoinModel(getView().getActivityContext());
                return coinModel.coinGetcoinbalance();
            }

            @Override
            protected void onPostExecute(ApiResult<JSONObject> result) {
                super.onPostExecute(result);

                int balance = 0;
                if (result != null && result.getData() != null) {
                    balance = result.getData().optInt("balance");
                    UserSP.putAndApply(getView().getActivityContext(),  UserSP.DOG_FOOT_BALANCE, balance);
                }
            }
        };
        enqueue(task);
    }

    @Override
    public void getStatisticUserQueryInfo(final MineContract.CoinBalanceListener listener) {
        enqueue(new AsyncTask<Object, Void, ApiResult<UserStatisticInfo>>() {

            @Override
            protected ApiResult<UserStatisticInfo> doInBackground(Object... voids) {
                UserModel userModel = new UserModel(getView().getActivityContext());
                return userModel.getStatisticUserQueryInfo();
            }

            @Override
            protected void onPostExecute(ApiResult<UserStatisticInfo> result) {
                super.onPostExecute(result);
                UserStatisticInfo userStatisticInfo = null;
                if (result != null && result.getData() != null) {
                    userStatisticInfo = result.getData();
                }
                listener.onStatisticUserQuery(userStatisticInfo);
            }
        });
    }

    @Override
    public void taskLoginReceiveAward(final MineContract.CoinBalanceListener listener) {
        enqueue(new AsyncTask<Object, Void, ApiResult<LoginReceiveAwardDTO>>() {

            @Override
            protected ApiResult<LoginReceiveAwardDTO> doInBackground(Object... voids) {
                return taskModel.taskLoginReceiveAward();
            }
            @Override
            protected void onPostExecute(ApiResult<LoginReceiveAwardDTO> result) {
                super.onPostExecute(result);
                if (result != null && result.getData() != null) {
                    listener.onTaskLoginReceiveAward(result.getData().getTaskAward(), result.getData().getReceived());
                }
            }
        });
    }
}
