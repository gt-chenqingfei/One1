package com.oneone.modules.msg.widget.emoji;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.oneone.R;
import com.oneone.framework.ui.glide.GlideUtils;
import com.oneone.modules.msg.IMManager;
import com.oneone.modules.msg.beans.IMEmoji;
import com.oneone.modules.msg.widget.emoji.adapter.MyEmojiAdapter;
import com.oneone.modules.msg.widget.emoji.contracts.MyEmojiDisplayListener;
import com.oneone.utils.ImageHelper;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;

import cn.jiguang.imui.chatinput.emoji.Constants;
import cn.jiguang.imui.chatinput.emoji.DefEmoticons;
import cn.jiguang.imui.chatinput.emoji.EmojiBean;
import cn.jiguang.imui.chatinput.emoji.EmojiFilter;
import cn.jiguang.imui.chatinput.emoji.ImageLoader;
import cn.jiguang.imui.chatinput.emoji.adapter.PageSetAdapter;
import cn.jiguang.imui.chatinput.emoji.data.EmoticonEntity;
import cn.jiguang.imui.chatinput.emoji.data.EmoticonPageEntity;
import cn.jiguang.imui.chatinput.emoji.data.EmoticonPageSetEntity;
import cn.jiguang.imui.chatinput.emoji.listener.EmoticonClickListener;
import cn.jiguang.imui.chatinput.emoji.listener.EmoticonDisplayListener;
import cn.jiguang.imui.chatinput.emoji.listener.ImageBase;
import cn.jiguang.imui.chatinput.emoji.listener.PageViewInstantiateListener;
import cn.jiguang.imui.chatinput.emoji.widget.EmoticonPageView;
import cn.jiguang.imui.chatinput.emoji.widget.EmoticonsEditText;

/**
 * Created by here on 18/5/14.
 */

public class MyEmojiCommonUtils {

    public static void initEmoticonsEditText(EmoticonsEditText etContent) {
        etContent.addEmoticonFilter(new EmojiFilter());
    }

    public static EmoticonClickListener getCommonEmoticonClickListener(final EditText editText) {
        return new EmoticonClickListener() {
            @Override
            public void onEmoticonClick(Object o, int actionType, boolean isDelBtn) {
                if (isDelBtn) {
                    cn.jiguang.imui.chatinput.utils.SimpleCommonUtils.delClick(editText);
                } else {
                    if (o == null) {
                        return;
                    }
                    if (actionType == Constants.EMOTICON_CLICK_TEXT) {
                        String content = null;
                        if (o instanceof EmojiBean) {
                            content = ((EmojiBean) o).emoji;
                        } else if (o instanceof EmoticonEntity) {
                            content = ((EmoticonEntity) o).getContent();
                        }

                        if (TextUtils.isEmpty(content)) {
                            return;
                        }
                        int index = editText.getSelectionStart();
                        Editable editable = editText.getText();
                        editable.insert(index, content);
                    }
                }
            }
        };
    }

    public static PageSetAdapter sCommonPageSetAdapter;

    public static PageSetAdapter getCommonAdapter(Context context, EmoticonClickListener emoticonClickListener) {

//        if(sCommonPageSetAdapter != null){
//            return sCommonPageSetAdapter;
//        }

        PageSetAdapter pageSetAdapter = new PageSetAdapter();

        addEmojiPageSetEntity(pageSetAdapter, context, emoticonClickListener);

        return pageSetAdapter;
    }

    /**
     * 插入emoji表情集
     *
     * @param pageSetAdapter
     * @param context
     * @param emoticonClickListener
     */
    public static void addEmojiPageSetEntity(PageSetAdapter pageSetAdapter, final Context context, final EmoticonClickListener emoticonClickListener) {
//        ArrayList<EmojiBean> emojiArray = new ArrayList<>();

//        Collections.addAll(emojiArray, DefEmoticons.sEmojiArray);
        EmoticonPageSetEntity emojiPageSetEntity
                = new EmoticonPageSetEntity.Builder()
                .setLine(2)
                .setRow(4)
                .setEmoticonList((ArrayList) IMManager.IM_EMOJI_LIST)
                .setIPageViewInstantiateItem(getDefaultEmoticonPageViewInstantiateItem(new MyEmojiDisplayListener<Object>() {
                    @Override
                    public void onBindView(int position, ViewGroup parent, MyEmojiAdapter.ViewHolder viewHolder, Object object, final boolean isDelBtn) {
                        final IMEmoji imEmoji = (IMEmoji) object;
                        if (imEmoji == null && !isDelBtn) {
                            return;
                        }

                        viewHolder.ly_root.setBackgroundResource(cn.jiguang.imui.chatinput.R.drawable.bg_emoticon);

                        if (isDelBtn) {
                            viewHolder.iv_emoticon.setImageResource(cn.jiguang.imui.chatinput.R.drawable.icon_del);
                        } else {
//                            viewHolder.iv_emoticon.setImageResource(emojiBean.icon);
                            if (viewHolder != null && viewHolder.iv_emoticon != null) {
                                ImageHelper.displayImage(context, viewHolder.iv_emoticon, imEmoji.getImage());
//                                GlideUtils.loadCircleImage(imEmoji.getImage(), viewHolder.iv_emoticon, new GlideUtils.ImageLoadListener<String, GlideDrawable>() {
//                                    @Override
//                                    public void onLoadingComplete(String uri, ImageView view, GlideDrawable resource) {
//
//                                    }
//
//                                    @Override
//                                    public void onLoadingError(String source, Exception e) {
//
//                                    }
//                                });
                                viewHolder.tv_emoticon.setText(imEmoji.getMessage());
                            }
                        }

                        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (emoticonClickListener != null) {
                                    emoticonClickListener.onEmoticonClick(imEmoji, Constants.EMOTICON_CLICK_TEXT, isDelBtn);
                                }
                            }
                        });
                    }
                }))
                .setShowDelBtn(EmoticonPageEntity.DelBtnStatus.GONE)
                .setIconUri(ImageBase.Scheme.DRAWABLE.toUri("icon_emoji"))
                .build();
        pageSetAdapter.add(emojiPageSetEntity);
    }



    @SuppressWarnings("unchecked")
    public static Object newInstance(Class _Class, Object... args) throws Exception {
        return newInstance(_Class, 0, args);
    }

    @SuppressWarnings("unchecked")
    public static Object newInstance(Class _Class, int constructorIndex, Object... args) throws Exception {
        Constructor cons = _Class.getConstructors()[constructorIndex];
        return cons.newInstance(args);
    }

    public static PageViewInstantiateListener<EmoticonPageEntity> getDefaultEmoticonPageViewInstantiateItem(final MyEmojiDisplayListener<Object> emoticonDisplayListener) {
        return getEmoticonPageViewInstantiateItem(MyEmojiAdapter.class, null, emoticonDisplayListener);
    }

    public static PageViewInstantiateListener<EmoticonPageEntity> getEmoticonPageViewInstantiateItem(final Class _class, EmoticonClickListener onEmoticonClickListener) {
        return getEmoticonPageViewInstantiateItem(_class, onEmoticonClickListener, null);
    }

    public static PageViewInstantiateListener<EmoticonPageEntity> getEmoticonPageViewInstantiateItem(final Class _class, final EmoticonClickListener onEmoticonClickListener, final MyEmojiDisplayListener<Object> emoticonDisplayListener) {
        return new PageViewInstantiateListener<EmoticonPageEntity>() {
            @Override
            public View instantiateItem(ViewGroup container, int position, EmoticonPageEntity pageEntity) {
                if (pageEntity.getRootView() == null) {
                    EmoticonPageView pageView = new EmoticonPageView(container.getContext());
                    pageView.setNumColumns(pageEntity.getRow());
                    pageEntity.setRootView(pageView);
                    try {
                        MyEmojiAdapter adapter = (MyEmojiAdapter) newInstance(_class, container.getContext(), pageEntity, onEmoticonClickListener);
                        if (emoticonDisplayListener != null) {
                            adapter.setOnDisPlayListener(emoticonDisplayListener);
                        }
                        pageView.getEmoticonsGridView().setAdapter(adapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return pageEntity.getRootView();
            }
        };
    }

//    public static MyEmojiDisplayListener<Object> getCommonEmoticonDisplayListener(final EmoticonClickListener onEmoticonClickListener, final int type) {
//        return new MyEmojiDisplayListener<Object>() {
//            @Override
//            public void onBindView(int position, ViewGroup parent, MyEmojiAdapter.ViewHolder viewHolder, Object object, final boolean isDelBtn) {
//
//                final EmoticonEntity emoticonEntity = (EmoticonEntity) object;
//                if (emoticonEntity == null && !isDelBtn) {
//                    return;
//                }
//                viewHolder.ly_root.setBackgroundResource(cn.jiguang.imui.chatinput.R.drawable.bg_emoticon);
//
//                if (isDelBtn) {
//                    viewHolder.iv_emoticon.setImageResource(cn.jiguang.imui.chatinput.R.drawable.icon_del);
//                } else {
//                    try {
//                        ImageLoader.getInstance(viewHolder.iv_emoticon.getContext()).displayImage(emoticonEntity.getIconUri(), viewHolder.iv_emoticon);
//                        viewHolder.tv_emoticon.setText(emoticonEntity.getContent());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (onEmoticonClickListener != null) {
//                            onEmoticonClickListener.onEmoticonClick(emoticonEntity, type, isDelBtn);
//                        }
//                    }
//                });
//            }
//        };
//    }

    public static void delClick(EditText editText) {
        int action = KeyEvent.ACTION_DOWN;
        int code = KeyEvent.KEYCODE_DEL;
        KeyEvent event = new KeyEvent(action, code);
        editText.onKeyDown(KeyEvent.KEYCODE_DEL, event);
    }

//    public static void spannableEmoticonFilter(TextView tv_content, String content) {
//        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(content);
//
//        Spannable spannable = EmojiDisplay.spannableFilter(tv_content.getContext(),
//                spannableStringBuilder,
//                content,
//                EmoticonsKeyboardUtils.getFontHeight(tv_content));
//
//        tv_content.setText(spannable);
//    }
}
