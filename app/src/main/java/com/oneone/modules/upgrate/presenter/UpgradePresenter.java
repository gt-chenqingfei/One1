package com.oneone.modules.upgrate.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.oneone.BasePresenter;
import com.oneone.api.constants.ApiStatus;
import com.oneone.modules.upgrate.contract.UpgradeContract;
import com.oneone.modules.upgrate.dto.UpgrateDTO;
import com.oneone.modules.upgrate.model.UpgradeModel;
import com.oneone.restful.ApiResult;

/**
 * 版本升级Presenter
 *
 * Created by ZhaiDongyang on 2018/6/28
 */
public class UpgradePresenter extends BasePresenter implements UpgradeContract.Presenter {

    private UpgradeModel upgradeModel;

    public UpgradePresenter(Context context) {
        upgradeModel = new UpgradeModel(context);
    }

    @Override
    public void getUpgrateInfo(final String platform, final UpgradeContract.OnUpgrateResultListener listener) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Object, Void, ApiResult<UpgrateDTO>>() {
            @Override
            protected ApiResult<UpgrateDTO> doInBackground(Object[] objects) {
                return upgradeModel.lastversion(platform);
            }

            @Override
            protected void onPostExecute(ApiResult<UpgrateDTO> result) {
                super.onPostExecute(result);
                if (result == null || listener == null) {
                    return;
                }
                if (result.getStatus() == ApiStatus.OK) {
                    listener.onUpgrateResultListener(result.getData());
                }
            }
        };
        task.execute();
    }
}
