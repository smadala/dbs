import static com.plethora.mem.DataBaseMemoryConfig.memoryProps;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.plethora.mem.DataBaseMemoryConfig;
import com.plethora.obj.Table;

public class DBSystem {
	public void readConfig(String configFilePath) {
		BufferedReader br=null;
		try {
			String line=null,tokens[]=null;
			br=new BufferedReader(new FileReader(configFilePath));
			
			while( !DataBaseMemoryConfig.TABLE_BEGIN.equals(line = br.readLine() )){
			    	
				tokens=line.split(DataBaseMemoryConfig.PROPS_DELIMITER);
				memoryProps.put(tokens[0],tokens[1]);
			}
			Table table=new Table();
			while( (line=br.readLine()) != null ){
				if( line.equals(""));
				
				
			}
			
			
			
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
		return "record";
	}
}