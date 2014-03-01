package com.plethora.oper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import com.plethora.mem.DataBaseMemoryConfig;
import com.plethora.obj.FieldType;
import com.plethora.obj.FileReader;
import com.plethora.obj.PageEntry;
import com.plethora.obj.Table;
import java.io.IOException;
import java.io.InputStream;

public class TableIterator {
	private Table table;
	public TableIterator(Table table){
		this.table=table;
	}
	InputStream br;
	ListIterator<PageEntry> pageEntries;
	Iterator fType;
	Map.Entry<String, FieldType> fmapEntry;
	PageEntry block;
	String tuple;
	int start=0,end=0;
	public void open(){
		try{
			br = FileReader.getTableInputStream(table.getTableName()+".csv");
			pageEntries=table.getPageEntries().listIterator();
			fType=table.getFields().entrySet().iterator();
			fmapEntry=(Entry<String, FieldType>) fType.next();
			block=pageEntries.next();
			start=0;
			end=block.getEndRecordId();
			tuple=FileReader.readLine(br);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public List<Object> getNext(){
		String tokens[];
		List<Object> list=new ArrayList<Object>();
		if(start>end){
			if(pageEntries.hasNext()){
				block=pageEntries.next();
				start=block.getStartRecordId();
				end=block.getEndRecordId();
				tuple=FileReader.readLine(br);
			}
			else
				return null;
		}
		start=start+1;
		tokens=tuple.split(",");
		for(String attr : tokens){
			switch(fmapEntry.getValue().getType().toString().toLowerCase()){
			case("integer"):
				list.add(Integer.parseInt(attr));
				break;
			case("varchar"):
				list.add(attr);
				break;
			case("float"):
				list.add(Float.parseFloat(attr));
				break;
			}
		}
		tuple=FileReader.readLine(br);
		return list;
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
