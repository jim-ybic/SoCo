package com.soco.user;

import static java.lang.String.format;

import java.util.Date;

import com.soco.log.Log;
import com.soco.table.BaseTable;

public class User implements BaseTable {

    /**
     * 
     */
    private static final long serialVersionUID = 3405171284950529637L;
    
    private static final String tableName = "user";
    
    private String updateString = "";

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
    
    /* gender */
    private int gender;
    
    /* hometown */
    private String hometown;
    
    /* biography */
    private String biography;
    
    /* latitude */
    private Float latitude;
    
    /* longitude */
    private Float longitude;
    
    /* number of friends */
    private int numOfFriends;
    
    /* number of following */
    private int numOfFollowing;
    
    /* number of followers */
    private int numOfFollowers;
    
    /* number of like */
    private int numOfLike;
    
    /* create date written by db, here just read from db */
    private Date createDate;
    
    /* modified date time, just read from db */
    private Date modifiedDate;
    
    /* user is enabled or not */
    private Boolean isEnabled;
    
    /* account validated */
    private Boolean isValidated;
    
    /* account locked */
    private Boolean isLocked;
    
    /* user is deleted */
    private Boolean isDeleted;
    
    /* user profile photo file path */
    private String userProfilePhotoPath;

    
    public User(){
    	this.updateString = "";
        this.id = 0;
        this.userName = "";
        this.userEncryptPassword = "";
        this.userPlainPassword = "";
        this.email = "";
        this.mobilePhone = "";
        this.latitude = 0f;
        this.longitude = 0f;
        this.numOfFollowers = 0;
        this.numOfFollowing = 0;
        this.numOfFriends = 0;
        this.numOfLike = 0;
        this.isEnabled = false;
        this.isValidated = false;
        this.isLocked = false;
        this.isDeleted = false;
        this.userProfilePhotoPath = "";
    }
    
    public User(long id, 
    		    String name, 
    		    String password, 
    		    String plainPassword, 
    		    String email, 
    		    String mPhone,
    		    Float latitude,
    		    Float longitude,
    		    int followers,
    		    int following,
    		    int friends,
    		    int like,
    		    Boolean enabled,
    		    Boolean validated,
    		    Boolean locked,
    		    Boolean deleted,
    		    String photoPath){
    	this.id = id;
    	this.userName = name;
    	this.userEncryptPassword = password;
    	this.userPlainPassword = plainPassword;
    	this.email = email;
    	this.mobilePhone = mPhone;
    	this.setLatitude(latitude);
    	this.setLongitude(longitude);
    	this.numOfFollowers = followers;
    	this.numOfFollowing = following;
    	this.numOfFriends = friends;
    	this.numOfLike = like;
    	this.isEnabled = enabled;
    	this.isValidated = validated;
    	this.isLocked = locked;
    	this.isDeleted = deleted;
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
	    /*
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
				*/
	    return null;
	}
	

	@Override
	public String getInsertSQL() {
		// TODO Auto-generated method stub
		String sql = format(
		        "insert into %s " +
		        "( uid, " +
		          "name, " +
		          "email, " +
		          "mobile_phone, " +
		          "encrypt_password, " +
		          "plain_password, " +
		          "gender, " +
		          "hometown, " +
		          "biography, " +
		          "photo_path, " +
		          "latitude, " +
		          "longitude " +
		          ") values(%s,'%s','%s','%s','%s','%s',%s,'%s','%s','%s',%s,%s)", 
				getTableName(),
				this.getId(),
				this.getUserName(),
				this.getEmail(),
				this.getMobilePhone(),
				this.getUserEncryptPassword(), 
				this.getUserPlainPassword(), 
				this.getGender(),
				this.getHometown(),
				this.getBiography(),
				this.getUserProfilePhotoPath(),
				this.getLatitude(),
				this.getLongitude()
				);
		Log.debug("insert sql: " + sql);
		return sql;
	}

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public int getNumOfFollowing() {
        return numOfFollowing;
    }

    public void setNumOfFollowing(int numOfFollowing) {
        this.numOfFollowing = numOfFollowing;
    }

    public int getNumOfFriends() {
        return numOfFriends;
    }

    public void setNumOfFriends(int numOfFriends) {
        this.numOfFriends = numOfFriends;
    }

    public int getNumOfFollowers() {
        return numOfFollowers;
    }

    public void setNumOfFollowers(int numOfFollowers) {
        this.numOfFollowers = numOfFollowers;
    }

    public int getNumOfLike() {
        return numOfLike;
    }

    public void setNumOfLike(int numOfLike) {
        this.numOfLike = numOfLike;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Boolean getIsValidated() {
        return isValidated;
    }

    public void setIsValidated(Boolean isValidated) {
        this.isValidated = isValidated;
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Boolean getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

	@Override
	public String getUpdateSQLById() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getQuerySQLByUIdOrEmail(){
		String sql = format("select * from %s where uid=%s or email='%s'", this.getTableName(), this.getId(), this.getEmail());
		Log.debug("query by uid or email: " + sql);
		return sql;
	}
}
