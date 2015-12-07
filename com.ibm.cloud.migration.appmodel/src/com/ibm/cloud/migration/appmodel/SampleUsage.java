package com.ibm.cloud.migration.appmodel;

import java.util.Collection;
import java.util.List;

import com.ibm.cloud.migration.appmodel.model.AppContainerNode;
import com.ibm.cloud.migration.appmodel.model.AppModel;
import com.ibm.cloud.migration.appmodel.model.Env;
import com.ibm.cloud.migration.appmodel.model.Property;
import com.ibm.cloud.migration.appmodel.model.ServiceNode;
import com.ibm.cloud.migration.appmodel.model.WebAppNode;

public class SampleUsage {

	static final String IDENT = "    ";
	public static void dumpWebAppInfo(WebAppNode webapp) throws AppModelException{
		System.out.println("--------------------------------------------");
		System.out.println("web app name: " +  webapp.getName());
		System.out.println("instance: " + webapp.getInstances());
		System.out.println("memory: " + webapp.getMemory());
		System.out.println("port: " + webapp.getPort());
		System.out.println("properties ");
		Collection<Property> props = webapp.getProperties();
		for(Property prop : props){
			System.out.println(IDENT+ prop.getName()+"="+prop.getValue());
		}
		
		System.out.println("envs:");
		List<Env> envs = webapp.getEnvs();
		for(Env env : envs){
			System.out.println(IDENT+env.getType()+","+env.getName()+"="+env.getValue());
		}
		AppContainerNode container = webapp.getHostedOnContainer();
		System.out.println("build pack " + container.getBuildPack());
		
		List<ServiceNode> services = webapp.getConnectsToServices();
		System.out.println("services:");
		for(ServiceNode service : services){
			System.out.println(IDENT+"name: " + service.getName());
			System.out.println(IDENT+"service type: " + service.getServiceType());
			System.out.println(IDENT+"properties: ");
			for(Property prop : service.getProperties()){
				System.out.println(IDENT+IDENT+prop.getName()+"="+prop.getValue());
			}
		}
		List<WebAppNode> deps = webapp.getDependsOnApps();
		System.out.println("dependsOn apps: " +deps.size());
		for(WebAppNode app : deps){
			System.out.println(IDENT+app.getName());
		}
		System.out.println("--------------------------------------------\n");
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AppModelParser parser = new AppModelParser();
		
		try {
			AppModel model = parser.parse("countly-heroku-sample.xml");
			System.out.println("app model source is " + model.getSource());
			List<WebAppNode> webapps = model.getWebApps();
			for(WebAppNode webapp : webapps){
				dumpWebAppInfo(webapp);
			}
			
		} catch (AppModelException e) {

			e.printStackTrace();
		}

	}

}
