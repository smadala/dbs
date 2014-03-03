package com.plethora.oper;

import java.io.OutputStream;
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
	int sublistId=1000;
	List<Table> subTables;
	List<OrderBy> orderBies;
	Table resultTable;
	
	public void sort(List<List<Object>> records, List<OrderBy> ordeBies){
		Comparator<List<Object>> comparator=new OrderBySort(ordeBies);
		Collections.sort(records, comparator);
	}
	public String getFileName(){
		StringBuilder fileName=new StringBuilder();
		fileName.append(table.getTableName()).append('_').append(sublistId++);
		return fileName.toString();
	}
	public void createSortFile(List<Page> pages){
		
		String fileName=getFileName();
		//subListFileNames.add(FileReader.getTableDataFile(fileName).getAbsolutePath());
		OutputStream oStream = FileReader.getTableOutputStream(fileName);
		List<List<Object>> records=new ArrayList<>();
		for(Page page:pages){
			records.addAll(page.getRecords());
		}
		sort(records,orderBies);
		Table auxTable=FileReader.createTable(table, records);
		subTables.add(auxTable);
		//FileReader.writePage(records, oStream);
	}
	
	public void merge(){
		//implement n phase merge currently two phase
		resultTable=merge(subTables);
	}
	
	public Table merge(List<Table> subTables){
		int size=subTables.size();
		List<TableIterator> tableIterators=new ArrayList<>(size);
		Table resultTable=null;
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
		
		
		while(pq.isEmpty()){
			mergeRecord=pq.poll();
			tableWriter.write(mergeRecord.record);
			record=tableIterators.get(mergeRecord.subTableIndex).getNext();
			if(record != null){
			pq.add( new MergeRecord( record, mergeRecord.subTableIndex));
			}
		}
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
			Object o2i=o1.get(orderBy.attributePos);
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
			Object o2i=o1.get(orderBy.attributePos);
			diff=ComparisonOperator.comapre(o1i, o2i);
			if(orderBy.desc)
				diff=diff*-1; //make reverse
			return diff;
		}
	}
}
