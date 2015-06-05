package com.soco.SoCoClient._v2.businesslogic.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.soco.SoCoClient._v2.businesslogic.config.DbConfig;
import com.soco.SoCoClient._v2.datamodel.Contact;
import com.soco.SoCoClient._v2.datamodel.Task;
import com.soco.SoCoClient.control.config.DataConfig;
import com.soco.SoCoClient.control.db.DBHelperSoco;

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
                "select * from " + DbConfig.TABLE_TASK
                        + " where " + DbConfig.COLUMN_TASK_ISTASKACTIVE + " = ?",
                new String[]{String.valueOf(DbConfig.TASK_IS_ACTIVE)});

        while(cursor.moveToNext()){
            Task task = new Task(cursor);
            Log.d(tag, "load task from database: " + task.toString());
            tasks.add(task);
        }
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
        return null;
    }


}
