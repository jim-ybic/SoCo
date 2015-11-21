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
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageButton;

import android.content.res.Resources;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by David_WANG on 11/15/2015.
 */
public class IconUrlUtil {
    private static final String tag="IconUrlUtil";
    private static LruCache<String, Bitmap> iconImageCache;
//    private static int screenSize=0;
    private static int sizeSmall=0;
    private static int sizeNormal=0;
    private static int sizeLarge=0;
//    private static int counter=0;

    public static void initialForIconDownloader(int screenSize,int cacheSize){
//        screenSize=screenSize;
        sizeSmall = Math.round(screenSize * 0.11111f);
        sizeNormal = Math.round(screenSize * 0.185185f);
        sizeLarge = Math.round(screenSize * 0.21f);
        iconImageCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public static void setImageForButtonSmall(Resources res, ImageButton mButton, String urlString){
        Log.v(tag, "set button image small: " + urlString);
        updateImageButton(res, mButton, urlString, sizeSmall);
    }
    public static void setImageForButtonNormal(Resources res, ImageButton mButton, String urlString){
        Log.v(tag, "set button image normal: " + urlString);
        updateImageButton( res,mButton, urlString,sizeNormal);
    }
    public static void setImageForButtonLarge(Resources res, ImageView mButton, String urlString){
        Log.v(tag, "set button image large: " + urlString);
        updateImageButton( res,mButton, urlString, sizeLarge);
    }

//    public static void setImageForViewSmall(Resources res, ImageView view, String urlString){
//        updateImageButton( res,view,urlString,sizeSmall);
//    }
//    public static void setImageForViewNormal(Resources res, ImageView view, String urlString){
//        updateImageButton( res,view, urlString,sizeNormal);
//    }
//    public static void setImageForViewLarge(Resources res, ImageView view, String urlString){
//        updateImageButton( res,view, urlString, sizeLarge);
//    }
    //Please use above method to get 3 fixed size image
    //below method for limited usage.
    private static void updateImageButton(Resources res, ImageView mButton, String urlString,int size){
        try {
            if (cancelPotentialWork(urlString, mButton)) {
                final IconDownloadTask task = new IconDownloadTask(mButton, size);
                final IconAsyncDrawable asyncDrawable =
                        new IconAsyncDrawable(res, Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888), task);
                mButton.setImageDrawable(asyncDrawable);
                task.execute(urlString);
            }
        }
        catch (Exception e){
            Log.e(tag, "cannot update image button: " + e);
            e.printStackTrace();
        }
    }

    public static boolean cancelPotentialWork(String url, ImageView mButton) {
        final IconDownloadTask iconDownloadTask = getBitmapWorkerTask(mButton);

        if (iconDownloadTask != null) {
            final String bitmapUrl = iconDownloadTask.url;
            // If bitmapData is not yet set or it differs from the new data
            if (StringUtil.isEmptyString(bitmapUrl) || !bitmapUrl.equalsIgnoreCase(url)) {
                // Cancel previous task
                iconDownloadTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }
    public static IconDownloadTask getBitmapWorkerTask(ImageView button) {
        if (button != null) {
            final Drawable drawable = button.getDrawable();
            if (drawable instanceof IconAsyncDrawable) {
                final IconAsyncDrawable asyncDrawable = (IconAsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }
    public static void addBitmapToImageCache(String url, Bitmap bitmap) {
        if (getBitmapFromImageCache(url) == null) {
            iconImageCache.put(url, bitmap);
        }
    }
    public static Bitmap getBitmapFromImageCache(String url) {
        return iconImageCache.get(url);
    }
    public static Bitmap processBitmap(Bitmap bp, int size){
        if(bp==null){
            Log.e(tag, "Not able to process bitmap as the input is empty");
        }else {
            if (size != 0) {
                bp = getResizedBitmap(bp, size, size);
                Log.d(tag, "Finished re-size bitmap");
                bp = getRoundedCornerBitmap(bp, size * size);
                Log.d(tag, "Finished round corner bitmap");
            }
        }
        return bp;
    }
    public static Bitmap getBitmapFromURL(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
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
