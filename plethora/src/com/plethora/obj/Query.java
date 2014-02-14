package com.plethora.obj;

public class Query {
	protected QueryType queryType;
	
	
	public QueryType getQueryType() {
		return queryType;
	}


	public void setQueryType(QueryType queryType) {
		this.queryType = queryType;
	}


	public static enum QueryType{
		CREATE("create"), SELECT("select"), INSERT("insert"), UPDATE("update"), DELETE("delete");
		private QueryType(String name){
			this.name=name;
		}
		private String name;
		
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public static QueryType getQueryType(String name){
			switch(name.toLowerCase()){
			case "create": return CREATE;
			case "select": return SELECT;
			case "insert": return INSERT;
			case "update": return UPDATE;
			case "delete": return DELETE;
			}
			return null;
		}
	}
}
