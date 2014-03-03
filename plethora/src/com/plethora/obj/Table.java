package com.plethora.obj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Table {
	
	private String tableName;
	private Map<String,FieldType> fields;
	private List<PageEntry> pageEntries;
	
	private Map<String,Integer> fieldPos; 
	public Table(String name) {
		this.tableName=name;
		this.fields=new LinkedHashMap<String,FieldType>();
		
		this.pageEntries=new ArrayList<PageEntry>();
	}
	
	public Map<String, FieldType> getFields() {
		return fields;
		
	}
	public void setFields(Map<String, FieldType> fields) {
		fieldPos=new HashMap<String, Integer>();
		Iterator<String> it=fields.keySet().iterator();
		int pos=0;
		while(it.hasNext()){
			fieldPos.put(it.next(), pos++);
		}
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
	
	public Integer getColumnPos(String column){
		return fieldPos.get(column);
	}
}
