package com.soco.SoCoClient.common;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.util.SocoApp;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.UUID;

public class SaveFileTask extends AsyncTask<String, Void, Boolean> {

    private static final String tag = "SaveFileTask";

    File absoluteDir;
    String filename;
    Bitmap bitmap;
    TaskCallBack callback;

    public SaveFileTask(
            File absoluteDir,
            String filename,
            Bitmap bitmap,
            TaskCallBack cb){

        this.absoluteDir = absoluteDir;
        this.filename = filename;
        this.bitmap = bitmap;
        this.callback = cb;
    }

    protected Boolean doInBackground(String... params) {
        Log.d(tag, "task begin");

        Log.w(tag, "saving local file: " + absoluteDir + "/" + filename);
        File file = new File(absoluteDir, filename);
        if(file.exists()) {
            Log.d(tag, "delete existing file");
            file.delete();
        }

        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);   //todo
            fos.flush();
            fos.close();
            Log.d(tag, "file saved locally success: " + absoluteDir + ", " + filename);
        }
        catch (Exception e){
            Log.e(tag, "cannot save file: " + absoluteDir + "/" + filename);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    protected void onPostExecute(Boolean result){
        Log.v(tag, "post execute: " + result);
        callback.doneTask(result);
    }

}
