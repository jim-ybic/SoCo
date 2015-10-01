package com.soco.user;

import static java.lang.String.format;

import java.util.Date;

import com.soco.table.BaseTable;

public class User implements BaseTable {

    /**
     * 
     */
    private static final long serialVersionUID = 3405171284950529637L;
    
    private static final String tableName = "user";

    /* identify */
    private long id;
    
    /* user name should be unique */
    private String userName;
    
    /* user encrypt password */
    private String userEncryptPassword;
    
    /* user plain password */
    private String userPlainPassword;
    
    /* user email is unique */
    private String email;
    
    /* mobile phone */
    private String mobilePhone;
    
    /* create date */
    private Date createDate;
    
    /* user is enabled or not */
    private Boolean isUserEnabled;
    
    /* account expired */
    private Boolean accountExpired;
    
    /* account locked */
    private Boolean accountLocked;
	
	/* password expired by every 3 month? */
    private Boolean passwordExpired;
    
    /* user profile photo file path */
    private String userProfilePhotoPath;

    
    public User(){
    	
    }
    
    public User(long id, 
    		    String name, 
    		    String password, 
    		    String plainPassword, 
    		    String email, 
    		    String mPhone, 
    		    Date createDate,
    		    Boolean enabled,
    		    Boolean expired,
    		    Boolean locked,
    		    Boolean passwordExpired,
    		    String photoPath){
    	this.id = id;
    	this.userName = name;
    	this.userEncryptPassword = password;
    	this.userPlainPassword = plainPassword;
    	this.email = email;
    	this.mobilePhone = mPhone;
    	this.createDate = createDate;
    	this.isUserEnabled = enabled;
    	this.accountExpired = expired;
    	this.accountLocked = locked;
    	this.passwordExpired = passwordExpired;
    	this.userProfilePhotoPath = photoPath;
    }

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserEncryptPassword() {
		return userEncryptPassword;
	}

	public void setUserEncryptPassword(String userEncryptPassword) {
		this.userEncryptPassword = userEncryptPassword;
	}

	public String getUserPlainPassword() {
		return userPlainPassword;
	}

	public void setUserPlainPassword(String userPlainPassword) {
		this.userPlainPassword = userPlainPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Boolean getIsUserEnabled() {
		return isUserEnabled;
	}

	public void setIsUserEnabled(Boolean isUserEnabled) {
		this.isUserEnabled = isUserEnabled;
	}

	public Boolean getAccountExpired() {
		return accountExpired;
	}

	public void setAccountExpired(Boolean accountExpired) {
		this.accountExpired = accountExpired;
	}

	public Boolean getAccountLocked() {
		return accountLocked;
	}

	public void setAccountLocked(Boolean accountLocked) {
		this.accountLocked = accountLocked;
	}

	public Boolean getPasswordExpired() {
		return passwordExpired;
	}

	public void setPasswordExpired(Boolean passwordExpired) {
		this.passwordExpired = passwordExpired;
	}

	public String getUserProfilePhotoPath() {
		return userProfilePhotoPath;
	}

	public void setUserProfilePhotoPath(String userProfilePhotoPath) {
		this.userProfilePhotoPath = userProfilePhotoPath;
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
	
	public String getTableCreateSQL() {
		// TODO Auto-generated method stub
		return "create table if not exists user ( id bigint not null AUTO_INCREMENT, "
				+ "name varchar(45), "
				+ "password varchar(45), "
				+ "plain_password varchar(45), "
				+ "email varchar(45), "
				+ "mobile_phone varchar(45), "
				+ "create_date DATETIME, "
				+ "enabled bool, "
				+ "expired bool, "
				+ "locked bool, "
				+ "password_expired bool, "
				+ "photo_path varchar(45), "
				+ "primary key (id))" ;
	}
	

	@Override
	public String getInsertSQL() {
		// TODO Auto-generated method stub
		return format("insert into %s (name, password, plain_password, email) values('%s','%s',%s,'%s')", 
				getTableName(),
				this.getUserName(), 
				this.getUserEncryptPassword(), 
				this.getUserPlainPassword(), 
				this.getEmail()
				);
	}
}
