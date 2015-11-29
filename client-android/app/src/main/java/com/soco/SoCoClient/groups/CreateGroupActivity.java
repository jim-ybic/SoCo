package com.soco.SoCoClient.groups;

import android.app.Activity;
import android.content.Context;
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
import com.soco.SoCoClient.groups.model.Group;
import com.soco.SoCoClient.groups.task.CreateGroupTask;

public class CreateGroupActivity extends ActionBarActivity
    implements TaskCallBack
{

    static final String tag = "CreateGroupActivity";
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        context = getApplicationContext();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void creategroup(View view){
        Log.v(tag, "tap create group");

        Group g = new Group();
        g.setGroup_name(((EditText) findViewById(R.id.name)).getText().toString());
        g.setDescription(((EditText) findViewById(R.id.desc)).getText().toString());
        Log.v(tag, "new group on screen: " + g.toString());

        Log.v(tag, "background task to create group");
        new CreateGroupTask(context, g, this).execute();
    }

    public void doneTask(Object o){
        Log.v(tag, "notify user and finish");
        Toast.makeText(context, "Create group success.", Toast.LENGTH_SHORT).show();
        finish();
    }
}
