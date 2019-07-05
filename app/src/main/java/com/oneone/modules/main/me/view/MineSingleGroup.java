package com.oneone.modules.main.me.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.framework.ui.BaseView;
import com.oneone.framework.ui.annotation.LayoutResource;

import butterknife.BindView;

/**
 * Created by here on 18/4/11.
 */

@LayoutResource(R.layout.view_mine_single_group_view)
public class MineSingleGroup extends BaseView {
    @BindView(R.id.person_count_tv_1)
    TextView personCountTv;
    @BindView(R.id.my_single_user_photo_layout)
    LinearLayout singleUserPhotoLl;
    @BindView(R.id.share_more_single_tv)
    TextView shareMoreSingleTv;
    @BindView(R.id.person_count_tv_dot)
    TextView mTvSingleDot;

    private MineSingleGroupOnClickListener clickListener;

    public void setListener(MineSingleGroup.MineSingleGroupOnClickListener listener) {
        this.clickListener = listener;
    }

    public interface MineSingleGroupOnClickListener {
        void clickGroupItem();
    }

    public MineSingleGroup(Context context) {
        super(context);
    }

    public MineSingleGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onViewCreated() {

    }

    public void setPersionCount(int count) {
        this.personCountTv.setText(count + "");
    }

    public void setUnreadCount(int unreadCount) {
        if (unreadCount > 0) {
            this.mTvSingleDot.setText("+" + unreadCount);
            this.mTvSingleDot.setVisibility(View.VISIBLE);
        } else {
            this.mTvSingleDot.setVisibility(View.GONE);
        }
    }
}
