package com.ibm.cloud.migration.cmd.tool.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class FileInsertRow {
//    public static void main(String args[]) {
//      try {
//        FileInsertRow j = new FileInsertRow();
//        j.insertStringInFile(new File("auto-config.js"), 1, "require auto-config.js");
//      } catch (Exception e) {
//        e.printStackTrace();
//      }
//    }
    /**
     * Insert one line into the assigned line number
     * 
     * @param inFile
     *          
     * @param lineno
     *          
     * @param lineToBeInserted
     *          
     * @throws Exception
     *           
     */
    public static void insertStringInFile(File inFile, int lineno, String lineToBeInserted)
        throws Exception {
      // tmp file
      File outFile = File.createTempFile("name", ".tmp");
      
      FileInputStream fis = new FileInputStream(inFile);
      BufferedReader in = new BufferedReader(new InputStreamReader(fis));
     
      FileOutputStream fos = new FileOutputStream(outFile);
      PrintWriter out = new PrintWriter(fos);
      String thisLine;
      int i = 1;
      while ((thisLine = in.readLine()) != null) {
        if (i == lineno) {
          out.println(lineToBeInserted);
        }
        out.println(thisLine);
        i++;
      }
      out.flush();
      out.close();
      in.close();
      // delete origin file
      inFile.delete();
      // rename the tmp file to the original name
      outFile.renameTo(inFile);
    }
  }