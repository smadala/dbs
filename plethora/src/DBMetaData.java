import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.plethora.mem.DataBaseMemoryConfig;
import com.plethora.mem.LRUMemory;
import com.plethora.obj.Page;
import com.plethora.obj.Table;


public class DBMetaData {
	
	public static List<String> tableNames=new ArrayList<String>();
	public static LRUMemory<String, Page> cachedPages=new LRUMemory<String, Page>(DataBaseMemoryConfig.NUM_OF_PAGES);
	
	public static Map<String,Table> tableMetaData=new HashMap<String, Table>();
	
	public static final String LRU_MEMORY_KEY_FORMAT="{0)_{1}"; // tableName_pageNumber
	
	
}
