package com.ibm.cloud.migration.appmodel.model;

public class Env {

	/**
	 * @author   chuanran
	 */
	public static enum EnvType {
		webapp_url,
		service_url,
		normal
	}
	
	EnvType type;
	String name;
	String value;
	String default_value;
	/**
	 * @return  the type
	 */
	public EnvType getType() {
		return type;
	}
	/**
	 * @param type  the type to set
	 */
	public void setType(EnvType type) {
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
	 * @return  the value
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * @return the default value
	 */
	public String getDefaultValue() {
		return default_value;
	}
	/**
	 * @param value  the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * @param value the value to set
	 */
	public void setDefaultValue(String default_value) {
		this.default_value = default_value;
	}
	
	
}
