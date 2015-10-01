package com.soco.mapstore.event;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

import com.hazelcast.core.MapStore;
import com.soco.db.StatementIterable;
import com.soco.db.mysql.DataSource;
import com.soco.event.Event;

public class EventMapStore implements MapStore<Long, Event>{

	private final Connection _con;
    private PreparedStatement allKeysStatement;
    
    public EventMapStore() {
        try {
        	Event event = new Event();
            _con = DataSource.getInstance().getConnection();
            _con.createStatement().executeUpdate( event.getTableCreateSQL() );
            allKeysStatement = _con.prepareStatement("select id from " + event.getTableName());
        } catch (SQLException | IOException | PropertyVetoException e) {
            throw new RuntimeException(e);
        }
    }
    
    
	@Override
	public Event load(Long arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Long, Event> loadAll(Collection<Long> arg0) {
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
	public void store(Long arg0, Event arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void storeAll(Map<Long, Event> arg0) {
		// TODO Auto-generated method stub
		
	}

}
