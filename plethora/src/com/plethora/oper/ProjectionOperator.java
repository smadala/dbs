package com.plethora.oper;

import java.util.ArrayList;
import java.util.List;

import com.plethora.obj.Expression;

public class ProjectionOperator {
	
	
/*	public List<List<Object>> project(List<List<Object>> records,List<Expression> exprs){
		List<List<Object>> resultRecords=new ArrayList<>();
		List<Object> resultRecord=new ArrayList<>();
		
		for(List<Object> record:records){
			resultRecord=new ArrayList<>();
			for(Expression expr:exprs){
				//expr eval should happen here... current assumed only one column
				resultRecord.add(record.get(expr.columnPos.get(0)));
			}
			resultRecords.add(resultRecord);
		}
		return resultRecords;
	}*/
	public List<Object> project(List<Object> record,List<Expression> exprs){
		
		if(exprs.size() == 0)
			return record;
		List<Object> resultRecord=new ArrayList<>();
		for(Expression expr:exprs){
			//expr eval should happen here... current assumed only one column
			resultRecord.add(record.get(expr.columnPos));
		}
		return resultRecord;
	}
/*	public static void main(String args[]){
		ProjectionOperator object=new ProjectionOperator();
		List<Object> records1=new ArrayList<>();
		records1.add("HarshaCol1");
		records1.add("HarshaCol2");
		records1.add("HarshaCol3");
		List<Expression> exprs1 = new ArrayList<>();
		Expression ex1=new Expression();
		List<Integer> expInt1=new ArrayList<>();
		expInt1.add(0);
		ex1.columnPos=expInt1;
		exprs1.add(ex1);
		Expression ex2=new Expression();
		List<Integer> expInt2=new ArrayList<>();
		expInt2.add(2);
		ex2.columnPos=expInt2;
		exprs1.add(ex2);
		List<Object>result=object.project(records1, exprs1);
		for(Object r : result){
			System.out.println(r);
		}
	}*/
}
