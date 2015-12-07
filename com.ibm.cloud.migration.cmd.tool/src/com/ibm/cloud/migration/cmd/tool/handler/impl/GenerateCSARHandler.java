package com.ibm.cloud.migration.cmd.tool.handler.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Scanner;

import org.apache.commons.cli.CommandLine;

import com.ibm.cloud.migration.cmd.tool.handler.AbstractCustomHandler;
import com.ibm.cloud.migration.csar.composer.constants.AppModelConstants;
import com.ibm.cloud.migration.csar.composer.main.GenerateCSAR;

public class GenerateCSARHandler extends AbstractCustomHandler{
	
	public static final String COMMAND_HELP = new StringBuilder().append(" -am/--appmodel ").append(resource.getString("appmodel")).append(" -d/--dir ").append(resource.getString("dir")).toString();
    public static final String DESCRIPTION = "Generate TOSCA CSAR(Cloud Service archive) file from the captured application model";
	
	public String getHandlerName() {
		// TODO Auto-generated method stub
		return "generate tosca csar";
	}
	
	public String getDescription(){
		return DESCRIPTION;
	}

	
	public void handle(CommandLine cli) throws IOException,
			Exception {
		// TODO Auto-generated method stub
		if(!check(cli))
			return;
		
		System.out.println("----------Generate TOSCA CSAR(Cloud Service Archive) for the captured applicaton----------"+"\n");
		String app_model = cli.getOptionValue("am");
		if(app_model==null){
			System.out.println("Assign the path of the captured application model file(e.g. /opt/application_model_cf.xml): ");
			Scanner scanner_am = new Scanner(System.in);
			app_model = scanner_am.nextLine();
			//app_model="application_model_heroku.xml";
		}
		//System.out.println("The input application model is: "+ app_model);
		
		File app_model_file = new File(app_model);
		if(!app_model_file.exists()){
			throw new FileNotFoundException("The application model file cannot be found!");
		}
		new AppModelConstants().setAppModel(app_model);
		//System.out.println("After setting the app model name, the app model is: " + AppModelConstants.APP_MODEL_NAME);
		String csar_dir = cli.getOptionValue("d");
		if(csar_dir==null){
			System.out.println("Assign the path where the generated CSAR.zip file will flow to(e.g. /opt)<default:./output>: ");
			Scanner scanner_cd = new Scanner(System.in);
			csar_dir = scanner_cd.nextLine();
			if(csar_dir.isEmpty()){
				csar_dir = "output"+ File.separator + "CSAR.zip";
			}
		}
		String[] args = {csar_dir};
		GenerateCSAR.main(args);
		
	}

	
	public String getOptionHelp() {
		// TODO Auto-generated method stub
		return COMMAND_HELP;
	}
	
	 private boolean check(CommandLine cl) {
		 boolean checkFlag = true;
		 String errMsg = "";
		 
//		 if (!cl.hasOption("cd")) {
//		      checkFlag = false;
//		      System.out.println();
//		      System.out.println("Must enter '-cd/--csardir'.");
//		      System.out.println("  -cd, --csardir CSAR_DIR			Csar_dir path and name");	
//		      System.out.println();
//		      System.out.println("Other options can see 'mig help -cm tosca' for help.");
//		      System.out.println();
//		      return false;
//		      //errMsg = new StringBuilder().append(errMsg).append(resource.getString("target")).toString();
//		 }
		 
//		 if (!cl.hasOption("am")) {
//			 System.out.println();
//			 System.out.println("Must enter '-am/--appmodel'.");
//			 System.out.println("  -am, --appmodel APPMODEL_XML		Appmodel_XML path and name");
//			 System.out.println();
//		     System.out.println("Other options can see 'mig help -cm tosca' for help.");
//		     System.out.println();
//			 return false;
//		      //errMsg = new StringBuilder().append(errMsg).append(resource.getString("username")).toString();
//		 }
		 
		 if (!checkFlag) {
			 if (errMsg != "") {
			        errMsg = MessageFormat.format(resource.getString("missingOption"), new Object[] { errMsg });
			        return false;
			 }
		 }		 
		 return true;
	 }

	
}
