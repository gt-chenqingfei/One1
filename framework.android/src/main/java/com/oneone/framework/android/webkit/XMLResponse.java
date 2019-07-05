package com.oneone.framework.android.webkit;

import android.content.res.AssetFileDescriptor;
import android.webkit.WebResourceResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class XMLResponse extends WebResourceResponse {

    public XMLResponse(InputStream data) {
        super("application/xml", "utf-8", data);
    }

    public XMLResponse(AssetFileDescriptor afd) throws IOException {
        this(afd.createInputStream());
    }

    public XMLResponse(String xml) {
        this(new ByteArrayInputStream(xml.getBytes()));
    }

}
