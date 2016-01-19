package com.soco.SoCoClient.topics;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.TaskCallBack;

public class CreateTopicActivity extends ActionBarActivity
        implements TaskCallBack {

    static final String tag = "CreateTopic";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_topic);
    }

    public void createtopic(View v){
        Log.d(tag, "tap create topic");

        String title = ((EditText) findViewById(R.id.title)).getText().toString();
        String desc = ((EditText) findViewById(R.id.desc)).getText().toString();
        if(title.isEmpty()){
            Log.d(tag, "error: title empty");
            Toast.makeText(getApplicationContext(), "Error: title is empty.", Toast.LENGTH_SHORT).show();
            return;
        }
        Topic t = new Topic();
        t.setTitle(title);
        t.setIntroduction(desc);

        Log.d(tag, "background task to create topic: " + t.toString());
        new AddTopicTask(t, this).execute();
    }

    public void doneTask(Object o){
        Log.v(tag, "done task from add topic");
        Boolean ret = (Boolean) o;
        if(ret) {
            Log.v(tag, "post success");
            Toast.makeText(getApplicationContext(), "Create topic success.", Toast.LENGTH_SHORT).show();
            finish();
        }else {
            Log.e(tag, "post fail");
            Toast.makeText(getApplicationContext(), R.string.msg_network_error, Toast.LENGTH_SHORT).show();
        }
    }

}
