package com.ibm.cloud.migration.heroukutocf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import com.heroku.api.App;
import com.ibm.cloud.migration.appmodel.AppModelException;
import com.ibm.cloud.migration.heroku.Exporter;

public class test {
	
	private static void createDestDirectoryIfNecessary(String destParam) {  
        File destDir = null;  
        if (destParam.endsWith(File.separator)) {  
            destDir = new File(destParam);  
        } else {  
            destDir = new File(destParam.substring(0, destParam.lastIndexOf(File.separator)));  
        }  
        if (!destDir.exists()) {  
            destDir.mkdirs();  
        }  
    }
public static void main(String[] args){
	
	
//	String hi =" hello";
//	System.out.println(hi.length());
//	String app_name ="countly-api-2013,     countly-ui-2013";
//	String[] sels = app_name.split(",");
//	//List<App> selApps = new ArrayList<App>(sels.length);
//	List<String> apps = new ArrayList<String>();
//	apps.add("countly-api-2013");
//	apps.add("countly-ui-2013");
//	if(sels.length==0){
//		System.out.println("nothing to export");
//		return;
//	} else{
//		for(int i=0;i<sels.length;i++){
//			for(String app :apps){
//				if(sels[i].trim().equals(app)){
//					System.out.println(sels[i] + " equals with " + app);
//				}
//			}
//		}
//	}
	System.out.print("hello: ");
	System.out.print("world");
	
	
	
	
//		String param = "\\opt\\hello";
//		System.out.println(param.substring(0, param.lastIndexOf(File.separator)));
//		System.out.println("Enter heroku credentails: ");
//		System.out.println("Email: ");
//		Scanner readers=new Scanner(System.in);
//		String username = readers.nextLine();
//		System.out.println("Password: ");
//		String password =readers.nextLine();
//		Exporter exporter = new Exporter();
//		try {
//			exporter.export(username, password, "hello.xml");
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (AppModelException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	
//	String hi = "api.ng.w3.bluemix.net";
//	String[] hi_arr = hi.split("\\.", 2);
//	String app_domain_postfix = "."+hi_arr[1];
//		System.out.println("hi_r: " + app_domain_postfix);
//		
//		boolean her = false;
//		if(her==true){
//			System.out.println("yes");
//		}
	
//	String hi = "mongodb";
//	String hell="mongo";
//	if(hi.contains(hell)){
//		System.out.println("contains");
//	}
//	String line = "\"start\": \"node api.js\"";	
//	System.out.println(line);
//	String[]  strs = line.split(":");
////	for(String str : strs){
////		System.out.println(str);
////	}
//	
//    String[] strss = strs[1].trim().split("\"node");
////    for(String str : strss){
////    	System.out.println(str);
////    }
//    String[] strsss = strss[1].split("\\.");
//    
//    System.out.println(strsss[0].trim());
//	Properties prop = new Properties();
//	InputStream fis;
//	try {
//		fis = new FileInputStream("vcap_services.properties");
//		prop.load(fis);
//	} catch (FileNotFoundException e1) {
//		// TODO Auto-generated catch block
//		e1.printStackTrace();
//	} catch (IOException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//	
//	String vcap_service_env = prop.getProperty("VCAP_SERVICES");
//	System.out.println("vcap_service_env is: " + vcap_service_env);
//	try {
//		JSONObject jo =new JSONObject(vcap_service_env); 
//		Iterator it = jo.keys();
//		while(it.hasNext()){
//			System.out.println("key is: " + jo.keys().next());
//			String key = (String)it.next();
//			JSONArray arr = jo.getJSONArray(key);
//			System.out.println(arr.length());
//			for (int i = 0; i < arr.length(); i++){
//				if(arr.get(i) instanceof JSONObject){
//					JSONObject jos = (JSONObject)arr.get(i);
//					System.out.println((JSONObject)(jos.get("credentials")));
//					System.out.println("username is: " +((JSONObject)(jos.get("credentials"))).get("username"));				
//					
//				}
//			}
//		}
//		
//		//for(JSONArray i : jo.getJSONArray("mongodb-2.2"))
//		//System.out.println(jo.get("mongodb-2.2"));
//		//System.out.println(jo.toString());
//	} catch (JSONException e1) {
//		// TODO Auto-generated catch block
//		e1.printStackTrace();
//	}
	
	
	  
}
}







