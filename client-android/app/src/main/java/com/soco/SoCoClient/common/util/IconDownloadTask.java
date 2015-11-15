package com.soco.SoCoClient.common.util;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageButton;

import java.util.ArrayList;

/**
 * Created by David_WANG on 11/15/2015.
 */
public class IconDownloadTask extends AsyncTask<String, Void, Bitmap> {
    private static final String tag = "IconDownloadTask";
    private ImageButton ib;
    private int height;
    private int width;
    public IconDownloadTask(ImageButton button,int height, int width){
        this.ib=button;
        this.height=height;
        this.width=width;
    }

    protected Bitmap doInBackground(String... urls) {
        return IconUrlUtil.getBitmapFromURL(urls[0],height,width);
    }

    /** The system calls this to perform work in the UI thread and delivers
     * the result from doInBackground() */
    protected void onPostExecute(Bitmap result) {
        ib.setImageBitmap(IconUrlUtil.getRoundedCornerBitmap(result,height*width));
    }

}
