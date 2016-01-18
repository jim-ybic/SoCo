package com.soco.SoCoClient.common.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.soco.SoCoClient.common.PhotoManager;
import com.soco.SoCoClient.common.TaskCallBack;

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
    TaskCallBack callBack = null;

    public IconDownloadTask(ImageView button,int size){
        imageButtonReference = new WeakReference<>(button);
        this.size=size;
        useRoundedCorner = true;
    }

    public IconDownloadTask(TaskCallBack cb){
        imageButtonReference = new WeakReference<>(null);
        this.callBack = cb;
    }

    public IconDownloadTask(ImageView button,int size, boolean useRoundedCorner, Resources res){
        imageButtonReference = new WeakReference<>(button);
        this.size=size;
        this.useRoundedCorner = useRoundedCorner;
        this.res = res;
    }

    protected Bitmap doInBackground(String... urls) {
        //1. get the bitmap from PhotoManager. It could be either from cache(memory/disk) or downloading from network
        //2. process the bitmap before set to view. Resize, Round the corner.
        Bitmap bp = null;
        for (String url : urls) {
            this.url = url;
            //use photo manager to find image
            Log.v(tag, "Using Photo Manager to find image: " + url);
            PhotoManager manager = new PhotoManager();
            bp = manager.getBitmap(url);
            if(bp!=null) {
                if (useRoundedCorner) {
                    Log.v(tag, "process bit map in normal shape");    //e.g. user post photos
                    bp = IconUrlUtil.processBitmap(bp, this.size);
                    Log.v(tag, "process bit map in rounded corner");    //e.g. user icon
                    bp = IconUrlUtil.processBitmapRoundedCorner(bp, this.size);
                } else {
                    Log.v(tag, "size ready");
                    bp = IconUrlUtil.processBitmapAutoAdjusted(bp, this.size);
                }
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
        //after get the bitmap, only if it is not empty,  set it to UI. this has to be done in the PostExecute
        if (bitmap != null) {
            final ImageView imageButton = imageButtonReference.get();
            final IconDownloadTask bitmapWorkerTask =
                    IconUrlUtil.getBitmapWorkerTask(imageButton);
            if (this == bitmapWorkerTask){
                imageButton.setImageBitmap(bitmap);
            }
        }
        if(callBack != null) {
            Log.v(tag, "call back return bitmap");
            callBack.doneTask(bitmap);
        }
    }
}
