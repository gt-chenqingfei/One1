package com.oneone.modules.support.contract;

import com.oneone.framework.ui.ibase.IBaseView;
import com.oneone.modules.support.bean.City;

import java.util.List;

public interface CityContract {

    interface View extends IBaseView {
    }

    interface Presenter {
        void getCityList(CityInfoListener listener);
    }

    interface CityInfoListener {
        void onCityInfoListener(List<City> list);
    }
}
