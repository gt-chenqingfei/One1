package com.oneone.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oneone.R;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.internal.InternalAbstract;

/**
 * 自定义全局刷新 Footer
 * <p>
 * Created by ZhaiDongyang on 2018/6/28
 */
public class CustomGlobalFooter extends InternalAbstract implements RefreshFooter {

    private RelativeLayout mRelativeLayout;
    private Context mContext;

    public CustomGlobalFooter(Context context) {
        this(context, null);
    }

    public CustomGlobalFooter(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomGlobalFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_refresh_footer, this);
        mRelativeLayout = view.findViewById(R.id.layout_container);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mRelativeLayout.getLayoutParams();
        params.height = 200;
        mRelativeLayout.setLayoutParams(params);
    }

    @Override
    public boolean setNoMoreData(boolean noMoreData) {
        return true;
    }

    /**
     * 没有数据时每种页面的底部显示文字不同，但是布局相同，传入不同的文字提示
     *
     * @param string 对应文字提示
     */
    public void setSpecifyFooterView(int string) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_refresh_footer_timeline, null);
        TextView textView = view.findViewById(R.id.tv_refresh_footer_hint);
        textView.setText(mContext.getResources().getString(string));
        mRelativeLayout.removeAllViews();
        mRelativeLayout.addView(view);
    }

    /**
     * 在下拉刷新后设置，设置为加载中
     */
    public void setFooterLoadingView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_refresh_footer_loading, null);
        mRelativeLayout.removeAllViews();
        mRelativeLayout.addView(view);
    }
}
