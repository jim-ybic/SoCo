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
    private static HashMap<String, Bitmap> imageMap;
    public static void setImageForButton(ImageButton mButton, String urlString){
        setImageForButton(mButton,urlString,300,300);
    }
    public static void setImageForButton(ImageButton mButton, String urlString, int size){
        IconDownloadTask idt = new IconDownloadTask(mButton,size,size);
        idt.execute(urlString);
    }

    public static void setImageForButton(ImageButton mButton, String urlString,int height, int width){
        IconDownloadTask idt = new IconDownloadTask(mButton,height,width);
        idt.execute(urlString);
    }

//    public static Bitmap getBitmapFromURL(String urlString) {
//        return getBitmapFromURL(urlString,0,0);
//    }
    public static Bitmap getBitmapFromURL(String urlString ,  int newHeight, int newWidth) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            if(newHeight==0&&newWidth==0){
                return myBitmap;
            }
            if(newHeight==0){
                newHeight=myBitmap.getHeight();
            }
            if(newWidth==0){
                newWidth=myBitmap.getWidth();
            }
            return getResizedBitmap(myBitmap,newHeight,newWidth);
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
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);
        return resizedBitmap;
    }
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
