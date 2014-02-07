
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.plethora.mem.ConfigConstants;
import com.plethora.mem.DataBaseMemoryConfig;
import com.plethora.mem.LRUMemory;
import com.plethora.obj.FileReader;
import com.plethora.obj.Page;
import com.plethora.obj.PageEntry;
import com.plethora.obj.Table;



public class DBSystem {
	public static List<String> tableNames=new ArrayList<String>();
	public static LRUMemory<String, Page> cachedPages;
	
	public static Map<String,Table> tableMetaData=new HashMap<String, Table>();
	
	public static final String LRU_MEMORY_KEY_FORMAT="{0}_{1}"; // tableName_pageNumber
	public static void readConfig(String configFilePath) {
		InputStream br=null;
		int flag=0;
		try {
			String line=null,tokens[]=null;
			br=new FileInputStream(configFilePath);
			while( (line = FileReader.readLine(br))!=null ){
				if((!(line.equals(ConfigConstants.TABLE_BEGIN)))&& flag!=1){
					tokens=line.split(ConfigConstants.PROPS_DELIMITER);
					//memoryProps.put(tokens[0],tokens[1]);
					if(tokens[0].equals(ConfigConstants.PAGESIZE)){
						DataBaseMemoryConfig.PAGE_SIZE=Integer.parseInt(tokens[1]);
						}
					else if(tokens[0].equals(ConfigConstants.NUM_PAGES)){
						DataBaseMemoryConfig.NUM_OF_PAGES=Integer.parseInt(tokens[1]);
						cachedPages=new LRUMemory<String, Page>(DataBaseMemoryConfig.NUM_OF_PAGES);
					}
					else if(tokens[0].equals(ConfigConstants.PATH_FOR_DATA)){
						DataBaseMemoryConfig.PATH_FOR_DATA=new String(tokens[1]);
					}
				}
				else if(line.equals(ConfigConstants.TABLE_BEGIN)){
					flag=1;
				}
				else if(flag==1){
					tableNames.add(line);
					flag=0;
				}
			}
			/*Table table=new Table();
			while( (line=br.readLine()) != null ){
				if( line.equals(""));
				
				
			}*/
			br.close();
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//System.out.println(configFilePath +" file not found!");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void populateDBInfo() {
		

		InputStream br=null;
		int recordId,pageNum;
		String line;
		PageEntry pageEntry=null;
		Table table=null;
		long offset;
		int temp=0;
		try{
			for(String tableName:tableNames){
				
				table=new Table(tableName);
				tableMetaData.put(tableName,table);
				recordId=0;
				br=FileReader.getTableInputStream(tableName);
				offset=0;
				pageNum=0;
				pageEntry = new PageEntry();
				pageEntry.setPageNumber(pageNum);
				pageEntry.setOffset(offset);
				pageEntry.setStartRecordId(recordId);
				
				while((line=FileReader.readLine(br))!=null){
					//System.out.println("Record Length is "+line.length()+" Record is-"+line);
					temp=temp+line.length();
					if(!pageEntry.canAddRecord(line)){
						//System.out.println(line);
						pageEntry.setEndRecordId(recordId-1);// if only one record in page then endRecordId should not be -1 
						table.getPageEntries().add(pageEntry);
						pageNum++;
						pageEntry=new PageEntry();
						pageEntry.canAddRecord(line);
						pageEntry.setStartRecordId(recordId); //Assume record length at most PAGE_SIZE
						pageEntry.setOffset(offset);
						pageEntry.setPageNumber(pageNum);
						temp=line.length();

					}
					recordId++;
					//System.out.println("Temp is "+temp);
					offset += line.length() + 1;
				}
				pageEntry.setEndRecordId(recordId-1);
				table.getPageEntries().add(pageEntry);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
//		pr();
	}

	public static String getRecord(String tableName, int recordId) {
//		System.out.println("Getrecord "+recordId);
		 
		PageEntry pageEntry = getPageEntry(tableName, recordId);
		
		String pageKey = MessageFormat.format(LRU_MEMORY_KEY_FORMAT, tableName,pageEntry.getPageNumber());
		
		Page page =cachedPages.get(pageKey,true);
		if(page == null){
			System.out.print("MISS ");
			page = loadPage(tableName,pageEntry);
			int pos=cachedPages.put(pageKey, page);
			//pos=pos < 0? 0: pos;
			System.out.println(pos);
		}else{
			System.out.println("HIT");
		}
		return page.getRecords().get( recordId - pageEntry.getStartRecordId() );
	}
	
	private static Page loadPage(String tableName,PageEntry pageEntry){
		RandomAccessFile fileReader = FileReader.getRandomAccessFile(tableName, "r"); 
		Page page = new Page();
		int numOfRecords=pageEntry.getEndRecordId() - pageEntry.getStartRecordId() + 1;
		try {
			fileReader.seek(pageEntry.getOffset());
			for(int i=0;i<numOfRecords;i++){
				page.getRecords().add(fileReader.readLine());
			}
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return page;
	}
	
	private static PageEntry getPageEntry(String tableName, int recordId){
		
		List<PageEntry> allEntries =tableMetaData.get(tableName).getPageEntries();
		
		
		PageEntry lookupEntry=new PageEntry();
		lookupEntry.setStartRecordId(recordId);
		int index=Collections.binarySearch(allEntries, lookupEntry,PageEntry.COMPARE_BY_START_RECORD_ID);
		//System.out.println("index "+index);
		if( index < 0){
			index *= -1;
			index--;
		}
		if(index > allEntries.size() -1 )
			index=allEntries.size() -1;
		if(index < 0)
			index=0;
		
		
		while( index < allEntries.size() && 
				!(recordId >= allEntries.get(index).getStartRecordId() && recordId <= allEntries.get(index).getEndRecordId()) ){
				if(recordId < allEntries.get(index).getStartRecordId())
					index--;
				else
					index++;
			}
		
		//TODO : check end condition if given recordId is not present in table
		//System.out.println("final index "+index);
		return allEntries.get(index);
	}
	
	
	
	
	public static void insertRecord(String tableName, String record){
//		System.out.println("Inserted is "+record);
		
		Table table = tableMetaData.get(tableName);
		List<PageEntry> pageEntries = table.getPageEntries();
		PageEntry lastEntry=pageEntries.get(pageEntries.size()-1);
		Page page=null;
		String pageKey;
		int lastRecordId=lastEntry.getEndRecordId();
		int lastPageNum=lastEntry.getPageNumber();
		
		//starting offset of new page in file is lastPage offset + number of bytes in lastPageEntry
		long offset = lastEntry.getOffset() + (DataBaseMemoryConfig.PAGE_SIZE - lastEntry .getLeftOver() );
		
		pageKey = MessageFormat.format(LRU_MEMORY_KEY_FORMAT, tableName, lastPageNum);
		
		
		if(lastEntry.canAddRecord(record)){ //space available in lastPage
			page = cachedPages.get(pageKey,true);  
			if( page == null ){
			   	page=loadPage(tableName, lastEntry);
			   	cachedPages.put(pageKey, page);
			}
			page.getRecords().add(record);
			lastEntry.setEndRecordId(++lastRecordId);
		}
		else{ //create new Page 
		 
			
			PageEntry newEntry= new PageEntry();
			if(newEntry.canAddRecord(record));  //Assume record size is less than PAGE_SIZE
			table.getPageEntries().add(newEntry);
			newEntry.setStartRecordId(++lastRecordId);
			newEntry.setEndRecordId(lastRecordId);
			newEntry.setPageNumber(++lastPageNum);
			newEntry.setOffset(offset);
			page=new Page();
			page.getRecords().add(record);
			pageKey = MessageFormat.format(LRU_MEMORY_KEY_FORMAT, tableName, lastPageNum);
			
			cachedPages.put(pageKey, page);
			
			//page.setRecords(records)
		}
		
		//write to file
		RandomAccessFile dataFile = FileReader.getRandomAccessFile(tableName, "rw");
		
		try {
			dataFile.seek(offset);
			FileReader.writeLine(dataFile, record);
			dataFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void pr(){
		DBSystem ob1=new DBSystem();
		Iterator<Map.Entry<String,Table>> it = tableMetaData.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String, Table> entry=it.next();
			System.out.println(entry.getKey() +"  " + entry.getValue().getPageEntries().size());
			for(PageEntry x:entry.getValue().getPageEntries()){
				System.out.println("Page Number "+x.getPageNumber()+" StartRecordId "+x.getStartRecordId()+" EndRecordId "+x.getEndRecordId()+" LeftOver "+x.getLeftOver());
			}
		}
	}
	
	/*public static void main(String args[]){
		//System.out.println("");
		
		DBSystem ob1=new DBSystem();
		ob1.readConfig("/home/harshas/Desktop/dbs/plethora/config.txt");
		System.out.println("# of Pages "+DataBaseMemoryConfig.NUM_OF_PAGES);
		System.out.println("Page Size "+DataBaseMemoryConfig.PAGE_SIZE);
		System.out.println("Path for Data "+DataBaseMemoryConfig.PATH_FOR_DATA);
		ob1.populatePageInfo();
		Iterator<Map.Entry<String,Table>> it = tableMetaData.entrySet().iterator();
		
		while(it.hasNext()){
			Map.Entry<String, Table> entry=it.next();
			System.out.println(entry.getKey() +"  " + entry.getValue().getPageEntries().size());
		}
		System.out.println(ob1.getRecord("countries", 3));
		for(int i=0;i<5;i++){
			System.out.println(ob1.getRecord("employee", i));
		}
		ob1.insertRecord("employee", "66666");
		for(int i=0;i<6;i++){
			System.out.println(ob1.getRecord("employee", i));
		}
	}*/
}