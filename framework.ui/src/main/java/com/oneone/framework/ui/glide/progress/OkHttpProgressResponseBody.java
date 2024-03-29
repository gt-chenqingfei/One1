package com.oneone.framework.ui.glide.progress;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * @author qingfei.chen
 * @since 17/12/20.
 * Copyright © 2017 HereTech Technology Co.,Ltd. All rights reserved.
 */

public class OkHttpProgressResponseBody extends ResponseBody {
    private final HttpUrl url;
    private final ResponseBody responseBody;
    private final ResponseProgressListener progressListener;
    private BufferedSource bufferedSource;

    public OkHttpProgressResponseBody(HttpUrl url, ResponseBody responseBody,
                                      ResponseProgressListener progressListener) {
        this.url = url;
        this.responseBody = responseBody;
        this.progressListener = progressListener;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                long fullLength = responseBody.contentLength();
                if (bytesRead == -1) { // this source is exhausted
                    totalBytesRead = fullLength;
                } else {
                    totalBytesRead += bytesRead;
                }
                progressListener.update(url, totalBytesRead, fullLength);
                return bytesRead;
            }
        };
    }
}
