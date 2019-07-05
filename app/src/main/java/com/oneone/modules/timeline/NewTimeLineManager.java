package com.oneone.modules.timeline;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.oneone.HereSingletonFactory;
import com.oneone.api.constants.ApiStatus;
import com.oneone.api.constants.TimelineStatus;
import com.oneone.api.request.TimelinePostDto;
import com.oneone.event.EventTimelinePost;
import com.oneone.modules.support.qiniu.PhotoUploadListener;
import com.oneone.modules.support.qiniu.UploadParam;
import com.oneone.modules.timeline.bean.TimeLine;
import com.oneone.modules.timeline.bean.TimeLineCompose;
import com.oneone.modules.timeline.bean.TimeLineDetail;
import com.oneone.modules.timeline.bean.TimeLineImage;
import com.oneone.modules.timeline.contract.TimeLineContract;
import com.oneone.modules.timeline.model.TimeLineModel;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.UserSP;
import com.oneone.restful.ApiResult;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qingfei.chen
 * @version V1.0.0
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 * @since 2018/6/25.
 */
public class NewTimeLineManager implements TimeLineContract.INewTimeLineManager, PhotoUploadListener {
    public static final long PREF_KEY_TIMELINE_IMAGE_TEXT = Long.MAX_VALUE;
    public static final long PREF_KEY_TIMELINE_TEXT = Long.MAX_VALUE - 1;

    public static final int UPLOAD_IMAGE_GROUP_ID = 2000;
    protected TimeLineModel manager;
    private TimeLine timeLine4ImageText;
    private TimeLine timeLine4Text;
    private Context mContext;

    private NewTimeLineManager(Context context) {
        this.mContext = context;
        manager = new TimeLineModel(context);
        restoreTimeLineFormLocal();
    }

    @Override
    public void newTimeLine(String content, List<TimeLineImage> images) {
        if (images != null) {
            newTimeLine4ImageText(content, images);
            uploadImageIfNeed(timeLine4ImageText.getDetail().getDetail().getTimelineImgs());
        } else {
            newTimeLine4Text(content);
            post(timeLine4Text);
        }
        EventBus.getDefault().post(new EventTimelinePost(EventTimelinePost.STATUS_SENDING));
    }

    @Override
    public void reSendTimeLine(TimeLine timeLine) {
        timeLine.setStatus(TimelineStatus.STATUS_SENDING);
        if (timeLine.getTimelineId() == PREF_KEY_TIMELINE_IMAGE_TEXT) {
            timeLine4ImageText = timeLine;
            uploadImageIfNeed(timeLine4ImageText.getDetail().getDetail().getTimelineImgs());
        } else {
            timeLine4Text = timeLine;
            post(timeLine4Text);
        }
    }

    public List<TimeLine> getTimeLineSendWaitList() {
        List<TimeLine> list = new ArrayList<>();
        if (timeLine4ImageText != null) {
            list.add(timeLine4ImageText);
        }
        if (timeLine4Text != null) {
            list.add(timeLine4Text);
        }

        return list;
    }

    public List<TimeLine> getTimeLineUnSendList() {
        List<TimeLine> list = new ArrayList<>();
        if (timeLine4ImageText != null
                && timeLine4ImageText.getStatus() == TimelineStatus.STATUS_SEND_FAILED) {
            list.add(timeLine4ImageText);
        }
        if (timeLine4Text != null
                && timeLine4Text.getStatus() == TimelineStatus.STATUS_SEND_FAILED) {
            list.add(timeLine4Text);
        }

        return list;
    }

    /**
     * 如果 有本地未发送的动态，就删除本地
     *
     * @param timelineId id
     * @return true 当前动态是本地未发送的，删除并返回
     */
    public boolean isLocalTimelineThenDelete(long timelineId) {
        if (timeLine4ImageText != null && timeLine4ImageText.getTimelineId() == timelineId) {
            if (timeLine4ImageText.getStatus() == TimelineStatus.STATUS_SEND_FAILED ||
                    timeLine4ImageText != null && timeLine4ImageText.getStatus() == TimelineStatus.STATUS_SENDING) {
                free(timeLine4ImageText);
                return true;
            }
        }
        if (timeLine4Text != null && timeLine4Text.getTimelineId() == timelineId ||
                timeLine4ImageText != null && timeLine4ImageText.getStatus() == TimelineStatus.STATUS_SENDING) {
            if (timeLine4Text.getStatus() == TimelineStatus.STATUS_SEND_FAILED) {
                free(timeLine4Text);
            }
            return true;
        }
        return false;
    }

    public TimeLine getTimeLine4ImageText() {
        return timeLine4ImageText;
    }

    public TimeLine getTimeLine4Text() {
        return timeLine4Text;
    }


    @Override
    public boolean isSendingTimeLine4ImageText() {
        if (timeLine4ImageText != null &&
                timeLine4ImageText.getStatus() == TimelineStatus.STATUS_SENDING) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isSendingTimeLine4Text() {
        if (timeLine4Text != null &&
                timeLine4Text.getStatus() == TimelineStatus.STATUS_SENDING) {
            return true;
        }
        return false;
    }

    private TimeLine newTimeLine4ImageText(String content, List<TimeLineImage> images) {
        if (timeLine4ImageText != null) {
            return timeLine4ImageText;
        }
        timeLine4ImageText = newTimeLine(PREF_KEY_TIMELINE_IMAGE_TEXT);
        timeLine4ImageText.getDetail().getDetail().setTimelineImgs(images);
        timeLine4ImageText.getDetail().getDetail().setContent(content);
        return timeLine4ImageText;
    }

    private TimeLine newTimeLine4Text(String content) {
        if (timeLine4Text != null) {
            return timeLine4Text;
        }
        timeLine4Text = newTimeLine(PREF_KEY_TIMELINE_TEXT);
        timeLine4Text.getDetail().getDetail().setContent(content);
        return timeLine4Text;
    }

    private TimeLine newTimeLine(long timelineId) {
        TimeLine timeLine = new TimeLine();
        timeLine.setTimelineId(timelineId);
        timeLine.setUserInfo(HereUser.getInstance().getUserInfo());
        timeLine.setPostTime(System.currentTimeMillis());
        timeLine.setStatus(TimelineStatus.STATUS_SENDING);
        TimeLineCompose compose = new TimeLineCompose();
        TimeLineDetail detail = new TimeLineDetail(TimeLineDetail.TYPE_MOMENT, compose);
        timeLine.setDetail(detail);
        saveTimeLineToLocal(timeLine);
        return timeLine;
    }

    private boolean uploadImageIfNeed(List<TimeLineImage> images) {
        List<UploadParam> params = null;
        if (images != null && !images.isEmpty()) {
            params = new ArrayList<>();
            for (TimeLineImage item : images) {
                UploadParam param = new UploadParam(UPLOAD_IMAGE_GROUP_ID, item.getUrl(), this);
                params.add(param);
            }
        }

        if (params != null) {
            HereSingletonFactory.getInstance().getPhotoUploadManager().enqueueWithGroup(params);
            return true;
        }

        return false;
    }

    private void post(final TimeLine timeLine) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Object, Object, ApiResult>() {
            @Override
            protected ApiResult doInBackground(Object[] objects) {
                TimelinePostDto dto = new TimelinePostDto();
                TimeLineCompose composeDetail = timeLine.getDetail().getDetail();
                dto.setContent(composeDetail.getContent());
                if (composeDetail.getTimelineImgs() != null) {
                    dto.setTimelineImgs(composeDetail.getTimelineImgs());
                }
                String postJson = new Gson().toJson(dto);
                return manager.newTimeLine(postJson);
            }

            @Override
            protected void onPostExecute(ApiResult result) {

                super.onPostExecute(result);
                if (result == null) {
                    handleSendError(timeLine);
                    return;
                }

                if (result.getStatus() != ApiStatus.OK) {
                    handleSendError(timeLine);
                    return;
                }

                free(timeLine);
                EventBus.getDefault().post(new EventTimelinePost(EventTimelinePost.STATUS_SEND_SUCCESS));
            }
        };
        task.execute();
    }

    private void handleSendError(TimeLine timeLine) {
        timeLine.setStatus(TimelineStatus.STATUS_SEND_FAILED);
        saveTimeLineToLocal(timeLine);
        EventBus.getDefault().post(new EventTimelinePost(EventTimelinePost.STATUS_SEND_EXCEPTION));
    }

    @Override
    public void clear() {
        free(timeLine4ImageText);
        free(timeLine4Text);
    }

    private void free(TimeLine timeLine) {
        if (timeLine == null) {
            return;
        }
        removeTimeLineFromLocal(timeLine);
        if (timeLine4Text != null && timeLine.getTimelineId() == timeLine4Text.getTimelineId()) {
            timeLine4Text = null;
        } else if (timeLine4ImageText != null && timeLine4ImageText.getTimelineId() == timeLine.getTimelineId()) {
            timeLine4ImageText = null;
        }
    }

    private void updateImagePath(UploadParam param) {
        for (TimeLineImage timeLineImage :
                timeLine4ImageText.getDetail().getDetail().getTimelineImgs()) {
            if (TextUtils.equals(timeLineImage.getUrl(), param.filePath)) {
                timeLineImage.setUrl(param.getRemotePath());
            }
        }
    }

    private void saveTimeLineToLocal(TimeLine timeLine) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(timeLine);
        UserSP.putAndApply(mContext, timeLine.getTimelineId() + "", jsonStr);
    }

    private void removeTimeLineFromLocal(TimeLine timeLine) {
        UserSP.remove(mContext, timeLine.getTimelineId() + "");
    }

    public void restoreTimeLineFormLocal() {
        Gson gson = new Gson();
        String jsonStr = UserSP.getString(mContext, PREF_KEY_TIMELINE_IMAGE_TEXT + "", null);
        if (!TextUtils.isEmpty(jsonStr)) {
            timeLine4ImageText = gson.fromJson(jsonStr, TimeLine.class);
        }

        jsonStr = UserSP.getString(mContext, PREF_KEY_TIMELINE_TEXT + "", null);
        if (!TextUtils.isEmpty(jsonStr)) {
            timeLine4Text = gson.fromJson(jsonStr, TimeLine.class);
        }
    }


    @Override
    public void onUploadCompleted(UploadParam param, List<UploadParam> group, boolean isAllEnd) {
        if (param.groupId == UPLOAD_IMAGE_GROUP_ID) {
            updateImagePath(param);
            if (isAllEnd) {
                post(timeLine4ImageText);
            }
        }
    }

    @Override
    public void onUploadError(UploadParam param, Throwable e) {
        if (param.groupId == UPLOAD_IMAGE_GROUP_ID) {
            handleSendError(timeLine4ImageText);
        }
    }

    @Override
    public void onUploadProgress(UploadParam param, double percent) {

    }

    @Override
    public void onUploadStart(UploadParam param) {

    }
}
