package com.ibm.cloud.migration.cmd.tool.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.domain.ApplicationStats;
import org.cloudfoundry.client.lib.domain.InstanceStats;
import org.cloudfoundry.client.lib.domain.Staging;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ibm.cloud.migration.appmodel.AppModelException;
import com.ibm.cloud.migration.appmodel.model.AppModel;
import com.ibm.cloud.migration.appmodel.model.Env;
import com.ibm.cloud.migration.appmodel.model.ServiceNode;
import com.ibm.cloud.migration.appmodel.model.WebAppNode;

public class PushAppToCF {
	private static final int SLEEP_TIME_TO_FETCH_ENV = 100000;
	private static final int SLEEP_ADDITIONAL_TIME_TO_FETCH_ENV = 5000;
	private static final int SLEEP_TIME_AFTER_STOP_APP = 5000;
	private static Map<String, String> depended_url = new HashMap<String, String>();
	public static void pushAppToCF(boolean mig_service, AppModel model, CloudFoundryClient cfClient, String app_dir) throws IOException{
		
		//Should first start the applications with depended on capability
		List<String> depended_on_apps = model.getAppNamesWithDependedOnCapability();
		startDependedOnApplications(depended_on_apps, mig_service, model, cfClient, app_dir);
		
		//Then start the applications with depends on requirement
		List<String> depends_on_apps = model.getAppNamesWithDependsOnRequirement();
		startDependsOnApplications(depends_on_apps, mig_service, model, cfClient, app_dir);
		
		//Finally start the standalone applications
		//remove all the depended_on and depends_on applications from the original list
		List<WebAppNode> webapps = model.getWebApps();
		List<String> web_app_names = new ArrayList<String>();
		for(WebAppNode webapp : webapps){
			 web_app_names.add(webapp.getName());
		}
		
		if(web_app_names.containsAll(depended_on_apps)){
			web_app_names.removeAll(depended_on_apps);
		}
		if(web_app_names.containsAll(depends_on_apps)){
			web_app_names.removeAll(depends_on_apps);
		}
		//start the remained standalone applications
		if(web_app_names!=null && web_app_names.size()>0){
			startApplications(web_app_names, mig_service, model, cfClient, app_dir);
		}
		
	}
	
    public static void startDependedOnApplications(List<String> depended_on_apps, boolean mig_service, AppModel model, CloudFoundryClient cfClient, String app_dir) throws IOException{
		if(depended_on_apps!=null && depended_on_apps.size()>0){
			startApplications(depended_on_apps, mig_service, model, cfClient, app_dir);
		}
	
	}
    
    public static void startDependsOnApplications(List<String> depends_on_apps, boolean mig_service, AppModel model, CloudFoundryClient cfClient, String app_dir) throws IOException{
		if(depends_on_apps!=null && depends_on_apps.size()>0){
			startApplications(depends_on_apps, mig_service, model, cfClient, app_dir);
		}
	
    }
    
    public static void startApplications(List<String> app_names, boolean mig_service_answer, AppModel model, CloudFoundryClient cfClient, String app_dir) throws IOException{
    	
    	String app_container="";
		//String buildpack_url ="";
		String default_domain = "ng.bluemix.net";
		List<WebAppNode> webapps = model.getWebApps();
		if(webapps != null && webapps.size()>0){
			for(WebAppNode webapp : webapps){
				try {
					app_container = webapp.getHostedOnContainer().getBuildPack().toString();
					boolean bp_supported = false;
					for (String buildpack: AppModelConstants.RUNTIME_BUILDPACK) {
						if (buildpack.equalsIgnoreCase(app_container)) {
							bp_supported = true;
							break;
						}
					}
					
					if (!bp_supported) {
						throw new AppModelException("build pack " + app_container +" is not supported");
					}
					
				} catch (AppModelException e) {
					// TODO 
					e.printStackTrace();
				}
				break;
			}
		}
		
//		String cf_url = RetrieveCloudCred.getTargetCFURL();
//		String[] domain_strs = cf_url.split("\\.", 2);
//		app_domain_postfix = "."+domain_strs[1];
//		app_domain_postfix =  ".cloudoe.apps";
		
		
    	for(String app_name : app_names){
    		String default_subdomain = app_name;
    		WebAppNode webapp = (WebAppNode)model.getNodeByName(app_name);
    		int instance_num = Integer.parseInt(webapp.getInstances());
    		int memory = Integer.parseInt(webapp.getMemory());
    		//Here don't assign start command or buildpack url, will use default bp url
    		Staging staging = new Staging(null, null);
    		//Using the default buildpack url
    		//Staging staging = new Staging(null, null);
    		//Environment list
    	    List<String> env_list = new ArrayList<String>();
    	    
    		List<String> uris = new ArrayList<String>();
    		
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
    		
    		System.out.println("Subdomain> (default: "+app_name+")");
			Scanner reader_subdomain=new Scanner(System.in);
			String subdomain = "";
			try {
				subdomain = reader_subdomain.nextLine();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				reader_subdomain.close();
			}
			if(subdomain.isEmpty()){
				subdomain = default_subdomain;
			}	
			
			System.out.println("domain> (default: "+default_domain+")");
			Scanner reader_domain=new Scanner(System.in);
			String domain = "";
			try {
				domain = reader_domain.nextLine();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				reader_domain.close();
			}
			if(domain.isEmpty()){
				domain = default_domain;
			}
			String app_url = subdomain + "." + domain;
    		uris.add(app_url);
			
    		if(mig_service_answer == true){
    			System.out.println("Created CloudFoundry services will be bound to application " + app_name);
    			cfClient.createApplication(app_name, staging, memory, uris, serviceNames);
    			//auto-generate the autoconfig.js to re-compose the origin environment variable from the CloudFoundry environment variable
    			
    		} else{
    			cfClient.createApplication(app_name, staging, memory, uris, null);
    			System.out.println("Application will bind to the Heroku services");
    			List<Env> app_envs = webapp.getEnvs();
    			if(app_envs !=null && app_envs.size()>0){
    				
    				for(Env app_env : app_envs){
    					if(app_env.getType().toString().equals(AppModelConstants.APP_SERVICE_URL_ENV_TYPE)){
//    						Map<String, String> env_map = new HashMap<String, String>();
//    						env_map.put(app_env.getName(), app_env.getDefaultValue());
    						env_list.add(app_env.getName() + "="+app_env.getDefaultValue());
    					}
    				}
    			}
    		}
    		
    		//update the environment variable for the depended on application url
    		
    		if(model.hasDependedOnCapability(app_name)){
    			depended_url.put(app_name, app_url);
    		}
    		if(model.hasDependsOnRequirement(app_name)){
    			List<Env> app_envs = webapp.getEnvs();
    			if(app_envs !=null && app_envs.size()>0){
    				for(Env app_env : app_envs){
    					if(app_env.getType().toString().equals(AppModelConstants.APP_URL_ENV_TYPE)){
    						String app_bind_env_value = app_env.getValue().substring(2, app_env.getValue().length()-1);
    						env_list.add(app_env.getName() +"=" + depended_url.get(app_bind_env_value));
    					}
    				}
    			}
    		}
    		
    		//update the env list
    		if(env_list !=null && env_list.size()>0){
    			cfClient.updateApplicationEnv(app_name, env_list);
    		}
    		
    		cfClient.updateApplicationInstances(app_name, instance_num);
    		
    		//upload application
    		String archive = app_dir + File.separator + app_name +  ".zip";
    		System.out.println("Upload the package of application " + app_name);
    		try {
    			cfClient.uploadApplication(app_name, archive);
    			//cfClient.uploadApplication(app_name, file);
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		
    		//if migrate service, we will tip user whether do the environment variable mapping
    		if(mig_service_answer == true){
    			System.out.println("Would you like to do service environment variable mapping? (Y/N)");
        		Scanner reader=new Scanner(System.in);
        		String answer = "";
        		try {
        			answer = reader.nextLine();
        		} catch (Exception e) {
    				e.printStackTrace();
    			} finally {
    				reader.close();
    			}
    			
    			if(answer.equalsIgnoreCase("Y")){
    				System.out.println("automatically map the service environment variable"); 
    				cfClient.startApplication(app_name);
    				//Here need to sleep for almost 80 seconds to wait while the env.log is generated by binding
    				try {
    	    			Thread.sleep(SLEEP_TIME_TO_FETCH_ENV);
    	    		} catch (InterruptedException e1) {
    	    			// TODO Auto-generated catch block
    	    			e1.printStackTrace();
    	    		}
    				String env_str ="";
    				
    				try{
    					env_str = cfClient.getFile(app_name, 0, "logs/env.log");
    				} catch (Exception e){
    					e.printStackTrace();
    				} finally{
    					try {
							Thread.sleep(SLEEP_ADDITIONAL_TIME_TO_FETCH_ENV);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							//e.printStackTrace();
						}
    					env_str = cfClient.getFile(app_name, 0, "logs/env.log");
    				}	
    				
    	    		try {
    	    			WriteStringToFile.writeStrToFile(env_str, "vcap_services.properties");
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
    	    			Thread.sleep(SLEEP_TIME_AFTER_STOP_APP);
    	    		} catch (InterruptedException e1) {
    	    			// TODO Auto-generated catch block
    	    			e1.printStackTrace();
    	    		}
    	    		cfClient.updateApplicationEnv(app_name, env_list);
    			}
    		}
    		
			System.out.println("Start the application " + app_name);
    		cfClient.startApplication(app_name);
    		//Check the application status till it is "Running"
    		//stats including "STARTING, RUNNING, CRASHED, FLAPPING, UNKNOWN"
    		
    		int poll_interval = 5000;
    		int max_times_poll = 120;
    		int times_poll =1; 
    		boolean running_flag = false;
    		boolean failed_flag = false;
    		while(true){
    			ApplicationStats stat = cfClient.getApplicationStats(app_name);
    			List<InstanceStats> instance_stats = stat.getRecords();

    			for(InstanceStats stats : instance_stats){
    				if(stats.getState().toString().equalsIgnoreCase("RUNNING")){
    					running_flag = true;
    					break;
    				}
    				if(stats.getState().toString().equalsIgnoreCase("CRASHED")){
    					failed_flag = true;
    					break;
    				}
    			}
    			if(running_flag){
    				System.out.println("Application " + app_name + "(url:" + app_url +")" + " is running.");
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

}
