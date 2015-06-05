package com.soco.SoCoClient.v2.businesslogic.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.soco.SoCoClient.v2.businesslogic.config.DataConfig2;
import com.soco.SoCoClient.v2.datamodel.Contact;
import com.soco.SoCoClient.v2.datamodel.Task;

import java.util.ArrayList;

public class DataLoader {

    String tag = "DataLoader";

    SQLiteDatabase db;

    public DataLoader(Context context){
        DbHelper dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public ArrayList<Task> loadActiveTasks(){
        Log.v(tag, "load active tasks from database");
        ArrayList<Task> tasks = new ArrayList<>();
        Cursor cursor = db.rawQuery(
                "select * from " + DataConfig2.TABLE_TASK
                        + " where " + DataConfig2.COLUMN_TASK_ISTASKACTIVE + " = ?",
                new String[]{String.valueOf(DataConfig2.TASK_IS_ACTIVE)});

        while(cursor.moveToNext()){
            Task task = new Task(cursor);
            Log.d(tag, "load task from database: " + task.toString());
            tasks.add(task);
        }

        Log.d(tag, tasks.size() + " tasks loaded from database");
        return tasks;
    }

    public ArrayList<Task> loadInactiveTasks(){
        return null;
    }

    public Task loadTaskByIdLocal(int idLocal){
        return null;
    }

    public Task loadTaskByIdServer(int idServer){
        return null;
    }

    public Task loadTaskByPath(String path){
        return null;
    }

    public ArrayList<Contact> loadContacts(){
        Log.v(tag, "load contacts from database");
        ArrayList<Contact> contacts = new ArrayList<>();
        String query =  "select * from " + DataConfig2.TABLE_CONTACT;
        Cursor cursor = db.rawQuery(query, null);

        while(cursor.moveToNext()){
            Contact contact = new Contact(cursor);
            Log.d(tag, "load contact from database: " + contact.toString());
            contacts.add(contact);
        }

        Log.d(tag, contacts.size() + " contacts loaded from database");
        return contacts;
    }


}
