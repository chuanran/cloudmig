package com.ibm.cloud.migration.cmd.tool.utility;

import java.util.HashMap;
import java.util.Map;

public class AppModelConstants {
	public static final String APP_MODEL_HEROKU_NAME= "application_model_heroku.xml";
	public static final String APP_SERVICE_URL_ENV_TYPE="service_url";
	public static final String APP_URL_ENV_TYPE="webapp_url";
	
	public static final String SERVICE_LABEL_KEY="SERVICE_LABEL";
	public static final String SERVICE_VERSION_KEY="SERVICE_VERSION";
	public static final String SERVICE_ENV_NAME_KEY="SERVICE_ENV";
	
	public static final String[] CLOUD_PLATFORM={"cf","heroku"};
	
	public static final String[] RUNTIME_BUILDPACK={"nodejs"};
	
	public static final Map<String, String> RUNTIME_BUILDPACK_URL = new HashMap() {{
		 put( "NodeJS" , "http://9.115.210.47:9393/vincent/heroku-buildpack-nodejs.git");
	}};

}
