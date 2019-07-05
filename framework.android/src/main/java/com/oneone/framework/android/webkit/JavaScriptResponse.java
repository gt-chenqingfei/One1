package com.oneone.framework.android.webkit;

import android.content.res.AssetFileDescriptor;
import android.webkit.WebResourceResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class JavaScriptResponse extends WebResourceResponse {

    public JavaScriptResponse(InputStream data) {
        super("application/javascript", "utf-8", data);
    }

    public JavaScriptResponse(AssetFileDescriptor afd) throws IOException {
        this(afd.createInputStream());
    }

    public JavaScriptResponse(String text) {
        this(new ByteArrayInputStream(text.getBytes()));
    }

}
