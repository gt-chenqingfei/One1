package com.oneone.framework.android.environment;


public enum StorageType {
    TYPE_TEMP(DirectoryName.TEMP_DIRECTORY_NAME),
    TYPE_FILE(DirectoryName.FILE_DIRECTORY_NAME),
    TYPE_AUDIO(DirectoryName.AUDIO_DIRECTORY_NAME),
    TYPE_IMAGE(DirectoryName.IMAGE_DIRECTORY_NAME),
    TYPE_VIDEO(DirectoryName.VIDEO_DIRECTORY_NAME),
    TYPE_THUMB_IMAGE(DirectoryName.THUMB_DIRECTORY_NAME),
    TYPE_THUMB_VIDEO(DirectoryName.THUMB_DIRECTORY_NAME),
    CACHE_GLIDE_DIRECTORY_NAME(DirectoryName.CACHE_Glide_DIRECTORY_NAME),
    CRASH_DIRECTORY_NAME(DirectoryName.CRASH_DIRECTORY_NAME),
    CACHE_WITH_APP(DirectoryName.CACHE_WITH_APP),
    DOWNLOAD_WITH_APP(DirectoryName.DOWNLOAD_WITH_APP),;

    private DirectoryName storageDirectoryName;
    private long storageMinSize;

    public String getStoragePath() {
        return storageDirectoryName.getPath();
    }

    public long getStorageMinSize() {
        return storageMinSize;
    }

    StorageType(DirectoryName dirName) {
        this(dirName, StorageUtil.THRESHOLD_MIN_SPCAE);
    }

    StorageType(DirectoryName dirName, long storageMinSize) {
        this.storageDirectoryName = dirName;
        this.storageMinSize = storageMinSize;
    }

    enum DirectoryName {
        AUDIO_DIRECTORY_NAME(CacheConfig.AUDIO_DIRECTORY_NAME),
        FILE_DIRECTORY_NAME(CacheConfig.FILE_DIRECTORY_NAME),
        CRASH_DIRECTORY_NAME(CacheConfig.CACHE_CRASH_PATH),
        TEMP_DIRECTORY_NAME(CacheConfig.TEMP_PATH),
        IMAGE_DIRECTORY_NAME(CacheConfig.IMAGE_DIRECTORY_NAME),
        THUMB_DIRECTORY_NAME(CacheConfig.THUMB_DIRECTORY_NAME),
        VIDEO_DIRECTORY_NAME(CacheConfig.VIDEO_DIRECTORY_NAME),
        CACHE_Glide_DIRECTORY_NAME(CacheConfig.CACHE_WITH_GLIDE_PATH),
        DOWNLOAD_WITH_APP(CacheConfig.DOWNLOAD_WITH_APP),
        CACHE_WITH_APP(CacheConfig.CACHE_WITH_APP),;

        private String path;

        public String getPath() {
            return path;
        }

        private DirectoryName(String path) {
            this.path = path;
        }
    }
}
