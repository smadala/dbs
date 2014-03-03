package com.plethora.obj;

import java.util.List;

public class Select {
	
	public Table table;
	public List<OrderBy> orderBies;
	public List<Expression> projCols;
	public WhereClause where;
	public WhereClause having;
}
