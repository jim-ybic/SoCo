package com.soco.db.user;

import com.soco.db.adapter.EventDBAdapter;
import com.soco.dbconnect.dbconnect;
import com.soco.log.Log;
import com.soco.user.User;

public class UserController {

	
	public User hasByUIdOrEmail(User user){
		Log.infor("In user controller");
		dbconnect dbc = new dbconnect();
        user = dbc.queryObjectOfUser(user.getQuerySQLByUIdOrEmail());
        //
		return user;
	}
	
	
    public int createUser(User user){
        int ret = 0;
        
        String dbAdapter = System.getProperty(EventDBAdapter.DB_ADAPTER_KEY);
        
        if (dbAdapter != null && dbAdapter.equals(EventDBAdapter.DB_ADAPTER_IN_MEMORY)){
            ret = this.createUserByInMemory(user);
        } else {
            ret = this.createUserByRDB(user);
        }
        
        return ret;
    }
    
    
    private int createUserByRDB(User user){
        int ret = 0;
        String sql = user.getInsertSQL();
        dbconnect dbc = new dbconnect();
        ret = dbc.exectuteUpdateSQL(sql);
        if( 0 == ret){
            //
        }
        
        return ret;
    }
    
    private int createUserByInMemory(User user){
        int ret = 0;
        return ret;
    }
}
