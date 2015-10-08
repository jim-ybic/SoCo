package com.soco.db.user;

import com.soco.dbconnect.dbconnect;
import com.soco.log.Log;
import com.soco.user.UserRole;

public class UserRoleController {

	public boolean has(UserRole ur){
		boolean ret = false;
		return ret;
	}
	
	public boolean createUserRole(UserRole userRole){
		boolean ret = false;
		Log.infor("In create user id : role id . " + userRole.getUserID() + " : " + userRole.getRoleID());
		dbconnect dbc = new dbconnect();
        int rows = dbc.exectuteUpdateSQL(userRole.getInsertSQL());
        if( rows > 0 ){
        	ret = true;
        }
		return ret;
	}
	
}
