package com.oneone.modules.msg.widget.emoji.contracts;

import android.view.ViewGroup;

import com.oneone.modules.msg.widget.emoji.adapter.MyEmojiAdapter;

/**
 * Created by here on 18/5/14.
 */

public interface MyEmojiDisplayListener<T> {
    void onBindView(int position, ViewGroup parent, MyEmojiAdapter.ViewHolder viewHolder, T t, boolean isDelBtn);
}
