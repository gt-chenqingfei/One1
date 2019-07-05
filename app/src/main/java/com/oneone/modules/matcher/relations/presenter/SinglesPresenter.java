package com.oneone.modules.matcher.relations.presenter;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.oneone.BasePresenter;
import com.oneone.api.constants.ApiStatus;
import com.oneone.modules.entry.beans.LoginInfo;
import com.oneone.modules.entry.model.AccountModel;
import com.oneone.modules.matcher.relations.contract.SinglesContract;
import com.oneone.modules.matcher.relations.dto.MyMatchersDto;
import com.oneone.modules.matcher.relations.dto.MySinglesDto;
import com.oneone.modules.matcher.relations.model.SinglesModel;
import com.oneone.modules.user.HereUser;
import com.oneone.restful.ApiResult;

import java.util.Arrays;
import java.util.List;

/**
 * @author qingfei.chen
 * @since 2018/4/20.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class SinglesPresenter extends BasePresenter<SinglesContract.View>
        implements SinglesContract.Presenter {
    private SinglesModel model;
    private static final int PAGE_COUNT = 10;
    private int skip = 0;

    @Override
    public void onAttachView(SinglesContract.View view) {
        super.onAttachView(view);
        model = new SinglesModel(view.getActivityContext());
    }


    @Override
    public void getMySingles(final boolean isLoadMore, final SinglesContract.OnMySinglesGetListener listener) {
        if (isLoadMore) {
            skip += PAGE_COUNT;
        } else {
            skip = 0;
        }
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Object, Object, ApiResult<MySinglesDto>>() {

            @Override
            protected ApiResult<MySinglesDto> doInBackground(Object... objects) {
                return model.getMySingles(skip, PAGE_COUNT);
            }

            @Override
            protected void onPostExecute(ApiResult<MySinglesDto> result) {
                super.onPostExecute(result);
                if (result.getStatus() != ApiStatus.OK) {
                    showError(result.getMessage());
                    return;
                }
                if (result.getData() == null) {
                    return;
                }
                MySinglesDto dto = result.getData();
                if (dto == null) {
                    return;
                }
                listener.onMySinglesGet(isLoadMore, dto.getList(), dto.getCount());
            }
        };

        enqueue(task);
    }

    @Override
    public void matcherSaid(final String userId, final String matcherSaid, final SinglesContract.OnMatcherSaidListener listener) {

        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Object, Object, ApiResult>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                getView().loading("");
            }

            @Override
            protected ApiResult<MySinglesDto> doInBackground(Object... objects) {
                return model.matcherSaid(userId, matcherSaid);
            }

            @Override
            protected void onPostExecute(ApiResult result) {
                super.onPostExecute(result);
                getView().loadingDismiss();
                if (result.getStatus() != ApiStatus.OK) {
                    showError(result.getMessage());
                    return;
                }
               listener.onMatcherSaid();
            }
        };

        enqueue(task);
    }

    @Override
    public void matcherRelation(final String userId, final String matcherRelation,
                                final SinglesContract.OnMatcherRelationListener listener) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Object, Object, ApiResult>() {

            @Override
            protected ApiResult<MySinglesDto> doInBackground(Object... objects) {
                return model.relationShip(userId, matcherRelation);
            }

            @Override
            protected void onPostExecute(ApiResult result) {
                super.onPostExecute(result);
                if (result.getStatus() != ApiStatus.OK) {
                    showError(result.getMessage());
                    return;
                }
               listener.onMatcherRelation();
            }
        };

        enqueue(task);
    }

    @Override
    public void getMatcherRelationTag(String userId, final SinglesContract.OnGetMatherRelationTagsListener listener) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Object, Object, ApiResult>() {

            @Override
            protected ApiResult<MySinglesDto> doInBackground(Object... objects) {
                return null;
            }

            @Override
            protected void onPostExecute(ApiResult result) {
                super.onPostExecute(result);
                String map[] = new String[]{"发小", "闺蜜", "哥们", "好友", "同学", "同事", "亲戚", "老乡"};
                List<String> mRelations = Arrays.asList(map);
                listener.onMatherRelationTags(mRelations);
            }
        };

        enqueue(task);
    }

    @Override
    public void getMyMatcher() {

        @SuppressLint("StaticFieldLeak")
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
            }
        };

        enqueue(task);
    }

    @Override
    public void bindWeChat(final String platform, final String platformId, final SinglesContract.OnBindWechatListener listener) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Object, Object, ApiResult>() {


            @Override
            protected ApiResult doInBackground(Object... objects) {
                return new AccountModel(getView().getActivityContext()).bindThird(HereUser.getUserId(), platform, platformId);
            }

            @Override
            protected void onPostExecute(ApiResult result) {
                super.onPostExecute(result);

                if (result == null) {
                    return;
                }

                switch (result.getStatus()) {
                    case ApiStatus.USER_NOT_EXIST:
                    case ApiStatus.COUNTRY_CODE_ERROR:
                    case ApiStatus.VALID_CODE_ERROR:
                        getView().showError(result.getMessage());
                        return;
                }
                LoginInfo info = HereUser.getInstance().getLoginInfo();
                info.setAlreadyBindWechat(true);
                HereUser.getInstance().updateUserInfo(info);
                if (listener != null) {
                    listener.onBindWeChat();
                }
            }
        };

        enqueue(task);
    }
}
