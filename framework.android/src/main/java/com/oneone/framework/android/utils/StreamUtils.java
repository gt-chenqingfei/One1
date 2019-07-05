package com.oneone.framework.android.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class StreamUtils {

    private static final int BUFFER_SIZE = 1024 * 1024;

    public static final String readAsString(InputStream is) throws IOException {
        return new String(readAsBytes(is));
    }

    public static byte[] readAsBytes(InputStream is) throws IOException {
        final byte[] buf = new byte[BUFFER_SIZE];

        ByteArrayOutputStream baos = null;

        try {
            baos = new ByteArrayOutputStream();

            for (int n = 0; -1 != (n = is.read(buf)); ) {
                baos.write(buf, 0, n);
            }

            baos.flush();
            return baos.toByteArray();
        } finally {
            if (null != baos) {
                try {
                    baos.close();
                } catch (IOException e) {
                }
                baos = null;
            }
        }
    }

    private StreamUtils() {
    }

}
