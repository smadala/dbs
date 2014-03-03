package com.plethora.oper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.plethora.obj.FieldType;
import com.plethora.obj.FileReader;
import com.plethora.obj.Page;
import com.plethora.obj.PageEntry;
import com.plethora.obj.Table;

public class TableIterator {
	private Table table;
	public TableIterator(Table table){
		this.table=table;
	}
	InputStream iStream;
	Iterator<PageEntry> pageEntries;
	Iterator<FieldType> fieldsIt;
	PageEntry pageEntry;
	Page page;
	List<Object> currentTuple;
	Iterator<List<Object>> tupleIterator;
	Set<FieldType> fields; 
	
	boolean readBlock( ){
		if(pageEntries.hasNext()){
			pageEntry=pageEntries.next();
			List<List<Object>> tuples=new ArrayList<List<Object>>();
			String line;
			int start=0,end=0;
			start=pageEntry.getStartRecordId();
			end=pageEntry.getEndRecordId();
			while((line=FileReader.readLine(iStream))!=null && start<=end){
				List<Object> attr=FileReader.getTuple(line, fieldsIt);
				start=start+1;
				tuples.add(attr);
			}
			page=new Page();
			page.setRecords(tuples);
			return true;
		}
		else
			return false;
	}
	
	public void open(){
		try{
			iStream = FileReader.getTableInputStream(table.getTableName());
			pageEntries=table.getPageEntries().iterator();
			fieldsIt=table.getFields().values().iterator();
			if(readBlock()){
				tupleIterator=page.getRecords().iterator();
				/*if(tupleIterator.hasNext()){
					currentTuple=tupleIterator.next();
				}*/
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public List<Object> getNext(){
		if(!tupleIterator.hasNext()){
			if(readBlock()){
				tupleIterator=page.getRecords().iterator();
				if(tupleIterator.hasNext())
					return tupleIterator.next();
				return null;
			}
			return null;
		}
		/*oldTuple=currentTuple;
		if(tupleIterator.hasNext()){
			currentTuple=tupleIterator.next();
		}*/
		return tupleIterator.next();
	}
	
	public void close(){
		try{
			iStream.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
