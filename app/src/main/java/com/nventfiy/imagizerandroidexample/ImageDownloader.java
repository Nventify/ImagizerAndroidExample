package com.nventfiy.imagizerandroidexample;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.nostra13.universalimageloader.core.assist.ContentLengthInputStream;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.IoUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * Created by nick on 9/11/16.
 */
public class ImageDownloader extends BaseImageDownloader {
    public ImageDownloader(Context context) {
        super(context);
    }

    public ImageDownloader(Context context, int connectTimeout, int readTimeout) {
        super(context, connectTimeout, readTimeout);
    }

    /**
     * Retrieves {@link InputStream} of image by URI (image is located in the network).
     *
     * @param imageUri Image URI
     * @param extra    Auxiliary object which was passed to {@link com.nostra13.universalimageloader.core.DisplayImageOptions.Builder#extraForDownloader(Object)
     *                 DisplayImageOptions.extraForDownloader(Object)}; can be null
     * @return {@link InputStream} of image
     * @throws IOException if some I/O error occurs during network request or if no InputStream could be created for
     *                     URL.
     */
    protected InputStream getStreamFromNetwork(String imageUri, Object extra) throws IOException {
        HttpURLConnection conn = createConnection(imageUri, extra);

        int redirectCount = 0;
        while (conn.getResponseCode() / 100 == 3 && redirectCount < MAX_REDIRECT_COUNT) {
            conn = createConnection(conn.getHeaderField("Location"), extra);
            redirectCount++;
        }

        InputStream imageStream;
        try {
            imageStream = conn.getInputStream();
        } catch (IOException e) {
            // Read all data to allow reuse connection (http://bit.ly/1ad35PY)
            IoUtils.readAndCloseStream(conn.getErrorStream());
            throw e;
        }
        if (!shouldBeProcessed(conn)) {
            IoUtils.closeSilently(imageStream);
            throw new IOException("Image request failed with response code " + conn.getResponseCode());
        }

        long fileSize = 0;
        long originalFileSize = 0;

        try {
            originalFileSize = Long.valueOf(conn.getHeaderField("X-Original-Filesize"));
        } catch (NumberFormatException e) {
        }

        try {
            fileSize = (long) conn.getContentLength();
        } catch (NumberFormatException e) {
        }

        if (originalFileSize == 0) {
            originalFileSize = fileSize;
        }

        DownloadStorageSizes.originalSizes.put(imageUri, originalFileSize);
        DownloadStorageSizes.sizes.put(imageUri, fileSize);

        return new ContentLengthInputStream(new BufferedInputStream(imageStream, BUFFER_SIZE), conn.getContentLength());
    }
}
