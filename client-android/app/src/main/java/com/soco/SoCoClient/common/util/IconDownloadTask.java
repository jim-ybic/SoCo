package com.soco.SoCoClient.common.util;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by David_WANG on 11/15/2015.
 */
public class IconDownloadTask extends AsyncTask<String, Void, Bitmap> {
    private static final String tag = "IconDownloadTask";

    private final WeakReference<ImageView> imageButtonReference;
    public String url;
    private int size;

    public IconDownloadTask(ImageView button,int size){
        imageButtonReference = new WeakReference<>(button);
        this.size=size;
    }

    protected Bitmap doInBackground(String... urls) {
//        urlList= new ArrayList<>();
        Bitmap bp = null;
        for(String url:urls){
            this.url = url;
            Log.v(tag, "Trying to find in cache for url: "+url);
            bp = IconUrlUtil.getBitmapFromImageCache(url);
            if(bp==null) {
                Log.v(tag, "No result found in cache for url: "+url);
                Log.v(tag, "Downloading image from server: "+url);
                bp = IconUrlUtil.getBitmapFromURL(url);
                if(bp!=null) {
                    IconUrlUtil.addBitmapToImageCache(url, bp);
                }
            }
            bp = IconUrlUtil.processBitmap(bp,this.size);
        }
        return bp;
    }

    /** The system calls this to perform work in the UI thread and delivers
     * the result from doInBackground() */
    protected void onPostExecute(Bitmap bitmap) {
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
