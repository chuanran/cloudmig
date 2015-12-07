package com.ibm.cloud.migration.appmodel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.ibm.cloud.migration.appmodel.model.AppModel;
import com.ibm.cloud.migration.appmodel.model.Env;
import com.ibm.cloud.migration.appmodel.model.Node;
import com.ibm.cloud.migration.appmodel.model.Property;
import com.ibm.cloud.migration.appmodel.model.Relationship;
import com.ibm.cloud.migration.appmodel.model.ServiceNode;
import com.ibm.cloud.migration.appmodel.model.WebAppNode;

public class AppModelSerializer {

	private Element buildNode(Node node){
		Element nodeElm = new Element("node");
		nodeElm.setAttribute("type", node.getType().toString());
		nodeElm.setAttribute("name", node.getName());
		if(node instanceof ServiceNode){
			nodeElm.setAttribute("serviceType", ((ServiceNode) node).getServiceType().toString());
		}
		
		// properties
		Element props = new Element("properties");
		nodeElm.addContent(props);
		for(Property prop : node.getProperties()){
			Element propElm = new Element("property");			
			propElm.setAttribute("name", prop.getName());
			propElm.setAttribute("value", prop.getValue());
			props.addContent(propElm);
		}
		
		if(node instanceof WebAppNode){
			List<Env> envs = ((WebAppNode) node).getEnvs();
			Element envsElm = new Element("envs");
			nodeElm.addContent(envsElm);
			
			for(Env env : envs){
				Element envElm = new Element("env");
				envElm.setAttribute("name", env.getName());
				envElm.setAttribute("value", env.getValue());
				envElm.setAttribute("default_value", env.getDefaultValue());
				envElm.setAttribute("type", env.getType().toString());
				envsElm.addContent(envElm);
			}
		}
		
		return nodeElm;
	}
	
	private Element buildRelation(Relationship relation){
		Element relationElm = new Element("relationship");
		relationElm.setAttribute("type", relation.getType().toString());
		relationElm.setAttribute("sourceNode", relation.getSourceNode());
		relationElm.setAttribute("targetNode", relation.getTargetNode());
		
		return relationElm;
	}
	
	private Document buildDoc(AppModel model) {
        Element root = new Element("applicationModel");
        root.setAttribute("source",model.getSource().toString());
        
        // build nodes
        Element nodesElm = new Element("nodes");
        root.addContent(nodesElm);
        List<Node> nodes = model.getNodes();
        for(Node node : nodes){
        	nodesElm.addContent(buildNode(node));
        }
        
        Element relationsElm = new Element("relationships");
        root.addContent(relationsElm);
        List<Relationship> relations = model.getRelationships();
        for(Relationship relation : relations){
        	relationsElm.addContent(buildRelation(relation));
        }
        
        Document doc = new Document(root);        
		return doc;
	}

	/**
	 * serialize model to output stream as XML
	 * 
	 * @param model
	 * @param output
	 */
	public void serialize(AppModel model, OutputStream output)
			throws AppModelException {
		XMLOutputter XMLOut = new XMLOutputter();
		XMLOut.setFormat(Format.getPrettyFormat());

		Document doc = buildDoc(model);

		try {
			XMLOut.output(doc, output);
		} catch (IOException e) {
			e.printStackTrace();
			throw new AppModelException("failed to serialize app model", e);
		}
	}
}
