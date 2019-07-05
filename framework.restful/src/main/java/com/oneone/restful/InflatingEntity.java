package com.oneone.restful;


import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

/**
 * @author qingfei.chen
 * @since 2018/3/27.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 * <p>
 * Enclosing entity to hold stream of gzip decoded data for accessing HttpEntity contents
 */
public class InflatingEntity extends HttpEntityWrapper {
    org.slf4j.Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
    InputStream wrappedStream;
    PushbackInputStream pushbackStream;
    GZIPInputStream gzippedStream;

    public InflatingEntity(HttpEntity wrapped) {
        super(wrapped);
    }

    @Override
    public InputStream getContent() throws IOException {
        wrappedStream = wrappedEntity.getContent();
        pushbackStream = new PushbackInputStream(wrappedStream, 2);
        if (Utils.isInputStreamGZIPCompressed(pushbackStream)) {
            gzippedStream = new GZIPInputStream(pushbackStream);
            return gzippedStream;
        } else {
            return pushbackStream;
        }
    }

    @Override
    public long getContentLength() {
        return wrappedEntity == null ? 0 : wrappedEntity.getContentLength();
    }

    @Override
    public void consumeContent() throws IOException {
        silentCloseInputStream(wrappedStream);
        silentCloseInputStream(pushbackStream);
        silentCloseInputStream(gzippedStream);
        super.consumeContent();
    }

    /**
     * A utility function to close an input stream without raising an exception.
     *
     * @param is input stream to close safely
     */
    private void silentCloseInputStream(InputStream is) {
        try {
            if (is != null) {
                is.close();
            }
        } catch (IOException e) {
            logger.error("Cannot close input stream " + e);
        }
    }
}
