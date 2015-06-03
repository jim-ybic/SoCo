package com.soco.SoCoClient._v2.businesslogic.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.soco.SoCoClient._v2.datamodel.Contact;
import com.soco.SoCoClient._v2.datamodel.Task;
import com.soco.SoCoClient.control.db.DBHelperSoco;

import java.util.ArrayList;

public class DataLoader {

    DBHelperSoco helper;
    SQLiteDatabase db;
    Context context;

    public DataLoader(Context context){
        this.context = context;
    }

    public ArrayList<Task> loadActiveTasks(){
        return null;
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
