package com.oneone.modules.user.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import com.oneone.BaseActivity;
import com.oneone.R;
import com.oneone.framework.android.utils.SoftKeyBoardUtil;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;
import com.oneone.HereSingletonFactory;
import com.oneone.modules.user.bean.UserProfileUpdateBean;
import com.oneone.modules.user.view.ModifyUserBasicLineItem;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.UserManager;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.utils.ToastUtil;

import butterknife.BindView;

/**
 * Created by here on 18/4/14.
 */

@ToolbarResource(title = R.string.str_modify_user_pet_title)
@LayoutResource(R.layout.activity_modify_single_user_pet)
public class ModifySinglePetActivity extends BaseActivity {

    @BindView(R.id.pet_type_item)
    ModifyUserBasicLineItem petTypeItem;
    @BindView(R.id.pet_name_item)
    ModifyUserBasicLineItem petNameItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getRightTextMenu().setTextColor(getResources().getColor(R.color.blue));
        getRightTextMenu().setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

        setRightTextMenu(R.string.str_menu_save);

        initView();
    }

    public void initView() {
        petTypeItem.setItemContent(R.string.str_modify_user_pet_basic_line_item_title_pet_type, HereUser.getInstance().getUserInfo().getPetSpecies(), R.string.str_modify_user_pet_basic_line_item_val_pet_type);
        petTypeItem.setLimit(10);
        petNameItem.setItemContent(R.string.str_modify_user_pet_basic_line_item_title_pet_name, HereUser.getInstance().getUserInfo().getPetName(), R.string.str_modify_user_pet_basic_line_item_val_pet_name);
        petNameItem.setLimit(10);
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ModifySinglePetActivity.class));
    }

    @Override
    public void onRightTextMenuClick(final View view) {
        super.onRightTextMenuClick(view);
        UserManager manager = HereSingletonFactory.getInstance().getUserManager();
        UserProfileUpdateBean updateBean = new UserProfileUpdateBean();
        String petTypeItemEtStr = petTypeItem.getEtStr();
        if (petTypeItemEtStr != null && !petTypeItemEtStr.equals("")) {
            updateBean.setPetSpecies(petTypeItemEtStr);
        }

        String petNameItemEtStr = petNameItem.getEtStr();
        if (petNameItemEtStr != null && !petNameItemEtStr.equals("")) {
            updateBean.setPetName(petNameItemEtStr);
        }

        manager.updateUserInfo(new UserManager.UserUpdateListener() {
            @Override
            public void onUserUpdate(UserInfo userInfo, boolean isOk, String message) {
                if (isOk) {
                    ToastUtil.show(getActivityContext(), getResources().getString(R.string.str_app_save_request_ok));
                    SoftKeyBoardUtil.hideSoftInput(view);
                    ModifySinglePetActivity.this.finish();
                } else {
                    ToastUtil.show(getActivityContext(), getResources().getString(R.string.str_app_save_request_fail));
                }
            }
        }, updateBean);
    }

}
