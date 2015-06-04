package com.soco.SoCoClient._v2.unittest;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient._v2.businesslogic.database.DataLoader;
import com.soco.SoCoClient._v2.datamodel.Task;

import java.util.ArrayList;

public class TestActivity extends ActionBarActivity {

    String tag = "TestActivity";
    EditText output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    public void test1 (View view){
        Log.i(tag, ">>>create new task");
        Task task = new Task(getApplicationContext());
        task.setTaskName("TEST1");
        task.save();
        Log.i(tag, "task: " + task.toString());

        Log.i(tag, ">>>update task");
        task.setTaskName("TEST2");
        task.save();
        Log.i(tag, "task: " + task.toString());

        Log.i(tag, ">>>load tasks from db");
        DataLoader dataLoader = new DataLoader(getApplicationContext());
        dataLoader.loadActiveTasks();

        Log.i(tag, ">>>delete task");
        task.delete();

        Log.i(tag, ">>>load tasks from db");
        dataLoader.loadActiveTasks();

        Log.i(tag, ">>>test1 complete success");
    }

    public void test2 (View view){
        Log.i(tag, ">>>");
    }

}
