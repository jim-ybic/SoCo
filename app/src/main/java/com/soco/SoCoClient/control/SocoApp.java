package com.soco.SoCoClient.control;

import android.app.Application;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SocoApp extends Application {

    public static String tag = "SocoApp";

    public static String REGISTRATION_STATUS_START = "registration_start";
    public static String REGISTRATION_STATUS_SUCCESS = "registration_success";
    public static String REGISTRATION_STATUS_FAIL = "registration_fail";

    public String myState, loginStatus, registrationStatus;
    public ArrayList<Map<String, String>> listNamePhone, listNameEmail;
    public boolean listNamePhoneReady = false, listNameEmailReady = false;
    public String currPicturePath;

    public String getState(){
        return myState;
    }

    public void setState(String s){
        myState = s;
    }

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