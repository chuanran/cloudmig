package com.ibm.cloud.migration.csar.composer.main;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Properties;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
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

public class GeneratePropertiesDir {
	
	private static AppModel model;
	private static Properties config;
	
	private static Properties velocity_property = new Properties();
	private static String class_path = "./tosca_templates/";
	//private static String class_path = GeneratePropertiesDir.class.getResource("").getPath();
	private static VelocityEngine ve_properties_template;
	private static String types_home = "CSAR" + "/" + "types";
	static{
		AppModelParser parser = new AppModelParser();
		try {
			model = parser.parse(AppModelConstants.APP_MODEL_NAME);
		} catch (AppModelException e) {
			// TODO 
			e.printStackTrace();
		}
		config = new Properties();
		velocity_property.setProperty("file.resource.loader.path", class_path);
		ve_properties_template = new VelocityEngine( );
		try {
			ve_properties_template.init(velocity_property);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void generatePropertiesDir() throws ResourceNotFoundException, ParseErrorException, MethodInvocationException, Exception{
		
		//create directory for storing types.xsd files
		File f = new File(types_home);
		if(! f.exists()){
			f.mkdir();
		}
		
		
		Template  template_types =ve_properties_template.getTemplate("types_template" + "/" + "GeneralTypes.xsd.template");
		VelocityContext context_types  = new VelocityContext();
		String types = "";
		//merge the application types file into GeneralTypes.xsd.template
		List<WebAppNode> web_apps = model.getWebApps();
		String app_types = processAppTypeTemplate(web_apps);
		if(app_types.trim().length() > 0){
			context_types.put(TOSCATemplateConstants.APP_TYPES_KEY, app_types);
		} else{
			context_types.put(TOSCATemplateConstants.APP_TYPES_KEY, "");
		}
		
		//merge the service instance types file into GeneralTypes.xsd.template
		List<ServiceNode> service_instances = model.getServices();
		String service_instance_types = processServiceInstanceTypeTemplate(service_instances);
		if(app_types.trim().length() > 0){
			context_types.put(TOSCATemplateConstants.SERVICE_INSTANCE_TYPES_KEY, service_instance_types);
		} else{
			context_types.put(TOSCATemplateConstants.SERVICE_INSTANCE_TYPES_KEY, "");
		}
		
		//Write the types string to the corresponding types directory
		StringWriter writer_types = new StringWriter();
		template_types.merge(context_types, writer_types);
		WriteStringToFile.writeStrToFile(writer_types.toString(), types_home + "/" + "GeneralTypes.xsd");
	}
	
	private static String processAppTypeTemplate(List<WebAppNode> web_apps) throws ResourceNotFoundException, ParseErrorException, MethodInvocationException, IOException, Exception{
		Template  template_app_types =ve_properties_template.getTemplate("types_template" + "/" + "application_types.template");
		VelocityContext context_app_types  = new VelocityContext();
		String app_types = "";
		if(web_apps != null && web_apps.size() > 0){
			for(WebAppNode web_app : web_apps){
				String app_name = web_app.getName();
				String app_port = web_app.getPort();
				context_app_types.put(TOSCATemplateConstants.APP_NAME_KEY, app_name);
				context_app_types.put(TOSCATemplateConstants.APP_PORT_KEY, app_port);
				StringWriter writer_app_types = new StringWriter();
				template_app_types.merge(context_app_types, writer_app_types);
				app_types = app_types + "\n" + writer_app_types.toString();
			}
		}
		return app_types;
	}
	
    private static String processServiceInstanceTypeTemplate(List<ServiceNode> service_instances) throws ResourceNotFoundException, ParseErrorException, MethodInvocationException, IOException, Exception{
    	Template  template_service_instance_types =ve_properties_template.getTemplate("types_template" + "/" + "service_instance.template");
		VelocityContext context_service_instance_types  = new VelocityContext();
		String service_instance_types = "";
		if(service_instances !=null && service_instances.size() > 0){
			for(ServiceNode service_instance : service_instances){
				String service_instance_name = service_instance.getPropertyValue("dbName");
				String service_type = service_instance.getServiceType().toString();
				context_service_instance_types.put(TOSCATemplateConstants.SERVICE_INSTANCE_KEY, service_instance_name);
				context_service_instance_types.put(TOSCATemplateConstants.SERVICE_PORT_KEY, AppModelConstants.SERVICE_DEFAULT_PORT.get(service_type));
				StringWriter writer_service_instance_types = new StringWriter();
				template_service_instance_types.merge(context_service_instance_types, writer_service_instance_types);
				service_instance_types = service_instance_types + "\n" + writer_service_instance_types.toString();
			}
		}
		return service_instance_types;
	}
    
//    public static void main(String[] agrs){
//    	try {
//			generatePropertiesDir();
//		} catch (IOException e) {
//			// TODO 自动生成 catch 块
//			e.printStackTrace();
//		}
//	}
}
