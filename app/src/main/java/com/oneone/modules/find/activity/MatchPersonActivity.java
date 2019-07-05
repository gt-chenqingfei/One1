package com.oneone.modules.find.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.oneone.BaseActivity;
import com.oneone.R;
import com.oneone.api.constants.Gender;
import com.oneone.api.constants.Role;
import com.oneone.framework.android.utils.Toasts;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;
import com.oneone.modules.find.beans.FindCondition;
import com.oneone.modules.find.contract.FindContract;
import com.oneone.modules.find.dto.FindPageUserInfoDTO;
import com.oneone.modules.find.presenter.FindPresenter;
import com.oneone.modules.support.activity.CitySelectActivity;
import com.oneone.modules.support.bean.City;
import com.oneone.modules.user.HereUser;
import com.oneone.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by here on 18/4/25.
 */

@ToolbarResource(title = R.string.str_match_person_page_title)
@LayoutResource(R.layout.activity_match_person)
public class MatchPersonActivity extends BaseActivity <FindPresenter, FindContract.View> implements FindContract.View {
    @BindView(R.id.input_data_content_inner_layout_1_radio_item_1)
    RelativeLayout personSetItem1BgLayout;
    @BindView(R.id.input_data_content_inner_layout_1_radio_item_2)
    RelativeLayout personSetItem2BgLayout;
    @BindView(R.id.input_data_content_inner_layout_1_radio_item_3)
    RelativeLayout personSetItem3BgLayout;
    @BindView(R.id.input_data_content_inner_layout_1_radio_item_4)
    RelativeLayout personSetItem4BgLayout;
    @BindView(R.id.input_data_content_inner_layout_1_radio_item_5)
    RelativeLayout personSetItem5BgLayout;
    @BindView(R.id.input_data_content_inner_layout_1_radio_item_6)
    RelativeLayout personSetItem6BgLayout;
    @BindView(R.id.input_data_content_inner_layout_1_radio_item_1_tv)
    TextView personSetItem1Tv;
    @BindView(R.id.input_data_content_inner_layout_1_radio_item_2_tv)
    TextView personSetItem2Tv;
    @BindView(R.id.input_data_content_inner_layout_1_radio_item_3_tv)
    TextView personSetItem3Tv;
    @BindView(R.id.input_data_content_inner_layout_1_radio_item_4_tv)
    TextView personSetItem4Tv;
    @BindView(R.id.input_data_content_inner_layout_1_radio_item_5_tv)
    TextView personSetItem5Tv;
    @BindView(R.id.input_data_content_inner_layout_1_radio_item_6_tv)
    TextView personSetItem6Tv;

    @BindView(R.id.person_set_city_layout)
    RelativeLayout personSetCityLayout;
    @BindView(R.id.person_set_city_tv)
    TextView personSetCityTv;

    @BindView(R.id.confirm_btn)
    Button confirmBtn;

    public static final int REQ_MATCH_PERSON = 99;

    private FindCondition findCondition;
    private List<Integer> characterList;
    private String setCityCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        characterList = new ArrayList<Integer>();

        initView();

        mPresenter.findGetCondition();
    }

    public void initView () {
        personSetItem1BgLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                changeItem(21, false);
            }
        });
        personSetItem2BgLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                changeItem(22, false);
            }
        });
        personSetItem3BgLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                changeItem(23, false);
            }
        });
        personSetItem4BgLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                changeItem(11, false);
            }
        });
        personSetItem5BgLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                changeItem(12, false);
            }
        });
        personSetItem6BgLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                changeItem(13, false);
            }
        });

        personSetCityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CitySelectActivity.startActivity4Result(MatchPersonActivity.this);
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (characterList != null && characterList.size() > 0) {// && setCityCode != null
                    UploadFindCondition uploadFindCondition = new UploadFindCondition();
                    uploadFindCondition.character = characterList;
//                    uploadFindCondition.cityCode = setCityCode;
                    Gson gson = new Gson();
                    mPresenter.findSetCondition(gson.toJson(uploadFindCondition));

                    ToastUtil.show(getActivityContext(), getActivityContext().getString(R.string.str_toast_person_set_confirm_toast));
                }
            }
        });
    }

    public void changeItem(int character, boolean isInit) {
        boolean isRemove = false;
        if (!isInit) {
            if (characterList.contains(character)) {
                isRemove = true;
                characterList.remove(new Integer(character));
            } else {
                characterList.add(character);
            }
        }
        if (character == 21) {
            if (!isRemove) {
                personSetItem1BgLayout.setBackgroundResource(R.drawable.shap_blue_bg);
                personSetItem1Tv.setTextColor(getResources().getColor(R.color.white));
            } else {
                personSetItem1BgLayout.setBackgroundResource(R.drawable.shape_white_radius_40dp);
                personSetItem1Tv.setTextColor(getResources().getColor(R.color.text_black_2));
            }
        }
        if (character == 22) {
            if (!isRemove) {
                personSetItem2BgLayout.setBackgroundResource(R.drawable.shap_blue_bg);
                personSetItem2Tv.setTextColor(getResources().getColor(R.color.white));
            } else {
                personSetItem2BgLayout.setBackgroundResource(R.drawable.shape_white_radius_40dp);
                personSetItem2Tv.setTextColor(getResources().getColor(R.color.text_black_2));
            }
        }
        if (character == 23) {
            if (!isRemove) {
                personSetItem3BgLayout.setBackgroundResource(R.drawable.shap_blue_bg);
                personSetItem3Tv.setTextColor(getResources().getColor(R.color.white));
            } else {
                personSetItem3BgLayout.setBackgroundResource(R.drawable.shape_white_radius_40dp);
                personSetItem3Tv.setTextColor(getResources().getColor(R.color.text_black_2));
            }
        }
        if (character == 11) {
            if (!isRemove) {
                personSetItem4BgLayout.setBackgroundResource(R.drawable.shap_blue_bg);
                personSetItem4Tv.setTextColor(getResources().getColor(R.color.white));
            } else {
                personSetItem4BgLayout.setBackgroundResource(R.drawable.shape_white_radius_40dp);
                personSetItem4Tv.setTextColor(getResources().getColor(R.color.text_black_2));
            }
        }
        if (character == 12) {
            if (!isRemove) {
                personSetItem5BgLayout.setBackgroundResource(R.drawable.shap_blue_bg);
                personSetItem5Tv.setTextColor(getResources().getColor(R.color.white));
            } else {
                personSetItem5BgLayout.setBackgroundResource(R.drawable.shape_white_radius_40dp);
                personSetItem5Tv.setTextColor(getResources().getColor(R.color.text_black_2));
            }
        }
        if (character == 13) {
            if (!isRemove) {
                personSetItem6BgLayout.setBackgroundResource(R.drawable.shap_blue_bg);
                personSetItem6Tv.setTextColor(getResources().getColor(R.color.white));
            } else {
                personSetItem6BgLayout.setBackgroundResource(R.drawable.shape_white_radius_40dp);
                personSetItem6Tv.setTextColor(getResources().getColor(R.color.text_black_2));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CitySelectActivity.REQ_CODE_CITY_SELECT) {
            if (data != null) {
                City city = data.getParcelableExtra(CitySelectActivity.EXTRA_CITY);
                personSetCityTv.setText(city.getName());
                setCityCode = city.getCode();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onFindRecommend(List<FindPageUserInfoDTO> userList, int expire, int recommandSize) {

    }

    @Override
    public void onFindSetCondition() {
        Intent it = new Intent();
        setResult(RESULT_OK, it);
        MatchPersonActivity.this.finish();
    }

    @Override
    public void onFindGetCondition(FindCondition findCondition) {
        if (findCondition != null) {
            this.findCondition = findCondition;
            this.characterList = findCondition.getCharacter();
            this.setCityCode = findCondition.getCityCode();

            if (characterList == null)
                characterList = new ArrayList<Integer>();

            if (characterList.size() <= 0) {
                if (HereUser.getInstance().getUserInfo().getSex() == Gender.FEMALE) {
                    characterList.add(new Integer(11));
                    characterList.add(new Integer(12));
                    characterList.add(new Integer(13));
                } else {
                    characterList.add(new Integer(21));
                    characterList.add(new Integer(22));
                    characterList.add(new Integer(23));
                }
            }

            for (int character : characterList) {
                changeItem(character, true);
            }
            personSetCityTv.setText(findCondition.getCity());
        } else {
            if (HereUser.getInstance().getUserInfo().getSex() == Gender.FEMALE) {
                characterList.add(new Integer(11));
                characterList.add(new Integer(12));
                characterList.add(new Integer(13));
            } else {
                characterList.add(new Integer(21));
                characterList.add(new Integer(22));
                characterList.add(new Integer(23));
            }

            for (int character : characterList) {
                changeItem(character, true);
            }
        }
    }

    @Override
    public void onSetLike() {

    }

    @Override
    public void onCancelLike() {

    }

    @Override
    public void onSetNoFeel() {

    }

    @Override
    public void onCancelNoFeel() {

    }

    @Override
    public FindPresenter onCreatePresenter() {
        return new FindPresenter();
    }

    class UploadFindCondition {
        public List<Integer> character;
        public String cityCode;
    }
}
