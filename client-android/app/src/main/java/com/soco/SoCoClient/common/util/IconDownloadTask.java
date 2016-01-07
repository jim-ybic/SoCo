package com.soco.SoCoClient.common.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by David_WANG on 11/15/2015.
 */
public class IconDownloadTask extends AsyncTask<String, Void, Bitmap> {
    private static final String tag = "IconDownloadTask";

    private final WeakReference<ImageView> imageButtonReference;
    public String url;
    private int size;
    private boolean useRoundedCorner;
    private Resources res;
    int width, height;
    boolean sizeReady = false;

    public IconDownloadTask(ImageView button,int size){
        imageButtonReference = new WeakReference<>(button);
        this.size=size;
        useRoundedCorner = true;
    }

    public IconDownloadTask(ImageView button,int size, boolean useRoundedCorner, Resources res){
        imageButtonReference = new WeakReference<>(button);
        this.size=size;
        this.useRoundedCorner = useRoundedCorner;
        this.res = res;
    }

    public IconDownloadTask(ImageView button,int width, int height, boolean useRoundedCorner, Resources res){
        imageButtonReference = new WeakReference<>(button);
        this.width = width;
        this.height = height;
        this.useRoundedCorner = useRoundedCorner;
        this.res = res;
        this.sizeReady = true;
    }

    protected Bitmap doInBackground(String... urls) {
//        urlList= new ArrayList<>();
        Bitmap bp = null;
        for(String url:urls){
            this.url = url;
            Log.v(tag, "Trying to find in cache for url: "+url);
            bp = IconUrlUtil.getBitmapFromImageCache(url);
            if(bp==null) {
                Log.v(tag, "No result found in cache for url, downloading image from server");
//                Log.v(tag, "Downloading image from server: "+url);
                bp = IconUrlUtil.getBitmapFromURL(url);

                if(bp!=null) {
                    IconUrlUtil.addBitmapToImageCache(url, bp);
                }
            }
            else
                Log.v(tag, "Found image in cache");

            if(sizeReady){
                Log.v(tag, "size ready");
                bp = IconUrlUtil.processBitmap(bp, this.width, this.height);
            }
            else if(useRoundedCorner) {
                Log.v(tag, "process bit map in rounded corner");    //e.g. user icon
                bp = IconUrlUtil.processBitmapRoundedCorner(bp, this.size);
            }
            else {
                Log.v(tag, "process bit map in normal shape");    //e.g. user post photos
                bp = IconUrlUtil.processBitmap(bp, this.size);
            }
        }
        return bp;
    }

    /** The system calls this to perform work in the UI thread and delivers
     * the result from doInBackground() */
    protected void onPostExecute(Bitmap bitmap) {
        Log.v(tag, "post execute, set image on screen: " + url);
        if (isCancelled()) {
            bitmap = null;
        }
        if (bitmap != null) {
            final ImageView imageButton = imageButtonReference.get();
            final IconDownloadTask bitmapWorkerTask =
                    IconUrlUtil.getBitmapWorkerTask(imageButton);
            if (this == bitmapWorkerTask){
                imageButton.setImageBitmap(bitmap);
            }
        }
    }
}
