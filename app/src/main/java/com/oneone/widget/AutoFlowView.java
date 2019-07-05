package com.oneone.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.framework.ui.BaseView;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.widget.FlowLayout;
import com.oneone.modules.matcher.relations.ui.dialog.AutoFlowTagAddDialog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;

/**
 * @author qingfei.chen
 * @since 2018/4/26.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */

@LayoutResource(R.layout.view_auto_flow_view)
public class AutoFlowView extends BaseView implements View.OnClickListener, AutoFlowTagAddDialog.OnDialogListener {
    @BindView(R.id.view_auto_flow_view_btn_add)
    TextView mTvAdd;

    @BindView(R.id.view_auto_flow_view_fl_container)
    FlowLayout mFlowLayout;

    private LayoutInflater inflater;
    private int itemLayoutRes = R.layout.item_auto_flow_default;
    private List<String> mPreSelectedArray = new ArrayList<>();
    private List<String> mSystemTags = new ArrayList<>();
    private int maxSelected = 1;
    private AutoFlowItemClickListener listener;
    private Set<ImageView> setImageView;
    private int mBackgroundResource = R.drawable.selector_step_7_tag_bg;
    private int mTextColor = R.color.color_selector_step_7_text;

    private int limit;
    private int hintText;
    private int toastText = R.string.str_set_single_flow_page_custom_tag_not_null_notice;

    private List<String> selectedValueList = new ArrayList<>();

    public AutoFlowView(Context context) {
        super(context);
    }

    public AutoFlowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoFlowView setMaxSelected(int max) {
        this.maxSelected = max;
        return this;
    }

    public AutoFlowView setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    public AutoFlowView setHintText(int hintText) {
        this.hintText = hintText;
        return this;
    }

    public AutoFlowView setToastText(int toastText) {
        this.toastText = toastText;
        return this;
    }

    public AutoFlowView setListener(AutoFlowItemClickListener listener) {
        this.listener = listener;
        return this;
    }

    public AutoFlowView setPreSelectedArray(List<String> array) {
        this.mPreSelectedArray = array;
        return this;
    }

    public AutoFlowView setItemLayoutRes(int itemLayoutRes) {
        this.itemLayoutRes = itemLayoutRes;
        return this;
    }


    public AutoFlowView setAddBtnColor(int textColor) {
        mTvAdd.setTextColor(textColor);
        return this;
    }

    public AutoFlowView setAddBtnText(int textRes) {
        mTvAdd.setText(textRes);
        return this;
    }

    public AutoFlowView setAddEditTextBackground(int background) {
        this.mBackgroundResource = background;
        return this;
    }

    public AutoFlowView setAddEditTextTextColor(int color) {
        this.mTextColor = color;
        return this;
    }

    @Override
    public void onViewCreated() {
        inflater = LayoutInflater.from(getContext());
        mTvAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_auto_flow_view_btn_add:
                AutoFlowTagAddDialog dialog = new AutoFlowTagAddDialog(getContext(), this,
                        limit, hintText,
                        toastText);
                dialog.show();
                break;

            case R.id.item_auto_flow_tv_display:
                TextView view = (TextView) v;
                performItemClick(view);
                break;

            case R.id.item_auto_flow_iv_display:
                ImageView imageView1 = (ImageView) v;
                if (setImageView != null) {
                    for (ImageView imageView : setImageView) {
                        if (imageView1.equals(imageView)) {
                            TextView textViewDelete = getTextViewFromImageView(imageView);
                            deleteCustomItem(textViewDelete);
                            if (selectedValueList.contains(textViewDelete.getText().toString())) {
                                selectedValueList.remove(textViewDelete.getText().toString());
                                if (listener != null) {
                                    listener.onSelected(getSelectedValue());
                                }
                            }
                        }
                    }
                }
                break;
        }
    }

    private void performItemClick(TextView view) {
        if (maxSelected == 1) {
            performSingleSelected(view);
        } else {
            performMultiSelected(view);
        }
    }

    private void performSingleSelected(TextView v) {
        List<TextView> selectedViews = getSelectedView();
        for (TextView tv : selectedViews) {
            if (!TextUtils.equals(v.getText(), tv.getText())) {
                tv.setSelected(false);
                break;
            }
        }
        v.setSelected(true);
    }

    private void performMultiSelected(TextView v) {
        List<TextView> selectedViews = getSelectedView();
        if (!v.isSelected() && selectedViews != null && selectedViews.size() >= maxSelected) {
            if (listener != null) {
                listener.onSelectOverflow();
            }
            return;
        }
        v.setSelected(!v.isSelected());

        // 判断是否选中；如果选中判断是否是自定义标签；如果是自定义标签就显示删除按钮；
        if (v.isSelected()) {
            if (!mSystemTags.contains(v.getText())) {
                ImageView imageView = (ImageView) v.getTag();
                imageView.setVisibility(View.VISIBLE);
                setImageView.add(imageView);
            }
        } else {
            ((ImageView) v.getTag()).setVisibility(View.GONE);
        }

        if (listener != null) {
            listener.onSelected(getSelectedValue());
        }
    }

    public void notifyDataChange(List<String> tags) {
        this.mSystemTags = tags;
        setImageView = new HashSet<>();
        for (String tag : tags) {
            if (!TextUtils.isEmpty(tag)) {
                TextView tv = addItem(tag);
                preSelectItemIfNeeded(tag, tv);
            }
        }

        appendSelectItemIfNeed();
        if (listener != null) {
            listener.onSelected(getSelectedValue());
        }
    }

    private void preSelectItemIfNeeded(String item, TextView textView) {
        if (textView == null || mPreSelectedArray == null) {
            return;
        }
        for (String tag : mPreSelectedArray) {
            if (TextUtils.equals(tag, item)) {
                textView.setSelected(true);
                return;
            }
        }
    }

    private void appendSelectItemIfNeed() {
        if (mPreSelectedArray == null) {
            return;
        }
        for (String tag : mPreSelectedArray) {
            TextView item = findItemByTag(tag);
            if (item == null) {
                TextView ret = addItem(tag);
                if (ret != null) {
                    ret.setSelected(true);
                }
            }
        }
    }

    public List<String> getSelectedValue() {
        for (int i = 0; i < mFlowLayout.getChildCount(); i++) {
            RelativeLayout view = (RelativeLayout) mFlowLayout.getChildAt(i);
            TextView textView = (TextView) view.getTag();
            String viewContentValue = textView.getText().toString();
            if (textView.isSelected()) {
                if (!selectedValueList.contains(viewContentValue)) {
                    selectedValueList.add(viewContentValue);
                }
            } else {
                if (selectedValueList != null) {
                    if (selectedValueList.contains(viewContentValue)) {
                        selectedValueList.remove(viewContentValue);
                    }
                }
            }
        }
        return selectedValueList;
    }

    public List<String> getSystemSelectedValue() {
        List<String> result = new ArrayList<>();
        List<String> selectedList = getSelectedValue();
        if (selectedList == null || selectedList.isEmpty()) {
            return result;
        }
        for (String tag : mSystemTags) {
            if (selectedList.contains(tag)) {
                result.add(tag);
            }
        }
        return result;
    }

    public List<String> getCustomerSelectedValue() {
        List<String> result = new ArrayList<>();

        List<String> selectedList = getSelectedValue();
        if (selectedList == null || selectedList.isEmpty()) {
            return result;
        }
        for (String tag : selectedList) {
            if (!mSystemTags.contains(tag)) {
                result.add(tag);
            }
        }
        return result;
    }


    private List<TextView> getSelectedView() {
        List<TextView> result = new ArrayList<>();
        for (int i = 0; i < mFlowLayout.getChildCount(); i++) {
            RelativeLayout view = (RelativeLayout) mFlowLayout.getChildAt(i);
            TextView textView = (TextView) view.getTag();
            if (textView.isSelected()) {
                result.add(textView);
            }
        }
        return result;
    }

    private TextView findItemByTag(String tag) {
        for (int i = 0; i < mFlowLayout.getChildCount(); i++) {
            RelativeLayout view = (RelativeLayout) mFlowLayout.getChildAt(i);
            TextView textView = (TextView) view.getTag();
            if (textView instanceof TextView) {
                TextView tv = (TextView) textView;
                if (TextUtils.equals(tv.getText(), tag)) {
                    return tv;
                }
            }
        }
        return null;
    }

    private TextView addItem(String display) {
        TextView item = findItemByTag(display);
        if (item != null) {
            return null;
        }
        RelativeLayout view = (RelativeLayout) inflater.inflate(itemLayoutRes, mFlowLayout, false);
        TextView tvDisplay = view.findViewById(R.id.item_auto_flow_tv_display);
        tvDisplay.setBackgroundResource(mBackgroundResource);
        tvDisplay.setTextColor(getResources().getColorStateList(mTextColor));
        ImageView ivDisplay = view.findViewById(R.id.item_auto_flow_iv_display);
        if(ivDisplay != null) {
            ivDisplay.setOnClickListener(this);
        }
        tvDisplay.setTag(ivDisplay);
        view.setTag(tvDisplay);
        tvDisplay.setText(display);
        tvDisplay.setOnClickListener(this);

        mFlowLayout.addView(view);
        return tvDisplay;
    }

    private void deleteCustomItem(TextView textView) {
        if (textView == null) return;
        for (int i = 0; i < mFlowLayout.getChildCount(); i++) {
            RelativeLayout relativeLayout = (RelativeLayout) mFlowLayout.getChildAt(i);
            if (relativeLayout.getTag().equals(textView)) {
                // 删除的就是这个 TextView 对应的 RelativeLayout
                mFlowLayout.removeView(relativeLayout);
            }
        }
    }

    private TextView getTextViewFromImageView(ImageView imageView) {
        if (imageView == null) return null;
        List<TextView> listTextView = getSelectedView();
        for (int i = 0; i < listTextView.size(); i++) {
            if (listTextView.get(i).getTag().equals(imageView)) {
                return listTextView.get(i);
            }
        }
        return null;
    }

    @Override
    public void onDialogOkClick(String content) {
        addItem(content);
    }

    public interface AutoFlowItemClickListener {
        void onSelected(List<String> tags);

        void onSelectOverflow();
    }
}
