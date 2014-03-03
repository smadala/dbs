package com.plethora.oper;

import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import com.plethora.mem.DataBaseMemoryConfig;
import com.plethora.obj.FileReader;
import com.plethora.obj.Page;
import com.plethora.obj.PageEntry;
import com.plethora.obj.Table;

public class TableWriter {
	Table table;
	
	public TableWriter(Table table){
		this.table=table;
	}
	
	OutputStream OStream=null;
	Page lastPage=null;
	PageEntry lastPageEntry;
	int pageNumbers=0;
	StringBuilder stringBuilder=new StringBuilder();
	
	public void open(){
		OStream=FileReader.getTableOutputStream(table.getTableName());
		lastPage=new Page();
		lastPageEntry=createPageEntry(0, 0, 0);
		System.out.println("In Open "+lastPageEntry.getLeftOver());
		System.out.println("PageSize "+DataBaseMemoryConfig.PAGE_SIZE);
		pageNumbers=pageNumbers+1;
		table.getPageEntries().add(lastPageEntry);
	}
	
	public void write(List<Object> record){
		List<PageEntry> pageEntries = table.getPageEntries();
		lastPageEntry = pageEntries.get(pageEntries.size() - 1);
		int lastRecordId = lastPageEntry.getEndRecordId();
		long offset = lastPageEntry.getOffset()
				+ (DataBaseMemoryConfig.PAGE_SIZE - lastPageEntry.getLeftOver());
        String test=stringBuilder.toString()+FileReader.toString(record)+'\n';
		if (lastPageEntry.canAddRecord(test)) { // space available in lastPage
			stringBuilder.append(FileReader.toString(record)).append("\n");	
			lastPage.getRecords().add(record);
			lastPageEntry.setEndRecordId(++lastRecordId);
		} else { // create new Page
			table.getPageEntries().add(lastPageEntry);
			//add the lastpage to repository , cachedPages may be.!
			stringBuilder.deleteCharAt(stringBuilder.length()-1);
			FileReader.writeLine(OStream, stringBuilder.toString());
			
			stringBuilder=new StringBuilder();
			stringBuilder.append(FileReader.toString(record)).append("\n");
			
			lastPageEntry=createPageEntry(++lastRecordId, lastRecordId, offset);
			pageNumbers=pageNumbers+1;
			
			if (lastPageEntry.canAddRecord(stringBuilder.toString()))
				; // Assume record size is less than PAGE_SIZE
			
			table.getPageEntries().add(lastPageEntry);
			
			lastPage = new Page();
			lastPage.getRecords().add(record);
		}

	}
	
	public PageEntry createPageEntry(int start,int end,long offset){
		PageEntry temp=new PageEntry();
		temp.setStartRecordId(start);
		temp.setEndRecordId(end);
		temp.setOffset(offset);
		temp.setPageNumber(pageNumbers);
		return temp;
	}
	
	public void close(){
		try{
			OStream.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
/*	public void testing(Table table){
		
		//DBSystem ob=new DBSystem();Can't create instance of default package's class.
		List<Object> record=new ArrayList();
		record.add("Harsha");
		record.add("Vardhan");
		record.add(8);
		TableWriter tableWriter=new TableWriter(table);
		tableWriter.open();
		tableWriter.write(record);
		record=new ArrayList();
		record.add("Harsha");
		record.add("Vardhan");
		record.add(8);
		tableWriter.write(record);
		record=new ArrayList();
		record.add("Harsha");
		record.add("Vardhan");
		record.add(8);
		tableWriter.write(record);
		record=new ArrayList();
		record.add("Harsha");
		record.add("Vardhan");
		record.add(8);
		tableWriter.write(record);
		record=new ArrayList();
		record.add("Harsha");
		record.add("Vardhan");
		record.add(8);
		tableWriter.write(record);
	}*/
}
