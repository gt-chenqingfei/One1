package com.oneone.modules.main.me.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.oneone.R;
import com.oneone.framework.ui.BaseView;
import com.oneone.framework.ui.annotation.LayoutResource;

import butterknife.BindView;

/**
 * Created by here on 18/4/11.
 */

@LayoutResource(R.layout.view_mine_matcher_open_single_view)
public class MineMatcherOpen extends BaseView {
    @BindView(R.id.open_single_btn)
    Button openSingleBtn;

    @BindView(R.id.close_btn)
    Button closeBtn;

    private MineMatcherOpenSummaryOnClickListener listener;

    public void setListener(MineMatcherOpenSummaryOnClickListener listener) {
        this.listener = listener;
    }

    public interface MineMatcherOpenSummaryOnClickListener {
        void onCloseBtnClick();

        void onOpenSingleBtnClick();
    }

    public MineMatcherOpen(Context context) {
        super(context);
    }

    public MineMatcherOpen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onViewCreated() {
        openSingleBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onOpenSingleBtnClick();
            }
        });

        closeBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCloseBtnClick();
            }
        });
    }
}
