package com.soco.SoCoClient.v2.unittest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.v2.businesslogic.config.DataConfig2;
import com.soco.SoCoClient.v2.businesslogic.config.GeneralConfig2;
import com.soco.SoCoClient.v2.businesslogic.config.HttpConfig2;
import com.soco.SoCoClient.v2.businesslogic.database.DataLoader;
import com.soco.SoCoClient.v2.businesslogic.http.Heartbeat2;
import com.soco.SoCoClient.v2.businesslogic.http.task.SendMessageJob;
import com.soco.SoCoClient.v2.datamodel.Attribute;
import com.soco.SoCoClient.v2.datamodel.Contact;
import com.soco.SoCoClient.v2.datamodel.Message;
import com.soco.SoCoClient.v2.datamodel.Task;

import java.util.ArrayList;

public class TestActivity extends ActionBarActivity {

    String tag = "TestActivity";

    Context context;
    DataLoader dataLoader;
    SharedPreferences settings;
    Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v2_activity_test);

        context = getApplicationContext();
        dataLoader = new DataLoader(context);
        settings = context.getSharedPreferences(GeneralConfig2.PROFILE_FILENAME, 0);
        editor = settings.edit();
    }

    public void jim(View view){
        Log.i(tag, ">>>setup profile: jim.ybic@gmail.com");
        editor.putString(HttpConfig2.PROFILE_LOGIN_ACCESS_TOKEN, "49bugba6gfkoqpc2fho92tc4ajfi7aaj");
        editor.commit();

        Log.i(tag, ">>>start heartbeat service");
        Intent heartbeat = new Intent(this, Heartbeat2.class);
        startService(heartbeat);
    }

    public void voljin(View view){
        Log.i(tag, ">>>setup profile: voljin.g@gmail.com");
        editor.putString(HttpConfig2.PROFILE_LOGIN_ACCESS_TOKEN, "mrqq9v1e4901vn065vfufep1f9iu9ejk");
        editor.commit();

        Log.i(tag, ">>>start heartbeat service");
        Intent heartbeat = new Intent(this, Heartbeat2.class);
        startService(heartbeat);
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

        Log.i(tag, ">>>setup profile: jim.ybic@gmail.com");
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

    public void test4(View view){
        Log.i(tag, ">>>test4: contact basic");
        DataLoader dataLoader = new DataLoader(context);

        Log.i(tag, ">>>create new contact");
        Contact contact1 = new Contact(context);
        contact1.setContactEmail("jim.ybic@gmail.com");
        contact1.setContactUsername("jim1");

        Log.i(tag, ">>>save to local");
        contact1.save();
        dataLoader.loadContacts();

        Log.i(tag, ">>>update and save");
        contact1.setContactUsername("jim2");
        contact1.save();
        dataLoader.loadContacts();

        Log.i(tag, ">>>delete contact");
        contact1.delete();
        dataLoader.loadContacts();

        Log.i(tag, ">>>test4 success");
        Toast.makeText(context, "test4 success", Toast.LENGTH_SHORT).show();
    }

    public void test5(View view){
        Log.i(tag, ">>>test5: message basic");
        DataLoader dataLoader = new DataLoader(context);

        Log.i(tag, ">>>create new message");
        Message msg1 = new Message(context);
        msg1.setFromType(1);
        msg1.setFromId("jim.ybic@gmail.com");
        msg1.setToType(1);
        msg1.setToId("voljin.g@gmail.com");
        msg1.setContent("hello world");

        Log.i(tag, ">>>save to local");
        msg1.save();
        dataLoader.loadMessages();

        Log.i(tag, ">>>send to server");
        SendMessageJob job = new SendMessageJob(context, msg1);
        job.execute();

        Log.i(tag, ">>>delete message");
        msg1.delete();
        dataLoader.loadMessages();

        Log.i(tag, ">>>test5 success");
        Toast.makeText(context, "test5 success", Toast.LENGTH_SHORT).show();

    }

    public void test6(View view) {
        Log.i(tag, ">>>test6: message exchange");

        Log.i(tag, ">>>start heartbeat service");
        Intent heartbeat = new Intent(this, Heartbeat2.class);
        startService(heartbeat);

        Log.i(tag, ">>>setup profile: jim.ybic@gmail.com");
        SharedPreferences settings = context.getSharedPreferences(GeneralConfig2.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(HttpConfig2.PROFILE_LOGIN_ACCESS_TOKEN, "49bugba6gfkoqpc2fho92tc4ajfi7aaj");
        editor.commit();

        Log.i(tag, ">>>create new message");
        Message msg1 = new Message(context);
        msg1.setFromType(1);
        msg1.setFromId("jim.ybic@gmail.com");
        msg1.setToType(1);
        msg1.setToId("voljin.g@gmail.com");
        msg1.setContent("hello world");
        msg1.save();

        Log.i(tag, ">>>send to server");
        SendMessageJob job = new SendMessageJob(context, msg1);
        job.execute();

        Log.i(tag, ">>>setup profile: voljin.g@gmail.com");
        editor.putString(HttpConfig2.PROFILE_LOGIN_ACCESS_TOKEN, "mrqq9v1e4901vn065vfufep1f9iu9ejk");
        editor.commit();
    }

    public void test7(View view){
        Log.i(tag, ">>>test7: member");

        Log.i(tag, ">>>create new task");
        Task task = new Task(context);
        task.setTaskName("Test task to server");
        task.save();

        Log.i(tag, ">>>create new contact");
        Contact contact1 = new Contact(context);
        contact1.setContactEmail("voljin.g@gmail.com");
        contact1.setContactUsername("voljin1");
        contact1.save();

        Log.i(tag, ">>>invite contact into task");
        task.refresh();
        task.addMember(contact1, "member", "new");

        ArrayList<Contact> members = task.loadMembers();
    }

}
