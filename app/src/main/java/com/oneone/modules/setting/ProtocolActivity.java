package com.oneone.modules.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import com.oneone.BaseActivity;
import com.oneone.Constants;
import com.oneone.R;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;

import butterknife.BindView;

/**
 * Created by here on 18/5/4.
 */

@ToolbarResource(title = R.string.str_about_oneone_protocol_page_title)
@LayoutResource(R.layout.activity_protocol)
public class ProtocolActivity extends BaseActivity {
    @BindView(R.id.protocol_wv)
    WebView protocolWv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    public void initView() {
        protocolWv.loadUrl(Constants.URL.PROTOCOL_URL());
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ProtocolActivity.class));
    }
}
