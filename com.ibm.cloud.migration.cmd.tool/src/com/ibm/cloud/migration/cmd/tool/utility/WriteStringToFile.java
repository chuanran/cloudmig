package com.ibm.cloud.migration.cmd.tool.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class WriteStringToFile {
	public static void writeStrToFile(String str, String file_path) throws IOException{
	File f=new File(file_path);
	if(!f.exists()){
	f.createNewFile();
	}
	byte bytes[]=new byte[512];
	bytes=str.getBytes();  
	int b=str.length();  
	FileOutputStream fos=new FileOutputStream(f);
	fos.write(bytes,0,b);
	fos.close();
	}
//	public static void main(String[] agrs){
//		String file_path="E:/material/hello.txt";
//		try {
//			WriteStringToFile.writeStrToFile("hello world", file_path);
//		} catch (IOException e) {
//			// TODO 自动生成 catch 块
//			e.printStackTrace();
//		}
//	}
}
