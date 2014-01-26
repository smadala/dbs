package com.plethora.mem;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class LRUMemory<K,V> {
	private int capacity;
	private Map<K,V> dataMap;
	private Deque<K> queue;
	private Map<K,Integer> posMap;
	private int pos;
	//private number
	
	public LRUMemory(int capacity){
		this.capacity=capacity;
		dataMap=new HashMap<K,V>(capacity);
		queue=new LinkedList<K>();
		pos=0;
		posMap=new HashMap<K,Integer>(capacity);
	}
	
	public V get(K key,boolean makeRecentlyUsed){
		V value=dataMap.get(key);
		//update recently accessed page
		if(makeRecentlyUsed && value != null){
			put(key,value);
		}
		return value;
	}
	
	public int put(K key, V value){
		int replace=-1;
			
		if(dataMap.size() >= capacity && !dataMap.containsKey(key)){
			K oldKey =queue.pollLast(); // remove old page
			dataMap.remove(oldKey);
			replace=posMap.get(oldKey); //update frame number
			posMap.remove(oldKey);
			posMap.put(key, replace);
		}else if( dataMap.size() < capacity && !dataMap.containsKey(key)){
			posMap.put(key, pos);
			replace=pos;
			pos++;
		}
		
		queue.remove(key); //make given key is LRU page
		queue.addFirst(key);
		dataMap.put(key, value);
		/*for(K s:queue){
			System.out.print(s+"   ");
		}
		System.out.println();*/
		return replace;
	}

}
