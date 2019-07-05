package com.oneone.modules.reddot;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oneone.OneOne;
import com.oneone.api.constants.RedDot;
import com.oneone.api.request.RedDotTypesParams;
import com.oneone.event.EventFindCountdownEnd;
import com.oneone.event.EventRefreshMsgBubble;
import com.oneone.modules.reddot.dto.RedDotDto;
import com.oneone.modules.reddot.model.RedDotModel;
import com.oneone.modules.user.UserSP;


import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

/**
 * @author qingfei.chen
 * @since 2018/7/3.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class RedDotManager {
    public static final int MSG_LOOP_FETCH_DOT = 100;
    public static final int LOOP_DELAY_MILLIS = 60 * 1000;
    public static final String PREF_DOT = "PrefDot_";
    public static String[] mDotQueryParam = new String[]{
            RedDot.FEEDBACK,
            RedDot.INTERACTION_NOTIFY,
            RedDot.LIKE_EACH_OTHER,
            RedDot.NEW_MATCHERS,
            RedDot.NEW_MEET,
            RedDot.NEW_SINGLES,
            RedDot.NEW_TIMELINE,
            RedDot.LIKE_ME,
            RedDot.ONEONE_HELPER};

    private Context mContext;
    private RedDotModel redDotModel;
    private Handler mMainHandler = null;
    private boolean isShowFindDot = false;

    private RedDotManager() {
        mContext = OneOne.getInstance();
        redDotModel = new RedDotModel(mContext);
    }

    private static class DotManagerHolder {
        private static RedDotManager getInstance() {
            return new RedDotManager();
        }
    }

    private static RedDotManager instance;

    public static RedDotManager getInstance() {
        if (instance == null) {
            instance = DotManagerHolder.getInstance();
        }

        return instance;
    }

    public void startLoopFetchDot() {
        if (mMainHandler != null) {
            return;
        }
        mMainHandler = new LoopHandler(Looper.getMainLooper());
        mMainHandler.sendEmptyMessageDelayed(MSG_LOOP_FETCH_DOT, LOOP_DELAY_MILLIS);
        fetchAllDot();
    }

    public void stopLoopFetchDot() {
        mMainHandler.removeMessages(MSG_LOOP_FETCH_DOT);
        mMainHandler = null;
    }

    public void notifyFindDotChanged(boolean isShowFindDot) {
        this.isShowFindDot = isShowFindDot;
        EventBus.getDefault().post(new EventFindCountdownEnd());
    }

    private class LoopHandler extends Handler {
        public LoopHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_LOOP_FETCH_DOT:
                    mMainHandler.sendEmptyMessageDelayed(MSG_LOOP_FETCH_DOT, LOOP_DELAY_MILLIS);
                    fetchAllDot();
                    break;
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public void fetchAllDot() {

        new AsyncTask<Object, Object, Object>() {

            @Override
            protected Object doInBackground(Object... objects) {
                StringBuilder paramsBuilder = new StringBuilder();
                for (int i = 0; i < mDotQueryParam.length; i++) {
                    paramsBuilder.append(mDotQueryParam[i]);

                    if (i < mDotQueryParam.length - 1) {
                        paramsBuilder.append(",");
                    }
                }
                String param = "";
                try {
                    param = URLEncoder.encode(paramsBuilder.toString(), "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return redDotModel.queryRedDot(param);
            }

            @Override
            protected void onPostExecute(Object result) {
                super.onPostExecute(result);
                if (result == null) {
                    return;
                }
                Type type = new TypeToken<Map<String, RedDotDto>>() {
                }.getType();
                Map<String, RedDotDto> map2 = new Gson().fromJson(result.toString(), type);
                Iterator<Map.Entry<String, RedDotDto>> iterator = map2.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, RedDotDto> entry = iterator.next();
                    String key = entry.getKey();
                    RedDotDto value = entry.getValue();
                    UserSP.putAndApply(mContext, PREF_DOT + key, value.getUnreadCount());
                }
            }
        }.execute();

    }

    @SuppressLint("StaticFieldLeak")
    public void clearDot(final String type) {
        new AsyncTask<Object, Object, Boolean>() {

            @Override
            protected Boolean doInBackground(Object... objects) {

                return redDotModel.clearRedDot(new RedDotTypesParams(type));
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                if (result) {
                    UserSP.putAndApply(mContext, PREF_DOT + type, 0);
                }
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void clearAndRefreshDot(final String type) {
        new AsyncTask<Object, Object, Boolean>() {

            @Override
            protected Boolean doInBackground(Object... objects) {

                return redDotModel.clearRedDot(new RedDotTypesParams(type));
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                if (result) {
                    UserSP.putAndApply(mContext, PREF_DOT + type, 0);
                }

                EventBus.getDefault().post(new EventRefreshMsgBubble());
            }
        }.execute();
    }

    public boolean isShowNewTimelineDot() {
        return UserSP.getInt(mContext, PREF_DOT + RedDot.NEW_TIMELINE, 0) > 0;
    }

    public int getFeedbackUnread() {
        return UserSP.getInt(mContext, PREF_DOT + RedDot.FEEDBACK, 0);
    }

    public int getInteractionNotifyUnread() {
        return UserSP.getInt(mContext, PREF_DOT + RedDot.INTERACTION_NOTIFY, 0);
    }

    public int getLikeEachOtherUnread() {
        return UserSP.getInt(mContext, PREF_DOT + RedDot.LIKE_EACH_OTHER, 0);
    }

    public int getNewMatchersUnread() {
        return UserSP.getInt(mContext, PREF_DOT + RedDot.NEW_MATCHERS, 0);
    }

    public int getNewMeetUnread() {
        return UserSP.getInt(mContext, PREF_DOT + RedDot.NEW_MEET, 0);
    }

    public int getNewSinglesUnread() {
        return UserSP.getInt(mContext, PREF_DOT + RedDot.NEW_SINGLES, 0);
    }

    public int getLikeMeUnread() {
        return UserSP.getInt(mContext, PREF_DOT + RedDot.LIKE_ME, 0);
    }

    public int getOneoneHelperUnread() {
        return UserSP.getInt(mContext, PREF_DOT + RedDot.ONEONE_HELPER, 0);
    }

    public boolean isShowFindDot() {
        return isShowFindDot;
    }


}
