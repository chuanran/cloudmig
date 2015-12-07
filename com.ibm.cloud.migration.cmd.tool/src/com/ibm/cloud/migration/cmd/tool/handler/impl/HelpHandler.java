package com.ibm.cloud.migration.cmd.tool.handler.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.domain.CloudApplication;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import com.heroku.api.App;
import com.heroku.api.HerokuAPI;
import com.ibm.cloud.migration.cmd.tool.handler.AbstractCustomHandler;
import com.ibm.cloud.migration.cmd.tool.utility.CFExporter;
import com.ibm.cloud.migration.cmd.tool.utility.RetrieveCloudCred;
import com.ibm.cloud.migration.cmd.tool.utility.CFExporter.Dependency;
import com.ibm.cloud.migration.cmd.tool.utility.HerokuExporter;
import com.ibm.cloud.migration.cmd.tool.utility.HerokuExporter.HerokuDependency;
import com.ibm.cloud.migration.appmodel.AppModelSerializer;
import com.ibm.cloud.migration.appmodel.model.AppModel;

public class HelpHandler extends AbstractCustomHandler{

	public static final String COMMAND_HELP = new StringBuilder().append(" -cm/--command ").append(resource.getString("command")).toString();
	
	public HelpHandler(){		
	}
	
	public String getHandlerName()
	  {
	    return "help";
	  }
	
	public String getOptionHelp(){
		
	    return COMMAND_HELP;
	}
	public String getDescription(){
		return null;
	}
	
	public void handle(CommandLine cl) throws Exception{
		System.out.println();
		System.out.println("Application Migration Command Line Interface, version [1.0]");
		System.out.println("  Use 'mig help' for basic help, 'mig [command] --help' for the details of each command");
		System.out.println();
		System.out.println("Getting Started");
		System.out.println("  login				Authenticated with the target cloud platform");
		System.out.println("  logout			Log out from the target cloud");
		System.out.println();
		System.out.println("Application");
		System.out.println("  cf-apps			List applications running on the Cloud Foundry");
		System.out.println("  heroku-apps			List applications running on the heroku");			
		System.out.println();
//		System.out.println("Services");
//		System.out.println("  cf-service-offerings		List your CloudFoundry offer services");
//		System.out.println("  heroku-app-addons		List Heroku addons");
//		System.out.println();
		System.out.println("Capture");
		System.out.println("  capture			capture the application model of the application from target cloud");
		System.out.println();
		System.out.println("Deploy");
		System.out.println("  deploy			Deploy the captured application model to target cloud");
		System.out.println();
		System.out.println("Tosca CSAR generating");
		System.out.println("  tosca				Generate TOSCA CSAR(Cloud Service archive) according to the captured application model");
		System.out.println();
			
		
//		else if(help_command.equalsIgnoreCase("login")){
//			System.out.println();
//			System.out.println("Usage: login ");
//			System.out.println();
//			System.out.println("Options:");
//			System.out.println("  -t, --target PLATEFORM		Plateform type");
//			System.out.println("  -l, --url URL				Plateform target");
//			System.out.println("  -u, --username EMAIL			Account email");
//			System.out.println("  -p, --password PASSWORD		Account password");
//			System.out.println();
//		}else if(help_command.equalsIgnoreCase("capture")){
//			System.out.println();
//			System.out.println("Usage: capture ");
//			System.out.println();
//			System.out.println("Options:");
//			System.out.println("  -t, --target PLATEFORM		Plateform type");
//			System.out.println("  -o, --org ORGANIZATION		Organization");
//			System.out.println("  -s, --space SPACE			Space");
//			System.out.println("  -a, --app APPLICATION			Applicaion");
//			System.out.println("  -d, --dep DEPENDENCY			Dependency");
//			System.out.println("  -am, --appmodel APPMODEL		Appmodel path and name");
//			System.out.println();
//		}else if(help_command.equalsIgnoreCase("deploy")){
//			System.out.println();
//			System.out.println("Usage: deploy ");
//			System.out.println();
//			System.out.println("Options:");
//			System.out.println("  -t, --target PLATEFORM		Plateform type");
//			System.out.println("  -ad, --appdir APPLICATION_DIR		Applicaion_dir");
//			System.out.println("  -o, --org ORGANIZATION		Organization");
//			System.out.println("  -s, --space SPACE			Space");			
//			System.out.println();
//		}else if(help_command.equalsIgnoreCase("tosca")){
//			System.out.println();
//			System.out.println("Usage: tosca ");
//			System.out.println();
//			System.out.println("Options:");
//			System.out.println("  -am, --appmodel APPMODEL			Appmodel  path and name");
//			System.out.println("  -cd, --csardir CSAR_DIR			Csar_dir path and name");			
//			System.out.println();
//		}else if(help_command.equalsIgnoreCase("heroku-apps")){
//			System.out.println();
//			System.out.println("Usage: heroku-apps ");
//			System.out.println();
//			System.out.println("Options:");
//			System.out.println("  None");			
//			System.out.println();
//		}else if(help_command.equalsIgnoreCase("heroku-app-addons")){
//			System.out.println();
//			System.out.println("Usage: heroku-app-addons ");
//			System.out.println();
//			System.out.println("Options:");
//			System.out.println("  -a, --app APPLICATION		Applicaion");		
//			System.out.println();
//		}else if(help_command.equalsIgnoreCase("cf-service-offerings")){
//			System.out.println();
//			System.out.println("Usage: cf-service-offerings ");
//			System.out.println();
//			System.out.println("Options:");
//			System.out.println("  None");			
//			System.out.println();
//		}else if(help_command.equalsIgnoreCase("cf-apps")){
//			System.out.println();
//			System.out.println("Usage: cf-apps ");
//			System.out.println();
//			System.out.println("Options:");
//			System.out.println("  -o, --org ORGANIZATION		Organization");
//			System.out.println("  -s, --space SPACE			Space");	
//			System.out.println();
//		}else if(help_command.equalsIgnoreCase("logout")){
//			System.out.println();
//			System.out.println("Usage: logout ");
//			System.out.println();
//			System.out.println("Options:");
//			System.out.println("  None");			
//			System.out.println();
//		}else{
//			System.out.println();
//			System.out.println("Unknown command '"+help_command+"'. See 'help' for available command !");
//			System.out.println();
//		}
	}
	
	 private void check(CommandLine cl) {
		 	 
	 }
}

