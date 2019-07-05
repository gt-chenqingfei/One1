package com.oneone.framework.ui.glide;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.ImageView;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.GifRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;
import com.oneone.framework.ui.R;
import com.oneone.framework.ui.glide.transformation.CircleTransform;
import com.oneone.framework.ui.glide.transformation.RoundTransform;
import com.oneone.framework.ui.utils.ScreenUtil;
import com.oneone.framework.ui.widget.CircleImageView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.concurrent.ExecutionException;


/**
 * Created by qingfei.chen on 17/9/21.
 */

public class GlideUtils {
    static final Logger logger = LoggerFactory.getLogger(GlideUtils.class.getSimpleName());

    /**
     * 简单图片加载回调
     *
     * @param <T> 图片url 或资源id 或 文件
     * @param <K> 返回的资源,GlideDrawable或者Bitmap或者GifDrawable,ImageView.setImageRecourse设置
     */
    public interface ImageLoadListener<T, K> {

        /**
         * 图片加载成功回调
         *
         * @param uri      图片url 或资源id 或 文件
         * @param view     目标载体，不传则为空
         * @param resource 返回的资源,GlideDrawable或者Bitmap或者GifDrawable,ImageView.setImageRecourse设置
         */
        void onLoadingComplete(T uri, ImageView view, K resource);

        /**
         * 图片加载异常返回
         *
         * @param source 图片地址、File、资源id
         * @param e      异常信息
         */
        void onLoadingError(T source, Exception e);

    }


    /**
     * 详细加载图片加载回调
     *
     * @param <T> 图片url 或资源id 或 文件
     * @param <K> 返回的资源
     */
    public interface ImageLoadDetailListener<T, K> {

        /**
         * 图片加载成功回调
         *
         * @param uri      图片url 或资源id 或 文件
         * @param view     目标载体，不传则为空
         * @param resource 返回的资源,GlideDrawable或者Bitmap或者GifDrawable,ImageView.setImageRecourse设置
         */
        void onLoadingComplete(T uri, ImageView view, K resource);

        /**
         * 图片加载异常返回
         *
         * @param source        图片地址、File、资源id
         * @param errorDrawable 加载错误占位图
         * @param e             异常信息
         */
        void onLoadingError(T source, Drawable errorDrawable, Exception e);

        /**
         * 加载开始
         *
         * @param source      图片来源
         * @param placeHolder 开始加载占位图
         */
        void onLoadingStart(T source, Drawable placeHolder);

    }


    /**
     * 根据上下文和 url获取 Glide的DrawableTypeRequest
     *
     * @param context 上下文
     * @param url     图片连接
     * @param <T>     Context类型
     * @param <K>     url类型
     * @return 返回DrawableTypeRequst<K> 类型
     */
    private static <T, K> DrawableTypeRequest<K> getDrawableTypeRequest(T context, K url) {
        DrawableTypeRequest<K> type = null;
        try {
            K tempUrl = url instanceof String ? (K) new KeyGlideUrl((String) url) : url;
            if (context instanceof android.support.v4.app.Fragment) {
                type = Glide.with((android.support.v4.app.Fragment) context).load(url);
            } else if (context instanceof android.app.Fragment) {
                type = Glide.with((android.app.Fragment) context).load(url);
            } else if (context instanceof Activity) {    //包括FragmentActivity
                type = Glide.with((Activity) context).load(url);
            } else if (context instanceof Context) {
                type = Glide.with((Context) context).load(url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return type;
    }


    /**
     * 图片加载监听类
     *
     * @param <T> 图片链接 的类型
     * @param <K> 图片资源返回类型
     * @param <Z> 返回的图片url
     */
    private static class GlideListener<T, K, Z> implements RequestListener<T, K> {

        ImageLoadListener<Z, K> imageLoadListener = null;
        Z url;
        ImageView imageView = null;

        GlideListener(ImageLoadListener<Z, K> imageLoadListener, Z url, ImageView view) {
            this.imageLoadListener = imageLoadListener;
            this.url = url;
            this.imageView = view;
        }

        GlideListener(ImageLoadListener<Z, K> imageLoadListener, Z url) {
            this.imageLoadListener = imageLoadListener;
            this.url = url;
        }

        GlideListener(Z url) {
            this.url = url;
        }

        @Override
        public boolean onResourceReady(K resource, T model, Target<K> target, boolean isFromMemoryCache, boolean isFirstResource) {
            if (null != imageLoadListener) {
                if (imageView != null) {
                    imageLoadListener.onLoadingComplete(url, imageView, resource);
                } else {
                    imageLoadListener.onLoadingComplete(url, null, resource);
                }
            }

            return false;
        }

        @Override
        public boolean onException(Exception e, T model, Target<K> target, boolean isFirstResource) {

            logger.error("Glide图片加载失败:" + e + " 地址为:" + url);

            if (imageLoadListener != null) {
                imageLoadListener.onLoadingError(url, e);
            }
            return false;
        }
    }


    /* 滚动的回调 */
    public interface OnScrollListener {
        void onScrolled(RecyclerView recyclerView, int dx, int dy);
    }

    /**
     * 滚动的时候不加载图片
     *
     * @param recyclerView
     * @param mContext
     */
    public static void loadImageScrollWithRecyclerView(RecyclerView recyclerView, final Context mContext, final OnScrollListener scrollListener) {
        final boolean[] sIsScrolling = {false};
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (scrollListener != null)
                    scrollListener.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                    ((StaggeredGridLayoutManager) recyclerView.getLayoutManager()).invalidateSpanAssignments();
                }
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    sIsScrolling[0] = true;
                    Glide.with(mContext).pauseRequests();
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (sIsScrolling[0] == true) {
                        Glide.with(mContext).resumeRequests();

                    }
                    sIsScrolling[0] = false;
                }
            }
        });

    }


    /**
     * 获取存储器上的图片,回调返回GlideDrawable
     *
     * @param context           上下文年
     * @param file              文件File
     * @param imageLoadListener 回调监听器
     */
    public static <T> DrawableRequestBuilder<File> loadImage(T context, @NonNull File file, @NonNull ImageLoadListener<File, GlideDrawable> imageLoadListener) {
        DrawableTypeRequest<File> type = getDrawableTypeRequest(context, file);
        if (type != null) {
            return type.listener(new GlideListener<File, GlideDrawable, File>(imageLoadListener, file));
        } else {
            return null;
        }
    }

    /**
     * 获取资源中的图片，回调返回GlideDrawable
     *
     * @param context           上下文
     * @param resourceId        资源id
     * @param imageLoadListener 回调监听器
     */
    public static <T> DrawableRequestBuilder<Integer> loadImage(T context, @DrawableRes int resourceId, @NonNull ImageLoadListener<Integer, GlideDrawable> imageLoadListener) {
        DrawableTypeRequest<Integer> type = getDrawableTypeRequest(context, resourceId);
        if (type != null) {
            return type.listener(new GlideListener<Integer, GlideDrawable, Integer>(imageLoadListener, resourceId));
        } else {
            return null;
        }

    }

    /**
     * 获取网络图片，回调返回 GlideDrawable
     *
     * @param context           上下文
     * @param url               图片url
     * @param imageLoadListener 回调监听器
     */
    public static <T> DrawableRequestBuilder<String> loadImage(T context, @NonNull final String url, @NonNull ImageLoadListener<String, GlideDrawable> imageLoadListener) {
        DrawableTypeRequest<String> type = getDrawableTypeRequest(context, url);
        if (type != null) {
            return type.listener(new GlideListener<String, GlideDrawable, String>(imageLoadListener, url));
        } else {
            return null;
        }

    }

    /**
     * 加载存储器上的图片到目标载体
     *
     * @param file      文件File
     * @param imageView 要显示到的图片ImageView
     */
    public static Target<GlideDrawable> loadImage(@NonNull final File file, @NonNull ImageView imageView, ImageLoadListener<File, GlideDrawable> imageLoadListener) {
        return getDrawableTypeRequest(imageView.getContext(), file)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//禁用磁盘缓存
                .skipMemoryCache(true)//跳过内存缓存
                .dontAnimate()
                .listener(new GlideListener<File, GlideDrawable, File>(imageLoadListener, file, imageView))
                .into(imageView);
    }

    /**
     * 加载资源中的图片到目标载体
     *
     * @param resourceId 资源id
     * @param imageView  图片View
     */
    public static Target<GlideDrawable> loadImage(@DrawableRes int resourceId,
                                                  @NonNull ImageView imageView,
                                                  ImageLoadListener<Integer, GlideDrawable> imageLoadListener) {
        return getDrawableTypeRequest(imageView.getContext(), resourceId)
                .listener(new GlideListener<Integer, GlideDrawable, Integer>(imageLoadListener, resourceId, imageView))
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageView);
    }


    /**
     * 加载成圆形头像到普通ImageView，有动画效果
     *
     * @param url               图片url
     * @param imageView         要显示到的ImageView
     * @param imageLoadListener 加载回调监听器
     * @param parms             设置占位符和加载失败图片
     * @return 返回Target<GlideDrawable>
     */
    public static Target<GlideDrawable> loadCircleImage(@NonNull String url, @NonNull ImageView imageView, ImageLoadListener<String, GlideDrawable> imageLoadListener, int... parms) {
        DrawableTypeRequest<String> type = getDrawableTypeRequest(imageView.getContext(), url);
        if (type != null) {
            if (parms != null && parms.length > 0) {
                type.placeholder(parms[0]);   //占位符
                if (parms.length > 1) {
                    type.error(parms[1]);    //图片加载失败显示图片
                }
            }
            type.transform(new CircleTransform(imageView.getContext()));
            return type.listener(new GlideListener<String, GlideDrawable, String>(imageLoadListener, url, imageView))
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(imageView);
        }
        return null;

    }

    /**
     * 加载成圆形头像到普通ImageView，有动画效果
     *
     * @param url               图片url
     * @param imageView         要显示到的ImageView
     * @param imageLoadListener 加载回调监听器
     * @param parms             设置占位符和加载失败图片
     * @return 返回Target<GlideDrawable>
     */
    public static Target<GlideDrawable> loadRoundImage(@NonNull String url, @NonNull ImageView imageView, ImageLoadListener<String, GlideDrawable> imageLoadListener, int radius, int... parms) {
        DrawableTypeRequest<String> type = getDrawableTypeRequest(imageView.getContext(), url);
        if (type != null) {
            if (parms != null && parms.length > 0) {
                type.placeholder(parms[0]);   //占位符
                if (parms.length > 1) {
                    type.error(parms[1]);    //图片加载失败显示图片
                }
            }
            type.transform(new RoundTransform(imageView.getContext(), radius));
            return type.listener(new GlideListener<String, GlideDrawable, String>(imageLoadListener, url, imageView))
                    .into(imageView);
        }
        return null;

    }


    /**
     * 加载网络图片到指定Imageview，支持CircleImageView
     *
     * @param url               图片url
     * @param imageView         要显示的Imageview
     * @param imageLoadListener 图片加载回调
     * @param parms             第一个是error的图片
     */
    public static <T> Target<GlideDrawable> loadImage(T context, @NonNull String url, @NonNull ImageView imageView,
                                                      ImageLoadListener<String, GlideDrawable> imageLoadListener, int... parms) {
        DrawableTypeRequest<String> type = getDrawableTypeRequest(context, url);
        if (type != null) {
            type.asBitmap();
            if (parms != null && parms.length > 0) {
                type.placeholder(parms[0]);   //占位符
                if (parms.length > 1) {
                    type.error(parms[1]);    //图片加载失败显示图片
                }
            }

            //单张CircleImageView不允许动画，不然会不显示,
            if (imageView instanceof CircleImageView) {
                type.dontAnimate();
            }
            return type
                    .listener(new GlideListener<String, GlideDrawable, String>(imageLoadListener, url, imageView))
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(imageView);
        } else {
            return null;
        }
    }

    /**
     * 加载网络图片到指定Imageview，支持CircleImageView
     *
     * @param url               图片url
     * @param imageView         要显示的Imageview
     * @param imageLoadListener 图片加载回调
     * @param parms             第一个是error的图片
     */
    public static <T> Target<GlideDrawable> loadImageNoCache(T context, @NonNull String url, @NonNull ImageView imageView,
                                                             ImageLoadListener<String, GlideDrawable> imageLoadListener, int... parms) {
        DrawableTypeRequest<String> type = getDrawableTypeRequest(context, url);
        if (type != null) {
            type.asBitmap();
            if (parms != null && parms.length > 0) {
                type.placeholder(parms[0]);   //占位符
                if (parms.length > 1) {
                    type.error(parms[1]);    //图片加载失败显示图片
                }
            }

            //单张CircleImageView不允许动画，不然会不显示,
            if (imageView instanceof CircleImageView) {
                type.dontAnimate();
            }
            return type
                    .listener(new GlideListener<String, GlideDrawable, String>(imageLoadListener, url, imageView))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                    .into(imageView);
        } else {
            return null;
        }
    }

    /**
     * 加载网络图片到指定Imageview，支持CircleImageView
     *
     * @param url               图片url
     * @param imageView         要显示的Imageview
     * @param imageLoadListener 图片加载回调
     * @param parms             第一个是error的图片
     */
    public static void loadFrameImage(Context context, @NonNull String url, @NonNull ImageView imageView,
                                      ImageLoadListener<String, GlideDrawable> imageLoadListener, int... parms) {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(parms[0])
                .error(parms[1])
                .into(imageView);
    }

    /**
     * 加载网络图片到指定Imageview，支持CircleImageView
     *
     * @param url               图片url
     * @param imageView         要显示的Imageview
     * @param imageLoadListener 图片加载回调
     * @param parms             第一个是error的图片
     */
    public static <T> Target<GlideDrawable> loadImageThumbnail(T context, @NonNull String
            url, @NonNull ImageView imageView,
                                                               ImageLoadListener<String, GlideDrawable> imageLoadListener, float thumbnail, int...
                                                                       parms) {
        DrawableTypeRequest<String> type = getDrawableTypeRequest(context, url);
        if (type != null) {
            type.asBitmap();
            if (parms != null && parms.length > 0) {
                type.placeholder(parms[0]);   //占位符
                if (parms.length > 1) {
                    type.error(parms[1]);    //图片加载失败显示图片
                }
            }

            //单张CircleImageView不允许动画，不然会不显示,
            if (imageView instanceof CircleImageView) {
                type.dontAnimate();
            }
            return type
                    .listener(new GlideListener<String, GlideDrawable, String>(imageLoadListener, url, imageView))
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .thumbnail(thumbnail)
                    .into(imageView);
        } else {
            return null;
        }

    }

    /**
     * 加载一帧视频，添加圆角
     *
     * @param url       图片地址
     * @param imageView 要加载到的ImageView
     */
    public static Target<GlideDrawable> loadImageFormVideo(@NonNull String
                                                                   url, @NonNull ImageView imageView, int width, int height) {
        return getDrawableTypeRequest(imageView.getContext(), url)
                .override(width, height)
                //.placeholder(android.R.drawable.picture_frame)   //占位图
                .dontAnimate()
                .into(imageView);
    }


    public static <T> Target<GlideDrawable> loadImageDetail(final T context,
                                                            @NonNull final String url,
                                                            @NonNull final ImageView imageView,
                                                            final Drawable drawable,
                                                            final ImageLoadDetailListener<String, GlideDrawable> imageLoadListener) {
        DrawableTypeRequest<String> type = getDrawableTypeRequest(context, url);
        if (type != null) {
            return type.into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    if (imageView != null && resource != null) {
                        imageView.setImageDrawable(resource);
                    }
                    if (imageLoadListener != null) {
                        imageLoadListener.onLoadingComplete(url, imageView, resource);
                    }

                }

                @Override
                public void onStart() {
                    super.onStart();
                    if (drawable != null && imageView != null) {
                        imageView.setImageDrawable(drawable);
                    }

                }

                @Override
                public void onLoadStarted(Drawable placeholder) {
                    super.onLoadStarted(placeholder);
                    if (imageLoadListener != null) {
                        imageLoadListener.onLoadingStart(url, placeholder);
                    }

                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    super.onLoadFailed(e, errorDrawable);
                    if (imageLoadListener != null) {
                        imageLoadListener.onLoadingError(url, errorDrawable, e);
                    }
                }
            });
        } else {
            return null;
        }

    }


    /**
     * 加载bitmap，回调返回 Bitmap
     *
     * @param context           上下文
     * @param url               图片url
     * @param imageLoadListener 图片加载监听器
     * @param <T>               上下文类型
     */
    public static <T> BitmapRequestBuilder<String, Bitmap> loadImageBitmap(T
                                                                                   context, @NonNull String
                                                                                   url, @NonNull ImageLoadListener<String, Bitmap> imageLoadListener) {
        DrawableTypeRequest<String> type = getDrawableTypeRequest(context, url);
        if (type != null) {
            return type.asBitmap()
                    .listener(new GlideListener<String, Bitmap, String>(imageLoadListener, url));
        } else {
            return null;
        }
    }


    /**
     * 加载GifDrawable，回调返回 GifDrawable
     *
     * @param context           上下文
     * @param url               图片url
     * @param imageLoadListener 图片加载监听器
     */
    public static <T> GifRequestBuilder<String> loadImageGif(T context, @NonNull String
            url, @NonNull ImageLoadListener<String, GifDrawable> imageLoadListener) {
        DrawableTypeRequest<String> type = getDrawableTypeRequest(context, url);
        if (type != null) {
            return type.asGif()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .listener(new GlideListener<String, GifDrawable, String>(imageLoadListener, url));

        } else {
            return null;
        }
    }


    /**
     * 加载Gif的一张图片到ImageView
     *
     * @param url               图片url
     * @param imageLoadListener 图片加载监听器
     * @param imageView
     * @param drawable          缩略图，可以为空
     */
    public static Target<Bitmap> loadImageGifSingle(@NonNull String url,
                                                    @NonNull ImageView imageView,
                                                    ImageLoadListener<String, Bitmap> imageLoadListener,
                                                    Drawable drawable) {

        DrawableTypeRequest<String> type = getDrawableTypeRequest(imageView.getContext(), url);
        return type.asBitmap()
                .placeholder(drawable)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(new GlideListener<String, Bitmap, String>(imageLoadListener, url))
                .into(imageView);
    }


    /**
     * 加载gif图片到指定ImageView
     *
     * @param url               图片Url
     * @param imageView         图片View
     * @param imageLoadListener 图片加载监听器
     */
    public static Target<GifDrawable> loadImageGif(@NonNull String url, @NonNull ImageView
            imageView, ImageLoadListener<String, GifDrawable> imageLoadListener) {
        DrawableTypeRequest<String> type = getDrawableTypeRequest(imageView.getContext(), url);
        return type.asGif()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(new GlideListener<String, GifDrawable, String>(imageLoadListener, url, imageView))
                .into(imageView);
    }

    /**
     * 加载本地资源gif图片到指定ImageView
     *
     * @param id        图片资源id
     * @param imageView 图片View
     */
    public static Target<GifDrawable> loadImageGif(@NonNull int id, @NonNull ImageView
            imageView) {

        return Glide.with((Activity) imageView.getContext()).load(id).asGif()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }


    /**
     * 释放内存
     *
     * @param context 上下文
     */

    public static void clearMemory(Context context) {
        Glide.get(context).clearMemory();
    }


    /**
     * 取消所有正在下载或等待下载的任务。
     *
     * @param context 上下文
     */
    public static void cancelAllTasks(Context context) {
        Glide.with(context).pauseRequests();
    }

    /**
     * 恢复所有任务
     */
    public static void resumeAllTasks(Context context) {
        Glide.with(context).resumeRequests();
    }

    /**
     * 清除磁盘缓存
     *
     * @param context 上下文
     */
    public static void clearDiskCache(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(context).clearDiskCache();
            }
        }).start();
    }


    /**
     * 清除所有缓存
     *
     * @param context 上下文
     */
    public static void cleanAll(Context context) {
        clearDiskCache(context);
        clearMemory(context);
    }

    public static File getImagePathFromCache(String url, Context context, int width, int height) {
        FutureTarget<File> futureTarget = Glide.with(context)
                .load(url).downloadOnly(width, height);
        File file = null;
        try {
            file = futureTarget.get();
            return file;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 简单加载图片
     *
     * @param context 上下文
     * @param url     图片路径
     * @param view    加载图片到控件边显示
     */
    public static void load(Context context, String url, ImageView view) {
        Glide.with(context).load(url).into(view);
    }

    public static void loadConnerImage(final Context context, String url, final ImageView imageView) {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .centerCrop()
                .placeholder(R.color.color_C4CFE1)
                .error(R.color.color_C4CFE1)
                .diskCacheStrategy(DiskCacheStrategy.ALL) //设置缓存
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(false);
                        circularBitmapDrawable.setCornerRadius(ScreenUtil.dip2px(10));
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }
}
