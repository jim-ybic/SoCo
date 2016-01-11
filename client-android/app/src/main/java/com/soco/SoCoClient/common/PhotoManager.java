package com.soco.SoCoClient.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.util.LruCache;

import com.soco.SoCoClient.common.util.IconDownloadTask;
import com.soco.SoCoClient.common.util.TimeUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class PhotoManager implements TaskCallBack {

    static String tag = "PhotoManager";
    static String IMAGE_PATH = "image_path";
    static String COMPANY_NAME = "soco";
    static String DATA_FOLDER_NAME = "data";
    static String EQUAL = "=";
    static String SEPARATOR = "/";
    static String PERFS_NAME_LOCAL_FILE_INDEX = "local_file_index";

    private static Context context;
    private static LruCache<String, Bitmap> bitmapCache;   //<url,bitmap>
    private static LinkedHashMap<String, String> localImageFileIndex = new LinkedHashMap<>();  //<url,timestamp>
    private String url;

    public PhotoManager(int cacheSize){
        bitmapCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;    //todo: use bitmap count or total size???
            }
        };
    }

    public static void init(Context c){
        Log.v(tag, "init local image file index");
        context = c;

        SharedPreferences index = context.getSharedPreferences(PERFS_NAME_LOCAL_FILE_INDEX, 0);
        Map<String, ?> map = index.getAll();
        for(Map.Entry<String, ?> e : map.entrySet()){
            Log.v(tag, "add entry: " + e.getKey() + ", " + e.getValue().toString());
            localImageFileIndex.put(e.getKey(), e.getValue().toString());
        }
        Log.v(tag, map.size() + " entries loaded");
        return;
    }

    public Bitmap getBitmap(String url){
        Log.v(tag, "get bitmap from url: " + url);
        this.url = url;

        Bitmap bitmap = bitmapCache.get(url);
        if(bitmap != null){
            Log.v(tag, "found bitmap in bitmapCache");
            return bitmap;
        }
        else{
            Log.v(tag, "bitmap not found in bitmapCache, find in internal storage");
            String timestamp = localImageFileIndex.get(url);
            if(timestamp != null){
                Log.v(tag, "found bitmap in local file storage - load to use, refresh timestamp");
                bitmap = loadLocalBitmapFile(url);
                bitmapCache.put(url, bitmap);
                localImageFileIndex.put(url, TimeUtil.now());   //put after every access
                return bitmap;
            }
            else{
                Log.v(tag, "bitmap not found in local file storage - download to use and save, refresh timestamp");
                bitmap = downloadBitmapFromUrl(url);
            }
            return null;    //???
        }
    }

    private Bitmap loadLocalBitmapFile(String url){
        Log.v(tag, "load local bitmap file: " + url);

        String localFilePath = getLocalFilePathFromUrl(url);
        String root = Environment.getExternalStorageDirectory().toString();
        String photoPath = root + SEPARATOR + DATA_FOLDER_NAME + "/" + COMPANY_NAME + SEPARATOR + localFilePath;
        Log.v(tag, "local file path: " + photoPath);

        //approach 1
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);

        //approach 2: avoid out of memory error
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);

        Log.v(tag, "loaded image from local: " + bitmap);
        return bitmap;
    }


    private Bitmap downloadBitmapFromUrl(String url){
        Log.v(tag, "download bitmap from url: " + url);
        IconDownloadTask task = new IconDownloadTask(this);
        task.execute(url);
        return null;
    }

    public void doneTask(Object o){
        Log.v(tag, "done task");
        if(o == null){
            Log.e(tag, "return null");
        }
        else if(o instanceof Bitmap){
            Log.v(tag, "return bitmap");
            Bitmap bitmap = (Bitmap) o;

            Log.v(tag, "update image cache:: " + url + ", " + bitmap);
            bitmapCache.put(url, bitmap);
            Log.v(tag, "image cache size: " + bitmapCache.size());

            saveBitmapFileToLocal2(bitmap, url);

            Log.v(tag, "save image to local index, url: " + url + ", timestamp: " + TimeUtil.now());
            localImageFileIndex.put(url, TimeUtil.now());   //put after every access
            Log.v(tag, "save image to local index, size is: " + localImageFileIndex.size());
        }
    }

    //e.g. http://54.254.147.226:80/v1/image?image_path=images/events/2000101449419180409/image/10056611452128154618.jpg
    public void saveBitmapFileToLocal2(Bitmap bitmap, String url){
        if(url.indexOf(IMAGE_PATH) == -1){
            Log.w(tag, "invalid image url, skip saving to local");
            return;
        }

        String localFilePath = getLocalFilePathFromUrl(url);
        saveBitmapFileToLocal(bitmap, localFilePath);
    }

    private String getLocalFilePathFromUrl(String url){
        Log.v(tag, "image url: " + url);
        String localFilePath = url.substring(url.indexOf(EQUAL) + 1, url.length());
        Log.v(tag, "local file path: " + localFilePath);
        return localFilePath;
    }


    //e.g. images/events/2000101449419180409/image/10056611452128154618.jpg
    private static void saveBitmapFileToLocal(Bitmap bitmap, String localFilePath){
        try {
            String root = Environment.getExternalStorageDirectory().toString();

            String localDir = localFilePath.substring(0, localFilePath.lastIndexOf("/")+1);
            String filename = localFilePath.substring(localFilePath.lastIndexOf("/")+1, localFilePath.length());
            Log.v(tag, "localdir: " + localDir + ", filename: " + filename);

            File absoluteDir = new File(root + SEPARATOR + DATA_FOLDER_NAME + "/" + COMPANY_NAME + SEPARATOR + localDir);
            absoluteDir.mkdirs();

            Log.v(tag, "save local file: " + absoluteDir + ", " + filename);
            File file = new File(absoluteDir, filename);
            if(file.exists()) {
                Log.v(tag, "delete existing file");
                file.delete();
            }

            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);   //todo
            fos.flush();
            fos.close();
            Log.v(tag, "file saved locally success: " + absoluteDir + ", " + filename);
        }
        catch(Exception e){
            Log.e(tag, "cannot save file: " + e.toString());
            e.printStackTrace();
        }

        return;
    }

}
