package com.oneone.modules.msg.fragment.likerelation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.oneone.R;
import com.oneone.framework.ui.AbstractPullListFragment;
import com.oneone.framework.ui.BaseRecyclerViewAdapter;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.widget.SimplePullRecyclerView;
import com.oneone.modules.find.beans.FindCondition;
import com.oneone.modules.find.dto.FindPageUserInfoDTO;
import com.oneone.modules.find.dto.LikeListDto;
import com.oneone.modules.find.dto.LikeUnreadListDto;
import com.oneone.modules.find.dto.LikeUserDto;
import com.oneone.modules.following.adapter.MyFollowingAdapter;
import com.oneone.modules.msg.adapter.likerelation.LikeRelationAdapter;
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
import com.oneone.modules.profile.ProfileStater;
import com.oneone.restful.ApiResult;
import com.oneone.widget.CustomGlobalHeader;
import com.oneone.widget.EmptyView4Common2;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * Created by here on 18/5/25.
 */

@LayoutResource(R.layout.frag_my_like_list)
public class LikeRelationFragment extends AbstractPullListFragment<LikeUserDto,MsgPresenter,MsgContract.View> implements MsgContract.View {
    public int myPageIndex = 0;
    public static final int PAGE_COUNT = 10;
    @BindView(R.id.like_list_recycler)
    SimplePullRecyclerView<LikeUserDto> mSimpleRecyclerView;
    private String type;
    private long preViewTime;

    private boolean firstLoadRefresh = true;
    public void setFragmentInfo (String type, long preViewTime) {
        this.type = type;
        this.preViewTime = preViewTime;
    }

    @Override
    public MsgPresenter onPresenterCreate() {
        return new MsgPresenter();
    }

    @NonNull
    @Override
    public BaseRecyclerViewAdapter<LikeUserDto> adapterToDisplay() {
        String noMoreText = null;
        switch (type) {
            case LikeRelationAdapter.TYPE_LIKE_ME:
                noMoreText = getContext().getString(R.string.str_like_relation_no_more_like_me);
                break;
            case LikeRelationAdapter.TYPE_MY_LIKE:
                noMoreText = getContext().getString(R.string.str_like_relation_no_more_my_like);
                break;
            case LikeRelationAdapter.TYPE_LIKE_EACHOTHER:
                noMoreText = getContext().getString(R.string.str_like_relation_no_more_like_each_other);
                break;
        }
        return new LikeRelationAdapter(new LikeRelationAdapter.LikeRelationAdapterListener() {
            @Override
            public void onItemClick(Object o, int id, int position) {
                LikeUserDto itemUser = (LikeUserDto) o;
                ProfileStater.startWithUserInfo(getContext(), itemUser.getUserInfo());
            }
        }, type, getActivityContext(), noMoreText);
    }

    public LikeRelationFragment scrollTop () {
//        mSimpleRecyclerView.getSmartRefreshLayout().scrollBy(0,0);
        mSimpleRecyclerView.getSmartRefreshLayout().autoRefresh();
        return this;
    }

    @NonNull
    @Override
    public SimplePullRecyclerView<LikeUserDto> getDisplayView() {
        EmptyView4Common2 emptyView4Common2 = null;
        switch (type) {
            case LikeRelationAdapter.TYPE_LIKE_ME:
                emptyView4Common2 = new EmptyView4Common2(getContext(), EmptyView4Common2.EMPTY_TYPE.EMPTY_LIKE_ME);
                break;
            case LikeRelationAdapter.TYPE_MY_LIKE:
                emptyView4Common2 = new EmptyView4Common2(getContext(), EmptyView4Common2.EMPTY_TYPE.EMPTY_MY_LIKE);
                break;
            case LikeRelationAdapter.TYPE_LIKE_EACHOTHER:
                emptyView4Common2 = new EmptyView4Common2(getContext(), EmptyView4Common2.EMPTY_TYPE.EMPTY_LIKE_EACHOTHER);
                break;
        }
        if (emptyView4Common2 != null)
            mSimpleRecyclerView.setEmptyView(emptyView4Common2);
        return mSimpleRecyclerView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((CustomGlobalHeader) mSimpleRecyclerView.getRefreshHeader()).setBackgroundColor(getResources().getColor(R.color.color_white));

        loadRefresh();
    }

    @Override
    public void loadMore(int pageIndex) {
        super.loadMore(pageIndex);

        if (type.equals(LikeRelationAdapter.TYPE_LIKE_ME)) {
            mPresenter.likeMeList(true, myPageIndex * PAGE_COUNT, PAGE_COUNT, preViewTime);
        } else if (type.equals(LikeRelationAdapter.TYPE_MY_LIKE)) {
            mPresenter.myLikeList(true, myPageIndex * PAGE_COUNT, PAGE_COUNT, preViewTime);
        } else if (type.equals(LikeRelationAdapter.TYPE_LIKE_EACHOTHER)) {
            mPresenter.likeEachotherList(true, myPageIndex * PAGE_COUNT, PAGE_COUNT, preViewTime);
        }
    }

    @Override
    public void loadRefresh() {
        super.loadRefresh();

        if (!firstLoadRefresh) {
            preViewTime = System.currentTimeMillis();
        }

        myPageIndex = 0;

        if (type.equals(LikeRelationAdapter.TYPE_LIKE_ME)) {
            mPresenter.likeMeList(false, myPageIndex * PAGE_COUNT, PAGE_COUNT, preViewTime);
        } else if (type.equals(LikeRelationAdapter.TYPE_MY_LIKE)) {
            mPresenter.myLikeList(false, myPageIndex * PAGE_COUNT, PAGE_COUNT, preViewTime);
        } else if (type.equals(LikeRelationAdapter.TYPE_LIKE_EACHOTHER)) {
            mPresenter.likeEachotherList(false, myPageIndex * PAGE_COUNT, PAGE_COUNT, preViewTime);
        }
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

    @Override
    public void onMsgNoteListmeta(boolean isLoadMore, List<MsgMeta> msgBeanList) {

    }

    @Override
    public void onMsgNoteLastmeta(MsgMetaDto msgMetaDto) {

    }

    @Override
    public void onMyLikeList(boolean isLoadMore, LikeListDto listDto) {
        if (listDto != null) {
            List<LikeUserDto> likeList = listDto.getLikeList();
            Collections.sort(likeList);
            if (isLoadMore) {
                onLoadMore(likeList);
            } else {
                onLoadCompleted(likeList);
            }

            if (likeList.size() < PAGE_COUNT) {
                getAdapter().setShowNoMoreView(true);
            } else {
                getAdapter().setShowNoMoreView(false);
            }

            myPageIndex ++;
        } else {
            getAdapter().setShowNoMoreView(false);
            onLoadFailed("");
        }
    }

    @Override
    public void onLikeMeList(boolean isLoadMore, LikeListDto listDto) {
        if (listDto != null) {
            List<LikeUserDto> likeList = listDto.getLikeList();
            Collections.sort(likeList);
            if (isLoadMore) {
                onLoadMore(likeList);
            } else {
                onLoadCompleted(likeList);
            }

            if (likeList.size() < PAGE_COUNT) {
                getAdapter().setShowNoMoreView(true);
            } else {
                getAdapter().setShowNoMoreView(false);
            }

            myPageIndex ++;
        } else {
            getAdapter().setShowNoMoreView(false);
            onLoadFailed("");
        }
    }

    @Override
    public void onLikeEachother(boolean isLoadMore, LikeListDto listDto) {
        if (listDto != null) {
            List<LikeUserDto> likeList = listDto.getLikeList();
            Collections.sort(likeList);
            if (isLoadMore) {
                onLoadMore(likeList);
            } else {
                onLoadCompleted(likeList);
            }

            if (likeList.size() < PAGE_COUNT) {
                getAdapter().setShowNoMoreView(true);
            } else {
                getAdapter().setShowNoMoreView(false);
            }

            myPageIndex ++;
        } else {
            getAdapter().setShowNoMoreView(false);
            onLoadFailed("");
        }
    }

    @Override
    public void onLikeUnread(LikeUnreadListDto likeUnreadListDto) {

    }

    @Override
    public void onGetNotifyList(boolean isRefresh, int count, List<TimeLineInfo> timelineList, List<NotifyListItem> list, long lastReadTime) {

    }
}
