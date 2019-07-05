package com.oneone.framework.android.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Algorithm related utility
 *
 * @author johnson
 */
public final class AlgorithmUtils {

    private static final Logger logger = LoggerFactory.getLogger(AlgorithmUtils.class);

    /**
     * Returns the MD5 of the specified string
     *
     * @param s
     * @return
     */
    public static String md5(String s) {
        try {
            final MessageDigest md = MessageDigest.getInstance("MD5");
            return toHexString(md.digest(s.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage(), e);
            throw new UnsupportedOperationException(e);
        }
    }

    /**
     * Returns the MD5 of the specified file
     *
     * @param f
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static String md5(File f) throws FileNotFoundException, IOException {
        final byte[] buf = new byte[4096];

        try {
            final MessageDigest md = MessageDigest.getInstance("MD5");
            final InputStream is = new FileInputStream(f);

            try {
                for (int n = 0; (n = is.read(buf)) != -1; ) {
                    md.update(buf, 0, n);
                }
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                    }
                }
            }

            return new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage(), e);
            throw new UnsupportedOperationException(e);
        }
    }

    /**
     * Returns the specified data as hex sequence
     *
     * @param data The data
     * @return
     */
    public static String toHexString(byte[] data) {
        final int n = data.length;
        final StringBuilder hex = new StringBuilder();

        for (int i = 0; i < n; i++) {
            final byte b = data[i];
            hex.append(String.format("%02x", b & 0xff));
        }

        return hex.toString();
    }

    private AlgorithmUtils() {
    }

}
