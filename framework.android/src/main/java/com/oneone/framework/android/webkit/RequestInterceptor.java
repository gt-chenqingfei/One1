package com.oneone.framework.android.webkit;

import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import java.util.Map;

public interface RequestInterceptor {

    public WebResourceResponse intercept(WebView view, String method, String url, Map<String, String> headers);

}
