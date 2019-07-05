package com.oneone.modules.profile.presenter;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.oneone.BasePresenter;
import com.oneone.api.constants.ApiStatus;
import com.oneone.event.EventNoFeel;
import com.oneone.modules.find.model.LikeModel;
import com.oneone.modules.following.model.FollowingModel;
import com.oneone.modules.profile.contract.ProfileContract;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.modules.user.dto.UserInfoDTO;
import com.oneone.modules.user.model.UserModel;
import com.oneone.restful.ApiResult;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qingfei.chen
 * @version v1.0.0
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 * @since 2018/5/12.
 */
public class ProfilePresenter extends BasePresenter<ProfileContract.View>
        implements ProfileContract.Presenter {
    private UserModel mUserMode;
    private LikeModel mLikeModel;
    private FollowingModel mFollowModel;
    private List<ProfileContract.OnFollowListener> followListeners = new ArrayList<>();
    private List<ProfileContract.OnUnFollowListener> unFollowListeners = new ArrayList<>();
    private UserInfo mUserInfo;

    @Override
    public void onAttachView(ProfileContract.View view) {
        super.onAttachView(view);
        mUserMode = new UserModel(view.getActivityContext());
        mFollowModel = new FollowingModel(view.getActivityContext());
        mLikeModel = new LikeModel(view.getActivityContext());
    }

    @Override
    public void getUserInfo(final String userId) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Object, Object, ApiResult<UserInfoDTO>>() {
            @Override
            protected ApiResult<UserInfoDTO> doInBackground(Object[] objects) {
                return mUserMode.getUserInfo(userId);
            }

            @Override
            protected void onPostExecute(ApiResult<UserInfoDTO> result) {
                super.onPostExecute(result);
                if (result == null) {
                    return;
                }

                if (result.getStatus() == ApiStatus.OK) {
                    UserInfoDTO dto = result.getData();
                    UserInfo userInfo = dto.getUserInfo();
                    userInfo.setFollow(dto.getFollow());
                    if (dto.getRelationInfo() != null) {
                        userInfo.setLikeStatus(dto.getRelationInfo().getLikeStatus());
                        userInfo.setNoFeelStatus(dto.getRelationInfo().getNoFeelStatus());
                    }
                    userInfo.setIntersectionValue(dto.getIntersectionValue());
                    userInfo.setMatchValue(dto.getMatchValue());
                    userInfo.setQaAnswer(dto.getQaAnswer());
                    mUserInfo = userInfo;
                    getView().onUserInfoGet(userInfo);
                }
            }
        };
        enqueue(task);
    }

    @Override
    public void addFollowAndUnFollowListener(ProfileContract.OnFollowListener followListener, ProfileContract.OnUnFollowListener unFollowListener) {
        unFollowListeners.add(unFollowListener);
        followListeners.add(followListener);
    }

    @Override
    public void follow(final String userId) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Object, Object, ApiResult<Integer>>() {
            @Override
            protected ApiResult<Integer> doInBackground(Object[] objects) {
                return mFollowModel.follow(userId);
            }

            @Override
            protected void onPostExecute(ApiResult<Integer> result) {
                super.onPostExecute(result);
                if (result == null) {
                    return;
                }

                if (result.getStatus() != ApiStatus.OK) {
                    getView().showError(result.getMessage());
                    return;
                }

                if (mUserInfo != null) {
                    mUserInfo.setFollow(result.getData());
                }
                for (ProfileContract.OnFollowListener listener : followListeners) {
                    if (listener != null) {
                        listener.onFollow(result.getData());
                    }
                }
            }
        };
        enqueue(task);
    }

    @Override
    public void unFollow(final String userId) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Object, Object, ApiResult<Integer>>() {
            @Override
            protected ApiResult<Integer> doInBackground(Object[] objects) {
                return mFollowModel.unFollow(userId);
            }

            @Override
            protected void onPostExecute(ApiResult<Integer> result) {
                super.onPostExecute(result);
                if (result == null) {
                    return;
                }

                if (result.getStatus() != ApiStatus.OK) {
                    if (TextUtils.isEmpty(result.getMessage())) {
                        return;
                    } else {
                        getView().showError(result.getMessage());
                    }
                    return;
                }
                if (mUserInfo != null) {
                    mUserInfo.setFollow(result.getData());
                }
                for (ProfileContract.OnUnFollowListener listener : unFollowListeners) {
                    if (listener != null) {
                        listener.onUnFollow(result.getData());
                    }
                }
            }
        };
        enqueue(task);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void like(final String userId, final ProfileContract.OnLikeListener listener) {
        enqueue(new AsyncTask<Object, Void, ApiResult>() {
            @Override
            protected ApiResult doInBackground(Object... voids) {
                return mLikeModel.likeSetLike(userId);
            }

            @Override
            protected void onPostExecute(ApiResult result) {
                super.onPostExecute(result);
                if (listener != null) {
                    listener.onLike(result.getStatus() == ApiStatus.OK);
                }
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void likeCancel(final String userId, final ProfileContract.OnLikeCancelListener listener) {
        enqueue(new AsyncTask<Object, Void, ApiResult>() {
            @Override
            protected ApiResult doInBackground(Object... voids) {
                return mLikeModel.likeCancelLike(userId);
            }

            @Override
            protected void onPostExecute(ApiResult result) {
                super.onPostExecute(result);
                if (listener != null) {
                    listener.onLikeCancel(result.getStatus() == ApiStatus.OK);
                }
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void noFeel(final String userId, final ProfileContract.OnNoFeelListener listener) {
        enqueue(new AsyncTask<Object, Void, ApiResult>() {

            @Override
            protected ApiResult doInBackground(Object... voids) {
                return mLikeModel.likeSetNoFeel(userId);
            }

            @Override
            protected void onPostExecute(ApiResult result) {
                super.onPostExecute(result);
                if (listener != null) {
                    EventBus.getDefault().post(new EventNoFeel());
                    listener.onNoFeel(result.getStatus() == ApiStatus.OK);
                }
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void noFeelCancel(final String userId, final ProfileContract.OnNoFeelCancelListener listener) {
        enqueue(new AsyncTask<Object, Void, ApiResult>() {

            @Override
            protected ApiResult doInBackground(Object... voids) {
                return mLikeModel.likeCancelNoFeel(userId);
            }

            @Override
            protected void onPostExecute(ApiResult result) {
                super.onPostExecute(result);
                if (listener != null) {
                    listener.onNoFeelCancel(result.getStatus() == ApiStatus.OK);
                }
            }
        });
    }

    public UserInfo getUserInfo() {
        return mUserInfo;
    }
}
