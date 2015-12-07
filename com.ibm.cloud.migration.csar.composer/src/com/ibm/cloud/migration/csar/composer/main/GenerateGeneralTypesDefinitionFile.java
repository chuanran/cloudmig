package com.ibm.cloud.migration.csar.composer.main;

import java.io.IOException;

import com.ibm.cloud.migration.csar.composer.constants.TOSCATemplateConstants;
import com.ibm.cloud.migration.csar.composer.template.generaltypes.definition.GenerateGeneralTypesDefinition;
import com.ibm.cloud.migration.csar.composer.utility.WriteStringToFile;

public class GenerateGeneralTypesDefinitionFile {
	public static void generateTypesDefinitionFile() throws Exception{
		
		String general_types_definition_file_path = "CSAR" + "/" + "Definitions" + "/" + "GeneralTypes-Definitions.xml";
		//String general_types_definition_file_path = GenerateGeneralDefinitionFile.class.getResource("").getPath() + "CSAR" + "/" + "Definitions" + "/" + "GeneralTypes-Definitions.xml";
		
		String general_types_definition_diagram_file_path = "CSAR" + "/" + "Definitions" + "/" + "GeneralTypes-Diagram.xml";
		//String general_types_definition_diagram_file_path = GenerateGeneralDefinitionFile.class.getResource("").getPath() + "CSAR" + "/" + "Definitions" + "/" + "GeneralTypes-Diagram.xml";
		
		//Write General definition to General-Definitions.xml file
		GenerateGeneralTypesDefinition.generateGeneralTypesDefinitionFile();
		String general_types_definition_file = GenerateGeneralTypesDefinition.getProperty(TOSCATemplateConstants.TYPES_DEFINITION_XML_FILE);
		//System.out.println("general_types_definition_file is: " + general_types_definition_file);
		
		String general_types_definition_diagram_file = GenerateGeneralTypesDefinition.getProperty(TOSCATemplateConstants.TYPES_DIAGRAM_DEFINITION_XML_FILE);
		//System.out.println("general_types_definition_diagram_file is: " + general_types_definition_diagram_file);
		try {
			WriteStringToFile.writeStrToFile(general_types_definition_file, general_types_definition_file_path);
			WriteStringToFile.writeStrToFile(general_types_definition_diagram_file, general_types_definition_diagram_file_path);
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args){
//		GenerateGeneralTypesDefinitionFile.generateTypesDefinitionFile();
		
//		UUID uuid = UUID.randomUUID();      
//        System.out.println(uuid.toString());   
//        
//        UUID uuid1 = UUID.randomUUID();      
//        System.out.println(uuid1.toString());   
		
	}
}
