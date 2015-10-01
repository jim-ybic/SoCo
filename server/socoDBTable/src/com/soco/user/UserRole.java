package com.soco.user;

import com.soco.table.BaseTable;

public class UserRole implements BaseTable {

    /**
     * 
     */
    private static final long serialVersionUID = -4399987162887524801L;
    
    private static final String tableName = "user_role";

    /* identify */
    private long id;
    
    /* user id */
    private long userID;
    
    /* role id */
    private long roleID;

	public long getUserID() {
		return userID;
	}

	public void setUserID(long userID) {
		this.userID = userID;
	}

	public long getRoleID() {
		return roleID;
	}

	public void setRoleID(long roleID) {
		this.roleID = roleID;
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
		return  "create table if not exists user_role ( id bigint not null, "
				+ "user_id bigint, "
				+ "role_id bigint, "
				+ "primary key (id))" ;
	}

	public String getInsertSQL() {
		// TODO Auto-generated method stub
		return null;
	}
    
}
