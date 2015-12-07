package com.ibm.cloud.migration.csar.composer.utility;

import java.io.InputStream;
import java.util.Properties;

//import org.apache.log4j.Logger;

public class LoadAppModelProperties {
	//private static final Logger logger = Logger.getLogger(LoadAppModelProperties.class);
	private static Properties config;
	private static final String appmodel_property = "/com/ibm/cloud/migration/csar/composer/utility/AppModel.properties";
	
	static {
		config = new Properties();
		// load properties
		try {
			Class config_class = Class
					.forName("com.ibm.cloud.migration.csar.composer.utility.LoadAppModelProperties");
			InputStream is = config_class.getResourceAsStream(appmodel_property);
			config.load(is);
			if (is != null) {
				config.load(is);
				System.out.println("Successfully loaded custom properties file from classpath");
			} else {
				System.out.println("Not Successfully loaded custom properties file from classpath");
				System.out.println("No Custom properties file found in classpath");
			}

		} catch (Exception e) {
			//logger.error("error", e);
		}
	}
	
	public static String getProperty(String key) {
//		logger.debug("Fetching property [" + key + "="
//				+ config.getProperty(key) + "]");
		String value = config.getProperty(key);
		return value == null ? value : value.trim();
	}
//	public static void main(String[] args){
//		System.out.println(LoadAppModelProperties.getProperty("RUNTIME_VM_CPU_Value"));
//	}
}
