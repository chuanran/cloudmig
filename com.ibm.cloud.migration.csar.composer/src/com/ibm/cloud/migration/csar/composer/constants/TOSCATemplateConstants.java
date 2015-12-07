package com.ibm.cloud.migration.csar.composer.constants;

public class TOSCATemplateConstants {
	
	//Constants for General Definitions/Diagram template
	 public static final String  GENERAL_DEFINITION_XML_KEY = "GeneralDefinitionXml.template";
	 public static final String  GENERAL_DEFINITION_DIAGRAM_XML_KEY = "GeneralDiagramXml.template";
	 public static final String GENERAL_DEFINITION_FILE_KEY = "general_definition_file";
	 public static final String GENERAL_DEFINITION_DIAGRAM_FILE_KEY = "general_definition_diagram_file";
	 public static final String GENERAL_DEFINITION_XML_FILE_KEY = "general_definition_xml_file";
	 public static final String GENERAL_DEFINITION_DIAGRAM_XML_FILE_KEY = "general_definition_diagram_xml_file";
		
		//Constants for Vm_Os_Definition.template
	    public static final String VM_OS_DEFINITION_TEMPLATE = "Vm_Os_Definition.template";
		public static final String VM_ROLE_KEY = "ROLE";
		public static final String VM_MEMORY_VALUE_KEY = "VM_Memory_Value";
		public static final String VM_SERVICE_DEFAULT_MEMORY_VALUE = "1024";
		public static final String RUNTIME_VMOS_DEFINITION_FILE = "RuntimeVmOsDefinition";
		public static final String SERVICE_VMOS_DEFINITION_FILE = "ServiceVmOsDefinition";
		public static final String VM_RUNTIME_DIAGRAM_UUID_KEY = "VmRUNTIME_ID";
		public static final String OS_RUNTIME_DIAGRAM_UUID_KEY = "OsRUNTIME_ID";
		public static final String OS_SERVICE_DIAGRAM_UUID_KEY = "OsSERVICE_ID";
		public static final String VM_SERVICE_DIAGRAM_UUID_KEY = "VmSERVICE_ID";
		
		//Constants for Service & Service Instance Definition/Diagram template
		public static final String SERVICE_DEFINITION_TEMPLATE = "Service_Definition.template";
		public static final String VM_OS_SERVICE_DEFINITION_DIAGRAM_TEMPLATE = "General_Diagram/General-VmOsService-Diagram.template";
		public static final String SERVICE_INSTANCE_DEFINITION_TEMPLATE = "Service_Instance_Definition.template";
		public static final String SERVICE_INSTANCE_KEY = "DBName";
		public static final String SERVICE_PORT_KEY = "DBPort";
		public static final String SERVICE_DEFAULT_PORT = "27017";
		public static final String SERVICE_KEY = "SERVICE";
		public static final String GENERAL_SERVICE_KEY = "GENERAL_SERVICE";
		public static final String SERVICE_DIAGRAM_UUID_KEY = "SERVICE_ID";
		public static final String SERVICE_INSTANCE_DIAGRAM_UUID_KEY = "DB_ID";
		
		//Constants for Runtime Server Definition/Diagram template
		public static final String RUNTIME_SERVER_DEFINITION_TEMPLATE = "RuntimeServer_Definition.template";
		public static final String VM_OS_RUNTIME_DEFINITION_DIAGRAM_TEMPLATE = "General_Diagram/General-VmOsRuntime-Diagram.template";
		public static final String GENERAL_RUNTIME_KEY = "GENERAL_RUNTIME";
		public static final String RUNTIME_KEY = "RUNTIME";
		public static final String APP_PORT_DEFINITION_KEY = "app_port_complete_definition";
		public static final String RUNTIME_DIAGRAM_UUID_KEY = "RuntimeServer_ID";
		
		
		
		
		//Constants for Application Definition/Diagram template
		public static final String APP_DEFINITION_TEMPLATE = "Application_Definition.template";
		public static final String APP1_DEFINITION_DIAGRAM_TEMPLATE = "General_Diagram/General-App1-Diagram.template";
		public static final String APP2_DEFINITION_DIAGRAM_TEMPLATE = "General_Diagram/General-App2-Diagram.template";
		public static final String APP_DB_ENDPOINT_REQUIREMENT_DEFINITION_TEMPLATE = "DatabaseEndpointRequirement.template";
		public static final String APP_DEPENDSON_REQUIREMENT_TEMPLATE = "DependsOnRequirement.template";
		public static final String APP_DEPENDSON_CAPABILITY_TEMPLATE = "DependedOnCapability.template";
		public static final String APP_CONNECTSTO_RELATIONSHIP_TEMPLATE = "ConnectToRelationship.template";
		public static final String APP_DEPENDSON_RELATIONSHIP_TEMPLATE = "DependsOnRelationship.template";
		public static final String APP_URL_ENV_VAR_TEMPLATE = "AppURLEnvironmentVariable.template";
		public static final String APP_SERVICE_URL_ENV_VAR_TEMPLATE = "ServiceURLEnvironmentVariable.template";

		public static final String APP_GENERAL_NAME_KEY = "AppGeneralName";
		public static final String APP_NAME_KEY = "AppName";
		public static final String APP_PORT_KEY = "App_Port_Value";
		public static final String APP_URL_ENV_VAR_KEY = "Web_App_URL_environment_variable";
		public static final String APP_URL_ENV_VAR_IN_URL_ENV_KEY = "depended_app_url";
		public static final String APP_SERVICE_URL_ENV_VAR_IN_SERVICE_URL_ENV_KEY = "service_url";
		public static final String APP_SERVICE_URL_ENV_VAR_KEY = "Service_URL_environment_variable";
		public static final String DB_ENDPOINT_REQUIREMENT_KEY = "DatabaseEndPointRequirement";
		public static final String DEPENDSON_REQUIREMENT_KEY = "DependsOnRequirement";
		public static final String DEPENDSON_CAPABILITY_KEY = "DependedOnCapability";
		public static final String CONNECTTO_RELATIONSHIP_KEY = "ConnectToRelationship";
		public static final String DEPENDSON_RELATIONSHIP_KEY = "DependsOnRelationship";
		
		//Constants for DependsOnRelationship between applications
		public static final String DEPENDSON_SOURCE_KEY = "App2Name";
		public static final String DEPENDSON_TARGET_KEY = "App1Name";
		public static final String APP2_ID_KEY = "App2ID";
		public static final String APP1_ID_KEY = "App1ID";
		
		//Constants for HostedOn Relationship
		public static final String RUNTIME_HOSTEDON_OS_RUNTIME_DIAGRAM_UUID_KEY =  "RUNTIME_HostedOn_Os_RUNTIME_ID";
		public static final String OS_RUNTIME_HOSTEDON_VM_RUNTIME_DIAGRAM_UUID_KEY =  "Os_RUNTIME_HostedOn_Vm_RUNTIME_ID";
		public static final String SERVICE_HOSTEDON_OS_SERVICE_DIAGRAM_UUID_KEY =  "SERVICE_HostedOn_Os_SERVICE_ID";
		public static final String OS_SERVICE_HOSTEDON_VM_SERVICE_DIAGRAM_UUID_KEY =  "Os_SERVICE_HostedOn_Vm_SERVICE_ID";
		public static final String SERVICE_INSTANCE_HOSTEDON_SERVICE_DIAGRAM_UUID_KEY = "SERVICE_Instance_HostedOn_SERVICE_ID";
		
		public static final String APP2_HOSTEDON_RUNTIME_DIAGRAM_UUID_KEY = "App2_HostedOn_RUNTIME_ID";
		public static final String APP2_CONNECTSTO_SERVICE_INSTANCE_DIAGRAM_UUID_KEY = "App2_ConnectsTo_SERVICE_Instance_ID";
		public static final String APP2_DEPENDSON_APP1_UUID_KEY = "App2_DependsOn_App1_ID";
		public static final String APP1_HOSTEDON_RUNTIME_DIAGRAM_UUID_KEY = "App1_HostedOn_RUNTIME_ID";
		public static final String APP1_CONNECTSTO_SERVICE_INSTANCE_DIAGRAM_UUID_KEY = "App1_ConnectsTo_SERVICE_Instance_ID";
		
		
  
   //Constants for General Types Definitions/Diagram template
		public static final String  TYPES_DEFINITION_XML_TEMPLATE = "GeneralTypes-Definitions-XML.template";
		public static final String  DATABASEENDPOINT_REQUIREMENT_CAPAPILITY_TYPES_DEFINITION_KEY = "Database_Endpoint_Requirement_Capability_Types";
		public static final String  APP_TYPES_DEFINITION_KEY = "Application_NodeType";
		
		//Constants for Runtime Server Types Definition /Types Diagram template
		public static final String RUNTIME_SERVER_TYPES_DEFINITION_TEMPLATE = "NodeType/RunTimeServer_Application.template";
		public static final String RUNTIME_SERVER_TYPES_DEFINITION_KEY= "Runtime_Server_NodeType";
		public static final String RUNTIME_INSTALL_SCRIPT_UUID_KEY = "Runtime_InstallScript_ID";
		public static final String RUNTIME_CONFIGURE_SCRIPT_UUID_KEY = "Runtime_ConfigureScript_ID";
		public static final String RUNTIME_START_SCRIPT_UUID_KEY = "Runtime_StartScript_ID";
		public static final String RUNTIME_STOP_SCRIPT_UUID_KEY = "Runtime_StopScript_ID";
		public static final String RUNTIME_UNINSTALL_SCRIPT_UUID_KEY = "Runtime_UninstallScript_ID";
		
		public static final String DEPLOYMENT_ARTIFACTS_TYPES_DEFINITION_KEY = "DeploymentArtifacts_Definition";
		public static final String DEPLOYMENT_ARTIFACT_TEMPLATE_DEFINITION_KEY = "Deployment_Artifact_Template";
		
		public static final String RUNTIME_DEPLOYMENT_OS_PACKAGE_ARTIFACT_UUID_KEY = "RuntimeDeploymentOsPackageArtifact_ID";
		public static final String RUNTIME_DEPLOYMENT_OS_PACKAGE_ARTIFACT = "NodeType/RuntimeDeploymentArtifact/DeploymentOsPackageArtifact.template";
		public static final String RUNTIME_DEPLOYMENT_OS_PACKAGE_ARTIFACT_TEMPLATE = "NodeType/RuntimeDeploymentArtifact/DeploymentOsPackageArtifactTemplate.template";
		public static final String RUNTIME_OS_PACKAGE_NAME_KEY = "RuntimeOsPackageName";
		
		public static final String RUNTIME_DEPLOYMENT_ARCHIVE_ARTIFACT_UUID_KEY = "Runtime_DeploymentArchiveArtifact_ID";
		public static final String RUNTIME_DEPLOYMENT_ARCHIVE_ARTIFACT = "NodeType/RuntimeDeploymentArtifact/DeploymentArchiveArtifact.template";
		public static final String RUNTIME_DEPLOYMENT_ARCHIVE_ARTIFACT_TEMPLATE = "NodeType/RuntimeDeploymentArtifact/DeploymentArchiveArtifactTemplate.template";
		
		
		//Constants for Application Types Definition /Types Diagram template
		
		public static final String  TYPES_DEFINITION_XML_FILE = "types_definition_xml";
		public static final String  TYPES_DIAGRAM_DEFINITION_XML_FILE = "types_diagram_definition_xml";
		public static final String  APPLICATION_TYPES_DEFINITION_TEMPLATE = "NodeType/Application.template";
		public static final String  APPLICATION_TYPES_DATABASE_ENDPOINT_REQUIREMENT_TEMPLATE = "NodeType/DatabaseEndPointRequirementDefinition.template";
		public static final String APPLICATION_TYPES_DEPENDEDON_CAPABILITY = "NodeType/DependedOnCapability.template";
		public static final String APPLICATION_TYPES_DEPENDSON_REQUIREMENT = "NodeType/DependsOnRequirement.template";
		public static final String APP_START_SCRIPT_UUID_KEY = "App_StartScriptID";
		public static final String APP_ARCHIVE_ARTIFACT_UUID_KEY = "App_ArchiveArtifactID";
		
		public static final String DATABASEENDPOINT_REQUIREMENT_DEFINITION_KEY = "DatabaseEndPointRequirementDefinition";
		public static final String DEPENDEDON_CAPABILITY_DEFINITION_KEY = "DependedOnCapabilityDefinition";
		public static final String DEPENDSON_REQUIREMENT_DEFINITION_KEY = "DependsOnRequirementDefinition";
		public static final String DATABASEENDPOINT_REQUIREMENT_TYPES_DEFINITION_KEY = "Database_Endpoint_Requirement_Types";
		//Constants for Relationship Types Definition  template
		public static final String  APPLICATION_DEPENDSON_RELATIONSHIP_TYPES_KEY = "ApplicationDependsOnRelationshipTypes";
		public static final String  APP_DEPENDSON_SCRIPT_UUID_KEY ="App_DependsOn_Script_ID";
		public static final String  APPLICATION_DEPENDSON_RELATIONSHIP_TYPES_TEMPLATE = "RelationshipType/ApplicationDependsOn.template";
		
		public static final String  APPLICATION_DATABASE_CONNECTION_RELATIONSHIP_TYPES_KEY = "ApplicationDatabaseConnectionRelationshipTypes";
		public static final String APP_DATABASE_CONNECTION_SCRIPT_UUID_KEY = "AppDatabaseConnection_ScriptID";
		public static final String  APPLICATION_DATABASE_CONNECTION_RELATIONSHIP_TYPES_TEMPLATE = "RelationshipType/ApplicationDatabaseConnection.template";
	
		//Constants for  Requirement/capability Types Definition  template
		
		public static final String DATABASEENDPOINT_REQUIREMENT_TYPES_DEFINITION_TEMPLATE = "Requirement_Capability/DatabaseEndPointRequirementType.template";
		public static final String DATABASE_CONTAINER_ENDPOINT_CAPABILITY_TYPES_DEFINITION_TEMPLATE = "Requirement_Capability/DatabaseContainer_DatabaseEndPointCapabilityType.template";
		public static final String DATABASE_CONTAINER_ENDPOINT_CAPABILITY_TYPES_KEY = "Database_Container_Endpoint_Capability_Types";
		public static final String APPLICATION_DEPENDSON_REQUIREMENT_TYPES_DEFINITION_TEMPLATE = "Requirement_Capability/ApplicationDependsOnRequirement.template";
		public static final String APP_DEPENDSON_REQUIREMENT_TYPES_KEY  = "App_DependsOn_Requirement_Types";
		public static final String APPLICATION_DEPENDEDON_CAPABILITY_TYPES_DEFINITION_TEMPLATE = "Requirement_Capability/ApplicationDependedOnCapability.template";
		public static final String APP_DEPENDEDON_CAPABILITY_TYPES_KEY  = "App_DependedOn_Capability_Types";
		public static final String APPLICATION_CONTAINER_REQUIREMENT_CAPABILITY_TEMPLATE = "Requirement_Capability/ApplicationContainer.template";
		public static final String APP_CONTAINER_REQUIRMENT_CAPABILITY_TYPES_KEY  = "App_Container_Requirement_Capability_Types";
		
		//Constants for Service&Service instance Types Definition /Types Diagram template
		public static final String SERVICE_NODE_TYPES_TEMPLATE = "NodeType/Service.template";
		public static final String SERVICE_NODE_TYPES_KEY  = "Service_NodeType";
		public static final String SERVICE_INSTANCE_TYPES_TEMPLATE = "NodeType/ServiceInstance.template";
		public static final String SERVICE_INSTANCE_NODE_TYPES_KEY  = "Service_Instance_NodeType";
		
		public static final String SERVICE_OS_PACKAGE_ARTIFACT_UUID_KEY  = "ServiceDeploymentOsPackageArtifact_ID";
		public static final String SERVICE_DEPLOYMENT_ARCHIVE_ARTIFACT_UUID_KEY = "Service_DeploymentArtifact_ID";
		public static final String  OS_PACKAGE_ARTIFACT_DEFINITION = "Os_Package_Definition";
		public static final String  SERVICE_OS_PACKAGE_ARTIFACT_TEMPLATE = "NodeType/ServiceDeploymentArtifact/DeploymentOsPackageArtifact.template";
		public static final String  SERVICE_OS_PACKAGE_TEMPLATE_ARTIFACT_TEMPLATE = "NodeType/ServiceDeploymentArtifact/DeploymentOsPackageArtifactTemplate.template";
		public static final String  SERVICE_ARCHIVE_ARTIFACT_TEMPLATE = "NodeType/ServiceDeploymentArtifact/DeploymentArchiveArtifactTemplate.template";
		public static final String  SERVICE_ARCHIVE_ARTIFACT = "NodeType/ServiceDeploymentArtifact/DeploymentArchiveArtifact.template";
		
		public static final String  SERVICE_CONF_ARCHIVE_ARTIFACT = "NodeType/ServiceConfigurationArtifact/ConfigurationArchiveArtifact.template";
		public static final String  SERVICE_CONF_ARCHIVE_ARTIFACT_TEMPLATE = "NodeType/ServiceConfigurationArtifact/ConfigurationArchiveArtifactTemplate.template";
		public static final String  SERVICE_CONF_ARTIFACT_UUID_KEY = "ServiceConfArtifactID";

		
		public static final String SERVICE_DATABASE_HOSTEDON_SERVICE_RELATIONSHIP_KEY  = "Service_Database_HostedOn_Service_Relationship_Types";
		public static final String SERVICE_DATABASE_HOSTEDON_SERVICE_UUID  = "DataBaseHostedOnDBMS_ScriptID";
		public static final String SERVICE_DATABASE_HOSTEDON_RELATIONSHIP_TEMPLATE = "RelationshipType/ServiceDatabaseHostedOnService.template";
		public static final String SERVICE_INSTALL_SCRIPT_UUID_KEY = "Service_InstallScript_ID";
		public static final String SERVICE_CONFIGURE_SCRIPT_UUID_KEY = "Service_ConfigureScript_ID";
		public static final String SERVICE_START_SCRIPT_UUID_KEY = "Service_StartScript_ID";
		public static final String SERVICE_STOP_SCRIPT_UUID_KEY = "Service_StopScript_ID";
		public static final String SERVICE_UNINSTALL_SCRIPT_UUID_KEY = "Service_UninstallScript_ID";
		
		
		public static final String SERVICE_INSTANCE_NODE_TYPES_TEMPLATE = "NodeType/ServiceInstance.template";
		public static final String DB_INSTALL_SCRIPT_UUID_KEY = "DB_InstallScriptID";
		public static final String DB_START_SCRIPT_UUID_KEY = "DB_StartScriptID";
		public static final String DB_UNINSTALL_SCRIPT_UUID_KEY = "DB_UninstallScriptID";
		public static final String SERVICE_INSTANCE_NODE_TYPE_KEY = "Service_Instance_NodeType";
		
		public static final String GENERAL_TYPES_DIAGRAM_TEMPLATE = "GeneralTypes-Diagram-XML.template";
		public static final String APP_TYPES_DIAGRAM_TEMPLATE = "GeneralTypes_Diagram/App.template";
		public static final String SERVICE_INSTANCE_TYPES_DIAGRAM_TEMPLATE = "GeneralTypes_Diagram/DB.template";
		public static final String SERVICE_TYPES_DIAGRAM_TEMPLATE = "GeneralTypes_Diagram/Service.template";
		public static final String RUNTIME_TYPES_DIAGRAM_TEMPLATE = "GeneralTypes_Diagram/Runtime.template";
		public static final String RUNTIME_NODE_DIAGRAM_KEY = "Runtime_Node_Diagram";
		public static final String SERVICE_NODE_DIAGRAM_KEY = "Service_Node_Diagram";
		public static final String APP_NODE_DIAGRAM_KEY =  "Application_Node_Diagram";
		public static final String SERVICE_INSTANCE_NODE_DIAGRAM_KEY =  "ServiceInstance_Node_Diagram";
		
		
		//Constants for scripts template
		public static final String  APP_START_POINT_KEY = "Start_Point_Dir";
		public static final String  VCAP_SERVICES_EXPORT_KEY = "Export_VCAP_SERVICES";
		public static final String  EXPORT_ENV_VARIABLES_KEY = "Export_Env_Variables";
		public static final String  SERVICE_FIREWALL_SETTING_KEY = "Service_Firewall_Setting";
		public static final String  HEROKU_ENV_TEMPLATE = "scripts_template/Relationship/ConnectTo/DatabaseConnection/heroku/ENV_VARIABLES.template";
		public static final String  ENV_NAME_KEY = "ENV_NAME";
		public static final String  VCAP_SERVICES_TEMPLATE = "scripts_template/Relationship/ConnectTo/DatabaseConnection/cf/VCAP_SERVICES.template";
		public static final String  APP_BINDING_TEMPLATE = "scripts_template/Relationship/DependsOn/applicationbind/application_bind.sh";
		public static final String  TARGET_APP_PORT_KEY = "Target_AppPort";
		
		//Constants for types template
		
		public static final String  APP_TYPES_KEY = "Application_Types";
		public static final String  SERVICE_INSTANCE_TYPES_KEY = "Service_Instance_Types";
		
		
	}