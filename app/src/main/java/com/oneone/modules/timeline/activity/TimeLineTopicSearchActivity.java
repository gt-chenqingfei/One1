package com.oneone.modules.timeline.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.framework.android.utils.SoftKeyBoardUtil;
import com.oneone.framework.ui.BaseListActivity;
import com.oneone.framework.ui.BaseRecyclerViewAdapter;
import com.oneone.framework.ui.BaseViewHolder;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;
import com.oneone.framework.ui.widget.SimpleRecyclerView;
import com.oneone.modules.timeline.adapter.TopicSearchAdapter;
import com.oneone.modules.timeline.bean.TimeLineTopic;
import com.oneone.modules.timeline.contract.TimeLineContract;
import com.oneone.modules.timeline.presenter.TimeLineTopicSearchPresenter;
import com.oneone.utils.StringUtil;
import com.qiniu.android.utils.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 话题搜索页面
 * <p>
 * Created by ZhaiDongyang on 2018/6/22
 */
@ToolbarResource(layout = R.layout.activity_topic_search_titlebar, navigationIcon = -1, title = R.string.empty_str, background = R.color.white)
@LayoutResource(R.layout.activity_topic_search)
public class TimeLineTopicSearchActivity extends BaseListActivity<TimeLineTopic, TimeLineTopicSearchPresenter, TimeLineContract.View>
        implements TimeLineContract.OnGetTopicSearchResultListener, TimeLineContract.View, BaseViewHolder.ItemClickListener<TimeLineTopic> {

    public static final int CODE_TOPIC_SELECTED = 100;
    public static final String CODE_TOPIC_RESULT = "topic";

    @BindView(R.id.topic_et_input)
    EditText topicInput;
    @BindView(R.id.tv_topic_search_content_first)
    TextView firstContent;
    @BindView(R.id.topic_tv_cancel)
    TextView cancel;
    @BindView(R.id.topic_search_recyclerView)
    SimpleRecyclerView simpleRecyclerView;

    public static void startActivityForResult(Activity context) {
        Intent intent = new Intent(context, TimeLineTopicSearchActivity.class);
        context.startActivityForResult(intent, CODE_TOPIC_SELECTED);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.getTopicSearchResult("", this);
        topicInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    String intputContent = topicInput.getText().toString();
                    if (!StringUtils.isBlank(intputContent)) {
                        SoftKeyBoardUtil.hideSoftInput(TimeLineTopicSearchActivity.this);
                        mPresenter.getTopicSearchResult(intputContent, TimeLineTopicSearchActivity.this);
                    }
                }
                return false;
            }
        });

        topicInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                mPresenter.getTopicSearchResult(topicInput.getText().toString().trim(), TimeLineTopicSearchActivity.this);
            }
        });
        initRecyclerViewScrollListener();
    }

    @Override
    protected boolean autoLoad() {
        return false;
    }

    @NonNull
    @Override
    public BaseRecyclerViewAdapter<TimeLineTopic> adapterToDisplay() {
        return new TopicSearchAdapter(this);
    }

    @NonNull
    @Override
    public SimpleRecyclerView<TimeLineTopic> getDisplayView() {
        return simpleRecyclerView;
    }

    @Override
    public TimeLineTopicSearchPresenter onCreatePresenter() {
        return new TimeLineTopicSearchPresenter();
    }

    @Override
    public void onGetTopicSearchResultListener(List<TimeLineTopic> timeLineTopic) {
        if (timeLineTopic == null) return;
        if (timeLineTopic.size() > 0) {
            firstContent.setVisibility(View.GONE);
        } else {
            firstContent.setVisibility(View.VISIBLE);
            firstContent.setText("# " + topicInput.getText().toString().trim());
        }
        onLoadCompleted(timeLineTopic);
    }

    @OnClick(R.id.topic_tv_cancel)
    public void cancel() {
        SoftKeyBoardUtil.hideSoftInput(TimeLineTopicSearchActivity.this);
        finish();
    }

    @OnClick(R.id.tv_topic_search_content_first)
    public void firstContent() {
        setTopicContent(topicInput.getText().toString());
    }

    @Override
    public void onItemClick(TimeLineTopic timeLineTopic, int id, int position) {
        switch (id) {
            case R.id.tv_topic_search_content:
                setTopicContent(timeLineTopic.getTag());
                break;
        }
    }

    private void setTopicContent(String content) {
        Intent intent = new Intent();
        intent.putExtra(CODE_TOPIC_RESULT, content);
        setResult(RESULT_OK, intent);
        SoftKeyBoardUtil.hideSoftInput(TimeLineTopicSearchActivity.this);
        this.finish();
    }

    private void initRecyclerViewScrollListener() {
        simpleRecyclerView.getRecyclerView().addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (Math.abs(dy) > 10) {
                    SoftKeyBoardUtil.hideSoftInput(TimeLineTopicSearchActivity.this);
                }
            }
        });
    }

}
