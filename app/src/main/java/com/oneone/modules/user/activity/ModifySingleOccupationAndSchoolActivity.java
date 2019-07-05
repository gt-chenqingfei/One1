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
import com.oneone.framework.ui.dialog.SheetItem;
import com.oneone.framework.ui.dialog.WheelDialog;
import com.oneone.HereSingletonFactory;
import com.oneone.modules.user.bean.UserProfileUpdateBean;
import com.oneone.modules.user.dialog.InComeDialog;
import com.oneone.modules.user.view.ModifyUserBasicLineItem;
import com.oneone.modules.support.activity.OccupationSelectActivity;
import com.oneone.modules.support.bean.Occupation;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.UserManager;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.utils.ToastUtil;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by here on 18/4/14.
 */

@ToolbarResource(title = R.string.str_modify_user_occupation_and_school_title)
@LayoutResource(R.layout.activity_modify_single_user_occupation_and_school)
public class ModifySingleOccupationAndSchoolActivity extends BaseActivity implements View.OnClickListener, WheelDialog.WheelSelectedListener {

    @BindView(R.id.occupation_item)
    ModifyUserBasicLineItem occupationItem;
    @BindView(R.id.company_item)
    ModifyUserBasicLineItem companyItem;
    @BindView(R.id.year_income_item)
    ModifyUserBasicLineItem yearIncomeItem;
    @BindView(R.id.school_item)
    ModifyUserBasicLineItem schoolItem;
    @BindView(R.id.education_item)
    ModifyUserBasicLineItem educationItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getRightTextMenu().setTextColor(getResources().getColor(R.color.blue));
        getRightTextMenu().setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

        setRightTextMenu(R.string.str_menu_save);

        initView();
    }

    public void initView() {
        occupationItem.setOnClickListener(this);
        companyItem.setOnClickListener(this);
        yearIncomeItem.setOnClickListener(this);
        schoolItem.setOnClickListener(this);
        educationItem.setOnClickListener(this);

        occupationItem.setItemContent(R.string.str_modify_user_occupation_and_school_basic_line_item_title_occupation, HereUser.getInstance().getUserInfo().getIndustry() + "/" + HereUser.getInstance().getUserInfo().getOccupation(), R.string.str_modify_user_occupation_and_school_basic_line_item_val_occupation, -1);
        companyItem.setItemContent(R.string.str_modify_user_occupation_and_school_basic_line_item_title_company, HereUser.getInstance().getUserInfo().getCompany(), R.string.str_modify_user_occupation_and_school_basic_line_item_val_company);
        companyItem.setLimit(10);
        companyItem.itemRightArrow.setVisibility(View.GONE);

        String incomeStr = "";
        if (HereUser.getInstance().getUserInfo().getAnnualIncomeMin() != 0 || HereUser.getInstance().getUserInfo().getAnnualIncomeMax() != 0) {
            String[] stringArray = getResources().getStringArray(com.oneone.R.array.income_array);
            for (String item : stringArray) {
                String[] value = item.split(",");
                if (HereUser.getInstance().getUserInfo().getAnnualIncomeMin() == Integer.valueOf(value[1])) {
                    incomeStr = value[0];
                }
            }
        }

//        else if ()
        yearIncomeItem.setItemContent(R.string.str_modify_user_occupation_and_school_basic_line_item_title_year_income, incomeStr, R.string.str_modify_user_occupation_and_school_basic_line_item_val_year_income, -1);
        schoolItem.setItemContent(R.string.str_modify_user_occupation_and_school_basic_line_item_title_school, HereUser.getInstance().getUserInfo().getCollegeName(), R.string.str_modify_user_occupation_and_school_basic_line_item_val_school);
        schoolItem.setLimit(10);
        schoolItem.itemRightArrow.setVisibility(View.GONE);
        educationItem.setItemContent(R.string.str_modify_user_occupation_and_school_basic_line_item_title_education, R.array.education_habits_array, HereUser.getInstance().getUserInfo().getDegree(), R.string.str_modify_user_occupation_and_school_basic_line_item_val_education);
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ModifySingleOccupationAndSchoolActivity.class));
    }

    @Override
    public void onRightTextMenuClick(final View view) {
        super.onRightTextMenuClick(view);
        UserProfileUpdateBean updateBean = new UserProfileUpdateBean();
        UserManager manager = HereSingletonFactory.getInstance().getUserManager();
        if (occupationItem.getTag() != null) {
            Occupation[] occupationArr = (Occupation[]) occupationItem.getTag();

            updateBean.setOccupationCode(occupationArr[0].getCode());
            updateBean.setOccupation(occupationArr[0].getName());
            updateBean.setIndustryCode(occupationArr[1].getCode());
            updateBean.setIndustry(occupationArr[1].getName());
        }

        String companyStr = companyItem.getEtStr();
        if (companyStr != null && !companyStr.equals("")) {
            updateBean.setCompany(companyStr);
        }

        String schoolStr = schoolItem.getEtStr();
        if (schoolStr != null && !schoolStr.equals("")) {
            updateBean.setCollegeName(schoolStr);
        }

        if (yearIncomeItem.getTag() != null) {
            String itemVal = (String) yearIncomeItem.getTag();
            String[] valArr = itemVal.split("-");
            updateBean.setAnnualIncomeMin(Integer.valueOf(valArr[0]));
            updateBean.setAnnualIncomeMax(Integer.valueOf(valArr[1]));
        }

        if (educationItem.getTag() != null) {
            Integer itemVal = (Integer) educationItem.getTag();
            updateBean.setDegree(itemVal);
        }

        manager.updateUserInfo(new UserManager.UserUpdateListener() {
            @Override
            public void onUserUpdate(UserInfo userInfo, boolean isOk, String message) {
                if (isOk) {
                    ToastUtil.show(getActivityContext(), getResources().getString(R.string.str_app_save_request_ok));
                    SoftKeyBoardUtil.hideSoftInput(view);
                    ModifySingleOccupationAndSchoolActivity.this.finish();
                } else {
                    ToastUtil.show(getActivityContext(), getResources().getString(R.string.str_app_save_request_fail));
                }
            }
        }, updateBean);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.occupation_item:
                OccupationSelectActivity.startActivity4Result(ModifySingleOccupationAndSchoolActivity.this);
                break;
            case R.id.year_income_item:
                new InComeDialog(getActivityContext(), new InComeDialog.InComeSelectedListener() {
                    @Override
                    public void onWheelSelected(int max, int min, String value) {
                        yearIncomeItem.setTag(min + "-" + max);
                        yearIncomeItem.setItemContent(R.string.str_modify_user_occupation_and_school_basic_line_item_title_year_income, value, R.string.str_modify_user_occupation_and_school_basic_line_item_val_year_income, -1);
                    }
                }).setPositive(getString(R.string.str_ok)).show();
                break;
            case R.id.education_item:
                new WheelDialog(this, view.getId(), R.array.education_habits_array, this)
                        .setPositive(getString(R.string.str_ok)).show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OccupationSelectActivity.REQ_CODE_OCCUPATION_SELECT) {
            if (data != null) {
                Occupation occupation = data.getParcelableExtra(OccupationSelectActivity.EXTRA_OCCUPATION);
                Occupation occupationParent = data.getParcelableExtra(OccupationSelectActivity.EXTRA_OCCUPATION_PARENT);
                System.out.println("-------------------->");
                System.out.println(occupation);
                System.out.println(occupationParent);

                occupationItem.setItemContent(R.string.str_modify_user_occupation_and_school_basic_line_item_title_occupation, occupation.getName() + "/" + occupationParent.getName(), R.string.str_modify_user_occupation_and_school_basic_line_item_val_occupation, -1);
                occupationItem.setTag(new Occupation[]{occupation, occupationParent});
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onWheelSelected(int id, SheetItem item) {
        switch (id) {
            case R.id.education_item:
                educationItem.setTag(item.getId());
                educationItem.setItemContent(R.string.str_modify_user_occupation_and_school_basic_line_item_title_education, R.array.education_habits_array, item.getId(), R.string.str_modify_user_occupation_and_school_basic_line_item_val_education);
                break;
        }
    }
}
