package com.soco.event;

import static java.lang.String.format;

import com.soco.table.BaseTable;

public class EventAttributes implements BaseTable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 151495506676406795L;
	
	private static final String tableName = "event_attributes";
	
	/* identify */
    private long id;
    
    /* task id */
    private long eventId;
    
    /* attribute name */
	private String name;
	
	/* attribute type */
	private String type;
	
	/* attribute value */
	private String value;

	
	public String getTableName(){
		return tableName;
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

	public long getEventId() {
		return eventId;
	}

	public void setEventId(long eventId) {
		this.eventId = eventId;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getTableCreateSQL(){
		return  "create table if not exists event_attributes ( id bigint not null AUTO_INCREMENT, "
				+ "event_id bigint, "
				+ "name varchar(45), "
				+ "type varchar(45), "
				+ "value varchar(45), "
				+ "primary key (id))" ;
	}

	@Override
	public String getInsertSQL() {
		// TODO Auto-generated method stub
		return format("insert into %s (event_id, name, type, value) "
				+ "values(%s,'%s','%s','%s')", 
				getTableName(),
				this.getEventId(), 
				this.getName(), 
				this.getType(), 
				this.getValue()
				);
	}


	@Override
	public String getUpdateSQLById() {
		// TODO Auto-generated method stub
		return null;
	}


}
