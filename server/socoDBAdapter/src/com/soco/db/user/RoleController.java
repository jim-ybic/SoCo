package com.soco.db.user;

import com.soco.dbconnect.dbconnect;
import com.soco.log.Log;
import com.soco.user.Role;

public class RoleController {

	
	public long has(Role role){
		long rid = -1;
		Log.infor("In has role : " + role.getAuthority());
		dbconnect dbc = new dbconnect();
        rid = dbc.queryValueOfLong(role.getQueryIdSQL());

		return rid;
	}
	
	public boolean createRole(Role role){
		boolean ret = false;
		Log.infor("In create role : " + role.getAuthority());
		dbconnect dbc = new dbconnect();
        int rows = dbc.exectuteUpdateSQL(role.getInsertSQL());
        if( rows > 0 ){
        	ret = true;
        }
		return ret;
	}
}
