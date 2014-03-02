package com.plethora.oper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.plethora.obj.ComparisonOperator;
import com.plethora.obj.Condition;
import com.plethora.obj.LogicalOperator;

public class SelectionOperator {
	
	/*public List<List<Object>> select(List<List<Object>> records, List<Condition> conditions, 
			List<LogicalOperator> logicalOper){
		List<List<Object>> resultRecords=new ArrayList<>();
		for(List<Object> record:records){
			for(int i=0;i<conditions.size();i++){
				
			}
		}
		return null;
	}*/
	public List<Object> select(List<Object> record, List<Condition> conditions, 
			List<LogicalOperator> logicalOper){
		if(conditions==null || conditions.isEmpty()==true)
			return record;
		Iterator<Condition> conditionIterator=conditions.iterator();
		Iterator<LogicalOperator> logOperIterator;
		boolean logicalOutput[]=new boolean[conditions.size()];;
		boolean decider=false;
		Condition condition;
		int position=0;
		int track=0;
		Object attribute;
		while(conditionIterator.hasNext()){
			 condition=conditionIterator.next();
			 position=condition.AttributePos;
			 attribute=record.get(position);
			logicalOutput[track]=ComparisonOperator.eval(condition.operator, attribute, condition.val);
			 track=track+1;
		}
		LogicalOperator logicalOperator;
		track=track-1;
		int i=0;
		if(track!=-1 && logicalOper!=null && logicalOper.isEmpty()==false){
			logOperIterator=logicalOper.iterator();
			while(logOperIterator.hasNext()){
				logicalOperator=logOperIterator.next();
				switch(logicalOperator){
				case AND:if(i==0){
						decider=logicalOutput[i] && logicalOutput[i+1];
						i=i+2;
					}
					else{
						decider=decider && logicalOutput[i];
						i=i+1;
					}
				break;
				case OR:if(i==0){
						decider=logicalOutput[i] || logicalOutput[i+1];
						i=i+2;
					}
					else{
						decider=decider || logicalOutput[i];
						i=i+1;
					}
				break;
				}
			}
		}
		if(decider==true || track==-1 || track==0)
			return record;
		else
			return null;
	}
	/*public static void main(String args[]){
		List<Object> record=new ArrayList<Object>();
		record.add("harsha");
		record.add(1);
		record.add(2000);
		List<Condition> conditions=new ArrayList<Condition>();
		Condition condi1=new Condition();
		condi1.AttributePos=0;
		condi1.val="harsha";
		condi1.operator=ComparisonOperator.EQUAL;
		conditions.add(condi1);
		Condition condi2=new Condition();
		condi2.AttributePos=1;
		condi2.val=8;
		condi2.operator=ComparisonOperator.GREATERTHAN;
		conditions.add(condi2);
		Condition condi3=new Condition();
		condi3.AttributePos=2;
		condi3.val=8;
		condi3.operator=ComparisonOperator.LESSTHAN;
		conditions.add(condi3);
		
		List<LogicalOperator> logicalOperation=new ArrayList<LogicalOperator>();
		logicalOperation.add(LogicalOperator.AND);
		logicalOperation.add(LogicalOperator.OR);
		SelectionOperator object=new SelectionOperator();
		List<Object> result=object.select(record, conditions, logicalOperation);
		if(result==null){
			System.out.println("oops");
		}
		else{
			System.out.println(result.get(0));
		}
	}*/
}
