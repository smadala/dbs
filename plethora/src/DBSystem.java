
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.plethora.mem.ConfigConstants;
import com.plethora.mem.DataBaseMemoryConfig;
import com.plethora.obj.Page;
import com.plethora.obj.Table;

public class DBSystem {
	public Page mainMemoryPages[];
	public void readConfig(String configFilePath) {
		BufferedReader br=null;
		try {
			String line=null,tokens[]=null;
			br=new BufferedReader(new FileReader(configFilePath));
			
			while( !ConfigConstants.TABLE_BEGIN.equals(line = br.readLine() )){
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
			/*Table table=new Table();
			while( (line=br.readLine()) != null ){
				
				
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
		mainMemoryPages=new Page[DataBaseMemoryConfig.NUM_OF_PAGES];
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