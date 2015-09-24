package com.soco.SoCoClient.model;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.soco.SoCoClient.control.config.DataConfig;
import com.soco.SoCoClient.control.database.DataLoader;
import com.soco.SoCoClient.control.database.DbHelper;
import com.soco.SoCoClient.control.http.task.CreateTaskOnServerJob;
import com.soco.SoCoClient.control.http.task.InviteContactJoinTaskJob;
import com.soco.SoCoClient.control.util.TimeUtil;

import java.util.ArrayList;
import java.util.HashSet;

public class Task {

    String tag = "Task";

    //fields saved to db
    int taskIdLocal;
    int taskIdServer;
    String taskName;
    String taskPath;
    int isTaskActive;

    //fields not saved to db
    Context context;
    SQLiteDatabase db;

    public Task(Context context) {
        Log.v(tag, "create new task object");

        this.context = context;

        this.taskIdLocal = DataConfig.ENTITIY_ID_NOT_READY;
        this.taskIdServer = DataConfig.ENTITIY_ID_NOT_READY;
        this.isTaskActive = DataConfig.TASK_IS_ACTIVE;

        DbHelper dbHelper = new DbHelper(context);
        this.db = dbHelper.getWritableDatabase();
    }

    public Task(Cursor cursor){
        Log.v(tag, "create task from cursor");
        this.taskIdLocal = cursor.getInt(cursor.getColumnIndex(DataConfig.COLUMN_TASK_TASKIDLOCAL));
        this.taskIdServer = cursor.getInt(cursor.getColumnIndex(DataConfig.COLUMN_TASK_TASKIDSERVER));
        this.taskName = cursor.getString(cursor.getColumnIndex(DataConfig.COLUMN_TASK_TASKNAME));
        this.taskPath = cursor.getString(cursor.getColumnIndex(DataConfig.COLUMN_TASK_TASKPATH));
        this.isTaskActive = cursor.getInt(cursor.getColumnIndex(DataConfig.COLUMN_TASK_ISTASKACTIVE));
        Log.v(tag, "created task from cursor: " + toString());
    }

    public void save(){
        if(taskIdLocal == DataConfig.ENTITIY_ID_NOT_READY) {
            Log.v(tag, "save new task");
            saveNew();
        }else{
            Log.v(tag, "update existing task");
            update();
        }
    }

    void saveNew(){
        Log.v(tag, "save new task to local database");
        try {
            db.beginTransaction();
            ContentValues cv = new ContentValues();
            cv.put(DataConfig.COLUMN_TASK_TASKIDSERVER, taskIdServer);
            cv.put(DataConfig.COLUMN_TASK_TASKNAME, taskName);
            cv.put(DataConfig.COLUMN_TASK_TASKPATH, taskPath);
            cv.put(DataConfig.COLUMN_TASK_ISTASKACTIVE, isTaskActive);
            db.insert(DataConfig.TABLE_TASK, null, cv);
            db.setTransactionSuccessful();
            Log.d(tag, "new task inserted into database: " + toString());
        } finally {
            db.endTransaction();
        }

        Log.v(tag, "get task id local from database");
        int tidLocal = -1;
        String query = "select max (" + DataConfig.COLUMN_TASK_TASKIDLOCAL
                + ") from " + DataConfig.TABLE_TASK;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            tidLocal = cursor.getInt(0);
            Log.d(tag, "update task id local: " + tidLocal);
            taskIdLocal = tidLocal;
        }

        if(taskIdServer == DataConfig.ENTITIY_ID_NOT_READY) {
            Log.v(tag, "save new task to server: " + toString());
            CreateTaskOnServerJob job = new CreateTaskOnServerJob(context, this);
            job.execute();
        }else
            Log.v(tag, "task already saved on server");
    }

    void update(){
        Log.v(tag, "update existing task to local database");
        try {
            db.beginTransaction();
            ContentValues cv = new ContentValues();
            cv.put(DataConfig.COLUMN_TASK_TASKIDLOCAL, taskIdLocal);
            cv.put(DataConfig.COLUMN_TASK_TASKIDSERVER, taskIdServer);
            cv.put(DataConfig.COLUMN_TASK_TASKNAME, taskName);
            cv.put(DataConfig.COLUMN_TASK_TASKPATH, taskPath);
            cv.put(DataConfig.COLUMN_TASK_ISTASKACTIVE, isTaskActive);
            db.update(DataConfig.TABLE_TASK, cv,
                    DataConfig.COLUMN_TASK_TASKIDLOCAL + " = ?",
                    new String[]{String.valueOf(taskIdLocal)});
            db.setTransactionSuccessful();
            Log.d(tag, "task updated into database: " + toString());
        } finally {
            db.endTransaction();
        }

        //todo: update task to server

    }

    public void refresh(){
        Log.v(tag, "refresh from database for task: " + toString());
        String query = "select * from " + DataConfig.TABLE_TASK
                + " where " + DataConfig.COLUMN_TASK_TASKIDLOCAL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(taskIdLocal)});

        Task task = null;
        while(cursor.moveToNext()) {
            task = new Task(cursor);
            Log.d(tag, "found task from database: " + toString());
        }

        if(task == null)
            Log.e(tag, "unexpected error, cannot refresh task from database");
        else{
            taskIdServer = task.taskIdServer;
            taskName = task.taskName;
            taskPath = task.taskPath;
            isTaskActive = task.isTaskActive;
            Log.d(tag, "refresh complete: " + toString());
        }
    }

    public void delete(){
        Log.v(tag, "delete existing task");
        if(taskIdLocal == DataConfig.ENTITIY_ID_NOT_READY){
            Log.e(tag, "cannot delete a non-existing task");
        } else {
            db.delete(DataConfig.TABLE_TASK,
                    DataConfig.COLUMN_TASK_TASKIDLOCAL + " = ?",
                    new String[]{String.valueOf(taskIdLocal)});
            Log.d(tag, "task deleted from database: " + toString());
        }

        //todo: delete task from server
    }

    public void addMember(Contact contact, String role, String status){
        Log.v(tag, "add individual party as task member: " + contact.toString());
        try{
            db.beginTransaction();
            ContentValues cv = new ContentValues();
            cv.put(DataConfig.COLUMN_PARTYJOINACTIVITY_PARTYTYPE, DataConfig.PARTY_TYPE_INDIVIDUAL);
            cv.put(DataConfig.COLUMN_PARTYJOINACTIVITY_PARTYIDLOCAL, contact.getContactIdLocal());
            cv.put(DataConfig.COLUMN_PARTYJOINACTIVITY_PARTYIDSERVER, contact.getContactIdServer());
            cv.put(DataConfig.COLUMN_PARTYJOINACTIVITY_ACTIVITYTYPE, DataConfig.ACTIVITY_TYPE_TASK);
            cv.put(DataConfig.COLUMN_PARTYJOINACTIVITY_ACTIVITYIDLOCAL, taskIdLocal);
            cv.put(DataConfig.COLUMN_PARTYJOINACTIVITY_ACTIVITYIDSERVER, taskIdServer);
            cv.put(DataConfig.COLUMN_PARTYJOINACTIVITY_ROLE, role);
            cv.put(DataConfig.COLUMN_PARTYJOINACTIVITY_STATUS, status);
            cv.put(DataConfig.COLUMN_PARTYJOINACTIVITY_JOINTIMESTAMP, TimeUtil.now());
            db.insert(DataConfig.TABLE_PARTYJOINACTIVITY, null, cv);
            db.setTransactionSuccessful();
            Log.d(tag, "new member [" + contact.toString() + "] added to task ["
                    + taskName + "]: " + toString()
                    + ", role is " + role + ", status is " + status);
        }finally{
            db.endTransaction();
        }

        //todo: save to server

        Log.v(tag, "send request to server");
        InviteContactJoinTaskJob job = new InviteContactJoinTaskJob(context, contact, this);
        job.execute();
    }

    public void setMemberStatus(Contact contact, String status){
        Log.v(tag, "set member [" + contact.toString() + "] status to: " + status);
        try{
            db.beginTransaction();
            ContentValues cv = new ContentValues();
            cv.put(DataConfig.COLUMN_PARTYJOINACTIVITY_STATUS, status);
            db.update(DataConfig.TABLE_PARTYJOINACTIVITY, cv,
                            DataConfig.COLUMN_PARTYJOINACTIVITY_PARTYTYPE + " = ? "
                            + " and " + DataConfig.COLUMN_PARTYJOINACTIVITY_PARTYIDLOCAL + " = ? "
                            + " and " + DataConfig.COLUMN_PARTYJOINACTIVITY_ACTIVITYTYPE + " = ? "
                            + " and " + DataConfig.COLUMN_PARTYJOINACTIVITY_ACTIVITYIDLOCAL + " = ?",
                    new String[]{
                            DataConfig.PARTY_TYPE_INDIVIDUAL,
                            String.valueOf(contact.contactIdLocal),
                            DataConfig.ACTIVITY_TYPE_TASK,
                            String.valueOf(taskIdLocal)
                    });
            db.setTransactionSuccessful();
            Log.v(tag, "set member [" + contact.toString() + "] status to: " + status);
        }finally{
            db.endTransaction();
        }
    }

    public ArrayList<Contact> loadMembers(){
        String query = "select " + DataConfig.COLUMN_PARTYJOINACTIVITY_PARTYIDLOCAL
                        + " from " + DataConfig.TABLE_PARTYJOINACTIVITY
                        + " where " + DataConfig.COLUMN_ATTACHMENT_ACTIVITYTYPE + " = ?"
                        + " and " + DataConfig.COLUMN_ATTRIBUTE_ACTIVITYIDLOCAL + " = ?";
        Cursor cursor = db.rawQuery(query,
                new String[]{DataConfig.ACTIVITY_TYPE_TASK, String.valueOf(taskIdLocal)});

        HashSet<Integer> contactIdLocalSet = new HashSet<>();
        while(cursor.moveToNext()){
            int idLocal = cursor.getInt(0);
            Log.v(tag, "get id local: " + idLocal);
            contactIdLocalSet.add(idLocal);
        }

        DataLoader dataLoader = new DataLoader(context);
        ArrayList<Contact> contacts = dataLoader.loadContacts(contactIdLocalSet);

        Log.d(tag, contacts.size() + " contact loaded for task: " + toString());
        return contacts;
    }

    public Attribute newAttribute(){
        Attribute attr = new Attribute(context, DataConfig.ACTIVITY_TYPE_TASK,
                taskIdLocal, taskIdServer);
        Log.d(tag, "created new attribute [" + attr.toString()
                + "] for task [" + toString() + "]");
        return attr;
    }
    public void updateAttribute(Attribute attr){}

    public void clearAttributes(){}

    public ArrayList<Attribute> loadAttributes(){
        Log.v(tag, "load attribute for task: " + toString());
        ArrayList<Attribute> attributes = new ArrayList<>();

        String query = "select * from " + DataConfig.TABLE_ATTRIBUTE
                + " where " + DataConfig.COLUMN_ATTRIBUTE_ACTIVITYTYPE + " = ? "
                + " and " + DataConfig.COLUMN_ATTRIBUTE_ACTIVITYIDLOCAL + " = ?";
        Cursor cursor = db.rawQuery(query,
                new String[]{DataConfig.ACTIVITY_TYPE_TASK, String.valueOf(taskIdLocal)});

        while(cursor.moveToNext()){
            Attribute attr = new Attribute(cursor);
            attributes.add(attr);
        }

        Log.d(tag, attributes.size() + " attributes loaded for task: " + toString());
        return attributes;
    }

    public Message newMessage(){return null;}

    public ArrayList<Message> loadMessages(){
        return null;
    }

    public Attachment newAttachment(){
        Attachment attachment = new Attachment(context, DataConfig.ACTIVITY_TYPE_TASK,
                taskIdLocal, taskIdServer);
        Log.d(tag, "created new attacment [" + attachment.toString()
                + "] for task [" + toString() + "]");
        return attachment;
    }

    public ArrayList<Attachment> loadAttachments(){
        return null;
    }

    public String toString() {
        return "Task{" +
                "taskIdLocal=" + taskIdLocal +
                ", taskIdServer=" + taskIdServer +
                ", taskName='" + taskName + '\'' +
                ", taskPath='" + taskPath + '\'' +
                ", isTaskActive=" + isTaskActive +
                '}';
    }

    public int getTaskIdLocal() {
        return taskIdLocal;
    }

    public void setTaskIdLocal(int taskIdLocal) {
        this.taskIdLocal = taskIdLocal;
    }

    public int getTaskIdServer() {
        return taskIdServer;
    }

    public void setTaskIdServer(int taskIdServer) {
        this.taskIdServer = taskIdServer;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskPath() {
        return taskPath;
    }

    public void setTaskPath(String taskPath) {
        this.taskPath = taskPath;
    }

    public int getIsTaskActive() {
        return isTaskActive;
    }

    public void setIsTaskActive(int isTaskActive) {
        this.isTaskActive = isTaskActive;
    }
}
