package com.plethora.obj;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.plethora.mem.ConfigConstants;
import com.plethora.mem.DataBaseMemoryConfig;

public class FileReader {
	public static String readLine(InputStream stream)
	{
		char line[]=new char[64];
		int i=0,size=64,status=0;
		char temp;
		try{
		while((i=stream.read())!=-1)
		{
			temp=(char)i;
			if(temp=='\n')
			{
				return new String(line,0,status);
			}
			else
			{
				if(size>status)
				{
					line[status]=temp;
					status=status+1;
				}
				else
				{
					line=Arrays.copyOf(line, size*2);
					line[status]=temp;
					status=status+1;
					size=size*2;
				}
			}
		}
	}
		catch(Exception e){
			e.printStackTrace();
		}
		if(status>0)
			return new String(line,0,status);
		else
			return null;
	}
	
	public static void writeLine(RandomAccessFile randomeAccessFile,String line){
		try {
			char ch;
			boolean addNewLineAtStart=false;
			int extraCharacters=1;
			
			randomeAccessFile.seek(randomeAccessFile.length()-2);
			if( (ch = (char) randomeAccessFile.readByte( )) != '\n'){
			    extraCharacters++;
			    addNewLineAtStart=true;
			}
			//System.out.println(ch);
			int length = line.length()+extraCharacters;
			byte [] bytes=new byte[length];
			int i=0;
			if(addNewLineAtStart)
				bytes[i++]='\n';
			
			for(int k=0;k<line.length();k++){
				bytes[i++] =(byte) line.charAt(k);
			}
			
			bytes[i]=(byte) '\n';
			randomeAccessFile.write(bytes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void writeLine(OutputStream outputStream, String line){
		byte[] bytes=new byte[line.length()+1];
		int i;
		for(i=0;i<line.length();i++){
			bytes[i] = (byte) line.charAt(i); 
		}
		bytes[i]=(byte) '\n';
		try {
			outputStream.write(bytes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//public static String getFile
	public static InputStream getTableInputStream(String tableName){
		File dataFile = getTableDataFile(tableName);
		InputStream is=null;
		try {
			 is = new FileInputStream(dataFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return is;
	}
	
	public static OutputStream getTableOutputStream(String tableName){
		File dataFile = getTableDataFile(tableName); 
		OutputStream os=null;
		try {
			 os = new FileOutputStream(dataFile,true);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return os;
		
	}
	public static File getTableDataFile(String tableName){
		return new File(DataBaseMemoryConfig.PATH_FOR_DATA, tableName+ConfigConstants.TABLE_DATA_FILE_EXTENSION);
	}
	
	
	
	public static RandomAccessFile getRandomAccessFile(String tableName,String mode){
		
		try {
			RandomAccessFile randomAccessFile = new RandomAccessFile(getTableDataFile(tableName), mode);
			return randomAccessFile;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static String toString(List<Object> record){
		
		StringBuilder text=new StringBuilder();
		for(int i=0;i<record.size()-1;i++){
			text.append('"').append(record.get(i)).append('"').append(',');
		}
		text.append('"').append(record.get(record.size()-1)).append('"');
		return text.toString();
	}
	
	public static void writePage(Page page,OutputStream oStream){
		for(List<Object> record:page.getRecords()){
			System.out.println(record.toString());
			writeLine(oStream, toString(record));
		}
	}
	public static void writePage(List<List<Object>> records,OutputStream oStream){
		for(List<Object> record:records){
			writeLine(oStream, toString(record));
		}
	}
	

	public static Table cloneTable(Table table, String tableName){
		Table clTable= new Table(tableName);
		clTable.setFields(table.getFields());
		return clTable;
	}
	
	public static List<Object> getTuple(String record,List<FieldType> types){
		
		List<Object> tuple=new ArrayList<>();
		List<String> tokens=getTokens(record);
		FieldType field=null;
		Object val=null; String token;
		
		for(int i=0;i<types.size();i++){
			token = tokens.get(i);
			field=types.get(i);
			switch(field.getType()){
			case INTEGER:
				val=Integer.parseInt(token);
				break;
			case VARCHAR:
				val=token;
				break;
			case FLOAT:
				val=Float.parseFloat(token);
				break;
			}
			tuple.add(val);
		}
		return tuple;
	}
	public static List<String> getTokens(String record){
		List<String> tokens=new ArrayList<>();
		boolean inQuotes=false;
		int len=record.length();
		int begin=1,end=0;
		for(int i=0;i<len;i++){
			
			if(record.charAt(i) == '"'){
				    
					inQuotes=!inQuotes;
					continue;
			}
			if( record.charAt(i) == ',' && !inQuotes){
				tokens.add(record.substring(begin,i-1));
				begin=i+2;
			}
		}
		tokens.add(record.substring(begin,len-1));
		return tokens;
	}
	public static String getCacheKey(String tableName, int pageNumber ){
		return MessageFormat.format(DataBaseMemoryConfig.LRU_MEMORY_KEY_FORMAT, tableName,
				pageNumber);
	}
}
