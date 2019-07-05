package com.oneone.modules.following.contract;

import com.oneone.framework.ui.ibase.IBaseView;
import com.oneone.modules.following.beans.FollowListItem;

import java.util.List;

/**
 * Created by here on 18/4/25.
 */

public interface FollowingContract {
    interface View extends IBaseView {
        void onFollowingFollow(Integer followStatus);
        void onFollowingUnfollow(Integer followStatus);
        void onFollowingFollowers(boolean isLoadMore, int count, List<FollowListItem> list);
        void onFollowingFollowings(boolean isLoadMore, int count, List<FollowListItem> list);
    }


    interface Presenter {
        void followingFollow(String userId);
        void followingUnfollow(String userId);
        void followingFollowers(boolean isLoadMore, int skip, int pageCount);
        void followingFollowings(boolean isLoadMore, int skip, int pageCount);
    }
}
