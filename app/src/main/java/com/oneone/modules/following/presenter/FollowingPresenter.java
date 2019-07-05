package com.oneone.modules.following.presenter;

import android.os.AsyncTask;

import com.oneone.BasePresenter;
import com.oneone.modules.following.contract.FollowingContract;
import com.oneone.modules.following.dto.FollowListDto;
import com.oneone.modules.following.model.FollowingModel;
import com.oneone.restful.ApiResult;

/**
 * Created by here on 18/4/26.
 */

public class FollowingPresenter extends BasePresenter<FollowingContract.View> implements FollowingContract.Presenter {
    private FollowingModel followingModel;

    @Override
    public void onAttachView(FollowingContract.View view) {
        super.onAttachView(view);

        followingModel = new FollowingModel(view.getActivityContext());
    }

    @Override
    public void followingFollow(final String userId) {
        enqueue(new AsyncTask<Object, Void, ApiResult<Integer>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                getView().loading("");
            }

            @Override
            protected ApiResult<Integer> doInBackground(Object... voids) {
                return followingModel.follow(userId);
            }

            @Override
            protected void onPostExecute(ApiResult<Integer> result) {
                super.onPostExecute(result);
                if (result != null && result.getData() != null) {
                    getView().onFollowingFollow(result.getData());
                }
                getView().loadingDismiss();
            }
        });
    }

    @Override
    public void followingUnfollow(final String userId) {
        enqueue(new AsyncTask<Object, Void, ApiResult<Integer>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                getView().loading("");
            }

            @Override
            protected ApiResult<Integer> doInBackground(Object... voids) {
                return followingModel.unFollow(userId);
            }

            @Override
            protected void onPostExecute(ApiResult<Integer> result) {
                super.onPostExecute(result);
                if (result != null && result.getData() != null) {
                    getView().onFollowingUnfollow(result.getData());
                }
                getView().loadingDismiss();
            }
        });
    }

    @Override
    public void followingFollowers(final boolean isLoadMore, final int skip, final int pageCount) {
        enqueue(new AsyncTask<Object, Void, ApiResult<FollowListDto>>() {

            @Override
            protected ApiResult<FollowListDto> doInBackground(Object... voids) {
                return followingModel.getFollowers(skip, pageCount);
            }

            @Override
            protected void onPostExecute(ApiResult<FollowListDto> result) {
                super.onPostExecute(result);
                if (result != null) {
                    if (result.getData() == null) {
                        getView().onFollowingFollowers(isLoadMore, result.getData().getCount(), null);
                    } else {
                        getView().onFollowingFollowers(isLoadMore, result.getData().getCount(), result.getData().getList());
                    }
                }
            }
        });
    }

    @Override
    public void followingFollowings(final boolean isLoadMore, final int skip, final int pageCount) {
        enqueue(new AsyncTask<Object, Void, ApiResult<FollowListDto>>() {

            @Override
            protected ApiResult<FollowListDto> doInBackground(Object... voids) {
                return followingModel.getFollowings(skip, pageCount);
            }

            @Override
            protected void onPostExecute(ApiResult<FollowListDto> result) {
                super.onPostExecute(result);
                if (result != null) {
                    if (result.getData() == null) {
                        getView().onFollowingFollowings(isLoadMore, 0, null);
                    } else {
                        getView().onFollowingFollowings(isLoadMore, result.getData().getCount(), result.getData().getList());
                    }
                }
            }
        });
    }
}
