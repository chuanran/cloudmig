package com.ibm.cloud.migration.csar.composer.utility;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
 

//Create for ICAP integration
/** 
 * ZIP utility for compressing and uncompressing
 */  
public class CompressUtil {  
      
    /** 
     * Uncompress the zip file to the assigned directory
     * If directory doesn't exist, it can be created automatically. Illegal path will cause exception throwing
     */  
    public static File [] unzip(String zip, String dest, String passwd) throws ZipException {  
        File zipFile = new File(zip);  
        return unzip(zipFile, dest, passwd);  
    }  
      
    /** 
     Uncompress the zip file to the current directory
     */  
    public static File [] unzip(String zip, String passwd) throws ZipException {  
        File zipFile = new File(zip);  
        File parentDir = zipFile.getParentFile();  
        return unzip(zipFile, parentDir.getAbsolutePath(), passwd);  
    }  
      
    /** 
      Uncompress the assigned zip file name to the assigned directory
     */  
    public static File [] unzip(File zipFile, String dest, String passwd) throws ZipException {  
        ZipFile zFile = new ZipFile(zipFile);  
        zFile.setFileNameCharset("GBK");  
        if (!zFile.isValidZipFile()) {  
            throw new ZipException("Zip file is not illegal, and may be damaged");  
        }  
        File destDir = new File(dest);  
        if (destDir.isDirectory() && !destDir.exists()) {  
            destDir.mkdir();  
        }  
        if (zFile.isEncrypted()) {  
            zFile.setPassword(passwd.toCharArray());  
        }  
        zFile.extractAll(dest);  
          
        List<FileHeader> headerList = zFile.getFileHeaders();  
        List<File> extractedFileList = new ArrayList<File>();  
        for(FileHeader fileHeader : headerList) {  
            if (!fileHeader.isDirectory()) {  
                extractedFileList.add(new File(destDir,fileHeader.getFileName()));  
            }  
        }  
        File [] extractedFiles = new File[extractedFileList.size()];  
        extractedFileList.toArray(extractedFiles);  
        return extractedFiles;  
    }  
      
    /** 
     * Compress the source file to the current path
     */  
    public static String zip(String src) {  
        return zip(src,null);  
    }  
      
    /** 
     * Compress the source file to the current path with assigned password
     */  
    public static String zip(String src, String passwd) {  
        return zip(src, null, passwd);  
    }  
      
    /** 
     *Compress the source file to the assigned path with assigned password
     */  
    public static String zip(String src, String dest, String passwd) {  
        return zip(src, dest, true, passwd);  
    }  
      
    /** 
     * Compress the source file to the assigned path with assigned password
     */  
    public static String zip(String src, String dest, boolean isCreateDir, String passwd) {  
        File srcFile = new File(src);  
        dest = buildDestinationZipFilePath(srcFile, dest);  
        ZipParameters parameters = new ZipParameters();  
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);           
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        try {  
            ZipFile zipFile = new ZipFile(dest);  
            if (srcFile.isDirectory()) {  
                // If don't create directory, will directly compress the file under the assigned directory without additional directory structure
            	File [] subFiles = srcFile.listFiles();
            	if (!isCreateDir) {  
                      
                    ArrayList<File> temp = new ArrayList<File>();  
                    Collections.addAll(temp, subFiles);  
                    zipFile.addFiles(temp, parameters);  
                    return dest;  
                }
                for(int i = 0;i< subFiles.length;i++)
                {
                	if(subFiles[i].isFile())
                	{
                		zipFile.addFile(subFiles[i], parameters);
                	}else
                	{
                		zipFile.addFolder(subFiles[i], parameters);
                	}
                }
            } else {  
                zipFile.addFile(srcFile, parameters);  
            }  
            return dest;  
        } catch (ZipException e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
      
    /** 
     *create the zip file path
     */  
    private static String buildDestinationZipFilePath(File srcFile,String destParam) {  
            createDestDirectoryIfNecessary(destParam);
            
            String fileName = "";  
            if (srcFile.isDirectory()) {  
                fileName = srcFile.getName();  
            } else {  
                fileName = srcFile.getName().substring(0, srcFile.getName().lastIndexOf("."));  
            }  
            
            if (destParam.endsWith(File.separator)) {  
            	destParam += fileName + ".zip";
            } else{
            	destParam += File.separator + fileName + ".zip";
            }
       // }  
        return destParam;  
    }  
      
    /** 
     * Create the directory for Zip file if necessary
     */  
    private static void createDestDirectoryIfNecessary(String destParam) {  
        File destDir = null;  
        destDir = new File(destParam);
//        if (destParam.endsWith(File.separator)) {  
//            destDir = new File(destParam);  
//        } else {  
//            destDir = new File(destParam.substring(0, destParam.lastIndexOf(File.separator)));  
//        }  
        if (!destDir.exists()) {  
            destDir.mkdirs();  
        }  
    }  
  
//    public static void main(String[] args) {  
//        zip("C:\\runtime-EclipseApplication\\temp", "C:\\runtime-EclipseApplication\\demo_test20130428\\demo_test.zip",true, "");  
//    }  
}  
