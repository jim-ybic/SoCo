package com.soco.SoCoClient.v2.control.config;

import android.app.Application;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.soco.SoCoClient.obsolete.v1.control.db.DBManagerSoco;
import com.soco.SoCoClient.v2.model.Profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SocoApp extends Application {

    public static String tag = "SocoApp";

    public static String REGISTRATION_STATUS_START = "registration_start";
    public static String REGISTRATION_STATUS_SUCCESS = "registration_success";
    public static String REGISTRATION_STATUS_FAIL = "registration_fail";
    public static String UPLOAD_STATUS_START = "upload_start";
    public static String UPLOAD_STATUS_SUCCESS = "upload_success";
    public static String UPLOAD_STATUS_FAIL = "upload_fail";

    public String myState, loginStatus, registrationStatus, uploadStatus;
    public ArrayList<Map<String, String>> listNamePhone, listNameEmail;
    public boolean listNamePhoneReady = false, listNameEmailReady = false;
    public String currPicturePath;
    public ArrayList<String> sharedFileNames = new ArrayList<>();
    public int pid, pid_onserver;
    public ArrayList<HashMap<String, String>> attrMap;
    public Uri dropboxDownloadUri;
    public String dropboxDownloadType;
    public String dropboxDownloadStatus;
    public DBManagerSoco dbManagerSoco;
    public String loginEmail, loginPassword;
    public DropboxAPI<AndroidAuthSession> dropboxApi;
    public Uri uri;
    public String lat, lng, zoom, locationName;
//    public String pid_onserver;
    public String username;
    public Profile profile;
    public String currentPath;
    public ContentResolver cr;

    public void setDropboxDownloadUri(Uri uri){
        this.dropboxDownloadUri = uri;
    }

    public Uri getDropboxDownloadUri(){
        return dropboxDownloadUri;
    }

    public void setDropboxDownloadType(String type){
        this.dropboxDownloadType = type;
    }

    public String getDropboxDownloadType(){
        return dropboxDownloadType;
    }

    public ArrayList<HashMap<String, String>> getAttrMap(){
        return attrMap;
    }

    public void setAttrMap(ArrayList<HashMap<String, String>> map){
        attrMap = map;
    }

    public int getPid(){
        return pid;
    }

    public void setPid(int pid){
        this.pid = pid;
    }

    public void setSharedFileNames(ArrayList<String> names){
        sharedFileNames = new ArrayList<>();
        for (String n : names)
            sharedFileNames.add(n);
    }

    public ArrayList<String> getSharedFileNames(){
        return sharedFileNames;
    }

    public String getState(){
        return myState;
    }

    public void setState(String s){
        myState = s;
    }

    public String getUploadStatus () { return uploadStatus; }

    public void setUploadStatus(String status) { uploadStatus = status; }

    public void setLoginStatus(String s){
        loginStatus = s;
    }

    public String getLoginStatus(){
        return loginStatus;
    }

    public void setRegistrationStatus(String s){
        registrationStatus = s;
    }

    public String getRegistationStatus(){
        return registrationStatus;
    }

    public ArrayList<Map<String, String>> loadNameEmailList() {
        Log.d(tag, "loadNameEmailList: start");

        if (!listNameEmailReady) {
            Log.d(tag, "listNameEmail not ready, load for the first time");
            listNameEmail = new ArrayList<Map<String, String>>();
            Cursor emails = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, null, null, null);

            int colDisplayName = emails.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            int colEmail = emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);

            while (emails.moveToNext()) {
                String contactName = emails.getString(colDisplayName);
                String email = emails.getString(colEmail);
                Log.v(tag, "Get email: " + contactName + ", " + email);

                Map<String, String> NameEmail = new HashMap<String, String>();
                NameEmail.put("Key", contactName);
                NameEmail.put("Value", email);
                listNameEmail.add(NameEmail); //add this map to the list.
            }
            emails.close();
            listNameEmailReady = true;
        }
        else
            Log.d(tag, "listNameEmail already loaded");

        return listNameEmail;
    }

    public ArrayList<Map<String, String>> loadNamePhoneList() {
        Log.d(tag, "loadNamePhoneList");

        if (!listNamePhoneReady) {
            Log.i(tag, "listNamePhone not ready, load for the first time");
            listNamePhone = new ArrayList<Map<String, String>>();
            Cursor phones = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

            int colDisplayName = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int colPhoneNumber = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

            while (phones.moveToNext()) {
                String contactName = phones.getString(colDisplayName);
                String phoneNumber = phones.getString(colPhoneNumber);
                Log.v(tag, "Get phone: " + contactName + ", " + phoneNumber);

                Map<String, String> NamePhone = new HashMap<String, String>();
                NamePhone.put("Key", contactName);
                NamePhone.put("Value", phoneNumber);
                listNamePhone.add(NamePhone); //add this map to the list.
            }
            phones.close();
            listNamePhoneReady = true;
        }
        else
            Log.i(tag, "listNamePhone already loaded");

        return listNamePhone;
    }
}