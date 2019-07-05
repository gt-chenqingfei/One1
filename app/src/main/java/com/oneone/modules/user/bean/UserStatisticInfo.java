package com.oneone.modules.user.bean;

/**
 * Created by here on 18/6/5.
 */

public class UserStatisticInfo {
    private UserStatisticCount countMyMatchers;
    private UserStatisticCount countMySingles;
    private UserStatisticCount countMyFollowing;
    private UserStatisticCount countFollowMe;
    private UserStatisticCount countTimeline;
    private UserStatisticCount countUserCompletedScore;

    public UserStatisticCount getCountMyMatchers() {
        return countMyMatchers;
    }

    public void setCountMyMatchers(UserStatisticCount countMyMatchers) {
        this.countMyMatchers = countMyMatchers;
    }

    public UserStatisticCount getCountMySingles() {
        return countMySingles;
    }

    public void setCountMySingles(UserStatisticCount countMySingles) {
        this.countMySingles = countMySingles;
    }

    public UserStatisticCount getCountMyFollowing() {
        return countMyFollowing;
    }

    public void setCountMyFollowing(UserStatisticCount countMyFollowing) {
        this.countMyFollowing = countMyFollowing;
    }

    public UserStatisticCount getCountFollowMe() {
        return countFollowMe;
    }

    public void setCountFollowMe(UserStatisticCount countFollowMe) {
        this.countFollowMe = countFollowMe;
    }

    public UserStatisticCount getCountTimeline() {
        return countTimeline;
    }

    public void setCountTimeline(UserStatisticCount countTimeline) {
        this.countTimeline = countTimeline;
    }

    public UserStatisticCount getCountUserCompletedScore() {
        return countUserCompletedScore;
    }

    public void setCountUserCompletedScore(UserStatisticCount countUserCompletedScore) {
        this.countUserCompletedScore = countUserCompletedScore;
    }

    public class UserStatisticCount {
        private int count;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}

