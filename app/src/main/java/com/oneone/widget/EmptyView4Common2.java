package com.oneone.widget;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.oneone.R;
import com.oneone.api.constants.Role;
import com.oneone.framework.ui.BaseView;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.modules.user.HereUser;
import com.oneone.schema.SchemaUtil;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import butterknife.BindView;

/**
 * Created by here on 18/7/10.
 */

@LayoutResource(R.layout.empty_view_4_common_2)
public class EmptyView4Common2 extends BaseView implements View.OnClickListener {
    public static final String EMPTY_DEFAULT_ANIM = "empty_default.json";
    public static final String EMPTY_MY_FOLLOW_ANIM = "zhayan.json";
    public static final String EMPTY_FOLLOW_ME_ANIM = "paizhao.json";
    public static final String EMPTY_MY_LIKE_ANIM = "zhayan.json";
    public static final String EMPTY_LIKE_EACHOTHER_ANIM = "zhayan.json";
    public static final String EMPTY_LIKE_ME_ANIM = "zhayan.json";

    public interface EMPTY_TYPE {
        public static final int EMPTY_DEFAULT = 0;
        public static final int EMPTY_MY_FOLLOW = 1;
        public static final int EMPTY_FOLLOW_ME = 2;
        public static final int EMPTY_MY_LIKE = 3;
        public static final int EMPTY_LIKE_EACHOTHER = 4;
        public static final int EMPTY_LIKE_ME = 5;
    }

    @BindView(R.id.no_data_iv)
    LottieAnimationView animView;
    @BindView(R.id.no_data_tv)
    TextView noDataTv;
    @BindView(R.id.oper_btn)
    Button openBtn;

    private int emptyType;

    public EmptyView4Common2(Context context, int emptyType) {
        super(context);
        this.emptyType = emptyType;

        int animWidth = 0;
        int animHeight = 0;

        switch (emptyType) {
            case EMPTY_TYPE.EMPTY_DEFAULT:
                animView.setAnimation(EMPTY_DEFAULT_ANIM, LottieAnimationView.CacheStrategy.Weak);
                animView.setRepeatCount(LottieDrawable.INFINITE);
                animView.playAnimation();

                noDataTv.setText(getResources().getString(R.string.str_common_no_data_text));
//                openBtn.setText(getResources().getString(R.string.str_empty_my_like_btn));
                animWidth = DensityUtil.dp2px(104);
                animHeight = DensityUtil.dp2px(120);
                break;
            case EMPTY_TYPE.EMPTY_MY_FOLLOW:
                animView.setAnimation(EMPTY_MY_FOLLOW_ANIM, LottieAnimationView.CacheStrategy.Weak);
                animView.setRepeatCount(LottieDrawable.INFINITE);
                animView.playAnimation();

                noDataTv.setText(getResources().getString(R.string.str_empty_my_follower));
                if (HereUser.getInstance().getUserInfo().getRole() == Role.SINGLE) {
                    openBtn.setVisibility(View.VISIBLE);
                    openBtn.setText(getResources().getString(R.string.str_empty_my_follower_btn));
                    openBtn.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SchemaUtil.doRouter(getContext(), "oneone://m.oneone.com/home/tab?tabCurrent=explore");
                        }
                    });
                } else {
                    openBtn.setVisibility(View.GONE);
                }

                animWidth = DensityUtil.dp2px(84);
                animHeight = DensityUtil.dp2px(120);
                break;
            case EMPTY_TYPE.EMPTY_FOLLOW_ME:
                animView.setAnimation(EMPTY_FOLLOW_ME_ANIM, LottieAnimationView.CacheStrategy.Weak);
                animView.setRepeatCount(LottieDrawable.INFINITE);
                animView.playAnimation();

                noDataTv.setText(getResources().getString(R.string.str_empty_follow_me));
                if (HereUser.getInstance().getUserInfo().getRole() == Role.SINGLE) {
                    openBtn.setVisibility(View.VISIBLE);
                    openBtn.setText(getResources().getString(R.string.str_empty_follow_me_btn));
                    openBtn.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SchemaUtil.doRouter(getContext(), "oneone://m.oneone.com/timeline/post");
                        }
                    });
                } else {
                    openBtn.setVisibility(View.GONE);
                }

                animWidth = DensityUtil.dp2px(84);
                animHeight = DensityUtil.dp2px(120);
                break;
            case EMPTY_TYPE.EMPTY_MY_LIKE:
                animView.setAnimation(EMPTY_MY_LIKE_ANIM, LottieAnimationView.CacheStrategy.Weak);
                animView.setRepeatCount(LottieDrawable.INFINITE);
                animView.playAnimation();

                noDataTv.setText(getResources().getString(R.string.str_empty_my_like));
                openBtn.setText(getResources().getString(R.string.str_empty_my_like_btn));
                openBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SchemaUtil.doRouter(getContext(), "oneone://m.oneone.com/home/tab?tabCurrent=explore");
                    }
                });

                animWidth = DensityUtil.dp2px(84);
                animHeight = DensityUtil.dp2px(120);
                break;
            case EMPTY_TYPE.EMPTY_LIKE_EACHOTHER:
                animView.setBackgroundResource(R.drawable.empty_like_eachother_bg);

                noDataTv.setText(getResources().getString(R.string.str_empty_like_eachother));
                openBtn.setText(getResources().getString(R.string.str_empty_like_eachother_btn));
                openBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SchemaUtil.doRouter(getContext(), "oneone://m.oneone.com/timeline/post");
                    }
                });

                animWidth = DensityUtil.dp2px(172);
                animHeight = DensityUtil.dp2px(110);
                break;
            case EMPTY_TYPE.EMPTY_LIKE_ME:
                animView.setAnimation(EMPTY_LIKE_ME_ANIM, LottieAnimationView.CacheStrategy.Weak);
                animView.setRepeatCount(LottieDrawable.INFINITE);
                animView.playAnimation();

                noDataTv.setText(getResources().getString(R.string.str_empty_like_me));
                openBtn.setText(getResources().getString(R.string.str_empty_like_me_btn));
                openBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SchemaUtil.doRouter(getContext(), "oneone://m.oneone.com/home/tab?tabCurrent=explore");
                    }
                });

                animWidth = DensityUtil.dp2px(84);
                animHeight = DensityUtil.dp2px(120);
                break;
        }

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) animView.getLayoutParams();
        params.width = animWidth;
        params.height = animHeight;
    }

    @Override
    public void onClick(View view) {
        switch (emptyType) {
            case EMPTY_TYPE.EMPTY_DEFAULT:
                break;
            case EMPTY_TYPE.EMPTY_MY_FOLLOW:
                break;
            case EMPTY_TYPE.EMPTY_FOLLOW_ME:
                break;
            case EMPTY_TYPE.EMPTY_MY_LIKE:
                break;
            case EMPTY_TYPE.EMPTY_LIKE_EACHOTHER:
                break;
            case EMPTY_TYPE.EMPTY_LIKE_ME:
                break;
        }
    }

    @Override
    public void onViewCreated() {

    }
}
