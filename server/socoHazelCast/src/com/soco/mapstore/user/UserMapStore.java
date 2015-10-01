package com.soco.mapstore.user;

import static java.lang.String.format;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.hazelcast.core.MapStore;
import com.soco.db.StatementIterable;
import com.soco.db.mysql.DataSource;
import com.soco.user.User;


public class UserMapStore implements MapStore<Long, User>{

	private final Connection _con;
    private PreparedStatement allKeysStatement;
    
    public UserMapStore() {
        try {
        	User user = new User();
            _con = DataSource.getInstance().getConnection();
            _con.createStatement().executeUpdate(  user.getTableCreateSQL() );
            allKeysStatement = _con.prepareStatement("select id from " + user.getTableName());
        } catch (SQLException | IOException | PropertyVetoException e) {
            throw new RuntimeException(e);
        }
    }
    
    
	@Override
	public User load(Long key) {
		// TODO Auto-generated method stub
		User user = null;
		try {
            ResultSet resultSet = _con.createStatement().executeQuery(
                    format("select * from Role where id =%s", key));
            try {
                if (resultSet.next()) {
	                String name = resultSet.getString(2);
	                String password = resultSet.getString(3);
	                String plainPassword = resultSet.getString(4);
	                String email = resultSet.getString(5);
	                String mPhone = resultSet.getString(6);
	                Date createDate = resultSet.getDate(7);
	                Boolean enabled = resultSet.getBoolean(8);
	                Boolean expired = resultSet.getBoolean(9);
	                Boolean locked = resultSet.getBoolean(10);
	                Boolean passwordExpired = resultSet.getBoolean(11);
	                String photoPath = resultSet.getString(12);
	                user = new User(key, name, password, plainPassword, email, mPhone, createDate, enabled, expired, locked, passwordExpired, photoPath);
                } else {
                	//todo add log
                }
            } finally {
                resultSet.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
		return user;
	}

	@Override
	public Map<Long, User> loadAll(Collection<Long> keys) {
		// TODO Auto-generated method stub
		Map<Long, User> result = new HashMap<Long, User>();
        for (Long key : keys) result.put(key, load(key));
        return result;
	}

	@Override
	public Iterable<Long> loadAllKeys() {
		// TODO Auto-generated method stub
		return new StatementIterable<Long>(allKeysStatement);
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
	public void store(Long arg0, User arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void storeAll(Map<Long, User> arg0) {
		// TODO Auto-generated method stub
		
	}

}
