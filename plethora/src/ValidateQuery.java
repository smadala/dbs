import gudusoft.gsqlparser.nodes.TResultColumn;
import gudusoft.gsqlparser.nodes.TTable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.plethora.obj.SelectQuery;
import com.plethora.obj.Table;
import com.plethora.obj.FieldType;;


public class ValidateQuery {
	
	public boolean validataQuery(SelectQuery query,Map<String,Table> tableMetaData){
		
		
		//validate tablename
		Iterator<Map.Entry<String,TTable>> it=query.getTables().entrySet().iterator();
		String tableName;
		while(it.hasNext()){
			
			Map.Entry<String, TTable> en=it.next();
			tableName=en.getValue().toString().toLowerCase();
			if( !tableMetaData.containsKey(tableName) )
				return false;
		}
		
		//validate columns
		Set<String> allColumnNames=new HashSet<String>(3);
		Map<String,Set<String>> tableFielNames=new HashMap<>(3);
		Map<String,Map<String,FieldType>> tableAttributes=new HashMap<String,Map<String,FieldType>>(3);
		Map<String,FieldType> fields;
		Set<String> fieldNames;
		String columnName;
		int begin,end;
		it=query.getTables().entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String, TTable> en=it.next();
			tableName=en.getValue().toString().toLowerCase();
			fields=tableMetaData.get(tableName).getFields();
			fieldNames=fields.keySet();
			tableFielNames.put(tableName, fieldNames);
			allColumnNames.addAll(fieldNames);
			tableAttributes.put(tableName, fields);
		}
		if(query.columnsToString().equals("*")){
			query.setResultColumnNames(allColumnNames);
		}else{
			for(int i=0;i<query.getColumns().size();i++){
				TResultColumn column=query.getColumns().getResultColumn(i);
				columnName=column.getExpr().toString();
				begin=columnName.indexOf('.');
				if(begin > -1){
					tableName=columnName.substring(0, begin);
					//fieldNames=
				}
			}
		}
		
		
		
		return false;
	}
}
