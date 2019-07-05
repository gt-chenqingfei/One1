package com.oneone.modules.timeline.view;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.framework.ui.Spannables.OnTextClickListener;
import com.oneone.framework.ui.Spannables.Range;
import com.oneone.framework.ui.Spannables.SimpleText;
import com.oneone.modules.timeline.activity.TimeLineTopicActivity;
import com.oneone.modules.timeline.activity.TimeLineTopicSearchActivity;
import com.oneone.modules.timeline.bean.TimeLine;

/**
 * @author qingfei.chen
 * @since 2018/5/30.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class TimeLineTextContentView extends TextView {
    public TimeLineTextContentView(Context context) {
        super(context);
    }

    public TimeLineTextContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void bind(TimeLine o) {
        this.setBackgroundResource(R.color.transparent);
        this.setVisibility(GONE);
        this.setPadding(0,0,0,0);
        if (o.getDetail() == null) {
            return;
        }
        if (o.getDetail().getDetail() == null) {
            return;
        }
        String content = o.getDetail().getDetail().getContent();
        if (TextUtils.isEmpty(content)) {
            return;
        }

        if (o.getDetail().getDetail().getTimelineImgs() == null || o.getDetail().getDetail().getTimelineImgs().isEmpty()) {
            this.setBackgroundResource(R.drawable.shape_timeline_text_bg);
        } else {
            this.setBackgroundResource(R.drawable.transparent);
        }
        SimpleText simpleText = getText(content, getContext());
        this.setVisibility(VISIBLE);
        this.setText(simpleText);
    }

    private SimpleText getText(final String content, Context context) {
        SimpleText simpleText = SimpleText.from(context, content)
                .allStartWith("#")
                .textColor(R.color.color_796CF0)
                .pressedTextColor(R.color.color_796CF0)
                .pressedBackground(R.color.transparent, 0)
                .onClick(this, new OnTextClickListener() {
                    @Override
                    public void onClicked(CharSequence text, Range range, Object tag) {
                        TimeLineTopicActivity.startActivity(getContext(), text.toString());
                    }
                });
        return simpleText;
    }
}
