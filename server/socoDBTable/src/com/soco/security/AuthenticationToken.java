package com.soco.security;

import static java.lang.String.format;

import java.util.Date;

import com.soco.log.Log;
import com.soco.table.BaseTable;

public class AuthenticationToken  implements BaseTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8923986680230042476L;
	
	private long uid;
	private String token ;
	private String key;
    private Date start_time;
    private long validity;
    
    private static final String tableName = "authentication_token";
    private String updateString = "";

	@Override
	public String getTableCreateSQL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return this.tableName;
	}

	@Override
	public String getInsertSQL() {
		// TODO Auto-generated method stub
		String sql = format(
		        "insert into %s " +
		        "( uid, " +
		          "token, " +
		          "token_key, " +
		          "start_time, " +
		          "validity_millionsecond " +
		          ") values(%s,'%s','%s','%s',%s)", 
				getTableName(),
				this.getUId(),
				this.getToken(),
				this.getKey(),
				new java.sql.Timestamp( this.getStartTime().getTime()),
				this.getValidity()
				);
		Log.debug("insert sql: " + sql);
		return sql;
	}

	@Override
	public String getUpdateSQLById() {
		// TODO Auto-generated method stub
		String sql = format(
		        "update %s set " + this.updateString + " where uid=%s", 
				getTableName(),
				this.getUId()
				);
		Log.debug("update sql: " + sql);
		return sql;
	}
	
	public String getQuerySQLByUId(){
		String sql = format("select * from %s where uid=%s", this.getTableName(), this.getUId());
		Log.debug("query by uid : " + sql);
		return sql;
	}
	
	public String getDeleteSQLByUId(){
		String sql = format("delete from %s where uid=%s", this.getTableName(), this.getUId());
		Log.debug("delete by uid : " + sql);
		return sql;
	}

	public long getUId() {
		return uid;
	}

	public void setUId(long uid) {
		this.uid = uid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
		if(!this.updateString.equals("")){
			this.updateString = this.updateString + ",";
		}
		this.updateString = this.updateString + "token='" + token + "'";
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
		if(!this.updateString.equals("")){
			this.updateString = this.updateString + ",";
		}
		this.updateString = this.updateString + "token_key='" + key + "'";
	}

	public Date getStartTime() {
		return start_time;
	}

	public void setStartTime(Date start_time) {
		this.start_time = start_time;
		if(!this.updateString.equals("")){
			this.updateString = this.updateString + ",";
		}
		this.updateString = this.updateString + "start_time='" + (new java.sql.Timestamp(start_time.getTime())) + "'";
	}

	public long getValidity() {
		return validity;
	}

	public void setValidity(long validity) {
		this.validity = validity;
		if(!this.updateString.equals("")){
			this.updateString = this.updateString + ",";
		}
		this.updateString = this.updateString + "validity_millionsecond=" + validity + "";
	}

	public static String getTablename() {
		return tableName;
	}

}
