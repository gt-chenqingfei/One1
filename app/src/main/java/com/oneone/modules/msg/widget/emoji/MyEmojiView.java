package com.oneone.modules.msg.widget.emoji;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import java.util.ArrayList;

import cn.jiguang.imui.chatinput.emoji.adapter.PageSetAdapter;
import cn.jiguang.imui.chatinput.emoji.data.PageSetEntity;
import cn.jiguang.imui.chatinput.emoji.widget.AutoHeightLayout;
import cn.jiguang.imui.chatinput.emoji.widget.EmoticonsFuncView;
import cn.jiguang.imui.chatinput.emoji.widget.EmoticonsIndicatorView;
import cn.jiguang.imui.chatinput.emoji.widget.EmoticonsToolBarView;

/**
 * Created by here on 18/5/14.
 */

public class MyEmojiView extends AutoHeightLayout implements EmoticonsFuncView.OnEmoticonsPageViewListener,
        EmoticonsToolBarView.OnToolBarItemClickListener {

    private Context mContext;
    private EmoticonsFuncView mEmoticonsFuncView;
    private EmoticonsIndicatorView mEmoticonsIndicatorView;
    private EmoticonsToolBarView mEmoticonsToolBarView;

    public MyEmojiView(Context context) {
        super(context, null);
        init(context, null);
    }

    public MyEmojiView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @Override
    public void onSoftKeyboardHeightChanged(int i) {

    }

    public MyEmojiView(@NonNull Context context, @Nullable AttributeSet attrs,
                     @AttrRes int defStyleAttr) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, cn.jiguang.imui.chatinput.R.layout.layout_chatinput_emoji, this);
        mContext = context;

        mEmoticonsFuncView = ((EmoticonsFuncView) findViewById(cn.jiguang.imui.chatinput.R.id.view_epv));
        mEmoticonsIndicatorView = ((EmoticonsIndicatorView) findViewById(cn.jiguang.imui.chatinput.R.id.view_eiv));
        mEmoticonsToolBarView = ((EmoticonsToolBarView) findViewById(cn.jiguang.imui.chatinput.R.id.view_etv));
        mEmoticonsFuncView.setOnIndicatorListener(this);
        mEmoticonsToolBarView.setOnToolBarItemClickListener(this);
    }

    public void setAdapter(PageSetAdapter pageSetAdapter) {
        if (pageSetAdapter != null) {
            ArrayList<PageSetEntity> pageSetEntities = pageSetAdapter.getPageSetEntityList();
            if (pageSetEntities != null) {
                for (PageSetEntity pageSetEntity : pageSetEntities) {
                    mEmoticonsToolBarView.addToolItemView(pageSetEntity);
                }
            }
        }
        mEmoticonsFuncView.setAdapter(pageSetAdapter);
    }

    @Override
    public void emoticonSetChanged(PageSetEntity pageSetEntity) {
        mEmoticonsToolBarView.setToolBtnSelect(pageSetEntity.getUuid());
    }

    @Override
    public void playTo(int position, PageSetEntity pageSetEntity) {
        mEmoticonsIndicatorView.playTo(position, pageSetEntity);
    }

    @Override
    public void playBy(int oldPosition, int newPosition, PageSetEntity pageSetEntity) {
        mEmoticonsIndicatorView.playBy(oldPosition, newPosition, pageSetEntity);
    }

    @Override
    public void onToolBarItemClick(PageSetEntity pageSetEntity) {
        mEmoticonsFuncView.setCurrentPageSet(pageSetEntity);
    }

    public EmoticonsFuncView getEmoticonsFuncView() {
        return this.mEmoticonsFuncView;
    }
}
