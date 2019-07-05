package com.oneone.modules.user.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.api.constants.Role;
import com.oneone.framework.ui.BaseView;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.modules.user.HereUser;
import com.oneone.widget.AvatarImageView;

import butterknife.BindView;

/**
 * Created by here on 18/4/12.
 */

@LayoutResource(R.layout.view_my_matcher_group_list_item_view)
public class MyMatcherGroupListItem extends BaseView {
    @BindView(R.id.user_photo_iv)
    AvatarImageView userPhotoIv;
    @BindView(R.id.user_name_tv)
    TextView userNameTv;
    @BindView(R.id.matcher_icon)
    ImageView matcherIcon;
    @BindView(R.id.user_company_and_title_tv)
    TextView userCompanyAndTitleTv;
    @BindView(R.id.is_new_matcher_iv)
    ImageView isNewMatcherIv;
    @BindView(R.id.matcher_impression_tv)
    TextView matcherImpressionTv;
    @BindView(R.id.wx_nickname_tv)
    TextView matcherWxNicknameTv;
    @BindView(R.id.user_relation_tv)
    TextView userRelationTv;
    @BindView(R.id.no_impression_layout)
    RelativeLayout noImpressionLayout;
    @BindView(R.id.invite_ta_to_write_btn)
    Button inviteTaToWriteBtn;

    public MyMatcherGroupListItem(Context context) {
        super(context);
    }

    public MyMatcherGroupListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onViewCreated() {
        userPhotoIv.init(HereUser.getInstance().getUserInfo(), true);
        if (HereUser.getInstance().getUserInfo().getRole() == Role.MATCHER) {
            matcherIcon.setVisibility(View.VISIBLE);
        }
    }
}
