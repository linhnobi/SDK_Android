package com.mobio.analytics.client.utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.HttpResponseCache;
import android.util.Log;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.CacheResponse;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class DownloadManager {
    private DownloadManager() {
        throw new IllegalStateException("DownloadManager class");
    }

    private static final String TAG = DownloadManager.class.getSimpleName();
    
    /**
     * This input stream extension is required to decode png images to bitmap
     */
    private static class FlushedInputStream extends FilterInputStream {
        FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0L;
            while (totalBytesSkipped < n) {
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0L) {
                    int b = read();
                    if (b < 0) {
                        break; // we reached EOF
                    } else {
                        bytesSkipped = 1; // we read one byte
                    }
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }

    public static Bitmap getBitmapFromURL(String src, boolean fromCacheOnly) {
        Log.d(TAG, "Image requested: " + src);
        InputStream input = null;
        FlushedInputStream flushedInputStream = null;
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(true);
            int maxStale = 60 * 60 * 24 * 3;  // 2 days
            connection.addRequestProperty("Cache-Control", "max-stale=" + maxStale);
            connection.setDoInput(true);
            if (fromCacheOnly) {
                connection.setRequestProperty("Cache-Control", "only-if-cached");
                HttpResponseCache responseCache = HttpResponseCache.getInstalled();
                if (responseCache != null) {
                    URI uri = new URI(src);
                    CacheResponse cacheResponse = responseCache.get(uri, "GET", connection.getRequestProperties());
                    if (cacheResponse != null) {
                        input = cacheResponse.getBody();
                    }
                } else {
                    Log.e(TAG, "Http cache not created");
                }
            } else {
                connection.connect();
                Log.d(TAG, "status response code: " + connection.getResponseCode());
                input = connection.getInputStream();
            }

            if (input == null) {
                return null;
            }

            flushedInputStream = new FlushedInputStream(input);

            BitmapFactory.Options bmOptions;
            bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 1;

            Bitmap myBitmap = BitmapFactory.decodeStream(flushedInputStream, null, bmOptions);
            Log.d(TAG, "Downloaded image is null: " + (myBitmap == null));
            return myBitmap;
        } catch (IOException e) {
            Log.e(TAG, "Exception while loading image: " + src, e);
            return null;
        } catch (URISyntaxException e) {
            Log.e(TAG, "Exception while creating URI from: " + src, e);
            return null;
        } catch (Exception e) {
            Log.e(TAG, "Unexpected exception while downloading bitmap from: " + src, e);
            return null;
        }
        finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    LogMobio.logE("DownloadManager","IOException "+e);
                }
            }

            if (flushedInputStream != null) {
                try {
                    flushedInputStream.close();
                } catch (IOException e) {
                    Log.e(TAG, "IOException " + e);
                }
            }
        }
    }
}
