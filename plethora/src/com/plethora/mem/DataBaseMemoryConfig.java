package com.plethora.mem;

import java.util.HashMap;
import java.util.Map;

public class DataBaseMemoryConfig {
	public static int NUM_OF_PAGES;
	public static int PAGE_SIZE;
	public static String PATH_FOR_DATA;
	public static final String TABLE_BEGIN="BEGIN";
	public static final String TABLE_END="END";
	public static final String PROPS_DELIMITER="\\s";
	public static Map<String,String> memoryProps=new HashMap<String,String>();
}
