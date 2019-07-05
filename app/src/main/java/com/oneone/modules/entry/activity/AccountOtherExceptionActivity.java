package com.oneone.modules.entry.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.oneone.BaseActivity;
import com.oneone.R;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.utils.ImageHelper;

import butterknife.BindView;

/**
 * Created by here on 18/4/9.
 */
@LayoutResource(R.layout.activity_other_account_exception_page)
public class AccountOtherExceptionActivity extends BaseActivity {
    @BindView(R.id.gif_iv)
    ImageView gifIv;

    private String otherUserName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            otherUserName = bundle.getString("otherUserName");
        }

        initView();
    }

    public void initView() {
        ImageHelper.displayGif(R.drawable.other_account_exception_gif, gifIv);
    }
}
