
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.plethora.mem.ConfigConstants;
import com.plethora.mem.DataBaseMemoryConfig;
import com.plethora.obj.FileReader;
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
		int totalTables=DBMetaData.tableNames.size();
		InputStream br=null;
		FileReader obj=null;
		int recordId=0,flag=0;
		String line;
		HashMap tables=new HashMap(totalTables);
		try{
			for(int i=0;i<totalTables;i++){
				tables.put(DBMetaData.tableNames.get(i),new Table(DBMetaData.tableNames.get(i)));
				Table temp=(Table) tables.get(DBMetaData.tableNames.get(i));
				br=new FileInputStream(DataBaseMemoryConfig.PATH_FOR_DATA+DBMetaData.tableNames.get(i));
				obj=new FileReader();
				while((line=obj.readLine(br))!=null){
					if(flag==0){
						PageEntry entry = new PageEntry();
						entry.setStartRecordId(recordId);
						recordId=recordId+1;
					}
					else{
						
					}
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public String getRecord(String tableName, int recordId) {
		return "record";
	}
	/*public static void main(String args[]){
		DBSystem ob1=new DBSystem();
		ob1.readConfig("/home/harshas/Desktop/config.txt");
		System.out.println("# of Pages "+DataBaseMemoryConfig.NUM_OF_PAGES);
		System.out.println("Page Size "+DataBaseMemoryConfig.PAGE_SIZE);
		System.out.println("Path for Data "+DataBaseMemoryConfig.PATH_FOR_DATA);
	}*/
}