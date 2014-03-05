import gudusoft.gsqlparser.EExpressionType;
import gudusoft.gsqlparser.nodes.TExpression;
import gudusoft.gsqlparser.nodes.TGroupByItemList;
import gudusoft.gsqlparser.nodes.TOrderByItemList;
import gudusoft.gsqlparser.nodes.TResultColumn;
import gudusoft.gsqlparser.nodes.TTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.plethora.obj.ComparisonOperator;
import com.plethora.obj.Condition;
import com.plethora.obj.DataType;
import com.plethora.obj.Expression;
import com.plethora.obj.FieldType;
import com.plethora.obj.LogicalOperator;
import com.plethora.obj.OrderBy;
import com.plethora.obj.Select;
import com.plethora.obj.SelectQuery;
import com.plethora.obj.Table;
import com.plethora.obj.WhereClause;
import com.plethore.excp.InvalidQuery;


public class ValidateQuery {
	
	private Map<String,Table> tableMetaData;
	private Map<String,String> allColumnNames;
	private Map<String,Set<String>> tableFieldNames;
	private Map<String,Map<String,FieldType>> tableAttributes;
	
	private SelectQuery query;
	private Select select;
	
	
	public ValidateQuery(Map<String,Table> tableMetaData,SelectQuery query){
		this.tableMetaData=tableMetaData;
		this.query=query;
		Iterator<Map.Entry<String,TTable>> it=query.getTables().entrySet().iterator();
		String tableNamelocal;
		allColumnNames=new LinkedHashMap<String,String>(3);
		tableFieldNames=new HashMap<>(3);
		tableAttributes=new HashMap<String,Map<String,FieldType>>(3);
		Map<String,FieldType> fields;
		Set<String> fieldNames;
		
		while(it.hasNext()){
			Map.Entry<String, TTable> en=it.next();
			tableNamelocal=en.getValue().toString().toLowerCase();
			fields=tableMetaData.get(tableNamelocal).getFields();
			fieldNames=fields.keySet();
			tableFieldNames.put(tableNamelocal, fieldNames);
			for(String fieldName:fieldNames){
				if(!allColumnNames.containsKey(fieldName)){
					allColumnNames.put(fieldName,fields.get(fieldName).getName());
				}
			}
			tableAttributes.put(tableNamelocal, fields);
		}
		select=new Select();
	}
	public boolean validataQuery()throws InvalidQuery 
	{
		//validate tablename
		String tableNamelocal;
		Iterator<Map.Entry<String,TTable>>  it=query.getTables().entrySet().iterator();
		while(it.hasNext()){
			
			Map.Entry<String, TTable> en=it.next();
			tableNamelocal=en.getValue().toString().toLowerCase();
			Table table = tableMetaData.get(tableNamelocal);
			if( table == null )
				throw new InvalidQuery("Unknown table name: "+tableNamelocal);
			select.table=table;
		}
		
		//validate columns
		String columnName;
		List<Expression> projCols=new ArrayList<>();
		Expression projCol=null;
		if(query.columnsToString().equals("*")){
			query.setResultColumnNames(new LinkedHashSet(allColumnNames.values()));
			
		}else{ //validate given columns
			for(int i=0;i<query.getColumns().size();i++){
				TResultColumn column=query.getColumns().getResultColumn(i);
				columnName=column.getExpr().toString();
				validateColumn(columnName);
				projCol=new Expression(select.table.getColumnPos(columnName.toLowerCase()),columnName);
				projCols.add(projCol);
			}
		}
		select.projCols=projCols;
		//validate condition
		WhereClause where=new WhereClause();
		if( query.getCondition() != null &&  query.getCondition().getCondition() != null){
			
			//System.out.println(query.getCondition().getCondition());
			if(!validateCondition(query.getCondition().getCondition(),where))
				throw new InvalidQuery("Invalid type in condition");
		}
		select.where=where;
		List<OrderBy> orderBies=new ArrayList<>();
		//validate OrderBy
		if(query.getOrderby() != null){
			TOrderByItemList orderList=query.getOrderby().getItems();
			OrderBy orderBy=null;
			String sortColumn, order;
			int pos;
			for(int i=0;i<orderList.size();i++){
				
				sortColumn = orderList.getElement(i).getStartToken().toString().toLowerCase();
				order = orderList.getElement(i).getEndToken().toString().toLowerCase();
				validateColumn(sortColumn);
				orderBy=new OrderBy();
				pos=select.table.getColumnPos(sortColumn);
				if(sortColumn.equals(order)){
					orderBy.attributePos=pos;
				}else{
					if(order.equals("desc")){
						orderBy.desc=true;
					}
					orderBy.attributePos=pos;
				}
				orderBies.add(orderBy);
			}
		}
		select.orderBies=orderBies;
		//validate GroupBy
		if(query.getGroupby()!= null){
			TGroupByItemList groupbyList=query.getGroupby().getItems();
			String sortColumn;
			for(int i=0;i<groupbyList.size();i++){
				sortColumn=groupbyList.getGroupByItem(i).toString();
				validateColumn(sortColumn);
			}
		}
		
		//validate having
		WhereClause having=new WhereClause();
		if(query.getHaving() != null){
			if(!validateCondition(query.getHaving(),having)){
				throw new InvalidQuery("Invalid type in condition");
			}
		}
		select.having=having;
//		SELECT Subject, Semester, Count(*) FROM student GROUP BY Subject, Semester
		return true;
	}
	private boolean validateCondition(TExpression tCondition ,WhereClause where) throws InvalidQuery{
		if( !is_compare_condition( tCondition.getExpressionType( ) )){
			
			LogicalOperator logicalOperator=LogicalOperator.OR;
			if( tCondition.getExpressionType() == EExpressionType.logical_and_t)
				logicalOperator=LogicalOperator.AND;
			where.logicalOperators.add(logicalOperator);
			
			return validateCondition(tCondition.getLeftOperand(),where) && 
					validateCondition(tCondition.getRightOperand(),where);
		}
		    Class leftClass=getType(tCondition.getLeftOperand());
		    Class rightClass=getType(tCondition.getRightOperand());
		    
		    if(leftClass == null || rightClass == null || !leftClass.equals(rightClass)){
		    	throw new InvalidQuery("invalid condition " +tCondition.toString());
		    }
		    where.conditions.add(getCondition(tCondition));
		 return true;  
	}
	
	
	private Condition getCondition(TExpression tCondition){
		TExpression lCond=tCondition.getLeftOperand(), rCond=tCondition.getRightOperand(); 
		String lopar=lCond.toString().toLowerCase();
		String ropar=stripQuotes(rCond.toString());
		Condition condition=new Condition();
		Integer pos=select.table.getColumnPos(lopar);
		condition.attributePos=pos;
		condition.val=getValue(ropar, select.table.getFields().get(lopar));
		if(tCondition.getExpressionType() == EExpressionType.simple_comparison_t)
			condition.operator=DataType.getComparisonOperator(tCondition.getComparisonOperator().astext);
		else
			condition.operator=ComparisonOperator.LIKE;
		return condition;
	}
	private Object getValue(String val,FieldType fieldType){
		DataType type =fieldType.getType();
		if( type == DataType.INTEGER)
			return Integer.parseInt(val);
		else if( type == DataType.VARCHAR)
			return val.toString();
		return Float.parseFloat(val);
	}
	
	private boolean is_compare_condition( EExpressionType t )
	{
		//System.out.println(t);
		return ( ( t == EExpressionType.simple_comparison_t )
				|| ( t == EExpressionType.group_comparison_t ) || ( t == EExpressionType.in_t ) 
				|| ( t == EExpressionType.pattern_matching_t ));
	}
	
	
	private Class  getType(TExpression oper ) throws InvalidQuery{
		String rawVal=oper.toString();
		Map<DataType,Class> typeClasses=getTypeMap();
		try{
			float val=Float.parseFloat(rawVal);
			if(val == (int) val){
				return Integer.class;
			}else{
				return Integer.class;
			}
		}catch(NumberFormatException e){
			if((rawVal.charAt(0) == '\"' && rawVal.charAt(rawVal.length()-1) == '\"' ) ||
					(rawVal.charAt(0) == '\'' && rawVal.charAt(rawVal.length()-1) == '\'' )){
				return String.class;
			}
			else{
				 //try {
					if(validateColumn(rawVal)){
						DataType type=getDataType(rawVal);
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
	private DataType getDataType(String columnName){
		String tableName;
		Set<String> fieldNames;
		columnName=SelectQuery.stripParanthesis(columnName);
		int begin=columnName.indexOf('.');
		if(begin > -1){
			tableName=columnName.substring(0, begin);
			fieldNames=tableFieldNames.get(tableName);
			if(fieldNames == null) {//unknown prefix
				 return null;
			}
			columnName=columnName.substring(begin+1);
			if(!fieldNames.contains(columnName)) //column name not exist
				return null;
			return getDataType(columnName, tableName);
			
		}else{
			if(!allColumnNames.containsKey(columnName.toLowerCase()))
				return null;
			Iterator<Map.Entry<String, Map<String,FieldType>>> it=tableAttributes.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry<String, Map<String,FieldType>> en=it.next();
				if(en.getValue().get(columnName.toLowerCase())!=null){
					return getDataType(columnName, en.getKey());
				}
			}
		}
		return null;
	}
	private DataType getDataType(String columnName, String tableName){
		
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
	
	private boolean validateColumn(String columnName) throws InvalidQuery {
		String tableName;
		Set<String> fieldNames;
		columnName=SelectQuery.stripParanthesis(columnName);
		int begin=columnName.indexOf('.');
		if(begin > -1){
			tableName=columnName.substring(0, begin);
			fieldNames=tableFieldNames.get(tableName.toLowerCase());
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
	public Select getSelect() {
		return select;
	}
	public void setSelect(Select select) {
		this.select = select;
	}
	
	public static String stripQuotes(String input){
		if(input == null || input.isEmpty())
			return input;
		boolean start=false,end=false;int len=input.length();
		if( input.charAt(0) == '"' || input.charAt(0) == '\'')
			start=true;
		
		if( input.charAt(len-1) == '"' || input.charAt(len-1) == '\'' )
			end=true;
		if( !start && !end)
			return input;
		else if(start && end)
			return input.substring(1, len-1);
		else if (start)
			return input.substring(1);
		else 
			return input.substring(0,len-1);
		
	}
}
