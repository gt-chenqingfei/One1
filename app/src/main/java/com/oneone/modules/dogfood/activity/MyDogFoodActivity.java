package com.oneone.modules.dogfood.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.oneone.BaseActivity;
import com.oneone.R;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;
import com.oneone.modules.dogfood.beans.CoinRecord;
import com.oneone.modules.dogfood.contract.CoinContract;
import com.oneone.modules.dogfood.dialog.WhatIsDogFoodDialog;
import com.oneone.modules.dogfood.presenter.CoinPresenter;

import java.util.List;

import butterknife.BindView;

/**
 * Created by here on 18/5/2.
 */

@Route(path = "/dog_food/account")
@ToolbarResource(title = R.string.str_dog_food_title_text, background = R.color.transparent, navigationIcon = R.drawable.white_left_arrow)
@LayoutResource(R.layout.activity_dog_food)
public class MyDogFoodActivity extends BaseActivity<CoinPresenter, CoinContract.View> implements CoinContract.View, View.OnClickListener{
    @BindView(R.id.dog_food_val_tv)
    TextView dogFoodValTv;
//    @BindView(R.id.today_earn_tv)
//    TextView todayEarnTv;
    @BindView(R.id.what_is_dog_food_btn)
    Button whatIsDogBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getMiddleTitle().setTextColor(getResources().getColor(R.color.white));
        initView();
        setRightTextMenu(R.string.str_dog_food_title_text).setTextColor(getResources().getColor(R.color.white));

        mPresenter.coinGetcoinbalance();
    }

    @Override
    public void onRightTextMenuClick(View view) {
        super.onRightTextMenuClick(view);

        DogFoodRecordActivity.startActivity(getActivityContext());
    }

    public void initView () {


//        todayEarnTv.setText(getResources().getString(R.string.str_dog_food_today_earn_left_text)
//                + "1000"
//                + getResources().getString(R.string.str_dog_food_today_earn_right_text));

        whatIsDogBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.what_is_dog_food_btn:
                new WhatIsDogFoodDialog(getActivityContext()).show();
                break;
        }
    }

    public static void startActivity (Context context) {
        Intent it = new Intent(context, MyDogFoodActivity.class);
        context.startActivity(it);
    }

    @Override
    public CoinPresenter onCreatePresenter() {
        return new CoinPresenter();
    }

    @Override
    public void onCoinGetcoinbalance(int balance) {
        System.out.println("==========onCoinGetcoinbalance=========");
        dogFoodValTv.setText(balance + "");
    }

    @Override
    public void onCoinRecords(List<CoinRecord> record, int count) {

    }
}
