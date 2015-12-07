package com.ibm.cloud.migration.heroku;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProcessExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ProcessBuilder builder = new ProcessBuilder();
		String path = System.getenv("PATH");
//		System.out.println(path);
		
		
		builder.command("heroku.bat", "config","-app","countly-api-2013");
//		builder.command("heroku.bat");
		try {
			Process proc = builder.start();
			BufferedReader error = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
			BufferedReader out = new BufferedReader(new InputStreamReader(proc.getInputStream()));
//			while(error.ready()||out.ready()){
//				if(error.ready()){
//					System.err.println(error.readLine());
//				}
//				if(out.ready()){
//					System.err.println(out.readLine());
//				}
//			}
			String line = out.readLine();
			while(line!=null){
				System.out.println(line);
				line = out.readLine();
					
			}
			line = error.readLine();
			while(line!=null){
				System.out.println(line);
				line = error.readLine();
					
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
