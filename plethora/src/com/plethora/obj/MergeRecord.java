package com.plethora.obj;

import java.util.List;

public class MergeRecord {
	public List<Object> record;
	public int subTableIndex;
	public MergeRecord(List<Object> record, int subTableIndex){
		this.record=record;
		this.subTableIndex=subTableIndex;
	}
	public String toString(){
		return subTableIndex+record.toString();
	}
}
