package com.plethora.obj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Table {
	
	private String tableName;
	private Map<String,FieldType> fields;
	private List<PageEntry> pageEntries;
	public Table(String name) {
		this.tableName=name;
		this.fields=new HashMap<String,FieldType>();
		this.pageEntries=new ArrayList<PageEntry>();
	}
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public List<PageEntry> getPageEntries() {
		return pageEntries;
	}
	public void setPageEntries(List<PageEntry> pageEntries) {
		this.pageEntries = pageEntries;
	}
	
	public Map<String, FieldType> getFields() {
		return fields;
	}
	public void setFields(Map<String, FieldType> fields) {
		this.fields = fields;
	}
	public static class FieldType{
		private DataType name;
		private int size;
		public DataType getName() {
			return name;
		}
		public void setName(DataType name) {
			this.name = name;
		}
		public int getSize() {
			return size;
		}
		public void setSize(int size) {
			this.size = size;
		}
		
	}
	//hello
	public  enum DataType{
		INT, FLOAT,	STRING,	FIXED_CHAR;
	}
}
