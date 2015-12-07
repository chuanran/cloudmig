package com.ibm.cloud.migration.csar.composer.main;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import com.ibm.cloud.migration.csar.composer.utility.CompressUtil;


public class GenerateCSAR {
	
	public static String nextLine(Scanner in, boolean allowEmpty){
		String line = in.nextLine();
		if(!allowEmpty){
			while(line.isEmpty()){
				line = in.nextLine();
			}	
		}
		
		return line;
	}
	public static String nextLine(Scanner in){
		return nextLine(in, false);
	}
	public static void main(String[] args) throws Exception{
		//Generate "scripts" directory for CSAR
		System.out.println("Generate \"scripts\"(including \"Node\" life-cycle scripts and \"Relationship\" scripts) directory for the application......");
		try {
			GenerateScriptDir.generateScriptDirectory();
		} catch (ResourceNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ParseErrorException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Done generating \"scripts\" directory for the application"+"\n");
		
		//Generate "types" directory for CSAR
		System.out.println("Generate \"types\"(.xsd properties definition file) directory for the application......");
		try {
			GeneratePropertiesDir.generatePropertiesDir();
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} catch (ResourceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MethodInvocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Done Generating \"types\" directory for the application" + "\n");
		
		System.out.println("Generate General-Definitions.xml(describe the application topology(including \"Node\" and \"Relationship\") description) in \"Definitions\" directory......");
		//Generate "Definitions/General-Definitions.xml"
		GenerateGeneralDefinitionFile.generateDefinitionFile();
		System.out.println("Done generating \"General-Definitions.xml\""+ "\n");
		
		//Generate "Definitions/GeneralTypes-Definitions.xml"
		System.out.println("Generate GeneralTypes-Definitions.xml(describe the implementation of the application topology) in \"Definitions\" directory......");
		try {
			GenerateGeneralTypesDefinitionFile.generateTypesDefinitionFile();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Done generating \"GeneralTypes-Definitions.xml\""+ "\n");
		
		//Generate "files" directory
		System.out.println("Generate \"files\"(store the application/runtime/service artifacts, etc) directory for the application");
		GenerateFilesDir.generateFilesDir();
		System.out.println("Done generating \"files\" directory"+ "\n");
		
		//Zip all the CSAR files in the "CSAR" directory and copy to the "output/CSAR.zip"
		System.out.println("Compress the generated directories to a CSAR file");
		//String class_path = GenerateCSAR.class.getResource("").getPath();
		File f = new File("CSAR");
//		ZipCompress.zip(f.getAbsolutePath() , "output"+ File.separator + "CSAR.zip");
		String csar_location = args[0];
		//System.out.println("csar location is: " +csar_location);
		CompressUtil.zip(f.getAbsolutePath() , csar_location, true, "");
		
		System.out.println("Finished building TOSCA model for the application"+ "\n");
		//File f1 = new File("output"+ File.separator + "CSAR.zip");
		File csar_dir = new File(csar_location);
		System.out.println("CSAR has been generated to path: " + csar_dir.getAbsolutePath());
		

	}

}
