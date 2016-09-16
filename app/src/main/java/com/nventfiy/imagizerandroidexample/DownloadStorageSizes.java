package com.nventfiy.imagizerandroidexample;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by nick on 9/12/16.
 */
public class DownloadStorageSizes {
    public static HashMap<String, Long> sizes = new HashMap<>();
    public static HashMap<String, Long> originalSizes = new HashMap<>();

    public static AtomicLong fileSize = new AtomicLong();
    public static AtomicLong originalFileSize = new AtomicLong();

    public static long sumOriginalImageSizeForImageUrl(String imageUri) {
        try {
            return originalFileSize.addAndGet(DownloadStorageSizes.originalSizes.get(imageUri));
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public static long sumImageSizesForImageUrl(String imageUri) {
        try {
            return fileSize.addAndGet(DownloadStorageSizes.sizes.get(imageUri));
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public static int getPercent() {
        if (originalFileSize.get() > 0) {
            return (int) -Math.ceil((((double) fileSize.get() - originalFileSize.get()) / originalFileSize.get()) * 100);
        }

        return 0;
    }

    public static void clearAll() {
        sizes.clear();
        originalSizes.clear();
        fileSize.set(0);
        originalFileSize.set(0);
    }
}
