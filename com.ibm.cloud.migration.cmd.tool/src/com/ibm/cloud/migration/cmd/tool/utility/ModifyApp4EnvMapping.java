package com.ibm.cloud.migration.cmd.tool.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.List;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import com.ibm.cloud.migration.appmodel.AppModelException;
import com.ibm.cloud.migration.appmodel.AppModelParser;
import com.ibm.cloud.migration.appmodel.model.AppModel;
import com.ibm.cloud.migration.appmodel.model.Env;
import com.ibm.cloud.migration.appmodel.model.ServiceNode;
import com.ibm.cloud.migration.appmodel.model.WebAppNode;

public class ModifyApp4EnvMapping { 
	
	public static final String NODEJS_AUTO_CONFIG_TEMPLATE = "auto-config.js.template";

	public static String generateAutoConfigJSStr(String env_name, String service_label, String service_version) throws ResourceNotFoundException, ParseErrorException, Exception{
		VelocityEngine ve_auto_config = new VelocityEngine();
		Template temp_auto_config = ve_auto_config.getTemplate(NODEJS_AUTO_CONFIG_TEMPLATE);
		VelocityContext context_auto_config = new VelocityContext();
		context_auto_config.put(AppModelConstants.SERVICE_ENV_NAME_KEY, env_name);
		context_auto_config.put(AppModelConstants.SERVICE_LABEL_KEY, service_label);
		context_auto_config.put(AppModelConstants.SERVICE_VERSION_KEY, service_version);
		StringWriter writer_auto_config = new StringWriter();
		temp_auto_config.merge(context_auto_config, writer_auto_config);
		return writer_auto_config.toString();
		
	}
	
	public static void generateAutoConfigFile(WebAppNode webapp, String service_label, String service_version) throws ResourceNotFoundException, ParseErrorException, Exception{
		String buildpack_type = webapp.getHostedOnContainer().getBuildPack().toString();
		String app_name = webapp.getName();
		List<Env> env_list = webapp.getEnvs();
		String auto_config_str = "";
		System.out.println("For application " + app_name  +", generating auto-config file for enviroment variable mapping");
		if(env_list!=null && env_list.size()>0){
			for(Env env : env_list){
				if(env.getType().toString().equalsIgnoreCase("service_url")){
					//Here support node.js runtime
					if(buildpack_type.equalsIgnoreCase("NODEJS")){
						auto_config_str = auto_config_str +  ModifyApp4EnvMapping.generateAutoConfigJSStr(env.getName(), service_label, service_version);
					}
				}
			}
		}
		//here support auto-generating auto-config-{app_name}.js file for nodejs runtime
		if(buildpack_type.equalsIgnoreCase("NODEJS")){
			WriteStringToFile.writeStrToFile(auto_config_str, "auto-config-"+app_name+".js");
		}
}
	
				
	
	public static File findFile(String baseDirName, String targetFileName) {  
        /** 
         */  
        String tempName = null;   
        File baseDir = new File(baseDirName);  
        if (!baseDir.exists() || !baseDir.isDirectory()){  
            //System.out.println(baseDirName + "is not a directory");  
        } else {  
            String[] filelist = baseDir.list();
            for (int i = 0; i < filelist.length; i++) { 
            	File readfile = new File(new File(baseDirName).getAbsolutePath() + File.separator + filelist[i]);  
                //System.out.println(readfile.getName());  
                if(!readfile.isDirectory()) {  
                    tempName =  readfile.getName();   
                    if (targetFileName.equals(tempName)) {  
                        //
                    	return readfile.getAbsoluteFile();
                        //fileList.add(readfile.getAbsoluteFile());   
                    }  
                } else if(readfile.isDirectory()){  
                    findFile(new File(baseDirName).getAbsolutePath() + File.separator + readfile,targetFileName);  
                } 
            }
            
        }
        return null;
    }  
	
	public static File getAppEntryPointFile(WebAppNode webapp) throws AppModelException, IOException{
		String buildpack_type = webapp.getHostedOnContainer().getBuildPack().toString();
		String app_name = webapp.getName();
		//Here support node.js runtime
		if(buildpack_type.equalsIgnoreCase("NODEJS")){
			File package_json_file  = findFile(app_name, "package.json");
			//Here parse the startpoint name in package.json file
			String entry_point_name = "";
			BufferedReader br = new BufferedReader(new InputStreamReader(  
	                new FileInputStream(package_json_file.getAbsolutePath())));  
	        String line ="";
	        for (line = br.readLine(); line != null; line = br.readLine()) {  
	            //System.out.println(line);
	        	if(line.contains("\"start\":")){
	        		break;
	        	}
	        }  
	        br.close();
	        String[]  strs = line.split(":");
	        String[] strss = strs[1].trim().split("\"node");
	        String[] strsss = strss[1].split("\"");
	        entry_point_name = strsss[0].trim();
			File app_entry_point_file = new File(package_json_file.getParent() +File.separator + entry_point_name);
			return app_entry_point_file;
		}
		return null;
	}
	
//	public static void main(String[] args){
//		AppModelParser parser = new AppModelParser();
//		AppModel model = null;
//		List<ServiceNode> services = null;
//		String app_model="application_model_heroku.xml";
//		try {
//			model = parser.parse(app_model);
//			services = model.getServices();
//		} catch (AppModelException e) {
//					// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		for(ServiceNode service: services){
//			String service_label ="mongodb";
//			String service_version="2.2";
//			List<WebAppNode> webapps = model.getAppsConnectedWithService(service.getName());
//			try {
//				generateAutoConfigJSFile(webapps, service_label,service_version);
//			} catch (ResourceNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (ParseErrorException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//	}
	
	public static void main(String[] args){
		File f = findFile("countly-api-2013", "package.json" );
		System.out.println(f.getParent());
	}

}
