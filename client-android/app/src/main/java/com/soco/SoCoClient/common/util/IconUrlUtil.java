package com.soco.SoCoClient.common.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.widget.ImageButton;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by David_WANG on 11/15/2015.
 */
public class IconUrlUtil {
    private static final String tag="IconUrlUtil";
    private static HashMap<String, Bitmap> imageCacheMap;
    private static int screenSize=0;
    private static int sizeSmall=0;
    private static int sizeNormal=0;
    private static int sizeLarge=0;
    private static int counter=0;

    public static void setSize(int size){
        screenSize=size;
        sizeSmall = Math.round(screenSize * 0.11111f);
        sizeNormal = Math.round(screenSize * 0.185185f);
        sizeLarge = Math.round(screenSize * 0.27778f);
    }

    public static void setImageForButtonSmall(ImageButton mButton, String urlString){
        updateImageButton(mButton,urlString,sizeSmall);
    }
    public static void setImageForButtonNormal(ImageButton mButton, String urlString){
        updateImageButton(mButton, urlString,sizeNormal);
    }
    public static void setImageForButtonLarge(ImageButton mButton, String urlString){
        updateImageButton(mButton, urlString, sizeLarge);
    }
    //Please use above method to get 3 fixed size image
    //below method for limited usage.
    private static void updateImageButton(ImageButton mButton, String urlString,int size){
        if(imageCacheMap!=null&&imageCacheMap.containsKey(urlString)){
            assignBitmapToImageButton(imageCacheMap.get(urlString),mButton,size);
        }else{
            IconDownloadTask idt = new IconDownloadTask(mButton,size);
            idt.execute(urlString);
        }
    }

    public static void addToImageCacheMap(String url,Bitmap image){
        if(imageCacheMap==null){
            imageCacheMap=new HashMap<>();
        }
        imageCacheMap.put(url,image);
    }
    public static Bitmap getBitmapFromImageCacheMap(String url){
        if(imageCacheMap.containsKey(url)){
            return imageCacheMap.get(url);
        }
        return null;
    }
    public static void assignBitmapToImageButton(Bitmap bp, ImageButton button,int size){
        if(bp==null){
            Log.v(tag, "Not able to download image from server");
            return;
        }
        if(size!=0) {
            bp = getResizedBitmap(bp, size, size);
            bp = getRoundedCornerBitmap(bp, size * size);
        }
        button.setImageBitmap(bp);
    }
    public static Bitmap getBitmapFromURL(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            counter++;
            Log.v(tag, counter +"Image downloaded from server");
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);
        // "RECREATE" THE NEW BITMAP
        return Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);
    }
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, pixels, pixels, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
