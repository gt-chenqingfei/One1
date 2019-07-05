package com.oneone.modules.user.view;

import android.content.Context;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.framework.ui.BaseView;
import com.oneone.framework.ui.annotation.LayoutResource;

import butterknife.BindView;

/**
 * Created by here on 18/4/13.
 */
@LayoutResource(R.layout.view_modify_user_basic_line_item_view)
public class ModifyUserBasicLineItem extends BaseView {
    @BindView(R.id.item_title_tv)
    public TextView itemTitleTv;
    @BindView(R.id.item_val_tv)
    public TextView itemValTv;
    @BindView(R.id.item_val_et)
    public EditText itemValEt;
    @BindView(R.id.item_person_setting_tag_iv)
    public ImageView itemPersonSettingTagIv;
    @BindView(R.id.item_right_arrow)
    public ImageView itemRightArrow;

    public String getEtStr () {
        return itemValEt.getText().toString();
    }

    public void setLimit (int max) {
        itemValEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(max)});
    }

    public void setItemContent(int titleId, String val, int defaultValId) {
        itemValTv.setVisibility(View.GONE);
        itemValEt.setVisibility(View.VISIBLE);

        itemRightArrow.setVisibility(View.GONE);

        itemTitleTv.setText(titleId);

        if (defaultValId != -1)
            itemValEt.setHint(defaultValId);
        if (val != null && !val.equals("")) {
            itemValEt.setText(val);
        }
    }

    public void setItemContent(int titleId, String val, int defaultValId, int personSetBgId) {
        itemValEt.setVisibility(View.GONE);
        itemValTv.setVisibility(View.VISIBLE);


        int valColor1 = getResources().getColor(R.color.text_blue_1);
        int valColor2 = getResources().getColor(R.color.text_gray_1);

        if (personSetBgId != -1) {
            itemPersonSettingTagIv.setVisibility(View.VISIBLE);
            itemPersonSettingTagIv.setBackgroundResource(personSetBgId);
        }

        itemTitleTv.setText(titleId);

        if (val != null && !val.trim().equals("") && !val.trim().equals("/")) {
            itemValTv.setTextColor(valColor1);
            itemValTv.setText(val);
        } else {
            itemValTv.setTextColor(valColor2);
            if (defaultValId != -1)
                itemValTv.setText(defaultValId);
            else
                itemValTv.setText("");
        }
    }

    public void setItemContent(int titleId, int arrayId, int valId, int defaultValId) {
        setItemContent(titleId, arrayId, valId, defaultValId, -1);
    }

    public void setItemContent(int titleId,int arrayId, int valId, int defaultValId, int personSetBgId) {
        itemValEt.setVisibility(View.GONE);
        itemValTv.setVisibility(View.VISIBLE);


        int valColor1 = getResources().getColor(R.color.text_blue_1);
        int valColor2 = getResources().getColor(R.color.text_gray_1);

        itemTitleTv.setText(titleId);
        if (personSetBgId != -1) {
            itemPersonSettingTagIv.setVisibility(View.VISIBLE);
            itemPersonSettingTagIv.setBackgroundResource(personSetBgId);
        }

        String val = "";
        String[] stringArray = getResources().getStringArray(arrayId);
        for (String item : stringArray) {
            String[] value = item.split(",");
            if (Integer.valueOf(value[1]).equals(valId)) {
                val = value[0];
                break ;
            }
        }

        if (val != null && !val.equals("")) {
            itemValTv.setTextColor(valColor1);
            itemValTv.setText(val);
        } else {
            itemValTv.setTextColor(valColor2);
            itemValTv.setText(defaultValId);
        }
    }

    public ModifyUserBasicLineItem(Context context) {
        super(context);
    }

    public ModifyUserBasicLineItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onViewCreated() {

    }
}
