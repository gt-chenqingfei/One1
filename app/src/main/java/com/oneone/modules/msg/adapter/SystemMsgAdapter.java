package com.oneone.modules.msg.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.format.DateFormat;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.framework.ui.BaseRecyclerViewAdapter;
import com.oneone.framework.ui.BaseViewHolder;
import com.oneone.framework.ui.glide.GlideUtils;
import com.oneone.modules.msg.beans.MsgMeta;
import com.oneone.schema.SchemaUtil;
import com.oneone.utils.ImageHelper;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;

/**
 * Created by here on 18/5/15.
 */

public class SystemMsgAdapter extends BaseRecyclerViewAdapter<MsgMeta> {
    public static final int TYPE_TEXT = 1;
    public static final int TYPE_PHOTO = 2;
    public static final int TYPE_PHOTO_TEXT = 3;

    private Context context;

    private SystemMsgAdapterListener myList;

    public abstract static class SystemMsgAdapterListener implements BaseViewHolder.ItemClickListener {
    }


    public SystemMsgAdapter (SystemMsgAdapter.SystemMsgAdapterListener listener, Context context) {
        super(listener);
        this.context = context;
        this.myList = listener;
    }

    @Override
    public int getItemViewType(int position) {
        MsgMeta item = getItem(position);
        if(item == null){
            return TYPE_TEXT;
        }

        return item.getMetaType();
    }

    @NonNull
    @Override
    public BaseViewHolder<MsgMeta> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_TEXT) {
            View viewGroup = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sys_msg_1, parent, false);
            return new SystemMsgAdapter.SystemMsgViewHolder1(viewGroup);
        } else if (viewType == TYPE_PHOTO) {
            View viewGroup = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sys_msg_2, parent, false);
            return new SystemMsgAdapter.SystemMsgViewHolder2(viewGroup);
        } else if (viewType == TYPE_PHOTO_TEXT) {
            View viewGroup = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sys_msg_3, parent, false);
            return new SystemMsgAdapter.SystemMsgViewHolder3(viewGroup);
        } else {
            View viewGroup = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sys_msg_1, parent, false);
            return new SystemMsgAdapter.SystemMsgViewHolder1(viewGroup);
        }

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    class SystemMsgViewHolder1 extends BaseViewHolder<MsgMeta> implements View.OnClickListener {
        @BindView(R.id.sys_msg_time_tv)
        TextView timeTv;
        @BindView(R.id.content_title_tv)
        TextView contentTitleTv;
        @BindView(R.id.content_tv)
        TextView contentTv;
        @BindView(R.id.link_title_tv)
        TextView linkTitleTv;


        private SystemMsgViewHolder1(View v) {
            super(v);
            v.setOnClickListener(this);
        }

        @Override
        public void bind(MsgMeta msgMeta, int position) {
            super.bind(msgMeta, position);

            timeTv.setText(DateFormat.format("yyyy-MM-dd-HH:mm", msgMeta.getTimestamp()));
            if (msgMeta.getMetaTitle() != null)
                contentTitleTv.setText(msgMeta.getMetaTitle());
            else
                contentTitleTv.setVisibility(View.GONE);
            if (msgMeta.getMetaValue() != null) {
//                contentTv.setText(msgMeta.getMetaValue());
                linkTextUtil(msgMeta.getMetaValue(), contentTv);
            } else
                contentTv.setVisibility(View.GONE);
            if (msgMeta.getLinkTitle() != null)
                linkTitleTv.setText(msgMeta.getLinkTitle());
            else
                linkTitleTv.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View v) {
            if (myList != null) {
                myList.onItemClick(getData(), v.getId(), getAdapterPosition());
            }
        }
    }

    class SystemMsgViewHolder2 extends BaseViewHolder<MsgMeta> implements View.OnClickListener {
        @BindView(R.id.sys_msg_time_tv)
        TextView timeTv;
        @BindView(R.id.content_iv)
        ImageView contentIv;

        private SystemMsgViewHolder2(View v) {
            super(v);
            v.setOnClickListener(this);
        }

        @Override
        public void bind(MsgMeta msgMeta, int position) {
            super.bind(msgMeta, position);

            timeTv.setText(DateFormat.format("yyyy-MM-dd-hh:mm", msgMeta.getTimestamp()));
//            if (msgMeta.getPicUrl() != null) {
//                contentIv.setVisibility(View.GONE);
                ImageHelper.displayImage(context, contentIv, msgMeta.getPicUrl());
//            } else {
//                contentIv.setVisibility(View.GONE);
//            }
        }

        @Override
        public void onClick(View v) {
            if (myList != null) {
                myList.onItemClick(getData(), v.getId(), getAdapterPosition());
            }
        }
    }

    class SystemMsgViewHolder3 extends BaseViewHolder<MsgMeta> implements View.OnClickListener {
        @BindView(R.id.sys_msg_time_tv)
        TextView timeTv;
        @BindView(R.id.content_iv)
        ImageView contentIv;
        @BindView(R.id.content_title_tv)
        TextView contentTitleTv;
        @BindView(R.id.content_tv)
        TextView contentTv;
        @BindView(R.id.link_title_tv)
        TextView linkTitleTv;

        private SystemMsgViewHolder3(View v) {
            super(v);
            v.setOnClickListener(this);
        }

        @Override
        public void bind(MsgMeta msgMeta, int position) {
            super.bind(msgMeta, position);

            timeTv.setText(DateFormat.format("yyyy-MM-dd-hh:mm", msgMeta.getTimestamp()));
            if (msgMeta.getMetaTitle() != null)
                contentTitleTv.setText(msgMeta.getMetaTitle());
            else
                contentTitleTv.setVisibility(View.GONE);
            if (msgMeta.getMetaValue() != null) {
//                contentTv.setText(msgMeta.getMetaValue());
                linkTextUtil(msgMeta.getMetaValue(), contentTv);
            } else
                contentTv.setVisibility(View.GONE);
            if (msgMeta.getLinkTitle() != null)
                linkTitleTv.setText(msgMeta.getLinkTitle());
            else
                linkTitleTv.setVisibility(View.GONE);

            if (msgMeta.getPicUrl() != null) {
                contentIv.setVisibility(View.VISIBLE);
                ImageHelper.displayImage(context, contentIv, msgMeta.getPicUrl());
            } else {
                contentIv.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            if (myList != null) {
                myList.onItemClick(getData(), v.getId(), getAdapterPosition());
            }
        }
    }

    class LinkText {
        String text;
        String textInner;
        String textUrl;
        String textUrlInner;

        int start1;
        int end1;
        int start2;
        int end2;
    }

    public void linkTextUtil (String textOirgin, TextView tv) {
        String text = textOirgin;
        ArrayList<LinkText> textList = new ArrayList<LinkText>();
        while (true) {
            LinkText lt = new LinkText();
            lt.start1 = text.indexOf("[");
            if (lt.start1 != -1) {
                lt.end1 = text.indexOf("]");
                lt.start2 = text.indexOf("(");
                lt.end2 = text.indexOf(")");
                char c = text.charAt(lt.start1);
                if (lt.end1 > lt.start1 && lt.start2 > lt.start1 && lt.end2 > lt.start1) {
                    lt.textInner = text.substring(lt.start1 + 1, lt.end1);
                    lt.text = "[" + lt.textInner + "]";
                    lt.textUrlInner = text.substring(lt.start2 + 1, lt.end2);
                    lt.textUrl = "(" + lt.textUrlInner + ")";

                    text = text.substring(0, lt.start1) + text.substring(lt.end2 + 1, text.length());
                    textOirgin = lt.textInner + text;
                    lt.end1 -= 2;
//                    text.replace(lt.text, "");
//                    text.replace(lt.textUrl, "");
//                    textOirgin.replace(lt.textUrl, "");
                    textList.add(lt);
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        final SpannableStringBuilder style = new SpannableStringBuilder();
        //设置文字
        style.append(textOirgin);

        if (textList.size() > 0) {
            for (final LinkText lt : textList) {
                int clickStart = textOirgin.indexOf(lt.textInner);
                int clickEnd = clickStart + lt.textInner.length();
//            textOirgin.replace(lt.textInner, lt.text);

                //设置部分文字点击事件
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
//                        System.out.println(lt.textUrlInner);
                        SchemaUtil.doRouter(context,lt.textUrlInner);
                    }
                };
                style.setSpan(clickableSpan, clickStart, clickEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv.setText(style);

                //设置部分文字颜色
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#0000FF"));
                style.setSpan(foregroundColorSpan, clickStart, clickEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                //配置给TextView
                tv.setMovementMethod(LinkMovementMethod.getInstance());
                tv.setText(style);
            }
        } else {
            tv.setText(textOirgin);
        }


//        String textStr = text;
//        boolean con = true;
//        int fromIndex = 0;
//
//        String rltStr = "";
//
//        while (con) {
//            int start1 = textStr.indexOf("[");
//            int end1 = textStr.indexOf("]");
//            int start2 = textStr.indexOf("(");
//            int end2 = textStr.indexOf(")");
//            if (start1 != -1 && end1 != -1 && start2 != -1 && end2 != -1) {
//                LinkText lt = new LinkText();
//                lt.textInner = textStr.substring(start1, end1);
//                lt.text = "[" + lt.textInner + "]";
//                lt.textUrlInner = textStr.substring(start2, end2);
//                lt.textUrl = "(" + lt.textUrlInner + ")";
//                textList.add(lt);
//
//                textStr = textStr.substring(end2);
//
//                text.replace(lt.text, lt.textInner);
//                text.replace(lt.textUrl, "");
//            } else {
//                con = false;
//            }
//        }
//
//        final SpannableStringBuilder style = new SpannableStringBuilder();
//        //设置文字
//        style.append(text);
//
//        for (LinkText lt : textList) {
//            int clickStart = text.indexOf(lt.text);
//            int clickEnd = clickStart + lt.text.length();
//            text.replace(lt.textInner, lt.text);
//
//            //设置部分文字点击事件
//            ClickableSpan clickableSpan = new ClickableSpan() {
//                @Override
//                public void onClick(View widget) {
//
//                }
//            };
//            style.setSpan(clickableSpan, 11, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            tv.setText(style);
//
//            //设置部分文字颜色
//            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#0000FF"));
//            style.setSpan(foregroundColorSpan, 11, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//            //配置给TextView
//            tv.setMovementMethod(LinkMovementMethod.getInstance());
//            tv.setText(style);
//        }
//
//
//
//
//        Pattern p = Pattern.compile("(\\[[^\\]]*\\])");
//        Matcher m = p.matcher(text);
//        while(m.find()) {
//            m.group().substring(1, m.group().length() - 1);
//        }


    }
}