package com.soco.SoCoClient.model;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.soco.SoCoClient.control.config.DataConfig;
import com.soco.SoCoClient.control.database.DbHelper;
import com.soco.SoCoClient.control.http.task.CreateContactOnServerJob;

import java.util.ArrayList;

public class Contact {

    String tag = "Contact";

    //fields saved to db
    int contactIdLocal;
    int contactIdServer;
    String contactEmail;
    String contactUsername;
    String contactServerStatus;

    //fields not saved to db
    Context context;
    SQLiteDatabase db;

    public Contact(Context context){
        Log.v(tag, "create new contact");

        this.context = context;

        this.contactIdLocal = DataConfig.ENTITIY_ID_NOT_READY;
        this.contactIdServer = DataConfig.ENTITIY_ID_NOT_READY;
        this.contactServerStatus = DataConfig.CONTACT_SERVER_STATUS_UNKNOWN;

        DbHelper dbHelper = new DbHelper(context);
        this.db = dbHelper.getWritableDatabase();
    }

    public Contact(Cursor cursor){
        Log.v(tag, "create contact from cursor");
        this.contactIdLocal = cursor.getInt(cursor.getColumnIndex(DataConfig.COLUMN_CONTACT_CONTACTIDLOCAL));
        this.contactIdServer = cursor.getInt(cursor.getColumnIndex(DataConfig.COLUMN_CONTACT_CONTACTIDSERVER));
        this.contactEmail = cursor.getString(cursor.getColumnIndex(DataConfig.COLUMN_CONTACT_CONTACTEMAIL));
        this.contactUsername = cursor.getString(cursor.getColumnIndex(DataConfig.COLUMN_CONTACT_CONTACTUSERNAME));
        this.contactServerStatus = cursor.getString(cursor.getColumnIndex(DataConfig.COLUMN_CONTACT_CONTACTSERVERSTATUS));
        Log.v(tag, "created contact from cursor: " + toString());
    }

    public void save(){
        if(contactIdLocal == DataConfig.ENTITIY_ID_NOT_READY){
            Log.v(tag, "save new contact");
            saveNew();
        }else{
            Log.v(tag, "update existing contact");
            update();
        }
    }

    void saveNew(){
        Log.v(tag, "save new contact to database");
        try{
            db.beginTransaction();
            ContentValues cv = new ContentValues();
            cv.put(DataConfig.COLUMN_CONTACT_CONTACTIDSERVER, contactIdServer);
            cv.put(DataConfig.COLUMN_CONTACT_CONTACTEMAIL, contactEmail);
            cv.put(DataConfig.COLUMN_CONTACT_CONTACTUSERNAME, contactUsername);
            cv.put(DataConfig.COLUMN_CONTACT_CONTACTSERVERSTATUS, contactServerStatus);
            db.insert(DataConfig.TABLE_CONTACT, null, cv);
            db.setTransactionSuccessful();
            Log.d(tag, "new contact inserted to database: " + toString());
        } finally {
            db.endTransaction();
        }

        Log.v(tag, "get contact id local from database");
        int cidLocal = -1;
        String query = "select max (" + DataConfig.COLUMN_CONTACT_CONTACTIDLOCAL
                + ") from " + DataConfig.TABLE_CONTACT;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            cidLocal = cursor.getInt(0);
            Log.d(tag, "update contact id local: " + cidLocal);
            contactIdLocal = cidLocal;
        }

        //todo: save contact in server, update contactServerStatus field
        Log.v(tag, "save new contact to server: " + toString());
        CreateContactOnServerJob job = new CreateContactOnServerJob(context, this);
        job.execute();
    }

    void update(){
        Log.v(tag, "update existing contact to local database");
        try {
            db.beginTransaction();
            ContentValues cv = new ContentValues();
            cv.put(DataConfig.COLUMN_CONTACT_CONTACTIDLOCAL, contactIdLocal);
            cv.put(DataConfig.COLUMN_CONTACT_CONTACTIDSERVER, contactIdServer);
            cv.put(DataConfig.COLUMN_CONTACT_CONTACTEMAIL, contactEmail);
            cv.put(DataConfig.COLUMN_CONTACT_CONTACTUSERNAME, contactUsername);
            cv.put(DataConfig.COLUMN_CONTACT_CONTACTSERVERSTATUS, contactServerStatus);
            db.update(DataConfig.TABLE_CONTACT, cv,
                    DataConfig.COLUMN_CONTACT_CONTACTIDLOCAL + " = ?",
                    new String[]{String.valueOf(contactIdLocal)});
            db.setTransactionSuccessful();
            Log.d(tag, "contact updated into database: " + toString());
        } finally {
            db.endTransaction();
        }

        //todo: update task to server

    }

    public void refresh(){
        Log.v(tag, "refresh from database for contact: " + toString());
        String query = "select * from " + DataConfig.TABLE_CONTACT
                + " where " + DataConfig.COLUMN_CONTACT_CONTACTIDLOCAL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(contactIdLocal)});

        Contact contact = null;
        while(cursor.moveToNext()) {
            contact = new Contact(cursor);
            Log.d(tag, "found contact from database: " + toString());
        }

        if(contact == null)
            Log.e(tag, "unexpected error, cannot refresh contact from database");
        else{
            contactIdServer = contact.contactIdServer;
            contactEmail = contact.contactEmail;
            contactUsername = contact.contactUsername;
            contactServerStatus = contact.contactServerStatus;
            Log.v(tag, "refresh complete");
        }
    }

    public void delete(){
        Log.v(tag, "delete existing contact");
        if(contactIdLocal == DataConfig.ENTITIY_ID_NOT_READY){
            Log.e(tag, "cannot delete a non-existing contact");
        } else {
            db.delete(DataConfig.TABLE_CONTACT,
                    DataConfig.COLUMN_CONTACT_CONTACTIDLOCAL + " = ?",
                    new String[]{String.valueOf(contactIdLocal)});
            Log.d(tag, "contact deleted from database: " + toString());
        }

        //todo: delete contact from server
    }

    public void addMessage(Message message){}

    public ArrayList<Message> loadMessages(){
        return null;
    }

    public String toString() {
        return "Contact{" +
                "contactIdLocal=" + contactIdLocal +
                ", contactIdServer=" + contactIdServer +
                ", contactEmail='" + contactEmail + '\'' +
                ", contactUsername='" + contactUsername + '\'' +
                ", contactServerStatus='" + contactServerStatus + '\'' +
                '}';
    }

    public int getContactIdLocal() {
        return contactIdLocal;
    }

    public void setContactIdLocal(int contactIdLocal) {
        this.contactIdLocal = contactIdLocal;
    }

    public int getContactIdServer() {
        return contactIdServer;
    }

    public void setContactIdServer(int contactIdServer) {
        this.contactIdServer = contactIdServer;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactUsername() {
        return contactUsername;
    }

    public void setContactUsername(String contactUsername) {
        this.contactUsername = contactUsername;
    }

    public String getContactServerStatus() {
        return contactServerStatus;
    }

    public void setContactServerStatus(String contactServerStatus) {
        this.contactServerStatus = contactServerStatus;
    }
}
