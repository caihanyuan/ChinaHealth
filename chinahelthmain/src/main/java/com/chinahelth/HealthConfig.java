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
    public final static boolean isDebug = BuildConfig.DEBUG_LOG;

    public final static boolean isLoop = true;

    public final static boolean isAutoRun = true;

    public final static int AUTO_SCROLL_DELAY = 12 * 1000;

    public final static int SCROLL_DURATION = 1 * 1000;

    private final static int IMAGE_MEMORY_SIZE = 10 * 1024 * 1024;

    private final static int IAMGE_CACHE_SIZE = 50 * 1024 * 1024;

    public final static int ARTICLE_ITEM_ONCE_LOAD_NUM = 10;

//    private final static String SERVER_HOST_DOMAIN = "www.chinahealth.com";

    private final static String SERVER_HOST_DOMAIN = "172.16.136.113";

    private final static int SERVER_HOST_PROT = 8080;

    public final static String SERVER_HOST_ROOT_URL = "http://" + SERVER_HOST_DOMAIN + ":" + SERVER_HOST_PROT + "/ChinaHealthServer/";

    private static DisplayImageOptions displayImageOptions = null;

    public static DisplayImageOptions getDefaultDisplayImageOptions() {
        if (displayImageOptions == null) {
            displayImageOptions = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .showImageOnLoading(R.drawable.homepage_item_img_default)
                    .showImageOnFail(R.drawable.homepage_item_img_default)
                    .showImageForEmptyUri(R.drawable.homepage_item_img_default)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
        }
        return displayImageOptions;
    }

    static void initDefaultImageLoader() {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(HealthApplication.getInstance());
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
