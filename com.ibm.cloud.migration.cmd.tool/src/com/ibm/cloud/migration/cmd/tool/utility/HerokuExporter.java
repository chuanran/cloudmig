package com.ibm.cloud.migration.cmd.tool.utility;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import com.heroku.api.Addon;
import com.heroku.api.App;
import com.heroku.api.HerokuAPI;
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

/**
 * Issues: 
 * 1) how to model Heroku worker. 
 *    *not supported yet.
 * 2) how to model ad-hoc environment variables.
 *    * intelligently infer environment variable type : service_url, webapp_url, normal *              
 * 3) how to model implicit app-service dependency
 *    * infer from environment variable type: service_url   
 * 4) how to model application code for different platforms     
 *    * do build back work during application install 
 * 5) how to model application entry point
 *    * heroku use Profile, use start script in package.json to replace it
 */
public class HerokuExporter {

	public static class HerokuDependency {
		App source;
		App target;		

		public HerokuDependency(){
			
		}
		public HerokuDependency(App src, App target){
			this.source = src;
			this.target = target;
		}
		
		
		/**
		 * @return the source
		 */
		public App getSource() {
			return source;
		}
		/**
		 * @return the target
		 */
		public App getTarget() {
			return target;
		}
		/**
		 * @param source the source to set
		 */
		public void setSource(App source) {
			this.source = source;
		}
		/**
		 * @param target the target to set
		 */
		public void setTarget(App target) {
			this.target = target;
		}
		
		
		
	}
	
	private List<App> selectApps(List<App> apps){
		List<App> selectedApps = new ArrayList<App>(apps.size());
		System.out.println("Selecting applications to export (use application number,separated by comma):");
		for(int i=1;i<=apps.size();i++){
			System.out.println(i+"."+apps.get(i-1).getName());
		}
		Scanner reader=new Scanner(System.in);
		String line = reader.nextLine();
		//FIXME: more robust input handling
		String[] sels = line.split(",");		
		for(int i=0;i<sels.length;i++){
			selectedApps.add(apps.get(Integer.parseInt(sels[i])-1));
		}
		
		return selectedApps;
	}
	
	private Map<String, List<HerokuDependency>> specifyDeps(List<App> apps){
		Map<String, List<HerokuDependency>> deps = new HashMap<String, List<HerokuDependency>>();
		
		Scanner reader=new Scanner(System.in);
		
		while(true){		
			System.out.println("Specify Dependency Using format A->B  (A depends on B), end with blank line");
			//FIXME: more robust input handling
			String line = reader.nextLine();
			if(line.isEmpty()){
				break;
			}			
			String[] ab = line.split("->");
			
			HerokuDependency dep = new HerokuDependency();
			App sourceApp = apps.get(Integer.parseInt(ab[0])-1);
			
			dep.setSource(sourceApp);
			dep.setTarget(apps.get(Integer.parseInt(ab[1])-1));			
			
			List<HerokuDependency> list = deps.get(sourceApp.getName());
			if(list == null){
				list = new ArrayList<HerokuDependency>();
				deps.put(sourceApp.getName(), list);
			}
			list.add(dep);				
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
		for(Entry<String, Map<String, String>> entry : configs.entrySet()){
			String appName = entry.getKey();
			Map<String, String> config = entry.getValue();
			//FIXME: to support more addons
			if(config.containsKey("MONGOLAB_URI")){
				serviceVKMap.put(config.get("MONGOLAB_URI"), appName+"Mongo");
			}
		}
		return serviceVKMap;
	}
	
	private Map<String, Map<String, String>> getAppsConfigs(HerokuAPI heroku, List<App> apps){
		Map<String, Map<String, String>> configs = new HashMap<String, Map<String, String>>();		
		for(App app : apps){
			configs.put(app.getName(), heroku.listConfig(app.getName()));
		}
		return configs;
	}
	
	/** build map of app web url to app mapping so that we can intelligently infer the application reference
	 * @param apps
	 * @return
	 */
	private Map<String, App> genAppurlVKMap(List<App> apps){
		Map<String, App> map = new HashMap<String, App>();
		for(App app : apps){
			String url = app.getWebUrl();
			if(url.endsWith("/")){
				url = url.substring(0, url.length()-1);
			}
			map.put(url, app);
		}
		
		return map;
	}
	
	/** 
	 * check if the given v contains one app's web url, if so, return the app.
	 * otherwise, reutrn null
	 * @param appurlMap
	 * @param v
	 * @return
	 */
	private App getRefApp(Map<String, App> appurlMap, String v){
		for(Entry<String, App> entry : appurlMap.entrySet()){
			String appurl = entry.getKey();
			if(v.startsWith(appurl)){
				return entry.getValue();
			}
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
	private Map<String, List<Env>> getAppsEnvs(HerokuAPI heroku, List<App> apps){
		Map<String, Map<String, String>> configs = getAppsConfigs(heroku, apps);		
		Map<String, String> serviceVKMap =  genServiceValueKeyMap(configs);
		Map<String, App> appurlVKMap = genAppurlVKMap(apps);
		
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
				env.setDefaultValue(envValue);
				String serviceName = serviceVKMap.get(envValue);						
				if(serviceName!=null){
					// app - service dependency 
					env.setValue("${"+serviceName+"}");
					env.setType(EnvType.service_url);
				}else{
					App refApp = getRefApp(appurlVKMap, envValue);
					if(refApp!=null){
						env.setType(EnvType.webapp_url);
						String webUrl = refApp.getWebUrl();
						String postfix = "";
						if(envValue.length()>webUrl.length()){
							postfix = envValue.substring(webUrl.length());
						}						
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
	
	private void genRelationFromEnv(AppModel model, App app, List<Env> envs){
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
	
	private void genWebAppNode(AppModel model, HerokuAPI heroku, List<App> apps){
		Map<String, List<Env>> envsMap = getAppsEnvs(heroku, apps);		
		int basePort = 6001;
		for(int i=0;i<apps.size();i++){
			App app = apps.get(i);
			WebAppNode  webnode = new WebAppNode(model);
			webnode.setName(app.getName());
			webnode.setMemory("512");
			webnode.setInstances(app.getDynos()+"");
			webnode.setPort(basePort+i+"");			
			List<Env> envs = envsMap.get(app.getName());			
			webnode.setEnvs(envs);			
			model.addNode(webnode);
			
			// generate implicit relationship from environment variables
			genRelationFromEnv(model, app, envs);
		}
	}
	
	private void genContainerAndRelation(AppModel model, HerokuAPI heroku, List<App> apps){
		for(App app : apps){
			String buildpack = app.getBuildpackProvidedDescription();
			//FIXME: to support more buildpacks
			if(!buildpack.equals("Node.js")){
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
	
	private void genAppDepRelation(AppModel model, Map<String, List<HerokuDependency>> deps){		
		for(List<HerokuDependency> list : deps.values()){
			for(HerokuDependency dep : list){
				Relationship r = new Relationship();
				r.setSourceNode(dep.getSource().getName());
				r.setTargetNode(dep.getTarget().getName());
				r.setType(RelationshipType.dependsOn);
				model.addRelationship(r);
			}
		}
	}
	
	private void genServiceNode(AppModel model, HerokuAPI heroku, List<App> apps){
		for(App app : apps){
			List<Addon> addons = heroku.listAppAddons(app.getName());
			for(Addon addon : addons){
				String name = addon.getName();
				//FIXME: to support more addons
				if(!name.contains("mongo")){
					//System.out.println("unsupported addon " + name +" for app " + app.getName());
					continue;
				}
				String serviceName = app.getName()+"Mongo";
				ServiceNode serviceNode = new ServiceNode(model);
				serviceNode.setName(app.getName()+"Mongo");
				serviceNode.setServiceType(ServiceType.mongo);
				serviceNode.setProperty("dbName", app.getName()+"-db");
				model.addNode(serviceNode);
				
				Relationship relation = new Relationship();
				relation.setSourceNode(app.getName());
				relation.setTargetNode(serviceName);
				relation.setType(RelationshipType.connectTo);
				model.addRelationship(relation);
			}
		}
	}
	
	public AppModel buildAppModel(HerokuAPI heroku, List<App> apps, 
			Map<String, List<HerokuDependency>> deps){
		AppModel model = new AppModel();
		model.setSource(Source.heroku);	
		
		genWebAppNode(model, heroku, apps);		
		genContainerAndRelation(model, heroku, apps);
		if(deps!=null && deps.size() > 0) {
			genAppDepRelation(model, deps);	
		}
		genServiceNode(model, heroku, apps);
		
		return model;
	}
	
	public void export(String username, String password, String outfile) 
			throws FileNotFoundException, AppModelException{		
		System.out.println("log in Heroku ......"+"\n");
		String apiKey = HerokuAPI.obtainApiKey(username, password);
		System.out.println("listing applications ...");
		HerokuAPI heroku = new HerokuAPI(apiKey);
		List<App> apps = heroku.listApps();		
		List<App> selApps = selectApps(apps);
		if(selApps.size()==0){
			System.out.println("nothing to export");
			return;
		}
		Map<String, List<HerokuDependency>> deps = null;
		if(selApps.size()>1){
			deps = specifyDeps(apps);
		}
		System.out.println("\n");
		System.out.println("starting to generate application model ...");
		AppModel appModel = buildAppModel(heroku, selApps, deps);
		
		AppModelSerializer serializer = new AppModelSerializer();
		serializer.serialize(appModel, new FileOutputStream(outfile));
		System.out.println("application model is saved to " + outfile);
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			System.out.println("**********Capture the application model from Heroku**********"+"\n");
			System.out.println("Enter heroku credentails: ");
			System.out.println("Email: ");
			Scanner reader=new Scanner(System.in);
			String username = reader.nextLine();
			
			System.out.println("Password: ");
			String password =reader.nextLine();
			
//			System.out.println("Output File Name: ");
//			String outfile = reader.nextLine();
			
			String outfile = "application_model.xml";
			HerokuExporter exporter = new HerokuExporter();
			exporter.export(username, password, outfile);
			System.out.println("\n");
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
