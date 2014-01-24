package com.plethora.obj;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;

public class Dummy {
String readLine(InputStream stream)
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
	
	
}
