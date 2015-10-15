package com.soco.db.security;

import com.soco.dbconnect.dbconnect;
import com.soco.log.Log;
import com.soco.security.AuthenticationToken;

public class AuthenticationTokenController {

	
	public AuthenticationToken hasTokenByUId(AuthenticationToken auToken){
		Log.infor("In AuthenticationToken controller.Query token for user id: " + auToken.getUId());
		dbconnect dbc = new dbconnect();
		auToken = dbc.queryObjectOfAuthenticationToken(auToken.getQuerySQLByUId());
		if(null != auToken){
			Log.debug("Get token: " + auToken.getToken());
		} else {
			Log.error("Token is not existent.");
		}
		return auToken;
	}
	
	
	public boolean createAuthenticationToken(AuthenticationToken auToken){
		boolean ret = false;
		Log.infor("In create Authentication Token.");
		dbconnect dbc = new dbconnect();
        int rows = dbc.exectuteUpdateSQL(auToken.getInsertSQL());
        if( rows > 0 ){
        	ret = true;
        }
		return ret;
	}
	
	public boolean updateAuthenticationToken(AuthenticationToken auToken){
		boolean ret = false;
		Log.infor("In update Authentication Token.");
		dbconnect dbc = new dbconnect();
        int rows = dbc.exectuteUpdateSQL(auToken.getUpdateSQLById());
        if( rows > 0 ){
        	ret = true;
        }
		return ret;
	}
	
	public boolean deleteAuthenticationToken(AuthenticationToken auToken){
		boolean ret = false;
		Log.infor("In delete Authentication Token.");
		dbconnect dbc = new dbconnect();
        int rows = dbc.exectuteUpdateSQL(auToken.getDeleteSQLByUId());
        if( rows > 0 ){
        	ret = true;
        }
		return ret;
	}
}
