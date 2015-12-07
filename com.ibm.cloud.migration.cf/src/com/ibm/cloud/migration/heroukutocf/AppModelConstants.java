package com.ibm.cloud.migration.heroukutocf;

import java.util.HashMap;
import java.util.Map;

public class AppModelConstants {
	public static final String APP_MODEL_HEROKU_NAME= "application_model_heroku.xml";
	public static final String APP_SERVICE_URL_ENV_TYPE="service_url";
	public static final String APP_URL_ENV_TYPE="webapp_url";
	
	public static final Map<String, String> RUNTIME_BUILDPACK_URL = new HashMap() {{
		 put( "NodeJS" , "http://9.115.210.47:9393/vincent/heroku-buildpack-nodejs.git");
	}};

}
