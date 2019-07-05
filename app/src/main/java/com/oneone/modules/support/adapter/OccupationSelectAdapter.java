package com.oneone.modules.support.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.framework.ui.BaseRecyclerViewAdapter;
import com.oneone.framework.ui.BaseViewHolder;
import com.oneone.modules.support.bean.City;
import com.oneone.modules.support.bean.Occupation;

import butterknife.BindView;


/**
 * @author qingfei.chen
 * @since 2018/4/9.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class OccupationSelectAdapter extends BaseRecyclerViewAdapter<Occupation> {
    public OccupationSelectAdapter(BaseViewHolder.ItemClickListener<Occupation> listener) {
        super(listener);
    }

    private Occupation mCurrentSelected;

    public void setSelected(Occupation city) {
        this.mCurrentSelected = city;
        notifyDataSetChanged();
    }

    public Occupation getSelected() {
        return this.mCurrentSelected;
    }

    @Override
    public BaseViewHolder<Occupation> onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_occupation_select, parent, false);


        return new CityViewHolder(convertView, mListener);
    }

    class CityViewHolder extends BaseViewHolder<Occupation> implements View.OnClickListener {
        @BindView(R.id.item_city_select_tv_display)
        TextView tvDisplay;
        @BindView(R.id.item_city_select_ll)
        View item;

        @BindView(R.id.item_city_select_iv_checked)
        ImageView ivChecked;

        private CityViewHolder(View v, ItemClickListener<Occupation> listener) {
            super(v, listener);
        }

        @Override
        public void bind(Occupation city, int position) {
            super.bind(city, position);
            tvDisplay.setText(city.getName());
            item.setOnClickListener(this);

            if (mCurrentSelected != null && city.getCode().equals(mCurrentSelected.getCode())) {
                ivChecked.setVisibility(View.VISIBLE);
            } else {
                ivChecked.setVisibility(View.INVISIBLE);
            }

        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(getData(), v.getId(), getAdapterPosition());
            }
        }
    }


}



