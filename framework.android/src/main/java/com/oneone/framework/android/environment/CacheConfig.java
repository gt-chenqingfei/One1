package com.oneone.framework.android.environment;


public class CacheConfig {

    static Runtime rt = Runtime.getRuntime();
    static long maxMemory = rt.maxMemory();

    /* 崩溃日志文件的目录 */
    public static String CACHE_CRASH_PATH = "/crash/";

    /* 语音文件 */
    public static String AUDIO_DIRECTORY_NAME = "/audio/";

    /* 其他文件 例如json */
    public static String FILE_DIRECTORY_NAME = "/file/";

    /* 图片文件 */
    public static String IMAGE_DIRECTORY_NAME = "/image/";

    /* 缩略图文件 */
    public static String THUMB_DIRECTORY_NAME = "/thumb/";

    /* 视频文件 */
    public static String VIDEO_DIRECTORY_NAME = "/video/";

    /* 零时文件目录 */
    public static String TEMP_PATH = "/tmp/";

    /* app缓存目录 */
    public static String CACHE_WITH_APP = "/cache/";

    /* download缓存目录 */
    public static String DOWNLOAD_WITH_APP = "/download/";

    /* Glide 的缓存配置 */
    public static int DEFAULT_DISK_CACHE_SIZE = 500 * 1024 * 1024;
    public static String CACHE_WITH_GLIDE_PATH = CACHE_WITH_APP + "/glide-cache/";
    public static long DEFAULT_MEMORY_CACHE_SIZE = maxMemory / 8;

    /* 存储指定大小不够用的时候清除数据缓存 **/
    public static int MAX_STORAGE_SZIE = 1024 * 1024 * 500;


}
