package com.soco.event;

import static java.lang.String.format;

import java.util.Date;

import com.soco.log.Log;
import com.soco.table.BaseTable;
import com.soco.user.User;

public class Event implements BaseTable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4233295206399287819L;
	
	private static final String tableName = "event";
	
	private String updateString;
	
	/* identify */
	private long id;
	/* parent task id, if > 0 then this is a sub task */
	private long parentId;
	/* task name */
	private String name;
	/* task description */
	private String description;
	/* location name */
	private String address;
	/* city name */
	private String city;
	/* latitude */
	private Double lat;
	/* longitude */
	private Double lon;
	/* task create time */
	private Date createTime;
	/* task start time */
	private Date startTime;
	/* task end time */
	private Date endTime;
	/*  */
	private String banner_url;
	/*  */
	private String event_url;
	/*  */
	private String status;
	/*  */
	private int number_of_views;
	/*  */
	private int number_of_likes;
	/*  */
	private int number_of_comments;
	/*  */
	private int number_of_photos;
	/* archive */
	private Boolean isArchived;
	/* deleted */
	private Boolean isDeleted;
	
	
	public Event(){
		this.updateString = "";
		this.setId(0L);
		this.setParentId(0L);
		this.setName("");
		this.setDescription("");
		this.setAddress("");
		this.setCity("");
		this.setLat(0.0);
		this.setLon(0.0);
		this.setStartTime(new Date());
		this.setEndTime(new Date());
		this.setBannerUrl("");
		this.setEventUrl("");
		this.setStatus("");
		this.setNumberOfViews(0);
		this.setNumberOfComments(0);
		this.setNumberOfLikes(0);
		this.setNumberOfPhotos(0);
		this.setIsArchived(false);
		this.setIsDeleted(false);
	}
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		if(!this.updateString.equals("")){
			this.updateString = this.updateString + ",";
		}
		this.updateString = this.updateString + "description='" + description + "'";
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
		if(!this.updateString.equals("")){
			this.updateString = this.updateString + ",";
		}
		this.updateString = this.updateString + "start_date='" + startTime + "'";
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
		if(!this.updateString.equals("")){
			this.updateString = this.updateString + ",";
		}
		this.updateString = this.updateString + "end_date='" + endTime + "'";
	}

	public Boolean getIsArchived() {
		return isArchived;
	}

	public void setIsArchived(Boolean isArchived) {
		this.isArchived = isArchived;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	public String getTableName(){
		return tableName;
	}
	
	public String getTableCreateSQL(){
		return null;
		/*
		return "create table if not exists event ( id bigint not null AUTO_INCREMENT, "
				+ "parentId bigint, "
				+ "name varchar(45), "
				+ "description varchar(200), "
				+ "createTime DATETIME, "
				+ "isArchived bool, "
				+ "isDeleted bool, "
				+ "primary key (id))" ;
				*/
	}

	public String getInsertSQL() {
		// TODO Auto-generated method stub
		String sql = format("insert into %s (eid, parent_id, name, description, address, city, lat, lon, "
				+ "start_date, end_date, banner_url, event_url, status, number_of_views, "
				+ "number_of_likes, number_of_comments, number_of_photos, is_archived, is_deleted) "
				+ "values(%s, %s, '%s','%s','%s', '%s', %s, %s, '%s','%s', '%s', '%s', '%s', %s, %s, %s, %s, %s, %s)", 
				getTableName(),
				this.getId(),
				this.getParentId(), 
				this.getName(), 
				this.getDescription(), 
				this.getAddress(),
				this.getCity(),
				this.getLat(),
				this.getLon(),
				new java.sql.Timestamp(this.getStartTime().getTime()),
				new java.sql.Timestamp(this.getEndTime().getTime()),
				this.getBannerUrl(),
				this.getEventUrl(),
				this.getStatus(),
				this.getNumberOfViews(),
				this.getNumberOfLikes(),
				this.getNumberOfComments(),
				this.getNumberOfPhotos(),
				this.getIsArchived(),
				this.getIsDeleted()
				);
		
		Log.debug("Insert event sql: " + sql);
		return sql;
	}

	@Override
	public String getUpdateSQLById() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String location) {
		this.address = location;
		if(!this.updateString.equals("")){
			this.updateString = this.updateString + ",";
		}
		this.updateString = this.updateString + "address='" + location + "'";
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
		if(!this.updateString.equals("")){
			this.updateString = this.updateString + ",";
		}
		this.updateString = this.updateString + "lat=" + lat;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
		if(!this.updateString.equals("")){
			this.updateString = this.updateString + ",";
		}
		this.updateString = this.updateString + "lon=" + lon;
	}


	public String getBannerUrl() {
		return banner_url;
	}


	public void setBannerUrl(String banner_url) {
		this.banner_url = banner_url;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public String getEventUrl() {
		return event_url;
	}


	public void setEventUrl(String event_url) {
		this.event_url = event_url;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public int getNumberOfViews() {
		return number_of_views;
	}


	public void setNumberOfViews(int number_of_views) {
		this.number_of_views = number_of_views;
	}


	public int getNumberOfLikes() {
		return number_of_likes;
	}


	public void setNumberOfLikes(int number_of_likes) {
		this.number_of_likes = number_of_likes;
	}


	public int getNumberOfComments() {
		return number_of_comments;
	}


	public void setNumberOfComments(int number_of_comments) {
		this.number_of_comments = number_of_comments;
	}


	public int getNumberOfPhotos() {
		return number_of_photos;
	}


	public void setNumberOfPhotos(int number_of_photos) {
		this.number_of_photos = number_of_photos;
	}

}
