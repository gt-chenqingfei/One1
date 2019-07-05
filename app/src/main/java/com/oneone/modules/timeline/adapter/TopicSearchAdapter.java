package com.oneone.modules.timeline.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.framework.ui.BaseRecyclerViewAdapter;
import com.oneone.framework.ui.BaseViewHolder;
import com.oneone.modules.timeline.bean.TimeLineTopic;

import butterknife.BindView;

public class TopicSearchAdapter extends BaseRecyclerViewAdapter<TimeLineTopic> {

    public TopicSearchAdapter(BaseViewHolder.ItemClickListener<TimeLineTopic> listener) {
        super(listener);
    }

    @NonNull
    @Override
    public BaseViewHolder<TimeLineTopic> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topic_search, parent, false);
        return new TopicSearchViewHolder(view, mListener);
    }

    public class TopicSearchViewHolder extends BaseViewHolder<TimeLineTopic> implements View.OnClickListener {

        @BindView(R.id.tv_topic_search_content)
        TextView searchContent;

        public TopicSearchViewHolder(View view, ItemClickListener<TimeLineTopic> listener) {
            super(view, listener);
            searchContent.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener == null) return;
            mListener.onItemClick(getData(), v.getId(), getAdapterPosition());
        }

        @Override
        public void bind(TimeLineTopic timeLineTopic, int position) {
            super.bind(timeLineTopic, position);
            searchContent.setText("# " + timeLineTopic.getTag());
        }
    }
}
