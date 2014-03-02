package com.plethora.obj;

public enum ComparisonOperator {
   EQUAL, 
   GREATER_AND_EQUAL,
   LESSTHAN_AND_EQUAL,
   NOT_EQUAL, 
   GREATERTHAN, 
   LESSTHAN,
   LIKE;
   public boolean eval(ComparisonOperator oper, Object o1, Object o2){
	   switch(oper){
	   case EQUAL: 
		   return o1.equals(o2);
	   case GREATER_AND_EQUAL:
		   int diff=-1;
		   if( o1 instanceof Integer)
		   		diff =((Integer)o1).compareTo((Integer) o2);
           else if( o1 instanceof Float)
        	   diff =((Float)o1).compareTo((Float) o2);
           else if( o1 instanceof String)
        	   diff =((String)o1).compareTo((String) o2);
		   
		   return diff >=0;
	   }
	   return true;
   }
}
