package com.oneone.modules.timeline.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.oneone.R;
import com.oneone.api.constants.TimeLineLikeType;
import com.oneone.framework.ui.utils.ScreenUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author qingfei.chen
 * @since 2018/6/20.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class LikePopupMenuView extends PopupWindow implements
        PopupWindow.OnDismissListener, View.OnClickListener {

    private Context mContext;
    private OnMenuItemClickListener mOnMenuItemClickListener;

    public LikePopupMenuView(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    @Override
    public void onDismiss() {
    }

    private void initView() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.popup_timeline_like, null);

        setOnDismissListener(this);
        this.setContentView(view);
        this.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
        this.setBackgroundDrawable(dw);
        this.setFocusable(true);
//        mUnBinder = ButterKnife.bind(view);

        view.findViewById(R.id.popup_timeline_like_iv_smile).setOnClickListener(this);
        view.findViewById(R.id.popup_timeline_like_iv_surprise).setOnClickListener(this);
        view.findViewById(R.id.popup_timeline_like_iv_like_it).setOnClickListener(this);
        view.findViewById(R.id.popup_timeline_like_iv_love_it).setOnClickListener(this);
    }

    public void show(View anchor, OnMenuItemClickListener listener) {
        this.showAsDropDown(anchor, 0,
                ScreenUtil.dip2px(-90), Gravity.BOTTOM);
        this.mOnMenuItemClickListener = listener;
    }


    public void onSmileClick(View view) {
        this.dismiss();
        if (mOnMenuItemClickListener != null) {
            mOnMenuItemClickListener.onMenuItemClick(TimeLineLikeType.SMILE);
        }
    }


    public void onSurpriseClick(View view) {
        this.dismiss();
        if (mOnMenuItemClickListener != null) {
            mOnMenuItemClickListener.onMenuItemClick(TimeLineLikeType.SURPRISE);
        }
    }


    public void onLikeItClick(View view) {
        this.dismiss();
        if (mOnMenuItemClickListener != null) {
            mOnMenuItemClickListener.onMenuItemClick(TimeLineLikeType.LIKE_IT);
        }
    }

    public void onLoveItClick(View view) {
        this.dismiss();
        if (mOnMenuItemClickListener != null) {
            mOnMenuItemClickListener.onMenuItemClick(TimeLineLikeType.LOVE_IT);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popup_timeline_like_iv_like_it:
                onLikeItClick(v);
                break;

            case R.id.popup_timeline_like_iv_love_it:
                onLoveItClick(v);
                break;

            case R.id.popup_timeline_like_iv_surprise:
                onSurpriseClick(v);
                break;

            case R.id.popup_timeline_like_iv_smile:
                onSmileClick(v);
                break;
        }
    }

    public interface OnMenuItemClickListener {
        void onMenuItemClick(int likeType);
    }
}
