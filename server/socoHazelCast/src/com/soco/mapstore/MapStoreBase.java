package com.soco.mapstore;

import static java.lang.String.format;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.hazelcast.core.MapStore;
import com.soco.db.StatementIterable;
import com.soco.table.BaseTable;

public class MapStoreBase <T extends BaseTable> implements MapStore<Long, T>{

	private Connection _con;
    private PreparedStatement allKeysStatement;

	@Override
	public Iterable<Long> loadAllKeys() {
		// TODO Auto-generated method stub
		return new StatementIterable<Long>(allKeysStatement);
	}

	@Override
	public void delete(Long key) {
		// TODO Auto-generated method stub
		/*
		System.out.println("Delete:" + key);
        try {
            _con.createStatement().executeUpdate(
                    format("delete from %s where id = %s",(new T()).getTableName(), key));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        */
	}

	@Override
	public void deleteAll(Collection<Long> keys) {
		// TODO Auto-generated method stub
		for (Long key : keys) delete(key);
	}


	@Override
	public T load(Long arg0) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Map<Long, T> loadAll(Collection<Long> keys) {
		// TODO Auto-generated method stub
		Map<Long, T> result = new HashMap<Long, T>();
        for (Long key : keys) result.put(key, load(key));
        return result;
	}


	@Override
	public void store(Long key, T value) {
		// TODO Auto-generated method stub
		try {
            _con.createStatement().executeUpdate(value.getInsertSQL());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
	}


	@Override
	public void storeAll(Map<Long, T> map) {
		// TODO Auto-generated method stub
		for (Map.Entry<Long, T> entry : map.entrySet())
            store(entry.getKey(), entry.getValue());
	}


}
