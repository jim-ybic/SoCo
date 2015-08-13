package com.soco.SoCoClient.v2.model;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.soco.SoCoClient.v2.control.config.DataConfig;
import com.soco.SoCoClient.v2.control.database.DbHelper;

import java.sql.SQLClientInfoException;

public class Event {

    String tag = "Event";

    //fields saved to db
    int id;
    String name;
    boolean isDraft;
    boolean isDone;

    //local variables
    Context context;
    SQLiteDatabase db;

    public Event(Context c){
        Log.v(tag, "create new event");

        this.context = c;

        this.id = DataConfig.ENTITIY_ID_NOT_READY;
        this.isDraft = true;
        this.isDone = false;

        DbHelper helper = new DbHelper(c);
        this.db = helper.getWritableDatabase();
    }
}
