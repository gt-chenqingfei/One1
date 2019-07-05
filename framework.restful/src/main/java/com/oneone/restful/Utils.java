package com.oneone.restful;

import org.apache.http.NameValuePair;

import java.io.IOException;
import java.io.PushbackInputStream;
import java.util.zip.GZIPInputStream;

/**
 * @author qingfei.chen
 * @since 2018/3/27.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class Utils {
    private static final String PARAMETER_SEPARATOR = "&";
    private static final String NAME_VALUE_SEPARATOR = "=";

    /**
     * Checks the InputStream if it contains  GZIP compressed data
     *
     * @param inputStream InputStream to be checked
     * @return true or false if the stream contains GZIP compressed data
     * @throws IOException if read from inputStream fails
     */
    public static boolean isInputStreamGZIPCompressed(final PushbackInputStream inputStream)
            throws IOException {
        if (inputStream == null)
            return false;

        byte[] signature = new byte[2];
        int count = 0;
        try {
            while (count < 2) {
                int readCount = inputStream.read(signature, count, 2 - count);
                if (readCount < 0) return false;
                count = count + readCount;
            }
        } finally {
            inputStream.unread(signature, 0, count);
        }
        int streamHeader = ((int) signature[0] & 0xff) | ((signature[1] << 8) & 0xff00);
        return GZIPInputStream.GZIP_MAGIC == streamHeader;
    }

    public static String format(
            final Iterable<? extends NameValuePair> parameters) {
        final StringBuilder result = new StringBuilder();
        for (final NameValuePair parameter : parameters) {
            final String encodedName = parameter.getName();
            final String encodedValue = parameter.getValue();
            if (result.length() > 0)
                result.append(PARAMETER_SEPARATOR);
            result.append(encodedName);
            if (encodedValue != null) {
                result.append(NAME_VALUE_SEPARATOR);
                result.append(encodedValue);
            }
        }
        return result.toString();
    }
}
