package com.ibm.cloud.migration.csar.composer.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
 
public class ZipCompress
{
    List<String> fileList;
//    private static final String OUTPUT_ZIP_FILE = "E:/material/cloudmig/com.ibm.cloud.migration.csar.composer/output/Hello.zip";
//    private static final String SOURCE_FOLDER = "E:/material/cloudmig/com.ibm.cloud.migration.csar.composer/output/test";
 
    ZipCompress(){
	fileList = new ArrayList<String>();
    }
 
//    public static void main( String[] args )
//    {
//    	ZipCompress appZip = new ZipCompress();
//    	appZip.generateFileList(new File(SOURCE_FOLDER));
//    	appZip.zipIt(OUTPUT_ZIP_FILE);
//    }
    
    public static void zip(String source_folder, String target_zip_path){
    	ZipCompress appZip = new ZipCompress();
    	
    	appZip.generateFileList(new File(source_folder), source_folder);
    	appZip.zipIt(target_zip_path, source_folder);
    }
 
    /**
     * Zip it
     * @param zipFile output ZIP file location
     */
    public void zipIt(String zipFile, String source_folder){
 
     byte[] buffer = new byte[1024];
 
     try{
 
    	FileOutputStream fos = new FileOutputStream(zipFile);
    	ZipOutputStream zos = new ZipOutputStream(fos);
 
    	System.out.println("Output to Zip : " + zipFile);
 
    	for(String file : this.fileList){
 
    		System.out.println("File Added : " + file);
    		ZipEntry ze= new ZipEntry(file);
        	zos.putNextEntry(ze);
 
        	FileInputStream in = 
                       new FileInputStream(source_folder + File.separator + file);
 
        	int len;
        	while ((len = in.read(buffer)) > 0) {
        		zos.write(buffer, 0, len);
        	}
 
        	in.close();
    	}
 
    	zos.closeEntry();
    	//remember close it
    	zos.close();
 
    	System.out.println("Done");
    }catch(IOException ex){
       ex.printStackTrace();   
    }
   }
 
    /**
     * Traverse a directory and get all files,
     * and add the file into fileList  
     * @param node file or directory
     */
    public void generateFileList(File node, String source_folder){
 
    	//add file only
	if(node.isFile()){
		fileList.add(generateZipEntry(node.getAbsoluteFile().toString(), source_folder));
	}
 
	if(node.isDirectory()){
		String[] subNote = node.list();
		for(String filename : subNote){
			generateFileList(new File(node, filename), source_folder);
		}
	}
 
    }
 
    /**
     * Format the file path for zip
     * @param file file path
     * @return Formatted file path
     */
    private String generateZipEntry(String file, String source_folder){
    	return file.substring(source_folder.length()+1, file.length());
    }
}
