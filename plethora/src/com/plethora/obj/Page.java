package com.plethora.obj;

import java.util.ArrayList;
import java.util.List;


public class Page {
	private List<List<Object>> records;
	public Page(){
		records=new ArrayList<List<Object>>();
	}
	public List<List<Object>> getRecords() {
		return records;
	}
	public void setRecords(List<List<Object>> records) {
		this.records = records;
	}
	
}
