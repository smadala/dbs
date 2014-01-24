package com.plethora.mem;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class LRUMemory<K,V> {
	private int capacity;
	private Map<K,V> dataMap;
	private Deque<K> queue;
	
	public LRUMemory(int capacity){
		this.capacity=capacity;
		this.dataMap=new HashMap<K,V>(capacity);
		this.queue=new LinkedList<K>();
		
	}
	
	public V get(K key){
		return dataMap.get(key);
	}
	
	public void put(K key, V value){
		
		if(queue.size() >= capacity){
			K oldKey =queue.peekLast();
			dataMap.remove(oldKey);
		}
		
		queue.remove(key);
		queue.addFirst(key);
		dataMap.put(key, value);
	}

}
