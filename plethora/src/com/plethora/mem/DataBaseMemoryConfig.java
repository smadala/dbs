package com.plethora.mem;

import com.plethora.obj.Page;


public class DataBaseMemoryConfig {
	public static int NUM_OF_PAGES;
	public static int PAGE_SIZE;//Page Size in bytes.
	public static  String PATH_FOR_DATA;
	public static String PATH_FOR_CONF_FILE;
	public static LRUMemory<String, Page> cachedPages;
	public static final String LRU_MEMORY_KEY_FORMAT = "{0}_{1}";
}
