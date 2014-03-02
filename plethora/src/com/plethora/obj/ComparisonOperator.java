package com.plethora.obj;

public enum ComparisonOperator {
   EQUAL, 
   GREATER_AND_EQUAL,
   LESSTHAN_AND_EQUAL,
   NOT_EQUAL, 
   GREATERTHAN, 
   LESSTHAN,
   LIKE;
   public static boolean eval(ComparisonOperator oper, Object o1, Object o2){
	   switch(oper){
	   case EQUAL: 
		   return comapre(o1, o2) == 0;
	   case GREATER_AND_EQUAL:
		   return comapre(o1, o2) >= 0;
	   case LESSTHAN_AND_EQUAL:
		   return comapre(o1, o2) <= 0;
	   case NOT_EQUAL:
		   return comapre(o1, o2) != 0;
	   case GREATERTHAN:
		   return comapre(o1, o2) > 0;
	   case LESSTHAN:
		   return comapre(o1, o2) < 0;
	   case LIKE:
		   return o1.toString().equalsIgnoreCase(o2.toString());
	   }
	   return true; 
   }
   
   public static int comapre(Object o1,Object o2){
	   int diff=0;
	   if( o1 instanceof Integer)
	   		diff =((Integer)o1).compareTo((Integer) o2);
       else if( o1 instanceof Float)
    	   diff =((Float)o1).compareTo((Float) o2);
       else if( o1 instanceof String)
    	   diff =((String)o1).compareTo((String) o2);
	   return diff;
   }
}
