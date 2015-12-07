package com.ibm.cloud.migration.cmd.tool.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.cloudfoundry.client.lib.CloudCredentials;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

public class RetrieveCloudCred {
	public static final String PROPERTY_FILE ="env.properties";
	
	public static String getTargetCFURL() throws IOException{
		//read the target_url  from env.properties(which was generated by the first login to the cloud) under project
		Properties prop = new Properties();
		InputStream fis = new FileInputStream(PROPERTY_FILE);
		prop.load(fis);
		String targetUrl = prop.getProperty("cf_url");
		return targetUrl;
	}
	
	public static String getHerokuAPIKey() throws IOException{
		//read the heroku_api_key from env.properties(which was generated by the first login to the cloud) under project
		Properties prop = new Properties();
		InputStream fis = new FileInputStream(PROPERTY_FILE);
		prop.load(fis);
		String heroku_api_key = prop.getProperty("heroku_api_key");
		return heroku_api_key;
	}
	
	public static CloudCredentials getCFCred() throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		OAuth2AccessToken token = mapper.readValue(new File("cfuser.json"), OAuth2AccessToken.class);
		CloudCredentials cred = new CloudCredentials(token);
		return cred;
		
	}

}
