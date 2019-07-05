package com.oneone.modules.main.me.presenter;

import android.os.AsyncTask;

import com.oneone.BasePresenter;
import com.oneone.api.constants.ApiStatus;
import com.oneone.modules.main.me.contract.MyMatcherContract;
import com.oneone.modules.matcher.relations.dto.MyMatchersDto;
import com.oneone.modules.matcher.relations.model.SinglesModel;
import com.oneone.restful.ApiResult;

/**
 * @author qingfei.chen
 * @since 2018/4/20.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class MyMatcherPresenter extends BasePresenter<MyMatcherContract.View>
        implements MyMatcherContract.Presenter {
    private SinglesModel model;
    private static final int PAGE_COUNT = 10;
    private int skip = 0;

    @Override
    public void onAttachView(MyMatcherContract.View view) {
        super.onAttachView(view);
        model = new SinglesModel(view.getActivityContext());
    }

    @Override
    public void getMyMatcher() {

        AsyncTask task = new AsyncTask<Object, Object, ApiResult<MyMatchersDto>>() {

            @Override
            protected ApiResult<MyMatchersDto> doInBackground(Object... objects) {
                return model.getMyMatcher();
            }

            @Override
            protected void onPostExecute(ApiResult<MyMatchersDto> result) {
                super.onPostExecute(result);
                if (result.getStatus() != ApiStatus.OK) {
                    showError(result.getMessage());
                    return;
                }
                if (result.getData() == null) {
                    return;
                }
                MyMatchersDto dto = result.getData();
                if (dto == null) {
                    return;
                }

                getView().onMyMatcherGet(dto.getList());
            }
        };

        enqueue(task);
    }

}
