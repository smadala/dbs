package com.plethora.obj;

import com.plethora.mem.DataBaseMemoryConfig;

public class PageEntry {
	private int pageNumber;
	private int startRecordId;
	private int endRecordId;
	private int numOfRecords; 
	private int leftOver;
	private boolean present;
	
	public PageEntry(){
		leftOver=DataBaseMemoryConfig.PAGE_SIZE;
	}
	
	public int getNumOfRecords() {
		return numOfRecords;
	}

	public void setNumOfRecords(int numOfRecords) {
		this.numOfRecords = numOfRecords;
	}

	public boolean isPresent() {
		return present;
	}

	public void setPresent(boolean present) {
		this.present = present;
	}
	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public void setStartRecordId(int x){
		this.startRecordId=x;
	}
	
	public void setEndRecordId(int y){
		this.endRecordId=y;
	}
	
	public void setLeftOver(int z){
		this.leftOver=z;
	}
	
	public int getStartRecordId(){
		return startRecordId;
	}
	
	public int getEndRecordId(){
		return endRecordId;
	}
	
	public int getLeftOver(){
		return leftOver;
	}
}
