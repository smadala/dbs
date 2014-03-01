package com.plethora.oper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.plethora.obj.FieldType;
import com.plethora.obj.FileReader;
import com.plethora.obj.Page;
import com.plethora.obj.PageEntry;
import com.plethora.obj.Table;
import java.io.InputStream;

public class TableIterator {
	private Table table;
	public TableIterator(Table table){
		this.table=table;
	}
	InputStream br;
	ListIterator<PageEntry> pageEntries;
	Iterator<Map.Entry<String, FieldType>> fieldsIt;
	Map.Entry<String, FieldType> fmapEntry;
	PageEntry pageEntry;
	Page page;
	List<Object> currentTuple;
	Iterator<List<Object>> tupleIterator;
	
	boolean readBlock(){
		if(pageEntries.hasNext()){
			pageEntry=pageEntries.next();
			List<List<Object>> tuples=new ArrayList<List<Object>>();
			String tokens[];
			String line;
			int start=0,end=0;
			start=pageEntry.getStartRecordId();
			end=pageEntry.getEndRecordId();
			while((line=FileReader.readLine(br))!=null && start<=end){
				List<Object> attr= new ArrayList<Object>();
				tokens=line.split(",");
				for(String temp : tokens){
					switch(fmapEntry.getValue().getType()){
					case INTEGER:
						attr.add(Integer.parseInt(temp));
						break;
					case VARCHAR:
						attr.add(temp);
						break;
					case FLOAT:
						attr.add(Float.parseFloat(temp));
						break;
					}
				}
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
			br = FileReader.getTableInputStream(table.getTableName()+".csv");
			pageEntries=table.getPageEntries().listIterator();
			fieldsIt=table.getFields().entrySet().iterator();
			if(fieldsIt.hasNext()){
				fmapEntry= fieldsIt.next();
				readBlock();
				tupleIterator=page.getRecords().iterator();
				if(tupleIterator.hasNext()){
					currentTuple=tupleIterator.next();
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public List<Object> getNext(){
		List<Object> oldTuple;
		if(!tupleIterator.hasNext()){
			if(readBlock()){
				tupleIterator=page.getRecords().iterator();
				if(tupleIterator.hasNext()){
					currentTuple=tupleIterator.next();
				}
			}
			else
				return null;
		}
		oldTuple=currentTuple;
		if(tupleIterator.hasNext()){
			currentTuple=tupleIterator.next();
		}
		return oldTuple;
	}
	
	public void close(){
		try{
			br.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
