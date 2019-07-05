package com.oneone.framework.android.cache;

import android.content.Context;

import com.oneone.framework.android.utils.FileUtils;

import java.io.File;

/**
 * The cache manager
 *
 * @author johnson
 */
@SuppressWarnings(value = {"unchecked", "deprecation"})
public class CacheManager {

    /**
     * Clear all cache data
     *
     * @param context
     */
    public void clear(Context context) {
        this.clearExternalCache(context);
        this.clearInternalCache(context);
    }

    /**
     * Clear external cache data
     *
     * @param context
     */
    public void clearExternalCache(Context context) {
        final File root = context.getExternalCacheDir();
        if (null == root) {
            return;
        }
        final File[] files = root.listFiles();
        if (files == null || files.length == 0)
            return;
        for (File file : files) {
            FileUtils.delete(file, true);
        }
    }

    /**
     * Clear internal cache data
     *
     * @param context
     */
    public void clearInternalCache(Context context) {
        final File root = context.getCacheDir();
        if (null == root) {
            return;
        }
        final File[] files = root.listFiles();
        if (files == null || files.length == 0)
            return;
        for (File file : files) {
            FileUtils.delete(file, true);
        }
    }

}
