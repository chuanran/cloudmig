package com.ibm.cloud.migration.csar.composer.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AppModelConstants {
	
	//app_model location should be set after capturing from other cloud platform
	public static String APP_MODEL_NAME= "application_model.xml";
	
	public static final String CLOUD_CF="cf";
	public static final String CLOUD_HEROKU="heroku";
	public static final String CF_APP_MODEL_NAME= "cf.xml";
	
	
	public static final String WEB_APP_URL_ENV_TYPE= "webapp_url";
	public static final String APP_SERVICE_URL_ENV_TYPE= "service_url";
	public static final String APP_NORMAL_ENV_TYPE= "normal";
	
	
	public static final Map<String, String> RUNTIME_DEPLOYMENT_OS_PACKAGE = new HashMap() {{
		 put( "apache" , "httpd");
	}};
	
	public static final Map<String, String> SERVICE_DEPLOYMENT_OS_PACKAGE = new HashMap() {{
		 put( "mysql" , "mysql, mysql-server");
	}};
	
	//Define the service/runtime types which need configuration artifact
	public static final String[] configuration_artifact = {"mongo"};
	
	//Define the default service port according to different service type
	public static final Map<String, String> SERVICE_DEFAULT_PORT = new HashMap() {{
		 put( "mongo" , "27017");
		 
	}};
	
	public void setAppModel(String app_model){
		this.APP_MODEL_NAME = app_model;
	}
	
	public String getAppModel(){
		return this.APP_MODEL_NAME;
	}
	
//	public static void main(String[] args){
//		String a = RUNTIME_DEPLOYMENT_OS_PACKAGE.get("apache");
//		for(String b : a.split(",")){
//			System.out.println(b.trim());
//		}
//	}

}
