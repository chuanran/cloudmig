package com.ibm.cloud.migration.appmodel.model;

public class ServiceNode extends Node {

	/**
	 * @author   chuanran
	 */
	public static enum ServiceType {
		mongo,
		mysql
	}
	ServiceType serviceType;
	public ServiceNode(AppModel appModel) {
		super(appModel);
		setType(NodeType.service);
	}

	/**
	 * @return  the serviceType
	 */
	public ServiceType getServiceType() {
		return serviceType;
	}

	/**
	 * @param serviceType  the serviceType to set
	 */
	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}
	
	
	
}
