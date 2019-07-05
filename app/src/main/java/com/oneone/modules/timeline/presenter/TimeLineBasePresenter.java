package com.oneone.modules.timeline.presenter;


import android.annotation.SuppressLint;
import android.os.AsyncTask;


import com.oneone.BasePresenter;
import com.oneone.HereSingletonFactory;
import com.oneone.api.constants.TimeLineLikeType;
import com.oneone.event.EventTimelineDelete;
import com.oneone.event.EventTimelineLike;
import com.oneone.modules.timeline.NewTimeLineManager;
import com.oneone.modules.timeline.bean.LikeType;
import com.oneone.modules.timeline.bean.TimeLine;
import com.oneone.modules.timeline.contract.TimeLineContract;
import com.oneone.modules.timeline.model.TimeLineModel;
import com.oneone.restful.ApiResult;

import org.greenrobot.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author qingfei.chen
 * @since 2018/3/31.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class TimeLineBasePresenter extends BasePresenter<TimeLineContract.View> implements
        TimeLineContract.Presenter {
    static final int DEFAULT_PAGE_COUNT = 20;
    static Logger logger = LoggerFactory.getLogger("TimeLineBasePresenter");
    protected TimeLineModel manager;
    protected int pageCount = DEFAULT_PAGE_COUNT;
    protected long lastTimelineId;

    @Override
    public void onAttachView(TimeLineContract.View mView) {
        super.onAttachView(mView);
        manager = new TimeLineModel(getView().getActivityContext());
    }

    @Override
    public void like(final TimeLine timeLine, final int likeType, final int position) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Object, Object, ApiResult>() {
            int type;

            @Override
            protected ApiResult doInBackground(Object... objects) {
                type = likeType;
                if (timeLine.getMyLikeType() == likeType) {
                    type = TimeLineLikeType.CANCEL;
                }
                return manager.like(timeLine.getTimelineId(), type);
            }

            @Override
            protected void onPostExecute(ApiResult result) {
                super.onPostExecute(result);

                if (result == null) {
                    return;
                }

                LikeType preLikeType = matchLikeType(timeLine, timeLine.getMyLikeType());

                if (type == TimeLineLikeType.CANCEL) {
                    removeLike(preLikeType);
                    timeLine.setMyLikeType(TimeLineLikeType.NONE);
                } else {

                    if (preLikeType != null) {
                        removeLike(preLikeType);
                        LikeType newLikeType = matchLikeType(timeLine, likeType);
                        if (newLikeType != null) {
                            newLikeType.setLikeTypeCount(newLikeType.getLikeTypeCount() + 1);
                        } else {
                            addLike();
                        }
                    } else {
                        addLike();
                    }
                    timeLine.setMyLikeType(likeType);
                }

                logger.error(timeLine.getLikeTypes().toString());
                EventBus.getDefault().post(new EventTimelineLike(timeLine, position));
            }

            private void addLike() {
                LikeType newLikeType = matchLikeType(timeLine, likeType);
                if (newLikeType == null) {
                    newLikeType = new LikeType(likeType, 1);
                    timeLine.getLikeTypes().add(newLikeType);
                } else {
                    newLikeType.setLikeTypeCount(newLikeType.getLikeTypeCount() + 1);
                }
                logger.error("addLike:" + newLikeType);
            }

            private void removeLike(LikeType likeType) {
                if (likeType == null) {
                    return;
                }
                if (likeType.getLikeTypeCount() <= 0) {
                    timeLine.getLikeTypes().remove(likeType);
                } else {
                    likeType.setLikeTypeCount(likeType.getLikeTypeCount() - 1);
                    if (likeType.getLikeTypeCount() <= 0) {
                        timeLine.getLikeTypes().remove(likeType);
                    }
                }
                logger.error("removeLike:" + likeType);
            }


        };
        enqueue(task);
    }


    @Override
    public void delete(final TimeLine timeLine, final int position) {

        NewTimeLineManager newTimeLineManager = HereSingletonFactory.getInstance().getNewTimeLineManager();
        if (newTimeLineManager.isLocalTimelineThenDelete(timeLine.getTimelineId())) {
            EventBus.getDefault().post(new EventTimelineDelete(timeLine, position));
            return;
        }
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Object, Object, ApiResult>() {

            @Override
            protected ApiResult doInBackground(Object... objects) {
                return manager.delete(timeLine.getTimelineId());
            }

            @Override
            protected void onPostExecute(ApiResult result) {
                super.onPostExecute(result);
                if (result == null) {
                    return;
                }

                EventBus.getDefault().post(new EventTimelineDelete(timeLine, position));
            }
        };
        enqueue(task);
    }

    private LikeType matchLikeType(TimeLine timeLine, int likeType) {
        LikeType ret = null;
        for (LikeType type : timeLine.getLikeTypes()) {
            if (type.getLikeType() == likeType) {
                ret = type;
                break;
            }
        }
        return ret;
    }

}
