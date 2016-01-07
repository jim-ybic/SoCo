package com.soco.SoCoClient.common.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.soco.SoCoClient.common.TaskCallBack;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PhotoPlacer implements TaskCallBack{
    static final String tag="PhotoPlacer";

    public static void showPhotoInPost(Resources res, ImageView view, String url){
        Log.v(tag, "showPhotoInPost: " + url + ", screensize: " + SocoApp.screenSize);
//        updateImageButtonRegularShape(res, view, url, SocoApp.screenSize);
        int size = SocoApp.screenSize;

        int displayWidth = size;
        int displayHeight = size;
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
//            Uri uri = Uri.parse(urlString);
//            BitmapFactory.decodeFile(uri.getPath(), options);
            Log.v(tag, "bitmap width/height: " + options.outWidth + "/" + options.outHeight);
            displayHeight = options.outHeight * size / options.outWidth;
        }
        catch(Exception e){
            Log.e(tag, "cannot get image from url");
            e.printStackTrace();
        }
        Log.v(tag, "display width/height: " + displayWidth + "/" + displayHeight);

        ImageView mButton = view;
        try {
            if (IconUrlUtil.cancelPotentialWork(url, mButton)) {
                final IconDownloadTask task = new IconDownloadTask(mButton, size, false, res);
                final IconAsyncDrawable asyncDrawable =
                        new IconAsyncDrawable(res, Bitmap.createBitmap(displayWidth, displayWidth, Bitmap.Config.ARGB_8888), task);
                mButton.setImageDrawable(asyncDrawable);
                task.execute(url);
            }
        }
        catch (Exception e){
            Log.e(tag, "cannot update image button: " + e);
            e.printStackTrace();
        }
    }

    public void showPhotoInPost2(Resources res, ImageView view, String url) {
        Log.v(tag, "screensize: " + SocoApp.screenSize + ", " + SocoApp.screenSizeWidth + "/" + SocoApp.screenSizeHeight);
        new PhotoSizeTask(res, view, SocoApp.screenSize, url, this).execute();
    }

    public void doneTask(Object o){
        //todo
    }
}

