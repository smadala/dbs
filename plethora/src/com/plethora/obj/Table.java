package com.plethora.obj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Table {
	
	private String tableName;
	private Map<String,FieldType> fields;
	private List<PageEntry> pageEntries;
	public Table(String name) {
		this.tableName=name;
		this.fields=new HashMap<String,FieldType>();
		this.pageEntries=new ArrayList<PageEntry>();
	}
	
	public Map<String, FieldType> getFields() {
		return fields;
	}
	public void setFields(Map<String, FieldType> fields) {
		this.fields = fields;
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
	
}
