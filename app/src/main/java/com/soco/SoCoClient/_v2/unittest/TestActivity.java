package com.soco.SoCoClient._v2.unittest;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient._v2.businesslogic.config.DbConfig;
import com.soco.SoCoClient._v2.businesslogic.database.DataLoader;
import com.soco.SoCoClient._v2.datamodel.Attribute;
import com.soco.SoCoClient._v2.datamodel.Task;

import java.util.ArrayList;

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
        Task task = new Task(getApplicationContext());
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
        Toast.makeText(getApplicationContext(), "test1 success", Toast.LENGTH_SHORT).show();
    }

    public void test2 (View view){
        Log.i(tag, ">>>test2 start: attribute basic");
        DataLoader dataLoader = new DataLoader(getApplicationContext());

        Log.i(tag, ">>>create new task");
        Task task = new Task(getApplicationContext());
        task.setTaskName("TEST1");
        task.save();

        Log.i(tag, ">>>create an attribute");
        Attribute attr1 = new Attribute(context, DbConfig.ACTIVITY_TYPE_TASK,
                task.getTaskIdLocal(), task.getTaskIdServer());
        attr1.setAttrName(DbConfig.ATTRIBUTE_NAME_DATE);
        attr1.setAttrValue("2015-06-06");
        attr1.save();

        Log.i(tag, ">>>update attribute");
        attr1.setAttrValue("2015-07-07");
        attr1.save();

        Log.i(tag, ">>>create another attribute");
        Attribute attr2 = new Attribute(context, DbConfig.ACTIVITY_TYPE_TASK,
                task.getTaskIdLocal(), task.getTaskIdServer());
        attr2.setAttrName(DbConfig.ATTRIBUTE_NAME_LOCATION);
        attr2.setAttrValue("hk");
        attr2.save();

        Attribute attr3 = new Attribute(context, DbConfig.ACTIVITY_TYPE_TASK,
                task.getTaskIdLocal(), task.getTaskIdServer());
        attr3.setAttrName(DbConfig.ATTRIBUTE_NAME_DESCRIPTION);
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
        Toast.makeText(getApplicationContext(), "test2 success", Toast.LENGTH_SHORT).show();
    }

}
