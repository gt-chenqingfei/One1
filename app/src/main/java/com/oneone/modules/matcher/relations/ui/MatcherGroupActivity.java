package com.oneone.modules.matcher.relations.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.oneone.R;
import com.oneone.api.constants.RedDot;
import com.oneone.framework.ui.BaseListActivity;
import com.oneone.framework.ui.BaseRecyclerViewAdapter;
import com.oneone.framework.ui.BaseViewHolder.ItemClickListener;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;
import com.oneone.framework.ui.widget.SimpleRecyclerView;
import com.oneone.modules.main.me.contract.MyMatcherContract;
import com.oneone.modules.main.me.presenter.MyMatcherPresenter;
import com.oneone.modules.matcher.relations.bean.MatcherInfo;
import com.oneone.modules.matcher.relations.ui.adapter.MatcherAdapter;
import com.oneone.modules.reddot.RedDotManager;
import com.oneone.support.share.Callback;
import com.oneone.support.share.ShareBase;
import com.oneone.support.share.ShareParams;
import com.oneone.support.share.ShareParamsUtil;
import com.oneone.support.share.Wechat;

import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import cn.sharesdk.framework.Platform;


/**
 * @author qingfei.chen
 * @since 2018/4/24.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@Route(path = "/my_matchers/list")
@ToolbarResource(navigationIcon = R.drawable.ic_btn_back_light, title = R.string.str_my_matcher_group_title_text, background = R.color.blue)
@LayoutResource(R.layout.activity_my_matcher_group)
public class MatcherGroupActivity extends
        BaseListActivity<MatcherInfo, MyMatcherPresenter, MyMatcherContract.View>
        implements MyMatcherContract.View, ItemClickListener<MatcherInfo>, Callback {
    private static final String EXTRA_COUNT = "extra_count";

    public static void startActivity(Context context, int count) {
        Intent intent = new Intent(context, MatcherGroupActivity.class);
        intent.putExtra(EXTRA_COUNT, count);
        context.startActivity(intent);
    }

    @BindView(R.id.activity_my_matcher_group_recycler_view)
    SimpleRecyclerView<MatcherInfo> simpleRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int count = getIntent().getIntExtra(EXTRA_COUNT, 0);
        if (count <= 0) {
            this.finish();
            MatcherInviteActivity.startActivity(this);
            return;
        }
        mPresenter.getMyMatcher();
        getTitleView().setTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public MyMatcherPresenter onCreatePresenter() {
        return new MyMatcherPresenter();
    }

    @Override
    protected boolean autoLoad() {
        return false;
    }

    @NonNull
    @Override
    public BaseRecyclerViewAdapter<MatcherInfo> adapterToDisplay() {
        return new MatcherAdapter(this);
    }

    @NonNull
    @Override
    public SimpleRecyclerView<MatcherInfo> getDisplayView() {
        return simpleRecyclerView;
    }

    @Override
    public void onItemClick(MatcherInfo matcherInfo, int id, int position) {
        switch (id) {
            case R.id.item_matcher_footer_btn:
                MatcherInviteActivity.startActivity(this);
                break;
            case R.id.invite_ta_to_write_btn:
                ShareBase shareBase = new Wechat(this, this);
                ShareParams params = ShareParamsUtil.getParam4SelfMatcherProfile(this);
                shareBase.share(params);
                break;
        }
    }

    @Override
    public void onMyMatcherGet(List<MatcherInfo> matcherInfos) {
        RedDotManager.getInstance().clearDot(RedDot.NEW_MATCHERS);
        if (matcherInfos == null) {
            matcherInfos = new ArrayList<>();
        }

        if (matcherInfos.isEmpty()) {
            this.finish();
            MatcherInviteActivity.startActivity(this);
            return;
        }

        matcherInfos.add(0, new MatcherInfo());
        matcherInfos.add(new MatcherInfo());
        onLoadCompleted(matcherInfos);
    }

    @Override
    public void onComplete(ShareParams shareParams, Platform platform, int i, HashMap<String, Object> hashMap) {
        LoggerFactory.getLogger("MatcherGroupActivity").info("invite success");
    }

    @Override
    public void onError(ShareParams shareParams, Platform platform, int i, Throwable throwable) {
        LoggerFactory.getLogger("MatcherGroupActivity").error("onError", throwable);
    }

    @Override
    public void onCancel(ShareParams shareParams, Platform platform, int i) {
        LoggerFactory.getLogger("MatcherGroupActivity").error("onCancel");
    }
}
