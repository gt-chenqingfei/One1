package com.oneone.support.share;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.framework.ui.BaseView;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.utils.ScreenUtil;
import com.oneone.modules.profile.dialog.ShareDialog;
import com.oneone.utils.ShareUtil;

import butterknife.BindView;

/**
 * @author qingfei.chen
 * @since 2018/4/25.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@LayoutResource(R.layout.view_share_box)
public class ShareBox extends BaseView implements View.OnClickListener, ShareDialog.OnShareCompleteListener {
    public static final int FRIEND_COUNT = 4;

    @BindView(R.id.activity_singles_invite_ll_container)
    LinearLayout mLlContainer;

    private int mShareCounter;
    private int iconRes;
    private String text;
    private SparseArray<InviteItemHolder> mSparseArray;
    private String mGroupID;

    public ShareBox(Context context) {
        super(context);
    }

    public ShareBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setGroupID(String groupID) {
        this.mGroupID = groupID;
    }

    @Override
    public void onViewCreated() {
        mSparseArray = new SparseArray<InviteItemHolder>();
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                getAttrs(), R.styleable.ShareBox, 0, 0);

        text = a.getString(R.styleable.ShareBox_sb_text);
        iconRes = a.getResourceId(R.styleable.ShareBox_sb_icon, 0);

        initInviteItem();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.item_share_box_invite_rl) {
            if (v.getTag() == null) {
                return;
            }
            Integer pos = (Integer) v.getTag();
            performShare(pos);
        }
    }

    private void performShare(int pos) {
        mShareCounter = pos;
        InviteItemHolder holder = mSparseArray.get(pos);

        if (!holder.item.isSelected()) {
            // new ShareUtil(getContext(), mGroupID, this).show();
            ShareDialog mShareDialog = new ShareDialog(mContext, mGroupID, this);
            mShareDialog.show();
        }
    }

    public void setIcon(int iconRes) {
        this.iconRes = iconRes;
    }

    public void setText(String text) {
        this.text = text;
    }

    private void initInviteItem() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (int i = 0; i < FRIEND_COUNT; i++) {
            InviteItemHolder itemHolder = new InviteItemHolder();
            View view = inflater.inflate(R.layout.item_share_box, null);
            itemHolder.item = view.findViewById(R.id.item_share_box_invite_rl);
            itemHolder.ivIcon = itemHolder.item.findViewById(R.id.item_share_box_invite_iv_friend_icon);
            itemHolder.tvText = itemHolder.item.findViewById(R.id.item_share_box_invite_tv_friend_display);

            itemHolder.ivIcon.setImageResource(iconRes);
            itemHolder.tvText.setText(text);
            itemHolder.item.setTag(i);
            itemHolder.item.setSelected(false);
            itemHolder.item.setOnClickListener(this);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) itemHolder.item.getLayoutParams();
            if (i == 0) {
                params.leftMargin = ScreenUtil.dip2px(38);
            } else {
                params.leftMargin = ScreenUtil.dip2px(10);
            }
            if (i == FRIEND_COUNT - 1) {
                params.rightMargin = ScreenUtil.dip2px(38);
            }
            mLlContainer.addView(view);
            mSparseArray.put(i, itemHolder);
        }
    }

    /**
     * 分享完成之后回调的方法
     */
    @Override
    public void onShareCompleteListener() {
        InviteItemHolder holder = mSparseArray.get(mShareCounter);
        holder.tvText.setText(R.string.str_have_invite);
        holder.item.setSelected(true);
    }

    class InviteItemHolder {
        View item;
        ImageView ivIcon;
        TextView tvText;
    }
}
