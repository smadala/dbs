package com.plethora.obj;

import java.util.HashMap;
import java.util.Map;

public class Table {
	
	private String name;
	private Map<String,FieldType> fields;
	
	public Table() {
		
		fields=new HashMap<String,FieldType>();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public  enum DataType{
		INT, FLOAT,	STRING,	FIXED_CHAR;
	}
}
