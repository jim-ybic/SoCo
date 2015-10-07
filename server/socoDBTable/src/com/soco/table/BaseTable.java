package com.soco.table;

import java.io.Serializable;

public interface BaseTable extends Serializable {

	/**
	 * 
	 */

	public String getTableCreateSQL();
	
	public String getTableName();
	
	public String getInsertSQL();
	
	public String getUpdateSQLById();
	
}
