package com.soco.dbconnect;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.soco.db.mysql.DataSource;
import com.soco.db.mysql.DataSourceHiCP;
import com.soco.log.Log;
import com.soco.security.AuthenticationToken;
import com.soco.user.FacebookUser;
import com.soco.user.User;

public class dbconnect {
	
	private ResultSet resultSet = null;
	private int updateCount;
	
    /** Start test
     * @param args none expected.
     */
    public static void main(String[] args) {
        dbconnect con = new dbconnect();
        //con.checkAndCreateTable();
    }
    
    private Connection getConnection() throws IOException, SQLException, PropertyVetoException{
    	Connection conn = null;
		//conn = DataSource.getInstance().getConnection();
		conn = DataSourceHiCP.getInstance().getConnection();
		
    	return conn;
    }
    
    /*
     * executeUpdateSQL for insert, update and delete
     * 
     * Returns:
     * either (1) the row count for SQL Data Manipulation Language (DML) statements 
     * or (2) 0 for SQL statements that return nothing
     * */
    public int exectuteUpdateSQL(String sql){
        Connection connection = null;
        Statement statement = null;
        int ret = 0;
 
        try {
            connection = getConnection();
            statement = connection.createStatement();
            ret = statement.executeUpdate(sql);
            
            this.setResultSet(statement.getResultSet());
            this.setUpdateCount(statement.getUpdateCount());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (statement != null) 
                try { 
                    statement.close(); 
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            if (connection != null) 
                try { 
                    connection.close(); 
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return ret;
    }
    
    /*
     * 
     * */
    public int queryRows(String sql){
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        int count = 0;
 
        try {
            connection = getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);
            while(rs.next()){
            	count++;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (statement != null) 
                try { 
                    statement.close(); 
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            if (connection != null) 
                try { 
                    connection.close(); 
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return count;
    }
    
    /*
     * 
     * */
    public long queryValueOfLong(String sql){
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        long value = 0L;
 
        try {
            connection = getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);
            while(rs.next()){
            	///// columnIndex - the first column is 1, the second is 2, ...
            	value = rs.getLong(1);
            	break;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (statement != null) 
                try { 
                    statement.close(); 
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            if (connection != null) 
                try { 
                    connection.close(); 
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return value;
    }
    
    /*
     * 
     * */
    public User queryObjectOfUser(String sql){
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        User user = null;
 
        try {
            connection = getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);
            while(rs.next()){
            	user = new User();
            	///// columnIndex - the first column is 1, the second is 2, ...
            	user.setId(rs.getLong("uid"));
            	user.setEmail(rs.getString("email"));
            	user.setUserName(rs.getString("name"));
            	user.setMobilePhone(rs.getString("mobile_phone"));
            	user.setUserEncryptPassword(rs.getString("encrypt_password"));
            	user.setUserPlainPassword(rs.getString("plain_password"));
            	user.setGender(rs.getInt("gender"));
            	user.setLatitude(rs.getFloat("latitude"));
            	user.setLongitude(rs.getFloat("longitude"));
            	user.setHometown(rs.getString("hometown"));
            	user.setBiography(rs.getString("biography"));
            	user.setUserProfilePhotoPath(rs.getString("photo_path"));
            	user.setNumOfFriends(rs.getInt("num_of_friends"));
            	user.setNumOfFollowing(rs.getInt("num_of_following"));
            	user.setNumOfFollowers(rs.getInt("num_of_followers"));
            	user.setNumOfLike(rs.getInt("num_of_like"));
            	user.setCreateDate(rs.getDate("create_date"));
            	user.setModifiedDate(rs.getDate("modify_date"));
            	user.setIsEnabled(rs.getBoolean("is_enabled"));
            	user.setIsValidated(rs.getBoolean("is_validated"));
            	user.setIsDeleted(rs.getBoolean("is_deleted"));
            	break;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (statement != null) 
                try { 
                    statement.close(); 
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            if (connection != null) 
                try { 
                    connection.close(); 
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return user;
    }
    
    /*
     * 
     * */
    public FacebookUser queryObjectOfFacebookUser(String sql){
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        FacebookUser user = null;
 
        try {
            connection = getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);
            while(rs.next()){
            	user = new FacebookUser();
            	///// columnIndex - the first column is 1, the second is 2, ...
            	user.setUid(rs.getLong("uid"));
            	user.setEmail(rs.getString("email"));
            	user.setId(rs.getLong("fb_id"));
            	user.setName(rs.getString("name"));
            	user.setFirstName(rs.getString("first_name"));
            	user.setLastName(rs.getString("last_name"));
            	user.setAgeRange(rs.getString("age_range"));
            	user.setLink(rs.getString("link"));
            	user.setGender(rs.getInt("gender"));
            	user.setLocale(rs.getString("locale"));
            	user.setTimezone(rs.getDouble("timezone"));
            	user.setUpdatedTime(rs.getDate("updated_time"));
            	user.setVerified(rs.getBoolean("verified"));
            	break;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (statement != null) 
                try { 
                    statement.close(); 
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            if (connection != null) 
                try { 
                    connection.close(); 
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return user;
    }
    
    /*
     * 
     * */
    public AuthenticationToken queryObjectOfAuthenticationToken(String sql){
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        AuthenticationToken auToken = null;
 
        try {
            connection = getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);
            while(rs.next()){
            	auToken = new AuthenticationToken();
            	///// columnIndex - the first column is 1, the second is 2, ...
            	auToken.setUId(rs.getLong("uid"));
            	auToken.setToken(rs.getString("token"));
            	auToken.setKey(rs.getString("token_key"));
            	auToken.setStartTime(rs.getDate("start_time"));
            	auToken.setValidity(rs.getLong("validity_millionsecond"));
            	
            	break;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (statement != null) 
                try { 
                    statement.close(); 
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            if (connection != null) 
                try { 
                    connection.close(); 
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return auToken;
    }
    
    /*
     * executeSQL
     * */
    public boolean exectuteSQL(String sql){
        Connection connection = null;
        Statement statement = null;
        boolean ret = false;
 
        try {
            connection = getConnection();
            statement = connection.createStatement();
            ret = statement.execute(sql);
            statement.executeUpdate(sql);
            
            this.setResultSet(statement.getResultSet());
            this.setUpdateCount(statement.getUpdateCount());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (statement != null) 
                try { 
                    statement.close(); 
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            if (connection != null) 
                try { 
                    connection.close(); 
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return ret;
    }
    
    
    /*
     * check and create the heartbeat table
     * */
    public void checkAndCreateTable(String tableName, String createSQL){
        //String table_name = "heartbeat";
        String query_table_sql = "show tables;";
        String create_table_sql = createSQL;
                /*"create table heartbeat(id bigint not null, " +
                            		"user_id bigint, " +
                            		"user_address varchar(20), " +
                            		"update_time varchar(20));";
                            		*/
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query_table_sql);
            boolean is_exist = false;
            while(resultSet.next()){
                String table_name = resultSet.getString(1);
                Log.infor("table:"+tableName);
                if(tableName.equals(table_name)){
                    is_exist = true;
                    break;
                }
            }
            if(!is_exist){
                this.exectuteSQL(create_table_sql);
                Log.infor("Create table:"+tableName+" successfully.");
            }else{
                Log.infor("Create table:"+tableName+" existent.");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            if(resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

	public ResultSet getResultSet() {
		return resultSet;
	}

	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	public int getUpdateCount() {
		return updateCount;
	}

	public void setUpdateCount(int updateCount) {
		this.updateCount = updateCount;
	}

}
