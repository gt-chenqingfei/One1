package com.oneone.modules.timeline.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.api.constants.TimeLineLikeType;
import com.oneone.api.constants.TimelineStatus;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.modules.timeline.bean.LikeType;
import com.oneone.modules.timeline.bean.TimeLine;
import com.oneone.modules.timeline.contract.TimeLineContract;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author qingfei.chen
 * @since 2018/5/30.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@LayoutResource(R.layout.view_timeline_bottom_like_view)
public class TimeLineBottomLikeView extends AbstractTimeLineView<TimeLine> {
    @BindView(R.id.view_timeline_bottom_like_view_like_types)
    LikeTypesView mLikeTypeView;
    @BindView(R.id.view_timeline_bottom_like_view_iv_praise_perform)
    ImageView mIvPraisePerform;
    @BindView(R.id.view_timeline_bottom_like_view_iv_comment)
    ImageView mIvComment;
    @BindView(R.id.view_timeline_bottom_like_view_tv_count)
    TextView mTvCount;
    TimeLineContract.Presenter mPresenter;
    TimeLine timeLine;
    int position;
    private int[] typeDrawableIds = {R.drawable.ic_btn_praise,
            R.drawable.ic_like_type_smile, R.drawable.ic_like_type_surprise,
            R.drawable.ic_like_type_love_it, R.drawable.ic_like_type_like_it};

    public TimeLineBottomLikeView(Context context) {
        super(context);
    }

    public TimeLineBottomLikeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onViewCreated() {

    }

    @Override
    public void bind(TimeLine timeLine, TimeLineContract.Presenter presenter, int position) {
        if (timeLine == null) {
            return;
        }
        boolean isGone = timeLine.getStatus() == TimelineStatus.STATUS_SENDING
                || timeLine.getStatus() == TimelineStatus.STATUS_SEND_FAILED;
        this.setVisibility(isGone ? View.GONE : View.VISIBLE);
        this.position = position;
        this.timeLine = timeLine;
        this.mPresenter = presenter;

        int likeType = timeLine.getMyLikeType();
        if (timeLine.getMyLikeType() > 4 || timeLine.getMyLikeType() < 0) {
            likeType = 0;
        }
        mIvPraisePerform.setImageResource(typeDrawableIds[likeType]);

        List<LikeType> afterFilterList = bindLikeTotalCountAndFilterList(timeLine.getLikeTypes());
        mLikeTypeView.bind(afterFilterList);
    }

    @OnClick(R.id.view_timeline_bottom_like_view_iv_praise_perform)
    public void onPraiseClick(View view) {
        new LikePopupMenuView(getContext()).show(mIvComment, new LikePopupMenuView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int likeType) {
                mPresenter.like(timeLine, likeType, position);
            }
        });
    }

    private List<LikeType> bindLikeTotalCountAndFilterList(List<LikeType> likeTypes) {

        List<LikeType> afterFilterList = new ArrayList<>();
        int total = 0;
        if (likeTypes == null || likeTypes.isEmpty()) {
            mTvCount.setVisibility(View.INVISIBLE);
            mLikeTypeView.setVisibility(View.INVISIBLE);
            return afterFilterList;
        }

        for (LikeType likeType : likeTypes) {
            if (likeType.getLikeType() == TimeLineLikeType.NONE || likeType.getLikeTypeCount() <= 0) {
                continue;
            }
            afterFilterList.add(likeType);
            total += likeType.getLikeTypeCount();
        }
        mTvCount.setVisibility(total > 0 ? View.VISIBLE : View.GONE);
        mLikeTypeView.setVisibility(total > 0 ? View.VISIBLE : View.GONE);
        mTvCount.setText(total + "");
        return afterFilterList;
    }

}
