package com.soco.SoCoClient._v2.businesslogic.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.soco.SoCoClient._v2.datamodel.Attachment;
import com.soco.SoCoClient._v2.datamodel.Attribute;
import com.soco.SoCoClient._v2.datamodel.Comment;
import com.soco.SoCoClient._v2.datamodel.Contact;
import com.soco.SoCoClient._v2.datamodel.Task;
import com.soco.SoCoClient.control.db.DBHelperSoco;

import java.util.ArrayList;

public class DbManager {

    DBHelperSoco helper;
    SQLiteDatabase db;
    Context context;

    public DbManager(Context context){
        this.context = context;
    }

    public void addMemberToTask(Contact contact, Task task){}

    public ArrayList<Contact> getMembersOfTask(Task task){
        return null;
    }

    public ArrayList<Comment> getCommentsOfTask(Task task){
        return null;
    }

    public ArrayList<Task> loadActiveTasks(){
        return null;
    }

    public ArrayList<Task> loadInactiveTasks(){
        return null;
    }

    public Task getTaskByIdLocal(int idLocal){
        return null;
    }

    public Task getTaskByIdServer(int idServer){
        return null;
    }

    public ArrayList<Contact> loadContacts(){
        return null;
    }


}
