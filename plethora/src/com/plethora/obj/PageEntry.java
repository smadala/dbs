package com.plethora.obj;

import java.util.Comparator;

import com.plethora.mem.DataBaseMemoryConfig;

public class PageEntry {
	private int pageNumber;
	private int startRecordId;
	private int endRecordId;
	private int leftOver=DataBaseMemoryConfig.PAGE_SIZE;
	private boolean present=false;
	
	public boolean isPresent() {
		return present;
	}

	public void setPresent(boolean present) {
		this.present = present;
	}
	
	

	public PageEntry() {
		
	}

	public PageEntry(int x,int y){
		startRecordId=x;
		endRecordId=y;
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
	
	public static final  CompareByStartId COMPARE_BY_START_RECORD_ID = new PageEntry.CompareByStartId();
	
	private static class CompareByStartId implements Comparator<PageEntry>{

		public int compare(PageEntry o1, PageEntry o2) {
			if(o1.getStartRecordId() > o2.getStartRecordId())	  		return 1;
			else if(o1.getStartRecordId() < o2.getStartRecordId())		return -1;
																		return 0;
		}
		
	}
}
