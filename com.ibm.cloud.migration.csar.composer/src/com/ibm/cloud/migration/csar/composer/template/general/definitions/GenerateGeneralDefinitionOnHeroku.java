package com.ibm.cloud.migration.csar.composer.template.general.definitions;

import java.io.StringWriter;
import java.util.List;
import java.util.Properties;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.ibm.cloud.migration.appmodel.AppModelException;
import com.ibm.cloud.migration.appmodel.AppModelParser;
import com.ibm.cloud.migration.appmodel.model.AppModel;
import com.ibm.cloud.migration.appmodel.model.Env;
import com.ibm.cloud.migration.appmodel.model.ServiceNode;
import com.ibm.cloud.migration.appmodel.model.WebAppNode;
import com.ibm.cloud.migration.csar.composer.constants.AppModelConstants;
import com.ibm.cloud.migration.csar.composer.constants.TOSCATemplateConstants;
import java.util.UUID; 

public class GenerateGeneralDefinitionOnHeroku {
	
	private static AppModel model;
	private static Properties velocity_property = new Properties();
	private static Properties config;
	public static final String runtime_vmos_definition_file = TOSCATemplateConstants.RUNTIME_VMOS_DEFINITION_FILE;
	public static final String service_vmos_definition_file = TOSCATemplateConstants.SERVICE_VMOS_DEFINITION_FILE;
	
	private static String general_definition_file = "";
	private static String general_definition_diagram_file = "";
	
	static{
		config = new Properties();
		velocity_property.setProperty("file.resource.loader.path", GenerateGeneralDefinitionOnHeroku.class.getResource("").getPath());
		AppModelParser parser = new AppModelParser();
		
		try {
			model = parser.parse(AppModelConstants.APP_MODEL_NAME);
		} catch (AppModelException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
	}
	
	public static void generateGeneralDefinitionFile() throws Exception{
		
		//According to the Application_Definition.template and the instance number for each application , generate the application's "NodeTemplate"
		
		//VelocityEngine statement
		VelocityEngine ve_definition_xml = new VelocityEngine( );
		ve_definition_xml.init(velocity_property);
		
	    Template template_definition_xml = ve_definition_xml.getTemplate(TOSCATemplateConstants.GENERAL_DEFINITION_XML_KEY);
		VelocityContext context_definition_xml = new VelocityContext();
		
	    Template template_definition_diagram_xml = ve_definition_xml.getTemplate(TOSCATemplateConstants.GENERAL_DEFINITION_DIAGRAM_XML_KEY);
		VelocityContext context_definition_diagram_xml = new VelocityContext();
		
		//Definition template initialization
	    Template template_app_definition = ve_definition_xml.getTemplate(TOSCATemplateConstants.APP_DEFINITION_TEMPLATE);
		VelocityContext context_app = new VelocityContext();
		
	    Template template_vmos_runtime_definition = ve_definition_xml.getTemplate(TOSCATemplateConstants.VM_OS_DEFINITION_TEMPLATE);
		VelocityContext context_vmos_runtime = new VelocityContext();
		
	    Template template_vmos_service_definition = ve_definition_xml.getTemplate(TOSCATemplateConstants.VM_OS_DEFINITION_TEMPLATE);
		VelocityContext context_vmos_service = new VelocityContext();
		
	    Template template_runtime_definition = ve_definition_xml.getTemplate(TOSCATemplateConstants.RUNTIME_SERVER_DEFINITION_TEMPLATE);
		VelocityContext context_runtime = new VelocityContext();
		
	    Template template_service_definition = ve_definition_xml.getTemplate(TOSCATemplateConstants.SERVICE_DEFINITION_TEMPLATE);
		VelocityContext context_service = new VelocityContext();
		
	    Template template_service_instance_definition = ve_definition_xml.getTemplate(TOSCATemplateConstants.SERVICE_INSTANCE_DEFINITION_TEMPLATE);
		VelocityContext context_service_instance = new VelocityContext();
		
		//Definition Diagram template initialization
	    Template template_vmos_runtime_definition_diagram = ve_definition_xml.getTemplate(TOSCATemplateConstants.VM_OS_RUNTIME_DEFINITION_DIAGRAM_TEMPLATE);
		VelocityContext context_vmos_runtime_diagram = new VelocityContext();
		
	    Template template_service_definition_diagram = ve_definition_xml.getTemplate(TOSCATemplateConstants.VM_OS_SERVICE_DEFINITION_DIAGRAM_TEMPLATE);
		VelocityContext context_service_diagram = new VelocityContext();
		
	    Template template_app1_definition_diagram = ve_definition_xml.getTemplate(TOSCATemplateConstants.APP1_DEFINITION_DIAGRAM_TEMPLATE);
		VelocityContext context_app1_diagram = new VelocityContext();
		
	    Template template_app2_definition_diagram = ve_definition_xml.getTemplate(TOSCATemplateConstants.APP2_DEFINITION_DIAGRAM_TEMPLATE);
		VelocityContext context_app2_diagram = new VelocityContext();
		
		if(model.getSource().toString().equals(AppModelConstants.CLOUD_HEROKU)){
			String db_uuid = "";
			System.out.println("The cloud source is heroku");
			List<WebAppNode> webapps = model.getWebApps();
			
			//Definition string declaration
			String definition_xml = "";
			String definition_diagram_xml = "";
			String application_definition = "";
			String vmos_runtime_definition = "";
			String vmos_service_definition = "";
			String runtime_definition = "";
			String service_definition = "";
			String service_instance_definition = "";
			
			//Definition diagram string declaration
			String vmos_runtime_definition_diagram = "";
			String service_definition_diagram = "";
			String app1_definition_diagram = "";
			String app2_definition_diagram = "";
			
			/**
			 *generate VM_OS_SERVICE/SERVICE/SERVICE INSTANCE definition
			 *currently simply assume that service instance will not increase even if application instances increase
			 */
			
			List<ServiceNode> vm_services = model.getServices();
			System.out.println("vm_services size is : " + vm_services.size());
			if(vm_services !=null && vm_services.size()>0){
				int times = 0;
				for(ServiceNode vm_service : vm_services){
					times = times + 1;
					context_vmos_service.put(TOSCATemplateConstants.VM_ROLE_KEY, vm_service.getServiceType().toString());
					context_vmos_service.put(TOSCATemplateConstants.VM_MEMORY_VALUE_KEY, TOSCATemplateConstants.VM_SERVICE_DEFAULT_MEMORY_VALUE);
					
					context_service.put(TOSCATemplateConstants.GENERAL_SERVICE_KEY, vm_service.getServiceType().toString());
					
					context_service_instance.put(TOSCATemplateConstants.SERVICE_INSTANCE_KEY, vm_service.getName());
					context_service_instance.put(TOSCATemplateConstants.GENERAL_SERVICE_KEY, vm_service.getServiceType().toString());
					context_service_instance.put(TOSCATemplateConstants.SERVICE_PORT_KEY, TOSCATemplateConstants.SERVICE_DEFAULT_PORT);
					
					//For VMOS service&service instance diagram
					context_service_diagram.put(TOSCATemplateConstants.SERVICE_INSTANCE_KEY, vm_service.getName());
					db_uuid = UUID.randomUUID().toString();
					String service_uuid = UUID.randomUUID().toString();
					String os_service_uuid = UUID.randomUUID().toString();
					String vm_service_uuid = UUID.randomUUID().toString();
					String service_hostedon_os_service_uuid = UUID.randomUUID().toString();  
					String os_service_hostedon_vm_service_uuid = UUID.randomUUID().toString(); 
					String service_instance_hostedon_ervice_uuid = UUID.randomUUID().toString(); 
					context_service_diagram.put(TOSCATemplateConstants.SERVICE_DIAGRAM_UUID_KEY, service_uuid);
					context_service_diagram.put(TOSCATemplateConstants.OS_SERVICE_DIAGRAM_UUID_KEY, os_service_uuid);
					context_service_diagram.put(TOSCATemplateConstants.VM_SERVICE_DIAGRAM_UUID_KEY, vm_service_uuid);
					context_service_diagram.put(TOSCATemplateConstants.SERVICE_INSTANCE_DIAGRAM_UUID_KEY, db_uuid);
					context_service_diagram.put(TOSCATemplateConstants.SERVICE_HOSTEDON_OS_SERVICE_DIAGRAM_UUID_KEY, service_hostedon_os_service_uuid);     
					context_service_diagram.put(TOSCATemplateConstants.OS_SERVICE_HOSTEDON_VM_SERVICE_DIAGRAM_UUID_KEY, os_service_hostedon_vm_service_uuid);
					context_service_diagram.put(TOSCATemplateConstants.SERVICE_INSTANCE_HOSTEDON_SERVICE_DIAGRAM_UUID_KEY, service_instance_hostedon_ervice_uuid);
					
					if(times == 1){
						context_service.put(TOSCATemplateConstants.SERVICE_KEY, vm_service.getServiceType().toString());
						context_service_instance.put(TOSCATemplateConstants.SERVICE_KEY, vm_service.getServiceType().toString());
						context_service_diagram.put(TOSCATemplateConstants.SERVICE_KEY, vm_service.getServiceType().toString());
					} else{
						context_service.put(TOSCATemplateConstants.SERVICE_KEY, vm_service.getServiceType().toString() + (times-1));
						context_service_instance.put(TOSCATemplateConstants.SERVICE_KEY, vm_service.getServiceType().toString() + (times-1));
						context_service_diagram.put(TOSCATemplateConstants.SERVICE_KEY, vm_service.getServiceType().toString() + (times-1));
					}
				}
			}
			
			for(WebAppNode webapp : webapps){
				int app_instance_number = Integer.parseInt(webapp.getInstances());
				String app_port = webapp.getPort();
				String app_general_name = webapp.getName();
				String hostedOnGeneralRuntime = webapp.getHostedOnContainer().getName();
				String vm_memory = String.valueOf((Integer.parseInt(webapp.getMemory()) * 2));
				for(int i =1; i<app_instance_number+1; i++){
					String runtime_name_in_definition;
					String app_name_in_definition;
					if(i==1){
						runtime_name_in_definition = hostedOnGeneralRuntime + "-" + webapp.getName();
						app_name_in_definition = webapp.getName();
					} else{
						runtime_name_in_definition = hostedOnGeneralRuntime + "-" + webapp.getName() + (i-1);
						app_name_in_definition = webapp.getName() + (i-1);
					}
					
					/**
					 *Process Vm_Os_Definition.template file, to generate VM_OS_RUNTIME in General_Definition.xml
					 */
					//generate VM_OS_RUNTIME 
					context_vmos_runtime.put(TOSCATemplateConstants.VM_ROLE_KEY, runtime_name_in_definition);
					context_vmos_runtime.put(TOSCATemplateConstants.VM_MEMORY_VALUE_KEY, vm_memory);
					
					/**
					 *Process General-VmOsRuntime-Diagram.template file
					 */
					context_vmos_runtime_diagram.put(TOSCATemplateConstants.RUNTIME_KEY, runtime_name_in_definition);
					String runtime_uuid = UUID.randomUUID().toString();      
					context_vmos_runtime_diagram.put(TOSCATemplateConstants.RUNTIME_DIAGRAM_UUID_KEY, runtime_uuid);
					String os_runtime_uuid = UUID.randomUUID().toString();      
					context_vmos_runtime_diagram.put(TOSCATemplateConstants.OS_RUNTIME_DIAGRAM_UUID_KEY, os_runtime_uuid);
					String vm_runtime_uuid = UUID.randomUUID().toString();      
					context_vmos_runtime_diagram.put(TOSCATemplateConstants.VM_RUNTIME_DIAGRAM_UUID_KEY, vm_runtime_uuid);
					String runtime_hostedon_os_runtime_uuid = UUID.randomUUID().toString();      
					context_vmos_runtime_diagram.put(TOSCATemplateConstants.RUNTIME_HOSTEDON_OS_RUNTIME_DIAGRAM_UUID_KEY, runtime_hostedon_os_runtime_uuid);
					String os_runtime_hostedon_vm_runtime_uuid = UUID.randomUUID().toString();      
					context_vmos_runtime_diagram.put(TOSCATemplateConstants.OS_RUNTIME_HOSTEDON_VM_RUNTIME_DIAGRAM_UUID_KEY, os_runtime_hostedon_vm_runtime_uuid);
					
					
					/**
					 *Process RuntimeServer_Definition.template file, to generate RuntimeServer Definition in General_Definition.xml
					 */
					
					context_runtime.put(TOSCATemplateConstants.RUNTIME_KEY, runtime_name_in_definition);
					context_runtime.put(TOSCATemplateConstants.GENERAL_RUNTIME_KEY, hostedOnGeneralRuntime);
					context_runtime.put(TOSCATemplateConstants.APP_GENERAL_NAME_KEY, app_general_name);
					context_runtime.put(TOSCATemplateConstants.APP_PORT_KEY, app_port);
					
					/**
					 *Process General-App2-Diagram.template and  Process General-App1-Diagram.template file. Here just workaround for Countly application. These code needs to be refined.
					 */
					
					List<WebAppNode> depends_on_app_in_diagrams = webapp.getDependsOnApps();
					String app1_uuid = UUID.randomUUID().toString();
					String app2_uuid = UUID.randomUUID().toString();
					String app2_hostedon_runtime_id = UUID.randomUUID().toString();
					String app1_hostedon_runtime_id =  UUID.randomUUID().toString();
					String app2_connectsto_service_instance_id = UUID.randomUUID().toString();
					String app1_connectsto_service_instance_id = UUID.randomUUID().toString();
					String app2_dependson_app1_id = UUID.randomUUID().toString();
					if(depends_on_app_in_diagrams != null  && depends_on_app_in_diagrams.size() >0){
						context_app2_diagram.put(TOSCATemplateConstants.APP2_ID_KEY, app2_uuid);
						context_app2_diagram.put(TOSCATemplateConstants.APP1_ID_KEY, app1_uuid);
						context_app2_diagram.put(TOSCATemplateConstants.APP2_HOSTEDON_RUNTIME_DIAGRAM_UUID_KEY, app2_hostedon_runtime_id);
					    context_app2_diagram.put(TOSCATemplateConstants.APP2_CONNECTSTO_SERVICE_INSTANCE_DIAGRAM_UUID_KEY, app2_connectsto_service_instance_id);
					    context_app2_diagram.put(TOSCATemplateConstants.APP2_DEPENDSON_APP1_UUID_KEY, app2_dependson_app1_id);
						context_app2_diagram.put(TOSCATemplateConstants.DEPENDSON_SOURCE_KEY, app_name_in_definition);
						context_app2_diagram.put(TOSCATemplateConstants.RUNTIME_DIAGRAM_UUID_KEY, runtime_uuid);
						context_app2_diagram.put(TOSCATemplateConstants.RUNTIME_KEY, runtime_name_in_definition);
						context_app2_diagram.put(TOSCATemplateConstants.SERVICE_INSTANCE_DIAGRAM_UUID_KEY, db_uuid);
						context_app2_diagram.put(TOSCATemplateConstants.SERVICE_INSTANCE_KEY, webapp.getConnectsToServices().get(0).getName());
						for(WebAppNode depends_on_app_in_diagram : depends_on_app_in_diagrams){
							context_app2_diagram.put(TOSCATemplateConstants.DEPENDSON_TARGET_KEY, depends_on_app_in_diagram.getName());
						}
					}
					List<WebAppNode> depended_on_app_in_diagrams = webapp.getDependedOnApps();
					if(depended_on_app_in_diagrams != null  && depended_on_app_in_diagrams.size() >0){
						context_app1_diagram.put(TOSCATemplateConstants.APP1_ID_KEY, app1_uuid);
						context_app1_diagram.put(TOSCATemplateConstants.APP1_HOSTEDON_RUNTIME_DIAGRAM_UUID_KEY, app1_hostedon_runtime_id);
					    context_app1_diagram.put(TOSCATemplateConstants.APP1_CONNECTSTO_SERVICE_INSTANCE_DIAGRAM_UUID_KEY, app1_connectsto_service_instance_id);
						context_app1_diagram.put(TOSCATemplateConstants.DEPENDSON_TARGET_KEY, app_name_in_definition);
						context_app1_diagram.put(TOSCATemplateConstants.RUNTIME_DIAGRAM_UUID_KEY, runtime_uuid);
						context_app1_diagram.put(TOSCATemplateConstants.RUNTIME_KEY, runtime_name_in_definition);
						context_app1_diagram.put(TOSCATemplateConstants.SERVICE_INSTANCE_DIAGRAM_UUID_KEY, db_uuid);
						context_app1_diagram.put(TOSCATemplateConstants.SERVICE_INSTANCE_KEY, webapp.getConnectsToServices().get(0).getName());
					}
					
					
					/**
					 *Process Application_Definition.template file, to generate Application Definition template in General_Definition.xml
					 */
					
					
					/**
					 *Substitute the variable in Application_Definition.template
					 */
					
					context_app.put(TOSCATemplateConstants.APP_NAME_KEY, app_name_in_definition);
					context_app.put(TOSCATemplateConstants.APP_GENERAL_NAME_KEY, app_general_name);
					context_app.put(TOSCATemplateConstants.RUNTIME_KEY, runtime_name_in_definition);
					context_app.put(TOSCATemplateConstants.GENERAL_RUNTIME_KEY, hostedOnGeneralRuntime);
					context_app.put(TOSCATemplateConstants.APP_PORT_KEY, app_port);
					
					/**
					 *substitute the environment variable definition in Application_Definition.template
					 */
					
					List<Env> app_envs= webapp.getEnvs();
					if(app_envs != null && app_envs.size()>0){
						String app_url_env="";
						String service_url_env="";
						for(Env app_env : app_envs){
							if(app_env.getType().toString().equals(AppModelConstants.WEB_APP_URL_ENV_TYPE)){
								//First modify the template of AppURLEnvironmentVariable.template
								VelocityEngine ve_app_url_env_var_definition_template = new VelocityEngine( );
								ve_app_url_env_var_definition_template.init(velocity_property);
							    Template template_app_url_env_var_definition = ve_definition_xml.getTemplate(TOSCATemplateConstants.APP_URL_ENV_VAR_TEMPLATE);
								VelocityContext context_app_url_env = new VelocityContext();
								context_app_url_env.put(TOSCATemplateConstants.APP_URL_ENV_VAR_IN_URL_ENV_KEY, app_env.getValue());
								StringWriter writer = new StringWriter();
								template_app_url_env_var_definition.merge(context_app_url_env, writer);
							    String value = writer.toString();
							    app_url_env = app_url_env + "\n" + value;
							} 
							if(app_env.getType().toString().equals(AppModelConstants.APP_SERVICE_URL_ENV_TYPE)){
								//First modify the template of ServiceURLEnvironmentVariable.template
								VelocityEngine ve_app_service_url_env_var_definition_template = new VelocityEngine( );
								ve_app_service_url_env_var_definition_template.init(velocity_property);
							    Template template_app_service_url_env_var_definition = ve_definition_xml.getTemplate(TOSCATemplateConstants.APP_SERVICE_URL_ENV_VAR_TEMPLATE);
								VelocityContext context_app_service_url_env = new VelocityContext();
								context_app_service_url_env.put(TOSCATemplateConstants.APP_SERVICE_URL_ENV_VAR_IN_SERVICE_URL_ENV_KEY, app_env.getValue());
								StringWriter writer = new StringWriter();
								template_app_service_url_env_var_definition.merge(context_app_service_url_env, writer);
							    String value = writer.toString();
							    service_url_env = service_url_env + "\n" + value;
							} 
						}
						if(app_url_env.trim().length() == 0){
							//if there is no app url env, just clean up it in Application_Definition.template
							context_app.put(TOSCATemplateConstants.APP_URL_ENV_VAR_KEY, "");
						} else{
							//write AppURLEnvironmentVariable.template to Application_Definition.template
							context_app.put(TOSCATemplateConstants.APP_URL_ENV_VAR_KEY, app_url_env);
						}
						if(service_url_env.trim().length() == 0){
							//if there is no service url env, just clean up it in Application_Definition.template
							context_app.put(TOSCATemplateConstants.APP_SERVICE_URL_ENV_VAR_KEY, "");
						} else{
							//write AppURLEnvironmentVariable.template to Application_Definition.template
						    context_app.put(TOSCATemplateConstants.APP_SERVICE_URL_ENV_VAR_KEY, service_url_env);
						}
					}else{
						//if there is no app/service url env, just clean up it in Application_Definition.template
						context_app.put(TOSCATemplateConstants.APP_URL_ENV_VAR_KEY, "");
						context_app.put(TOSCATemplateConstants.APP_SERVICE_URL_ENV_VAR_KEY, "");
					}
					
					/**
					 *Substitute the "DatabaseEndPointRequirement" in Application_Definition.template
					 */
					
					//First modify the template of DatabaseEndpointRequirement.template
					List<ServiceNode> connectToServices = webapp.getConnectsToServices();
					if(connectToServices !=null && connectToServices.size()>0){
						String db_endpoint_requirment = "";
						String app_connectto_relationship = "";
						for(ServiceNode service : connectToServices){
							//First modify the template of DatabaseEndpointRequirement.template
							VelocityEngine ve_app_db_endpoint_requirement_definition_template = new VelocityEngine( );
							ve_app_db_endpoint_requirement_definition_template.init(velocity_property);
						    Template template_app_db_endpoint_requirement = ve_app_db_endpoint_requirement_definition_template.getTemplate(TOSCATemplateConstants.APP_DB_ENDPOINT_REQUIREMENT_DEFINITION_TEMPLATE);
							VelocityContext context_app_db_endpoint = new VelocityContext();
							context_app_db_endpoint.put(TOSCATemplateConstants.APP_NAME_KEY, app_name_in_definition);
							context_app_db_endpoint.put(TOSCATemplateConstants.APP_GENERAL_NAME_KEY, app_general_name);
							context_app_db_endpoint.put(TOSCATemplateConstants.SERVICE_KEY, service.getServiceType().toString());
							StringWriter writer = new StringWriter();
							template_app_db_endpoint_requirement.merge(context_app_db_endpoint, writer);
						    String value = writer.toString();
						    db_endpoint_requirment = db_endpoint_requirment + "\n" + value;
						    
						    //First modify the template of ConnectToRelationship.template
						    VelocityEngine ve_app_connectto_relationship_definition_template = new VelocityEngine( );
						    ve_app_connectto_relationship_definition_template.init(velocity_property);
						    Template app_connectto_relationship_definition_template = ve_app_connectto_relationship_definition_template.getTemplate(TOSCATemplateConstants.APP_CONNECTSTO_RELATIONSHIP_TEMPLATE);
							VelocityContext context_app_connectto_relationship = new VelocityContext();
							context_app_connectto_relationship.put(TOSCATemplateConstants.APP_NAME_KEY, app_name_in_definition);
							context_app_connectto_relationship.put(TOSCATemplateConstants.APP_GENERAL_NAME_KEY, app_general_name);
							context_app_connectto_relationship.put(TOSCATemplateConstants.SERVICE_KEY, service.getServiceType().toString());
							context_app_connectto_relationship.put(TOSCATemplateConstants.SERVICE_INSTANCE_KEY, service.getName());
							StringWriter writer1 = new StringWriter();
							app_connectto_relationship_definition_template.merge(context_app_connectto_relationship, writer1);
						    String value1 = writer1.toString();
						    app_connectto_relationship = app_connectto_relationship + "\n" + value1;
						}
						if(db_endpoint_requirment.trim().length() == 0){
							//if there is no database target, just clean up DB_ENDPOINT_REQUIREMENT in Application_Definition.template
							context_app.put(TOSCATemplateConstants.DB_ENDPOINT_REQUIREMENT_KEY, "");
						} else{
							//write DatabaseEndpointRequirement.template to Application_Definition.template
							context_app.put(TOSCATemplateConstants.DB_ENDPOINT_REQUIREMENT_KEY, db_endpoint_requirment);
						}
						if(app_connectto_relationship.trim().length() == 0 ){
							//if there is no database target, just clean up CONNECTTO_RELATIONSHIP in Application_Definition.template
							context_app.put(TOSCATemplateConstants.CONNECTTO_RELATIONSHIP_KEY, "");
						} else{
							//write CONNECTTO_RELATIONSHIP.template to Application_Definition.template
							context_app.put(TOSCATemplateConstants.CONNECTTO_RELATIONSHIP_KEY, app_connectto_relationship);
						}
					}else{
						//if there is no service target, just clean up the DB_ENDPOINT_REQUIREMENT and CONNECTTO_RELATIONSHIP in Application_Definition.template
						context_app.put(TOSCATemplateConstants.DB_ENDPOINT_REQUIREMENT_KEY, "");
						context_app.put(TOSCATemplateConstants.CONNECTTO_RELATIONSHIP_KEY, "");
					}
					
					/**
					 *Substitute the template "DependsOnRequirement", template "DependedOnCapability" and DependsOnRelationship.template in Application_Definition.template
					 */
					
					
					List<WebAppNode> depends_on_nodes = webapp.getDependsOnApps();
					if(depends_on_nodes != null  && depends_on_nodes.size() >0){
						String depends_on_relationship = "";
						
						//First modify the DependsOnRequirement
						VelocityEngine ve_app_dependson_requirement_definition_template = new VelocityEngine( );
						ve_app_dependson_requirement_definition_template.init(velocity_property);
					    Template template_app_dependson_requirement = ve_app_dependson_requirement_definition_template.getTemplate(TOSCATemplateConstants.APP_DEPENDSON_REQUIREMENT_TEMPLATE);
						VelocityContext context_app_dependson_requirement = new VelocityContext();
						context_app_dependson_requirement.put(TOSCATemplateConstants.APP_NAME_KEY, app_name_in_definition);
						StringWriter writer = new StringWriter();
						template_app_dependson_requirement.merge(context_app_dependson_requirement, writer);
					    String value = writer.toString();
						//Then  write  DependsOnRequirement.template to the Application_Definition.template
						context_app.put(TOSCATemplateConstants.DEPENDSON_REQUIREMENT_KEY, value);
						context_app.put(TOSCATemplateConstants.DEPENDSON_CAPABILITY_KEY, "");
						
						//Modify the source application "App2Name" in DependsOnRelationship.template
						VelocityEngine ve_app_dependson_relationship_definition_template = new VelocityEngine( );
						ve_app_dependson_relationship_definition_template.init(velocity_property);
					    Template template_app_dependson_relationship = ve_app_dependson_relationship_definition_template.getTemplate(TOSCATemplateConstants.APP_DEPENDSON_RELATIONSHIP_TEMPLATE);
						VelocityContext context_app_dependson_relationship = new VelocityContext();
						context_app_dependson_relationship.put(TOSCATemplateConstants.DEPENDSON_SOURCE_KEY, app_name_in_definition);
					    //String value1 = writer1.toString();
						for(WebAppNode depends_on_node : depends_on_nodes){
							depends_on_node.getName();
							//Modify the target application "App1Name" in DependsOnRelationship.template
							context_app_dependson_relationship.put(TOSCATemplateConstants.DEPENDSON_TARGET_KEY, depends_on_node.getName());
							StringWriter writer1 = new StringWriter();
							template_app_dependson_relationship.merge(context_app_dependson_relationship, writer1);
							depends_on_relationship = depends_on_relationship + "\n" + writer1.toString();
						}
						context_app.put(TOSCATemplateConstants.DEPENDSON_RELATIONSHIP_KEY, depends_on_relationship);
					}else{
						//if there is no dependsOn target, just clean up the DEPENDSON_RELATIONSHIP and DEPENDSON_REQUIREMENT in Application_Definition.template
						context_app.put(TOSCATemplateConstants.DEPENDSON_REQUIREMENT_KEY, "");
						context_app.put(TOSCATemplateConstants.DEPENDSON_RELATIONSHIP_KEY, "");
					}
					
					List<WebAppNode> depended_on_nodes = webapp.getDependedOnApps();
					if(depended_on_nodes != null  && depended_on_nodes.size() >0){
						VelocityEngine ve_app_dependedon_capability_definition_template = new VelocityEngine( );
						ve_app_dependedon_capability_definition_template.init(velocity_property);
					    Template template_app_dependedon_capability = ve_app_dependedon_capability_definition_template.getTemplate(TOSCATemplateConstants.APP_DEPENDSON_CAPABILITY_TEMPLATE);
						VelocityContext context_app_dependedon_capability = new VelocityContext();
						context_app_dependedon_capability.put(TOSCATemplateConstants.APP_NAME_KEY, app_name_in_definition);
						StringWriter writer = new StringWriter();
						template_app_dependedon_capability.merge(context_app_dependedon_capability, writer);
					    String value = writer.toString();
						//Then  write  DependsOnRequirement.template to the Application_Definition.template
						context_app.put(TOSCATemplateConstants.DEPENDSON_CAPABILITY_KEY, value);
					}else{
						context_app.put(TOSCATemplateConstants.DEPENDSON_CAPABILITY_KEY, "");
					}
					
				}
				StringWriter writer_app = new StringWriter();
				template_app_definition.merge(context_app, writer_app);
				application_definition = application_definition + "\n" + writer_app.toString(); 
				
				StringWriter writer_vmos_runtime = new StringWriter();
				template_vmos_runtime_definition.merge(context_vmos_runtime, writer_vmos_runtime);
				vmos_runtime_definition = vmos_runtime_definition + "\n" + writer_vmos_runtime.toString(); 
				
				StringWriter writer_runtime = new StringWriter();
				template_runtime_definition.merge(context_runtime, writer_runtime);
				runtime_definition = runtime_definition + "\n" + writer_runtime.toString(); 
				
				StringWriter writer_vmos_runtime_diagram = new StringWriter();
				template_vmos_runtime_definition_diagram.merge(context_vmos_runtime_diagram, writer_vmos_runtime_diagram);
				vmos_runtime_definition_diagram = vmos_runtime_definition_diagram + "\n" + writer_vmos_runtime_diagram.toString();
				
			}
			StringWriter writer_vmos_service = new StringWriter();
			template_vmos_service_definition.merge(context_vmos_service, writer_vmos_service);
			vmos_service_definition = vmos_service_definition + "\n" + writer_vmos_service.toString();
			
			StringWriter writer_service = new StringWriter();
			template_service_definition.merge(context_service, writer_service);
		    service_definition = service_definition + "\n" + writer_service.toString();
		    
		    StringWriter writer_service_instance = new StringWriter();
			template_service_instance_definition.merge(context_service_instance, writer_service_instance);
		    service_instance_definition = service_instance_definition + "\n" + writer_service_instance.toString();
		    
		    StringWriter writer_service_diagram = new StringWriter();
			template_service_definition_diagram.merge(context_service_diagram, writer_service_diagram);
		    service_definition_diagram = service_definition_diagram + "\n" + writer_service_diagram.toString();
		    
		    StringWriter writer_app1_diagram = new StringWriter();
			template_app1_definition_diagram.merge(context_app1_diagram, writer_app1_diagram);
			app1_definition_diagram = app1_definition_diagram + "\n" + writer_app1_diagram.toString();
			
			StringWriter writer_app2_diagram = new StringWriter();
			template_app2_definition_diagram.merge(context_app2_diagram, writer_app2_diagram);
			app2_definition_diagram = app2_definition_diagram + "\n" + writer_app2_diagram.toString();
			
			general_definition_file = vmos_runtime_definition + "\n" +  runtime_definition + "\n" + application_definition + "\n" + vmos_service_definition + "\n" + service_definition + "\n" + service_instance_definition;
			general_definition_diagram_file = vmos_runtime_definition_diagram + "\n" + service_definition_diagram + "\n" + app1_definition_diagram + "\n" + app2_definition_diagram;
			
			context_definition_xml.put(TOSCATemplateConstants.GENERAL_DEFINITION_FILE_KEY, general_definition_file);
			StringWriter writer_definition_xml = new StringWriter();
			template_definition_xml.merge(context_definition_xml, writer_definition_xml);
			definition_xml = definition_xml + "\n" + writer_definition_xml.toString();
			
			context_definition_diagram_xml.put(TOSCATemplateConstants.GENERAL_DEFINITION_DIAGRAM_FILE_KEY, general_definition_diagram_file);
			StringWriter writer_definition_diagram_xml = new StringWriter();
			template_definition_diagram_xml.merge(context_definition_diagram_xml, writer_definition_diagram_xml);
			definition_diagram_xml = definition_diagram_xml + "\n" + writer_definition_diagram_xml.toString();
			
			config.setProperty(TOSCATemplateConstants.GENERAL_DEFINITION_XML_FILE_KEY, definition_xml);
			config.setProperty(TOSCATemplateConstants.GENERAL_DEFINITION_DIAGRAM_XML_FILE_KEY, definition_diagram_xml);
			
		}
	}
	
	public static String getProperty(String key) {
		String value = config.getProperty(key);
		return value == null ? value : value.trim();
	}
	
	
	public static void main(String[] agrs){
		//GenerateGeneralDefinitionOnHeroku.generateGeneralDefinitionFile();
		//System.out.println(GenerateGeneralDefinitionOnHeroku.getProperty(TOSCATemplateConstants.GENERAL_DEFINITION_XML_FILE_KEY));
		//System.out.println(GenerateGeneralDefinitionOnHeroku.getProperty(TOSCATemplateConstants.GENERAL_DEFINITION_DIAGRAM_XML_FILE_KEY));
//		//System.out.println(GenerateGeneralDefinitionOnHeroku.getProperty(TOSCATemplateConstants.GENERAL_DEFINITION_DIAGRAM_FILE_KEY));
    }
	
	

}
