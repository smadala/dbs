package com.plethora.oper;

import java.io.RandomAccessFile;

import static com.plethora.mem.DataBaseMemoryConfig.cachedPages;
import java.util.Iterator;
import java.util.List;
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
		tableName=table.getTableName();
		fileReader = FileReader.getRandomAccessFile(tableName,"r");
		open();
	}
	RandomAccessFile fileReader; 
	private Iterator<PageEntry> pageEntriesIt;
	List<FieldType> fieldList;
	private PageEntry pageEntry;
	Page page;
	Iterator<List<Object>> tupleIterator;
	String tableName;
	boolean readBlock( ){
		String cacheKey=null;
		if(pageEntriesIt.hasNext()){
			
			pageEntry=pageEntriesIt.next();
			cacheKey=FileReader.getCacheKey(table.getTableName(), pageEntry.getPageNumber());
			page=cachedPages.get(cacheKey, true);
			if( page != null)
				return true;
			page=FileReader.loadPage(cacheKey, pageEntry, fieldList, fileReader);
			cachedPages.put(cacheKey, page);

			return true;
		}
		else{
			return false;
		}
	}
	
	public void open(){
		try{
			pageEntriesIt=table.getPageEntries().iterator();
			fieldList=table.getFieldList();
			if(readBlock()){
				tupleIterator=page.getRecords().iterator();
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
				if(tupleIterator.hasNext()){
					return tupleIterator.next();
				}
				return null;
			}
			return null;
		}
		return tupleIterator.next();
	}
	
	public void close(){
		try{
			fileReader.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
