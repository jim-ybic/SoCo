package com.soco.SoCoClient.events.details;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.soco.SoCoClient.R;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class AddPostActivity extends ActionBarActivity {

    static final String tag = "AddPostActivity";
    static final int REQUESTCODE_ADDPHOTO = 101;
    static final int REQUESTCODE_CUTTING = 102;
    public static final String EVENT_ID = "event_id";

    Context context;
    long eventId;
    ProgressDialog pd;
    Uri uriFile;
    String suffix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        eventId = i.getLongExtra(EVENT_ID, 0);
        Log.v(tag, "get event id: " + eventId);

        context = getApplicationContext();
        //todo
    }

    public void post(View view){
        Log.v(tag, "tap post");

        String text = ((EditText) findViewById(R.id.text)).getText().toString();
        Log.v(tag, "get text: " + text);

        //todo
    }

    public void addPhoto(View view){
        Log.v(tag, "tap add photo");
        Intent i =  new Intent(Intent.ACTION_PICK, null);
        i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(i, REQUESTCODE_ADDPHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data == null){
            Log.w(tag, "Activity return result is null");
            return;
        }
        if (requestCode == REQUESTCODE_ADDPHOTO && resultCode == Activity.RESULT_OK) {
            Log.v(tag, "add photo result ok");
            uriFile = data.getData();
            Log.i(tag, "file selected with uri: " + uriFile + ", " + uriFile.toString() + ", " + uriFile.getPath());

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uriFile, filePathColumn, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                Log.d(tag, "filepath: " + filePath + ", bitmap: " + bitmap);
                suffix = filePath.substring(filePath.lastIndexOf("."));  //e.g. .jpg
            }
            cursor.close();

            new Thread(new Runnable(){
                public void run(){
                    File file = new File(uriFile.getPath());
                    Log.d(tag, "file: " + file + ", length: " + file.length() + ", bitmap: null");
                    String url = "http://54.254.147.226:80/v1/user_icon";
                    FileImageUpload.uploadFile(
                            file, url, getContentResolver(), uriFile, suffix);
                }
            }).start();
        }
    }

}


