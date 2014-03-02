package com.plethora.oper;

import java.util.Comparator;
import java.util.List;

import com.plethora.obj.ComparisonOperator;
import com.plethora.obj.OrderBy;

public class OrderByOperator {
	
	
	public void sort(List<List<Object>> records, List<OrderBy> ordeBies){
	
		
	}
	private void sort(List<List<Object>> records, List<OrderBy> ordeBies, int orderbypos){
		
	}
	
	
	public void mergeList(){
		
	}
	
	public class OrderBySort implements Comparator<List<Object>>{
		private List<OrderBy> orderBies;
		public OrderBySort(List<OrderBy> ordeBies){
			this.orderBies=ordeBies;
		}

		@Override
		public int compare(List<Object> o1, List<Object> o2) {
			// TODO Auto-generated method stub
			int diff=0;
			for(int i=0;i<orderBies.size();i++){
				if( (diff=compare(o1, o2, orderBies.get(i))) != 0) // until same  for all values 
					break;
			}
			return diff;
		}
		private int compare(List<Object> o1, List<Object> o2, OrderBy orderBy){
			int diff;
			Object o1i=o1.get(orderBy.attributePos);
			Object o2i=o1.get(orderBy.attributePos);
			diff=ComparisonOperator.comapre(o1i, o2i);
			if(orderBy.desc)
				diff=diff*-1; //make reverse
			return diff;
		}
	}
}
