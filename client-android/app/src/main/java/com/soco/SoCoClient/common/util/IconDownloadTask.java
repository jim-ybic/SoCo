package com.soco.SoCoClient.common.util;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageButton;

import java.util.ArrayList;

/**
 * Created by David_WANG on 11/15/2015.
 */
public class IconDownloadTask extends AsyncTask<String, Void, Void> {
    private static final String tag = "IconDownloadTask";
    private ArrayList<ImageButton> ibs;
    private ArrayList<String> urlList;
    private int size;
    public IconDownloadTask(ImageButton button,int size){
        this.ibs = new ArrayList<>();
        this.ibs.add(button);
        this.size=size;
    }
    public IconDownloadTask(ArrayList<ImageButton> buttons,int size){
        this.ibs = buttons;
        this.size=size;
    }

    protected Void doInBackground(String... urls) {
        urlList= new ArrayList<>();
        for(String url:urls){
            Log.v(tag, "Downloading image from server"+url);
            urlList.add(url);
            Bitmap bp = IconUrlUtil.getBitmapFromURL(url);
            IconUrlUtil.addToImageCacheMap(url,bp);
//            publishProgress();
        }
        return null;
    }

    /** The system calls this to perform work in the UI thread and delivers
     * the result from doInBackground() */
    protected void onPostExecute(Void v) {
        for(int i=0;i<ibs.size()&&i<urlList.size();i++){
            String url = urlList.get(i);
            IconUrlUtil.assignBitmapToImageButton(IconUrlUtil.getBitmapFromImageCacheMap(url),ibs.get(i),size);
        }
        ibs = new ArrayList<>();
        urlList = new ArrayList<>();
    }
}
