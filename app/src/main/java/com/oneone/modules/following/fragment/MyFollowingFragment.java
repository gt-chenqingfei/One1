package com.oneone.modules.following.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.oneone.R;
import com.oneone.framework.ui.AbstractPullListFragment;
import com.oneone.framework.ui.BaseRecyclerViewAdapter;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.widget.SimplePullRecyclerView;
import com.oneone.modules.following.adapter.MyFollowingAdapter;
import com.oneone.modules.following.beans.FollowListItem;
import com.oneone.modules.following.contract.FollowingContract;
import com.oneone.modules.following.dialog.CancelFollowingDialog;
import com.oneone.modules.following.presenter.FollowingPresenter;
import com.oneone.widget.CustomGlobalHeader;
import com.oneone.widget.EmptyView4Common2;

import java.util.List;

import butterknife.BindView;

/**
 * Created by here on 18/4/26.
 */

@LayoutResource(R.layout.frag_my_following)
public class MyFollowingFragment extends AbstractPullListFragment<FollowListItem, FollowingPresenter, FollowingContract.View> implements FollowingContract.View {
    public int myPageIndex = 0;
    public static final int PAGE_COUNT = 10;

    @BindView(R.id.frag_my_following_recycler)
    SimplePullRecyclerView<FollowListItem> myFollowingRecycler;

    private CancelFollowingDialog cancelFollowingDialog;

    private int type;
    private MyFollowingFragmentListener listener;

    public interface MyFollowingFragmentListener {
        public void showEmpty(boolean isEmpty);
    }


    public void setFragmentContent(int type, MyFollowingFragmentListener listener) {
        this.type = type;
        this.listener = listener;
    }

    @Override
    public FollowingPresenter onPresenterCreate() {
        return new FollowingPresenter();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((CustomGlobalHeader) myFollowingRecycler.getRefreshHeader()).setBackgroundColor(getResources().getColor(R.color.color_white));
    }


    @NonNull
    @Override
    public SimplePullRecyclerView<FollowListItem> getDisplayView() {
        EmptyView4Common2 emptyView4Common2 = null;
        switch (type) {
            case MyFollowingAdapter.TYPE_MY_FOLLOW:
                emptyView4Common2 = new EmptyView4Common2(getContext(), EmptyView4Common2.EMPTY_TYPE.EMPTY_MY_FOLLOW);
                break;
            case MyFollowingAdapter.TYPE_MY_ATTENTION:
                emptyView4Common2 = new EmptyView4Common2(getContext(), EmptyView4Common2.EMPTY_TYPE.EMPTY_FOLLOW_ME);
                break;
        }
        if (emptyView4Common2 != null)
            myFollowingRecycler.setEmptyView(emptyView4Common2);
        return myFollowingRecycler;
    }

    @Override
    public void loadMore(int pageIndex) {
        super.loadMore(pageIndex);

        if (type == MyFollowingAdapter.TYPE_MY_FOLLOW)
            mPresenter.followingFollowings(true, myPageIndex * PAGE_COUNT, PAGE_COUNT);
        else if (type == MyFollowingAdapter.TYPE_MY_ATTENTION)
            mPresenter.followingFollowers(true, myPageIndex * PAGE_COUNT, PAGE_COUNT);
    }

    @Override
    public boolean loadRefreshOnStart() {
        return true;
    }

    @Override
    public void loadRefresh() {
        super.loadRefresh();

        myPageIndex = 0;

        if (mPresenter == null) {
            return;
        }
        if (type == MyFollowingAdapter.TYPE_MY_FOLLOW)
            mPresenter.followingFollowings(false, myPageIndex * PAGE_COUNT, PAGE_COUNT);
        else if (type == MyFollowingAdapter.TYPE_MY_ATTENTION)
            mPresenter.followingFollowers(false, myPageIndex * PAGE_COUNT, PAGE_COUNT);
    }

    @Override
    public void onFollowingFollow(Integer followStatus) {
    }

    @Override
    public void onFollowingUnfollow(Integer followStatus) {
    }

    @Override
    public void onFollowingFollowers(boolean isLoadMore, int count, List<FollowListItem> list) {
        if (isLoadMore) {
            if (list == null || list.size() == 0) {
                onLoadFailed("");
            } else {
                myPageIndex ++;
                onLoadMore(list);
            }

            if (list != null && list.size() < PAGE_COUNT) {
                getAdapter().setShowNoMoreView(true);
            } else {
                getAdapter().setShowNoMoreView(false);
            }
        } else {
            if (list == null || list.size() == 0) {
//                listener.showEmpty(true);
            } else {
                myPageIndex ++;
//                listener.showEmpty(false);
            }

            if (list != null && list.size() < PAGE_COUNT) {
                getAdapter().setShowNoMoreView(true);
            } else {
                getAdapter().setShowNoMoreView(false);
            }
            onLoadCompleted(list);
        }
    }

    @Override
    public void onFollowingFollowings(boolean isLoadMore, int count, List<FollowListItem> list) {
        if (isLoadMore) {
            if (list == null || list.size() == 0) {
                onLoadFailed("");
            } else {
                myPageIndex ++;
                onLoadMore(list);
            }

            if (list != null && list.size() < PAGE_COUNT) {
                getAdapter().setShowNoMoreView(true);
            } else {
                getAdapter().setShowNoMoreView(false);
            }
        } else {
            if (list == null || list.size() == 0) {
//                listener.showEmpty(true);
            } else {
                myPageIndex ++;
//                listener.showEmpty(false);
            }

            if (list != null && list.size() < PAGE_COUNT) {
                getAdapter().setShowNoMoreView(true);
            } else {
                getAdapter().setShowNoMoreView(false);
            }
            onLoadCompleted(list);
        }
    }

    @NonNull
    @Override
    public BaseRecyclerViewAdapter<FollowListItem> adapterToDisplay() {

        return new MyFollowingAdapter(new MyFollowingAdapter.MyFollowingAdapterListener() {
            @Override
            public void onItemClick(Object o, int id, int position) {

            }

            @Override
            public void itemBtnClick(FollowListItem followListItem, final View btnView) {
                super.itemBtnClick(followListItem, btnView);
                int followStatus = followListItem.getFollowStatus();
                switch (followStatus) {
                    case 0:
                        btnView.setBackgroundResource(R.drawable.already_follow_bg);
                        followListItem.setFollowStatus(1);
                        mPresenter.followingFollow(followListItem.getUserInfo().getUserId());
                        break;
                    case 2:
                        btnView.setBackgroundResource(R.drawable.follow_eachother_bg);
                        followListItem.setFollowStatus(3);
                        mPresenter.followingFollow(followListItem.getUserInfo().getUserId());
                        break;
                    case 1:
                        cancelFollowingDialog = new CancelFollowingDialog(getActivityContext(), followListItem, new CancelFollowingDialog.CancelFollowingDialogListener() {
                            @Override
                            public void onCancelSelected(FollowListItem followListItem) {
                                followListItem.setFollowStatus(0);
                                btnView.setBackgroundResource(R.drawable.not_follow_bg);
                                mPresenter.followingUnfollow(followListItem.getUserInfo().getUserId());
                                cancelFollowingDialog.dismiss();
                            }
                        });
                        cancelFollowingDialog.show();
                        break;
                    case 3:
                        cancelFollowingDialog = new CancelFollowingDialog(getActivityContext(), followListItem, new CancelFollowingDialog.CancelFollowingDialogListener() {
                            @Override
                            public void onCancelSelected(FollowListItem followListItem) {
                                followListItem.setFollowStatus(2);
                                btnView.setBackgroundResource(R.drawable.not_follow_bg);
                                mPresenter.followingUnfollow(followListItem.getUserInfo().getUserId());
                                cancelFollowingDialog.dismiss();
                            }
                        });
                        cancelFollowingDialog.show();
                        break;
                }

            }
        }, getActivityContext(), type);
    }

}