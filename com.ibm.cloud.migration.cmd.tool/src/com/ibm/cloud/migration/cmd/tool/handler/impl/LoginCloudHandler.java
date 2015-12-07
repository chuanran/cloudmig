package com.ibm.cloud.migration.cmd.tool.handler.impl;

import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.Scanner;

import org.apache.commons.cli.CommandLine;
import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.domain.CloudOrganization;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import com.heroku.api.HerokuAPI;
import com.ibm.cloud.migration.cmd.tool.handler.AbstractCustomHandler;
import com.ibm.cloud.migration.cmd.tool.utility.AppModelConstants;

public class LoginCloudHandler extends AbstractCustomHandler{
	private static final String PROPERTY_FILE = "env.properties";
	public static final String DESCRIPTION = "Authenticated with the target cloud platform"; 
	public static final String COMMAND_HELP =  new StringBuilder().append(" -t/--target ").append(resource.getString("target")+"\n").append(" -l/--url ").append(resource.getString("url")+"\n").append(" -u/--username ").append(resource.getString("username")+"\n").append(" -p/--password ").append(resource.getString("password")+"\n").append(" -o/--org ").append(resource.getString("org")+"\n").append(" -s/--space ").append(resource.getString("space")+"\n").toString();
	//private static String space;
	protected CloudFoundryClient cfClient;

	public LoginCloudHandler(){		
	}
	
	
	public String getHandlerName()
	  {
	    return "LoginCloud";
	  }
	
	public String getDescription(){
		return DESCRIPTION;
	}
	public String getOptionHelp(){
		
	    return COMMAND_HELP;
	}
	
	public void handle(CommandLine cl) throws Exception{
		if(!check(cl))
			return;
		
		String username = (cl.getOptionValue("u"));
		String password = (cl.getOptionValue("p"));
		
		String target_cloud = (cl.getOptionValue("t"));
		
		if(target_cloud ==null){
			System.out.println("list common cloud platforms ......");
			String[] platforms = AppModelConstants.CLOUD_PLATFORM;
			for(int i=1; i<=platforms.length; i++){
				System.out.println(i+"."+platforms[i-1]);
			}
			System.out.println("select one cloud platform to log in:");
			Scanner reader_platform=new Scanner(System.in);
			String index_platform = reader_platform.nextLine();
			target_cloud = platforms[Integer.parseInt(index_platform)-1];
		}
		if(target_cloud.equalsIgnoreCase("cf")){
			System.out.println("log in Cloud Foundry ......");
			String default_target_cf_url = "http://api.ng.bluemix.net";
			String target_url = (cl.getOptionValue("l"));
			String org_name = (cl.getOptionValue("o"));
			String space_name = (cl.getOptionValue("s"));
			CloudCredentials cred = null;
			if(target_url == null){
				System.out.println("target> <default:http://api.ng.bluemix.net>");					
				Scanner reader=new Scanner(System.in);
				target_url = reader.nextLine();
				if(target_url.isEmpty()){
					target_url = default_target_cf_url;
				} 
			
			}
			if(username == null){
				System.out.println("Email: ");
				Scanner reader_username=new Scanner(System.in);
				username = reader_username.nextLine();
//				System.out.println("Password: ");
//				Scanner reader_password=new Scanner(System.in);
//				password = reader_password.nextLine();
				Console con=System.console();
				password=new String(con.readPassword("Password <typing will be hidden>: "));
			}
			cred = new CloudCredentials(username, password);
			if(org_name == null){
				//System.out.println(target_url);
				cfClient = new CloudFoundryClient(cred, new URL(target_url));
				OAuth2AccessToken token = cfClient.login();
				System.out.println("Listing organizations: ");
				for(int i =1; i<=cfClient.getOrganizations().size(); i++){
					System.out.println(i+ ". "+ cfClient.getOrganizations().get(i-1).getName());
				}
				System.out.println("Choose an organization: ");
				Scanner reader_org=new Scanner(System.in);
				String org_index = reader_org.nextLine();
				org_name = cfClient.getOrganizations().get(Integer.parseInt(org_index)-1).getName();
				
				System.out.println("Listing spaces: ");
				for(int i =1; i<=cfClient.getSpaces().size(); i++){
					System.out.println(i+ ". "+ cfClient.getSpaces().get(i-1).getName());
				}
				System.out.println("Choose an space: ");
				Scanner reader_space=new Scanner(System.in);
				String space_index = reader_space.nextLine();
				space_name = cfClient.getSpaces().get(Integer.parseInt(space_index)-1).getName();
			}
			
			
			/*if(target_url != null && target_url.trim().length()>0){
				cfClient = new CloudFoundryClient(cred, new URL(target_url));
			  } else{
					System.out.println("target> <default:http://api.ng.w3.bluemix.net>");					
					Scanner reader=new Scanner(System.in);
					String answer = reader.nextLine();
					if(answer.isEmpty()){
						target_url = default_target_url;
					}
				
				//cfClient = new CloudFoundryClient(cred, new URL(target_url));
					String org_name = "netflix";
					String space_name = "dev";
				cfClient = new CloudFoundryClient(cred, new URL(target_url), org_name, space_name);
			  }*/
			
			
			cfClient = new CloudFoundryClient(cred, new URL(target_url), org_name, space_name);
			
			
			
			//Add the target url to the resource.properties file
			Properties prop = new Properties();
			try{
				InputStream fis = new FileInputStream(PROPERTY_FILE);
				prop.load(fis);
				OutputStream fos = new FileOutputStream(PROPERTY_FILE);
				prop.setProperty("cf_url", target_url);
				prop.store(fos, "Update '" + "cf_url" + "' value");
			} catch (IOException e) {
				e.printStackTrace();
		         System.err.println("Visit "+"env.properties"+" for updating "+"cf_url"+" value error");
	        }
			OAuth2AccessToken token = cfClient.login();			
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(new File("cfuser.json"), token);
			
			System.out.println("Log in Cloud Foundry : success!");
			
		  } else if(target_cloud.equalsIgnoreCase("heroku")){
			  System.out.println("log in Heroku ......");
			  
			  if(username == null){
					System.out.println("Email: ");
					Scanner reader=new Scanner(System.in);
					username = reader.nextLine();
//					System.out.println("Password: ");
//					Scanner reader_password=new Scanner(System.in);
//					password = reader_password.nextLine();
					Console con=System.console();
					password=new String(con.readPassword("Password <typing will be hidden>: "));
				}
			  
			  String apiKey = HerokuAPI.obtainApiKey(username, password);
			  
			  //add the heroku key into env.properties file for later use
			  Properties prop = new Properties();
				try{
					InputStream fis = new FileInputStream(PROPERTY_FILE);
					prop.load(fis);
					OutputStream fos = new FileOutputStream(PROPERTY_FILE);
					prop.setProperty("heroku_api_key", apiKey);
					prop.store(fos, "Update '" + "heroku_api_key" + "' value");
				} catch (IOException e) {
					e.printStackTrace();
			         System.err.println("Visit "+"env.properties"+" for updating "+"heroku_api_key"+" value error");
		        }
				
				System.out.println("Log in Heroku : success!");
		  }

	}
	
	 private boolean check(CommandLine cl) {
		 boolean checkFlag = true;
		 String errMsg = "";
		 
//		 if (!cl.hasOption("t")) {
//		      checkFlag = false;
//		      System.out.println();
//		      System.out.println("Miss '-t/--target'.");
//		      System.out.println("  -t, --target cloud platform		Platform type");
//		      System.out.println();
//		      System.out.println("Other options can see 'mig login -h' for help.");
//		      System.out.println();
//		      return false;
//		      //errMsg = new StringBuilder().append(errMsg).append(resource.getString("target")).toString();
//		 }
		 
		 //the target url parameter can be empty, there should be a default one
//		 if (!cl.hasOption("l")) {
//		      checkFlag = false;
//		      errMsg = new StringBuilder().append(errMsg).append(resource.getString("url")).toString();
//		 }

		 if (!checkFlag) {
			 if (errMsg != "") {
			        errMsg = MessageFormat.format(resource.getString("missingOption"), new Object[] { errMsg });
			 }
		 }
		 return true;
	 }
}

