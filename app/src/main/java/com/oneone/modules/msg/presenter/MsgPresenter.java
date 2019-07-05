package com.oneone.modules.msg.presenter;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.oneone.BasePresenter;
import com.oneone.api.constants.ApiStatus;
import com.oneone.modules.find.dto.LikeListDto;
import com.oneone.modules.find.dto.LikeUnreadListDto;
import com.oneone.modules.find.model.LikeModel;
import com.oneone.modules.following.model.FollowingModel;
import com.oneone.modules.msg.beans.IMEmoji;
import com.oneone.modules.msg.beans.IMUser;
import com.oneone.modules.msg.beans.IMUserPrerelation;
import com.oneone.modules.msg.beans.MsgMeta;
import com.oneone.modules.msg.beans.TalkBeans.MyMessage;
import com.oneone.modules.msg.contract.MsgContract;
import com.oneone.modules.msg.dto.GiftProdDto;
import com.oneone.modules.msg.dto.IMRelationListDto;
import com.oneone.modules.msg.dto.MsgMetaDto;
import com.oneone.modules.msg.dto.NotifyListDto;
import com.oneone.modules.msg.models.IMModel;
import com.oneone.modules.profile.contract.ProfileContract;
import com.oneone.restful.ApiResult;
import com.oneone.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by here on 18/5/3.
 */

public class MsgPresenter extends BasePresenter<MsgContract.View> implements MsgContract.Presenter {
    public static final int DOG_FOOD_NOT_ENOUGH = 201003;

    public static final int FIRST_RELATION_LIST_TYPE_TALK_PAGE = 1;
    public static final int FIRST_RELATION_LIST_TYPE_SEE_ALL = 2;
    private IMModel imModel;
    private LikeModel likeModel;
    private FollowingModel mFollowModel;

    @Override
    public void onAttachView(MsgContract.View view) {
        super.onAttachView(view);

        imModel = new IMModel(view.getActivityContext());
        likeModel = new LikeModel(view.getActivityContext());
        mFollowModel = new FollowingModel(view.getActivityContext());
    }

    @Override
    public void findRecommend() {

    }

    @Override
    public void findSetCondition(int character, String cityCode) {

    }

    @Override
    public void findGetCondition() {

    }

    @Override
    public void imUserGettoken() {
        enqueue(new AsyncTask<Object, Void, ApiResult<IMUser>>() {

            @Override
            protected ApiResult<IMUser> doInBackground(Object... voids) {
                return imModel.imUserGettoken();
            }
            @Override
            protected void onPostExecute(ApiResult<IMUser> result) {
                super.onPostExecute(result);
                if (result != null && result.getData() != null) {
                    getView().onImUserGettoken(result.getData());
                }
            }
        });
    }

    @Override
    public void imRefreshToken() {
        enqueue(new AsyncTask<Object, Void, ApiResult<IMUser>>() {

            @Override
            protected ApiResult<IMUser> doInBackground(Object... voids) {
                return imModel.imRefreshToken();
            }
            @Override
            protected void onPostExecute(ApiResult<IMUser> result) {
                super.onPostExecute(result);
                if (result != null && result.getData() != null) {
                    getView().onImRefreshToken(result.getData());
                }
            }
        });
    }

    @Override
    public void imUserIsrelation(final String toUid) {
        enqueue(new AsyncTask<Object, Void, ApiResult<Boolean>>() {

            @Override
            protected ApiResult<Boolean> doInBackground(Object... voids) {
                return imModel.imUserIsrelation(toUid);
            }
            @Override
            protected void onPostExecute(ApiResult<Boolean> result) {
                super.onPostExecute(result);
                if (result != null && result.getData() != null) {
                    getView().onImUserIsrelation(result.getData());
                }
            }
        });
    }

    @Override
    public void imUserPrerelation(final String toUid) {
        enqueue(new AsyncTask<Object, Void, ApiResult<IMUserPrerelation>>() {

            @Override
            protected ApiResult<IMUserPrerelation> doInBackground(Object... voids) {
                return imModel.imUserPrerelation(toUid);
            }
            @Override
            protected void onPostExecute(ApiResult<IMUserPrerelation> result) {
                super.onPostExecute(result);
                if (result != null && result.getData() != null) {
                    getView().onImUserPrerelation(result.getData());
                }
            }
        });
    }

    @Override
    public void imUserApply(final String toUid, final String prodCode) {
        enqueue(new AsyncTask<Object, Void, ApiResult<JSONObject>>() {

            @Override
            protected ApiResult<JSONObject> doInBackground(Object... voids) {
                return imModel.imUserApply(toUid, prodCode);
            }
            @Override
            protected void onPostExecute(ApiResult<JSONObject> result) {
                super.onPostExecute(result);
                if (result != null) {
                    if (result.getData() != null) {
                        try {
                            getView().onImUserApply(result.getStatus(), result.getData().getString("toImUserId"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (result.getStatus() == DOG_FOOD_NOT_ENOUGH) {
                        getView().onImUserApply(result.getStatus(), "");
//                        ToastUtil.show(getView().getActivityContext(), result.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public void imUserMsgreply(final String targetImUid) {
        enqueue(new AsyncTask<Object, Void, ApiResult<String>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                getView().loading("");
            }
            @Override
            protected ApiResult<String> doInBackground(Object... voids) {
                return imModel.imUserMsgreply(targetImUid);
            }
            @Override
            protected void onPostExecute(ApiResult<String> result) {
                super.onPostExecute(result);
                getView().loadingDismiss();
                if (result != null && result.getData() != null) {
                    getView().onImUserMsgreply(result);
                }
            }
        });
    }

    @Override
    public void imUserDelrealation(final String targetImUid) {
        enqueue(new AsyncTask<Object, Void, ApiResult<String>>() {

            @Override
            protected ApiResult<String> doInBackground(Object... voids) {
                return imModel.imUserDelrealation(targetImUid);
            }
            @Override
            protected void onPostExecute(ApiResult<String> result) {
                super.onPostExecute(result);
                if (result != null && result.getData() != null) {
                    getView().onImUserDelrealation(result);
                }
            }
        });
    }

    @Override
    public void imUserFirstRelationList(final boolean isLoadMore, final int queryType, final int skip, final int pageCount) {
        enqueue(new AsyncTask<Object, Void, ApiResult<IMRelationListDto>>() {
            @Override
            protected ApiResult<IMRelationListDto> doInBackground(Object... voids) {
                return imModel.imUserFirstRelationList(queryType, skip, pageCount);
            }
            @Override
            protected void onPostExecute(ApiResult<IMRelationListDto> result) {
                super.onPostExecute(result);
                if (result != null && result.getData() != null) {
                    getView().onImUserFirstRelationList(isLoadMore, result.getData().getOldCount(), result.getData().getNewCount(), result.getData().getCount(), result.getData().getList());
                }
            }
        });
    }

    @Override
    public void imUserRelationList(final boolean isShowLoading, final String targetImUserIds) {
        enqueue(new AsyncTask<Object, Void, ApiResult<IMRelationListDto>>() {

            @Override
            protected ApiResult<IMRelationListDto> doInBackground(Object... voids) {
                return imModel.imUserRelationList(targetImUserIds);
            }
            @Override
            protected void onPostExecute(ApiResult<IMRelationListDto> result) {
                super.onPostExecute(result);
                if (result != null && result.getData() != null) {
                    getView().onImUserRelationList(result.getData().getCount(), result.getData().getList());
                }
            }
        });
    }

    @Override
    public void imMsgCheck(final String toImUid, final String msgType, final String message, final MyMessage myMessage) {
        enqueue(new AsyncTask<Object, Void, ApiResult<String>>() {

            @Override
            protected ApiResult<String> doInBackground(Object... voids) {
                return imModel.imMsgCheck(toImUid, msgType, message);
            }
            @Override
            protected void onPostExecute(ApiResult<String> result) {
                super.onPostExecute(result);
                getView().onImMsgCheck(result, myMessage);
            }
        });
    }

    @Override
    public void imMsgListEmoji() {
        enqueue(new AsyncTask<Object, Void, ApiResult<List<IMEmoji>>>() {

            @Override
            protected ApiResult<List<IMEmoji>> doInBackground(Object... voids) {
                return imModel.imMsgListEmoji();
            }
            @Override
            protected void onPostExecute(ApiResult<List<IMEmoji>> result) {
                super.onPostExecute(result);
                if (result != null && result.getData() != null) {
                    getView().onImMsgListEmoji(result.getData());
                }
            }
        });
    }

    @Override
    public void prodGiftList(final String prodType) {
        enqueue(new AsyncTask<Object, Void, ApiResult<GiftProdDto>>() {

            @Override
            protected ApiResult<GiftProdDto> doInBackground(Object... voids) {
                return imModel.prodGiftList(prodType);
            }
            @Override
            protected void onPostExecute(ApiResult<GiftProdDto> result) {
                super.onPostExecute(result);
                if (result != null && result.getData() != null) {
                    getView().onProdGiftList(result.getData().getCount(), result.getData().getList());
                }
            }
        });
    }

    @Override
    public void msgNoteListmeta(final boolean isLoadMore, final String maxTimestamp, final String limit) {
        enqueue(new AsyncTask<Object, Void, ApiResult<List<MsgMeta>>>() {

            @Override
            protected ApiResult<List<MsgMeta>> doInBackground(Object... voids) {
                return imModel.msgNoteListmeta(maxTimestamp, limit);
            }
            @Override
            protected void onPostExecute(ApiResult<List<MsgMeta>> result) {
                super.onPostExecute(result);
                if (result != null) {
                    getView().onMsgNoteListmeta(isLoadMore, result.getData());
                }
            }
        });
    }

    @Override
    public void msgNoteLastmeta() {
        enqueue(new AsyncTask<Object, Void, ApiResult<MsgMetaDto>>() {

            @Override
            protected ApiResult<MsgMetaDto> doInBackground(Object... voids) {
                return imModel.msgNoteLastmeta();
            }
            @Override
            protected void onPostExecute(ApiResult<MsgMetaDto> result) {
                super.onPostExecute(result);
                if (result != null && result.getData() != null) {
                    getView().onMsgNoteLastmeta(result.getData());
                }
            }
        });
    }

    public void myLikeList (final boolean isLoadMore, final int skip, final int pageCount, final long preReadTime) {
        enqueue(new AsyncTask<Object, Void, ApiResult<LikeListDto>>() {

            @Override
            protected ApiResult<LikeListDto> doInBackground(Object... voids) {
                return likeModel.likeFromme(skip, pageCount, preReadTime);
            }
            @Override
            protected void onPostExecute(ApiResult<LikeListDto> result) {
                super.onPostExecute(result);
                if (result != null) {
                    getView().onMyLikeList(isLoadMore, result.getData());
                }
            }
        });
    }

    public void likeMeList (final boolean isLoadMore, final int skip, final int pageCount, final long preReadTime) {
        enqueue(new AsyncTask<Object, Void, ApiResult<LikeListDto>>() {
            @Override
            protected ApiResult<LikeListDto> doInBackground(Object... voids) {
                return likeModel.likeTome(skip, pageCount, preReadTime);
            }
            @Override
            protected void onPostExecute(ApiResult<LikeListDto> result) {
                super.onPostExecute(result);
                if (result != null) {
                    getView().onLikeMeList(isLoadMore, result.getData());
                }
            }
        });
    }

    public void likeEachotherList(final boolean isLoadMore, final int skip, final int pageCount, final long preReadTime) {
        enqueue(new AsyncTask<Object, Void, ApiResult<LikeListDto>>() {

            @Override
            protected ApiResult<LikeListDto> doInBackground(Object... voids) {
                return likeModel.likeEachother(skip, pageCount, preReadTime);
            }
            @Override
            protected void onPostExecute(ApiResult<LikeListDto> result) {
                super.onPostExecute(result);
                if (result != null) {
                    getView().onLikeEachother(isLoadMore, result.getData());
                }
            }
        });
    }

    public void likeUnread (final boolean isShowLoading) {
        enqueue(new AsyncTask<Object, Void, ApiResult<LikeUnreadListDto>>() {

            @Override
            protected ApiResult<LikeUnreadListDto> doInBackground(Object... voids) {
                return likeModel.likeUnread();
            }
            @Override
            protected void onPostExecute(ApiResult<LikeUnreadListDto> result) {
                super.onPostExecute(result);
                if (result != null) {
                    getView().onLikeUnread(result.getData());
                }
            }
        });
    }

    public void getNotifyList (final boolean isRefresh, final long lastNotifyTime, final int pageCount, final long lastReadTime) {
        enqueue(new AsyncTask<Object, Void, ApiResult<NotifyListDto>>() {

            @Override
            protected ApiResult<NotifyListDto> doInBackground(Object... voids) {
                return imModel.getNotifyList(lastNotifyTime, pageCount, lastReadTime);
            }
            @Override
            protected void onPostExecute(ApiResult<NotifyListDto> result) {
                super.onPostExecute(result);
                if (result != null && result.getStatus() == 0) {
                    NotifyListDto notifyListDto = result.getData();
                    if (notifyListDto != null)
                        getView().onGetNotifyList(isRefresh, notifyListDto.getCount(), notifyListDto.getTimelineList(), notifyListDto.getList(), notifyListDto.getLastReadTime());
                    else
                        getView().onGetNotifyList(isRefresh, -1, null, null, 0);
                } else {
                    getView().onGetNotifyList(isRefresh, -1, null, null, 0);
                }
            }
        });
    }

    @Override
    public void follow(final String userId, final ProfileContract.OnFollowListener listener) {
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

                if (listener != null) {
                    listener.onFollow(result.getData());
                }
            }
        };
        enqueue(task);
    }

    @Override
    public void unFollow(final String userId, final ProfileContract.OnUnFollowListener listener) {
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
                    getView().showError(result.getMessage());
                    return;
                }

                if (listener != null) {
                    listener.onUnFollow(result.getData());
                }
            }
        };
        enqueue(task);
    }
}
