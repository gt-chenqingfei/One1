package com.oneone.modules.user.fragment;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oneone.HereSingletonFactory;
import com.oneone.R;
import com.oneone.event.EventProfileUpdateByRole;
import com.oneone.framework.android.analytics.annotation.Alias;
import com.oneone.framework.android.utils.LocaleUtils;
import com.oneone.framework.ui.BasePresenterFragment;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.modules.entry.presenter.OpenRelationPresenter;
import com.oneone.modules.support.activity.CitySelectActivity;
import com.oneone.modules.support.bean.City;
import com.oneone.modules.support.contract.CityContract;
import com.oneone.modules.support.presenter.CityPresenter;
import com.oneone.modules.user.bean.UserProfileUpdateBean;
import com.oneone.utils.LocationUtil;
import com.oneone.utils.StringUtil;
import com.oneone.utils.ToastUtil;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;

/**
 * @author qingfei.chen
 * @since 2018/4/25.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@Alias("城市")
@LayoutResource(R.layout.frag_profile_city_edit)
public class ProfileCityEditFrag extends BasePresenterFragment<CityPresenter, CityContract.View>
        implements View.OnClickListener, CityContract.CityInfoListener, CityContract.View {
    @BindView(R.id.step_3_city_layout)
    RelativeLayout mRlCity;

    @BindView(R.id.step_3_city_tv)
    TextView mTvCity;

    @BindView(R.id.step_3_confirm_btn)
    Button mBtnConfirm;

    private City city;
    private City cityParent;
    private boolean isSelectCity = false;
    private String mAdminArea;
    private String mCityOrDistrict;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRlCity.setOnClickListener(this);
        mBtnConfirm.setOnClickListener(this);
        UserProfileUpdateBean bean = OpenRelationPresenter.getTempUserInfo();
        String cityStr = bean.getCity();
        mTvCity.setText(cityStr);
        city = new City();
        city.setName(cityStr);
        city.setCode(bean.getCityCode());

        cityParent = new City();
        cityParent.setName(bean.getProvince());
        cityParent.setCode(bean.getProvinceCode());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.step_3_city_layout:
                CitySelectActivity.startActivity4Result(this, CitySelectActivity.REQ_CODE_CITY_SELECT);
                break;

            case R.id.step_3_confirm_btn:
                if (city == null || TextUtils.isEmpty(city.getName())) {
                    ToastUtil.show(getContext(),
                            getString(R.string.str_set_single_flow_page_input_city_notice));
                    return;
                }
                UserProfileUpdateBean userInfo = new UserProfileUpdateBean();
                userInfo.setCity(city.getName());
                userInfo.setCityCode(city.getCode());
                userInfo.setProvinceCode(cityParent.getCode());
                userInfo.setProvince(cityParent.getName());
                String countryCode = HereSingletonFactory.getInstance()
                        .getUserManager().getCountryCode();

                if (TextUtils.isEmpty(countryCode)) {
                    countryCode = "+" + LocaleUtils.getCountryCode(getContext());
                }
                userInfo.setCountryCode(countryCode);

                EventBus.getDefault().post(new EventProfileUpdateByRole(userInfo));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CitySelectActivity.REQ_CODE_CITY_SELECT) {
            if (data != null) {
                isSelectCity = true;
                city = data.getParcelableExtra(CitySelectActivity.EXTRA_CITY);
                cityParent = data.getParcelableExtra(CitySelectActivity.EXTRA_CITY_PARENT);
                mTvCity.setText(cityParent.getName() + " " + city.getName());
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!isSelectCity)
            checkPermissions();
    }

    private void checkPermissions() {
        if (!AndPermission.hasPermissions(getActivityContext(), Permission.Group.LOCATION)
                ) {
            AndPermission.with(getActivityContext())
                    .runtime()
                    .permission(Permission.Group.LOCATION)
                    .start();
        } else {
            initLocation();
        }
    }

    private void initLocation() {
        LocationUtil.getInstance(getActivity()).initLocation(new LocationUtil.LocationHelper() {
            @Override
            public void UpdateLastLocation(Location location) {
                Geocoder geocoder = new Geocoder(getActivity());
                try {
                    List<Address> list = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 3);
                    if (list == null || list.size() == 0) return;
                    for (int i = 0; i < list.size(); i++) {
                        Address address = list.get(i);
                        if (!StringUtil.isNullOrEmpty(address.getCountryCode())) {// CN
                            String adminArea = list.get(i).getAdminArea();// 省份
                            String locality = list.get(i).getLocality();// 市
                            String subLocality = list.get(i).getSubLocality();// 区
                            if (adminArea.equals(locality)) {
                                // 直辖市 需要市和区
                                mAdminArea = locality;
                                mCityOrDistrict = subLocality;
                            } else {
                                // 省 需要省和市
                                mAdminArea = adminArea;
                                mCityOrDistrict = locality;
                            }
                            mPresenter.getCityList(ProfileCityEditFrag.this);
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public CityPresenter onPresenterCreate() {
        return new CityPresenter();
    }

    @Override
    public void onCityInfoListener(List<City> data) {
        if (data == null || data.size() == 0) return;
        for (int i = 0; i < data.size(); i++) {
            if (mAdminArea.contains(data.get(i).getName())
                    || mAdminArea.equals(data.get(i).getName())) {
                // 省份相同设置code
                cityParent.setName(data.get(i).getName());
                cityParent.setCode(data.get(i).getCode());
                List<City> cityListByParentId = mPresenter.getCityListByParentId(data, data.get(i).getId());
                for (int j = 0; j < cityListByParentId.size(); j++) {
                    if (mCityOrDistrict.contains(cityListByParentId.get(i).getName())
                            || mCityOrDistrict.equals(cityListByParentId.get(i).getName())) {
                        city.setName(cityListByParentId.get(j).getName());
                        city.setCode(cityListByParentId.get(j).getCode());
                        break;
                    }
                }
            }
            break;
        }
        if (TextUtils.isEmpty(cityParent.getCode()) || TextUtils.isEmpty(city.getCode())) {
            ToastUtil.show(getActivity(), getActivity().getResources().getString(R.string.location_failed_select_city));
        } else {
            mTvCity.setText(cityParent.getName() + " " + city.getName());
        }
    }
}
