package com.ibm.cloud.migration.cf;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.domain.CloudApplication;
import org.cloudfoundry.client.lib.domain.CloudService;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import com.ibm.cloud.migration.appmodel.AppModelException;
import com.ibm.cloud.migration.appmodel.AppModelSerializer;
import com.ibm.cloud.migration.appmodel.model.AppContainerNode;
import com.ibm.cloud.migration.appmodel.model.AppContainerNode.BuildPack;
import com.ibm.cloud.migration.appmodel.model.AppModel;
import com.ibm.cloud.migration.appmodel.model.AppModel.Source;
import com.ibm.cloud.migration.appmodel.model.Env;
import com.ibm.cloud.migration.appmodel.model.Env.EnvType;
import com.ibm.cloud.migration.appmodel.model.Relationship;
import com.ibm.cloud.migration.appmodel.model.Relationship.RelationshipType;
import com.ibm.cloud.migration.appmodel.model.ServiceNode;
import com.ibm.cloud.migration.appmodel.model.ServiceNode.ServiceType;
import com.ibm.cloud.migration.appmodel.model.WebAppNode;
//import com.ibm.cloud.migration.utility.Passwd;


public class Exporter {

	static class Dependency {
		CloudApplication source;
		CloudApplication target;		

		public Dependency(){
			
		}
		public Dependency(CloudApplication src, CloudApplication target){
			this.source = src;
			this.target = target;
		}
		
		
		/**
		 * @return the source
		 */
		public CloudApplication getSource() {
			return source;
		}
		/**
		 * @return the target
		 */
		public CloudApplication getTarget() {
			return target;
		}
		/**
		 * @param source the source to set
		 */
		public void setSource(CloudApplication source) {
			this.source = source;
		}
		/**
		 * @param target the target to set
		 */
		public void setTarget(CloudApplication target) {
			this.target = target;
		}
		
		
		
	}
	
	private List<CloudApplication> selectApps(List<CloudApplication> apps){
		List<CloudApplication> selectedApps = new ArrayList<CloudApplication>(apps.size());
		System.out.println("Selecting applications to export (use application number,separated by comma):");
		for(int i=1;i<=apps.size();i++){
			System.out.println(i+"."+apps.get(i-1).getName());
		}
		Scanner reader=new Scanner(System.in);
		String line = "";
		try {
			 line = reader.nextLine();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			reader.close();
		}
		//FIXME: more robust input handling
		String[] sels = line.split(",");		
		for(int i=0;i<sels.length;i++){
			selectedApps.add(apps.get(Integer.parseInt(sels[i])-1));
		}
		
		return selectedApps;
	}
	
	private Map<String, List<Dependency>> specifyDeps(List<CloudApplication> apps){
		Map<String, List<Dependency>> deps = new HashMap<String, List<Dependency>>();
		
		Scanner reader=new Scanner(System.in);
		String line = "";
		
		while(true){		
			//System.out.println("Specify Dependency Using format A->B  (A depends on B)?(Y/N)");
			//FIXME: more robust input handling
			try {
				line = reader.nextLine();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				reader.close();
			}
			
			if(line.isEmpty()){
				break;
			}			
			String[] ab = line.split("->");
			
			Dependency dep = new Dependency();
			CloudApplication sourceApp = apps.get(Integer.parseInt(ab[0])-1);
			
			dep.setSource(sourceApp);
			dep.setTarget(apps.get(Integer.parseInt(ab[1])-1));			
			
			List<Dependency> list = deps.get(sourceApp.getName());
			if(list == null){
				list = new ArrayList<Dependency>();
				deps.put(sourceApp.getName(), list);
			}
			list.add(dep);
			System.out.println("Generate the depends-on relationship in application model according to the manually-defined dependency");
		}
		
		
		return deps;
	}
		
	/** generate service value - service name mapping, so that we can intelligently 
	 * infer service name from value
	 * 
	 * @param configs Map of appname - environment variables 
	 * @return
	 */
	private Map<String, String> genServiceValueKeyMap(Map<String, Map<String, String>> configs){
		Map<String, String> serviceVKMap = new HashMap<String, String>();
		// not needed for cloud foundry
//		for(Entry<String, Map<String, String>> entry : configs.entrySet()){
//			String appName = entry.getKey();
//			Map<String, String> config = entry.getValue();
//			//FIXME: to support more addons
//			if(config.containsKey("MONGOLAB_URI")){
//				serviceVKMap.put(config.get("MONGOLAB_URI"), appName+"Mongo");
//			}
//		}
		return serviceVKMap;
	}
	
	private Map<String, Map<String, String>> getAppsConfigs(CloudFoundryClient cfClient, List<CloudApplication> apps){
		Map<String, Map<String, String>> configs = new HashMap<String, Map<String, String>>();		
		for(CloudApplication app : apps){			
			configs.put(app.getName(), app.getEnvAsMap());
		}
		return configs;
	}
	
	/** build map of app web url to app mapping so that we can intelligently infer the application reference
	 * @param apps
	 * @return
	 */
	private Map<String, CloudApplication> genAppurlVKMap(List<CloudApplication> apps){
		Map<String, CloudApplication> map = new HashMap<String, CloudApplication>();
		for(CloudApplication app : apps){
			for(String url : app.getUris()){				
				if(url.endsWith("/")){
					url = url.substring(0, url.length()-1);
				}
				if(!url.startsWith("http")){
					url = "http://"+url;
				}
				map.put(url, app);	
			}
			
		}
		
		return map;
	}
	
	/** 
	 * check if the given v contains one app's web url, if so, return the app url.
	 * otherwise, reutrn null
	 * @param appurlMap
	 * @param v
	 * @return
	 */
	private String getRefURL(Map<String, CloudApplication> appurlMap, String v){
		for(Entry<String, CloudApplication> entry : appurlMap.entrySet()){
			String appurl = entry.getKey();
			if(appurl.endsWith(v)){
				return appurl;
			}
//			if(v.startsWith(appurl)){
//				return appurl;
//			}
		}
		return null;
	}
	
	
	/** 
	 * get apps env list, key is app name, value is env list. 
	 * handle different types of environment variable
	 * @param heroku
	 * @param apps
	 * @return map of app name, list<Env> for apps
	 */
	private Map<String, List<Env>> getAppsEnvs(CloudFoundryClient cfClient, List<CloudApplication> apps){
		Map<String, Map<String, String>> configs = getAppsConfigs(cfClient, apps);		
		Map<String, String> serviceVKMap =  genServiceValueKeyMap(configs);
		Map<String, CloudApplication> appurlVKMap = genAppurlVKMap(apps);
		
		Map<String, List<Env>> appEnvMap = new HashMap<String, List<Env>>();
			
		for(Entry<String, Map<String, String>> entry : configs.entrySet()){
			String appName = entry.getKey();
			Map<String, String> config = entry.getValue();
			List<Env> envList = new ArrayList<Env>();
			for(Entry<String, String> c : config.entrySet()){
				String envName = c.getKey();
				Env env = new Env();								
				env.setName(envName);					
				String envValue = c.getValue();
				String serviceName = serviceVKMap.get(envValue);
				env.setDefaultValue(envValue);
				if(serviceName!=null){
					// app - service dependency 
					env.setValue("${"+serviceName+"}");
					env.setType(EnvType.service_url);
				}else{
					String refURL = getRefURL(appurlVKMap, envValue);
					if(refURL!=null){
						env.setType(EnvType.webapp_url);
						String webUrl = refURL;
						String postfix = "";
						if(envValue.length()>webUrl.length()){
							postfix = envValue.substring(webUrl.length());
						}					
						CloudApplication refApp = appurlVKMap.get(refURL);
						env.setValue("${"+refApp.getName()+"}"+postfix);
					}else{
						// normal environment varialbes
						env.setType(EnvType.normal);
						env.setValue(envValue);	
					}					
				}					
				
				envList.add(env);
			}
			appEnvMap.put(appName, envList);
		}
		
		return appEnvMap;
	}
	
	/**
	 * parse environment variable value to extract referenced target.
	 * @param envValue
	 * @return
	 */
	private String parseEnvRefTarget(String envValue){
		if(!envValue.startsWith("${")){
			throw new RuntimeException("env value must start with ${");
		}
		return envValue.substring(2, envValue.indexOf("}", 2));
	}
	
	private void genRelationFromEnv(AppModel model, CloudApplication app, List<Env> envs){
		for(Env env : envs){
			if(env.getType().equals(EnvType.service_url)){
				Relationship relation = new Relationship();
				relation.setSourceNode(app.getName());
				relation.setTargetNode(parseEnvRefTarget(env.getValue()));
				relation.setType(RelationshipType.connectTo);
				model.addRelationship(relation);
			}else if(env.getType().equals(EnvType.webapp_url)){
				Relationship relation = new Relationship();
				relation.setSourceNode(app.getName());
				relation.setTargetNode(parseEnvRefTarget(env.getValue()));
				relation.setType(RelationshipType.dependsOn);
				model.addRelationship(relation);
			}
		}
	}
	
	private void genWebAppNode(AppModel model, CloudFoundryClient cfClient, List<CloudApplication> apps){
		Map<String, List<Env>> envsMap = getAppsEnvs(cfClient, apps);		
		int basePort = 6001;
		for(int i=0;i<apps.size();i++){
			CloudApplication app = apps.get(i);
			WebAppNode  webnode = new WebAppNode(model);
			webnode.setName(app.getName());
			webnode.setMemory(app.getMemory()+"");
			webnode.setInstances(app.getInstances()+"");
			webnode.setPort(basePort+i+"");			
			List<Env> envs = envsMap.get(app.getName());
			webnode.setEnvs(envs);			
			model.addNode(webnode);
			
			// generate implicit relationship from environment variables
			genRelationFromEnv(model, app, envs);
		}
	}
	
	private void genContainerAndRelation(AppModel model, CloudFoundryClient cfClient, List<CloudApplication> apps){
		for(CloudApplication app : apps){
			//Currently only when pushing an application with an assigned buildpack url, can get the runtime type app hosted on
			String buildpack = app.getStaging().getBuildpackUrl();
			
			//FIXME: to support more buildpacks
			if(!buildpack.toLowerCase().contains("node")){
				System.out.println("unsupported build pack " + buildpack);
				continue;
			}
			
			AppContainerNode containerNode = new AppContainerNode(model);
			containerNode.setName(BuildPack.NodeJS.toString());
			containerNode.seteBuildPack(BuildPack.NodeJS);
			model.addNode(containerNode);
			
			Relationship relation = new Relationship();
			relation.setSourceNode(app.getName());
			relation.setTargetNode(containerNode.getName());
			relation.setType(Relationship.RelationshipType.hostedOn);
			model.addRelationship(relation);
		}
	}
	
	private void genAppDepRelation(AppModel model, Map<String, List<Dependency>> deps){		
		for(List<Dependency> list : deps.values()){
			for(Dependency dep : list){
				Relationship r = new Relationship();
				r.setSourceNode(dep.getSource().getName());
				r.setTargetNode(dep.getTarget().getName());
				r.setType(RelationshipType.dependsOn);
				model.addRelationship(r);
			}
		}
	}
	
	private Map<String, CloudService> services = null;
	private CloudService getCloudService(CloudFoundryClient client,
			String serviceName){
		if(services==null){
			services = new HashMap<String, CloudService>();
			List<CloudService> list = client.getServices();
			for(CloudService cs : list){
				services.put(cs.getName(), cs);
			}
		}
		return services.get(serviceName);
	}
	
	private void genServiceNode(AppModel model, CloudFoundryClient cfClient, List<CloudApplication> apps){
		
		for(CloudApplication app : apps){
			List<String> services = app.getServices();
			for(String service : services){
				// return from cloud foundry client getService() does not include label info
				// workaround it with method getCloudService
				CloudService cloudService = getCloudService(cfClient, service);			
				String label = cloudService.getLabel();
				//FIXME: to support more services
				if(!label.contains("mongodb")){
					System.out.println("unsupported service " + label +" for app " + app.getName());
					continue;
				}				
				ServiceNode serviceNode = new ServiceNode(model);
				serviceNode.setName(cloudService.getName());
				serviceNode.setServiceType(ServiceType.mongo);
				serviceNode.setProperty("dbName", "mydb");
				model.addNode(serviceNode);
				
				Relationship relation = new Relationship();
				relation.setSourceNode(app.getName());
				relation.setTargetNode(cloudService.getName());
				relation.setType(RelationshipType.connectTo);
				model.addRelationship(relation);
			}
		}
	}
	
	private AppModel buildAppModel(CloudFoundryClient cfClient, List<CloudApplication> apps, 
			Map<String, List<Dependency>> deps){
		AppModel model = new AppModel();
		model.setSource(Source.cf);	
		genWebAppNode(model, cfClient, apps);		
		genContainerAndRelation(model, cfClient, apps);		
		genAppDepRelation(model, deps);		
		genServiceNode(model, cfClient, apps);
		
		return model;
	}
	
	private void downloadAppFiles(CloudFoundryClient cfClient, List<CloudApplication>  apps){
		//System.out.println("Retreving applicaiton files from CF(y/n)? >N");
		Scanner reader=new Scanner(System.in);
		String answer = "";
		try {
			answer = reader.nextLine().toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			reader.close();
		}
		if(answer.equals("yes")||answer.equals("y")){
			for(CloudApplication app : apps){
				//FIXME: download app.
				// looks like cound foundry has no way to know whether a path is directory or file.				
			}
		}
	}
	
	public void export(String targetUrl, String username, String password, String outfile) 
			throws FileNotFoundException, AppModelException, MalformedURLException{		
		System.out.println("log in Cloud Foundry ......"+"\n");
		CloudCredentials cred = new CloudCredentials(username, password);
		CloudFoundryClient cfClient = new CloudFoundryClient(cred, new URL(targetUrl));
		OAuth2AccessToken token = cfClient.login();
		
		System.out.println("listing applications ...");
		List<CloudApplication> apps = cfClient.getApplications();		
		List<CloudApplication> selApps = selectApps(apps);
		
		if(selApps.size()==0){
			System.out.println("nothing to export");
			return;
		}
		Map<String, List<Dependency>> deps = new HashMap<String, List<Dependency>>();
		
		if(selApps.size()>1) {
			System.out.println("Specify Dependency Using format A->B  (A depends on B)?(Y/N)");
			Scanner dependency_answer_reader=new Scanner(System.in);
			String dependency_answer = "";
			try {
				dependency_answer = dependency_answer_reader.nextLine();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dependency_answer_reader.close();
			}
			if(dependency_answer.equalsIgnoreCase("Y")){
				deps = specifyDeps(apps);
			} else{
				System.out.println("Generate the depends-on relationship in application model according to the environment variables set in CloudFoundry");
			}
		}
		
		System.out.println("starting to generate application model ...");
		AppModel appModel = buildAppModel(cfClient, selApps, deps);
		
		AppModelSerializer serializer = new AppModelSerializer();
		serializer.serialize(appModel, new FileOutputStream(outfile));
		System.out.println("application model is saved to " + outfile + "\n");
		
		//downloadAppFiles(cfClient, selApps);		
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try{			
			String defaultUrl = "http://api.ng.bluemix.net";
			System.out.println("**********Capture the application model from Cloud Foundry**********"+"\n");
			System.out.print("Enter Cloud Foundry Target URL (default: "+defaultUrl+")");
			Scanner reader=new Scanner(System.in);
			String targetUrl = reader.nextLine();
			if(targetUrl.isEmpty()){
				targetUrl = defaultUrl;
			}
			
			System.out.println("Email: ");			
			String username = reader.nextLine();
			
			System.out.println("Password: ");
			String password =reader.nextLine();	
//			System.out.println("Output File Name: ");
//			String outfile = reader.nextLine();

			String outfile = "application_model.xml";
			Exporter exporter = new Exporter();
			exporter.export(targetUrl, username, password, outfile);
			
			System.out.println("Would you like to take a look at the application model?(Y/N)");
			Scanner model_answer_reader=new Scanner(System.in);
			String model_answer = model_answer_reader.nextLine();
			if(model_answer.equalsIgnoreCase("Y")){
				System.out.println("The contents of captured application model:\n");
				//Display the content of the application model
				try{
					BufferedReader in = new BufferedReader(new FileReader(outfile));
					String app_model_str;
			        while ((app_model_str = in.readLine()) != null) 
			        {
			              System.out.println(app_model_str);
			        }
			        in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}catch(Exception e){
			e.printStackTrace();
		}		

		
	}
	

}
