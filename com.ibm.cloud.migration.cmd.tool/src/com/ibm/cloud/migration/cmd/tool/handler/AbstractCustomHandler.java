package com.ibm.cloud.migration.cmd.tool.handler;

import java.io.IOException;
import java.util.ResourceBundle;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

public abstract class AbstractCustomHandler implements CustomHandlerCommand{
	protected Options options = new Options();
	public static final ResourceBundle resource = ResourceBundle.getBundle("resource");
	public AbstractCustomHandler(){
		this.options.addOption("h", "help", false, resource.getString("forHelp"));
	    this.options.addOption("?", false, resource.getString("forHelp"));
	}
	
	public Options getOptions(){
	    return this.options;
	}
	
	protected String printToString(Object o){
	    if (o != null) {
	      return o.toString();
	    }
	    return "";
	}

}
