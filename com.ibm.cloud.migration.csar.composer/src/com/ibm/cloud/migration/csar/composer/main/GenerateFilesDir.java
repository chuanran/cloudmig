package com.ibm.cloud.migration.csar.composer.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import com.ibm.cloud.migration.appmodel.AppModelException;
import com.ibm.cloud.migration.appmodel.AppModelParser;
import com.ibm.cloud.migration.appmodel.model.AppModel;
import com.ibm.cloud.migration.appmodel.model.WebAppNode;
import com.ibm.cloud.migration.csar.composer.constants.AppModelConstants;
import com.ibm.cloud.migration.csar.composer.utility.CopyDirectoryContents;

public class GenerateFilesDir {
	private static AppModel model;
	//private static String class_path = GenerateScriptDir.class.getResource("").getPath();
	private static String class_path = "./tosca_templates/";
	private static String files_home = "CSAR" + "/" + "files";
	
	static{
		AppModelParser parser = new AppModelParser();
		try {
			model = parser.parse(AppModelConstants.APP_MODEL_NAME);
		} catch (AppModelException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
	}
	
	public static void generateFilesDir(){
		//create directory for storing "files"
		File f = new File(files_home);
		if(! f.exists()){
			f.mkdir();
		}
		
		List<WebAppNode> web_apps = model.getWebApps();
		String app_container = "";
		for(WebAppNode webapp : web_apps){
			try {
				app_container = webapp.getHostedOnContainer().getBuildPack().toString();
			} catch (AppModelException e) {
				// TODO 
				e.printStackTrace();
			}
			break;
		}
		//Copy the runtime package to the "files" directory
		String runtime_installation_package_path = class_path +"files_template" + "/" + "runtime" + "/" + app_container + "/" + "installation_package";
		CopyDirectoryContents.copyDirectory(runtime_installation_package_path, files_home, true);
		String runtime_config_package_path = class_path +"files_template" + "/" + "runtime" + "/" + app_container + "/" + "configuration";
		CopyDirectoryContents.copyDirectory(runtime_config_package_path, files_home, true);
		
		//Copy the service package to the files directory
		List<String> service_types = model.getServiceTypes();
		if(service_types !=null && service_types.size() >0){
			for(String service_type : service_types){
				String service_installation_package_path = class_path + "files_template" + "/" + "service" + "/" + service_type + "/" + "installation_package";
				CopyDirectoryContents.copyDirectory(service_installation_package_path, files_home, true);
				String service_config_package_path = class_path + "files_template" + "/" + "service" + "/" + service_type + "/" + "configuration";
				CopyDirectoryContents.copyDirectory(service_config_package_path, files_home, true);
			}
		}
		
		//Copy the application packages to the "files" directory
		System.out.println("Specify the application file path(e.g. /opt/application_package)");
		Scanner reader_app_package_path=new Scanner(System.in);
		String app_package_path = reader_app_package_path.nextLine();
		if(app_package_path.trim().isEmpty()){
			app_package_path = class_path + "files_template" + "/" + "application";
		}
		//System.out.println("app package path is: " + app_package_path);
		File app_package = new File(app_package_path);
		if(!app_package.exists()){
			try {
				throw new FileNotFoundException("The directory for application package you designed doesn't exist!");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		CopyDirectoryContents.copyDirectory(app_package_path, files_home, true);
	}
	
//	public static void main(String[] agrs){
//		generateFilesDir();
//	}

}
