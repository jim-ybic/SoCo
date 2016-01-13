package com.soco.SoCoClient.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.util.LruCache;

import com.soco.SoCoClient.common.util.IconDownloadTask;
import com.soco.SoCoClient.common.util.IconUrlUtil;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.common.util.TimeUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * local cache file structure:
 * a) for photos/images in events
 * /root/data/soco/images/events/<eventid>/image/<filename>
 * b) for user icons
 * /root/images/user_icon/<userid>/user_icon.jpg
 * c) for photos/images in topics
 * n.a.
 *
 * strategy for find image for a given url: check function getBitmap
 *
 *
 */

public class PhotoManager implements TaskCallBack {

    static String tag = "PhotoManager";
    static String IMAGE_PATH = "image_path";
    static String COMPANY_NAME = "soco";
    static String DATA_FOLDER_NAME = "data";
    static String EQUAL = "=";
    static String SEPARATOR = "/";
    static String PERFS_NAME_LOCAL_FILE_INDEX = "local_file_index";

    static String URL_IDENTIFIER_USERICON = "user_icon?";
    static String URL_IDENTIFIER_IMAGE= "image?image_path=";
    static String TOKEN_EQUAL = "token=";
    static String IMAGES = "images";
    static String USER_ICON = "user_icon";
    static String USER_ICON_JPG = "user_icon.jpg";
    static String USER_ID_EQUAL = "user_id=";
    static String BUDDY_USER_ID_EQUAL = "buddy_user_id=";

    private static Context context;
    private static LruCache<String, Bitmap> bitmapCache;   //<url,bitmap>
    private static LinkedHashMap<String, String> localImageFileIndex = new LinkedHashMap<>();  //<url,timestamp>
    private static SharedPreferences index;

    private String urlToProcess;

    public PhotoManager(){}

    public static void init(Context c, int cacheSize){
        Log.d(tag, "init photo manager, cache size: " + cacheSize + " kB");
        context = c;

        bitmapCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;    // cache unit: 1k (1024 byte)
            }
        };

        index = context.getSharedPreferences(PERFS_NAME_LOCAL_FILE_INDEX, 0);
        Map<String, ?> map = index.getAll();
        for(Map.Entry<String, ?> e : map.entrySet()){
            Log.d(tag, "add entry: " + e.getKey() + ", " + e.getValue().toString());
            localImageFileIndex.put(e.getKey(), e.getValue().toString());
        }
        Log.d(tag, map.size() + " entries loaded");
        return;
    }

    public Bitmap getBitmap(String url){
        Log.d(tag, ">>> find image: " + url);
        if(isImageUrl(url)){
            urlToProcess = url;
            Log.d(tag, "image url to process: " + urlToProcess);
        }
        else if(isUsericonUrl(url)){
            urlToProcess = getUsericonUrlWithoutToken(url);
            Log.d(tag, "user icon url to process: " + urlToProcess);
        }
        else{
            Log.e(tag, "url not supported by PhotoManager");
            return null;
        }

        Bitmap bitmap = bitmapCache.get(urlToProcess);
        if(bitmap != null){
            Log.d(tag, "found bitmap in bitmapCache");
            return bitmap;
        }
        else{
            Log.d(tag, "not found in bitmapCache, find in internal storage");
            String timestamp = localImageFileIndex.get(urlToProcess);
            if(timestamp != null){
                Log.d(tag, "found bitmap in local file index: " + urlToProcess);
                if(isImageUrl(url))
                    bitmap = loadLocalBitmapFileForImage(urlToProcess);
                else if(isUsericonUrl(url))
                    bitmap = loadLocalBitmapFileForUsericon(urlToProcess);

                if(bitmap == null){
                    Log.d(tag, "bitmap not loaded from local - remove url from index: " + urlToProcess);
                    localImageFileIndex.remove(urlToProcess);
                    SharedPreferences.Editor editor = index.edit();
                    editor.remove(urlToProcess);
                    editor.commit();

                    Log.d(tag, "download to use and save, refresh timestamp: " + url);
                    downloadBitmapFromUrl(url);
                    return null;
                }
                else {
                    Log.d(tag, "loaded bitmap from local storage, refresh timestamp: " + urlToProcess + ", " + TimeUtil.now());
                    bitmapCache.put(urlToProcess, bitmap);
                    localImageFileIndex.put(urlToProcess, TimeUtil.now());   //put after every access
                    SharedPreferences.Editor editor = index.edit();
                    editor.putString(urlToProcess, TimeUtil.now());
                    editor.commit();
                    return bitmap;
                }
            }
            else{
                Log.d(tag, "bitmap not found in local file storage - download to use and save, refresh timestamp");
                downloadBitmapFromUrl(url);
            }
            return null;
        }
    }

    //e.g. http://54.254.147.226:80/v1/user_icon?user_id=1100101446780893099&token=39DB2A2FC2D26CFB1053F0229A6AAEDF7ECAF4BD9FA1A5C8390475293368A414&buddy_user_id=1100101446780892087
    // after: http://54.254.147.226:80/v1/user_icon?buddy_user_id=1100101446780892087
    private String getUsericonUrlWithoutToken(String url){
        String prefix = url.substring(0, url.indexOf(USER_ID_EQUAL));
        String suffix = url.substring(url.indexOf(BUDDY_USER_ID_EQUAL), url.length());
        String url2 = prefix + suffix;
        Log.v(tag, "user icon url without token: " + url2);
        return url2;
    }

    //e.g. http://54.254.147.226:80/v1/image?image_path=images/events/2000101449419180409/image/10056611452128154618.jpg
    // local file path: root/data/soco/images/user_icon/1100101446780892087/user_icon.jpg
    private static Bitmap loadLocalBitmapFileForImage(String url){
        Log.d(tag, "load image bitmap file from local: " + url);

        String localFilePath = getLocalFilePathFromImageUrl(url);
        String root = Environment.getExternalStorageDirectory().toString();
        String photoPath = root + SEPARATOR + DATA_FOLDER_NAME + "/" + COMPANY_NAME + SEPARATOR + localFilePath;
        Log.d(tag, "local image file path: " + photoPath);

        return loadBitmap(photoPath);
    }

    // after: http://54.254.147.226:80/v1/user_icon?buddy_user_id=1100101446780892087
    // local file path: root/data/soco/images/user_icon/1100101446780892087/user_icon.jpg
    private static Bitmap loadLocalBitmapFileForUsericon(String urlToProcess){
        Log.d(tag, "load usericon bitmap file from local: " + urlToProcess);

        String userId = urlToProcess.substring(urlToProcess.indexOf(USER_ID_EQUAL) + 8, urlToProcess.length());
        Log.d(tag, "userid: " + userId);

        String root = Environment.getExternalStorageDirectory().toString();
        String photoPath = root + SEPARATOR + DATA_FOLDER_NAME + "/" + COMPANY_NAME
                            + SEPARATOR + IMAGES + SEPARATOR + USER_ICON
                            + SEPARATOR + userId + SEPARATOR + USER_ICON_JPG;
        Log.d(tag, "local usericon file path: " + photoPath);

        return loadBitmap(photoPath);
    }

    private static Bitmap loadBitmap(String photoPath){
        try {
            //approach 1
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);

            //approach 2: avoid out of memory error
            BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 8;
            Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);


            //approach 3: give a scale factor (according to screensize), auto calculate inSampleSize
            bitmap = IconUrlUtil.decodeSampledBitmapFromFile(
                    photoPath, SocoApp.screenSizeWidth / 2, SocoApp.screenSizeHeight / 2);

            Log.d(tag, "loaded image from local: " + bitmap);
            return bitmap;
        }
        catch(OutOfMemoryError e){
            Log.e(tag, "out of memory when load image: " + e.toString());
            e.printStackTrace();
            return null;
        }
    }

    private Bitmap downloadBitmapFromUrl(String url){
        Log.d(tag, "download bitmap from url: " + url);
        IconDownloadTask task = new IconDownloadTask(this);
        task.execute(url);
        return null;
    }

    public void doneTask(Object o){
        Log.d(tag, "done task");
        if(o == null){
            Log.e(tag, "return null");
        }
        else if(o instanceof Bitmap){   //IconDownloadTask
            Log.d(tag, "return bitmap");
            Bitmap bitmap = (Bitmap) o;

            Log.d(tag, "image url - update image cache:: " + urlToProcess+ ", " + bitmap);
            bitmapCache.put(urlToProcess, bitmap);
            Log.d(tag, "image cache size: " + bitmapCache.size() + " kB");
            saveBitmapFileToLocal2(bitmap, urlToProcess);
        }
        else if(o instanceof Boolean){  //SaveFileTask
            boolean ret = (boolean) o;
            if(ret){
                Log.d(tag, "task save file success");
            }
        }
    }

    private boolean isImageUrl(String url){
        if(url.indexOf(URL_IDENTIFIER_IMAGE) != -1)
            return true;
        else
            return false;
    }

    private boolean isUsericonUrl(String url){
        if(url.indexOf(URL_IDENTIFIER_USERICON) != -1)
            return true;
        else
            return false;
    }

    //e.g. http://54.254.147.226:80/v1/image?image_path=images/events/2000101449419180409/image/10056611452128154618.jpg
    public void saveBitmapFileToLocal2(Bitmap bitmap, String url){
        if(isImageUrl(url)){
            Log.d(tag, "save image bitmap to local: " + url);
            String localFilePath = getLocalFilePathFromImageUrl(url);
            saveBitmapFileToLocal(bitmap, localFilePath);
        }
        else if(isUsericonUrl(url)){
            Log.d(tag, "save usericon bitmap to local: " + url);
            String localFilePath = getLocalFilePathFromUsericonUrl(url);
            saveBitmapFileToLocal(bitmap, localFilePath);
        }

        Log.d(tag, "save image to local index, url: " + url + ", timestamp: " + TimeUtil.now());
        localImageFileIndex.put(url, TimeUtil.now());   //put after every access
        Log.d(tag, "saved image to local index, current size: " + localImageFileIndex.size());

        if(isImageUrl(url)) {
            Log.d(tag, "persistence image index to local storage: " + url);
            SharedPreferences.Editor editor = index.edit();
            editor.putString(url, TimeUtil.now());
            editor.commit();
        }
        else if(isUsericonUrl(url)){
            //approach 1:
            Log.d(tag, "do not persistence usericon index - let App download all latest usericon on each starts");

            //approach 2: still persist (done), but refresh after some time (n.a.)
            SharedPreferences.Editor editor = index.edit();
            editor.putString(url, TimeUtil.now());
            editor.commit();
        }
    }

    //e.g. url: http://54.254.147.226:80/v1/image?image_path=images/events/2000101449419180409/image/8792531452149707670.jpg
    // local file path: images/events/2000101449419180409/image/8792531452149707670.jpg
    private static String getLocalFilePathFromImageUrl(String url){
        Log.d(tag, "get local file for image: " + url);
        String localFilePath = url.substring(url.indexOf(EQUAL) + 1, url.length());
        Log.d(tag, "local image file path: " + localFilePath);
        return localFilePath;
    }

    //e.g. url: http://54.254.147.226:80/v1/user_icon?user_id=1100101446780893099
    // local file path: images/user_icon/1100101446780893099/user_icon.jpg
    private static String getLocalFilePathFromUsericonUrl(String urlWithoutToken){
        Log.d(tag, "usericon url: " + urlWithoutToken);
        String userId = urlWithoutToken.substring(urlWithoutToken.indexOf(USER_ID_EQUAL)+8, urlWithoutToken.length());
        Log.d(tag, "userid: " + userId);

        String localFilePath = IMAGES + SEPARATOR + USER_ICON + SEPARATOR + userId + SEPARATOR + USER_ICON_JPG;
        Log.d(tag, "local usericon file path: " + localFilePath);
        return localFilePath;
    }


    //e.g. images/events/2000101449419180409/image/10056611452128154618.jpg
    private void saveBitmapFileToLocal(Bitmap bitmap, String localFilePath){
        try {
            String root = Environment.getExternalStorageDirectory().toString();

            String localDir = localFilePath.substring(0, localFilePath.lastIndexOf("/")+1);
            String filename = localFilePath.substring(localFilePath.lastIndexOf("/")+1, localFilePath.length());
            Log.d(tag, "localdir: " + localDir + ", filename: " + filename);

            File absoluteDir = new File(root + SEPARATOR + DATA_FOLDER_NAME + "/" + COMPANY_NAME + SEPARATOR + localDir);
            absoluteDir.mkdirs();

//            Log.w(tag, "saving local file: " + absoluteDir + "/" + filename);
//            File file = new File(absoluteDir, filename);
//            if(file.exists()) {
//                Log.d(tag, "delete existing file");
//                file.delete();
//            }
//
//            FileOutputStream fos = new FileOutputStream(file);
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);   //todo
//            fos.flush();
//            fos.close();
//            Log.d(tag, "file saved locally success: " + absoluteDir + ", " + filename);
            SaveFileTask task = new SaveFileTask(absoluteDir, filename, bitmap, this);
            task.execute();
        }
        catch(Exception e){
            Log.e(tag, "cannot save file: " + e.toString());
            e.printStackTrace();
        }

        return;
    }

}
