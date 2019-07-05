package com.oneone.modules.support.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.framework.ui.BaseListActivity;
import com.oneone.framework.ui.BaseRecyclerViewAdapter;
import com.oneone.framework.ui.BaseViewHolder;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;
import com.oneone.framework.ui.widget.SimpleRecyclerView;
import com.oneone.modules.support.adapter.CitySelectAdapter;
import com.oneone.modules.support.bean.City;
import com.oneone.modules.support.contract.CitySelectContract;
import com.oneone.modules.support.presenter.CitySelectPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author qingfei.chen
 * @since 2018/4/9.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */

@ToolbarResource(title = R.string.title_city_select)
@LayoutResource(R.layout.activity_city_select)
public class CitySelectActivity extends BaseListActivity<City, CitySelectPresenter,
        CitySelectContract.View> implements BaseViewHolder.ItemClickListener<City>,
        CitySelectContract.View {

    public static void startActivity4Result(Activity context, List<City> cities, City cityParent) {
        Intent intent = new Intent(context, CitySelectActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_CITY_LIST, (ArrayList<? extends Parcelable>) cities);
        intent.putExtra(EXTRA_CITY_PARENT, cityParent);
        context.startActivityForResult(intent, REQ_CODE_CITY_SELECT);
    }

    public static void startActivity4Result(Activity context) {
        Intent intent = new Intent(context, CitySelectActivity.class);
        context.startActivityForResult(intent, REQ_CODE_CITY_SELECT);
    }

    public static void startActivity4Result(Activity context,int reqCode) {
        Intent intent = new Intent(context, CitySelectActivity.class);
        context.startActivityForResult(intent, reqCode);
    }

    public static void startActivity4Result(Fragment context, int reqCode) {
        Intent intent = new Intent(context.getActivity(), CitySelectActivity.class);
        context.startActivityForResult(intent, reqCode);
    }

    public static final String EXTRA_CITY_LIST = "city_list";
    public static final String EXTRA_CITY = "city";
    public static final String EXTRA_CITY_PARENT = "city_parent";
    public static final int REQ_CODE_CITY_SELECT = 100;

    @BindView(R.id.activity_city_select_srv)
    SimpleRecyclerView<City> mSimpleRecyclerView;

    private List<City> extraCityList = null;
    private City extraCity;

    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);
        if (intent != null) {
            extraCityList = intent.getParcelableArrayListExtra(EXTRA_CITY_LIST);
            extraCity = intent.getParcelableExtra(EXTRA_CITY_PARENT);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView rightMenu = setRightTextMenu(R.string.str_completed);
        rightMenu.setEnabled(false);
    }

    @Override
    public CitySelectPresenter onCreatePresenter() {
        return new CitySelectPresenter();
    }

    @NonNull
    @Override
    public BaseRecyclerViewAdapter<City> adapterToDisplay() {
        return new CitySelectAdapter(this);
    }

    @NonNull
    @Override
    public SimpleRecyclerView<City> getDisplayView() {
        return mSimpleRecyclerView;
    }

    @Override
    public List<City> doLoad() {
        return mPresenter.getCityList(extraCityList);
    }

    @Override
    public void onItemClick(City city, int id, int position) {

        List<City> cityList = mPresenter.getCityListByParentId(city.getId());
        boolean hasChild = mPresenter.hasChild(cityList, city.getId());
        if (hasChild) {
            startActivity4Result(this, cityList, city);
            return;
        }
        ((CitySelectAdapter) getAdapter()).setSelected(city);
        getRightTextMenu().setEnabled(true);
    }

    @Override
    public void onRightTextMenuClick(View view) {
        super.onRightTextMenuClick(view);
        Intent it = new Intent();
        it.putExtra(EXTRA_CITY, ((CitySelectAdapter) getAdapter()).getSelected());
        it.putExtra(EXTRA_CITY_PARENT, extraCity);
        setResult(RESULT_OK, it);
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_CITY_SELECT) {
            if (data != null) {
                setResult(RESULT_OK, data);
            } else {
                setResult(RESULT_CANCELED);
            }
            this.finish();
        }
    }

}
