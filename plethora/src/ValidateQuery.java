import gudusoft.gsqlparser.EExpressionType;
import gudusoft.gsqlparser.nodes.TExpression;
import gudusoft.gsqlparser.nodes.TGroupByItemList;
import gudusoft.gsqlparser.nodes.TOrderByItemList;
import gudusoft.gsqlparser.nodes.TResultColumn;
import gudusoft.gsqlparser.nodes.TTable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.plethora.obj.DataType;
import com.plethora.obj.FieldType;
import com.plethora.obj.SelectQuery;
import com.plethora.obj.Table;
import com.plethore.excp.InvalidQuery;


public class ValidateQuery {
	
	public boolean validataQuery(SelectQuery query,
			Map<String,Table> tableMetaData)throws InvalidQuery 
	{
		//validate tablename
		Iterator<Map.Entry<String,TTable>> it=query.getTables().entrySet().iterator();
		String tableName;
		while(it.hasNext()){
			
			Map.Entry<String, TTable> en=it.next();
			tableName=en.getValue().toString().toLowerCase();
			if( !tableMetaData.containsKey(tableName) )
				throw new InvalidQuery("Unknown table name: "+tableName);
		}
		
		//validate columns
		Map<String,String> allColumnNames=new LinkedHashMap<String,String>(3);
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
			for(String fieldName:fieldNames){
				if(!allColumnNames.containsKey(fieldName)){
					allColumnNames.put(fieldName,fields.get(fieldName).getName());
				}
			}
			tableAttributes.put(tableName, fields);
		}
		if(query.columnsToString().equals("*")){
			query.setResultColumnNames(new LinkedHashSet(allColumnNames.values()));
		}else{ //validate given columns
			for(int i=0;i<query.getColumns().size();i++){
				TResultColumn column=query.getColumns().getResultColumn(i);
				columnName=column.getExpr().toString();
				validateColumn(columnName, tableFielNames, allColumnNames);
			}
		}
		
		//validate condition
		if( query.getCondition() != null &&  query.getCondition().getCondition() != null){
			if(!validateCondition(query.getCondition().getCondition(), tableFielNames, allColumnNames, tableAttributes))
				throw new InvalidQuery("Invalid type in condition");
		}
		
		//validate OrderBy
		if(query.getOrderby() != null){
			TOrderByItemList orderList=query.getOrderby().getItems();
			String sortColumn;
			for(int i=0;i<orderList.size();i++){
				sortColumn=orderList.getElement(i).getStartToken().toString();
				validateColumn(sortColumn, tableFielNames, allColumnNames);
			}
		}
		//validate GroupBy
		if(query.getGroupby()!= null){
			TGroupByItemList groupbyList=query.getGroupby().getItems();
			String sortColumn;
			for(int i=0;i<groupbyList.size();i++){
				sortColumn=groupbyList.getGroupByItem(i).toString();
				validateColumn(sortColumn, tableFielNames, allColumnNames);
			}
		}
		//validate having
		if(query.getHaving() != null){
			if(!validateCondition(query.getHaving(), tableFielNames, allColumnNames, tableAttributes)){
				throw new InvalidQuery("Invalid type in condition");
			}
		}
		
//		SELECT Subject, Semester, Count(*) FROM student GROUP BY Subject, Semester
		return true;
	}
	private boolean validateCondition(TExpression condition,Map<String,Set<String>> tableFielNames,
			Map<String,String> allColumnNames,Map<String,Map<String,FieldType>> tableAttributes ) throws InvalidQuery{
		if( !is_compare_condition( condition.getExpressionType( ) ))
			return validateCondition(condition.getLeftOperand(), tableFielNames, allColumnNames, tableAttributes) && 
					validateCondition(condition.getRightOperand(), tableFielNames, allColumnNames, tableAttributes);
		    Class leftClass=getType(condition.getLeftOperand(), tableFielNames, allColumnNames, tableAttributes);
		    Class rightClass=getType(condition.getRightOperand(), tableFielNames, allColumnNames, tableAttributes);
		    if(leftClass == null || rightClass == null)
		    	return false;
		    if(leftClass.equals(rightClass))
		    	return true;
		throw new InvalidQuery("invalid condition" +condition.toString());
	}
	private boolean is_compare_condition( EExpressionType t )
	{
		return ( ( t == EExpressionType.simple_comparison_t )
				|| ( t == EExpressionType.group_comparison_t ) || ( t == EExpressionType.in_t ) 
				|| ( t == EExpressionType.pattern_matching_t ));
	}
	
	private Class  getType(TExpression oper, Map<String,Set<String>> tableFielNames,
			Map<String,String> allColumnNames,Map<String,Map<String,FieldType>> tableAttributes ) throws InvalidQuery{
		String rawVal=oper.toString();
		Map<DataType,Class> typeClasses=getTypeMap();
		try{
			double val=Double.parseDouble(rawVal);
			if(val == (int) val){
				return Integer.class;
			}else{
				return Double.class;
			}
		}catch(NumberFormatException e){
			if((rawVal.charAt(0) == '\"' && rawVal.charAt(rawVal.length()-1) == '\"' ) ||
					(rawVal.charAt(0) == '\'' && rawVal.charAt(rawVal.length()-1) == '\'' )){
				return String.class;
			}
			else{
				 //try {
					if(validateColumn(rawVal , tableFielNames, allColumnNames)){
						DataType type=getDataType(rawVal , tableAttributes, tableFielNames, allColumnNames);
						return typeClasses.get(type);
					}
				/*} catch (InvalidQuery e1) {
					// TODO Auto-generated catch block
					return null;
				}*/
			}
		}
		return null;
	}
	private DataType getDataType(String columnName,Map<String,Map<String,FieldType>> tableAttributes, 
			Map<String,Set<String>> tableFielNames,	Map<String,String> allColumnNames){
		String tableName;
		Set<String> fieldNames;
		columnName=SelectQuery.stripParanthesis(columnName);
		int begin=columnName.indexOf('.');
		if(begin > -1){
			tableName=columnName.substring(0, begin);
			fieldNames=tableFielNames.get(tableName);
			if(fieldNames == null) {//unknown prefix
				 return null;
			}
			columnName=columnName.substring(begin+1);
			if(!fieldNames.contains(columnName)) //column name not exist
				return null;
			return getDataType(columnName, tableName, tableAttributes);
			
		}else{
			if(!allColumnNames.containsKey(columnName.toLowerCase()))
				return null;
			Iterator<Map.Entry<String, Map<String,FieldType>>> it=tableAttributes.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry<String, Map<String,FieldType>> en=it.next();
				if(en.getValue().get(columnName.toLowerCase())!=null){
					return getDataType(columnName, en.getKey(), tableAttributes);
				}
			}
		}
		return null;
	}
	private DataType getDataType(String columnName, String tableName, Map<String, Map<String,FieldType>> tableAttributes){
		
		if(tableName !=null){
			Map<String, FieldType> fields=tableAttributes.get(tableName);
			FieldType type=fields.get(columnName.toLowerCase());
			return type.getType();
		}
		return null;
	}
	private Map<DataType,Class> getTypeMap(){
		Map<DataType,Class> types=new HashMap<DataType,Class>();
		types.put(DataType.INTEGER, Integer.class);
		types.put(DataType.FLOAT, Integer.class);
		types.put(DataType.VARCHAR, String.class);
		return types;
	}
	
	private boolean validateColumn(String columnName,Map<String,Set<String>> tableFielNames,
			Map<String,String> allColumnNames) throws InvalidQuery {
		String tableName;
		Set<String> fieldNames;
		columnName=SelectQuery.stripParanthesis(columnName);
		int begin=columnName.indexOf('.');
		if(begin > -1){
			tableName=columnName.substring(0, begin);
			fieldNames=tableFielNames.get(tableName.toLowerCase());
			if(fieldNames == null) {//unknown prefix
				 throw  new InvalidQuery("Unknown table name: " +columnName);
		}
			columnName=columnName.substring(begin+1);
			if(!fieldNames.contains(columnName)) //column name not exist
				throw  new InvalidQuery("Unknown column name: " +columnName + " in "+tableName);
		}else{
			if(!allColumnNames.containsKey(columnName.toLowerCase()))
				throw  new InvalidQuery("Unknown column name: " +columnName );
		}
		return true;
	}
	/*public static String stripParanthesis(String input){
		int begin=0,parnCount=1;
		while(input.charAt(begin) == ' ') begin++;
		if(input.charAt(begin) == '('){
			int end=begin;
			while( parnCount != 0){
				end++;
				if(input.charAt(end) == '(')
					parnCount++;
				else if(input.charAt(end) == ')')
					parnCount--;
			}
			return input.substring(begin+1, end).trim();
		}
		return input;
	}*/
}
