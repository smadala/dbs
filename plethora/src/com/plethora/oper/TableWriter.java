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
		pageNumbers=pageNumbers+1;
		table.getPageEntries().add(lastPageEntry);
	}
	
	public void write(List<Object> record){
		
		stringBuilder.append(FileReader.toString(record)).append("\n");		
		List<PageEntry> pageEntries = table.getPageEntries();
		lastPageEntry = pageEntries.get(pageEntries.size() - 1);
		int lastRecordId = lastPageEntry.getEndRecordId();
		long offset = lastPageEntry.getOffset()
				+ (DataBaseMemoryConfig.PAGE_SIZE - lastPageEntry.getLeftOver());

		if (lastPageEntry.canAddRecord(stringBuilder.toString())) { // space available in lastPage
			List<Object> r=new ArrayList<>();
			r.add(record);
			lastPage.getRecords().add(r);
			lastPageEntry.setEndRecordId(++lastRecordId);
		} else { // create new Page
			table.getPageEntries().add(lastPageEntry);
			//add the lastpage to repository , cachedPages may be.!
			FileReader.writePage(lastPage, OStream);
			
			stringBuilder=new StringBuilder();
			stringBuilder.append(FileReader.toString(record)).append("\n");
			
			lastPageEntry=createPageEntry(++lastRecordId, lastRecordId, offset);
			pageNumbers=pageNumbers+1;
			
			if (lastPageEntry.canAddRecord(stringBuilder.toString()))
				; // Assume record size is less than PAGE_SIZE
			
			table.getPageEntries().add(lastPageEntry);
			
			lastPage = new Page();
			List<Object> r=new ArrayList<>();
			r.add(record);
			lastPage.getRecords().add(r);
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
	public static void main(String args[]){
		Table table=new Table("temp");
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
	}
}
