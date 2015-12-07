package com.ibm.cloud.migration.appmodel.model;

import java.util.List;

public class WebAppNode extends Node {

	static final String INSTANCES_PROP = "instances";
	static final String MEMORY_PROP = "memory";
	static final String PORT_PROP = "port";

	List<Env> envs;
	
	
	public WebAppNode(AppModel appModel) {
		super(appModel);		
		setType(NodeType.webapp);
	}

	public String getInstances(){
		String v = getPropertyValue(INSTANCES_PROP);
		if(v==null){
			v = "1"; // default to be 1
		}
		return v;
	}

	public void setInstances(String instances){
		setProperty(INSTANCES_PROP, instances);		
	}

	public void setMemory(String memeory){
		setProperty(MEMORY_PROP, memeory);
	}
	
	public void setPort(String port){
		setProperty(PORT_PROP, port);
	}

	public String getMemory(){
		String v = getPropertyValue(MEMORY_PROP);
		if(v==null){
			v = "512"; // default to be 512M
		}
		return v;
	}
	
	public String getPort(){
		String v = getPropertyValue(PORT_PROP);
		if(v==null){
			v = "5000"; // default to be 5000
		}
		return v;
	}

	/**
	 * @return the envs
	 */
	public List<Env> getEnvs() {
		return envs;
	}

	/**
	 * @param envs the envs to set
	 */
	public void setEnvs(List<Env> envs) {
		this.envs = envs;
	}
	
	public AppContainerNode getHostedOnContainer(){
		return getAppModel().getAppHostedOnContainer(this.getName());
	}
	
	public List<WebAppNode> getDependsOnApps(){
		return getAppModel().getAppDependsOnApps(this.getName());
	}
	
	public List<WebAppNode> getDependedOnApps(){
		return getAppModel().getAppDependedOnApps(this.getName());
	}
	
	
	public List<ServiceNode> getConnectsToServices(){
		return getAppModel().getAppConnectsToServices(this.getName());
	}
	
}
