package com.oneone.modules.msg.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.oneone.R;
import com.oneone.framework.ui.widget.CircleImageView;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.modules.user.bean.UserInfoBase;
import com.oneone.utils.ImageHelper;
import com.oneone.widget.AvatarImageView;
import com.scwang.smartrefresh.layout.util.DensityUtil;

/**
 * Created by here on 18/4/28.
 */

public class UserRelationPhotoLayout extends RelativeLayout {
    public static final int LIKE_EACHOTHER = 0;
    public static final int OTHER = 1;

    private int photoLength1;
    private int photoLength2;
    private int relationWidth;
    private int relationHeight;

    public UserRelationPhotoLayout(Context context) {
        super(context);
    }

    public UserRelationPhotoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);

        photoLength1 = params.height;
        photoLength2 = (int) (photoLength1 / 1.05);
        relationHeight = (int) (photoLength1 / 1.5);
        relationWidth = (int) (photoLength1 / 4.2);
    }

    public void setLayout (UserInfoBase leftUserInfo, String rightImgUrl, int type) {
        System.out.println("photoLength1 ======= >" + photoLength1);
        System.out.println("photoLength2 ======= >" + photoLength2);

        AvatarImageView leftPhoto = new AvatarImageView(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(photoLength1, photoLength1);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        addView(leftPhoto, params);
        leftPhoto.init(leftUserInfo, false);
//        ImageHelper.displayAvatar(getContext(), leftPhoto, leftIvUrl);

        CircleImageView rightPhoto = new CircleImageView(getContext());
        if (type == LIKE_EACHOTHER) {
            params = new RelativeLayout.LayoutParams(photoLength1, photoLength1);
        } else {
            params = new RelativeLayout.LayoutParams(photoLength2, photoLength2);
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            params.rightMargin = (int) ((float)(photoLength1 - photoLength2) / 2);

            rightPhoto.setBorderWidth(DensityUtil.dp2px(2));
            rightPhoto.setBorderColor(Color.WHITE);
        }
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        addView(rightPhoto, params);
        ImageHelper.displayAvatar(getContext(), rightPhoto, rightImgUrl);

        if (type == LIKE_EACHOTHER) {
            ImageView photoIntersectIv = new ImageView(getContext());
            params = new RelativeLayout.LayoutParams(relationWidth, relationHeight);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            photoIntersectIv.setBackgroundResource(R.drawable.ic_match_love);
            addView(photoIntersectIv, params);
        }
    }

    public void setLayout (UserInfoBase leftUserInfo, UserInfoBase rightUserInfo, int type) {
        System.out.println("photoLength1 ======= >" + photoLength1);
        System.out.println("photoLength2 ======= >" + photoLength2);

        AvatarImageView leftPhoto = new AvatarImageView(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(photoLength1, photoLength1);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        addView(leftPhoto, params);
        leftPhoto.init(leftUserInfo, false);
//        ImageHelper.displayAvatar(getContext(), leftPhoto, leftIvUrl);

        AvatarImageView rightPhoto = new AvatarImageView(getContext());
        if (type == LIKE_EACHOTHER) {
            params = new RelativeLayout.LayoutParams(photoLength1, photoLength1);
        } else {
            params = new RelativeLayout.LayoutParams(photoLength2, photoLength2);
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            params.rightMargin = (int) ((float)(photoLength1 - photoLength2) / 2);

            rightPhoto.setBorderWidth(DensityUtil.dp2px(2));
            rightPhoto.setBorderColor(Color.WHITE);
        }
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        addView(rightPhoto, params);
        rightPhoto.init(rightUserInfo, false);
//        ImageHelper.displayAvatar(getContext(), rightPhoto, rightIv);

        if (type == LIKE_EACHOTHER) {
            ImageView photoIntersectIv = new ImageView(getContext());
            params = new RelativeLayout.LayoutParams(relationWidth, relationHeight);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            photoIntersectIv.setBackgroundResource(R.drawable.ic_match_love);
            addView(photoIntersectIv, params);
        }
    }

    public void setAvatarPic(UserInfo myUserInfo, UserInfo otherUserInfo, int type) {
        AvatarImageView avatarImageViewMine = new AvatarImageView(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(photoLength1, photoLength1);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        addView(avatarImageViewMine, params);
        avatarImageViewMine.init(myUserInfo, true);

        AvatarImageView avatarImageViewOther = new AvatarImageView(getContext());
        if (type == LIKE_EACHOTHER) {
            params = new RelativeLayout.LayoutParams(photoLength1, photoLength1);
        } else {
            params = new RelativeLayout.LayoutParams(photoLength2, photoLength2);
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            params.rightMargin = (int) ((float)(photoLength1 - photoLength2) / 2);

            avatarImageViewOther.setBorderWidth(DensityUtil.dp2px(2));
            avatarImageViewOther.setBorderColor(Color.WHITE);
        }
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        addView(avatarImageViewOther, params);
        avatarImageViewOther.init(otherUserInfo, true);

        if (type == LIKE_EACHOTHER) {
            ImageView photoIntersectIv = new ImageView(getContext());
            params = new RelativeLayout.LayoutParams(relationWidth, relationHeight);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            photoIntersectIv.setBackgroundResource(R.drawable.ic_match_love);
            addView(photoIntersectIv, params);
        }
    }

}
