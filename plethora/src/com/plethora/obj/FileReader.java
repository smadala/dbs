package com.plethora.obj;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;

import com.plethora.mem.ConfigConstants;

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
	
	public static void writeLine(OutputStream os,String line, boolean flush){
		byte [] bytes=new byte[line.length()+1];
		int i=0;
		for(;i<line.length();i++){
			bytes[i] =(byte) line.charAt(i);
		}
		bytes[i]=(byte) '\n';
		try {
			os.write(bytes);
			if(flush)
				os.flush();
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
		return new File(ConfigConstants.PATH_FOR_DATA, tableName+ConfigConstants.TABLE_DATA_FILE_EXTENSION);
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
	
	
}
