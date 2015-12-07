package com.ibm.cloud.migration.cmd.tool;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibm.cloud.migration.cmd.tool.handler.CustomHandlerCommand;
import com.ibm.cloud.migration.cmd.tool.handler.impl.CaptureHandler;
import com.ibm.cloud.migration.cmd.tool.handler.impl.DeployHandler;
import com.ibm.cloud.migration.cmd.tool.handler.impl.GenerateCSARHandler;
import com.ibm.cloud.migration.cmd.tool.handler.impl.HelpHandler;
import com.ibm.cloud.migration.cmd.tool.handler.impl.ListCFAppsHandler;
import com.ibm.cloud.migration.cmd.tool.handler.impl.ListHerokuAppsHandler;
import com.ibm.cloud.migration.cmd.tool.handler.impl.ListServiceOfferingsHandler;
import com.ibm.cloud.migration.cmd.tool.handler.impl.LoginCloudHandler;
import com.ibm.cloud.migration.cmd.tool.handler.impl.LogoutHandler;

public class CommandLineHandler {
	
	private String action = "";
	
	private static Log log = LogFactory.getLog(CommandLineHandler.class);
	private CommandLine commandLine;
	private CommandLineParser parser = new BasicParser();
	private Options opt = new Options();
	
	private HelpFormatter formatter = new HelpFormatter();
    private Map<String, CustomHandlerCommand> customHandlersMap = new HashMap();
	ResourceBundle resource = ResourceBundle.getBundle("resource");
	
	public CommandLineHandler(){
		this.opt.addOption("h", "help", false, this.resource.getString("forHelp"));
	    this.opt.addOption("?", false, this.resource.getString("forHelp"));
	    
		this.opt.addOption("action", "action", true, resource.getString("action"));
		this.opt.addOption("login", "login", true, resource.getString("login"));
		this.opt.addOption("t", "target", true, resource.getString("target"));
		this.opt.addOption("l", "url", true, resource.getString("url"));
		this.opt.addOption("u", "username", true, resource.getString("username"));
		this.opt.addOption("p", "password", true, resource.getString("password"));
		this.opt.addOption("o", "org", true, resource.getString("org"));
		this.opt.addOption("s", "space", true, resource.getString("space"));

		this.opt.addOption("capture", "capture", true, resource.getString("capture"));
		this.opt.addOption("a", "app", true, resource.getString("app"));
		this.opt.addOption("dep", "dependency", true, resource.getString("dependency"));
		this.opt.addOption("am", "appmodel", true, resource.getString("appmodel"));	
		this.opt.addOption("ad", "appdir", true, resource.getString("appdir"));
		this.opt.addOption("rs", "reuse-service", true, resource.getString("reuse-service"));
		this.opt.addOption("d", "dir", true, resource.getString("dir"));
		
		//this.opt.addOption("", "all", true, resource.getString("all"));
		this.opt.addOption("cm", "command", true, resource.getString("command"));
		
		customHandlersMap.put("login", new LoginCloudHandler());
		customHandlersMap.put("cf-apps", new ListCFAppsHandler());
		customHandlersMap.put("cf-service-offerings", new ListServiceOfferingsHandler());
		
		customHandlersMap.put("heroku-apps", new ListHerokuAppsHandler());
		customHandlersMap.put("heroku-app-addons", new ListServiceOfferingsHandler());
		
		customHandlersMap.put("tosca", new GenerateCSARHandler());
		
		customHandlersMap.put("capture", new CaptureHandler());
		customHandlersMap.put("deploy", new DeployHandler());
		
		customHandlersMap.put("logout", new LogoutHandler());		
		
		customHandlersMap.put("help", new HelpHandler());
	}
	public CommandLine checkArgs(String[] args){
		
		try
	    {
	      this.commandLine = this.parser.parse(this.opt, args);
	      action = this.commandLine.getOptionValue("action");
	      //System.out.println("action: " + action);
	    } catch (ParseException e) {
	      log.error("Parse command line error!", e);

	      System.out.println("Error while executing command.\n" + e.getMessage());

	      usage(action, 1);
	    }
		
		if ((this.commandLine.hasOption("?")) || (this.commandLine.hasOption("h"))) {
	        usage(action, 0);
	    }
		
		
		return this.commandLine;
	}
	
	public void execute(){
		
		String action = this.commandLine.getOptionValue("action");

	    boolean result = false;
	    if (this.commandLine.hasOption("action")) {
	      //log.info("Input action : " + action);
	      //System.out.println(MessageFormat.format(this.resource.getString("executingAction"), new Object[] { action }));

	      if (this.customHandlersMap.containsKey(action))
	      {
	        CustomHandlerCommand handler = (CustomHandlerCommand)this.customHandlersMap.get(action);	        
	        try {
	        	//log.info("Start to executing " + handler.getHandlerName());	          
				handler.handle(this.commandLine);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	          //log.info("Finish executing " + handler.getHandlerName());
	          //System.out.println(MessageFormat.format(this.resource.getString("finished"), new Object[] { handler.getHandlerName() }));

	          result = true;
	        
	      }else {
	    	  try {
				new HelpHandler().handle(this.commandLine);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	          //System.out.println(this.resource.getString("unknownCommand"));
	      }
	    }else {
	        log.error("Command line input without action!");
	        System.out.println(this.resource.getString("unknownAction"));
	      }

	      if (result)
	        System.exit(0);
	      else
	        System.exit(1);

	        
//		if(action.equalsIgnoreCase("-h")){			
//			System.out.println("help command");
//		}
		
	}
	
	public void usage(String commandname, int result){
	    CustomHandlerCommand command = (CustomHandlerCommand)this.customHandlersMap.get(commandname);
	    if (command != null) {
	      this.formatter.setSyntaxPrefix(this.resource.getString("SyntaxPrefix"));
	      System.out.println(command.getDescription());
	      this.formatter.printHelp(command.getOptionHelp(), command.getOptions());
	    } else {
	      log.error("action type = " + commandname + ". Can't find the handler! ");
	    }

	    System.exit(result);
	}

}
