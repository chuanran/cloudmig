package com.ibm.cloud.migration.utility;

import java.io.File;
import java.util.Scanner;

public class FileMove {
	
	private String destPath=".";
	
	public FileMove() {
		// TODO Auto-generated constructor stub
	}
	
	//move file
	public boolean moveFile(String srcFile, String destPath) {
		File file=new File(srcFile);
		File dir=new File(destPath);
		boolean success = file.renameTo(new File(dir, file.getName())); 
		return success; 
	}
	
	//read file
	public void readFile(String srcPath) {
		File file=new File(srcPath);
		String[] srcFiles=file.list();
		for(int i=0;i<srcFiles.length;i++){
			String type=srcFiles[i].substring(srcFiles[i].length()-3, srcFiles[i].length());
			if(type.equals("zip")){
				String srcFile=file.getAbsolutePath()+File.separator+srcFiles[i];
				moveFile(srcFile, destPath);
			}
		}
	}
	
	/*
	public static void main(String[] args) {
		
		System.out.println("Please Enter the srcPath:");
		Scanner reader=new Scanner(System.in);
		String srcPath = reader.nextLine();
		FileMove fm=new FileMove();
		fm.readFile(srcPath);
	}
	*/

}
