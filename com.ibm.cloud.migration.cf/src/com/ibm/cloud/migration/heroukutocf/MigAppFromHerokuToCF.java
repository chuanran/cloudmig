package com.ibm.cloud.migration.heroukutocf;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.UUID;


import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.domain.ApplicationStats;
import org.cloudfoundry.client.lib.domain.CloudApplication;
import org.cloudfoundry.client.lib.domain.CloudEntity.Meta;
import org.cloudfoundry.client.lib.domain.CloudOrganization;
import org.cloudfoundry.client.lib.domain.CloudService;
import org.cloudfoundry.client.lib.domain.CloudServicePlan;
import org.cloudfoundry.client.lib.domain.CloudSpace;
import org.cloudfoundry.client.lib.domain.InstanceStats;
import org.cloudfoundry.client.lib.domain.Staging;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import com.ibm.cloud.migration.appmodel.AppModelException;
import com.ibm.cloud.migration.appmodel.AppModelParser;
import com.ibm.cloud.migration.appmodel.model.AppModel;
import com.ibm.cloud.migration.appmodel.model.Env;
import com.ibm.cloud.migration.appmodel.model.ServiceNode;
import com.ibm.cloud.migration.appmodel.model.WebAppNode;
//import com.ibm.cloud.migration.heroku.Exporter;
import com.ibm.cloud.migration.utility.FileMove;

public class MigAppFromHerokuToCF {
	
	private static CloudFoundryClient cfClient1;
	private static CloudFoundryClient cfClient;
	private static String buildpack_url = "";
	private static String app_container = "";
	private static List<WebAppNode> webapps;
	private static List<ServiceNode> services; 
	private static AppModel model;
	//private static String app_domain_postfix = ".cloudoe.apps";
	private static String app_domain_postfix = ".ng.bluemix.net";
	private static CloudSpace space;
	private static String mig_service_answer;
	static{
		//This code cannot work well. Workaround for manually move the application_model.xml to this project
		//Generate application_model.xml from heroku, which will be parsed for pushing application to CF
//		System.out.println("Enter heroku credentails: ");
//		System.out.println("Email: ");
//		Scanner reader=new Scanner(System.in);
//		String username = reader.nextLine();
//		System.out.println("Password: ");
//		String password =reader.nextLine();
//		Exporter exporter = new Exporter();
//		try {
//			exporter.export(username, password, AppModelConstants.APP_MODEL_NAME);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (AppModelException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		//Transfer the generated application_model.xml file under project "com.ibm.cloud.migration.heroku" to project "com.ibm.cloud.migration.cf"
		
		
		//Parse the application_model_heroku.xml
		AppModelParser parser = new AppModelParser();
		try {
			model = parser.parse(AppModelConstants.APP_MODEL_HEROKU_NAME);
		} catch (AppModelException e) {
					// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Get the services in the application model 
		services = model.getServices();
		
		//Get applications and the buildpack_url in the application model
		webapps = model.getWebApps();
		for(WebAppNode webapp : webapps){
			try {
				app_container = webapp.getHostedOnContainer().getBuildPack().toString();
				if(AppModelConstants.RUNTIME_BUILDPACK_URL.containsKey(app_container)){
					buildpack_url = AppModelConstants.RUNTIME_BUILDPACK_URL.get(app_container);
				} else{
					throw new AppModelException("build pack " + app_container +" is not supported");
				}
			} catch (AppModelException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
			break;
		}
	}
	
	private static void loginToCF() throws Exception{			
			String defaultUrl = "http://api.ng.bluemix.net";
			System.out.print("Enter Cloud Foundry Target URL (default: "+defaultUrl+")");
			Scanner reader=new Scanner(System.in);
			String targetUrl = reader.nextLine();
			if(targetUrl.isEmpty()){
				targetUrl = defaultUrl;
			}
			//domain_postfix = targetUrl.s
			
			System.out.println("Email: ");			
			String username = reader.nextLine();
			
			System.out.println("Password: ");
			String password = "";
			try {
				password =reader.nextLine();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				reader.close();
			}

			System.out.println("log in Cloud Foundry ......" + "\n");
			CloudCredentials cred = new CloudCredentials(username, password);
			//CloudSpace space = new CloudSpace(meta, "dev", null);
			cfClient1 = new CloudFoundryClient(cred, new URL(targetUrl));
			OAuth2AccessToken token1 = cfClient1.login();
			space = cfClient1.getSpaces().get(0);
			cfClient = new CloudFoundryClient(cred, new URL(targetUrl), space);
			OAuth2AccessToken token = cfClient.login();
			System.out.println("List applications: ");
			List<CloudApplication> apps = cfClient.getApplications();
//		    for(CloudApplication app : apps){
//		    	System.out.println("app name is : " + app.getName());
//		    }
			
			//test code for service plan
//			CloudService service = cfClient.getService("mymongo");
//			System.out.println("service plan is: " + service.getPlan());
			
			
	}
	
	private static void createServices(){
		if(services!=null && services.size()>0){
			for(ServiceNode service : services){
				String service_name = service.getName();
				String service_type = service.getServiceType().toString();
				CloudService cloudservice = new CloudService();
				
				//Set service name/provider/label
				cloudservice.setName(service_name);
				cloudservice.setLabel(service_type+"db");
				cloudservice.setMeta(space.getMeta());
				cloudservice.setPlan("free");
				//cloudservice.setPlan("100");
				cloudservice.setVersion("2.2");
				cfClient.createService(cloudservice);
				
			}
		}
			
	}
	
	private static void startDependedOnApplications(String mig_service_answer){
		
		List<String> app_names = model.getAppNamesWithDependedOnCapability();
		if(app_names!=null && app_names.size()>0){
			startApplications(app_names, mig_service_answer);
		}
	
	}
	
	
private static void startDependsOnApplications(String mig_service_answer){
		
		List<String> app_names = model.getAppNamesWithDependsOnRequirement();
		if(app_names!=null && app_names.size()>0){
			startApplications(app_names, mig_service_answer);
		}
	
}

private static void startApplications(List<String> app_names, String mig_service_answer){
	for(String app_name : app_names){
		WebAppNode webapp = (WebAppNode)model.getNodeByName(app_name);
		int instance_num = Integer.parseInt(webapp.getInstances());
		int memory = Integer.parseInt(webapp.getMemory());
		Staging staging = new Staging(null, buildpack_url);
		
		//Environment list
	    List<String> env_list = new ArrayList<String>();
	    
		List<String> uris = new ArrayList<String>();
		//The url for dev box
		//uris.add(app_name + ".cloudoe.apps");
		
		//url for ng bluemix
		uris.add(app_name + ".labs.w3.bluemix.net");
		//uris.add(app_name + ".ng.w3.bluemix.net");
		
		//Get the connect_to services
		List<String> serviceNames = new ArrayList<String>();
		
		List<ServiceNode> connected_services = webapp.getConnectsToServices();
		if(connected_services !=null && connected_services.size()>0){
			for(ServiceNode connected_service : connected_services){
				serviceNames.add(connected_service.getName());
			}
		}
		
		//Create the application
		System.out.println("Push application " + app_name +" to cloud Foundry......");
		if(mig_service_answer.equalsIgnoreCase("Y")){
			System.out.println("Created CloudFoundry services will be bound to application " + app_name);
			//cfClient.bindService(appName, serviceName);
			cfClient.createApplication(app_name, staging, memory, uris, serviceNames);
			
		} else{
			cfClient.createApplication(app_name, staging, memory, uris, null);
			System.out.println("Application will bind to the Heroku services");
			List<Env> app_envs = webapp.getEnvs();
			if(app_envs !=null && app_envs.size()>0){
				
				for(Env app_env : app_envs){
					if(app_env.getType().toString().equals(AppModelConstants.APP_SERVICE_URL_ENV_TYPE)){
						env_list.add(app_env.getName() + "="+app_env.getDefaultValue());
					}
				}
			}
		}
		//System.out.println("Bind application " + app_name +" to the created CloudFoundry service?(Y/N)");
		//Scanner mig_app_service_reader=new Scanner(System.in);
		//String mig_app_service_answer = mig_app_service_reader.nextLine();
		
		
		
//		if(mig_app_service_answer.equalsIgnoreCase("Y")){
//			System.out.println("Created CloudFoundry services will be bound to application " + app_name);
//			cfClient.createApplication(app_name, staging, memory, uris, serviceNames);
//		} 
//		//if don't migrate the service, need to set the service environment variable, and set services to null
//		else{
//			cfClient.createApplication(app_name, staging, memory, uris, null);
//			System.out.println("Application will bind to the Heroku services");
//			List<Env> app_envs = webapp.getEnvs();
//			if(app_envs !=null && app_envs.size()>0){
//				
//				for(Env app_env : app_envs){
//					if(app_env.getType().toString().equals(AppModelConstants.APP_SERVICE_URL_ENV_TYPE)){
//						Map<String, String> env_map = new HashMap<String, String>();
//						env_map.put(app_env.getName(), app_env.getDefaultValue());
//						System.out.println("app_env name is: " + app_env.getName() + "app_env default value is: " + app_env.getDefaultValue());
//						env_list.add(app_env.getName() + "="+app_env.getDefaultValue());
//					}
//				}
//			}
//		}
		
		//update the environment variable for the depended on application url
		if(model.hasDependsOnRequirement(app_name)){
			List<Env> app_envs = webapp.getEnvs();
			if(app_envs !=null && app_envs.size()>0){
				for(Env app_env : app_envs){
					if(app_env.getType().toString().equals(AppModelConstants.APP_URL_ENV_TYPE)){
						String app_bind_env_value = app_env.getValue().substring(2, app_env.getValue().length()-1);
						env_list.add(app_env.getName() +"="+app_bind_env_value+app_domain_postfix);
					}
				}
			}
		}
		
		//update the env list
		if(env_list !=null && env_list.size()>0){
			cfClient.updateApplicationEnv(app_name, env_list);
		}
		
		//Test code for app envs
//		CloudApplication cloudapp = cfClient.getApplication(app_name);
//		List<String> envs = cloudapp.getEnv();
//		for(String app_env : envs){
//			System.out.println("app_env is " + app_env );
//		}
		
		//Can update the instance number here??
		cfClient.updateApplicationInstances(app_name, instance_num);
		
		//upload application
		String archive = app_name + ".zip";
		System.out.println("Upload the package of application " + app_name);
		try {
			cfClient.uploadApplication(app_name, archive);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//start the application
		System.out.println("Start the application " + app_name);
//		cfClient.bindService(appName, serviceName)
		cfClient.startApplication(app_name);
		try {
			Thread.sleep(80000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("vcap service is: " +cfClient.getFile(app_name, 0, "logs/env.log"));
		String str = cfClient.getFile(app_name, 0, "logs/env.log");
		try {
			WriteStringToFile.writeStrToFile(str, "vcap_services.properties");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Properties prop = new Properties();
		InputStream fis;
		try {
			fis = new FileInputStream("vcap_services.properties");
			prop.load(fis);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String vcap_service_env = prop.getProperty("VCAP_SERVICES");
		System.out.println("vcap_service_env is: " + vcap_service_env);
		try {
			JSONObject jo =new JSONObject(vcap_service_env);
			Iterator it = jo.keys();
			while(it.hasNext()){
				String key = (String)it.next();
				JSONArray arr = jo.getJSONArray(key);
				List<Env> app_envs = webapp.getEnvs();
				if(app_envs !=null && app_envs.size()>0){
					for(Env app_env : app_envs){
						if(app_env.getType().toString().equals(AppModelConstants.APP_SERVICE_URL_ENV_TYPE)){
							for (int i = 0; i < arr.length(); i++){
								if(arr.get(i) instanceof JSONObject){
									JSONObject jos = (JSONObject)arr.get(i);
									JSONObject jo_cred = (JSONObject)(jos.get("credentials"));
									String hostname = jo_cred.getString("hostname");
									String port = jo_cred.getString("port");
									String username = jo_cred.getString("username");
									String password = jo_cred.getString("password");
									String db = jo_cred.getString("db");
									String url = jo_cred.getString("url");
									if(url!=null &&url.trim().length()>0){
										env_list.add(app_env.getName() + "="+url);	
									} else{
										if(username != null &&username.trim().length()>0 &&  password != null &&password.trim().length()>0){
											String url_env = "mongodb://"+username+":"+password+"@"+hostname+ ":" + port + "/" + db;
											env_list.add(app_env.getName() + "="+url_env);
										} else{
											String url_env = "mongodb://" + hostname+ ":" + port + "/" + db;
											env_list.add(app_env.getName() + "="+url_env);
										}
									}
								}
							}
						}
					}
				}
			}
			
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		cfClient.stopApplication(app_name);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		cfClient.updateApplicationEnv(app_name, env_list);
		cfClient.startApplication(app_name);
		//
//		List<Env> app_envs = webapp.getEnvs();
//		if(app_envs !=null && app_envs.size()>0){
//			
//			for(Env app_env : app_envs){
//				if(app_env.getType().toString().equals(AppModelConstants.APP_SERVICE_URL_ENV_TYPE)){
//					env_list.add(app_env.getName() + "="+app_env.getDefaultValue());
//				}
//			}
//		}
		
//		CloudApplication app = cfClient.getApplication(app_name);
//		Map<String, String> envmap = app.getEnvAsMap();
//		for(String i : envmap.keySet()){
//			System.out.println("the value for the specific key: " + i);
//			System.out.println(envmap.get(i));
//		}
//		if(envmap.containsKey("VCAP_SERVICES")){
//			System.out.println("VCAP SERVICES ARE: " + envmap.get("VCAP_SERVICES"));
//		}
		
		//Check the application status till it is "Running"
		//stats including "STARTING, RUNNING, CRASHED, FLAPPING, UNKNOWN"
		
		int poll_interval = 5000;
		int max_times_poll = 30;
		int times_poll =1; 
		boolean running_flag = false;
		boolean failed_flag = false;
		while(true){
			ApplicationStats stat = cfClient.getApplicationStats(app_name);
			List<InstanceStats> instance_stats = stat.getRecords();

			for(InstanceStats stats : instance_stats){
				if(stats.getState().toString().equalsIgnoreCase("RUNNING")){
					System.out.println("the status is running!");
					running_flag = true;
					break;
				}
				
				if(stats.getState().toString().equalsIgnoreCase("CRASHED")){
					System.out.println("the status is crashed!");
					failed_flag = true;
					break;
				}
			}
			if(running_flag){
				System.out.println("Application " + app_name + "(url:" + app_name + ".ng.bluemix.net)" + " is running.");
				System.out.println("\n");
				break;
			} else if(failed_flag){
				System.out.println("Application " + app_name + " is crashed.");
				System.out.println("\n");
				break;
			} else if(times_poll > max_times_poll){
				System.out.println("Application " + app_name + " failed to be started in " + max_times_poll*poll_interval + " seconds");
				System.out.println("\n");
				break;
			}
			try {
				Thread.sleep(poll_interval);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			times_poll = times_poll+1;
		}
	}
}
	
	public static void main(String[] args){
		
		System.out.println("----------Deploy the application model captured from Heroku to Cloud Foundry----------"+"\n");
		
		System.out.println("Deploy the captured application model to Cloud Foundry? (Y/N)");
		Scanner reader=new Scanner(System.in);
		String mig_answer = "";
		try {
			mig_answer = reader.nextLine();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			reader.close();
		}
		if(mig_answer.equalsIgnoreCase("Y")){
			// Login to CloudFoundry
			try {
				loginToCF();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			CloudApplication app_test = cfClient.getApplication("countlyapi");
			System.out.println("vcap service is:P " +cfClient.getFile("countlyapi", 0, "logs/env.log"));
			
			
			//create the services in application model from CF JAVA API
			System.out.println("Replace heroku services with  CloudFoundry services(Y/N)");
			Scanner mig_service_reader=new Scanner(System.in);
			try {
				mig_service_answer = mig_service_reader.nextLine();
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				mig_service_reader.close();
			}
			String mig_service_answer = mig_service_reader.nextLine();
			
			if(mig_service_answer.equalsIgnoreCase("Y")){
				System.out.println("Creating the CloudFoundry services"+"\n");
				if(services != null && services.size() >0){
					createServices();
				}
			} else{
				System.out.println("Reusing the heroku services"+"\n");
			}
			
			//push the application to the CF, currently push the application locally
			
			System.out.println("Retrive the application package from Heroku?(Y/N)");
			Scanner app_retrieve_reader =new Scanner(System.in);
			String app_retrieve_answer = app_retrieve_reader.nextLine();
			if(app_retrieve_answer.equalsIgnoreCase("Y")){
				//retrieve the application package from heroku
				
				
				
			} else{
				System.out.println("Specify the location of the application package: ");
				Scanner app_location_reader =new Scanner(System.in);
				String app_location = app_location_reader.nextLine();
				
				//move files
				FileMove fm=new FileMove();
				fm.readFile(app_location);
			}
			
			
			System.out.println("\n");
			if(webapps != null && webapps.size()>0){
				//Should first start the applications with depended on capability
				startDependedOnApplications(mig_service_answer);
				//Then start the applications with depends on requirement
				startDependsOnApplications(mig_service_answer);
			}
		} else{
			System.out.println("You will miss the CloudFoundry soup....");
		}
		
	}

}
