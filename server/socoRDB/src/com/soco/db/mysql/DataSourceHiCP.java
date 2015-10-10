package com.soco.db.mysql;

import java.sql.Connection;
import java.sql.SQLException;

import com.soco.log.Log;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DataSourceHiCP {
	
	private static DataSourceHiCP _datasource = null;
	private HikariDataSource hcp_datasource = null; 
	
	private boolean _update = true;
	
	private String _db = "socoserver1";
    private String _port = "3306";
    private String _jdbc_url = "jdbc:mysql://socodb1.cke0xhfpla2s.ap-southeast-1.rds.amazonaws.com";
    private String _user = "soco1";
    private String _password = "soco123456";
	
    public static DataSourceHiCP getInstance(){
    	if(null == _datasource){
    		_datasource = new DataSourceHiCP();
    	} else {
    		//
    	}
    	return _datasource;
    }
    
    private DataSourceHiCP(){
    	this.setDBConfigFromSystemProperty();
        this.createDBSource();
    }
    
	private void createDBSource(String dbName, String port, String url, String userName, String password){

		HikariConfig config = new HikariConfig();

		config.setDriverClassName("com.mysql.jdbc.Driver");
		config.setJdbcUrl(url + ":" + port + "/" + dbName + "?useUnicode=true&characterEncoding=UTF-8");
		config.setUsername(userName);
		config.setPassword(password);
		
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

		hcp_datasource = new HikariDataSource(config);
		
	}
	
	private void createDBSource(){
		this.createDBSource(this._db, this._port, this._jdbc_url, this._user, this._password);
    }
	
	public Connection getConnection() {
        Connection conn = null;
		try {
			conn = this.hcp_datasource.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Log.error("DB Connect is not valide or disconnection.");
			Log.error("DB exception: " + e.getMessage());
			Log.error("Exception stack trace: " + e.getStackTrace());
			//reconnection again
			this.createDBSource(this._db, this._port, this._jdbc_url, this._user, this._password);
		}
        
        return conn;
    }
	
	public void updateDBConfiguration(String dbName, String port, String url, String userName, String password){
    	
    	this._update = true;
    	this.createDBSource(dbName, port, url, userName, password);
    }
    
    public void setDBConfigFromSystemProperty(){
    	
    	String url = System.getProperties().containsKey("DBUrl") ? System.getProperty("DBUrl") : null;
    	String dbName = System.getProperties().containsKey("DBName") ? System.getProperty("DBName") : null;
    	String dbPort = System.getProperties().containsKey("DBPort") ? System.getProperty("DBPort") : null;
    	String dbUser = System.getProperties().containsKey("DBUser") ? System.getProperty("DBUser") : null;
    	String dbPassword = System.getProperties().containsKey("DBPassword") ? System.getProperty("DBPassword") : null;
    	
    	if(null != url) this._jdbc_url = url;
    	if(null != dbName) this._db = dbName;
    	if(null != dbPort) this._port = dbPort;
    	if(null != dbUser) this._user = dbUser;
    	if(null != dbPassword) this._password = dbPassword;
    }
    
    public void updateDBConfigFromSystemProperty(){
    	this._update = true;
    	this.setDBConfigFromSystemProperty();
    	this.createDBSource();
    }
}
