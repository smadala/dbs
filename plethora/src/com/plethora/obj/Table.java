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
	/*public static enum DataType{
		INTEGER("integer"), FLOAT("float"), VARCHAR("varchar");
		private String name;
		private static Map<DataType, Set<String>> validOperations;
		

		private DataType(String name) {
			this.name = name;
			Set<String> intValidOperation = new HashSet<>(Arrays.asList("<", ">",
					">=", "<=", "==", "<>"));
			Set<String> floatValidOperation = new HashSet<>(Arrays.asList("<", ">",
					">=", "<=", "==", "<>"));
			Set<String> varcharValidOperation = new HashSet<>(Arrays.asList("LIKE"));
			validOperations.put(INTEGER, intValidOperation);
			validOperations.put(FLOAT, floatValidOperation);
			validOperations.put(VARCHAR, varcharValidOperation);
		}

		public boolean isValidOperation(DataType type, String oper) {
			// TODO trim oper
			Set<String> typeOpearion = validOperations.get(type);
			return typeOpearion.contains(oper);
		}
		
	}
	public static boolean isValidDataType(String x){
		System.out.println(DataType.INTEGER.name);
		if(x.equals(DataType.FLOAT.toString()) || x.equals(DataType.INTEGER.toString())){
			return true;
		}
		else if(x.matches("varchar(\\d+)")){
			return true;
		}
		else
			return false;
	}*/
	
	
}
