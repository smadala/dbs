package com.plethora.oper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.plethora.obj.FieldType;
import com.plethora.obj.FileReader;
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
	Iterator<Map.Entry<String, FieldType>> fType;
	Map.Entry<String, FieldType> fmapEntry;
	PageEntry block;
	String tuple;
	int start=0,end=0;
	public void open(){
		try{
			br = FileReader.getTableInputStream(table.getTableName()+".csv");
			pageEntries=table.getPageEntries().listIterator();
			fType=table.getFields().entrySet().iterator();
			fmapEntry= fType.next();
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
			switch(fmapEntry.getValue().getType()){
			case INTEGER:
				list.add(Integer.parseInt(attr));
				break;
			case VARCHAR:
				list.add(attr);
				break;
			case FLOAT:
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
