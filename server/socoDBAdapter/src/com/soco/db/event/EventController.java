package com.soco.db.event;

import com.soco.db.adapter.EventDBAdapter;
import com.soco.dbconnect.dbconnect;
import com.soco.event.Event;

public class EventController {
	
	public int createEvent(Event event){
		int ret = 0;
        
        String dbAdapter = System.getProperty(EventDBAdapter.DB_ADAPTER_KEY);
        
        if (dbAdapter != null && dbAdapter.equals(EventDBAdapter.DB_ADAPTER_IN_MEMORY)){
            ret = this.createEventByInMemory(event);
        } else {
            ret = this.createEventByRDB(event);
        }
        
        return ret;
	}
	
	public boolean updateEvent(Event event){
		return true;
	}
	
	private int createEventByInMemory(Event event){
		int ret = 0;
		return ret;
	}
	
	private int createEventByRDB(Event event){
		int ret = 0;
        String sql = event.getInsertSQL();
        dbconnect dbc = new dbconnect();
        ret = dbc.exectuteUpdateSQL(sql);
        if( 0 == ret){
            //
        }
        
        return ret;	
	}

}
