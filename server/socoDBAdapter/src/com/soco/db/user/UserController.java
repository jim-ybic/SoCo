package com.soco.db.user;

import com.soco.db.adapter.EventDBAdapter;
import com.soco.dbconnect.dbconnect;
import com.soco.user.User;

public class UserController {

    public boolean createUser(User user){
        boolean ret = false;
        
        String dbAdapter = System.getProperty(EventDBAdapter.DB_ADAPTER_KEY);
        
        if (dbAdapter != null && dbAdapter.equals(EventDBAdapter.DB_ADAPTER_IN_MEMORY)){
            ret = this.createUserByInMemory(user);
        } else {
            ret = this.createUserByRDB(user);
        }
        
        return ret;
    }
    
    
    private boolean createUserByRDB(User user){
        boolean ret = false;
        String sql = user.getInsertSQL();
        dbconnect dbc = new dbconnect();
        ret = dbc.exectuteSQL(sql);
        if(!ret){
            //
        }
        
        return ret;
    }
    
    private boolean createUserByInMemory(User user){
        boolean ret = false;
        return ret;
    }
}
