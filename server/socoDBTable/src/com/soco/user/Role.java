package com.soco.user;

import static java.lang.String.format;

import com.soco.log.Log;
import com.soco.table.BaseTable;

public class Role implements BaseTable {

    /**
     * 
     */
	public static final String ROLE_USER = "ROLE_USER";
	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	public static final String[] ROLE_ARRAY = { ROLE_USER, ROLE_ADMIN };
	
    private static final long serialVersionUID = -8088572321839120757L;
    
    private static final String tableName = "role";
    
    
    /* identify */
    private long id;
    
    /* role name */
    private String authority;
    
    public Role() {};
    
    public Role(long id, String authority){
    	this.id = id;
    	this.authority = authority;
    }

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getTableName(){
		return tableName;
	}
	
	public String getTableCreateSQL(){
		return  "create table if not exists role ( id bigint not null, "
				+ "authority varchar(45), "
				+ "primary key (id))" ;
	}

	@Override
	public String getInsertSQL() {
		// TODO Auto-generated method stub
		String sql = format(
		        "insert into %s " +
		        "( name " +
		          ") values('%s')", 
				getTableName(),
				this.getAuthority()
				);
		Log.debug("insert sql: " + sql);
		return sql;
	}
	
	public String getQueryIdSQL(){
		String sql = format("select id from %s where name='%s'", this.getTableName(), this.getAuthority());
		Log.debug("query id by : " + sql);
		return sql;
	}


	@Override
	public String getUpdateSQLById() {
		// TODO Auto-generated method stub
		return null;
	}

}
