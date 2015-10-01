package com.soco.user;

import static java.lang.String.format;

import com.soco.table.BaseTable;

public class UserAttributes implements BaseTable {

    /**
     * 
     */
    private static final long serialVersionUID = -6294371642969331188L;
    
    private static final String tableName = "user_attributes";

    /* identify */
    private long id;
    
    /* user id */
    private long userID;
    
    /* attribute name */
	private String name;
	
	/* attribute type */
	private String type;
	
	/* attribute value */
	private String value;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserID() {
		return userID;
	}

	public void setUserID(long userID) {
		this.userID = userID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String getTableName(){
		return tableName;
	}
	
	public String getTableCreateSQL(){
		return  "create table if not exists user_attributes ( id bigint not null, "
				+ "user_id bigint, "
				+ "name varchar(40), "
				+ "type varchar(40), "
				+ "value varchar(120), "
				+ "primary key (id))" ;
	}

	@Override
	public String getInsertSQL() {
		// TODO Auto-generated method stub
		return format("insert into %s (user_id, name, type, value) "
				+ "values(%s,'%s','%s','%s')", 
				getTableName(),
				this.getUserID(), 
				this.getName(), 
				this.getType(), 
				this.getValue()
				);
	}
}
