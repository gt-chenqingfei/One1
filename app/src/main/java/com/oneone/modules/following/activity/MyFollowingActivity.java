package com.oneone.modules.following.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.oneone.BaseActivity;
import com.oneone.R;
import com.oneone.api.constants.Role;
import com.oneone.event.EventMainTabSelection;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.modules.following.adapter.MyFollowingAdapter;
import com.oneone.modules.following.adapter.MyFollowingFragmentAdapter;
import com.oneone.modules.following.beans.FollowListItem;
import com.oneone.modules.following.contract.FollowingContract;
import com.oneone.modules.following.fragment.MyFollowingFragment;
import com.oneone.modules.following.presenter.FollowingPresenter;
import com.oneone.modules.timeline.activity.TimeLinePostActivity;
import com.oneone.modules.user.HereUser;
import com.oneone.utils.ImageHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by here on 18/4/25.
 */

@Route(path = "/follow/tab")
@LayoutResource(R.layout.activity_my_following)
public class MyFollowingActivity extends BaseActivity<FollowingPresenter, FollowingContract.View> implements FollowingContract.View {

    public static final String EXTRA_FOLLOW_STATUS = "followStatus";
    public static final String STATUS_FOLLOWING = "following";
    public static final String STATUS_FOLLOWER = "follower";

    public static void startActivity(Context context, boolean isFollower) {
        String followStatus = isFollower ? STATUS_FOLLOWING : STATUS_FOLLOWER;
        ARouter.getInstance().build("/follow/tab").withString(EXTRA_FOLLOW_STATUS,followStatus).navigation(context);
    }

    @BindView(R.id.back_btn)
    ImageButton backBtn;
    @BindView(R.id.following_view_pager)
    ViewPager viewPager;
    @BindView(R.id.left_navigator_layout)
    RelativeLayout leftNavigatorLayout;
    @BindView(R.id.right_navigator_layout)
    RelativeLayout rightNavigatorLayout;
    @BindView(R.id.left_navigator_tv)
    TextView leftNavigatorTv;
    @BindView(R.id.left_navigator)
    View leftNavigator;
    @BindView(R.id.right_navigator_tv)
    TextView rightNavigatorTv;
    @BindView(R.id.right_navigator)
    View rightNavigator;

    @BindView(R.id.empty_layout_left)
    LinearLayout emptyLayoutLeft;
    @BindView(R.id.empty_tv_left)
    TextView emptyTvLeft;
    @BindView(R.id.empty_btn_left)
    Button emptyBtnLeft;
    @BindView(R.id.empty_gif_iv_left)
    ImageView emptyIvLeft;
    @BindView(R.id.empty_layout_right)
    LinearLayout emptyLayoutRight;
    @BindView(R.id.empty_tv_right)
    TextView emptyTvRight;
    @BindView(R.id.empty_btn_right)
    Button emptyBtnRight;
    @BindView(R.id.empty_gif_iv_right)
    ImageView emptyIvRight;

    private List<MyFollowingFragment> fragList;
    @Autowired
    String followStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        initView();
    }

    public void initView() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyFollowingActivity.this.finish();
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                titleSelected(position);
                fragList.get(position).loadRefresh();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        fragList = new ArrayList<MyFollowingFragment>();
        MyFollowingFragment frag = new MyFollowingFragment();
        frag.setFragmentContent(MyFollowingAdapter.TYPE_MY_FOLLOW, new MyFollowingFragment.MyFollowingFragmentListener() {
            @Override
            public void showEmpty(boolean isEmpty) {
                if (isEmpty) {
                    if (viewPager.getCurrentItem() == 1) {
                        emptyLayoutLeft.setVisibility(View.GONE);
                        emptyLayoutRight.setVisibility(View.VISIBLE);
                    } else {
                        emptyLayoutRight.setVisibility(View.GONE);
                        emptyLayoutLeft.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        fragList.add(frag);
        frag = new MyFollowingFragment();
        frag.setFragmentContent(MyFollowingAdapter.TYPE_MY_ATTENTION, new MyFollowingFragment.MyFollowingFragmentListener() {
            @Override
            public void showEmpty(boolean isEmpty) {
                System.out.println("EMPTY == > " + viewPager.getCurrentItem());
                if (isEmpty) {
                    if (viewPager.getCurrentItem() == 1) {
                        emptyLayoutLeft.setVisibility(View.GONE);
                        emptyLayoutRight.setVisibility(View.VISIBLE);
                    } else {
                        emptyLayoutRight.setVisibility(View.GONE);
                        emptyLayoutLeft.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        fragList.add(frag);

        MyFollowingFragmentAdapter myFollowingFragmentAdapter = new MyFollowingFragmentAdapter(getSupportFragmentManager(), fragList);
        viewPager.setAdapter(myFollowingFragmentAdapter);

        if(followStatus != null){
            switch (followStatus){
                case STATUS_FOLLOWING:
                    titleSelected(0);
                    viewPager.setCurrentItem(0);
                    break;
                case STATUS_FOLLOWER:
                    titleSelected(1);
                    viewPager.setCurrentItem(1);
                    break;
            }
        }
        leftNavigatorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                fragList.get(0).loadRefresh();
                titleSelected(0);
                viewPager.setCurrentItem(0);
            }
        });
        rightNavigatorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                fragList.get(1).loadRefresh();
                titleSelected(1);
                viewPager.setCurrentItem(1);
            }
        });

        ImageHelper.displayGif(R.drawable.search_gif, emptyIvLeft);
        emptyTvLeft.setText(getResources().getString(R.string.str_my_following_left_empty_str));
        if (HereUser.getInstance().getUserInfo().getRole() == Role.SINGLE) {
            emptyBtnLeft.setText(getResources().getString(R.string.str_my_following_left_empty_btn_str));
            emptyBtnLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new EventMainTabSelection(EventMainTabSelection.TAB_EXPLORE));
                    MyFollowingActivity.this.finish();
                }
            });
        } else {
            emptyBtnLeft.setVisibility(View.GONE);
        }

        ImageHelper.displayGif(R.drawable.take_pic_gif, emptyIvRight);
        emptyTvRight.setText(getResources().getString(R.string.str_my_following_right_empty_str));
        if (HereUser.getInstance().getUserInfo().getRole() == Role.SINGLE) {
            emptyBtnRight.setText(getResources().getString(R.string.str_my_following_right_empty_btn_str));
            emptyBtnRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new EventMainTabSelection(EventMainTabSelection.TAB_TIMELINE));
                    TimeLinePostActivity.startPostImageTextActivity(MyFollowingActivity.this);
                    MyFollowingActivity.this.finish();
                }
            });
        }
    }

    public void titleSelected(int pos) {
        if (pos == 0) {
            leftNavigator.setVisibility(View.VISIBLE);
            leftNavigatorTv.setTextColor(getResources().getColor(R.color.blue));
            rightNavigator.setVisibility(View.GONE);
            rightNavigatorTv.setTextColor(getResources().getColor(R.color.text_blue_1));
        } else {
            rightNavigator.setVisibility(View.VISIBLE);
            rightNavigatorTv.setTextColor(getResources().getColor(R.color.blue));
            leftNavigator.setVisibility(View.GONE);
            leftNavigatorTv.setTextColor(getResources().getColor(R.color.text_blue_1));
        }
    }

    @Override
    public void onFollowingFollow(Integer followStatus) {

    }

    @Override
    public void onFollowingUnfollow(Integer followStatus) {

    }

    @Override
    public void onFollowingFollowers(boolean isLoadMore, int count, List<FollowListItem> list) {

    }

    @Override
    public void onFollowingFollowings(boolean isLoadMore, int count, List<FollowListItem> list) {

    }


}
