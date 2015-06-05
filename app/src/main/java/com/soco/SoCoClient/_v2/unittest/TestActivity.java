package com.soco.SoCoClient._v2.unittest;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient._v2.businesslogic.config.DataConfig2;
import com.soco.SoCoClient._v2.businesslogic.config.GeneralConfig2;
import com.soco.SoCoClient._v2.businesslogic.config.HttpConfig2;
import com.soco.SoCoClient._v2.businesslogic.database.DataLoader;
import com.soco.SoCoClient._v2.datamodel.Attribute;
import com.soco.SoCoClient._v2.datamodel.Task;
import com.soco.SoCoClient.control.config.GeneralConfig;

public class TestActivity extends ActionBarActivity {

    String tag = "TestActivity";

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        context = getApplicationContext();
    }

    public void test1 (View view){
        Log.i(tag, ">>>test1 start: task basic");
        DataLoader dataLoader = new DataLoader(context);

        Log.i(tag, ">>>create new task");
        Task task = new Task(context);
        task.setTaskName("TEST1");
        task.save();

        Log.i(tag, ">>>update task");
        task.setTaskName("TEST2");
        task.save();

        Log.i(tag, ">>>load tasks from db");
        dataLoader.loadActiveTasks();

        Log.i(tag, ">>>delete task");
        task.delete();
        dataLoader.loadActiveTasks();

        Log.i(tag, ">>>test1 success");
        Toast.makeText(context, "test1 success", Toast.LENGTH_SHORT).show();
    }

    public void test2 (View view){
        Log.i(tag, ">>>test2 start: attribute basic");
        DataLoader dataLoader = new DataLoader(getApplicationContext());

        Log.i(tag, ">>>create new task");
        Task task = new Task(getApplicationContext());
        task.setTaskName("TEST1");
        task.save();

        Log.i(tag, ">>>create an attribute");
        Attribute attr1 = new Attribute(context, DataConfig2.ACTIVITY_TYPE_TASK,
                task.getTaskIdLocal(), task.getTaskIdServer());
        attr1.setAttrName(DataConfig2.ATTRIBUTE_NAME_DATE);
        attr1.setAttrValue("2015-06-06");
        attr1.save();

        Log.i(tag, ">>>update attribute");
        attr1.setAttrValue("2015-07-07");
        attr1.save();

        Log.i(tag, ">>>create another attribute");
        Attribute attr2 = new Attribute(context, DataConfig2.ACTIVITY_TYPE_TASK,
                task.getTaskIdLocal(), task.getTaskIdServer());
        attr2.setAttrName(DataConfig2.ATTRIBUTE_NAME_LOCATION);
        attr2.setAttrValue("hk");
        attr2.save();

        Attribute attr3 = new Attribute(context, DataConfig2.ACTIVITY_TYPE_TASK,
                task.getTaskIdLocal(), task.getTaskIdServer());
        attr3.setAttrName(DataConfig2.ATTRIBUTE_NAME_DESCRIPTION);
        attr3.setAttrValue("this is a testing attribute");
        attr3.save();

        Log.i(tag, ">>>load attributes for the task");
        task.loadAttributes();

        Log.i(tag, ">>>delete attribute");
        attr1.delete();
        attr2.delete();
        attr3.delete();
        task.loadAttributes();

        Log.i(tag, ">>>test2 success");
        Toast.makeText(context, "test2 success", Toast.LENGTH_SHORT).show();
    }

    public void test3(View view){
        Log.i(tag, ">>>test3 start: task remote");

        Log.i(tag, ">>>setup profile");
        SharedPreferences settings = context.getSharedPreferences(GeneralConfig2.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(HttpConfig2.PROFILE_SERVER_IP, "192.168.0.100");
        editor.putString(HttpConfig2.PROFILE_SERVER_PORT, "8080");
        editor.putString(HttpConfig2.PROFILE_LOGIN_ACCESS_TOKEN, "49bugba6gfkoqpc2fho92tc4ajfi7aaj");
        editor.commit();

        Log.i(tag, ">>>create new task");
        Task task = new Task(context);
        task.setTaskName("Test task to server");

        Log.i(tag, ">>>save task to local and server");
        task.save();

        Log.i(tag, ">>>test3 success local");
        Toast.makeText(context, "test3 success local", Toast.LENGTH_SHORT).show();
    }

}
