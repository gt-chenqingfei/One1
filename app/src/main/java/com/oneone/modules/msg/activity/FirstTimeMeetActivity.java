package com.oneone.modules.msg.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.oneone.BasePullListActivity;
import com.oneone.R;
import com.oneone.framework.ui.BaseRecyclerViewAdapter;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;
import com.oneone.framework.ui.widget.SimplePullRecyclerView;
import com.oneone.modules.find.beans.FindCondition;
import com.oneone.modules.find.dto.FindPageUserInfoDTO;
import com.oneone.modules.find.dto.LikeListDto;
import com.oneone.modules.find.dto.LikeUnreadListDto;
import com.oneone.modules.find.dto.LikeUserDto;
import com.oneone.modules.msg.IMManager;
import com.oneone.modules.msg.adapter.FirstTimeMeetAdapter;
import com.oneone.modules.msg.beans.GiftProd;
import com.oneone.modules.msg.beans.IMEmoji;
import com.oneone.modules.msg.beans.IMFirstRelation;
import com.oneone.modules.msg.beans.IMUser;
import com.oneone.modules.msg.beans.IMUserPrerelation;
import com.oneone.modules.msg.beans.MsgMeta;
import com.oneone.modules.msg.beans.NotifyListItem;
import com.oneone.modules.msg.beans.TalkBeans.MyMessage;
import com.oneone.modules.msg.beans.TalkBeans.MyRecentContact;
import com.oneone.modules.msg.beans.TimeLineInfo;
import com.oneone.modules.msg.contract.MsgContract;
import com.oneone.modules.msg.dto.MsgMetaDto;
import com.oneone.modules.msg.event.ImContactChangeEvent;
import com.oneone.modules.msg.presenter.MsgPresenter;
import com.oneone.restful.ApiResult;
import com.oneone.widget.EmptyView4Common;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * Created by here on 18/5/26.
 */

@ToolbarResource(title = R.string.str_first_time_meet_title_text)
@LayoutResource(R.layout.activity_first_time_meet)
public class FirstTimeMeetActivity extends BasePullListActivity<IMFirstRelation, MsgPresenter, MsgContract.View> implements MsgContract.View {
    @BindView(R.id.first_time_meet_lv)
    SimplePullRecyclerView<IMFirstRelation> mSimpleRecyclerView;

    public static final int PAGE_COUNT = 50;
    private long lastTimeMillis;

    private String loadingStatus = "normal";

    public static void startActivity(Activity context) {
        Intent intent = new Intent(context, FirstTimeMeetActivity.class);
        context.startActivity(intent);
    }


    @NonNull
    @Override
    public BaseRecyclerViewAdapter<IMFirstRelation> adapterToDisplay() {
        return new FirstTimeMeetAdapter(new FirstTimeMeetAdapter.FirstTimeMeetAdapterListener() {
            @Override
            public void onItemClick(Object o, int id, int position) {
                LikeUserDto likeUserDto = (LikeUserDto) o;

            }

            @Override
            public void onDeleteClick(MyRecentContact myRecentContact) {
                super.onDeleteClick(myRecentContact);
                getAdapter().removeItem(myRecentContact.getFirstRelation(), 0);

                IMManager.getInstance().deleteRecentContact(myRecentContact.getFirstRelation().getOtherUserImUid());

                mPresenter.imUserDelrealation(myRecentContact.getFirstRelation().getOtherUserImUid());
            }
        }, getActivityContext());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent (ImContactChangeEvent imContactChangeEvent) {
        System.out.println("receive contact change!!!!!!!!!!!");
        getAdapter().notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SimplePullRecyclerView<IMFirstRelation> getDisplayView() {
        mSimpleRecyclerView.setEmptyView(new EmptyView4Common(mContext, getTitleView().getText().toString()));
        return mSimpleRecyclerView;
    }

    @Override
    public MsgPresenter onCreatePresenter() {
        return new MsgPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         EventBus.getDefault().register(this);

        lastTimeMillis = System.currentTimeMillis();

        loadMore(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void loadMore(int pageIndex) {
        super.loadMore(pageIndex);

        mPresenter.imUserFirstRelationList(true, MsgPresenter.FIRST_RELATION_LIST_TYPE_TALK_PAGE, PAGE_COUNT * pageIndex, PAGE_COUNT);
    }

    @Override
    public void loadRefresh() {
        super.loadRefresh();

        mPresenter.imUserFirstRelationList(false, MsgPresenter.FIRST_RELATION_LIST_TYPE_TALK_PAGE, 0, PAGE_COUNT);
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
        getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onImUserFirstRelationList(boolean isLoadMore, int oldCount, int newCount, int count, List<IMFirstRelation> imFirstRelationList) {
        if (imFirstRelationList != null && imFirstRelationList.size() > 0) {
            Collections.sort(imFirstRelationList);
            if (isLoadMore) {
                onLoadMoreCompleted(imFirstRelationList);
            } else {
                onRefreshCompleted(imFirstRelationList);
            }
        } else {
            onLoadFailed("");
        }
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

    @Override
    public void onMsgNoteListmeta(boolean isLoadMore, List<MsgMeta> msgBeanList) {
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