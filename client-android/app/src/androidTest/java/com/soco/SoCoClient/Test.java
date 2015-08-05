package com.soco.SoCoClient;

import android.content.Context;
import android.test.ActivityTestCase;
import android.util.Log;
import android.widget.Toast;

import com.soco.SoCoClient.v2.businesslogic.database.DataLoader;
import com.soco.SoCoClient.v2.datamodel.Task;

import junit.framework.Assert;

public class Test extends ActivityTestCase {

    String tag = "Test";
    Context context = getActivity().getApplicationContext();

    public void testHappy(){
        Assert.assertTrue(true);
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

}
