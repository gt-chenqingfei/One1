package com.oneone.modules.main.message;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.api.constants.RedDot;
import com.oneone.event.EventRefreshMsgBubble;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.BaseMainFragment;
import com.oneone.modules.msg.adapter.MsgContentFragmentAdapter;
import com.oneone.modules.msg.fragment.InteractionFragment;
import com.oneone.modules.msg.fragment.TalkFragment;
import com.oneone.modules.reddot.RedDotManager;
import com.oneone.modules.user.UserSP;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author qingfei.chen
 * @since 2018/3/31.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@LayoutResource(R.layout.frag_msg)
public class MsgFragment extends BaseMainFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    @BindView(R.id.talk_title_layout)
    RelativeLayout talkTitleLayout;
    @BindView(R.id.talk_title_tv)
    TextView talkTitleTv;
    @BindView(R.id.talk_title_point_iv)
    ImageView talkTitlePointIv;
    @BindView(R.id.talk_indicator)
    View talkIndicator;
    @BindView(R.id.interaction_title_layout)
    RelativeLayout interactionTitleLayout;
    @BindView(R.id.interaction_title_tv)
    TextView interactionTitleTv;
    @BindView(R.id.interaction_title_point_iv)
    ImageView interactionTitlePointIv;
    @BindView(R.id.interaction_indicator)
    View interactionIndicator;

    @BindView(R.id.msg_view_pager)
    ViewPager viewPager;

    @BindView(R.id.frag_msg_test_view)
    View msgTitleView;

    private TalkFragment talkFragment;
    private InteractionFragment interactionFragment;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventRefreshMsgBubble bubbleEvent) {
        refreshBubble();
        RedDotManager.getInstance().fetchAllDot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        UserSP.getInstance().registerListener(getContext(), this);

        EventBus.getDefault().register(this);
        initView();


        refreshBubble();
//        talkFragment.setFirstMeetIncrease(DotManager.getInstance().getNewMeetDot());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UserSP.getInstance().unregisterListener(getContext(), this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String tab = intent.getStringExtra("childTabCurrent");
        if (TextUtils.isEmpty(tab)) {
            return;
        }
        if (viewPager == null) {
            return;
        }
        switch (tab) {
            case "conversation":
                viewPager.setCurrentItem(0);
                break;
            case "interaction":
                viewPager.setCurrentItem(1);
                break;
        }
    }

    public void refreshBubble() {
        int totalBubble = 0;
        if (talkFragment != null) {
            totalBubble += talkFragment.getBubbleCount();
        }
        if (interactionFragment != null) {
            totalBubble += interactionFragment.getBubbleCount();
        }
        totalBubble += (RedDotManager.getInstance().getNewMeetUnread() + RedDotManager.getInstance().getInteractionNotifyUnread() + RedDotManager.getInstance().getLikeEachOtherUnread());

        setBubble(totalBubble);
        setTitleBubble();
    }

    public void initView() {
        talkFragment = new TalkFragment();
        interactionFragment = new InteractionFragment();

        List<Fragment> fragList = new ArrayList<Fragment>();
        fragList.add(talkFragment);
        fragList.add(interactionFragment);
        MsgContentFragmentAdapter fragmentAdapter = new MsgContentFragmentAdapter(getActivity().getSupportFragmentManager(), fragList);
        viewPager.setAdapter(fragmentAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                EventBus.getDefault().post(new EventRefreshMsgBubble());
            }

            @Override
            public void onPageSelected(int position) {
                choosePager(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        talkTitleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(0);
            }
        });

        interactionTitleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
            }
        });
    }

    public void choosePager(int pos) {
        if (pos == 0) {
            talkIndicator.setVisibility(View.VISIBLE);
            talkTitleTv.setTextColor(getResources().getColor(R.color.blue));

            interactionIndicator.setVisibility(View.GONE);
            interactionTitleTv.setTextColor(getResources().getColor(R.color.text_blue_1));
        } else if (pos == 1) {
            talkIndicator.setVisibility(View.GONE);
            talkTitleTv.setTextColor(getResources().getColor(R.color.text_blue_1));

            interactionIndicator.setVisibility(View.VISIBLE);
            interactionTitleTv.setTextColor(getResources().getColor(R.color.blue));
        }
    }

    @Override
    public View getTitleView() {
        return msgTitleView;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        int totalBubble = 0;
        if (key.equals(RedDotManager.PREF_DOT + RedDot.NEW_MEET)) {
            if (talkFragment != null) {
                totalBubble += talkFragment.getBubbleCount();
            }
            if (interactionFragment != null) {
                totalBubble += interactionFragment.getBubbleCount();
            }
            totalBubble += (RedDotManager.getInstance().getNewMeetUnread() + RedDotManager.getInstance().getInteractionNotifyUnread() + RedDotManager.getInstance().getLikeEachOtherUnread());

            setBubble(totalBubble);

            talkFragment.setFirstMeetIncrease();
        } else if (key.equals(RedDotManager.PREF_DOT + RedDot.INTERACTION_NOTIFY)) {
            if (talkFragment != null) {
                totalBubble += talkFragment.getBubbleCount();
            }
            if (interactionFragment != null) {
                totalBubble += interactionFragment.getBubbleCount();
            }
            totalBubble += (RedDotManager.getInstance().getNewMeetUnread() + RedDotManager.getInstance().getInteractionNotifyUnread() + RedDotManager.getInstance().getLikeEachOtherUnread());

            setBubble(totalBubble);
        } else if (key.equals(RedDotManager.PREF_DOT + RedDot.LIKE_EACH_OTHER)) {
            if (talkFragment != null) {
                totalBubble += talkFragment.getBubbleCount();
            }
            if (interactionFragment != null) {
                totalBubble += interactionFragment.getBubbleCount();

                interactionFragment.showNoLike();
            }
            totalBubble += (RedDotManager.getInstance().getNewMeetUnread() + RedDotManager.getInstance().getInteractionNotifyUnread() + RedDotManager.getInstance().getLikeEachOtherUnread());

            setBubble(totalBubble);
        } else if (key.equals(RedDotManager.PREF_DOT + RedDot.LIKE_ME)) {
            if (interactionFragment != null) {
                interactionFragment.initLikeMe();

                interactionFragment.showNoLike();
            }
        }

        setTitleBubble();
    }

    public void setTitleBubble() {
        if (talkFragment != null) {
            int talkTitleBubble = talkFragment.getBubbleCount();

            if (talkTitleBubble > 0) {
                talkTitlePointIv.setVisibility(View.VISIBLE);
            } else {
                talkTitlePointIv.setVisibility(View.GONE);
            }
        } else {
            talkTitlePointIv.setVisibility(View.GONE);
        }

        if (interactionFragment != null) {
            int interactionTitleBubble = interactionFragment.getBubbleCount();
            interactionTitleBubble += (RedDotManager.getInstance().getNewMeetUnread() + RedDotManager.getInstance().getInteractionNotifyUnread() + RedDotManager.getInstance().getLikeEachOtherUnread());

            if (interactionTitleBubble > 0) {
                interactionTitlePointIv.setVisibility(View.VISIBLE);
            } else {
                interactionTitlePointIv.setVisibility(View.GONE);
            }
        } else {
            interactionTitlePointIv.setVisibility(View.GONE);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //相当于Fragment的onResume，为true时，Fragment已经可见
        } else {
            //相当于Fragment的onPause，为false时，Fragment不可见
        }
    }
}
