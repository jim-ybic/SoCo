package com.soco.SoCoClient.control;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class BackgroudUtil extends AsyncTask<Void, Void, Boolean> {

    private static BackgroudUtil mInstance= null;

    public boolean isStarted = false, isComplete = false;
    public ArrayList<Map<String, String>> mPeopleList = new ArrayList<Map<String, String>>();
    public Context context;

    public static synchronized BackgroudUtil getInstance(){
        if(null == mInstance){
            mInstance = new BackgroudUtil();
        }
        return mInstance;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        PopulatePeopleList(context);
        return null;
    }

    public void PopulatePeopleList(Context context)
    {
        Log.i("auto", "Populate people list");
        isStarted = true;

        mPeopleList = new ArrayList<Map<String, String>>();

        mPeopleList.clear();
        Log.i("auto", "Query contacts");
        Cursor people = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        Log.i("auto", "Process results");
        while (people.moveToNext()) {
            String contactName = people.getString(people.getColumnIndex(
                    ContactsContract.Contacts.DISPLAY_NAME));
            String contactId = people.getString(people.getColumnIndex(
                    ContactsContract.Contacts._ID));
            String hasPhone = people.getString(people.getColumnIndex(
                    ContactsContract.Contacts.HAS_PHONE_NUMBER));
            Log.i("auto", "Current contactName: " + contactName + ", contactId: " + contactId
                    + ", hasPhone: " + hasPhone);

            if ((Integer.parseInt(hasPhone) > 0)) {
                // You know have the number so now query it like this
                Cursor phones = context.getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,
                        null, null);
                while (phones.moveToNext()) {
                    //store numbers and display a dialog letting the user select which.
                    String phoneNumber = phones.getString(
                            phones.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String numberType = phones.getString(phones.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.TYPE));
                    Log.i("auto", "Load phoneNumber: " + phoneNumber + ", numberType: " + numberType);

                    Map<String, String> NamePhoneType = new HashMap<String, String>();
                    NamePhoneType.put("Name", contactName);
                    NamePhoneType.put("Phone", phoneNumber);

                    if(numberType.equals("0"))
                        NamePhoneType.put("Type", "Work");
                    else if(numberType.equals("1"))
                        NamePhoneType.put("Type", "Home");
                    else if(numberType.equals("2"))
                        NamePhoneType.put("Type",  "Mobile");
                    else
                        NamePhoneType.put("Type", "Other");

                    //Then add this map to the list.
                    mPeopleList.add(NamePhoneType);
                }
                phones.close();
            }
        }
        people.close();
//        startManagingCursor(people);

        Log.i("auto", "Populate people list complete");
        isComplete = true;
        return;
    }


}
