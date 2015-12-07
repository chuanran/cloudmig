package com.ibm.cloud.migration.csar.composer.main;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

//import org.apache.log4j.Logger;

import com.ibm.cloud.migration.csar.composer.utility.LinuxCmd;
import com.ibm.cloud.migration.csar.composer.utility.SedCommandObtainer;

public class LoadTemplate {
	//private static final Logger logger = Logger.getLogger(LoadTemplate.class);
	private static Properties config;
	private static final String default_config = "/com/ibm/p2/network/nginx/nginx.properties";
	private static final String custom_config = "/nginx-custom.properties";
	private static final String Vm_Os_Definition_template_file = "/com/ibm/cloud/migration/csar/composer/main/Definitions/General_Definitions/Vm_Os_Definition.template";
	public static final String prop_zkserver = "zkserver";
	public static final String prop_VmOsDefinition_template = "nginx_template";
	static {
		config = new Properties();
//		// load properties
//		try {
//			Class config_class = Class
//					.forName("com.ibm.p2.network.nginx.NginxConfig");
//
//			InputStream is = config_class.getResourceAsStream(default_config);
//			config.load(is);
//
//			// now, see if we can find our custom config
//			is = config_class.getResourceAsStream(custom_config);
//			if (is != null) {
//				config.load(is);
//				logger.info("Successfully loaded custom properties file from classpath");
//			} else {
//				logger.info("No Custom properties file found in classpath");
//			}
//
//		} catch (Exception e) {
//			logger.error("error", e);
//		}

		// load template configuration
		//Get the path for sed.exe for windows support
//		String ProjectPath=System.getProperty("user.dir");
//		System.out.println("ProjectPath is "+ ProjectPath);
		//String sedcmd = SedCommandObtainer.ObtainSedCommand();
		System.out.println(System.getProperty("user.dir"));
		System.out.println(Thread.currentThread().getContextClassLoader().getResource(""));
		System.out.println(LoadTemplate.class.getResource("").getPath());
		String sedcmd = "E:/workspace-icap/com.ibm.cloud.migration.csar.composer/sed.exe";
		String cmd = "E:/workspace-icap/com.ibm.cloud.migration.csar.composer/src/com/ibm/cloud/migration/csar/composer/main/sed.exe -i s/chuan/Mongo/g /Definitions/General_Definitions/Vm_Os_Definition.template";
		try {
			LinuxCmd.runLinuxCmd(cmd);
		} catch (InterruptedException e1) {
			// TODO 自动生成 catch 块
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO 自动生成 catch 块
			e1.printStackTrace();
		}
		BufferedInputStream bis = new BufferedInputStream(LoadTemplate.class
				.getResourceAsStream(Vm_Os_Definition_template_file));
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		byte[] b = new byte[512];
		int c;
		try {
			while ((c = bis.read(b)) != -1) {
				os.write(b, 0, c);
			}
			String value = new String(os.toByteArray());
			
			//LinuxCmd.runLinuxCmd(sedcmd + "-i s");
			System.out.println(value);
			config.setProperty(prop_VmOsDefinition_template, value);

		} catch (IOException e) {
			//logger.error("error", e);
		}
	}
	
	public static String getProperty(String key) {
		//logger.debug("Fetching property [" + key + "="
		//		+ config.getProperty(key) + "]");
		String value = config.getProperty(key);
		return value == null ? value : value.trim();
	}
	public static void main(String[] args){
		new LoadTemplate();
	}
}
