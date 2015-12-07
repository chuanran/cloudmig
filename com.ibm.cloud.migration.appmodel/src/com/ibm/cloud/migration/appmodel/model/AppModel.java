package com.ibm.cloud.migration.appmodel.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AppModel {

	public static enum Source {
		cf,
		heroku
	}
	
	Source source;	
	Map<String, Node> nodes;
	Set<Relationship> relationships;


	/**
	 * @return  the source
	 */
	public Source getSource() {
		return source;
	}
	/**
	 * @param source  the source to set
	 */
	public void setSource(Source source) {
		this.source = source;
	}
	/**
	 * @return the nodes
	 */
	public List<Node> getNodes() {
		List<Node> l = new ArrayList<Node>(nodes.size());
		l.addAll(nodes.values());
		return l;
	}
	
	public void addNode(Node node){
		if(nodes==null){
			nodes = new HashMap<String, Node>();
		}
		nodes.put(node.getName(), node);
	}

	/**
	 * @param nodes the nodes to set
	 */
	public void setNodes(List<Node> nodes) {
		this.nodes = new HashMap<String, Node>();
		for(Node node : nodes){
			this.nodes.put(node.getName(), node);
		}
	}

	/**
	 * @return the relationships
	 */
	public List<Relationship> getRelationships() {
		List<Relationship> list = new ArrayList<Relationship>();
		if(relationships!=null){
			list.addAll(relationships);
		}
		return list;
	}
	
	public void addRelationship(Relationship relation){
		if(relationships==null){
			relationships = new HashSet<Relationship>();
		}
		relationships.add(relation);
	}
	/**
	 * @param relationships the relationships to set
	 */
	public void setRelationships(List<Relationship> relationships) {
		this.relationships = new HashSet<Relationship>();
		this.relationships.addAll(relationships);
	}

	
	public Node getNodeByName(String nodeName){
		for(Node node : getNodes()){
			if(node.getName().equals(nodeName)){
				return node;
			}
		}
		return null;
	}

	
	public List<WebAppNode> getWebApps(){
		List<WebAppNode> webapps = new ArrayList<WebAppNode>();
		for(Node node : getNodes()){
			if(node instanceof WebAppNode){
				webapps.add((WebAppNode)node);
			}
		}
		return webapps;
	}

	
	public List<ServiceNode> getServices(){
		List<ServiceNode> services = new ArrayList<ServiceNode>();
		for(Node node : getNodes()){
			if(node instanceof ServiceNode){
				services.add((ServiceNode)node);
			}
		}
		return services;
	}

	
	public List<String> getServiceTypes(){
		List<String> service_types = new ArrayList<String>();
		for(Node node : getNodes()){
			if(node instanceof ServiceNode){
				service_types.add(((ServiceNode)node).getServiceType().toString());
			}
		}
		HashSet h  =  new  HashSet(service_types);
		service_types.clear();
		service_types.addAll(h);
		return service_types;
	}

	/**
	 *  
	 * @param appName
	 * @return null if not found. 
	 */
	public AppContainerNode getAppHostedOnContainer(String appName){
		for(Relationship r : relationships){
			if(r.getType().equals(Relationship.RelationshipType.hostedOn)
					&& r.getSourceNode().equals(appName)){
				return (AppContainerNode)getNodeByName(r.getTargetNode());
			}
		}
		return null;
	}
	
	public List<WebAppNode> getAppDependsOnApps(String appName){
		List<WebAppNode> list = new ArrayList<WebAppNode>();
		for(Relationship r : relationships){
			if(r.getType().equals(Relationship.RelationshipType.dependsOn)
					&& r.getSourceNode().equals(appName)){				
				list.add((WebAppNode)getNodeByName(r.getTargetNode()));
			}
		}
		return list;
	}
	
	public List<WebAppNode> getAppDependedOnApps(String appName){
		List<WebAppNode> list = new ArrayList<WebAppNode>();
		for(Relationship r : relationships){
			if(r.getType().equals(Relationship.RelationshipType.dependsOn) 
					&& r.getTargetNode().equals(appName)){
				list.add((WebAppNode)getNodeByName(r.getSourceNode()));
			}
		
		}
		return list;
	}
	
	public boolean hasDependsOnRequirement(String appName){
		boolean is_dependson_app = false;
		for(Relationship r : relationships){
			if(r.getType().equals(Relationship.RelationshipType.dependsOn) 
					&& r.getSourceNode().equals(appName)){
				is_dependson_app = true;
				break;
			}
		}
		return is_dependson_app;
	}
	
	public boolean hasDependedOnCapability(String appName){
		boolean is_dependedon_app = false;
		for(Relationship r : relationships){
			if(r.getType().equals(Relationship.RelationshipType.dependsOn) 
					&& r.getTargetNode().equals(appName)){
				is_dependedon_app = true;
				break;
			}
		}
		return is_dependedon_app;
	}
	
	public List<String> getAppNamesWithDependedOnCapability(){
		List<String> list = new ArrayList<String>();
		for(Relationship r : relationships){
			if(r.getType().equals(Relationship.RelationshipType.dependsOn) &&r.getTargetNode()!= null && r.getTargetNode().length() !=0){
				list.add(((WebAppNode)getNodeByName(r.getTargetNode())).getName());
			}
		}
		HashSet h  =  new  HashSet(list);
		list.clear();
		list.addAll(h);
		return list;
	}
	
	public List<String> getAppNamesWithDependsOnRequirement(){
		List<String> list = new ArrayList<String>();
		for(Relationship r : relationships){
			if(r.getType().equals(Relationship.RelationshipType.dependsOn) &&r.getSourceNode()!= null && r.getSourceNode().length() !=0){
				list.add(((WebAppNode)getNodeByName(r.getSourceNode())).getName());
			}
		}
		HashSet h  =  new  HashSet(list);
		list.clear();
		list.addAll(h);
		return list;
	}
	
	public List<ServiceNode> getAppConnectsToServices(String appName){
		List<ServiceNode> list = new ArrayList<ServiceNode>();
		for(Relationship r : relationships){
			if(r.getType().equals(Relationship.RelationshipType.connectTo)
					&& r.getSourceNode().equals(appName)){				
				list.add((ServiceNode)getNodeByName(r.getTargetNode()));
			}
		}
		return list;
	}
	
	public List<String> getAppConnectsToServicesTypes(String appName){
		List<String> list = new ArrayList<String>();
		for(Relationship r : relationships){
			if(r.getType().equals(Relationship.RelationshipType.connectTo)
					&& r.getSourceNode().equals(appName)){				
				list.add(((ServiceNode)getNodeByName(r.getTargetNode())).getServiceType().toString());
			}
		}
		HashSet h  =  new  HashSet(list);
		list.clear();
		list.addAll(h);
		return list;
	}
	
	public List<WebAppNode> getAppsConnectedWithService(String serviceName){
		List<WebAppNode> list = new ArrayList<WebAppNode>();
		
		for(Relationship r : relationships){
			if(r.getType().equals(Relationship.RelationshipType.connectTo)
					&& r.getTargetNode().equals(serviceName)){				
				list.add(((WebAppNode)getNodeByName(r.getSourceNode())));
			}
		}
		HashSet h  =  new  HashSet(list);
		list.clear();
		list.addAll(h);
		return list;
	}
}
