package com.oneone.framework.android.utils;

import com.oneone.framework.android.ApplicationContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * File operating utility
 *
 * @author johnson
 */
public final class FileUtils {
    static Logger logger = LoggerFactory.getLogger(FileUtils.class);
    private static ConcurrentHashMap<String, ReentrantReadWriteLock> fileLocks = new ConcurrentHashMap();

    /**
     * Copy the {@code src} to {@code dest}
     *
     * @param dest The destination file
     * @param src  The source file
     * @throws IOException
     */
    public static void copy(File dest, File src) throws IOException {
        final byte[] buffer = new byte[8192];

        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            if (!dest.exists()) {
                dest.createNewFile();
            }

            fis = new FileInputStream(src);
            fos = new FileOutputStream(dest);

            for (int n = 0; (-1 != (n = fis.read(buffer))); ) {
                fos.write(buffer, 0, n);
            }

            fos.flush();
        } finally {
            if (null != fis) {
                try {
                    fis.close();
                } catch (IOException e) {
                }
                fis = null;
            }

            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException e) {
                }
                fos = null;
            }
        }
    }

    /**
     * Delete the specified file
     *
     * @param f         The file to be deleted
     * @param recursive The value that indicated whether delete the specified file
     *                  recursively or not if the specified file is a directory
     * @return
     */
    public static boolean delete(File f, boolean recursive) {
        if (!recursive || !f.isDirectory())
            return f.delete();

        boolean result = true;
        final File[] files = f.listFiles();
        if (files == null || files.length == 0)
            return false;
        for (int i = 0; i < files.length; i++) {
            result = delete(files[i], recursive) && result;
        }

        return f.delete() && result;
    }

    private FileUtils() {
    }

    public static byte[] readContentBytesFromFile(File fileForRead) {
        if (fileForRead == null) {
            logger.error("null file object.");
            return null;
        } else if (fileForRead.exists() && fileForRead.isFile()) {
            ReentrantReadWriteLock.ReadLock readLock = getLock(fileForRead.getAbsolutePath()).readLock();
            readLock.lock();
            Object data = null;
            BufferedInputStream input = null;

            try {
                byte[] data1 = new byte[(int) fileForRead.length()];
                int e = 0;
                input = new BufferedInputStream(new FileInputStream(fileForRead), 8192);

                while (e < data1.length) {
                    int bytesRemaining = data1.length - e;
                    int bytesRead = input.read(data1, e, bytesRemaining);
                    if (bytesRead > 0) {
                        e += bytesRead;
                    }
                }

                byte[] bytesRemaining1 = data1;
                return bytesRemaining1;
            } catch (IOException var10) {
                logger.error("Exception during file read", var10);
            } finally {
                closeQuietly(input);
                readLock.unlock();
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        } else {
            logger.debug("not file object", new FileNotFoundException());
            return null;
        }
    }

    private static ReentrantReadWriteLock getLock(String path) {
        ReentrantReadWriteLock lock = (ReentrantReadWriteLock) fileLocks.get(path);
        if (lock == null) {
            lock = new ReentrantReadWriteLock();
            ReentrantReadWriteLock oldLock = (ReentrantReadWriteLock) fileLocks.putIfAbsent(path, lock);
            if (oldLock != null) {
                lock = oldLock;
            }
        }

        return lock;
    }

    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException var2) {
            logger.error(var2.toString());
        }
    }

    public static File getPaasDocumentDir() {
        if (ApplicationContext.getInstance() == null) {
            throw new IllegalStateException("applicationContext is null, Please call AVOSCloud.initialize first");
        } else {
            return ApplicationContext.getInstance().getDir("Paas", 0);
        }
    }
}
