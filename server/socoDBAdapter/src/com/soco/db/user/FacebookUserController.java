package com.soco.db.user;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.soco.dbconnect.dbconnect;
import com.soco.log.Log;
import com.soco.user.FacebookUser;

public class FacebookUserController {

	public boolean has(long id){
		boolean ret = false;
		
    	FacebookUser fbUser = new FacebookUser();
		fbUser.setId(id);
		dbconnect dbc = new dbconnect();
        int count = dbc.queryRows(fbUser.getQuerySQLById());
        Log.debug("query count rows: " + count);
		if( count > 0 ){
		    ret = true;
		}
		
		return ret;
	}
	
	public boolean createFBUser(FacebookUser fbUser){
		boolean ret = false;
		Log.infor("In create facebook user.");
		dbconnect dbc = new dbconnect();
        int rs = dbc.exectuteUpdateSQL(fbUser.getInsertSQL());
        if( rs > 0 ){
        	ret = true;
        }
	
		return ret;
	}
	
	public boolean updateFBUser(FacebookUser fbUser){
		boolean ret = false;
		Log.infor("In update facebook user.");
		return ret;
	}
}
