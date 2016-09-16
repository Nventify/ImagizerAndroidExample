package com.nventfiy.imagizerandroidexample;

import android.app.Application;

import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by nick on 8/26/16.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Lets setup Android Universal Image Loader
        // https://github.com/nostra13/Android-Universal-Image-Loader

        // Setup image caching on both disk and memory
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();


        // Configure the image loader
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(options)
                // using a custom image Downloader to collect image file sizes
                .imageDownloader(new ImageDownloader(this))
                //                .writeDebugLogs()
                .build();

        // Initialize image loader
        ImageLoader.getInstance().init(config);

        // make sure we start out with clean cache
        ImageLoader.getInstance().clearDiskCache();
        ImageLoader.getInstance().clearMemoryCache();
    }
}
