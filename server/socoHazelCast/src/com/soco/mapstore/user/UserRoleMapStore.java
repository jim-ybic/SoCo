package com.soco.mapstore.user;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

import com.hazelcast.core.MapStore;
import com.soco.db.mysql.DataSource;
import com.soco.user.UserRole;

public class UserRoleMapStore implements MapStore<Long, UserRole>{

	private final Connection _con;
    private PreparedStatement allKeysStatement;
    
    public UserRoleMapStore() {
        try {
        	UserRole ur = new UserRole();
            _con = DataSource.getInstance().getConnection();
            _con.createStatement().executeUpdate( ur.getTableCreateSQL() );
            allKeysStatement = _con.prepareStatement("select id from " + ur.getTableName());
        } catch (SQLException | IOException | PropertyVetoException e) {
            throw new RuntimeException(e);
        }
    }
	
	@Override
	public UserRole load(Long arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Long, UserRole> loadAll(Collection<Long> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<Long> loadAllKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Long arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll(Collection<Long> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void store(Long arg0, UserRole arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void storeAll(Map<Long, UserRole> arg0) {
		// TODO Auto-generated method stub
		
	}

}
