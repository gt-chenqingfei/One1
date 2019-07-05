package com.oneone.modules.support.presenter;

import com.oneone.BasePresenter;
import com.oneone.modules.support.bean.City;
import com.oneone.modules.support.bean.Occupation;
import com.oneone.modules.support.contract.CitySelectContract;
import com.oneone.modules.support.contract.OccupationSelectContract;
import com.oneone.modules.support.model.SupportModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qingfei.chen
 * @since 2018/4/9.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class OccupationSelectPresenter extends BasePresenter<OccupationSelectContract.View>
        implements OccupationSelectContract.Presenter {

    private SupportModel mSupportModel;
    private List<Occupation> mCityList = null;

    @Override
    public void onAttachView(OccupationSelectContract.View view) {
        super.onAttachView(view);
        mSupportModel = new SupportModel(view.getActivityContext());
    }

    @Override
    public List<Occupation> getCityList(List<Occupation> extraList) {
        if (extraList != null) {
            return extraList;
        }
        if (mCityList == null) {
            mCityList = mSupportModel.getOccupationList();
        }
        return getCityListByParentId("0");
    }

    @Override
    public List<Occupation> getCityListByParentId(String parentId) {
        if (mCityList == null || mCityList.isEmpty()) {
            return null;
        }
        List<Occupation> result = new ArrayList<>();
        for (Occupation city : mCityList) {
            if (city.getParentCode().equals(parentId)) {
                result.add(city);
            }
        }
        return result;
    }

    @Override
    public boolean hasChild(List<Occupation> cities, String parentId) {
        if (cities == null || cities.isEmpty()) {
            return false;
        }

        for (Occupation city : cities) {
            if (city.getParentCode().equals(parentId)) {
                return true;
            }
        }
        return false;
    }


}
