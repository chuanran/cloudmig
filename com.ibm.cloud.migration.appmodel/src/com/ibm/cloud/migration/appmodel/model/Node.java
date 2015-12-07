package com.ibm.cloud.migration.appmodel.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class Node {

	/**
	 * @author   chuanran
	 */
	public enum NodeType{
		webapp,
		applicationContainer,
		service
	}
	NodeType type;
	String name;
	Map<String, Property> properties;
	AppModel appModel;
	
	public Node(AppModel appModel) {
		this.appModel = appModel;
	}
	/**
	 * @return  the type
	 */
	public NodeType getType() {
		return type;
	}

	/**
	 * @param type  the type to set
	 */
	public void setType(NodeType type) {
		this.type = type;
	}

	/**
	 * @return  the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name  the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the properties
	 */
	public Collection<Property> getProperties() {
		return properties.values();
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(Collection<Property> properties) {
		this.properties = new HashMap<String, Property>();
		for(Property prop : properties){
			this.properties.put(prop.getName(), prop);
		}
	}
	
	public void setProperty(String propName, String propValue){
		if(this.properties==null){
			this.properties = new HashMap<String, Property>();
		}
		Property prop = new Property();
		prop.setName(propName);
		prop.setValue(propValue);
		properties.put(propName, prop);		
	}

	
	public String getPropertyValue(String propertyName){
		if(this.properties==null){
			return null;
		}
		return properties.get(propertyName).getValue();
	}

	/**
	 * @return  the appModel
	 * @uml.property  name="appModel"
	 */
	protected AppModel getAppModel() {
		return appModel;
	}
	
	
}
