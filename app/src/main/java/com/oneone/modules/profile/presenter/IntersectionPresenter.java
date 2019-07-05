package com.oneone.modules.profile.presenter;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.oneone.BasePresenter;
import com.oneone.api.constants.ApiStatus;
import com.oneone.modules.profile.bean.IntersectionInfo;
import com.oneone.modules.profile.contract.IntersectionContract;
import com.oneone.modules.profile.model.IntersectionModel;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.modules.user.dto.UserInfoDTO;
import com.oneone.modules.user.model.UserModel;
import com.oneone.restful.ApiResult;

public class IntersectionPresenter extends BasePresenter<IntersectionContract.View> implements IntersectionContract.Presenter {

    private IntersectionModel intersectionModel;
    private UserModel mUserMode;

    @Override
    public void onAttachView(IntersectionContract.View view) {
        super.onAttachView(view);
        intersectionModel = new IntersectionModel(getView().getActivityContext());
        mUserMode = new UserModel(view.getActivityContext());
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void getUserIntersectionInfo(final String targetUserId, final IntersectionContract.IntersectionListener listener) {
        AsyncTask task = new AsyncTask<Object, Void, ApiResult<IntersectionInfo>>() {

            @Override
            protected ApiResult<IntersectionInfo> doInBackground(Object... voids) {
                return intersectionModel.getUserntersectionInfo(targetUserId);
            }

            @Override
            protected void onPostExecute(ApiResult<IntersectionInfo> result) {
                super.onPostExecute(result);
                IntersectionInfo intersectionInfo = null;
                if (result != null && result.getData() != null) {
                    intersectionInfo = result.getData();
                }
                listener.onIntersectionListener(intersectionInfo);
            }
        };
        enqueue(task);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void getUserInfo(final String userId, final IntersectionContract.IntersectionUserInfoListener listener) {
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
                    listener.onIntersectionUserInfoListener(userInfo);
                }
            }
        };
        enqueue(task);
    }

}
