package com.oneone.modules.support.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.framework.ui.BaseListActivity;
import com.oneone.framework.ui.BaseRecyclerViewAdapter;
import com.oneone.framework.ui.BaseViewHolder;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;
import com.oneone.framework.ui.widget.SimpleRecyclerView;
import com.oneone.modules.support.adapter.OccupationSelectAdapter;
import com.oneone.modules.support.bean.Occupation;
import com.oneone.modules.support.contract.OccupationSelectContract;
import com.oneone.modules.support.presenter.OccupationSelectPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author qingfei.chen
 * @since 2018/4/9.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */

@ToolbarResource(title = R.string.title_occupation_select)
@LayoutResource(R.layout.activity_occupation_select)
public class OccupationSelectActivity extends BaseListActivity<Occupation, OccupationSelectPresenter,
        OccupationSelectContract.View> implements BaseViewHolder.ItemClickListener<Occupation>,
        OccupationSelectContract.View {

    public static void startActivity4Result(Activity context, List<Occupation> cities, Occupation occupationParent) {
        Intent intent = new Intent(context, OccupationSelectActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_OCCUPATION_LIST, (ArrayList<? extends Parcelable>) cities);
        intent.putExtra(EXTRA_OCCUPATION_PARENT, occupationParent);
        context.startActivityForResult(intent, REQ_CODE_OCCUPATION_SELECT);
    }

    public static void startActivity4Result(Activity context) {
        Intent intent = new Intent(context, OccupationSelectActivity.class);
        context.startActivityForResult(intent, REQ_CODE_OCCUPATION_SELECT);
    }

    public static final String EXTRA_OCCUPATION_LIST = "occupation_list";
    public static final String EXTRA_OCCUPATION = "occupation";
    public static final String EXTRA_OCCUPATION_PARENT = "occupation_parent";
    public static final int REQ_CODE_OCCUPATION_SELECT = 102;

    @BindView(R.id.activity_city_select_srv)
    SimpleRecyclerView<Occupation> mSimpleRecyclerView;

    private List<Occupation> extraOccupationList = null;
    private Occupation extraOccupationParent;

    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);
        if (intent != null) {
            extraOccupationList = intent.getParcelableArrayListExtra(EXTRA_OCCUPATION_LIST);
            extraOccupationParent = intent.getParcelableExtra(EXTRA_OCCUPATION_PARENT);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView rightMenu = setRightTextMenu(R.string.str_completed);
        rightMenu.setEnabled(false);
    }

    @Override
    public OccupationSelectPresenter onCreatePresenter() {
        return new OccupationSelectPresenter();
    }

    @NonNull
    @Override
    public BaseRecyclerViewAdapter<Occupation> adapterToDisplay() {
        return new OccupationSelectAdapter(this);
    }

    @NonNull
    @Override
    public SimpleRecyclerView<Occupation> getDisplayView() {
        return mSimpleRecyclerView;
    }

    @Override
    public List<Occupation> doLoad() {
        return mPresenter.getCityList(extraOccupationList);
    }

    @Override
    public void onItemClick(Occupation occupation, int id, int position) {

        List<Occupation> occupationList = mPresenter.getCityListByParentId(occupation.getCode());
        boolean hasChild = mPresenter.hasChild(occupationList, occupation.getCode());
        if (hasChild) {
            startActivity4Result(this, occupationList, occupation);
            return;
        }
        ((OccupationSelectAdapter) getAdapter()).setSelected(occupation);
        getRightTextMenu().setEnabled(true);
    }

    @Override
    public void onRightTextMenuClick(View view) {
        super.onRightTextMenuClick(view);
        Intent it = new Intent();
        it.putExtra(EXTRA_OCCUPATION, ((OccupationSelectAdapter) getAdapter()).getSelected());
        it.putExtra(EXTRA_OCCUPATION_PARENT, extraOccupationParent);
        setResult(RESULT_OK, it);
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_OCCUPATION_SELECT) {
            if (data != null) {
                setResult(RESULT_OK, data);
            } else {
                setResult(RESULT_CANCELED);
            }
            this.finish();
        }
    }

}
