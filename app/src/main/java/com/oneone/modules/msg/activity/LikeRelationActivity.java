package com.oneone.modules.msg.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.oneone.BaseActivity;
import com.oneone.R;
import com.oneone.api.constants.RedDot;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.modules.find.beans.FindCondition;
import com.oneone.modules.find.dto.FindPageUserInfoDTO;
import com.oneone.modules.find.dto.LikeListDto;
import com.oneone.modules.find.dto.LikeUnreadListDto;
import com.oneone.modules.msg.adapter.likerelation.LikeRelationAdapter;
import com.oneone.modules.msg.adapter.likerelation.LikeRelationFragmentAdapter;
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
import com.oneone.modules.msg.fragment.likerelation.LikeRelationFragment;
import com.oneone.modules.msg.presenter.MsgPresenter;
import com.oneone.modules.qa.activity.MyQaActivity;
import com.oneone.modules.qa.adapter.MyQaClassifyPagerFragmentAdapter;
import com.oneone.modules.qa.fragment.AlreadyAnswerQuestionListFragment;
import com.oneone.modules.reddot.RedDotManager;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.UserSP;
import com.oneone.restful.ApiResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by here on 18/5/24.
 */

@Route(path = "/like/tab")
@LayoutResource(R.layout.activity_like_relation)
public class LikeRelationActivity extends BaseActivity<MsgPresenter, MsgContract.View> implements MsgContract.View, View.OnClickListener {
    public static final String TAB_MUTUAL = "mutual";
    public static final String TAB_FROM_ME = "fromMe";
    public static final String TAB_TO_ME = "toMe";

    public static void startActivity(Context context, String tab) {
        ARouter.getInstance().build("/like/tab").withString("tabCurrent", tab).navigation(context);
    }

    @BindView(R.id.back_btn)
    ImageButton backBtn;
    @BindView(R.id.my_like_title_layout)
    RelativeLayout myLikeTitleLayout;
    @BindView(R.id.like_eacher_title_layout)
    RelativeLayout likeEachotherTitleLayout;
    @BindView(R.id.like_me_title_layout)
    RelativeLayout likeMeTitleLayout;
    @BindView(R.id.my_like_title_tv)
    TextView myLikeTitleTv;
    @BindView(R.id.like_eacher_title_tv)
    TextView likeEachotherTitleTv;
    @BindView(R.id.like_me_title_tv)
    TextView likeMeTitleTv;
    @BindView(R.id.my_like_indicator)
    View myLikeIndicator;
    @BindView(R.id.like_eacher_indicator)
    View likeEachotherIndicator;
    @BindView(R.id.like_me_indicator)
    View likeMeIndicator;

    @BindView(R.id.like_relation_view_pager)
    ViewPager viewPager;

    private long myLikeTime;
    private long likeMeTime;
    private long likeEachotherTime;


    @Autowired
    String tabCurrent;

    private ArrayList<LikeRelationFragment> fragmentList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);

        myLikeTime = UserSP.getLong(getActivityContext(), UserSP.LIKE_RELATION_MY_LIKE + HereUser.getUserId(), 1);
        likeMeTime = UserSP.getLong(getActivityContext(), UserSP.LIKE_RELATION_LIKE_ME + HereUser.getUserId(), 1);
        likeEachotherTime = UserSP.getLong(getActivityContext(), UserSP.LIKE_RELATION_LIKE_EACHOTHER + HereUser.getUserId(), 1);
        int index = getTabIndexByTag(tabCurrent);
        initView(index);

        setListener();
    }

    private int getTabIndexByTag(String tag) {
        if (TextUtils.isEmpty(tag)) {
            return 0;
        }
        switch (tag) {
            case TAB_MUTUAL:
                return 1;

            case TAB_FROM_ME:
                return 0;

            case TAB_TO_ME:
                return 2;

        }
        return 0;
    }

    public void setListener() {
        backBtn.setOnClickListener(this);
        myLikeTitleLayout.setOnClickListener(this);
        likeEachotherTitleLayout.setOnClickListener(this);
        likeMeTitleLayout.setOnClickListener(this);
    }

    public void initView(int navigatorIndex) {
        LikeRelationFragment likeRelationFragment = new LikeRelationFragment();
        likeRelationFragment.setFragmentInfo(LikeRelationAdapter.TYPE_MY_LIKE, myLikeTime);
        fragmentList = new ArrayList<LikeRelationFragment>();
        fragmentList.add(likeRelationFragment);

        likeRelationFragment = new LikeRelationFragment();
        likeRelationFragment.setFragmentInfo(LikeRelationAdapter.TYPE_LIKE_EACHOTHER, likeEachotherTime);
        fragmentList.add(likeRelationFragment);

        likeRelationFragment = new LikeRelationFragment();
        likeRelationFragment.setFragmentInfo(LikeRelationAdapter.TYPE_LIKE_ME, likeMeTime);
        fragmentList.add(likeRelationFragment);

        LikeRelationFragmentAdapter likeRelationFragmentAdapter = new LikeRelationFragmentAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(likeRelationFragmentAdapter);
        viewPager.setCurrentItem(navigatorIndex);
        setNavigator(navigatorIndex);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setNavigator(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void setNavigator(int navigatorIndex) {
        switch (navigatorIndex) {
            case 0:
                myLikeTitleTv.setTextColor(getResources().getColor(R.color.blue));
                myLikeIndicator.setVisibility(View.VISIBLE);
                likeEachotherTitleTv.setTextColor(getResources().getColor(R.color.text_blue_1));
                likeEachotherIndicator.setVisibility(View.GONE);
                likeMeTitleTv.setTextColor(getResources().getColor(R.color.text_blue_1));
                likeMeIndicator.setVisibility(View.GONE);
                break;
            case 1:
                myLikeTitleTv.setTextColor(getResources().getColor(R.color.text_blue_1));
                myLikeIndicator.setVisibility(View.GONE);
                likeEachotherTitleTv.setTextColor(getResources().getColor(R.color.blue));
                likeEachotherIndicator.setVisibility(View.VISIBLE);
                likeMeTitleTv.setTextColor(getResources().getColor(R.color.text_blue_1));
                likeMeIndicator.setVisibility(View.GONE);

                RedDotManager.getInstance().clearDot(RedDot.LIKE_EACH_OTHER);
                break;
            case 2:
                myLikeTitleTv.setTextColor(getResources().getColor(R.color.text_blue_1));
                myLikeIndicator.setVisibility(View.GONE);
                likeEachotherTitleTv.setTextColor(getResources().getColor(R.color.text_blue_1));
                likeEachotherIndicator.setVisibility(View.GONE);
                likeMeTitleTv.setTextColor(getResources().getColor(R.color.blue));
                likeMeIndicator.setVisibility(View.VISIBLE);

                RedDotManager.getInstance().clearDot(RedDot.LIKE_ME);
                break;
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_like_title_layout:
                viewPager.setCurrentItem(0);
                fragmentList.get(0).scrollTop();
                break;
            case R.id.like_eacher_title_layout:
                viewPager.setCurrentItem(1);
                fragmentList.get(1).scrollTop();
                break;
            case R.id.like_me_title_layout:
                viewPager.setCurrentItem(2);
                fragmentList.get(2).scrollTop();
                break;
            case R.id.back_btn:
                LikeRelationActivity.this.finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserSP.putAndCommit(getActivityContext(), UserSP.LIKE_RELATION_MY_LIKE + HereUser.getUserId(), System.currentTimeMillis());
        UserSP.putAndCommit(getActivityContext(), UserSP.LIKE_RELATION_LIKE_ME + HereUser.getUserId(), System.currentTimeMillis());
        UserSP.putAndCommit(getActivityContext(), UserSP.LIKE_RELATION_LIKE_EACHOTHER + HereUser.getUserId(), System.currentTimeMillis());
    }
}
