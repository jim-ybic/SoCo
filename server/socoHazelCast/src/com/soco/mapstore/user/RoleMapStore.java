package com.soco.mapstore.user;

import static java.lang.String.format;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

import com.hazelcast.core.MapStore;
import com.soco.db.StatementIterable;
import com.soco.db.mysql.DataSource;
import com.soco.user.Role;

public class RoleMapStore implements MapStore<Long, Role>{
	
	private final Connection _con;
    private PreparedStatement allKeysStatement;
    
    public RoleMapStore() {
        try {
            _con = DataSource.getInstance().getConnection();
            _con.createStatement().executeUpdate( (new Role()).getTableCreateSQL() );
            allKeysStatement = _con.prepareStatement("select id from " + (new Role()).getTableName());
        } catch (SQLException | IOException | PropertyVetoException e) {
            throw new RuntimeException(e);
        }
    }

	@Override
	public Role load(Long key) {
		// TODO Auto-generated method stub
		Role role = null;
		try {
            ResultSet resultSet = _con.createStatement().executeQuery(
                    format("select * from Role where id =%s", key));
            try {
                if (resultSet.next()) {
	                String authority = resultSet.getString(2);
	                role = new Role(key, authority);
                } else {
                	//todo add log
                }
            } finally {
                resultSet.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
		return role;
	}

	@Override
	public Map<Long, Role> loadAll(Collection<Long> arg0) {
		// TODO Auto-generated method stub
		return null;
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
	public void store(Long arg0, Role arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void storeAll(Map<Long, Role> arg0) {
		// TODO Auto-generated method stub
		
	}

	
}
