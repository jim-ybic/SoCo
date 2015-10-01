package com.soco.user;

import com.soco.table.BaseTable;

public class Role implements BaseTable {

    /**
     * 
     */
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
		return null;
	}

}
