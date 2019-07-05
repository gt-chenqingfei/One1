package com.oneone.modules.support.presenter;

import com.oneone.BasePresenter;
import com.oneone.modules.support.bean.City;
import com.oneone.modules.support.contract.CitySelectContract;
import com.oneone.modules.support.model.SupportModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qingfei.chen
 * @since 2018/4/9.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class CitySelectPresenter extends BasePresenter<CitySelectContract.View>
        implements CitySelectContract.Presenter {

    private SupportModel mSupportModel;
    private List<City> mCityList = null;

    @Override
    public void onAttachView(CitySelectContract.View view) {
        super.onAttachView(view);
        mSupportModel = new SupportModel(view.getActivityContext());
    }

    @Override
    public List<City> getCityList(List<City> extraList) {
        if (extraList != null) {
            return extraList;
        }
        if (mCityList == null) {
            mCityList = mSupportModel.getCityList();
        }
        return getCityListByParentId(0);
    }

    @Override
    public List<City> getCityListByParentId(int parentId) {
        if (mCityList == null || mCityList.isEmpty()) {
            return null;
        }
        List<City> result = new ArrayList<>();
        for (City city : mCityList) {
            if (city.getParentId() == parentId) {
                result.add(city);
            }
        }
        return result;
    }

    @Override
    public boolean hasChild(List<City> cities, int parentId) {
        if (cities == null || cities.isEmpty()) {
            return false;
        }

        for (City city : cities) {
            if (city.getParentId() == parentId) {
                return true;
            }
        }
        return false;
    }


}
