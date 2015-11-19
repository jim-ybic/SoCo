package com.soco.SoCoClient.common.util;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

//import com.dropbox.client2.DropboxAPI;
//import com.dropbox.client2.android.AndroidAuthSession;
import com.facebook.AccessToken;
import com.facebook.GraphResponse;
import com.soco.SoCoClient.buddies.suggested.ui.BuddyCardContainer;
import com.soco.SoCoClient.buddies.suggested.ui.BuddyCardStackAdapter;
import com.soco.SoCoClient.common.database._ref.DBManagerSoco;
import com.soco.SoCoClient.common.model.Person;
import com.soco.SoCoClient.common.model.Profile;
import com.soco.SoCoClient.events.model.ui.EventCardContainer;
import com.soco.SoCoClient.events.model.ui.EventCardStackAdapter;
import com.soco.SoCoClient.events.model.Event;
import com.soco.SoCoClient.userprofile.model.User;

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
//    public String loginEmail, loginPassword;
//    public DropboxAPI<AndroidAuthSession> dropboxApi;
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

    public ArrayList<Person> loadRawPhoneContacts(Context context) {
        Log.v(tag, "load phone contacts start");

        //test - force loading each time
        listNameEmailReady = false;

        //new code
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        ArrayList<Person> persons = new ArrayList<>();
        int counter = 0;

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Log.d(tag, "name : " + name + ", ID : " + id);
                String phone = "", email = "", emailType = "";

                    // get the phone number
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        phone = pCur.getString(
                                pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Log.d(tag, "phone: " + phone);
                        break; //testing: import one phone only
                    }
                    pCur.close();

                    // get email and type
                    Cursor emailCur = cr.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (emailCur.moveToNext()) {
                        email = emailCur.getString(
                                emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        emailType = emailCur.getString(
                                emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
                        Log.d(tag, "Email " + email + " Email Type : " + emailType);
                        break; //testing: import one email only
                    }
                    emailCur.close();

//                }

                Person p = new Person(context, name, phone, email);
                persons.add(p);

                if(counter ++ > 100)
                    break;  //testing
            }
        }

        //old code
//                    ArrayList<Person> persons = new ArrayList<>();
//        if (!listNameEmailReady) {
//            Log.d(tag, "listNameEmail not ready, load for the first time");
//            listNameEmail = new ArrayList<Map<String, String>>();
//            Cursor emails = getContentResolver().query(
//                    ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, null, null, null);
//
//            int colDisplayName = emails.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
//            int colEmail = emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
//
//            while (emails.moveToNext()) {
//                String contactName = emails.getString(colDisplayName);
//                String email = emails.getString(colEmail);
//                Log.v(tag, "Get email: " + contactName + ", " + email);
//
//                Person p = new Person(context, contactName, email);
//                persons.add(p);
//            }
//            emails.close();
//            listNameEmailReady = true;
//        }
//        else
//            Log.d(tag, "listNameEmail already loaded");

        return persons;
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

    public void mappingSuggestedEventListToMap(){
        if(suggestedEventsMap==null) {
            suggestedEventsMap = new HashMap<>();
        }
        for (Event e : suggestedEvents) {
            long id = e.getId();
            suggestedEventsMap.put(id,e);
        }
    }
    public Event getCurrentSuggestedEvent(){
        if(suggestedEvents!=null && suggestedEvents.size()>0) {
            int size = suggestedEvents.size();
            int pos = size - currentEventIndex - 1;
            return suggestedEvents.get(pos);
        }
        return null;
    }
    public void mappingSuggestedBuddyListToMap(){
        if(suggestedBuddiesMap==null) {
            suggestedBuddiesMap = new HashMap<>();
        }
        for (User u : suggestedBuddies) {
            String id = u.getUser_id();
            suggestedBuddiesMap.put(id, u);
        }
    }
    public User getCurrentSuggestedBuddy(){
        if(suggestedBuddies!=null && suggestedBuddies.size()>0) {
            int size = suggestedBuddies.size();
            int pos = size - currentBuddyIndex - 1;
            return suggestedBuddies.get(pos);
        }
        return null;
    }
    /////////////////////////////////////////////////////////////////

    //environment
    public static boolean CAN_SKIP_FACEBOOK_LOGIN = true;
    public static boolean SKIP_LOGIN = false;
    public static boolean USE_SIMILATOR_REGISTER = true;
    public static boolean USE_SIMILATOR_LOGIN_NORMAL = true;
    public static boolean USE_SIMULATOR_SUGGESTED_EVENTS = false;
    public static boolean BUDDY_INTERFACE_READY = false;
    public static boolean OFFLINE_MODE = false;

    //testing conditions
    public static boolean TEST_BUDDY_TAB_FIRST = false;

    //error message
    public static String error_message;
    public static String error_code;
    public static String more_info;

    //onboarding
    public String registerEmail;
    public String registerPassword;
    public String registerName;
    public String registerPhone;
    public String registerLocation;
    public boolean registerResponse;
    public boolean registerResult;
    public String loginEmail;
    public String loginPassword;
    public boolean loginNormalResponse;
    public boolean loginNormalResult;
    public GraphResponse facebookUserinfoResponse;
    public boolean facebookUserinfoReady;
    public boolean loginViaFacebookResponse;
    public boolean loginViaFacebookResult;

    public static String user_id;
    public static String token;
//    public AccessToken facebookAccessToken;

    //create event
    public Event newEvent;
    public boolean createEventResponse;
    public boolean createEventResult;

    //suggested event
    public boolean downloadSuggestedEventsResponse;
    public boolean downloadSuggestedEventsResult;
    public ArrayList<Event> suggestedEvents;
    public EventCardStackAdapter eventCardStackAdapter;
    public EventCardContainer mEventCardContainer;
    public int eventGroupsBuddiesTabIndex = 0;
    public View suggestedEventsView;
    public HashMap<Long, Event> suggestedEventsMap;


    //suggested event
    public boolean downloadSuggestedBuddiesResponse;
    public boolean downloadSuggestedBuddiesResult;
    public ArrayList<User> suggestedBuddies;
    public BuddyCardStackAdapter buddyCardStackAdapter;
    public BuddyCardContainer mBuddyCardContainer;
    public View suggestedBuddiesView;
    public HashMap<String, User> suggestedBuddiesMap;
    //event details
    public int currentBuddyIndex;

    //event details
    public int currentEventIndex;   //increate by 1 when an event card is dismissed

    //suggested buddy
    public long currentBuddyId;
    public boolean addBuddyResponse;
    public boolean addBuddyResult;

    //like event or revert like event
    public boolean likeEventResponse;
    public boolean likeEventResult;
    public boolean revertLikeEventResponse;
    public boolean revertLikeEventResult;

    //join event
    public boolean joinEventResponse;
    public boolean joinEventResult;
    //event groups and buddies
    public boolean eventGroupsBuddiesResponse;
    public boolean eventGroupsBuddiesResult;

    //create group
    public boolean createGroupResult;
    public String newGroupId;

    public static String getCurrentUserTokenForUrl(){
        StringBuffer sb = new StringBuffer();
        sb.append("user_id=");
        sb.append(user_id);
        sb.append("&token=");
        sb.append(token);
        return sb.toString();
    }
}