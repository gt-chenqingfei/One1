package com.oneone.modules.entry.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.oneone.R;
import com.oneone.modules.user.bean.ShowCaseUserInfo;
import com.oneone.widget.AvatarImageView;

public class SingleSuggestAdapter extends BaseAdapter {
    private HashMap<Integer, View> convertViewMap = new HashMap<Integer, View>();
    private Context context;
    private List<ShowCaseUserInfo> userInfoList;

    public void clearMap() {
        convertViewMap.clear();
    }

    public SingleSuggestAdapter(Context context, List<ShowCaseUserInfo> userInfoList) {
        super();
        this.context = context;
        this.userInfoList = userInfoList;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (userInfoList != null) {
            count = userInfoList.size();
        }
        return count;
    }

    @Override
    public Object getItem(int arg0) {
        return userInfoList.get(arg0);
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
                    R.layout.adapter_single_suggest, null);

            holder = new ViewHolder();
            holder.userPhotoIv = convertView.findViewById(R.id.user_photo_iv);
            holder.userNameTv = convertView.findViewById(R.id.user_name_tv);
            holder.userInfoTv = convertView.findViewById(R.id.user_into_tv);

            convertView.setTag(holder);
            convertViewMap.put(position, convertView);

            ShowCaseUserInfo userInfo = userInfoList.get(position);
            holder.userPhotoIv.init(userInfo.getUserInfo(), false);
            String nickname = userInfo.getUserInfo().getNickname();
            if (nickname.length() > 3) {
                nickname = nickname.substring(0, 3) + "···";
            }
            holder.userNameTv.setText(nickname);
            holder.userInfoTv.setText(userInfo.getUserInfo().getAge() + "," + userInfo.getUserInfo().getCity());
        }
        return convertView;
    }

    class ViewHolder {
        AvatarImageView userPhotoIv;
        TextView userNameTv;
        TextView userInfoTv;
    }


}
