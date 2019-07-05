package com.oneone.modules.support.contract;

import com.oneone.framework.ui.ibase.IBaseView;
import com.oneone.modules.support.bean.City;
import com.oneone.modules.support.bean.Occupation;
import com.oneone.restful.ApiResult;

import java.util.List;

/**
 * @author qingfei.chen
 * @since 2018/4/3.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public interface OccupationSelectContract {
    interface Model {
        ApiResult getCity();
    }

    interface View extends IBaseView {
    }

    interface Presenter {
        List<Occupation> getCityList(List<Occupation> extraCityList);

        List<Occupation> getCityListByParentId(String parentId);

        boolean hasChild(List<Occupation> list, String parentId);
    }


}
