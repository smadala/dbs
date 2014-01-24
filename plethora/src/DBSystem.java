
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import com.plethora.mem.ConfigConstants;
import com.plethora.mem.DataBaseMemoryConfig;
import com.plethora.obj.FileReader;
import com.plethora.obj.Page;
import com.plethora.obj.PageEntry;
import com.plethora.obj.Table;


public class DBSystem {
	public void readConfig(String configFilePath) {
		InputStream br=null;
		int flag=0;
		try {
			String line=null,tokens[]=null;
			br=new FileInputStream(configFilePath);
			FileReader objectRead=new FileReader();
			while( (line = objectRead.readLine(br))!=null ){
				if((!(line.equals(ConfigConstants.TABLE_BEGIN)))&& flag!=1){
					tokens=line.split(ConfigConstants.PROPS_DELIMITER);
					//memoryProps.put(tokens[0],tokens[1]);
					if(tokens[0].equals(ConfigConstants.PAGESIZE)){
						DataBaseMemoryConfig.PAGE_SIZE=Integer.parseInt(tokens[1]);
						}
					else if(tokens[0].equals(ConfigConstants.NUM_PAGES)){
						DataBaseMemoryConfig.NUM_OF_PAGES=Integer.parseInt(tokens[1]);
					}
					else if(tokens[0].equals(ConfigConstants.PATH_FOR_DATA)){
						DataBaseMemoryConfig.PATH_FOR_DATA=new String(tokens[1]);
					}
				}
				else if(line.equals(ConfigConstants.TABLE_BEGIN)){
					flag=1;
				}
				else if(flag==1){
					DBMetaData.tableNames.add(line);
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

	public void populatePageInfo() {

	}

	public String getRecord(String tableName, int recordId) {
		 
		PageEntry pageEntry = getPageEntry(tableName, recordId);
		
		String pageKey = String.format(DBMetaData.LRU_MEMORY_KEY_FORMAT, tableName,pageEntry.getPageNumber());
		
		Page page =DBMetaData.cachedPages.get(pageKey);
		if(page == null){
			System.out.println("MISS ");
			page = loadPage(tableName,pageEntry);
			DBMetaData.cachedPages.put(pageKey, page);
		}else{
			System.out.println("HIT");
		}
		return page.getRecords().get(pageEntry.getStartRecordId() - recordId);
	}
	
	private Page loadPage(String tableName,PageEntry pageEntry){
		return null;
	}
	
	private PageEntry getPageEntry(String tableName, int recordId){
		
		List<PageEntry> allEntries =DBMetaData.tableMetaData.get(tableName).getPageEntries();
		PageEntry lookupEntry=new PageEntry();
		lookupEntry.setStartRecordId(recordId);
		int index=Collections.binarySearch(allEntries, lookupEntry,PageEntry.COMPARE_BY_START_RECORD_ID);
		if( index < 0){
			index *= -1;
			index--;
		}
		//TODO : check end condition if given recordId is not present in table
		return allEntries.get(index);
	}
	
	
	public void insertRecord(String tableName, String record){
		
		Table table = DBMetaData.tableMetaData.get(tableName);
		List<PageEntry> pageEntries = table.getPageEntries();
		PageEntry lastEntry=pageEntries.get(pageEntries.size()-1);
		if(lastEntry.canAddRecord(record)){
			String pageKey = String.format(DBMetaData.LRU_MEMORY_KEY_FORMAT, tableName,lastEntry.getPageNumber());
			
			
		}
		
		
		
	}
	
	
	
	
	public static void main(String args[]){
		//System.out.println("");
		
		/*DBSystem ob1=new DBSystem();
		ob1.readConfig("/home/harshas/Desktop/config.txt");
		System.out.println("# of Pages "+DataBaseMemoryConfig.NUM_OF_PAGES);
		System.out.println("Page Size "+DataBaseMemoryConfig.PAGE_SIZE);
		System.out.println("Path for Data "+DataBaseMemoryConfig.PATH_FOR_DATA);*/
	}
}