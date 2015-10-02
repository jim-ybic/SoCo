package com.soco.db.adapter;

import com.soco.log.Log;

public class EventDBAdapter {
    
    public static final String DB_ADAPTER_KEY = "db";
    public static final String DB_ADAPTER_RDB = "mysql";
    public static final String DB_ADAPTER_IN_MEMORY = "hazelcast";

	public static void main(String[] args){
		Log.infor("In Event DB Adapter.");
	}
	
}
