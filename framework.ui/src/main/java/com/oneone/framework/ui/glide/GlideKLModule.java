package com.oneone.framework.ui.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.request.target.ViewTarget;
import com.oneone.framework.android.environment.CacheConfig;
import com.oneone.framework.android.environment.ExternalStorage;
import com.oneone.framework.android.environment.StorageType;
import com.oneone.framework.ui.R;
import com.oneone.framework.ui.glide.progress.DispatchingProgressListener;
import com.oneone.framework.ui.glide.progress.OkHttpProgressResponseBody;
import com.oneone.framework.ui.glide.progress.OkHttpUrlLoader;
import com.oneone.framework.ui.glide.progress.ResponseProgressListener;
import com.oneone.framework.ui.glide.progress.UIProgressListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by qingfei.chen on 17/9/21.
 */

public class GlideKLModule implements GlideModule {

    @Override
    public void applyOptions(final Context context, GlideBuilder builder) {
        /* 设置标识 **/
        ViewTarget.setTagId(R.id.glide_image_tag);
        /* 调整图片的色彩 */
        builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565);
        /* 设置文件缓存的大小 */
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context,
                new File(ExternalStorage.getInstance().getDirectoryByDirType(StorageType.CACHE_GLIDE_DIRECTORY_NAME)).getName(),
                CacheConfig.DEFAULT_DISK_CACHE_SIZE));
        /* 设置内存的缓存大小 */
        builder.setMemoryCache(new LruResourceCache((int) CacheConfig.DEFAULT_MEMORY_CACHE_SIZE));

        /* 设置缓存的路径 */
        builder.setDiskCache(new DiskCache.Factory() {
            @Override
            public DiskCache build() {
                File cacheLocation = new File(ExternalStorage.getInstance().getDirectoryByDirType(StorageType.CACHE_GLIDE_DIRECTORY_NAME));
                cacheLocation.mkdirs();
                return DiskLruCacheWrapper.get(cacheLocation, CacheConfig.DEFAULT_DISK_CACHE_SIZE);
            }
        });


    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(createInterceptor(new DispatchingProgressListener()))
                .build();
        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(client));
    }



    private static Interceptor createInterceptor(final ResponseProgressListener listener) {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response response = chain.proceed(request);
                return response.newBuilder()
                        .body(new OkHttpProgressResponseBody(request.url(), response.body(), listener))
                        .build();
            }
        };
    }


    public static void forget(String url) {
        DispatchingProgressListener.forget(url);
    }

    public static void expect(String url, UIProgressListener listener) {
        DispatchingProgressListener.expect(url, listener);
    }




}
