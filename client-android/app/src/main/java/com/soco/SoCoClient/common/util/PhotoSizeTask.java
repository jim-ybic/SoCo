package com.soco.SoCoClient.common.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.soco.SoCoClient.common.TaskCallBack;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class PhotoSizeTask extends AsyncTask<String, Void, Boolean> {
    private static final String tag = "PhotoSizeTask";

    Resources res;
    ImageView view;
    String url;
    int size;
    TaskCallBack callBack;

    int displayWidth;
    int displayHeight;

    public PhotoSizeTask(Resources res, ImageView view, int size, String url, TaskCallBack callBack){
        this.res = res;
        this.view = view;
        this.size=size;
        this.url = url;
        this.callBack = callBack;
    }

    protected Boolean doInBackground(String... urls) {
        Log.v(tag, "task begin");

        displayWidth = size;
        displayHeight = size;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            URL u = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) u
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            BitmapFactory.decodeStream(input, null, options);
            Log.v(tag, "bitmap width/height: " + options.outWidth + "/" + options.outHeight);
            displayHeight = options.outHeight * size / options.outWidth;
        }
        catch(Exception e){
            Log.e(tag, "cannot get image from url");
            e.printStackTrace();
        }
        Log.v(tag, "display width/height: " + displayWidth + "/" + displayHeight);

        return true;
    }

    /** The system calls this to perform work in the UI thread and delivers
     * the result from doInBackground() */
    protected void onPostExecute(Boolean result) {
        Log.v(tag, "post execute, download image with size: " + displayWidth + "/" + displayHeight);

        try {
            if (IconUrlUtil.cancelPotentialWork(url, view)) {
                final IconDownloadTask task = new IconDownloadTask(view, displayWidth, displayHeight, false, res);
                final IconAsyncDrawable asyncDrawable =
                        new IconAsyncDrawable(res, Bitmap.createBitmap(displayWidth, displayHeight, Bitmap.Config.ARGB_8888), task);
                view.setImageDrawable(asyncDrawable);
                task.execute(url);
            }
        }
        catch (Exception e){
            Log.e(tag, "cannot update image button: " + e);
            e.printStackTrace();
        }
    }
}
