package com.ibm.cloud.migration.appmodel.model;

import com.ibm.cloud.migration.appmodel.AppModelException;

public class AppContainerNode extends Node {

	/**
	 * @author   chuanran
	 */
	public static enum BuildPack {
		/**
		 * @uml.property  name="nodeJS"
		 * @uml.associationEnd  
		 */
		NodeJS
	}
	
	static final String BUILD_PACK_PROP = "buildpack";
	
	
	public AppContainerNode(AppModel appModel) {
		super(appModel);
		setType(NodeType.applicationContainer);
	}

	public void seteBuildPack(BuildPack buildpack){
		setProperty(BUILD_PACK_PROP, buildpack.toString());
	}

	public BuildPack getBuildPack() throws AppModelException {
		String value = getPropertyValue(BUILD_PACK_PROP);
		if(value==null){
			throw new AppModelException("build pack is not set");
		}
		if(value.toLowerCase().startsWith("node")){
			return BuildPack.NodeJS;
		}else{
			throw new AppModelException("build pack " + value +" is not supported");
		}
		
	}
}
