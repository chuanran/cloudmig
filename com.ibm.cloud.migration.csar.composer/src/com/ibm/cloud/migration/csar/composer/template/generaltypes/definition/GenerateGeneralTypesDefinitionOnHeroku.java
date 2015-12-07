package com.ibm.cloud.migration.csar.composer.template.generaltypes.definition;

import java.io.StringWriter;
import java.util.List;
import java.util.Properties;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.ibm.cloud.migration.appmodel.AppModelException;
import com.ibm.cloud.migration.appmodel.AppModelParser;
import com.ibm.cloud.migration.appmodel.model.AppModel;
import com.ibm.cloud.migration.appmodel.model.ServiceNode;
import com.ibm.cloud.migration.appmodel.model.WebAppNode;
import com.ibm.cloud.migration.csar.composer.constants.AppModelConstants;
import com.ibm.cloud.migration.csar.composer.constants.TOSCATemplateConstants;
import java.util.UUID; 

public class GenerateGeneralTypesDefinitionOnHeroku {
	
	private static AppModel model;
	private static Properties velocity_property = new Properties();
	private static Properties config;
	public static final String runtime_vmos_definition_file = TOSCATemplateConstants.RUNTIME_VMOS_DEFINITION_FILE;
	public static final String service_vmos_definition_file = TOSCATemplateConstants.SERVICE_VMOS_DEFINITION_FILE;
	
	private static String general_types_definition_file = "";
	private static String general_types_definition_diagram_file = "";
	
	static{
		config = new Properties();
		velocity_property.setProperty("file.resource.loader.path", GenerateGeneralTypesDefinitionOnHeroku.class.getResource("").getPath());
		AppModelParser parser = new AppModelParser();
		
		try {
			model = parser.parse(AppModelConstants.APP_MODEL_NAME);
		} catch (AppModelException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
	}
	
	
	public static void generateGeneralTypesDefinitionFile() throws Exception{
		//GeneralTypes-Definitions-XML.template which needed to be substitute in the file
		
		//Velocity Engine definition
		VelocityEngine ve_types_definition = new VelocityEngine( );
		ve_types_definition.init(velocity_property);
		
		//Velocity template for  "GeneralTypes-Definitions-XML.template"
	    Template types_definition_xml_template = ve_types_definition.getTemplate(TOSCATemplateConstants.TYPES_DEFINITION_XML_TEMPLATE);
		VelocityContext context_types_definition_xml = new VelocityContext();
		
		//velocity template for "NodeType/Application.template"
	    Template app_types_definition_template = ve_types_definition.getTemplate(TOSCATemplateConstants.APPLICATION_TYPES_DEFINITION_TEMPLATE);
		VelocityContext context_app_types_definition = new VelocityContext();
		
		//velocity template for "Requirement_Capability/DatabaseEndPointRequirementType.template"
		 Template template_db_endpoint_requirement_types = ve_types_definition.getTemplate(TOSCATemplateConstants.DATABASEENDPOINT_REQUIREMENT_TYPES_DEFINITION_TEMPLATE);
	     VelocityContext context_db_endpoint_requirement_types = new VelocityContext();
	     
	     //velocity template for ""NodeType/DatabaseEndPointRequirementDefinition.template""
		 Template template_db_endpoint_requirement_definition = ve_types_definition.getTemplate(TOSCATemplateConstants.APPLICATION_TYPES_DATABASE_ENDPOINT_REQUIREMENT_TEMPLATE);
		 VelocityContext context_db_endpoint_requirement_definition = new VelocityContext();
		 
		//velocity template for ""NodeType/DependsOnRequirement.template""
		 Template template_app_dependson_requirement_definition = ve_types_definition.getTemplate(TOSCATemplateConstants.APPLICATION_TYPES_DEPENDSON_REQUIREMENT);
		 VelocityContext context_app_dependson_requirement_definition = new VelocityContext();
		 
		//velocity template for ""NodeType/DependedOnCapability.template""
		 Template template_app_dependedon_capability_definition = ve_types_definition.getTemplate(TOSCATemplateConstants.APPLICATION_TYPES_DEPENDEDON_CAPABILITY);
		 VelocityContext context_app_dependedon_capability_definition = new VelocityContext();
		 
		//velocity template for "RelationshipType/ApplicationDependsOn.template""
		 Template template_app_dependson_relationship_types_definition = ve_types_definition.getTemplate(TOSCATemplateConstants.APPLICATION_DEPENDSON_RELATIONSHIP_TYPES_TEMPLATE);
		 VelocityContext context_app_dependson_relationship_types_definition = new VelocityContext();
		 
		//velocity template for "RelationshipType/ApplicationDatabaseConnection.template""
		 Template template_app_databaseconnection_relationship_types_definition = ve_types_definition.getTemplate(TOSCATemplateConstants.APPLICATION_DATABASE_CONNECTION_RELATIONSHIP_TYPES_TEMPLATE);
		 VelocityContext context_app_databaseconnection_relationship_types_definition = new VelocityContext();
		 
		//velocity template for "Requirement_Capability/ApplicationDependsOnRequirement.template""
		 Template template_app_dependson_requirement_types_definition = ve_types_definition.getTemplate(TOSCATemplateConstants.APPLICATION_DEPENDSON_REQUIREMENT_TYPES_DEFINITION_TEMPLATE);
		 VelocityContext context_app_dependson_requirement_types_definition = new VelocityContext();
		 
		//velocity template for "Requirement_Capability/ApplicationDependedOnCapability.template"
		 Template template_app_dependedon_capability_types_definition = ve_types_definition.getTemplate(TOSCATemplateConstants.APPLICATION_DEPENDEDON_CAPABILITY_TYPES_DEFINITION_TEMPLATE);
		 VelocityContext context_app_dependedon_capability_types_definition = new VelocityContext();
		 
		//velocity template for "NodeType/RunTimeServer_Application.template"
		 Template template_runtime_server_types_definition = ve_types_definition.getTemplate(TOSCATemplateConstants.RUNTIME_SERVER_TYPES_DEFINITION_TEMPLATE);
		 VelocityContext context_runtime_server_types_definition = new VelocityContext();
		 
		//velocity template for "Requirement_Capability/ApplicationContainer.template"
		 Template template_runtime_server_requirement_capability = ve_types_definition.getTemplate(TOSCATemplateConstants.APPLICATION_CONTAINER_REQUIREMENT_CAPABILITY_TEMPLATE);
		 VelocityContext context_runtime_server_requirement_capability = new VelocityContext();
		 
		//velocity template for "NodeType/ServiceDeploymentArtifact/DeploymentOsPackageArtifactTemplate.template"
		 Template template_service_ospackage_template_artifact = ve_types_definition.getTemplate(TOSCATemplateConstants.SERVICE_OS_PACKAGE_TEMPLATE_ARTIFACT_TEMPLATE);
		 VelocityContext context_service_ospackage_template_artifact = new VelocityContext();
		 
		//velocity template for "NodeType/ServiceDeploymentArtifact/DeploymentOsPackageArtifact.template"
		 Template template_service_ospackage_artifact = ve_types_definition.getTemplate(TOSCATemplateConstants.SERVICE_OS_PACKAGE_ARTIFACT_TEMPLATE);
		 VelocityContext context_service_ospackage_artifact = new VelocityContext();
		 
		//velocity template for "NodeType/ServiceDeploymentArtifact/DeploymentOsPackageArtifactTemplate.template"
		 Template template_service_archive_template_artifact = ve_types_definition.getTemplate(TOSCATemplateConstants.SERVICE_ARCHIVE_ARTIFACT_TEMPLATE);
		 VelocityContext context_service_archive_template_artifact = new VelocityContext();
		 
		//velocity template for "NodeType/ServiceDeploymentArtifact/DeploymentArchiveArtifact.template"
		 Template template_service_archive_artifact = ve_types_definition.getTemplate(TOSCATemplateConstants.SERVICE_ARCHIVE_ARTIFACT);
		 VelocityContext context_service_archive_artifact = new VelocityContext();
		 
		//velocity template for "NodeType/ServiceInstance.template"
		 Template template_service_instance_types = ve_types_definition.getTemplate(TOSCATemplateConstants.SERVICE_INSTANCE_NODE_TYPES_TEMPLATE);
		 VelocityContext context_service_instance_types  = new VelocityContext();
		 
		//velocity template for "GeneralTypes-Diagram-XML.template"
		 Template template_general_types_diagram = ve_types_definition.getTemplate(TOSCATemplateConstants.GENERAL_TYPES_DIAGRAM_TEMPLATE);
		 VelocityContext context_general_types_diagram  = new VelocityContext();
		 
		String app_types_definition = "";
		
		if(model.getSource().toString().equals(AppModelConstants.CLOUD_HEROKU)){
			
			System.out.println("The cloud source is heroku");

			//The requirement/capability types definition which will be merged to "GeneralTypes-Definitions-XML.template"
			String app_dependson_requirement_types_definition = "";
			String app_dependedon_capability_types_definition = "";
			String databaseendpoint_requirement_types_definition = "";
			String dbcontainer_capability_types_definition = "";
			
			
			//NodeType types definition
			String runtime_server_types_definition = "";
			String service_types_definition = "";
			String service_instance_types_definition = "";
			
			//Relationship type definition
			String service_database_hostedon_relationship_type = "";
			
			
			/**
			 * Process "RuntimeServer_Application.template", which will be merged into GeneralTypes-Definitions-XML.template file
			 */
			
			//Here assume that there should be only 1 application container for the application
			List<WebAppNode> web_apps = model.getWebApps();
			String app_container = "";
			String runtime_install_script_uuid = UUID.randomUUID().toString();
			String runtime_configure_script_uuid = UUID.randomUUID().toString();
			String runtime_start_script_uuid = UUID.randomUUID().toString();
			String runtime_stop_script_uuid = UUID.randomUUID().toString();
			String runtime_uninstall_script_uuid = UUID.randomUUID().toString();
			for(WebAppNode webapp : web_apps){
				app_container = webapp.getHostedOnContainer().getName();
				break;
			}
			context_runtime_server_types_definition.put(TOSCATemplateConstants.RUNTIME_KEY, app_container);
			context_runtime_server_types_definition.put(TOSCATemplateConstants.RUNTIME_INSTALL_SCRIPT_UUID_KEY, runtime_install_script_uuid);
			context_runtime_server_types_definition.put(TOSCATemplateConstants.RUNTIME_CONFIGURE_SCRIPT_UUID_KEY, runtime_configure_script_uuid);
			context_runtime_server_types_definition.put(TOSCATemplateConstants.RUNTIME_START_SCRIPT_UUID_KEY, runtime_start_script_uuid);
			context_runtime_server_types_definition.put(TOSCATemplateConstants.RUNTIME_STOP_SCRIPT_UUID_KEY, runtime_stop_script_uuid);
			context_runtime_server_types_definition.put(TOSCATemplateConstants.RUNTIME_UNINSTALL_SCRIPT_UUID_KEY, runtime_uninstall_script_uuid);
			
			/**
			 * Process "DeploymentOsPackageArtifact.template" and DeploymentOsPackageArtifactTemplate.template, which will be merged into RunTimeServer_Application.template file
			 */
			
			//To detect which type of DeploymentArtifacts should be used. 
			//For example, for runtime "NodeJS", using DeploymentArchiveArtifact; for runtime "apache", may use DeploymentOsPackageArtifact
			
			//if the runtime name is in the Map object which assign the key/value pair of the runtime deployment ospackage, 
			//the DeploymentOsPackageArtifact.template will be processed.
			if(AppModelConstants.RUNTIME_DEPLOYMENT_OS_PACKAGE.keySet().contains(app_container)){
				//velocity template for "NodeType/RuntimeDeploymentArtifact/DeploymentOsPackageArtifact.template"
				 Template template_runtime_ospackage_artifact = ve_types_definition.getTemplate(TOSCATemplateConstants.RUNTIME_DEPLOYMENT_OS_PACKAGE_ARTIFACT);
				 VelocityContext context_runtime_oapackage_artifact_template_types_definition = new VelocityContext();
				 String runtime_deployment_os_package_artifact_uuid = UUID.randomUUID().toString();
				 context_runtime_oapackage_artifact_template_types_definition.put(TOSCATemplateConstants.RUNTIME_DEPLOYMENT_OS_PACKAGE_ARTIFACT_UUID_KEY, runtime_deployment_os_package_artifact_uuid);
				 StringWriter writer_ospackage_artifact = new StringWriter();
				 template_runtime_ospackage_artifact.merge(context_runtime_oapackage_artifact_template_types_definition, writer_ospackage_artifact);
				 context_runtime_server_types_definition.put(TOSCATemplateConstants.DEPLOYMENT_ARTIFACTS_TYPES_DEFINITION_KEY, writer_ospackage_artifact.toString());
				 
				//velocity template for  "NodeType/RuntimeDeploymentArtifact/DeploymentOsPackageArtifactTemplate.template"
				 Template template_runtime_ospackage_artifact_template = ve_types_definition.getTemplate(TOSCATemplateConstants.RUNTIME_DEPLOYMENT_OS_PACKAGE_ARTIFACT_TEMPLATE);
				 VelocityContext context_runtime_ospackage_artifact_template = new VelocityContext();
				 String os_package_artifact_definition = "";
				 String os_packages = AppModelConstants.RUNTIME_DEPLOYMENT_OS_PACKAGE.get(app_container);
				 for(String os_package : os_packages.split(",")){
					 String os_package_artifact_definition_tmp =  "<PackageInformation packageName=" + "\"" + os_package.trim() + "\""+ "/>";
					 os_package_artifact_definition = os_package_artifact_definition + os_package_artifact_definition_tmp;
				 }
				 context_runtime_ospackage_artifact_template.put(TOSCATemplateConstants.OS_PACKAGE_ARTIFACT_DEFINITION, os_package_artifact_definition);
				 context_runtime_ospackage_artifact_template.put(TOSCATemplateConstants.RUNTIME_DEPLOYMENT_OS_PACKAGE_ARTIFACT_UUID_KEY, runtime_deployment_os_package_artifact_uuid);
				 StringWriter writer_ospackage_artifact_template = new StringWriter();
				 template_runtime_ospackage_artifact_template.merge(context_runtime_ospackage_artifact_template, writer_ospackage_artifact_template);
				 context_runtime_server_types_definition.put(TOSCATemplateConstants.DEPLOYMENT_ARTIFACT_TEMPLATE_DEFINITION_KEY, writer_ospackage_artifact_template.toString());
			} 
			
			/**
			 * Process "DeploymentArchiveArtifact.template" and DeploymentArchiveArtifactTemplate.template, which will be merged into RunTimeServer_Application.template file
			 */
			
			 else{
				//velocity template for "NodeType/RuntimeDeploymentArtifact/DeploymentArchiveArtifact.template"
				 Template template_runtime_archive_artifact = ve_types_definition.getTemplate(TOSCATemplateConstants.RUNTIME_DEPLOYMENT_ARCHIVE_ARTIFACT);
				 VelocityContext context_runtime_archive_artifact = new VelocityContext();
				 String runtime_archive_artifact_uuid = UUID.randomUUID().toString();
				 context_runtime_archive_artifact.put(TOSCATemplateConstants.RUNTIME_DEPLOYMENT_ARCHIVE_ARTIFACT_UUID_KEY, runtime_archive_artifact_uuid);
				 context_runtime_archive_artifact.put(TOSCATemplateConstants.RUNTIME_KEY, app_container);
				 StringWriter writer_runtime_archive_artifact = new StringWriter();
				 template_runtime_archive_artifact.merge(context_runtime_archive_artifact, writer_runtime_archive_artifact);
				 //Merge "NodeType/RuntimeDeploymentArtifact/DeploymentArchiveArtifact.template" into RunTimeServer_Application.template file
				 context_runtime_server_types_definition.put(TOSCATemplateConstants.DEPLOYMENT_ARTIFACTS_TYPES_DEFINITION_KEY, writer_runtime_archive_artifact.toString());
				 
				//velocity template for "NodeType/RuntimeDeploymentArtifact/DeploymentArchiveArtifactTemplate.template"
				 Template template_runtime_archive_artifact_template = ve_types_definition.getTemplate(TOSCATemplateConstants.RUNTIME_DEPLOYMENT_ARCHIVE_ARTIFACT_TEMPLATE);
				 VelocityContext context_runtime_archive_artifact_template = new VelocityContext();
				 context_runtime_archive_artifact_template.put(TOSCATemplateConstants.RUNTIME_DEPLOYMENT_ARCHIVE_ARTIFACT_UUID_KEY, runtime_archive_artifact_uuid);
				 context_runtime_archive_artifact_template.put(TOSCATemplateConstants.RUNTIME_KEY, app_container);
				 StringWriter writer_runtime_archive_artifact_template = new StringWriter();
				 template_runtime_archive_artifact_template.merge(context_runtime_archive_artifact_template, writer_runtime_archive_artifact_template);
				 //Merge "NodeType/RuntimeDeploymentArtifact/DeploymentArchiveArtifactTemplate.template" into RunTimeServer_Application.template file
				 context_runtime_server_types_definition.put(TOSCATemplateConstants.DEPLOYMENT_ARTIFACT_TEMPLATE_DEFINITION_KEY, writer_runtime_archive_artifact_template.toString());
				 
			}
			StringWriter writer_runtime_server_types_definition = new StringWriter();
			template_runtime_server_types_definition.merge(context_runtime_server_types_definition, writer_runtime_server_types_definition);
			runtime_server_types_definition = writer_runtime_server_types_definition.toString();
			
			/**
			 *Merge runtime_server_types_definition into GeneralTypes-Definitions-XML.template file
			 *
			 */
			if(runtime_server_types_definition.trim().length() == 0){
				context_types_definition_xml.put(TOSCATemplateConstants.RUNTIME_SERVER_TYPES_DEFINITION_KEY, "");
			} else{
				context_types_definition_xml.put(TOSCATemplateConstants.RUNTIME_SERVER_TYPES_DEFINITION_KEY, runtime_server_types_definition);
			}
			
			/**
			 * Process "GeneralTypes_Diagram/Runtime.template", which will be merged into GeneralTypes-Diagram-XML.template file
			 */
			//velocity template for "GeneralTypes_Diagram/Runtime.template"
			 Template template_runtime_diagram = ve_types_definition.getTemplate(TOSCATemplateConstants.RUNTIME_TYPES_DIAGRAM_TEMPLATE);
			 VelocityContext context_runtime_diagram = new VelocityContext();
			 context_runtime_diagram.put(TOSCATemplateConstants.RUNTIME_KEY, app_container);
			 StringWriter writer_runtime_diagram = new StringWriter();
			 template_runtime_diagram.merge(context_runtime_diagram, writer_runtime_diagram);
			 context_general_types_diagram.put(TOSCATemplateConstants.RUNTIME_NODE_DIAGRAM_KEY, writer_runtime_diagram.toString());
			 
			/**
			 * Process "Requirement_Capability/ApplicationContainer.template", which will be merged into GeneralTypes-Definitions-XML.template file
			 */
			context_runtime_server_requirement_capability.put(TOSCATemplateConstants.RUNTIME_KEY, app_container);
			StringWriter writer_runtime_requirement_capability = new StringWriter();
			template_runtime_server_requirement_capability.merge(context_runtime_server_requirement_capability, writer_runtime_requirement_capability);
			//Merge "Requirement_Capability/ApplicationContainer.template" into GeneralTypes-Definitions-XML.template file
			context_types_definition_xml.put(TOSCATemplateConstants.APP_CONTAINER_REQUIRMENT_CAPABILITY_TYPES_KEY, writer_runtime_requirement_capability.toString());
			
			
			/**
			 * Process "Requirement_Capability/ApplicationDependedOnCapability.template", which will be merged into GeneralTypes-Definitions-XML.template file
			 */
			
			List<String> apps_with_dependedon_capability = model.getAppNamesWithDependedOnCapability();
			if(apps_with_dependedon_capability !=null && apps_with_dependedon_capability.size() > 0){
				for(String app_with_dependedon_capability : apps_with_dependedon_capability){
					context_app_dependedon_capability_types_definition.put(TOSCATemplateConstants.DEPENDSON_TARGET_KEY, app_with_dependedon_capability);
					StringWriter writer = new StringWriter();
					template_app_dependedon_capability_types_definition.merge(context_app_dependedon_capability_types_definition, writer);
					app_dependedon_capability_types_definition = app_dependedon_capability_types_definition + "\n" + writer.toString();
				}
			} else{
				context_types_definition_xml.put(TOSCATemplateConstants.APP_DEPENDEDON_CAPABILITY_TYPES_KEY, "");
			}
			 //Merge app_dependedon_capability_types_definition into GeneralTypes-Definitions-XML.template file
			if(app_dependedon_capability_types_definition.trim().length() == 0){
				context_types_definition_xml.put(TOSCATemplateConstants.APP_DEPENDEDON_CAPABILITY_TYPES_KEY, "");
			} else{
				context_types_definition_xml.put(TOSCATemplateConstants.APP_DEPENDEDON_CAPABILITY_TYPES_KEY, app_dependedon_capability_types_definition);
			}
			
			/**
			 * Process templates according to each service type
			 * Process "NodeType/Service.template", which will be merged into GeneralTypes-Definitions-XML.template file
			 * Process "Requirement_Capability/DatabaseContainer_DatabaseEndPointCapabilityType.template", which will be merged into GeneralTypes-Definitions-XML.template file
			 * Process RelationshipType/ServiceDatabaseHostedOnService.template, which will be merged into Service.template
			 * Process GeneralTypes_Diagram/Service.template, which will be merged into GeneralTypes-Diagram-XML.template
			 */
			
		    //velocity template for "NodeType/Service.template"
			Template template_service_node_type = ve_types_definition.getTemplate(TOSCATemplateConstants.SERVICE_NODE_TYPES_TEMPLATE);
			VelocityContext context_service_node_type = new VelocityContext();
			//velocity template for "Requirement_Capability/DatabaseContainer_DatabaseEndPointCapabilityType.template"
			Template template_dbcontainer_capability_type = ve_types_definition.getTemplate(TOSCATemplateConstants.DATABASE_CONTAINER_ENDPOINT_CAPABILITY_TYPES_DEFINITION_TEMPLATE);
			VelocityContext context_dbcontainer_capability_type = new VelocityContext();
			//velocity template for "RelationshipType/ServiceDatabaseHostedOnService.template"
			Template template_service_database_hostedon_relationship_type = ve_types_definition.getTemplate(TOSCATemplateConstants.SERVICE_DATABASE_HOSTEDON_RELATIONSHIP_TEMPLATE);
			VelocityContext context_service_database_hostedon_relationship_type = new VelocityContext();
			//velocity template for "GeneralTypes_Diagram/Service.template"
			Template template_service_diagram = ve_types_definition.getTemplate(TOSCATemplateConstants.SERVICE_TYPES_DIAGRAM_TEMPLATE);
			VelocityContext context_service_diagram  = new VelocityContext();
			String service_diagram = "";
			
			List<String> service_types = model.getServiceTypes();
		    if(service_types !=null && service_types.size()>0){
				
		    	for(String service_type : service_types){
		    		//Process "Requirement_Capability/DatabaseContainer_DatabaseEndPointCapabilityType.template"
		    		context_dbcontainer_capability_type.put(TOSCATemplateConstants.SERVICE_KEY, service_type);
		    		StringWriter writer_dbcontainer_capability_type = new StringWriter();
		    		template_dbcontainer_capability_type.merge(context_dbcontainer_capability_type, writer_dbcontainer_capability_type);
		    		dbcontainer_capability_types_definition = dbcontainer_capability_types_definition + writer_dbcontainer_capability_type.toString();
		    		
		    		//Process RelationshipType/ServiceDatabaseHostedOnService.template
		    		String databasehostedondbms_scriptid = UUID.randomUUID().toString();
		    		context_service_database_hostedon_relationship_type.put(TOSCATemplateConstants.SERVICE_KEY, service_type);
		    		context_service_database_hostedon_relationship_type.put(TOSCATemplateConstants.SERVICE_DATABASE_HOSTEDON_SERVICE_UUID, databasehostedondbms_scriptid);
		    		StringWriter writer_service_database_hostedon_relationship_type = new StringWriter();
		    		template_service_database_hostedon_relationship_type.merge(context_service_database_hostedon_relationship_type, writer_service_database_hostedon_relationship_type);		    		
		    		
		    		//Process "NodeType/Service.template"
		    		//Merge "RelationshipType/ServiceDatabaseHostedOnService.template" into Service.template
		    		//Merge Deployment Artifact/OS package template into Service.template
		    		String service_install_script_uuid = UUID.randomUUID().toString();
		    		String service_configure_script_uuid = UUID.randomUUID().toString();
		    		String service_start_script_uuid = UUID.randomUUID().toString();
		    		String service_stop_script_uuid = UUID.randomUUID().toString();
		    		String service_uninstall_script_uuid = UUID.randomUUID().toString();
		    		context_service_node_type.put(TOSCATemplateConstants.SERVICE_DATABASE_HOSTEDON_SERVICE_RELATIONSHIP_KEY, writer_service_database_hostedon_relationship_type.toString());
		    		context_service_node_type.put(TOSCATemplateConstants.SERVICE_KEY, service_type);
		    		context_service_node_type.put(TOSCATemplateConstants.SERVICE_INSTALL_SCRIPT_UUID_KEY, service_install_script_uuid);
		    		context_service_node_type.put(TOSCATemplateConstants.SERVICE_CONFIGURE_SCRIPT_UUID_KEY, service_configure_script_uuid);
		    		context_service_node_type.put(TOSCATemplateConstants.SERVICE_START_SCRIPT_UUID_KEY, service_start_script_uuid);
		    		context_service_node_type.put(TOSCATemplateConstants.SERVICE_STOP_SCRIPT_UUID_KEY, service_stop_script_uuid);
		    		context_service_node_type.put(TOSCATemplateConstants.SERVICE_UNINSTALL_SCRIPT_UUID_KEY, service_uninstall_script_uuid);
		    		
		    		//Process "GeneralTypes_Diagram/Service.template"
		    		context_service_diagram.put(TOSCATemplateConstants.SERVICE_KEY, service_type);
		    		StringWriter writer_service_diagram = new StringWriter();
		    		template_service_diagram.merge(context_service_diagram, writer_service_diagram);
		    		service_diagram = service_diagram + "\n" + writer_service_diagram.toString();
		    	    
		    		
		    		//To detect which type of DeploymentArtifacts should be used. 
					//For example, for service "mongo", using DeploymentArchiveArtifact; for runtime "mysql", may use DeploymentOsPackageArtifact
		    		
		    		if(AppModelConstants.SERVICE_DEPLOYMENT_OS_PACKAGE.containsKey(service_type)){
		    			
		    			//Process "NodeType/ServiceDeploymentArtifact/DeploymentOsPackageArtifactTemplate.template"
		    			 String os_package_artifact_definition = "";
						 String os_packages = AppModelConstants.SERVICE_DEPLOYMENT_OS_PACKAGE.get(service_type);
						 for(String os_package : os_packages.split(",")){
							 String os_package_artifact_definition_tmp =  "<PackageInformation packageName=" + "\"" + os_package.trim() + "\""+ "/>";
							 os_package_artifact_definition = os_package_artifact_definition + os_package_artifact_definition_tmp;
						 }
						 context_service_ospackage_template_artifact.put(TOSCATemplateConstants.OS_PACKAGE_ARTIFACT_DEFINITION, os_package_artifact_definition);
						 String service_os_package_artifact_uuid_key = UUID.randomUUID().toString();
						 context_service_ospackage_template_artifact.put(TOSCATemplateConstants.SERVICE_OS_PACKAGE_ARTIFACT_UUID_KEY, service_os_package_artifact_uuid_key);
						 StringWriter writer_service_ospackage_template_artifact = new StringWriter();
						 template_service_ospackage_template_artifact.merge(context_service_ospackage_template_artifact, writer_service_ospackage_template_artifact);
						 context_service_node_type.put(TOSCATemplateConstants.DEPLOYMENT_ARTIFACT_TEMPLATE_DEFINITION_KEY, writer_service_ospackage_template_artifact.toString());
						 
						 //Process NodeType/ServiceDeploymentArtifact/DeploymentOsPackageArtifact.template
						 context_service_ospackage_artifact.put(TOSCATemplateConstants.SERVICE_OS_PACKAGE_ARTIFACT_UUID_KEY, service_os_package_artifact_uuid_key);
						 StringWriter writer_service_ospackage_artifact = new StringWriter();
						 template_service_ospackage_artifact.merge(context_service_ospackage_artifact, writer_service_ospackage_artifact);
						 context_service_node_type.put(TOSCATemplateConstants.DEPLOYMENT_ARTIFACTS_TYPES_DEFINITION_KEY, writer_service_ospackage_artifact.toString());

		    		} 
		    		else{
			    		//Process "NodeType/ServiceDeploymentArtifact/DeploymentArchiveArtifact.template". service which should be installed from Archive Artifact
		    			String service_archive_artifact_uuid = UUID.randomUUID().toString();
		    			context_service_archive_artifact.put(TOSCATemplateConstants.SERVICE_DEPLOYMENT_ARCHIVE_ARTIFACT_UUID_KEY, service_archive_artifact_uuid);
		    			context_service_archive_artifact.put(TOSCATemplateConstants.SERVICE_KEY, service_type);
		    			String service_conf_artifact_uuid = UUID.randomUUID().toString();
		    			context_service_archive_artifact.put(TOSCATemplateConstants.SERVICE_CONF_ARTIFACT_UUID_KEY, service_conf_artifact_uuid);
		    			StringWriter writer_service_archive_artifact = new StringWriter();
		    			template_service_archive_artifact.merge(context_service_archive_artifact, writer_service_archive_artifact);
		    			context_service_node_type.put(TOSCATemplateConstants.DEPLOYMENT_ARTIFACTS_TYPES_DEFINITION_KEY, writer_service_archive_artifact.toString());
		    			
		    			//Process "NodeType/ServiceDeploymentArtifact/DeploymentArchiveArtifactTemplate.template"
		    			context_service_archive_template_artifact.put(TOSCATemplateConstants.SERVICE_DEPLOYMENT_ARCHIVE_ARTIFACT_UUID_KEY, service_archive_artifact_uuid);	
		    			context_service_archive_template_artifact.put(TOSCATemplateConstants.SERVICE_CONF_ARTIFACT_UUID_KEY, service_conf_artifact_uuid);
		    			context_service_archive_template_artifact.put(TOSCATemplateConstants.SERVICE_KEY, service_type);
		    			StringWriter writer_service_archive_artifact_template = new StringWriter();
		    			template_service_archive_template_artifact.merge(context_service_archive_template_artifact, writer_service_archive_artifact_template);
		    			context_service_node_type.put(TOSCATemplateConstants.DEPLOYMENT_ARTIFACT_TEMPLATE_DEFINITION_KEY, writer_service_archive_artifact_template.toString());
		    			
		    		}
		    		
		    		StringWriter writer_service_type = new StringWriter();
		    		template_service_node_type.merge(context_service_node_type, writer_service_type);
		    		service_types_definition = service_types_definition + writer_service_type.toString();
		    	}
		    	
		    } else{
		    	context_types_definition_xml.put(TOSCATemplateConstants.SERVICE_NODE_TYPES_KEY, "");
		    	context_types_definition_xml.put(TOSCATemplateConstants.DATABASE_CONTAINER_ENDPOINT_CAPABILITY_TYPES_KEY, "");
		    	context_service_node_type.put(TOSCATemplateConstants.SERVICE_DATABASE_HOSTEDON_SERVICE_RELATIONSHIP_KEY, "");
		    }
		    
		    /**
			 *Merge dbcontainer_capability_types_definition into GeneralTypes-Definitions-XML.template file
			 *
			 */
			if(dbcontainer_capability_types_definition.trim().length() == 0){
				context_types_definition_xml.put(TOSCATemplateConstants.DATABASE_CONTAINER_ENDPOINT_CAPABILITY_TYPES_KEY, "");
			} else{
				context_types_definition_xml.put(TOSCATemplateConstants.DATABASE_CONTAINER_ENDPOINT_CAPABILITY_TYPES_KEY, dbcontainer_capability_types_definition);
			}
			
			/**
			 *Merge service_types_definition into GeneralTypes-Definitions-XML.template file
			 *
			 */
			if(service_types_definition.trim().length() == 0){
				context_types_definition_xml.put(TOSCATemplateConstants.SERVICE_NODE_TYPES_KEY, "");
			} else{
				context_types_definition_xml.put(TOSCATemplateConstants.SERVICE_NODE_TYPES_KEY, service_types_definition);
			}
			
			/**
			 *Merge service_diagram into GeneralTypes-Diagram-XML.template file
			 *
			 */
			if(service_diagram.trim().length() == 0){
				context_general_types_diagram.put(TOSCATemplateConstants.SERVICE_NODE_DIAGRAM_KEY, "");
			} else{
				context_general_types_diagram.put(TOSCATemplateConstants.SERVICE_NODE_DIAGRAM_KEY, service_diagram);
			}
		    
		    /**
			 * Process templates according to each service instance
			 * Process "NodeType/ServiceInstance.template", which will be merged into GeneralTypes-Definitions-XML.template file
			 * Process "GeneralTypes_Diagram/DB.template", which will be merged into GeneralTypes-Doiagram-XML.template file
			 */
			
			//velocity template for "GeneralTypes-Diagram-XML.template"
			 Template template_service_instance_diagram = ve_types_definition.getTemplate(TOSCATemplateConstants.SERVICE_INSTANCE_TYPES_DIAGRAM_TEMPLATE);
			 VelocityContext context_service_instance_diagram  = new VelocityContext();
			 String service_instance_diagram = "";
		    List<ServiceNode> service_instances = model.getServices();
		    if(service_types !=null && service_types.size()>0){
		    	for(ServiceNode service_instance : service_instances){
		    		String service_instance_name = service_instance.getName();
		    		String service_instance_type = service_instance.getServiceType().toString();
		    	    String db_install_script_id = UUID.randomUUID().toString();
		    	    String db_start_script_id = UUID.randomUUID().toString();
		    	    String db_uninstall_script_id = UUID.randomUUID().toString();
		    	    //Process "NodeType/ServiceInstance.template"
		    		context_service_instance_types.put(TOSCATemplateConstants.SERVICE_INSTANCE_KEY, service_instance_name);
		    		context_service_instance_types.put(TOSCATemplateConstants.SERVICE_KEY, service_instance_type);
		    		context_service_instance_types.put(TOSCATemplateConstants.DB_INSTALL_SCRIPT_UUID_KEY, db_install_script_id);
		    		context_service_instance_types.put(TOSCATemplateConstants.DB_START_SCRIPT_UUID_KEY, db_start_script_id);
		    		context_service_instance_types.put(TOSCATemplateConstants.DB_UNINSTALL_SCRIPT_UUID_KEY, db_uninstall_script_id);
		    		StringWriter writer_service_instance_types = new StringWriter();
		    		template_service_instance_types.merge(context_service_instance_types, writer_service_instance_types);
		    		service_instance_types_definition = service_instance_types_definition + writer_service_instance_types.toString();
		    		
		    		//Process "GeneralTypes_Diagram/DB.template"
		    		context_service_instance_diagram.put(TOSCATemplateConstants.SERVICE_INSTANCE_KEY, service_instance_name);
		    		StringWriter writer_service_instance_diagram = new StringWriter();
		    		template_service_instance_diagram.merge(context_service_instance_diagram, writer_service_instance_diagram);
		    		service_instance_diagram = service_instance_diagram + "\n" + writer_service_instance_diagram.toString();
		    		
		    	}
		    	
		    } else{
		    	context_types_definition_xml.put(TOSCATemplateConstants.SERVICE_INSTANCE_NODE_TYPES_KEY, "");
		    }
		    
		    /**
			 *Merge service_instance_diagram into GeneralTypes-Diagram-XML.template file
			 *
			 */
			if(service_instance_diagram.trim().length() == 0){
				context_general_types_diagram.put(TOSCATemplateConstants.SERVICE_INSTANCE_NODE_DIAGRAM_KEY, "");
			} else{
				context_general_types_diagram.put(TOSCATemplateConstants.SERVICE_INSTANCE_NODE_DIAGRAM_KEY, service_instance_diagram);
			}
		    
		    /**
			 *Merge service_instance_types_definition into GeneralTypes-Definitions-XML.template file
			 *
			 */
			if(service_instance_types_definition.trim().length() == 0){
				context_types_definition_xml.put(TOSCATemplateConstants.SERVICE_INSTANCE_NODE_TYPES_KEY, "");
			} else{
				context_types_definition_xml.put(TOSCATemplateConstants.SERVICE_INSTANCE_NODE_TYPES_KEY, service_instance_types_definition);
			}
			
//			System.out.println("service_instance_types_definition is: " + service_instance_types_definition);
			
			
			/**
			 * Process templates according to each application
			 */
			
			//Velocity template definition
			
			//velocity template for "GeneralTypes-Diagram/App.template"
			Template template_app_diagram = ve_types_definition.getTemplate(TOSCATemplateConstants.APP_TYPES_DIAGRAM_TEMPLATE);
			VelocityContext context_app_diagram  = new VelocityContext();
			String app_diagram = "";
			
			List<WebAppNode> webapps = model.getWebApps();
			for(WebAppNode webapp : webapps){
				String app_name = webapp.getName();
				String hostedOnRuntime = webapp.getHostedOnContainer().getName();
				
				/**
				 * Process GeneralTypes_Diagram/App.template, which will be merged into GeneralType-Diagram-XML.template
				 */
				context_app_diagram.put(TOSCATemplateConstants.APP_NAME_KEY, app_name);
				StringWriter writer_app_diagram = new StringWriter();
				template_app_diagram.merge(context_app_diagram, writer_app_diagram);
				app_diagram = app_diagram + "\n" + writer_app_diagram.toString();
				
				
				/**
				 * Process "Requirement_Capability/DatabaseEndPointRequirementType.template"
				 *Get the DatabaseEndPoint Requirement Types Definition which will be merged into GeneralTypes-Definitions-XML.template file
				 */
				
				List<String> connectToServiceTypes = model.getAppConnectsToServicesTypes(app_name);
				if(connectToServiceTypes !=null && connectToServiceTypes.size()>0){
					for(String connectToServiceType : connectToServiceTypes){
					     context_db_endpoint_requirement_types.put(TOSCATemplateConstants.SERVICE_KEY, connectToServiceType);
					     context_db_endpoint_requirement_types.put(TOSCATemplateConstants.APP_NAME_KEY, app_name);
						 StringWriter writer1 = new StringWriter();
						 template_db_endpoint_requirement_types.merge(context_db_endpoint_requirement_types, writer1);
						 String value1 = writer1.toString();
						 databaseendpoint_requirement_types_definition = databaseendpoint_requirement_types_definition + "\n" + value1;
					}
				}
				
				/**
				 *Process corresponding template file which will be merged into Application.template file 
				 */
				String app_start_script_uuid = UUID.randomUUID().toString();
				String app_archive_artifact_uuid = UUID.randomUUID().toString();
				context_app_types_definition.put(TOSCATemplateConstants.APP_NAME_KEY, app_name);
				context_app_types_definition.put(TOSCATemplateConstants.RUNTIME_KEY, hostedOnRuntime);
				context_app_types_definition.put(TOSCATemplateConstants.APP_START_SCRIPT_UUID_KEY, app_start_script_uuid);
				context_app_types_definition.put(TOSCATemplateConstants.APP_ARCHIVE_ARTIFACT_UUID_KEY, app_archive_artifact_uuid);
				
				List<String> connectToServicesTypes = model.getAppConnectsToServicesTypes(app_name);
				if(connectToServicesTypes !=null && connectToServicesTypes.size()>0){
					
					/**
					 *Process "NodeType/DatabaseEndPointRequirementDefinition.template", "RelationshipType/ApplicationDatabaseConnection.template"
					 *Which will be merged into "NodeType/Application.template"
					 */
					
					//Get the DatabaseEndPoint Requirement& database connection relationship Definition which will be merged into Application.template file
					String databaseendpoint_requirement_definition = "";
					String databaseconnection_relationship_definition = "";
					for(String connectToServicesType : connectToServicesTypes){
						context_db_endpoint_requirement_definition.put(TOSCATemplateConstants.APP_NAME_KEY, app_name);
						context_db_endpoint_requirement_definition.put(TOSCATemplateConstants.SERVICE_KEY, connectToServicesType);
						StringWriter writer = new StringWriter();
						template_db_endpoint_requirement_definition.merge(context_db_endpoint_requirement_definition, writer);
					    String value = writer.toString();
					    databaseendpoint_requirement_definition = databaseendpoint_requirement_definition + "\n" + value;
					    
					    String app_databaseconnection_script_id = UUID.randomUUID().toString();
					    context_app_databaseconnection_relationship_types_definition.put(TOSCATemplateConstants.APP_NAME_KEY, app_name);
					    context_app_databaseconnection_relationship_types_definition.put(TOSCATemplateConstants.SERVICE_KEY, connectToServicesType);
					    context_app_databaseconnection_relationship_types_definition.put(TOSCATemplateConstants.APP_DATABASE_CONNECTION_SCRIPT_UUID_KEY, app_databaseconnection_script_id);
					    StringWriter writer_connection_relationship = new StringWriter();
					    template_app_databaseconnection_relationship_types_definition.merge(context_app_databaseconnection_relationship_types_definition, writer_connection_relationship);
					    String value_connection_relationship = writer_connection_relationship.toString();
					    databaseconnection_relationship_definition = databaseconnection_relationship_definition + "\n" + value_connection_relationship;
					}
					//Merge databaseendpoint_requirement_definition into application.template file
					if(databaseendpoint_requirement_definition.trim().length() == 0){
						//if there is no database target, just clean up DB_ENDPOINT_REQUIREMENT in Application.template
						context_app_types_definition.put(TOSCATemplateConstants.DATABASEENDPOINT_REQUIREMENT_DEFINITION_KEY, "");
					} else{
						//write DatabaseEndpointRequirement.template to Application_Definition.template
						context_app_types_definition.put(TOSCATemplateConstants.DATABASEENDPOINT_REQUIREMENT_DEFINITION_KEY, databaseendpoint_requirement_definition);
					}
					
					//Merge databaseconnection_relationship_definition into application.template file
					if(databaseconnection_relationship_definition.trim().length() == 0){
						context_app_types_definition.put(TOSCATemplateConstants.APPLICATION_DATABASE_CONNECTION_RELATIONSHIP_TYPES_KEY, "");
					} else{
						context_app_types_definition.put(TOSCATemplateConstants.APPLICATION_DATABASE_CONNECTION_RELATIONSHIP_TYPES_KEY, databaseconnection_relationship_definition);
					}
					
				} else{
					context_app_types_definition.put(TOSCATemplateConstants.DATABASEENDPOINT_REQUIREMENT_DEFINITION_KEY, "");
					context_app_types_definition.put(TOSCATemplateConstants.APPLICATION_DATABASE_CONNECTION_RELATIONSHIP_TYPES_KEY, "");
				}
				
				/**
				 *Process "NodeType/DependsOnRequirement.template", "RelationshipType/ApplicationDependsOn.template", which will be merged into "NodeType/Application.template"
				 *Process "Requirement_Capability/ApplicationDependsOnRequirement.template", which will be merged into "GeneralTypes-Definitions-XML.template"
				 */
							
				List<WebAppNode> dependsOnApps = webapp.getDependsOnApps();
				if(dependsOnApps != null && dependsOnApps.size()>0){
					
					//Modify "NodeType/DependsOnRequirement.template"
					context_app_dependson_requirement_definition.put(TOSCATemplateConstants.APP_NAME_KEY, app_name);
					StringWriter writer = new StringWriter();
					template_app_dependson_requirement_definition.merge(context_app_dependson_requirement_definition, writer);
				    context_app_types_definition.put(TOSCATemplateConstants.DEPENDSON_REQUIREMENT_DEFINITION_KEY, writer.toString());
				   
				    String app_dependson_relationship_types_definition = "";
					for(WebAppNode dependsOnApp : dependsOnApps){
						
						//Modify RelationshipType/ApplicationDependsOn.template
						String app_dependson_script_uuid = UUID.randomUUID().toString();
						context_app_dependson_relationship_types_definition.put(TOSCATemplateConstants.DEPENDSON_SOURCE_KEY, app_name);
						context_app_dependson_relationship_types_definition.put(TOSCATemplateConstants.DEPENDSON_TARGET_KEY, dependsOnApp.getName());
						context_app_dependson_relationship_types_definition.put(TOSCATemplateConstants.APP_DEPENDSON_SCRIPT_UUID_KEY, app_dependson_script_uuid);
						StringWriter writer_depends_relationshiptype = new StringWriter();
						template_app_dependson_relationship_types_definition.merge(context_app_dependson_relationship_types_definition, writer_depends_relationshiptype);
					    app_dependson_relationship_types_definition = app_dependson_relationship_types_definition + "\n" + writer_depends_relationshiptype.toString();
					    
					    //Modify "Requirement_Capability/ApplicationDependsOnRequirement.template"
					    context_app_dependson_requirement_types_definition.put(TOSCATemplateConstants.DEPENDSON_SOURCE_KEY, app_name);
					    context_app_dependson_requirement_types_definition.put(TOSCATemplateConstants.DEPENDSON_TARGET_KEY, dependsOnApp.getName());
					    StringWriter writer_dependson_requirement_type = new StringWriter();
					    template_app_dependson_requirement_types_definition.merge(context_app_dependson_requirement_types_definition, writer_dependson_requirement_type);
					    app_dependson_requirement_types_definition = app_dependson_requirement_types_definition + "\n" + writer_dependson_requirement_type.toString();
					}
					
					//Merge "NodeType/DependsOnRequirement.template" into application.template file
					if(app_dependson_relationship_types_definition.trim().length() == 0){
						//if there is no app_dependson target, just clean up it in Application.template
						context_app_types_definition.put(TOSCATemplateConstants.APPLICATION_DEPENDSON_RELATIONSHIP_TYPES_KEY, "");
					} else{
						context_app_types_definition.put(TOSCATemplateConstants.APPLICATION_DEPENDSON_RELATIONSHIP_TYPES_KEY, app_dependson_relationship_types_definition);
					}
					
				} else{
					context_app_types_definition.put(TOSCATemplateConstants.DEPENDSON_REQUIREMENT_DEFINITION_KEY, "");
					context_app_types_definition.put(TOSCATemplateConstants.APPLICATION_DEPENDSON_RELATIONSHIP_TYPES_KEY, "");
				}
				
				/**
				 *Process "NodeType/DependedOnCapability.template", which will be merged into "NodeType/Application.template"
				 *
				 */

				//Get the Application DependedOn Capability Definition which will be merged into Application.template file
				List<WebAppNode> dependedOnApps = webapp.getDependedOnApps();
				if(dependedOnApps != null && dependedOnApps.size()>0){
					//Merge app_dependedon_capability_definition into application.template file
					context_app_dependedon_capability_definition.put(TOSCATemplateConstants.APP_NAME_KEY, app_name);
					StringWriter writer = new StringWriter();
					template_app_dependedon_capability_definition.merge(context_app_dependedon_capability_definition, writer);
					context_app_types_definition.put(TOSCATemplateConstants.DEPENDEDON_CAPABILITY_DEFINITION_KEY, writer.toString());
				} else{
					context_app_types_definition.put(TOSCATemplateConstants.DEPENDEDON_CAPABILITY_DEFINITION_KEY, "");
				}
				
				/**
				 *Get the application types definition string, which will be merged into GeneralTypes-Definitions-XML.template file
				 *
				 */

				StringWriter writer_app_types_definition = new StringWriter();
				app_types_definition_template.merge(context_app_types_definition, writer_app_types_definition);
				app_types_definition = app_types_definition + "\n" + writer_app_types_definition.toString();
			}
			
			/**
			 * END: Process templates according to each application
			 */
			
			/**
			 *Merge "GeneralTypes_Diagram/App.template" into GeneralTypes-Diagram-XML.template file
			 *
			 */
			if(app_diagram.trim().length() == 0){
				context_general_types_diagram.put(TOSCATemplateConstants.APP_NODE_DIAGRAM_KEY, "");
			} else{
				context_general_types_diagram.put(TOSCATemplateConstants.APP_NODE_DIAGRAM_KEY, app_diagram);
			}
			
			/**
			 *Merge databaseendpoint_requirement_types_definition into GeneralTypes-Definitions-XML.template file
			 *
			 */
			
			if(databaseendpoint_requirement_types_definition.trim().length() == 0){
				context_types_definition_xml.put(TOSCATemplateConstants.DATABASEENDPOINT_REQUIREMENT_TYPES_DEFINITION_KEY, "");
			} else{
				context_types_definition_xml.put(TOSCATemplateConstants.DATABASEENDPOINT_REQUIREMENT_TYPES_DEFINITION_KEY, databaseendpoint_requirement_types_definition);
			}
			
			/**
			 *Merge app_dependson_requirement_types_definition into GeneralTypes-Definitions-XML.template file
			 *
			 */
			if(app_dependson_requirement_types_definition.trim().length() == 0){
				context_types_definition_xml.put(TOSCATemplateConstants.APP_DEPENDSON_REQUIREMENT_TYPES_KEY, "");
			} else{
				context_types_definition_xml.put(TOSCATemplateConstants.APP_DEPENDSON_REQUIREMENT_TYPES_KEY, app_dependson_requirement_types_definition);
			}
			
			/**
			 *Merge application_node_types_definition into GeneralTypes-Definitions-XML.template file
			 *
			 */
			if(app_types_definition.trim().length() == 0){
				context_types_definition_xml.put(TOSCATemplateConstants.APP_TYPES_DEFINITION_KEY, "");
			} else{
				context_types_definition_xml.put(TOSCATemplateConstants.APP_TYPES_DEFINITION_KEY, app_types_definition);
			}
			
			/**
			 *Generate the whole GeneralTypes-Definition-XML string
			 *
			 */
			
			StringWriter writer_types_definition_xml = new StringWriter();
			types_definition_xml_template.merge(context_types_definition_xml, writer_types_definition_xml);
			general_types_definition_file = general_types_definition_file + "\n" + writer_types_definition_xml.toString();
			//System.out.println("types_definition xml is: " + general_types_definition_file);
			
			/**
			 *Generate the whole GeneralTypes-Diagram-XML string
			 *
			 */
			
			StringWriter writer_types_diagram_xml = new StringWriter();
			template_general_types_diagram.merge(context_general_types_diagram, writer_types_diagram_xml);
			general_types_definition_diagram_file = general_types_definition_diagram_file + "\n" + writer_types_diagram_xml.toString();
			System.out.println("types_definition diagram xml is: " + general_types_definition_diagram_file);
			
			/**
			 *Write GeneralTypes-Definition-XML string  to the config
			 *
			 */
			config.setProperty(TOSCATemplateConstants.TYPES_DEFINITION_XML_FILE, general_types_definition_file);
			config.setProperty(TOSCATemplateConstants.TYPES_DIAGRAM_DEFINITION_XML_FILE, general_types_definition_diagram_file);
			
		}
			
	}
	
	
	public static String getProperty(String key) {
		String value = config.getProperty(key);
		return value == null ? value : value.trim();
	}
	
	public static void main(String[] agrs){
		//GenerateGeneralTypesDefinitionOnHeroku.generateGeneralTypesDefinitionFile();
	}

}
