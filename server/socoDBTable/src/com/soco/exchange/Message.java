package com.soco.exchange;

import com.soco.table.BaseTable;

public class Message implements BaseTable {

    /**
     * 
     */
    private static final long serialVersionUID = -4054039638591513583L;
    
    /* identify */
    private long id;
    
    /* to type */
    private Integer toType;
    
    /* to id */
    private String toID;
    
    /* from type */
    private Integer fromType;
    
    /* from id */
    private String fromID;
    
    /* content type */
    private Integer contentType;
    
    /* content vector */
    private String content;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Integer getToType() {
		return toType;
	}

	public void setToType(Integer toType) {
		this.toType = toType;
	}

	public String getToID() {
		return toID;
	}

	public void setToID(String toID) {
		this.toID = toID;
	}

	public Integer getFromType() {
		return fromType;
	}

	public void setFromType(Integer fromType) {
		this.fromType = fromType;
	}

	public String getFromID() {
		return fromID;
	}

	public void setFromID(String fromID) {
		this.fromID = fromID;
	}

	public Integer getContentType() {
		return contentType;
	}

	public void setContentType(Integer contentType) {
		this.contentType = contentType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getTableCreateSQL(){
		return null;
	}

	@Override
	public String getInsertSQL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUpdateSQLById() {
		// TODO Auto-generated method stub
		return null;
	}

}
