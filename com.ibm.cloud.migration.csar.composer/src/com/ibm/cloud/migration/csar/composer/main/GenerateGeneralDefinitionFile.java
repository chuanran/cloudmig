package com.ibm.cloud.migration.csar.composer.main;

import java.io.IOException;

import com.ibm.cloud.migration.csar.composer.constants.TOSCATemplateConstants;
import com.ibm.cloud.migration.csar.composer.template.general.definitions.*;
import com.ibm.cloud.migration.csar.composer.utility.*;

public class GenerateGeneralDefinitionFile {
	
	//private static final String Calendar = null;

	public static void generateDefinitionFile() throws Exception{
		
		String general_definition_file_path = "CSAR" + "/" + "Definitions" + "/" + "General-Definitions.xml";
		//String general_definition_file_path = GenerateGeneralDefinitionFile.class.getResource("").getPath() + "CSAR" + "/" + "Definitions" + "/" + "General-Definitions.xml";
		
		String general_definition_diagram_file_path = "CSAR" + "/" + "Definitions" + "/" + "General-Diagram.xml";
		//String general_definition_diagram_file_path = GenerateGeneralDefinitionFile.class.getResource("").getPath() + "CSAR" + "/" + "Definitions" + "/" + "General-Diagram.xml";
		
		//Write General definition to General-Definitions.xml file
		try {
			GenerateGeneralDefinition.generateGeneralDefinitionFile();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String general_definition_file = GenerateGeneralDefinition.getProperty(TOSCATemplateConstants.GENERAL_DEFINITION_XML_FILE_KEY);
		//System.out.println("general_definition_file is: " + general_definition_file);
		String general_definition_diagram_file = GenerateGeneralDefinition.getProperty(TOSCATemplateConstants.GENERAL_DEFINITION_DIAGRAM_XML_FILE_KEY);
		//System.out.println("general_definition_diagram_file is: " + general_definition_diagram_file);
		try {
			WriteStringToFile.writeStrToFile(general_definition_file, general_definition_file_path);
			WriteStringToFile.writeStrToFile(general_definition_diagram_file, general_definition_diagram_file_path);
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args){
//		GenerateGeneralDefinitionFile.generateDefinitionFile();
		
//		UUID uuid = UUID.randomUUID();      
//        System.out.println(uuid.toString());   
//        
//        UUID uuid1 = UUID.randomUUID();      
//        System.out.println(uuid1.toString());   
		
	}
}
