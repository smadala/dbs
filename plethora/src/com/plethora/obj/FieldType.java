package com.plethora.obj;

import com.plethora.obj.DataType;

public class FieldType {
	private String name;
	private DataType type;
	private int size;
	private boolean isPrimaryKey;
	public boolean getIsPrimaryKey(){
		return isPrimaryKey;
	}
	public void setIsPrimaryKey(boolean x){
		isPrimaryKey=x;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public DataType getType() {
		return type;
	}
	public void setType(DataType type) {
		this.type = type;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}

}
