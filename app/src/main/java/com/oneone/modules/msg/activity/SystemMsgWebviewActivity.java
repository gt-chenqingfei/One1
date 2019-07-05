package com.oneone.modules.msg.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.oneone.BaseActivity;
import com.oneone.R;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;

import java.net.URLDecoder;

import butterknife.BindView;

/**
 * Created by here on 18/5/16.
 */

@Route(path = "/web_view/native")
@ToolbarResource(title = R.string.str_my_msg_sys_webview_title_text)
@LayoutResource(R.layout.activity_system_msg_webview)
public class SystemMsgWebviewActivity extends BaseActivity {
    @BindView(R.id.webview)
    WebView webview;

    @Autowired
    String url;

    public static void startActivity(Activity context, String url) {
        Intent intent = new Intent(context, SystemMsgWebviewActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);


        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        String url = getIntent().getExtras().getString("url");
        url = URLDecoder.decode(url);
        webview.loadUrl(url);
    }
}
