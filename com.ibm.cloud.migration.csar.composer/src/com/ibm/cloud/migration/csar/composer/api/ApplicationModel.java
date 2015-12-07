package com.ibm.cloud.migration.csar.composer.api;

import java.util.List;


/**
 * @author chuanran
 *
 */

public interface ApplicationModel {
	
	/**
	 * 
	 *
	 * @return the Cloud Name application hosted on, such as: Cloudfoundry, heroku, etc
	 */
	public String getCloudSourceName();
	
	/**
	 * 
	 *
	 * @return the Virtual machine number brought up in SCO for runtime role
	 */
	
	public int getRuntimeVmNum();

	/**
	 * 
	 *
	 * @return the runtime name list
	 */
	
	public List<String> getRuntimeNameList();
	
	/**
	 * 
	 *
	 * @return the memory reservation value for the application
	 */
	
	public String getAppReservedMemoryValue();
	
	/**
	 * 
	 *
	 * @return the application number
	 */
	
	public int getAppNum();
	
	/**
	 * 
	 *
	 * @return the application name list
	 */
	
	public List<String> getAppNameList();
	
	/**
	 * @param application name
	 *
	 * @return the runtime name which is hosted on by the defined application
	 */
	
	public String getAppHostedOnTargetRuntime(String appname);
	
	/**
	 * @param application name
	 *
	 * @return the database name which is consumed by the defined application
	 */
	
	public String getAppConnectToTargetDb(String appname);
	
	/**
	 * @param application name
	 *
	 * @return the application name which is depended on by the defined application
	 */
	
	public String getAppDependsOnTargetApp(String appname);
	
	public int getServiceVmNum();
	
	/**
	 * 
	 *
	 * @return the service name list
	 */
	
	public List<String> getServiceNameList();
	
	
	
	
	

}
