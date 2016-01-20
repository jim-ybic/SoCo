package com.soco.SoCoClient.events.details;

import android.app.Activity;
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
import android.widget.Toast;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.util.IconUrlUtil;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.events.service.AddPostTask;

public class AddPostActivity extends ActionBarActivity
        implements TaskCallBack{

    static final String tag = "AddPostActivity";
    static final int REQUESTCODE_ADDPHOTO = 101;
    static final int REQUESTCODE_CUTTING = 102;

    public static final String EVENT_ID = "event_id";
    public static final String TOPIC_ID = "topic_id";

    Context context;
    SocoApp socoApp;
    String eventId;
    String topicId;
    Uri uriFile;
    String comment;
    Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().hide();

        Intent i = getIntent();
        eventId = i.getStringExtra(EVENT_ID);
        Log.v(tag, "get event id: " + eventId);
        topicId = i.getStringExtra(TOPIC_ID);
        Log.v(tag, "get topic id: " + topicId);

        context = getApplicationContext();
        socoApp = (SocoApp) context;
    }

    public void post(View view){
        Log.v(tag, "tap post");

        comment = ((EditText) findViewById(R.id.text)).getText().toString();
        Log.v(tag, "get comment: " + comment);

        Log.v(tag, "start add post task, comment: " + comment + ", bitmap: " + bitmap);
        new AddPostTask(
                getApplicationContext(), getContentResolver(),
                uriFile, comment, eventId, topicId,
                this)
                .execute();
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

                bitmap = IconUrlUtil.decodeSampledBitmapFromFile(
                        filePath, socoApp.screenSizeWidth / 4, socoApp.screenSizeHeight / 4);
                if(bitmap == null){
                    Toast.makeText(getApplicationContext(), R.string.msg_image_too_large, Toast.LENGTH_SHORT).show();
                }else {
                    Log.d(tag, "bitmap: " + bitmap);
                    ((ImageView) findViewById(R.id.pic1)).setImageBitmap(bitmap);
                    ImageView view = (ImageView) findViewById(R.id.pic1);
                    view.setRotation(orientation);
                }
            }
            cursor.close();
        }
    }

    public void doneTask(Object o){
        Log.v(tag, "donetask from add post");
        Boolean ret = (Boolean) o;
        if(ret) {
            Log.v(tag, "post success");
            Toast.makeText(getApplicationContext(), R.string.msg_post_success, Toast.LENGTH_SHORT).show();
            finish();
        }else {
            Log.e(tag, "post fail");
            Toast.makeText(getApplicationContext(), R.string.msg_network_error, Toast.LENGTH_SHORT).show();
        }
    }


}


