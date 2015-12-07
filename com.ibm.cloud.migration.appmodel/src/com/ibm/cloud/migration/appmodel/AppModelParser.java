package com.ibm.cloud.migration.appmodel;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import com.ibm.cloud.migration.appmodel.model.AppContainerNode;
import com.ibm.cloud.migration.appmodel.model.AppModel;
import com.ibm.cloud.migration.appmodel.model.AppModel.Source;
import com.ibm.cloud.migration.appmodel.model.Env;
import com.ibm.cloud.migration.appmodel.model.Node;
import com.ibm.cloud.migration.appmodel.model.Node.NodeType;
import com.ibm.cloud.migration.appmodel.model.Property;
import com.ibm.cloud.migration.appmodel.model.Relationship;
import com.ibm.cloud.migration.appmodel.model.Relationship.RelationshipType;
import com.ibm.cloud.migration.appmodel.model.ServiceNode;
import com.ibm.cloud.migration.appmodel.model.WebAppNode;

public class AppModelParser {

	List<Property> parseProperies(Element element){
		List<Property> list = new ArrayList<Property>();
		if(element!=null){
			List<Element> propElms = element.getChildren("property");
			for(Element elm : propElms){
				String name = elm.getAttributeValue("name");
				String value = elm.getAttributeValue("value");
				
				Property prop = new Property();
				prop.setName(name);
				prop.setValue(value);
				list.add(prop);
			}
		}
		
		return list;
	}
	
	List<Env> parseEnvs(Element element){
		List<Env> envs = new ArrayList<Env>();
		List<Element> elms = element.getChildren("env");
		for(Element elm : elms){
			Env env = new Env();
			env.setName(elm.getAttributeValue("name"));
			env.setType(Env.EnvType.valueOf(elm.getAttributeValue("type")));
			env.setValue(elm.getAttributeValue("value"));
			env.setDefaultValue(elm.getAttributeValue("default_value"));
			envs.add(env);
		}
		return envs;
	}
	
	void parseWebAppNode(WebAppNode node, Element element){
		Element envElm = element.getChild("envs");
		if(envElm!=null){
			node.setEnvs(parseEnvs(envElm));
		}
	}
	
	void parseServiceNode(ServiceNode node, Element element){
		node.setServiceType(ServiceNode.ServiceType.valueOf(element.getAttributeValue("serviceType")));
	}
	
	Node parseNode(Element element, AppModel model){
		String name = element.getAttributeValue("name");
		String type = element.getAttributeValue("type");
		
		Node node = null;
		NodeType nodeType = NodeType.valueOf(type);
		if(nodeType.equals(NodeType.webapp)){
			node = new WebAppNode(model);
			parseWebAppNode((WebAppNode)node, element);
		}else if(nodeType.equals(NodeType.applicationContainer)){
			node = new AppContainerNode(model);
		}else if(nodeType.equals(NodeType.service)){
			node = new ServiceNode(model);
			parseServiceNode((ServiceNode)node, element);
		}
		node.setName(name);		
		Element props = element.getChild("properties");
		node.setProperties(parseProperies(props));
		return node;
	}
	
	List<Node> parseNodes(Element element, AppModel model){
		List<Element> nodeElems = element.getChildren("node");
		List<Node> nodes = new ArrayList<Node>(nodeElems.size());
		for(Element elm : nodeElems){
			nodes.add(parseNode(elm, model));
		}
		
		return nodes;
	}
	
	List<Relationship> parseRelationships(Element element){
		List<Relationship> list = new ArrayList<Relationship>();
		List<Element> elms = element.getChildren("relationship");
		for(Element elm : elms){
			String sourceNode = elm.getAttributeValue("sourceNode");
			String targetNode = elm.getAttributeValue("targetNode");
			String type = elm.getAttributeValue("type");
			Relationship relation = new Relationship();
			relation.setSourceNode(sourceNode);
			relation.setTargetNode(targetNode);
			relation.setType(RelationshipType.valueOf(type));
			list.add(relation);
		}
		return list;
	}
	
	void parseRoot(Element root, AppModel model){
		 model.setSource(Source.valueOf(root.getAttributeValue("source")));
		 
		 Element nodesElm = root.getChild("nodes");
		 Element relationshipsElm = root.getChild("relationships");
		 
		 model.setNodes(parseNodes(nodesElm, model));
		 model.setRelationships(parseRelationships(relationshipsElm));
	}
	
	public AppModel parse(String modelFile) throws AppModelException{
		AppModel model = new AppModel();
		try{
			 SAXBuilder sax = new SAXBuilder();  
			 Document doc = sax.build(modelFile);
			 Element root = doc.getRootElement();
			 
			 parseRoot(root, model);
			 
	
			
		}catch(Exception e){
			e.printStackTrace();
			throw new AppModelException("failed to parse " + modelFile, e);
		}
		
		return model;
	}

}
