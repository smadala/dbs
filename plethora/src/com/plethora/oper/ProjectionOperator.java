package com.plethora.oper;

import java.util.ArrayList;
import java.util.List;

import com.plethora.obj.Expression;

public class ProjectionOperator {
	
	
	public List<List<Object>> project(List<List<Object>> records,List<Expression> exprs){
		List<List<Object>> resultRecords=new ArrayList<>();
		List<Object> resultRecord;
		for(Expression expr:exprs){
			resultRecords.add(new ArrayList<>());
		}
		for(List<Object> record:records){
			resultRecord=new ArrayList<>();
			for(Expression expr:exprs){
				//expr eval should happen here... current assumed only one column
				resultRecord.add(record.get(expr.columnNames.get(0)));
			}
			resultRecords.add(resultRecord);
		}
		return resultRecords;
	}
}
