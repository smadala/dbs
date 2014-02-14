package com.plethora.obj;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/*In the WHERE clause, we can have only the following comparison clauses:
For Integer and Float, <, >, >=, <=, ==, <>
For Strings, LIKE ( eg. Name LIKE 'Student' )
these clauses will be combined only using "AND" and "OR"*/ 
//hello
public enum DataType{
	INTEGER("integer"), FLOAT("float"), VARCHAR("varchar");
	private String name;
	private static Map<DataType, Set<String>> validOperations;
	/*static {
		Set<String> intValidOperation = new HashSet<>(Arrays.asList("<", ">",
				">=", "<=", "==", "<>"));
		Set<String> floatValidOperation = new HashSet<>(Arrays.asList("<", ">",
				">=", "<=", "==", "<>"));
		Set<String> varcharValidOperation = new HashSet<>(Arrays.asList("LIKE"));
		validOperations.put(INTEGER, intValidOperation);
		validOperations.put(FLOAT, floatValidOperation);
		validOperations.put(VARCHAR, varcharValidOperation);
	}*/

	private DataType(String name) {
		this.name = name;
	}

	public boolean isValidOperation(DataType type, String oper) {
		// TODO trim oper
		Set<String> typeOpearion = validOperations.get(type);
		return typeOpearion.contains(oper);
	}
	public static DataType isValidDataType(String x){
		if(x.equals(FLOAT.toString().toLowerCase())){
			return DataType.FLOAT;
		}
		else if(x.equals(INTEGER.toString().toLowerCase())){
			return DataType.INTEGER;
		}
		else if(x.matches("varchar\\([0-9]+\\)")){
			return DataType.VARCHAR;
		}
		else
			return null;
	}
}
