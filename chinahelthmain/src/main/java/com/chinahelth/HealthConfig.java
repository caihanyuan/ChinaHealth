package com.chinahelth;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by caihanyuan on 7/2/15.
 */
public class HealthConfig {
    public final static boolean isDebug = true;

    public final static boolean isLoop = true;

    public final static boolean isAutoRun = true;

    public final static int AUTO_SCROLL_DELAY = 12 * 1000;

    public final static int SCROLL_DURATION = 1 * 1000;

    private final static int IMAGE_MEMORY_SIZE = 10 * 1024 * 1024;

    private final static int IAMGE_CACHE_SIZE = 50 * 1024 * 1024;

    private static DisplayImageOptions displayImageOptions = null;

    public static DisplayImageOptions getDefaultDisplayImageOptions() {
        if (displayImageOptions == null) {
            displayImageOptions = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
        }
        return displayImageOptions;
    }

    static void initDefaultImageLoader() {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(HealthApplication.context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(IAMGE_CACHE_SIZE);
        config.memoryCacheSize(IMAGE_MEMORY_SIZE);
        config.tasksProcessingOrder(QueueProcessingType.FIFO);
        if (isDebug) {
            config.writeDebugLogs();
        }

        ImageLoader.getInstance().init(config.build());
    }

    static void destory() {
        ImageLoader.getInstance().destroy();
        displayImageOptions = null;
    }
}
