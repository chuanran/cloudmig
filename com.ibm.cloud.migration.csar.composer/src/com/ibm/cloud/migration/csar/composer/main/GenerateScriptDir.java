package com.ibm.cloud.migration.csar.composer.main;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Properties;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import java.io.File;

import com.ibm.cloud.migration.appmodel.AppModelException;
import com.ibm.cloud.migration.appmodel.AppModelParser;
import com.ibm.cloud.migration.appmodel.model.AppModel;
import com.ibm.cloud.migration.appmodel.model.Env;
import com.ibm.cloud.migration.appmodel.model.ServiceNode;
import com.ibm.cloud.migration.appmodel.model.WebAppNode;
import com.ibm.cloud.migration.csar.composer.constants.AppModelConstants;
import com.ibm.cloud.migration.csar.composer.constants.TOSCATemplateConstants;
import com.ibm.cloud.migration.csar.composer.utility.CopyDirectoryContents;
import com.ibm.cloud.migration.csar.composer.utility.WriteStringToFile;

public class GenerateScriptDir {
	
	private static Properties config;
	private static Properties velocity_property = new Properties();
	private static AppModel model;
	private static String class_path = "./tosca_templates/";
	//private static String class_path = GenerateScriptDir.class.getResource("").getPath();
	private static String script_home = "CSAR" + "/" + "scripts";
	private static String app_mount_point = "/opt/";
	private static String app_script_template_home = class_path + "scripts_template" + "/" + "Node" + "/" + "application";
	private static String runtime_script_template_home = class_path + "scripts_template" + "/" + "Node" + "/" + "runtime";
	private static String service_script_template_home = class_path + "scripts_template" + "/" + "Node" + "/" + "service";
	private static String service_instance_script_template_home = class_path + "scripts_template" + "/" + "Node" + "/" + "service_instance";
	private static String connect_to_script_template_home = class_path + "scripts_template" + "/" + "Relationship" + "/" + "ConnectTo" + "/" + "DatabaseConnection";
	private static String  db_hostedon_script_template_home = class_path + "scripts_template" + "/" + "Relationship" + "/" + "HostedOn" + "/" + "DataBaseHostedOnDBMS";
	private static VelocityEngine ve_scripts_template;
	
	private static String cloud_name = "";
	//private static List<WebAppNode> web_apps = null;
	static{
		AppModelParser parser = new AppModelParser();
		try {
			model = parser.parse(AppModelConstants.APP_MODEL_NAME);
		} catch (AppModelException e) {
			// TODO
			e.printStackTrace();
		}
		cloud_name = model.getSource().toString();
		config = new Properties();
		velocity_property.setProperty("file.resource.loader.path", class_path);
		ve_scripts_template = new VelocityEngine( );
		try {
			ve_scripts_template.init(velocity_property);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void generateScriptDirectory() throws ResourceNotFoundException, ParseErrorException, Exception{
		
		/**
		 * Generate script directory for NodeType: application
		 */
		List<WebAppNode> web_apps = model.getWebApps();
		List<String> services = model.getServiceTypes();
		List<ServiceNode> service_instances = model.getServices();
		List<String> service_types = model.getServiceTypes();
		String app_container = "";
		for(WebAppNode webapp : web_apps){
			try {
				app_container = webapp.getHostedOnContainer().getBuildPack().toString();
			} catch (AppModelException e) {
				// TODO
				e.printStackTrace();
			}
			break;
		}
		try {
			generateAppScriptDir(web_apps, app_container);
		} catch (IOException e) {
			// TODO
			e.printStackTrace();
		}
		
		/**
		 * Generate script directory for NodeType: runtime
		 */
		generateRuntimeScriptDir(app_container);
		
		/**
		 * Generate script directory for NodeType: service
		 */
		generateServicesScriptDir(services);
		
		/**
		 * Generate script directory for NodeType: service
		 */
		generateServiceInstancesScriptDir(service_instances);
		
		/**
		 * Generate script directory for RelationshipType: connectTo
		 */
		try {
			generateConnectsToScriptDir(web_apps, service_instances);
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		
		/**
		 * Generate script directory for RelationshipType: HostedOn
		 * Currently only consider the "databasehostedonDBMS"
		 */
			generateDbHostedOnScriptDir(service_types);
			
		/**
		 * Generate script directory for RelationshipType: DependsOn
		 * Currently only consider the application dependsOn relationship
		 */
			try {
				generateDependsOnScriptDir(web_apps);
			} catch (IOException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
	}
	/**
	 * Generate script directory for NodeType: application
	 * @throws Exception 
	 * @throws ParseErrorException 
	 * @throws ResourceNotFoundException 
	 */
	public static void generateAppScriptDir(List<WebAppNode> web_apps, String app_container) throws ResourceNotFoundException, ParseErrorException, Exception{
		String app_script_template_relative_path = "scripts_template" + "/" + "Node" + "/" + "application" + "/" + app_container + "/" + "start.sh";
		//String vcap_services_template_relative_path = "";
//		if(cloud_name.equals(AppModelConstants.CLOUD_HEROKU)){
//			app_script_template_relative_path = "scripts_template" + "/" + "Node" + "/" + "application" + "/" + "heroku" + "/" + app_container + "/" + "start.sh";
//		} else if(cloud_name.equals(AppModelConstants.CLOUD_CF)){
//			app_script_template_relative_path = "scripts_template" + "/" + "Node" + "/" + "application" + "/" + "cf" + "/" + app_container + "/" + "start.sh";
//			vcap_services_template_relative_path = "scripts_template" + "/" + "Node" + "/" + "application" + "/" + "cf" + "/" + app_container + "/" + "VCAP_SERVICES.template";
//		} else{
//			System.out.println("Currently the cloud is not supported");
//		}
		Template template_app_scripts = ve_scripts_template.getTemplate(app_script_template_relative_path);
		VelocityContext context_app_scripts  = new VelocityContext();
//		Template template_vcap_services = ve_scripts_template.getTemplate(vcap_services_template_relative_path);
//		VelocityContext context_vcap_services  = new VelocityContext();
		
		for(WebAppNode webapp : web_apps){
			String app_name = webapp.getName();
			String app_name_for_script = webapp.getName() + "application";
			
			//Create the directory for storing the application scripts 
			String app_scripts_path = script_home + "/" + app_name_for_script;
			//System.out.println("app_scripts_path is: " + app_scripts_path );
			File f = new File(app_scripts_path);
			if(! f.exists()){
				f.mkdirs();
				
			}
			//Processing the application script template
			String app_start_dir = app_mount_point + app_name;
			//To substitute the "$Start_Point_Dir" in "start.sh" of the application 
			context_app_scripts.put(TOSCATemplateConstants.APP_START_POINT_KEY, app_start_dir);
			context_app_scripts.put(TOSCATemplateConstants.APP_NAME_KEY, app_name);
			String app_port_env_var = "${" + "AppPort}";
			context_app_scripts.put(TOSCATemplateConstants.APP_PORT_KEY, app_port_env_var);
			
			StringWriter writer_app_scripts = new StringWriter();
			template_app_scripts.merge(context_app_scripts, writer_app_scripts);
			//Write the substituted String to the corresponding scripts directory
			//System.out.println("app_latest_scripts_path is: " + app_scripts_path);
			WriteStringToFile.writeStrToFile(writer_app_scripts.toString(), app_scripts_path + "/" + "start.sh");
			
			//The target cloud will be cf if there exists vcap_Service template
//			if(vcap_services_template_relative_path.trim().length() > 0){
//				//Process VCAP_SERVICES.template which will be merged into the "start.sh"
//				List<String> connect_to_service_types = model.getAppConnectsToServicesTypes(app_name);
//				String vcap_services = "";
//				String app_scripts = "";
//				if(connect_to_service_types !=null && connect_to_service_types.size() >0){
//					for(String connect_to_service_type : connect_to_service_types){
//						context_vcap_services.put(TOSCATemplateConstants.SERVICE_KEY, connect_to_service_type);
//						StringWriter writer_vcap_service = new StringWriter();
//						template_vcap_services.merge(context_vcap_services, writer_vcap_service);
//						vcap_services = vcap_services + "/n" + writer_vcap_service.toString();
//					}
//					if(vcap_services.trim().length() > 0){
//						context_app_scripts.put(TOSCATemplateConstants.VCAP_SERVICES_EXPORT_KEY, vcap_services);
//					} else{
//						context_app_scripts.put(TOSCATemplateConstants.VCAP_SERVICES_EXPORT_KEY, "");
//					}
//				} else{
//					context_app_scripts.put(TOSCATemplateConstants.VCAP_SERVICES_EXPORT_KEY, "");
//				}
//				
//			} 
			  //The target cloud will be heroku if there doesn't exist vcap_Service template
//			  else{
//				  
//			
//			}
		}
		
	}
	
	public static void generateRuntimeScriptDir(String app_container){
		
		//Create the directory for storing the runtime scripts 
		String runtime_node_name = app_container + "server";
		String runtime_scripts_path = script_home + "/" + runtime_node_name; 
		File f = new File(runtime_scripts_path);
		if(! f.exists()){
			f.mkdir();
		}
		//System.out.println("runtime_script_template_home is " + runtime_script_template_home);
		//System.out.println("runtime_scripts_path is : " + runtime_scripts_path);
		//There need no changes on the life-cycle scripts for the runtime, just copy them to the corresponding directory
		CopyDirectoryContents.copyDirectory(runtime_script_template_home + "/" + app_container, runtime_scripts_path, true);
		
	}
	
	public static void generateServicesScriptDir(List<String> services){
		//Create the directory for storing the runtime scripts
		if(services != null && services.size() > 0){
			for(String service : services){
				String service_scripts_path = script_home + "/" + service; 
				File f = new File(service_scripts_path);
				if(! f.exists()){
					f.mkdir();
				}
				//There need no changes on the life-cycle scripts for the service, just copy them to the corresponding directory
				CopyDirectoryContents.copyDirectory(service_script_template_home + "/" + service, service_scripts_path, true);
			}
		}
	}
	
	public static void generateServiceInstancesScriptDir(List<ServiceNode> service_instances){
		
		//Create the directory for storing the runtime scripts
		if(service_instances != null && service_instances.size() > 0){
			for(ServiceNode service_instance : service_instances){
				String service_instances_scripts_path = script_home + "/" + service_instance.getPropertyValue("dbName"); 
				File f = new File(service_instances_scripts_path);
				if(! f.exists()){
					f.mkdir();
				}
				//There need no changes on the life-cycle scripts for the service instances, just copy them to the corresponding directory
				CopyDirectoryContents.copyDirectory(service_instance_script_template_home + "/" + service_instance.getServiceType().toString(), service_instances_scripts_path, true);
			}
		}
	}
	
	public static void generateConnectsToScriptDir(List<WebAppNode> web_apps, List<ServiceNode> service_instances) throws ResourceNotFoundException, ParseErrorException, Exception{
		String connectto_script_template_relative_path = "";
		String vcap_services_template_relative_path = "";
		String connectto_script_template_relative_home = "scripts_template" + "/" + "Relationship" + "/" + "ConnectTo" + "/" + "DatabaseConnection";
		String service_firewall_port_template_relative_path = connectto_script_template_relative_home + "/" + "Service_FireWall_Port.template";
		if(cloud_name.equals(AppModelConstants.CLOUD_HEROKU)){
			connectto_script_template_relative_path = connectto_script_template_relative_home + "/" + "heroku" + "/" + "ConnectToDatabase.sh";
		} else if(cloud_name.equals(AppModelConstants.CLOUD_CF)){
			connectto_script_template_relative_path = connectto_script_template_relative_home + "/" + "cf" + "/" + "ConnectToDatabase.sh";
			vcap_services_template_relative_path = connectto_script_template_relative_home + "/" + "cf" + "/" + "VCAP_SERVICES.template";
		} else{
			//System.out.println("Currently the cloud is not supported");
		}
		
		if(web_apps != null && web_apps.size() > 0){
			
			//create directory to store the connectTo scripts
			String connect_to_scripts_path = script_home + "/" + "DatabaseConnection"; 
			File f = new File(connect_to_scripts_path);
			if(! f.exists()){
				f.mkdir();
			}
			Template template_connect_to_script = ve_scripts_template.getTemplate(connectto_script_template_relative_path);
			VelocityContext context_connect_to_scripts  = new VelocityContext();
			
			Template template_service_firewall_setting = ve_scripts_template.getTemplate(service_firewall_port_template_relative_path);
			VelocityContext context_service_firewall_setting = new VelocityContext();
			
			Template template_vcap_services = ve_scripts_template.getTemplate(TOSCATemplateConstants.VCAP_SERVICES_TEMPLATE);
			VelocityContext context_vcap_services = new VelocityContext();
			
			
			Template template_env_variables = ve_scripts_template.getTemplate(TOSCATemplateConstants.HEROKU_ENV_TEMPLATE);
			VelocityContext context_env_variables = new VelocityContext();
			
			for(WebAppNode web_app : web_apps){
				
				String app_name = web_app.getName();
				List<String> connect_to_service_types = model.getAppConnectsToServicesTypes(app_name);
				String service_firewall_setting = "";
				String connect_to_scripts = "";
				String vcap_services = "";
				
				//To process service firewall setting template and vcap_services.template regardless of whether it exists
				if(connect_to_service_types !=null && connect_to_service_types.size() >0){
					for(String connect_to_service_type : connect_to_service_types){
						//process service firewall setting template
						context_service_firewall_setting.put(TOSCATemplateConstants.SERVICE_KEY, connect_to_service_type);
						StringWriter writer_service_firewall_setting = new StringWriter();
						template_service_firewall_setting.merge(context_service_firewall_setting, writer_service_firewall_setting);
						service_firewall_setting = service_firewall_setting + "\n" + writer_service_firewall_setting.toString();
						
						//process vcap_services.template regardless of whether it exists
						context_vcap_services.put(TOSCATemplateConstants.SERVICE_KEY, connect_to_service_type);
						StringWriter writer_vcap_services = new StringWriter();
						template_vcap_services.merge(context_vcap_services, writer_vcap_services);
						vcap_services = vcap_services + "\n" + writer_vcap_services.toString();
					}
						
				} else{
					context_connect_to_scripts.put(TOSCATemplateConstants.SERVICE_FIREWALL_SETTING_KEY, "");
					context_connect_to_scripts.put(TOSCATemplateConstants.VCAP_SERVICES_EXPORT_KEY, "");
				}
				
				//Currently, need to parse the application envs for heroku to set the env variables of service credential
				//Currently for heroku only consider the service_url type 
				List<Env> app_envs = web_app.getEnvs();
				String app_env_variables = "";
				if(app_envs !=null && app_envs.size() > 0){
					for(Env app_env : app_envs){
						String service_env_name = app_env.getName();
						String service_env_value = app_env.getValue().substring(2, app_env.getValue().length()-1);
						String service_env_type = "";
						if(app_env.getType().toString().equalsIgnoreCase("service_url")){
							for(ServiceNode service_instance : service_instances){
								if(service_instance.getName().equals(service_env_value)){
									service_env_type = service_instance.getServiceType().toString();
									break;
								}
							}
							//Process the ENV_VARIABLES.template
							context_env_variables.put(TOSCATemplateConstants.SERVICE_KEY, service_env_type);
							context_env_variables.put(TOSCATemplateConstants.ENV_NAME_KEY, service_env_name);
							StringWriter writer_env_variables = new StringWriter();
							template_env_variables.merge(context_env_variables, writer_env_variables);
							app_env_variables = writer_env_variables.toString() + "\n" + app_env_variables;
						}
						
						
					}
				} 
				
				if(service_firewall_setting.trim().length() > 0){
					context_connect_to_scripts.put(TOSCATemplateConstants.SERVICE_FIREWALL_SETTING_KEY, service_firewall_setting);
				} else{
					context_connect_to_scripts.put(TOSCATemplateConstants.SERVICE_FIREWALL_SETTING_KEY, "");
				}
				
				if(vcap_services.trim().length() > 0){
					context_connect_to_scripts.put(TOSCATemplateConstants.VCAP_SERVICES_EXPORT_KEY, vcap_services);
				} else{
					context_connect_to_scripts.put(TOSCATemplateConstants.VCAP_SERVICES_EXPORT_KEY, "");
				}
				
				if(app_env_variables.trim().length() > 0){
					context_connect_to_scripts.put(TOSCATemplateConstants.EXPORT_ENV_VARIABLES_KEY, app_env_variables);
				} else{
					context_connect_to_scripts.put(TOSCATemplateConstants.EXPORT_ENV_VARIABLES_KEY, "");
				}
				
				StringWriter writer_connect_to_scripts = new StringWriter();
				template_connect_to_script.merge(context_connect_to_scripts, writer_connect_to_scripts);
				connect_to_scripts = connect_to_scripts + "\n" + writer_connect_to_scripts.toString();
			    WriteStringToFile.writeStrToFile(connect_to_scripts, connect_to_scripts_path + "/" + app_name +"ConnectToDatabase.sh");
			}
		}
	}
	
	public static void generateDbHostedOnScriptDir(List<String> service_types){
		String db_hosted_on_scripts_relative_path = "scripts_template/Relationship/HostedOn/DataBaseHostedOnDBMS";
		if(service_types !=null && service_types.size() >0){
			for(String service_type : service_types){
				//Create the directory for storing the database hostedon relationship scripts
				String db_hostedon_scripts_path = script_home + "/" + service_type + "DatabaseHostedOn" +service_type; 
				File f = new File(db_hostedon_scripts_path);
				if(! f.exists()){
					f.mkdir();
				}
				//Copy the configureDB.sh file to the corresponding directory
				String db_hosted_on_scripts_template_path = db_hostedon_script_template_home + "/" + service_type;
				CopyDirectoryContents.copyDirectory(db_hosted_on_scripts_template_path, db_hostedon_scripts_path, true);
			}
		}
	}
	 
	
	public static void generateDependsOnScriptDir(List<WebAppNode> web_apps) throws ResourceNotFoundException, ParseErrorException, Exception{
		Template template_app_binding = ve_scripts_template.getTemplate(TOSCATemplateConstants.APP_BINDING_TEMPLATE);
		VelocityContext context_app_binding = new VelocityContext();
		for(WebAppNode web_app : web_apps){
			String app_name = web_app.getName();
			List<WebAppNode> depends_on_apps = web_app.getDependsOnApps();
			if(depends_on_apps !=null && depends_on_apps.size() >0){
				for(WebAppNode depends_on_app : depends_on_apps){
					//Create the directory for storing the app dependson relationship scripts
					String app_dependson_scripts_path = script_home + "/" + app_name + "DependsOn" + depends_on_app.getName();
					File f = new File(app_dependson_scripts_path);
					if(! f.exists()){
						f.mkdir();
					}
				}
			}
			
			List<Env> app_envs = web_app.getEnvs();
			if(app_envs !=null && app_envs.size() > 0){
				for(Env app_env : app_envs){
					if(app_env.getType().toString().equalsIgnoreCase("webapp_url")){
						//Process template application_bind.sh
						String app_bind_env_name = app_env.getName();
						String app_bind_env_value = app_env.getValue().substring(2, app_env.getValue().length()-1);
						context_app_binding.put(TOSCATemplateConstants.ENV_NAME_KEY, app_bind_env_name);
						context_app_binding.put(TOSCATemplateConstants.TARGET_APP_PORT_KEY, "${Target_" +"AppPort}");
						StringWriter writer_app_binding = new StringWriter();
						template_app_binding.merge(context_app_binding, writer_app_binding);
						WriteStringToFile.writeStrToFile(writer_app_binding.toString(), script_home + "/" + app_name + "DependsOn" + app_bind_env_value + "/" + "bind.sh");
					}
				}
			}
			
		}
	}
	
	
	
	
	

	public static void main(String[] agrs){
		try {
			generateScriptDirectory();
		} catch (ResourceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
