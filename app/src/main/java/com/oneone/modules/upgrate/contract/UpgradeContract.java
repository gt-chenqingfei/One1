package com.oneone.modules.upgrate.contract;

import com.oneone.framework.ui.ibase.IBaseView;
import com.oneone.modules.upgrate.dto.UpgrateDTO;
import com.oneone.restful.ApiResult;

/**
 * 版本升级接口 Contract
 * <p>
 * Created by ZhaiDongyang on 2018/6/26
 */
public interface UpgradeContract {

    interface View extends IBaseView {
    }

    interface Model {
        ApiResult<UpgrateDTO> lastversion(String platform);
    }

    interface Presenter {
        void getUpgrateInfo(String platform, OnUpgrateResultListener listener);
    }

    interface OnUpgrateResultListener {
        void onUpgrateResultListener(UpgrateDTO upgrateDTO);
    }
}
