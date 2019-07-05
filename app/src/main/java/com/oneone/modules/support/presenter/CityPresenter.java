package com.oneone.modules.support.presenter;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.oneone.BasePresenter;
import com.oneone.modules.support.bean.City;
import com.oneone.modules.support.contract.CityContract;
import com.oneone.modules.support.model.SupportModel;

import java.util.ArrayList;
import java.util.List;

public class CityPresenter extends BasePresenter<CityContract.View> implements CityContract.Presenter {

    private SupportModel mSupportModel;

    @Override
    public void onAttachView(CityContract.View view) {
        super.onAttachView(view);
        mSupportModel = new SupportModel(view.getActivityContext());
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void getCityList(final CityContract.CityInfoListener listener) {
        AsyncTask task = new AsyncTask<Object, Void, List<City>>() {

            @Override
            protected List<City> doInBackground(Object... voids) {
                return mSupportModel.getCityList();
            }

            @Override
            protected void onPostExecute(List<City> result) {
                super.onPostExecute(result);
                listener.onCityInfoListener(result);
            }
        };
        enqueue(task);
    }

    public List<City> getCityListByParentId(List<City> cityList, int parentId) {
        if (cityList == null || cityList.isEmpty()) {
            return null;
        }
        List<City> result = new ArrayList<>();
        for (City city : cityList) {
            if (city.getParentId() == parentId) {
                result.add(city);
            }
        }
        return result;
    }
}
