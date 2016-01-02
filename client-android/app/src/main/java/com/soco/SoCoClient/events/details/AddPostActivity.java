package com.soco.SoCoClient.events.details;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.util.IconUrlUtil;
import com.soco.SoCoClient.common.util.SocoApp;

public class AddPostActivity extends ActionBarActivity
        implements TaskCallBack{

    static final String tag = "AddPostActivity";
    static final int REQUESTCODE_ADDPHOTO = 101;
    static final int REQUESTCODE_CUTTING = 102;
    public static final String EVENT_ID = "event_id";

    Context context;
    SocoApp socoApp;
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
        socoApp = (SocoApp) context;
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

            String[] orientationColumn = {MediaStore.Images.Media.ORIENTATION};
            Cursor cur = getContentResolver().query(uriFile, orientationColumn, null, null, null);
            int orientation = -1;
            if (cur != null && cur.moveToFirst()) {
                orientation = cur.getInt(cur.getColumnIndex(orientationColumn[0]));
            }
            Log.d(tag, "orientation: " + orientation);

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uriFile, filePathColumn, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);

                Bitmap bitmap = IconUrlUtil.decodeSampledBitmapFromFile(filePath, socoApp.screenSizeX / 2, socoApp.screenSizeY / 2);
                Log.d(tag, "bitmap: " + bitmap);
                ((ImageView) findViewById(R.id.pic1)).setImageBitmap(bitmap);
                ImageView view = (ImageView) findViewById(R.id.pic1);
                view.setRotation(orientation);

//                Log.d(tag, "filepath: " + filePath);
//                suffix = filePath.substring(filePath.lastIndexOf("."));  //e.g. .jpg
            }
            cursor.close();

//            File file = new File(uriFile.getPath());
//            Log.d(tag, "file: " + file + ", length: " + file.length() + ", bitmap: null");
            new SetUserIconTask(getApplicationContext(), getContentResolver(),
                    uriFile, this).execute();

        }
    }

    public void doneTask(Object o){
        Log.v(tag, "donetask");
        //todo
    }


}


