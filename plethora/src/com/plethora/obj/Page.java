package com.plethora.obj;

import java.util.ArrayList;
import java.util.List;


public class Page {
	private List<String> records;
	public Page(){
		records=new ArrayList<String>();
	}
	public List<String> getRecords() {
		return records;
	}
	public void setRecords(List<String> records) {
		this.records = records;
	}
	
}
