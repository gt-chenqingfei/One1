package com.oneone.modules.msg.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.oneone.BasePullListActivity;
import com.oneone.R;
import com.oneone.api.constants.RedDot;
import com.oneone.framework.ui.BaseRecyclerViewAdapter;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;
import com.oneone.framework.ui.widget.SimplePullRecyclerView;
import com.oneone.modules.find.beans.FindCondition;
import com.oneone.modules.find.dto.FindPageUserInfoDTO;
import com.oneone.modules.find.dto.LikeListDto;
import com.oneone.modules.find.dto.LikeUnreadListDto;
import com.oneone.modules.msg.adapter.SystemMsgAdapter;
import com.oneone.modules.msg.beans.GiftProd;
import com.oneone.modules.msg.beans.IMEmoji;
import com.oneone.modules.msg.beans.IMFirstRelation;
import com.oneone.modules.msg.beans.IMUser;
import com.oneone.modules.msg.beans.IMUserPrerelation;
import com.oneone.modules.msg.beans.MsgMeta;
import com.oneone.modules.msg.beans.NotifyListItem;
import com.oneone.modules.msg.beans.TalkBeans.MyMessage;
import com.oneone.modules.msg.beans.TimeLineInfo;
import com.oneone.modules.msg.contract.MsgContract;
import com.oneone.modules.msg.dto.MsgMetaDto;
import com.oneone.modules.msg.presenter.MsgPresenter;
import com.oneone.modules.reddot.RedDotManager;
import com.oneone.restful.ApiResult;
import com.oneone.schema.SchemaUtil;
import com.oneone.widget.CustomGlobalHeader;
import com.oneone.widget.EmptyView4Common;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * Created by here on 18/5/15.
 */
@Route(path = "/system_message/list")
@ToolbarResource(title = R.string.str_my_msg_sys_title_text)
@LayoutResource(R.layout.activity_client_and_system_msg)
public class ClientAndSystemMsgActivity extends BasePullListActivity<MsgMeta, MsgPresenter, MsgContract.View> implements MsgContract.View {
    public static void startActivity (Activity context) {
        RedDotManager.getInstance().clearAndRefreshDot(RedDot.ONEONE_HELPER);
        Intent intent = new Intent(context, ClientAndSystemMsgActivity.class);
        context.startActivity(intent);
    }

    @BindView(R.id.sys_msg_lv)
    SimplePullRecyclerView<MsgMeta> mSimpleRecyclerView;

    public static final int PAGE_COUNT = 10;
    private long lastTimeMillis;

    private String loadingStatus = "normal";

    @NonNull
    @Override
    public BaseRecyclerViewAdapter<MsgMeta> adapterToDisplay() {
        return new SystemMsgAdapter(new SystemMsgAdapter.SystemMsgAdapterListener() {
            @Override
            public void onItemClick(Object o, int id, int position) {
                MsgMeta msgMeta = (MsgMeta) o;
                if (msgMeta == null) {
                    return;
                }

                SchemaUtil.doRouter(ClientAndSystemMsgActivity.this,msgMeta.getLinkUrl());
            }
        }, getActivityContext());
    }

    @NonNull
    @Override
    public SimplePullRecyclerView<MsgMeta> getDisplayView() {
        EmptyView4Common emptyView4Common = new EmptyView4Common(getActivityContext(), getResources().getString(R.string.str_common_no_data_text_2));
        mSimpleRecyclerView.setEmptyView(emptyView4Common);
        return mSimpleRecyclerView;
    }

    @Override
    public MsgPresenter onCreatePresenter() {
        return new MsgPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((CustomGlobalHeader) mSimpleRecyclerView.getSmartRefreshLayout().getRefreshHeader()).
                setBackgroundColor(getResources().getColor(R.color.color_EDF3FC));
        lastTimeMillis = System.currentTimeMillis();

        loadMore(0);
    }

    @Override
    public void loadMore(int pageIndex) {
        super.loadMore(pageIndex);

        lastTimeMillis = System.currentTimeMillis();
        mPresenter.msgNoteListmeta(false, lastTimeMillis + "", PAGE_COUNT + "");
    }

    @Override
    public void loadRefresh() {
        super.loadRefresh();

        mPresenter.msgNoteListmeta(true, lastTimeMillis + "", PAGE_COUNT + "");
    }
    @Override
    public void onFindRecommend(List<FindPageUserInfoDTO> userList, int expire) {

    }

    @Override
    public void onFindSetCondition() {

    }

    @Override
    public void onFindGetCondition(FindCondition findCondition) {

    }

    @Override
    public void onSetLike() {

    }

    @Override
    public void onCancelLike() {

    }

    @Override
    public void onSetNoFeel() {

    }

    @Override
    public void onCancelNoFeel() {

    }

    @Override
    public void onImUserGettoken(IMUser imUser) {

    }

    @Override
    public void onImRefreshToken(IMUser imUser) {

    }

    @Override
    public void onImUserIsrelation(boolean relation) {

    }

    @Override
    public void onImUserPrerelation(IMUserPrerelation imUserPrerelation) {

    }

    @Override
    public void onImUserApply(int status, String toImUserId) {

    }

    @Override
    public void onImUserMsgreply(ApiResult rlt) {

    }

    @Override
    public void onImUserDelrealation(ApiResult rlt) {

    }

    @Override
    public void onImUserFirstRelationList(boolean isLoadMore, int oldCount, int newCount, int count, List<IMFirstRelation> imFirstRelationList) {

    }

    @Override
    public void onImUserRelationList(int count, List<IMFirstRelation> imFirstRelationList) {

    }

    @Override
    public void onImMsgCheck(ApiResult<String> rlt, MyMessage myMessage) {

    }

    @Override
    public void onImMsgListEmoji(List<IMEmoji> imEmojiList) {

    }

    @Override
    public void onProdGiftList(int count, List<GiftProd> giftProdList) {

    }

//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            sysMsgLv.getRefreshableView().setSelection(adapter.getCount() - 1);
//        }
//    };

    @Override
    public void onMsgNoteListmeta(boolean isLoadMore, List<MsgMeta> msgBeanList) {
        if (msgBeanList != null && msgBeanList.size() > 0) {
            RedDotManager.getInstance().clearDot(RedDot.NEW_SINGLES);
            Collections.sort(msgBeanList);
            lastTimeMillis = msgBeanList.get(0).getTimestamp();
            if (isLoadMore) {
                getAdapter().getList().addAll(0, msgBeanList);
                getAdapter().notifyDataSetChanged();
                mSimpleRecyclerView.getSmartRefreshLayout().finishRefresh();
            } else {
                getAdapter().getList().clear();
                getAdapter().getList().addAll(msgBeanList);
                getAdapter().notifyDataSetChanged();
                getAdapter().showEmptyViewIfNeeded();
                mSimpleRecyclerView.getSmartRefreshLayout().finishLoadMore();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSimpleRecyclerView.getRecyclerView().smoothScrollToPosition(10);
                    }
                }, 200);
            }
        } else {
            onLoadFailed("");
        }



//        if (msgBeanList != null && msgBeanList.size() > 0) {
//            if (adapter == null) {
//                System.out.println("onMsgNoteListmeta null");
//                adapter = new SystemMsgAdapter(getActivityContext(), msgBeanList);
//                lastTimeMillis = adapter.getLastTime();
//                sysMsgLv.setAdapter(adapter);
//
//                handler.sendEmptyMessageDelayed(0, 300);
//
//            } else {
//                lastTimeMillis = adapter.loadMore(msgBeanList);
//            }
//        }
//        loadingStatus = "normal";
//        sysMsgLv.onRefreshComplete();
    }

    @Override
    public void onMsgNoteLastmeta(MsgMetaDto msgMetaDto) {

    }

    @Override
    public void onMyLikeList(boolean isLoadMore, LikeListDto listDto) {

    }

    @Override
    public void onLikeMeList(boolean isLoadMore, LikeListDto listDto) {

    }

    @Override
    public void onLikeEachother(boolean isLoadMore, LikeListDto listDto) {

    }

    @Override
    public void onLikeUnread(LikeUnreadListDto likeUnreadListDto) {

    }

    @Override
    public void onGetNotifyList(boolean isRefresh, int count, List<TimeLineInfo> timelineList, List<NotifyListItem> list, long lastReadTime) {

    }
}
