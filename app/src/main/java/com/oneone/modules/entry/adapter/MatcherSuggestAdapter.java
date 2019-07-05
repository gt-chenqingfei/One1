package com.oneone.modules.entry.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.framework.ui.utils.ScreenUtil;
import com.oneone.utils.ImageHelper;
import com.oneone.widget.AvatarImageView;

import java.util.ArrayList;
import java.util.HashMap;

public class MatcherSuggestAdapter extends BaseAdapter {
    private HashMap<Integer, View> convertViewMap = new HashMap<Integer, View>();
    private Context context;
    private ArrayList<String> strList;

    public void clearMap() {
        convertViewMap.clear();
    }

    public MatcherSuggestAdapter(Context context, ArrayList<String> strList) {
        super();
        this.context = context;
        this.strList = strList;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (strList != null) {
            count = strList.size();
        }
        return count;
    }

    @Override
    public Object getItem(int arg0) {
        return strList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    private ViewHolder holder = null;

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        convertView = convertViewMap.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.adapter_matcher_suggest, null);

            holder = new ViewHolder();
            holder.userPhotoIv = convertView.findViewById(R.id.user_photo_iv);
            holder.userPhotoIv.setBorderWidth(ScreenUtil.dip2px(2));
            holder.userPhotoIv.setBorderColor(Color.TRANSPARENT);
            holder.userNameTv = convertView.findViewById(R.id.user_name_tv);
            holder.userTitleTv = convertView.findViewById(R.id.user_title_tv);
            holder.singleCountTv = convertView.findViewById(R.id.user_single_count_tv);

            convertView.setTag(holder);
            convertViewMap.put(position, convertView);

            String testStr = strList.get(position);
            ImageHelper.displayAvatar(context, holder.userPhotoIv, "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1522665144219&di=7050bd3f7236f18983c2d7281d8c3ef7&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dpixel_huitu%252C0%252C0%252C294%252C40%2Fsign%3Decfe83b9042442a7ba03f5e5b83bc827%2F728da9773912b31bc2fe74138d18367adab4e17e.jpg");
            holder.userNameTv.setText(testStr);
            holder.userTitleTv.setText("1号媒婆CEO");
            holder.singleCountTv.setText("834位单身");
        }
        return convertView;
    }

    class ViewHolder {
        AvatarImageView userPhotoIv;
        TextView userNameTv;
        TextView userTitleTv;
        TextView singleCountTv;
    }


}
