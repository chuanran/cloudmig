package com.ibm.cloud.migration.csar.composer.utility;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ParseXML {
	private static HashMap<String, String> application_attr_map = new HashMap<String, String>();
	private static List<HashMap<String, String>> application_attr_list = new ArrayList<HashMap<String, String>>();
	private static Map<String, List<HashMap<String, String>>> application_map;
	private static final String cloud_name_key = "source";
	private static String cloudName = "";
	private static final String nodes_element = "nodes";
	private static final String node_element = "node";
	private static final String properties_element = "properties";
	private static final String property_element = "property";
	private static final String envs_element = "envs";
	private static final String env_element = "env";
	private static final String type_key = "type";
	private static final String app_type = "webapp";
	private static final String name_key = "name";
	private static final String value_key = "value";
	static{
		try {  
            SAXReader reader = new SAXReader();  
            try {
				Class config_class = Class
				.forName("com.ibm.cloud.migration.csar.composer.utility.ParseXML");
				InputStream in = config_class.getResourceAsStream("countly-cloudfoundry.xml");  
				Document doc = reader.read(in);  
				Element root = doc.getRootElement();  
				cloudName = root.attributeValue(cloud_name_key);
				for(Iterator i = root.elementIterator(); i.hasNext();){ 
					Element nodes = (Element)i.next();
					if(nodes.getName().equalsIgnoreCase(nodes_element)){
						System.out.println("parsing nodes starting");
						for(Iterator j = nodes.elementIterator(); j.hasNext();){
							Element node = (Element)j.next();
							if(node.getName().equalsIgnoreCase(node_element)){
								//System.out.println("this is a node");
								String node_type = node.attributeValue(type_key);
								if(node_type.equalsIgnoreCase(app_type)){
									String app_name = node.attributeValue(name_key);
									for(Iterator k = node.elementIterator(); k.hasNext();){
										Element properties = (Element)k.next();
										if(properties.getName().equalsIgnoreCase(properties_element)){
											for(Iterator l = properties.elementIterator(); l.hasNext();){
												Element property = (Element)l.next();
												if(property.getName().equalsIgnoreCase(property_element)){
													String property_key = node.attributeValue(name_key);
													String property_value = node.attributeValue(value_key);
													application_attr_map.put(property_key, property_value);
													application_attr_list.add(application_attr_map);
												}
											}
										}
									}
									application_map.put(app_name, application_attr_list);
									//System.out.println(app_name);
								}
							}
						}
					} else{
						System.out.println("the element should be nodes");
					}
					
				}
				
			} catch (ClassNotFoundException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
              
        } catch (DocumentException e) {  
            e.printStackTrace();  
        }  
	
	}
		
	
  
//    public static void read1() {  
//        try {  
//            SAXReader reader = new SAXReader();  
//            try {
//				Class config_class = Class
//				.forName("com.ibm.cloud.migration.csar.composer.utility.ParseXML");
//				InputStream in = config_class.getResourceAsStream("example.xml");  
//				Document doc = reader.read(in);  
//				Element root = doc.getRootElement();  
//		        readNode(root, "");
//			} catch (ClassNotFoundException e) {
//				// TODO 自动生成 catch 块
//				e.printStackTrace();
//			}
//              
//        } catch (DocumentException e) {  
//            e.printStackTrace();  
//        }  
//    }  
//      
//    @SuppressWarnings("unchecked")  
//    public static void readNode(Element root, String prefix) {  
//        if (root == null) return;  
//        // 获取属性  
//        List<Attribute> attrs = root.attributes();  
//        if (attrs != null && attrs.size() > 0) {  
//            System.err.print(prefix);  
//            for (Attribute attr : attrs) {
//            	//if(attr.getName().equalsIgnoreCase("dependsOn"))
//                System.err.print(attr.getName() +attr.getValue() + " ");  
//            }  
//            System.err.println();  
//        }  
//        // 获取他的子节点  
//        List<Element> childNodes = root.elements();  
//        prefix += "\t";  
//        for (Element e : childNodes) {  
//            readNode(e, prefix);  
//        }  
//    }
	
	public static String getCloudSourceName(){
		return cloudName;
	}
	
	public static String  getHostedOnContainer(String appname){
		return null;
	}
	
	public List<String> getDependsOnApps(){
		return null;
	}
	
	public List<String> getConnectsToServices(){
		return null;
	}
    
    public static void main(String[] args) {  
    	System.out.println(getCloudSourceName());
//        read1();  
        //read2();  
        //write();  
    }  

}
