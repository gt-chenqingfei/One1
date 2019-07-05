package com.oneone.modules.dogfood.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.framework.android.utils.TimeUtils;
import com.oneone.framework.ui.BaseRecyclerViewAdapter;
import com.oneone.framework.ui.BaseViewHolder;
import com.oneone.modules.dogfood.beans.CoinRecord;
import com.oneone.utils.TimeUtil;

import java.text.SimpleDateFormat;

import butterknife.BindView;

/**
 * Created by here on 18/6/27.
 */

public class DogFoodRecordAdapter extends BaseRecyclerViewAdapter<CoinRecord> {
    private static final String DEFAULT_FORMAT = "MM-dd HH:mm";


    public DogFoodRecordAdapter(BaseViewHolder.ItemClickListener<CoinRecord> listener) {
        super(listener);
    }

    @NonNull
    @Override
    public BaseViewHolder<CoinRecord> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dog_food_record, parent, false);

        return new DogFoodRecordViewHolder(convertView, mListener);
    }

    class DogFoodRecordViewHolder extends BaseViewHolder<CoinRecord> implements View.OnClickListener {
        @BindView(R.id.record_item_title_tv)
        TextView recordItemTitleTv;
        @BindView(R.id.record_item_time_tv)
        TextView recordItemTimeTv;
        @BindView(R.id.record_item_coin_val_tv)
        TextView recordItemCoinValTv;

        private DogFoodRecordViewHolder(View v, BaseViewHolder.ItemClickListener<CoinRecord> listener) {
            super(v, listener);
        }

        @Override
        public void bind(CoinRecord coinRecord, int position) {
            super.bind(coinRecord, position);

            recordItemTitleTv.setText(coinRecord.getCoinTypeDesc());
            recordItemTimeTv.setText(TimeUtils.millis2String(coinRecord.getGmtCreate(), new SimpleDateFormat(DEFAULT_FORMAT)));

            if (coinRecord.getAmount() >= 0) {
                recordItemCoinValTv.setTextColor(getContext().getResources().getColor(R.color.color_7E94BB));
                recordItemCoinValTv.setText("+" + coinRecord.getAmount());
            } else {
                recordItemCoinValTv.setTextColor(getContext().getResources().getColor(R.color.color_F5ABA1));
                recordItemCoinValTv.setText(coinRecord.getAmount() + "");
            }
        }

        @Override
        public void onClick(View view) {

        }
    }
}
