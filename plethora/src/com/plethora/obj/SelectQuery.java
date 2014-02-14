package com.plethora.obj;

import gudusoft.gsqlparser.TBaseType;
import gudusoft.gsqlparser.nodes.TExpression;
import gudusoft.gsqlparser.nodes.TGroupBy;
import gudusoft.gsqlparser.nodes.TJoin;
import gudusoft.gsqlparser.nodes.TJoinItem;
import gudusoft.gsqlparser.nodes.TOrderBy;
import gudusoft.gsqlparser.nodes.TResultColumn;
import gudusoft.gsqlparser.nodes.TResultColumnList;
import gudusoft.gsqlparser.nodes.TSelectDistinct;
import gudusoft.gsqlparser.nodes.TTable;
import gudusoft.gsqlparser.nodes.TWhereClause;
import gudusoft.gsqlparser.stmt.TSelectSqlStatement;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SelectQuery extends Query {
	private Map<String,TTable> tables;
	private TResultColumnList columns;
	private TSelectDistinct distinct;
	private TWhereClause condition;
	private TOrderBy orderby;
	private TGroupBy groupby;
	private TExpression having;
	private Set<String> resultColumnNames;
	private String distinctString;
	
	public SelectQuery(TSelectSqlStatement stmt,String rawQuery){
		queryType=QueryType.SELECT;
		this.columns=stmt.getResultColumnList();//TSelectSqlStatement
		this.condition=stmt.getWhereClause();
		this.groupby=stmt.getGroupByClause();
		if(groupby!=null){
			having =groupby.getHavingClause();
		}
		this.orderby=stmt.getOrderbyClause();
		this.distinct=stmt.getSelectDistinct();
		if(distinct != null){
			StringBuilder text=new StringBuilder();
			int parnCount=1;
			int begin=rawQuery.toLowerCase().indexOf("distinct");
			begin+=8;
			while(rawQuery.charAt(begin) == ' ') begin++;
			if(rawQuery.charAt(begin) == '('){
				int end=begin;
				while( parnCount != 0){
					end++;
					if(rawQuery.charAt(end) == '(')
						parnCount++;
					else if(rawQuery.charAt(end) == ')')
						parnCount--;
				}
				distinctString=rawQuery.substring(begin+1, end).trim();
			}
		}
		this.tables=new LinkedHashMap<>();
		String tableKey;
		for(int i=0;i<stmt.joins.size();i++){
            TJoin join = stmt.joins.getJoin(i);
            switch (join.getKind()){
                case TBaseType.join_source_fake:
                	tableKey=join.getTable().getAliasClause()==null?join.getTable().toString():join.getTable().getAliasClause().toString();
                	tables.put(tableKey,join.getTable());
        //            System.out.printf("table: %s, alias: %s\n",join.getTable().toString(),(join.getTable().getAliasClause() !=null)?join.getTable().getAliasClause().toString():"");
                    break;
                case TBaseType.join_source_table:
//                    System.out.printf("table: %s, alias: %s\n",join.getTable().toString(),(join.getTable().getAliasClause() !=null)?join.getTable().getAliasClause().toString():"");
                    for(int j=0;j<join.getJoinItems().size();j++){
                        TJoinItem joinItem = join.getJoinItems().getJoinItem(j);
                      //  System.out.printf("Join type: %s\n",joinItem.getJoinType().toString());
                        System.out.printf("table: %s, alias: %s\n",joinItem.getTable().toString(),(joinItem.getTable().getAliasClause() !=null)?joinItem.getTable().getAliasClause().toString():"");
                        tableKey=join.getTable().getAliasClause()==null?join.getTable().toString():join.getTable().getAliasClause().toString();
                    	tables.put(tableKey,join.getTable());
                        if (joinItem.getOnCondition() != null){
                            System.out.printf("On: %s\n",joinItem.getOnCondition().toString());
                        }else  if (joinItem.getUsingColumns() != null){
                            //System.out.printf("using: %s\n",joinItem.getUsingColumns().toString());
                        }
                    }
                    break;
                case TBaseType.join_source_join:
                    TJoin source_join = join.getJoin();
                  //  System.out.printf("table: %s, alias: %s\n",source_join.getTable().toString(),(source_join.getTable().getAliasClause() !=null)?source_join.getTable().getAliasClause().toString():"");

                    for(int j=0;j<source_join.getJoinItems().size();j++){
                        TJoinItem joinItem = source_join.getJoinItems().getJoinItem(j);
                  //      System.out.printf("source_join type: %s\n",joinItem.getJoinType().toString());
                        System.out.printf("table: %s, alias: %s\n",joinItem.getTable().toString(),(joinItem.getTable().getAliasClause() !=null)?joinItem.getTable().getAliasClause().toString():"");
                        tableKey=join.getTable().getAliasClause()==null?join.getTable().toString():join.getTable().getAliasClause().toString();
                    	tables.put(tableKey,join.getTable());
                        if (joinItem.getOnCondition() != null){
                            System.out.printf("On: %s\n",joinItem.getOnCondition().toString());
                        }else  if (joinItem.getUsingColumns() != null){
                            System.out.printf("using: %s\n",joinItem.getUsingColumns().toString());
                        }
                    }

                    for(int j=0;j<join.getJoinItems().size();j++){
                        TJoinItem joinItem = join.getJoinItems().getJoinItem(j);
                        System.out.printf("Join type: %s\n",joinItem.getJoinType().toString());
                        System.out.printf("table: %s, alias: %s\n",joinItem.getTable().toString(),(joinItem.getTable().getAliasClause() !=null)?joinItem.getTable().getAliasClause().toString():"");
                        tableKey=join.getTable().getAliasClause()==null?join.getTable().toString():join.getTable().getAliasClause().toString();
                    	tables.put(tableKey,join.getTable());
                        if (joinItem.getOnCondition() != null){
                            System.out.printf("On: %s\n",joinItem.getOnCondition().toString());
                        }else  if (joinItem.getUsingColumns() != null){
                            System.out.printf("using: %s\n",joinItem.getUsingColumns().toString());
                        }
                    }

                    break;
                default:
                    System.out.println("unknown type in join!");
                    break;
            }
        }
	}

	

	public Map<String, TTable> getTables() {
		return tables;
	}



	public void setTables(Map<String, TTable> tables) {
		this.tables = tables;
	}



	public TResultColumnList getColumns() {
		return columns;
	}

	public void setColumns(TResultColumnList columns) {
		this.columns = columns;
	}

	public TSelectDistinct getDistinct() {
		return distinct;
	}

	public void setDistinct(TSelectDistinct distinct) {
		this.distinct = distinct;
	}

	public TWhereClause getCondition() {
		return condition;
	}

	public void setCondition(TWhereClause condition) {
		this.condition = condition;
	}

	public TOrderBy getOrderby() {
		return orderby;
	}

	public void setOrderby(TOrderBy orderby) {
		this.orderby = orderby;
	}

	public TGroupBy getGroupby() {
		return groupby;
	}

	public void setGroupby(TGroupBy groupby) {
		this.groupby = groupby;
	}
	
    private String tablesToString(){
    	StringBuilder text=new StringBuilder(); 
    	Iterator<Map.Entry<String, TTable>> it=tables.entrySet().iterator();
    	while(it.hasNext()){
    		Entry<String, TTable> en=it.next();
    		text.append(en.getValue().toString()).append(",");
    	}
    	text.delete(text.length()-1, text.length());
    	return text.toString();
    }
    public String columnsToString(){
    	StringBuilder text=new StringBuilder();
    	TResultColumn resultColumn;
    	for(int i=0;i<columns.size();i++){
    		resultColumn =columns.getResultColumn(i);
    		text.append(stripParanthesis( resultColumn.getExpr().toString())).append(",");
    	}
    	text.delete(text.length()-1, text.length());
    	if(text.toString().equals("*") && resultColumnNames != null){
    		text=new StringBuilder();
    		for(String columnNames:resultColumnNames){
    			text.append(columnNames).append(',');
    		}
    		text.delete(text.length()-1, text.length());
    	}
    	return text.toString();
    }
        /*Input : select * from <table1>, <table2> where <col1>=<col2> groupby <col1> having
	<col1>=50 orderby <col2>;
	Output:
	Querytype:select
	Tablename:<table1>,<table2>
	Columns:<col1>,<col2>,..
	Distinct:NA
	Condition:<col1>=<value>
	Orderby:<col2>
	Groupby:<col1>
	Having:<col1>=50*/
	public String toString(){
		StringBuilder text=new StringBuilder();
		text.append("Querytype:");
		text.append(queryType.getName()).append('\n');
		
		text.append("Tablename:");
		text.append(tablesToString()).append('\n');
		
		text.append("Columns:");
		text.append(columnsToString()).append('\n');
		
		text.append("Distinct:");
		text.append(distinct==null?"NA":distinctString==null?columnsToString():distinctString).append('\n');
		
		text.append("Condition:");
		text.append(condition==null?"NA":condition.getCondition().toString()).append('\n');
		//condition.getCondition().get
		
		text.append("Orderby:");
		text.append(orderby==null?"NA":orderby.getItems().toString()).append('\n');
		
		text.append("Groupby:");
		text.append(groupby==null?"NA":groupby.getItems().toString()).append('\n');
		
		text.append("Having:");
		text.append(having==null?"NA":having.toString());
		
		return text.toString();
	}

	public Set<String> getResultColumnNames() {
		return resultColumnNames;
	}

	public void setResultColumnNames(Set<String> resultColumnNames) {
		this.resultColumnNames = resultColumnNames;
	}

	public TExpression getHaving() {
		return having;
	}

	public void setHaving(TExpression having) {
		this.having = having;
	}
	public static String stripParanthesis(String input){
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
	}
}
