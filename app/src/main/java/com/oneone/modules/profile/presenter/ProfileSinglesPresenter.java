package com.oneone.modules.profile.presenter;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.oneone.BasePresenter;
import com.oneone.api.constants.ApiStatus;
import com.oneone.modules.matcher.relations.bean.SingleInfo;
import com.oneone.modules.matcher.relations.dto.MySinglesDto;
import com.oneone.modules.matcher.relations.model.SinglesModel;
import com.oneone.modules.profile.contract.ProfileSinglesContract;
import com.oneone.modules.timeline.bean.TimeLine;
import com.oneone.restful.ApiResult;

import java.util.List;

/**
 * @author qingfei.chen
 * @since 2018/5/14.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class ProfileSinglesPresenter extends BasePresenter<ProfileSinglesContract.View> implements ProfileSinglesContract.Presenter {
    private SinglesModel mSinglesModel;
    private static final int PAGE_COUNT = 10;
    private int skip = 0;

    @Override
    public void onAttachView(ProfileSinglesContract.View view) {
        super.onAttachView(view);
        mSinglesModel = new SinglesModel(view.getActivityContext());
    }

    @Override
    public void getSingles(final boolean isLoadMore, final String userId) {
        if (isLoadMore) {
            skip += PAGE_COUNT;
        } else {
            skip = 0;
        }
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Object, Object, ApiResult<MySinglesDto>>() {

            @Override
            protected ApiResult<MySinglesDto> doInBackground(Object... objects) {
                return mSinglesModel.getOtherSingles(userId, skip, PAGE_COUNT);
            }

            @Override
            protected void onPostExecute(ApiResult<MySinglesDto> result) {
                super.onPostExecute(result);
                if (result == null) {
                    return;
                }
                if (result.getStatus() == ApiStatus.OK) {
                    List<SingleInfo> singleInfos = result.getData().getList();
                    boolean isLoadEnd = true;
                    if (singleInfos != null && !singleInfos.isEmpty()) {
                        isLoadEnd = singleInfos.size() < skip+PAGE_COUNT;
                    }
                    getView().onGetSingles(result.getData().getList(), isLoadMore,
                            result.getData().getCount(), isLoadEnd);
                    return;
                }
                getView().onGetSingles(null, isLoadMore, 0, true);
            }
        };

        enqueue(task);
    }
}
