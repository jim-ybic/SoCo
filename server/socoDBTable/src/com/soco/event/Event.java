package com.soco.event;

import static java.lang.String.format;

import java.util.Date;

import com.soco.table.BaseTable;
import com.soco.user.User;

public class Event implements BaseTable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4233295206399287819L;
	
	private static final String tableName = "event";
	
	/* identify */
	private long id;
	
	/* parent task id, if > 0 then this is a sub task */
	private long parentId;
	
	/* task name */
	private String name;
	
	/* task description */
	private String description;
	
	/* task create time */
	private Date createTime;
	
	/* task start time */
	private Date startTime;
	
	/* task end time */
	private Date endTime;
	
	/* archive */
	private Boolean isArchived;
	
	/* deleted */
	private Boolean isDeleted;
	

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
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
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
		return "create table if not exists event ( id bigint not null AUTO_INCREMENT, "
				+ "parentId bigint, "
				+ "name varchar(45), "
				+ "description varchar(200), "
				+ "createTime DATETIME, "
				+ "isArchived bool, "
				+ "isDeleted bool, "
				+ "primary key (id))" ;
	}

	public String getInsertSQL() {
		// TODO Auto-generated method stub
		return format("insert into %s (parentId, name, description, createTime, isArchived, isDeleted) "
				+ "values(%s,'%s','%s',%s,%s,%s)", 
				getTableName(),
				this.getParentId(), 
				this.getName(), 
				this.getDescription(), 
				this.getCreateTime(),
				this.getIsArchived(),
				this.getIsDeleted()
				);
	}

	@Override
	public String getUpdateSQLById() {
		// TODO Auto-generated method stub
		return null;
	}

}
