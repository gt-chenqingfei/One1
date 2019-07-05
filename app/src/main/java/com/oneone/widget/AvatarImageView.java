package com.oneone.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.oneone.Constants;
import com.oneone.R;
import com.oneone.api.constants.Role;
import com.oneone.framework.ui.utils.Res;
import com.oneone.framework.ui.widget.CircleImageView;
import com.oneone.modules.profile.ProfileStater;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.bean.UserInfoBase;
import com.oneone.utils.ImageHelper;
import com.oneone.utils.ToastUtil;
import com.scwang.smartrefresh.layout.util.DensityUtil;

/**
 * Created by here on 18/4/25.
 */

public class AvatarImageView extends CircleImageView implements View.OnClickListener {

    private UserInfoBase userInfoBase;

    public AvatarImageView(Context context) {
        super(context);
    }

    public AvatarImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AvatarImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void init(UserInfoBase baseUserInfo, boolean isClick) {
        if (baseUserInfo == null)
            return;

        if (isClick) {
            setOnClickListener(this);
        }

        this.userInfoBase = baseUserInfo;
        int defaultAvatar = R.drawable.default_avatar_single;
        String avatarUrl = userInfoBase.getAvatar();
        if (TextUtils.equals(HereUser.getInstance().getUserInfo().getUserId(), userInfoBase.getUserId())) {
            if (userInfoBase.getRole() == Role.MATCHER) {
                defaultAvatar = R.drawable.default_avatar_matcher;
                setBorder();
            }
            avatarUrl = userInfoBase.getMyAvatar();
        } else if (Constants.ID.ONE_ONE.equals(baseUserInfo.getUserId())) {
            setOnClickListener(null);
            defaultAvatar = R.drawable.default_avatar_oneone;
        } else {
            if (userInfoBase.getRole() == Role.MATCHER) {
                setBorder();
                defaultAvatar = R.drawable.default_avatar_matcher;
            }
        }

        ImageHelper.displayAvatar(getContext(), this, avatarUrl, defaultAvatar);
    }

    private void setBorder() {
        setBorderWidth(DensityUtil.dp2px(3.5f));
        setBorderColor(0, DensityUtil.dp2px(78), DensityUtil.dp2px(78), 0,
                getResources().getColor(R.color.matcher_photo_border_left_bottom),
                getResources().getColor(R.color.matcher_photo_border_right_top));
    }


    @Override
    public void onClick(View view) {
        UserInfoBase userInfoBase = this.userInfoBase;
        if (TextUtils.isEmpty(userInfoBase.getUserId())) {
            ToastUtil.show(getContext(), Res.getString(R.string.str_user_auth_eror));
            return;
        }
        if (!userInfoBase.getUserId().equals(HereUser.getUserId())) {
            ProfileStater.startWithUserInfo(getContext(), userInfoBase);
        }
    }
}
