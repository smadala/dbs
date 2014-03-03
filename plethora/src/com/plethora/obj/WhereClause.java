package com.plethora.obj;

import java.util.ArrayList;
import java.util.List;

public class WhereClause {
	public List<Condition> conditions;
	public List<LogicalOperator> logicalOperators;
	public WhereClause(){
		conditions=new ArrayList<>();
		logicalOperators=new ArrayList<>();
	}
}
