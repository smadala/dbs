package com.plethora.obj;

import java.util.ArrayList;
import java.util.List;

import com.plethora.mem.DataBaseMemoryConfig;

public class Page {
	private List<String> records;
	private int leftOverSize=DataBaseMemoryConfig.PAGE_SIZE;
	private int pageNumber;
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
		this.records=new ArrayList<String>();
	}
	
	public boolean addRecord(String line){
		if(line.length()<=leftOverSize)
		{
			this.records.add(line);
			leftOverSize=leftOverSize-line.length();
		}
		else
			return false;
		return true;
	}
}
