package com.oneone.modules.dogfood.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.oneone.BasePullListActivity;
import com.oneone.R;
import com.oneone.framework.ui.BaseRecyclerViewAdapter;
import com.oneone.framework.ui.BaseViewHolder;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;
import com.oneone.framework.ui.widget.SimplePullRecyclerView;
import com.oneone.modules.dogfood.adapter.DogFoodRecordAdapter;
import com.oneone.modules.dogfood.beans.CoinRecord;
import com.oneone.modules.dogfood.contract.CoinContract;
import com.oneone.modules.dogfood.presenter.CoinPresenter;
import com.oneone.widget.CustomGlobalFooter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.List;

import butterknife.BindView;

/**
 * Created by here on 18/5/2.
 */

@Route(path = "/dog_food/records")
@ToolbarResource(title = R.string.str_dog_food_record_title_text)
@LayoutResource(R.layout.activity_dog_food_record)
public class DogFoodRecordActivity extends BasePullListActivity<CoinRecord, CoinPresenter, CoinContract.View>
        implements BaseViewHolder.ItemClickListener<CoinRecord>, CoinContract.View {

    @BindView(R.id.dog_food_record_srv)
    SimplePullRecyclerView<CoinRecord> mSimpleRecyclerView;
    DogFoodRecordAdapter dogFoodRecordAdapter;

    private static final int PAGE_COUNT = 10;
    private int curIndex = 0;
    private boolean isLoadMore;// 除了第一次进入页面其余都是填充加载更多的数据
    private int mTotalCount;// 记录总数
    private int page = 1;// 页数
    private int pageCount;// 每页要加载多少数据，除了最后一页可能不是 10 条，其余都是 10 条

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.coinRecords(curIndex, PAGE_COUNT);
        mSimpleRecyclerView.getSmartRefreshLayout().setEnableRefresh(false);
    }

    @Override
    public CoinPresenter onCreatePresenter() {
        return new CoinPresenter();
    }

    @NonNull
    @Override
    public BaseRecyclerViewAdapter adapterToDisplay() {
        dogFoodRecordAdapter = new DogFoodRecordAdapter(this);
        return dogFoodRecordAdapter;
    }

    @NonNull
    @Override
    public SimplePullRecyclerView getDisplayView() {
        return mSimpleRecyclerView;
    }

    public static void startActivity(Context context) {
        Intent it = new Intent(context, DogFoodRecordActivity.class);
        context.startActivity(it);
    }

    @Override
    public void onCoinGetcoinbalance(int balance) {
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if (dogFoodRecordAdapter.getItemCount() == 0) {
            return;
        }
        if (dogFoodRecordAdapter.getItemCount() >= mTotalCount) {
            ((CustomGlobalFooter) refreshLayout.getRefreshFooter()).
                    setSpecifyFooterView(R.string.refresh_footer_nomore_data_record);
            mSimpleRecyclerView.getSmartRefreshLayout().finishLoadMore();
            return;
        }
        mPresenter.coinRecords(dogFoodRecordAdapter.getItemCount(), pageCount);
    }

    @Override
    public void onCoinRecords(List<CoinRecord> record, int count) {
        mTotalCount = count;
        page++;
        if (page * PAGE_COUNT <= count) {
            pageCount = PAGE_COUNT;
        } else {
            pageCount = count - ((page - 1) * PAGE_COUNT);
        }
        if (!isLoadMore) {
            isLoadMore = true;
            onRefreshCompleted(record);
        } else {
            onLoadMoreCompleted(record);
        }
    }

    @Override
    public void onItemClick(CoinRecord coinRecord, int id, int position) {
    }
}
