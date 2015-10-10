package com.soco.db.user;

import com.soco.dbconnect.dbconnect;
import com.soco.log.Log;
import com.soco.user.FacebookUser;

public class FacebookUserController {

	public FacebookUser hasById(long id){
		
    	FacebookUser fbUser = new FacebookUser();
		fbUser.setId(id);
		dbconnect dbc = new dbconnect();
		fbUser = dbc.queryObjectOfFacebookUser(fbUser.getQuerySQLById());
		if(null != fbUser){
			Log.debug("query facebook user id: " + id);
		} else {
			Log.error("Can't query face book user id: " + id);
		}
		
		return fbUser;
	}
	
	public long getUId(long id){
		long uid = 0L;
		FacebookUser fbUser = new FacebookUser();
		fbUser.setId(id);
		dbconnect dbc = new dbconnect();
        uid = dbc.queryValueOfLong(fbUser.getQueryUIdSQLById());
        Log.debug("query user id: " + uid);
		
		  
		return uid;
	}
	
	public boolean createFBUser(FacebookUser fbUser){
		boolean ret = false;
		Log.infor("In create facebook user.");
		dbconnect dbc = new dbconnect();
        int rows = dbc.exectuteUpdateSQL(fbUser.getInsertSQL());
        if( rows > 0 ){
        	ret = true;
        }
	
		return ret;
	}
	
	public boolean updateFBUser(FacebookUser fbUser){
		boolean ret = false;
		Log.infor("In update facebook user.");
		dbconnect dbc = new dbconnect();
		int rows = dbc.exectuteUpdateSQL(fbUser.getUpdateSQLById());
		if( rows > 0 ){
        	ret = true;
        }
		return ret;
	}
}
