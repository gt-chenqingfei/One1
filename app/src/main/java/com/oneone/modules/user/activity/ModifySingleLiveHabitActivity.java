package com.oneone.modules.user.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import com.oneone.BaseActivity;
import com.oneone.R;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;
import com.oneone.framework.ui.dialog.SheetItem;
import com.oneone.framework.ui.dialog.WheelDialog;
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

@ToolbarResource(title = R.string.str_modify_user_live_habit_title)
@LayoutResource(R.layout.activity_modify_single_user_life_habit)
public class ModifySingleLiveHabitActivity extends BaseActivity implements
        View.OnClickListener, WheelDialog.WheelSelectedListener {
    @BindView(R.id.eat_item)
    ModifyUserBasicLineItem eatItem;
    @BindView(R.id.sleep_item)
    ModifyUserBasicLineItem sleepItem;
    @BindView(R.id.smoke_item)
    ModifyUserBasicLineItem smokeItem;
    @BindView(R.id.drink_item)
    ModifyUserBasicLineItem drinkItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getRightTextMenu().setTextColor(getResources().getColor(R.color.blue));
        getRightTextMenu().setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

        setRightTextMenu(R.string.str_menu_save);

        initView();
    }

    public void initView() {
        eatItem.setOnClickListener(this);
        sleepItem.setOnClickListener(this);
        smokeItem.setOnClickListener(this);
        drinkItem.setOnClickListener(this);

        eatItem.setItemContent(R.string.str_modify_user_live_habit_basic_line_item_title_eat, R.array.eating_habits_array, HereUser.getInstance().getUserInfo().getEatingHabits(), R.string.str_modify_user_live_habit_basic_line_item_val_eat);
        sleepItem.setItemContent(R.string.str_modify_user_live_habit_basic_line_item_title_sleep, R.array.life_habits_array, HereUser.getInstance().getUserInfo().getLifeHabits(), R.string.str_modify_user_live_habit_basic_line_item_val_sleep);
        smokeItem.setItemContent(R.string.str_modify_user_live_habit_basic_line_item_title_smoke, R.array.smoking_habits_array, HereUser.getInstance().getUserInfo().getSmokingHabits(), R.string.str_modify_user_live_habit_basic_line_item_val_smoke);
        drinkItem.setItemContent(R.string.str_modify_user_live_habit_basic_line_item_title_drink, R.array.drinking_habits_array, HereUser.getInstance().getUserInfo().getDrinkingHabits(), R.string.str_modify_user_live_habit_basic_line_item_val_drink);
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ModifySingleLiveHabitActivity.class));
    }

    @Override
    public void onRightTextMenuClick(View view) {
        super.onRightTextMenuClick(view);

        UserProfileUpdateBean updateBean = new UserProfileUpdateBean();

        if (eatItem.getTag() != null) {
            Integer itemVal = (Integer) eatItem.getTag();
            updateBean.setEatingHabits(itemVal);
        }

        if (sleepItem.getTag() != null) {
            Integer itemVal = (Integer) sleepItem.getTag();
            updateBean.setLifeHabits(itemVal);
        }

        if (smokeItem.getTag() != null) {
            Integer itemVal = (Integer) smokeItem.getTag();
            updateBean.setSmokingHabits(itemVal);
        }

        if (drinkItem.getTag() != null) {
            Integer itemVal = (Integer) drinkItem.getTag();
            updateBean.setDrinkingHabits(itemVal);
        }

        HereSingletonFactory.getInstance().getUserManager().updateUserInfo(new UserManager.UserUpdateListener() {
            @Override
            public void onUserUpdate(UserInfo userInfo, boolean isOk, String message) {
                if (isOk) {
                    ToastUtil.show(getActivityContext(), getResources().getString(R.string.str_app_save_request_ok));
                    ModifySingleLiveHabitActivity.this.finish();
                } else {
                    ToastUtil.show(getActivityContext(), getResources().getString(R.string.str_app_save_request_fail));
                }
            }
        }, updateBean);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.eat_item:
                new WheelDialog(this, view.getId(), R.array.eating_habits_array, this)
                        .setPositive(getString(R.string.str_ok)).show();
                break;
            case R.id.sleep_item:
                new WheelDialog(this, view.getId(), R.array.life_habits_array, this)
                        .setPositive(getString(R.string.str_ok)).show();
                break;
            case R.id.smoke_item:
                new WheelDialog(this, view.getId(), R.array.smoking_habits_array, this)
                        .setPositive(getString(R.string.str_ok)).show();
                break;
            case R.id.drink_item:
                new WheelDialog(this, view.getId(), R.array.drinking_habits_array, this)
                        .setPositive(getString(R.string.str_ok)).show();
                break;
        }
    }


    @Override
    public void onWheelSelected(int id, SheetItem item) {
        switch (id) {
            case R.id.eat_item:
                eatItem.setTag(item.getId());
                eatItem.setItemContent(R.string.str_modify_user_live_habit_basic_line_item_title_eat, R.array.eating_habits_array, item.getId(), R.string.str_modify_user_live_habit_basic_line_item_val_eat);
                break;
            case R.id.sleep_item:
                sleepItem.setTag(item.getId());
                sleepItem.setItemContent(R.string.str_modify_user_live_habit_basic_line_item_title_sleep, R.array.life_habits_array, item.getId(), R.string.str_modify_user_live_habit_basic_line_item_val_sleep);
                break;
            case R.id.smoke_item:
                smokeItem.setTag(item.getId());
                smokeItem.setItemContent(R.string.str_modify_user_live_habit_basic_line_item_title_smoke, R.array.smoking_habits_array, item.getId(), R.string.str_modify_user_live_habit_basic_line_item_val_smoke);
                break;
            case R.id.drink_item:
                drinkItem.setTag(item.getId());
                drinkItem.setItemContent(R.string.str_modify_user_live_habit_basic_line_item_title_drink, R.array.drinking_habits_array, item.getId(), R.string.str_modify_user_live_habit_basic_line_item_val_drink);
                break;
        }
    }
}
