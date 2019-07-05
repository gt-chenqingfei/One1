package com.oneone.modules.feedback.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.oneone.R;
import com.oneone.utils.ImageHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by here on 18/6/12.
 */

public class FeedbackItemPicAdapter extends BaseAdapter {
    private HashMap<Integer, View> mapView = new HashMap<Integer, View>();
    private List<String> imgList;
    private Context context;
    private LayoutInflater inflater;

    public FeedbackItemPicAdapter (Context context, List<String> imgList) {
        this.imgList = imgList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return imgList.size();
    }

    @Override
    public Object getItem(int i) {
        return imgList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup viewGroup) {
        convertView = mapView.get(pos);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_feedback_item_pic, null);
            mapView.put(pos, convertView);
            ViewHolder holder = new ViewHolder();
            holder.picIv = convertView.findViewById(R.id.pic_iv);
            ImageHelper.displayImage(context, holder.picIv, imgList.get(pos));

            convertView.setTag(holder);
        }
        return convertView;
    }

    public class ViewHolder {
        public ImageView picIv;
    }
}
