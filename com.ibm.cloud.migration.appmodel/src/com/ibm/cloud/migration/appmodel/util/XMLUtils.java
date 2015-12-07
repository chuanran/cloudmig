package com.ibm.cloud.migration.appmodel.util;

import org.jdom2.Element;

public class XMLUtils {

	public static Element buildAppModel(){
		return new Element("applicationModel").setAttribute("source","heroku");
	}
	
	public static Element buildNodes(){
		return new Element("nodes");
	}
	
	public static Element buildRelationships(){
		return new Element("relationships");
	}
	
	public static Element buildAppContainer(String buildPack){
		if(buildPack.equals("Node.js")){
			
//		<node type="applicationContainer" name="nodeJSServer">
//			<properties>
//				<property name="framework" value="node" />
//				<property name="runtime" value="node08" />
//			</properties>
//		</node>
			Element node = new Element("node");
			node.setAttribute("type", "applicationContainer");
			node.setAttribute("name", "nodeJSServer");

			Element props = new Element("properties");
			node.addContent(props);
			return node;
		}else{
			throw new UnsupportedOperationException("build pack "+buildPack+" is not supported");
		}
	}
	
	public static Element buildWebApp(String name, int instances){	
			
//		<node type="webapp" name="countly-ui">
//			<properties>
//				<property name="instances" value="1" />
//				<property name="memory" value="256M" />
//				<property name="port" value="6001" />
//				<property name="urlPrefix" value="countly-ui" />
//			</properties>
//		</node>
		Element node = new Element("node");
		node.setAttribute("type", "webapp");
		node.setAttribute("name", name);

		Element props = new Element("properties");
		props.addContent(new Element("property")
			.setAttribute("name","instances").setAttribute("value",instances+""));
		props.addContent(new Element("property")
			.setAttribute("name","memory").setAttribute("value","512M"));
		props.addContent(new Element("property")
			.setAttribute("name","urlPrefix").setAttribute("value",name));
		
		
		node.addContent(props);
		return node;
	}
	
	public static Element buildService(String appName, String addonName){	
		
//	<node type="service" name="countly-mongo" serviceType="database">
//		<properties>
//			<property name="dbName" value="countly" />
//			<property name="vendor" value="mongodb" />
//			<property name="version" value="2.0" />
//		</properties>
//	</node>
		if(!addonName.startsWith("mongo")){
			throw new UnsupportedOperationException("Addon " + addonName+" is not supported");
		}
		Element node = new Element("node");
		node.setAttribute("type", "service");
		node.setAttribute("serviceType", "mongo");
		node.setAttribute("name",appName+"-mongo");
		
		Element props = new Element("properties");
		props.addContent(new Element("property")
			.setAttribute("name","dbName").setAttribute("value",appName+"DB"));
		props.addContent(new Element("property")
			.setAttribute("name","vendor").setAttribute("value","mongodb"));
		props.addContent(new Element("property")
			.setAttribute("name","version").setAttribute("value","2.0"));
		
		node.addContent(props);
		return node;
	}
	
	public static Element buildDependency(String type, String src, String target){	
		Element node = new Element("relationship");
		node.setAttribute("type", type);
		node.setAttribute("sourceNode", src);
		node.setAttribute("targetNode",target);
		
		return node;
	}
}
