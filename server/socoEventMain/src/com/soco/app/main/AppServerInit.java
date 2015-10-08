package com.soco.app.main;

import com.soco.db.user.RoleController;
import com.soco.log.Log;
import com.soco.user.Role;

public class AppServerInit {

	
	public static void initEnv(){
		//initialize Role once
		RoleController rc = new RoleController();
		for(String roleName : Role.ROLE_ARRAY){
			if(rc.has(roleName) == -1){
				Role role = new Role();
				role.setAuthority(roleName);
				boolean ret = rc.createRole(role);
				if(ret){
					Log.infor("In initEnv. Success to insert role: " + roleName);
				} else {
					Log.error("In initEnv. Failed to insert role: " + roleName);
				}
			} else {
				Log.debug("In initEnv. Role : " + roleName + " is existent.");
			}
		}
	}
}
