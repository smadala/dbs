package com.plethora.oper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import com.plethora.obj.ComparisonOperator;
import com.plethora.obj.FileReader;
import com.plethora.obj.MergeRecord;
import com.plethora.obj.OrderBy;
import com.plethora.obj.Page;
import com.plethora.obj.Table;

public class OrderByOperator {
	
	Table table;
	int sublistId;
	List<Table> subTables;
	List<OrderBy> orderBies;
	Table resultTable;
	
	public OrderByOperator(Table table, List<OrderBy> orderBies){
		this.table=table;
		this.orderBies=orderBies;
		subTables=new ArrayList<>();
		sublistId=1000;
	}
	public void sort(List<List<Object>> records, List<OrderBy> orderBies){
		if(orderBies.size() == 0)
			return;
		Comparator<List<Object>> comparator=new OrderBySort(orderBies);
		Collections.sort(records, comparator);
	}
	
	public String getTemporaryTableName(){
		StringBuilder fileName=new StringBuilder();
		fileName.append(table.getTableName()).append('_').append(sublistId++);
		return fileName.toString();
	}
	
	public void createSortFile(List<List<Object>> records){
		
		String tableName=getTemporaryTableName();
		sort(records,orderBies);
		Table auxTable=FileReader.cloneTable(table, tableName);
		subTables.add(auxTable);
		writeRecords(auxTable, records);
	}
	public void writeRecords(Table table,List<List<Object>> records){
		TableWriter tableWriter=new TableWriter(table);
		for(List<Object> record:records){
			tableWriter.write(record);
		}
		tableWriter.close();
	}
	
	public void merge(){
		//implement n phase merge currently two phase
		merge(subTables);
		
	}
	
	public Table merge(List<Table> subTables){
		int size=subTables.size();
		List<TableIterator> tableIterators=new ArrayList<>(size);
		String resultTableName=getTemporaryTableName();
		resultTable= FileReader.cloneTable(table, resultTableName);
		TableWriter tableWriter=new TableWriter(resultTable);
		for(Table table:subTables){
			tableIterators.add(new TableIterator(table));
		}
		MergeRecord mergeRecord;
		List<Object> record;
		PriorityQueue<MergeRecord> pq=new PriorityQueue<>(size, new MergeLineOrderBySort(orderBies));
		for(int i=0;i<size;i++){
			record=tableIterators.get(i).getNext();
			if(record != null){
			pq.add( new MergeRecord( record, i));
			}
		}
		while(!pq.isEmpty()){
			
			mergeRecord=pq.poll();
			tableWriter.write(mergeRecord.record);
			record=tableIterators.get(mergeRecord.subTableIndex).getNext();
			if(record != null){
			pq.add( new MergeRecord( record, mergeRecord.subTableIndex));
			}
		}
		tableWriter.close();
		return resultTable;
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
			Object o2i=o2.get(orderBy.attributePos);
			diff=ComparisonOperator.comapre(o1i, o2i);
			if(orderBy.desc)
				diff=diff*-1; //make reverse
			return diff;
		}
	}
	
	public class MergeLineOrderBySort implements Comparator<MergeRecord>{
		private List<OrderBy> orderBies;
		public MergeLineOrderBySort(List<OrderBy> orderBies){
			this.orderBies=orderBies;
		}

		@Override
		public int compare(MergeRecord m1, MergeRecord m2) {
			List<Object> o1=m1.record , o2=m2.record;
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
			Object o2i=o2.get(orderBy.attributePos);
			diff=ComparisonOperator.comapre(o1i, o2i);
			if(orderBy.desc)
				diff=diff*-1; //make reverse
			return diff;
		}
	}
	
	public boolean isMultiPhaseSort(){
		return !subTables.isEmpty();
	}
	public Table getResultTable() {
		return resultTable;
	}
	public void setResultTable(Table resultTable) {
		this.resultTable = resultTable;
	}
	
}
