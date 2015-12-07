package com.ibm.cloud.migration.cmd.tool.handler.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;


import org.apache.commons.cli.CommandLine;
import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.domain.CloudApplication;

import com.heroku.api.App;
import com.heroku.api.HerokuAPI;
import com.ibm.cloud.migration.cmd.tool.handler.AbstractCustomHandler;
import com.ibm.cloud.migration.cmd.tool.utility.AppModelConstants;
import com.ibm.cloud.migration.cmd.tool.utility.RetrieveCloudCred;

public class LogoutHandler extends AbstractCustomHandler{	
	private static final String PROPERTY_FILE = "env.properties";
	
	public static final String COMMAND_HELP = new StringBuilder().append(" -t/--target ").append(resource.getString("target")).toString();
	
	public static final String DESCRIPTION = "Log out the target cloud platform";
	public LogoutHandler(){		
	}
	
	public String getHandlerName()
	  {
	    return "Log out";
	  }
	
	public String getOptionHelp(){
		
	    return COMMAND_HELP;
	}
	
	public String getDescription(){
		return DESCRIPTION;
	}
	
	public boolean deleteFile(String sPath) {  
	    boolean flag = false;  
	    File file = new File(sPath);  
	     
	    if (file.isFile() && file.exists()) {  
	        file.delete();  
	        flag = true;  
	    }  
	    return flag;  
	}  
	
	public void handle(CommandLine cl) throws Exception{
		String target_cloud = cl.getOptionValue("t");
		if(target_cloud == null){
			System.out.println("list common cloud platforms ......");
			String[] platforms = AppModelConstants.CLOUD_PLATFORM;
			for(int i=1; i<=platforms.length; i++){
				System.out.println(i+"."+platforms[i-1]);
			}
			System.out.println("select one cloud platform to log out:");
			Scanner reader_platform=new Scanner(System.in);
			String index_platform = reader_platform.nextLine();
			target_cloud = platforms[Integer.parseInt(index_platform)-1];
		}
		//System.out.println(target_cloud);
		if(target_cloud.equalsIgnoreCase("cf")){
			deleteFile("cfuser.json");
		}else if(target_cloud.equalsIgnoreCase("heroku")){
			//read the heroku_api_key from env.properties(which was generated by the first login to the cloud) under project
			Properties prop = new Properties();
			InputStream fis = new FileInputStream(PROPERTY_FILE);
			prop.load(fis);
			OutputStream fos = new FileOutputStream(PROPERTY_FILE);
			prop.setProperty("heroku_api_key","");
			prop.store(fos, "Update '" + "heroku_api_key" + "' value");
		}
	}
	
}
